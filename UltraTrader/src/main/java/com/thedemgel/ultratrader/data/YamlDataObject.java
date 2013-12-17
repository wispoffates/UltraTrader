package com.thedemgel.ultratrader.data;

import com.thedemgel.ultratrader.UltraTrader;
import com.thedemgel.ultratrader.L;
import com.thedemgel.ultratrader.StoreConfig;
import com.thedemgel.ultratrader.inventory.InventoryInterfaceHandler;
import com.thedemgel.ultratrader.shop.ItemPrice;
import com.thedemgel.ultratrader.shop.Shop;
import com.thedemgel.ultratrader.util.ConfigValue;
import com.thedemgel.ultratrader.util.ShopAction;
import com.thedemgel.ultratrader.util.YamlFilenameFilter;
import com.thedemgel.ultratrader.wallet.WalletHandler;
import java.io.File;
import java.math.BigDecimal;
import java.util.*;
import java.util.Map.Entry;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class YamlDataObject extends DataObject {

	private File STORE_DIR = new File(UltraTrader.getInstance().getDataFolder() + File.separator + UltraTrader.STORE_DIR + File.separator);

	public YamlDataObject() {
		STORE_DIR.mkdirs();
	}

	@Override
	public void save(Shop shop) {
		getPool().execute(new SaveShop(shop));
	}

	@Override
	public void load(int shopid) {
		File loadfile = new File(STORE_DIR, shopid + ".yml");
		StoreConfig config = new StoreConfig(UltraTrader.getInstance(), loadfile);

		Shop shop = new Shop(config);

		// Find or create Inventory Section
		ConfigurationSection invconfig = config.getConfig().getConfigurationSection("inventory");
		if (invconfig == null) {
			invconfig = config.getConfig().createSection("inventory");
		}

		// Load the Inventory Interface
		// Find or create inventory interface "interface" Configuration Section
		ConfigurationSection interfaceconfig = invconfig.getConfigurationSection("info");
		if (interfaceconfig == null) {
			interfaceconfig = invconfig.createSection("info");
		}

		for (String key : interfaceconfig.getKeys(false)) {
			shop.getInventoryinfo().put(key, new ConfigValue(interfaceconfig.get(key)));
		}

		if (!shop.getInventoryinfo().containsKey("type")) {
			System.out.println("Setting to Default Inventory Interface!!!!!!");
			shop.getInventoryinfo().put("type", new ConfigValue(InventoryInterfaceHandler.DEFAULT_INVENTORY_TYPE));
		}

		ConfigValue<String> inventorytype = shop.getInventoryinfo().get("type");

		shop.setInventoryInterface(UltraTrader.getInventoryInterfaceHandler().getInventoryInterfaceInstance(inventorytype.getValue(), shop));

		// Find or create Inventory Item Section
		ConfigurationSection invitemconfig = invconfig.getConfigurationSection("items");
		if (invitemconfig == null) {
			invitemconfig = invconfig.createSection("items");
		}

		// Load all the items
		for (String num : invitemconfig.getKeys(false)) {
			ItemStack item = invitemconfig.getItemStack(num + ".itemstack");
			Integer amount = invitemconfig.getInt(num + ".amount");
			shop.getInventory().put(item, amount);
		}

		// Load all SellPrices
		// Find or create configuration section
		ConfigurationSection sellconfig = config.getConfig().getConfigurationSection("sellprices");
		if (sellconfig == null) {
			sellconfig = config.getConfig().createSection("sellprices");
		}

		// Load all sell prices
		for (String id : sellconfig.getKeys(false)) {
			ConfigurationSection itemconfig = sellconfig.getConfigurationSection(id);
			ItemPrice item = new ItemPrice();
			item.setAmount(itemconfig.getInt("amount"));
			item.setDescription(itemconfig.getString("description"));
			item.setItemStack(itemconfig.getItemStack("itemStack"));
			item.setPrice(BigDecimal.valueOf(itemconfig.getDouble("price")));
			item.setRandom(itemconfig.getString("random"));
			item.setSlot(itemconfig.getInt("slot"));
			shop.getSellPrices().put(item.getId(), item);
		}

		// Load all BuyPrices
		// Find or create configuration section
		ConfigurationSection buyconfig = config.getConfig().getConfigurationSection("buyprices");
		if (buyconfig == null) {
			buyconfig = config.getConfig().createSection("buyprices");
		}

		// Load all sell prices
		for (String id : buyconfig.getKeys(false)) {
			ConfigurationSection itemconfig = buyconfig.getConfigurationSection(id);
			ItemPrice item = new ItemPrice();
			item.setAmount(itemconfig.getInt("amount"));
			item.setDescription(itemconfig.getString("description"));
			item.setItemStack(itemconfig.getItemStack("itemStack"));
			item.setPrice(BigDecimal.valueOf(itemconfig.getDouble("price")));
			item.setRandom(itemconfig.getString("random"));
			item.setSlot(itemconfig.getInt("slot"));
			shop.getBuyPrices().put(item.getId(), item);
		}

		// Load the Wallet
		// Find or create wallet Configuration Section
		ConfigurationSection walletconfig = config.getConfig().getConfigurationSection("wallet");
		if (walletconfig == null) {
			walletconfig = config.getConfig().createSection("wallet");
		}

		for (String key : walletconfig.getKeys(false)) {
			shop.getWalletinfo().put(key, new ConfigValue(walletconfig.get(key)));
		}

		if (!shop.getWalletinfo().containsKey("type")) {
			shop.getWalletinfo().put("type", new ConfigValue(WalletHandler.DEFAULT_WALLET_TYPE));
		}

		ConfigValue<String> wallettype = shop.getWalletinfo().get("type");

		shop.setWallet(UltraTrader.getWallethandler().getWalletInstance(wallettype.getValue(), shop));

		//Load the Info section
		// Find or create info Configuration Section
		ConfigurationSection infoconfig = config.getConfig().getConfigurationSection("info");
		if (infoconfig == null) {
			infoconfig = config.getConfig().createSection("info");
		}

		for (String key : infoconfig.getKeys(false)) {
			shop.getInfo().put(key, new ConfigValue(infoconfig.get(key)));
		}

        ConfigurationSection blockConfig = config.getConfig().getConfigurationSection("blocks");
        if (blockConfig == null) {
            blockConfig = config.getConfig().createSection("blocks");
        }

        //blockConfig.set("blocklocation", shop.getBlockShops());
        //if (blockConfig.contains("blocklocation")) {
            ConfigurationSection blockLoc = blockConfig.getConfigurationSection("blocklocation");

        if (blockLoc == null) {
            blockLoc = blockConfig.createSection("blocklocation");
        }

        Set<String> keys = blockLoc.getKeys(false);

        for (String key : keys) {
            int x = blockLoc.getInt(key + ".x");
            int y = blockLoc.getInt(key + ".y");
            int z = blockLoc.getInt(key + ".z");
            String world = blockLoc.getString(key + ".world");

            World world1 = Bukkit.getWorld(world);
            Location loc = new Location(world1, x, y, z);

            shop.getBlockShops().add(loc);
        }

		UltraTrader.getStoreHandler().addShop(shop);

		//Bukkit.getLogger().log(Level.INFO, L.getFormatString("general.initialized", shop.getName(), shop.getId()));
	}

	@Override
	public void initShops() {
		for (int id : getShopIds()) {
			load(id);
		}
	}

	public List<Integer> getShopIds() {
		List<Integer> ids = new ArrayList<>();
		File[] files = STORE_DIR.listFiles(new YamlFilenameFilter());

		for (File file : files) {
			String id = file.getName().replace(".yml", "");
			ids.add(Integer.parseInt(id));
		}

		return ids;
	}

	@Override
	public void initLogger(UltraTrader plugin) {
	}

	@Override
	public void doLog(Shop shop, Player player, EconomyResponse resp, ShopAction action, String message) {
	}

	private class SaveShop implements Runnable {

		private StoreConfig config;
		private Shop shop;

		public SaveShop(Shop shop) {
			config = new StoreConfig(UltraTrader.getInstance(), new File(STORE_DIR, shop.getId() + ".yml"));
			this.shop = shop;
		}

		@Override
		public void run() {
			ConfigurationSection sellConfig = config.getConfig().createSection("sellprices");

			for (ItemPrice ip : shop.getSellPrices().values()) {
				sellConfig.set(ip.getId() + ".amount", ip.getAmount());
				sellConfig.set(ip.getId() + ".description", ip.getDescription());
				sellConfig.set(ip.getId() + ".itemStack", ip.getItemStack());
				sellConfig.set(ip.getId() + ".price", ip.getPrice());
				sellConfig.set(ip.getId() + ".random", ip.getId());
				sellConfig.set(ip.getId() + ".slot", ip.getSlot());
			}

			ConfigurationSection buyConfig = config.getConfig().createSection("buyprices");

			for (ItemPrice ip : shop.getBuyPrices().values()) {
				buyConfig.set(ip.getId() + ".amount", ip.getAmount());
				buyConfig.set(ip.getId() + ".description", ip.getDescription());
				buyConfig.set(ip.getId() + ".itemStack", ip.getItemStack());
				buyConfig.set(ip.getId() + ".price", ip.getPrice());
				buyConfig.set(ip.getId() + ".random", ip.getId());
				buyConfig.set(ip.getId() + ".slot", ip.getSlot());
			}

			ConfigurationSection invConfig = config.getConfig().getConfigurationSection("inventory");
			if (invConfig == null) {
				invConfig = config.getConfig().createSection("inventory");
			}

			// Save Inventory INFO
			ConfigurationSection inventoryInfoConfig = invConfig.getConfigurationSection("info");
			if (inventoryInfoConfig == null) {
				inventoryInfoConfig = invConfig.createSection("info");
				ConfigValue<String> defaultinventory = new ConfigValue(InventoryInterfaceHandler.DEFAULT_INVENTORY_TYPE);
				inventoryInfoConfig.set("type", defaultinventory.getValue());
			}

			for (Entry<String, ConfigValue> inventoryinfo : shop.getInventoryinfo().entrySet()) {
				inventoryInfoConfig.set(inventoryinfo.getKey(), inventoryinfo.getValue().getValue());
			}

			// Save Inventory items
			ConfigurationSection invconf = invConfig.createSection("items");

			Integer count = 0;

			for (Map.Entry<ItemStack, Integer> entry : shop.getInventory().entrySet()) {
				invconf.set(count.toString() + ".itemstack", entry.getKey());
				invconf.set(count.toString() + ".amount", entry.getValue());
				count++;
			}

			// Get wallet information
			ConfigurationSection walletconfig = config.getConfig().getConfigurationSection("wallet");
			if (walletconfig == null) {
				walletconfig = config.getConfig().createSection("wallet");
				walletconfig.set("type", WalletHandler.DEFAULT_WALLET_TYPE);
			}

			for (Entry<String, ConfigValue> walletinfo : shop.getWalletinfo().entrySet()) {
				walletconfig.set(walletinfo.getKey(), walletinfo.getValue().getValue());
			}

			ConfigurationSection infoconfig = config.getConfig().getConfigurationSection("info");
			if (infoconfig == null) {
				infoconfig = config.getConfig().createSection("info");
				//ConfigValue<String> defaultname = new ConfigValue(L.getString("general.newshopname"));
				infoconfig.set("name", new ConfigValue(L.getString("general.newshopname")));
			}

			for (Entry<String, ConfigValue> info : shop.getInfo().entrySet()) {
				infoconfig.set(info.getKey(), info.getValue().getValue());
			}

            // TODO: get Block location info
			ConfigurationSection blockConfig = config.getConfig().getConfigurationSection("blocks");
            if (blockConfig == null) {
                blockConfig = config.getConfig().createSection("blocks");
            }

            ConfigurationSection blocks = blockConfig.createSection("blocklocation");

            int i = 0;
            for (Location loc : shop.getBlockShops()) {
                blocks.set(i + ".world", loc.getWorld().getName());
                blocks.set(i + ".x", loc.getBlockX());
                blocks.set(i + ".y", loc.getBlockY());
                blocks.set(i + ".z", loc.getBlockZ());
                i++;
            }

			config.saveConfig();
		}
	}
}
