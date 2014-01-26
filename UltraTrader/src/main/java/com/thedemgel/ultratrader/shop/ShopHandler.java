package com.thedemgel.ultratrader.shop;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.thedemgel.ultratrader.*;
import com.thedemgel.ultratrader.inventory.InventoryInterfaceHandler;
import com.thedemgel.ultratrader.util.Permissions;
import com.thedemgel.ultratrader.wallet.WalletHandler;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.economy.EconomyResponse.ResponseType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ShopHandler {

	public static final int SHOP_NULL = -1;
	private final UltraTrader plugin;
	private Map<Integer, Shop> shops = new ConcurrentHashMap<>();
	private final InventoryHandler inventoryHandler;

	public ShopHandler(UltraTrader instance) {
		plugin = instance;
		inventoryHandler = new InventoryHandler(plugin);
	}

	public void addShop(Shop shop) {
		shops.put(shop.getId(), shop);
        shop.setMetaData();
	}

	public void initShops() {
		UltraTrader.getDbObj().initShops();
	}

	public void saveShops(boolean async) {
		for (Shop shop : shops.values()) {
			shop.save(async);
		}
	}

	public InventoryHandler getInventoryHandler() {
		return inventoryHandler;
	}

	public Shop getShop(Integer shopId) {
		return shops.get(shopId);
	}

	public Collection<Shop> getShopsByOwner(final Player player) {
		Predicate<Shop> owner = new Predicate<Shop>() {
			@Override
			public boolean apply(Shop shop) {
				return (shop.getOwner() == null ? player.getName() == null : shop.getOwner().equals(player.getName()));
			}
		};

		Collection<Shop> ownedShops = Collections2.filter(shops.values(), owner);

		return ownedShops;
	}

	public Shop createShop(Player player) {
        if (!player.hasPermission(Permissions.CREATE_STORES)) {
            player.sendRawMessage(ChatColor.RED + "You don't have permission to create Stores.");
            return null;
        }

		if (!LimitHandler.canCreate(player)) {
			player.sendRawMessage(ChatColor.RED + "You already have to many shops.");
			return null;
		}

		// Check to be sure the player has the money to create the shop
		double cost = LimitHandler.getCreateCost(player);
		boolean has = UltraTrader.getEconomy().has(player.getName(), player.getWorld().getName(), cost);
		if (!has) {
			player.sendRawMessage(ChatColor.RED + "Not enough funds.");
			return null;
		}

		Random rnd = new Random();
		int increment = UltraTrader.STORE_ID_RAND_INCREMENT;
		boolean found = true;
		int i = 0;
		int randid = -1;
		File tempConfig = null;
		while (found) {
			int seed = UltraTrader.STORE_ID_RAND_BASE + (i * increment);
			randid = rnd.nextInt(seed);
			tempConfig = new File(plugin.getDataFolder() + File.separator + UltraTrader.STORE_DIR + File.separator + randid + ".yml");
			if (!tempConfig.exists()) {
				found = false;
			}
			i++;
		}

		if (tempConfig == null || randid == -1) {
			return null;
		}

		// Withdraw the money if successfully created
		EconomyResponse response = UltraTrader.getEconomy().withdrawPlayer(player.getName(), player.getWorld().getName(), cost);
		if (response.type.equals(ResponseType.SUCCESS)) {
			player.sendRawMessage(cost + " withdrawn from account");
		} else {
			player.sendRawMessage("Could not withdraw " + cost);
			return null;
		}

		//StoreConfig tempShopConfig = new StoreConfig(plugin, tempConfig);
		Shop tempShop = new Shop();
		tempShop.setId(randid);
		tempShop.setOwner(player);
		tempShop.setName(L.getString("general.newshopname"));
		tempShop.setWalletType(WalletHandler.DEFAULT_WALLET_TYPE);
		tempShop.setInventoryInterfaceType(InventoryInterfaceHandler.DEFAULT_INVENTORY_TYPE);
		tempShop.setLevel(LimitHandler.getLevelAtCreate(player));

        // Add default category
        CategoryItem categoryItem = new CategoryItem(new ItemStack(Material.APPLE));
        categoryItem.setDisplayName("Default Category");
        categoryItem.setCategoryId("default");
        List<String> lore = new ArrayList<>();
        lore.add("This is a default Category");
        categoryItem.setLore(lore);

        tempShop.getCategoryItem().put(categoryItem.getCategoryId(), categoryItem);

		tempShop.save(true);

		shops.put(tempShop.getId(), tempShop);
		return tempShop;
	}

    public void deleteShop(int shopId) {
        Shop shop = getShop(shopId);

        if (shop == null) {
            return;
        }

        deleteShop(shop);
    }

    public void deleteShop(Shop shop) {
        if (shops.containsKey(shop.getId())) {
            shops.remove(shop.getId());
        }

        UltraTrader.getDbObj().removeShopFile(shop.getId());
    }

    public Map<Integer, Shop> getShops() {
        return shops;
    }
}
