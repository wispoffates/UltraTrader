package com.thedemgel.cititradersre.shop;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.thedemgel.cititradersre.CitiTrader;
import com.thedemgel.cititradersre.InventoryHandler;
import com.thedemgel.cititradersre.L;
import com.thedemgel.cititradersre.StoreConfig;
import com.thedemgel.cititradersre.inventory.InventoryInterfaceHandler;
import com.thedemgel.cititradersre.wallet.WalletHandler;
import java.io.File;
import java.util.Collection;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.entity.Player;

public class ShopHandler {

	public static final int SHOP_NULL = -1;
	private final CitiTrader plugin;
	private Map<Integer, Shop> shops = new ConcurrentHashMap<>();
	private final InventoryHandler inventoryHandler;

	public ShopHandler(CitiTrader instance) {
		plugin = instance;
		inventoryHandler = new InventoryHandler(plugin);
	}

	public void addShop(Shop shop) {
		shops.put(shop.getId(), shop);
	}

	public void initShops() {
		CitiTrader.getDbObj().initShops();
	}

	public void saveShops() {
		for (Shop shop : shops.values()) {
			shop.save();
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
		Random rnd = new Random();
		int increment = CitiTrader.STORE_ID_RAND_INCREMENT;
		boolean found = true;
		int i = 0;
		int randid = -1;
		File tempConfig = null;
		while (found) {
			int seed = CitiTrader.STORE_ID_RAND_BASE + (i * increment);
			randid = rnd.nextInt(seed);
			tempConfig = new File(plugin.getDataFolder() + File.separator + CitiTrader.STORE_DIR + File.separator + randid + ".yml");
			if (!tempConfig.exists()) {
				found = false;
			}
			i++;
		}

		if (tempConfig == null || randid == -1) {
			return null;
		}

		StoreConfig tempShopConfig = new StoreConfig(plugin, tempConfig);
		Shop tempShop = new Shop(tempShopConfig);
		tempShop.setId(randid);
		tempShop.setOwner(player);
		tempShop.setName(L.getString("general.newshopname"));
		tempShop.setWalletType(WalletHandler.DEFAULT_WALLET_TYPE);
		tempShop.setInventoryInterfaceType(InventoryInterfaceHandler.DEFAULT_INVENTORY_TYPE);
		tempShop.save();

		shops.put(tempShop.getId(), tempShop);
		return tempShop;
	}
}
