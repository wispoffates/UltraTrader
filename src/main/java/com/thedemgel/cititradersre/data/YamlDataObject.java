package com.thedemgel.cititradersre.data;

import com.thedemgel.cititradersre.CitiTrader;
import com.thedemgel.cititradersre.L;
import com.thedemgel.cititradersre.StoreConfig;
import com.thedemgel.cititradersre.shop.ItemPrice;
import com.thedemgel.cititradersre.shop.Shop;
import com.thedemgel.cititradersre.util.ConfigValue;
import com.thedemgel.cititradersre.util.YamlFilenameFilter;
import com.thedemgel.cititradersre.wallet.WalletHandler;
import java.io.File;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

public class YamlDataObject extends DataObject {

	private File STORE_DIR = new File(CitiTrader.getInstance().getDataFolder() + File.separator + CitiTrader.STORE_DIR + File.separator);

	@Override
	public void save(Shop shop) {
		getPool().execute(new SaveShop(shop));
	}

	@Override
	public void load(int shopid) {
		File loadfile = new File(STORE_DIR, shopid + ".yml");
		StoreConfig config = new StoreConfig(CitiTrader.getInstance(), loadfile);

		Shop shop = new Shop(config);

		// Find or create Inventory Section
		ConfigurationSection invconfig = config.getConfig().getConfigurationSection("inventory");
		if (invconfig == null) {
			invconfig = config.getConfig().createSection("inventory");
		}

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
			shop.getSellprices().put(item.getId(), item);
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
		
		//WalletType walletType = wallettype.getValue(); //WalletType.valueOf(wallettype.getValue());
		
		shop.setWallet(CitiTrader.getWallethandler().getWalletInstance(wallettype.getValue(), shop));
		
		//Load the Info section
		// Find or create info Configuration Section
		ConfigurationSection infoconfig = config.getConfig().getConfigurationSection("info");
		if (infoconfig == null) {
			infoconfig = config.getConfig().createSection("info");
		}
		
		for (String key : infoconfig.getKeys(false)) {
			shop.getInfo().put(key, new ConfigValue(infoconfig.get(key)));
		}
		
		CitiTrader.getStoreHandler().addShop(shop);

		Bukkit.getLogger().log(Level.INFO, L.getFormatString("general.initialized", shop.getName(), shop.getId()));
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

	private class SaveShop implements Runnable {

		private StoreConfig config;
		private Shop shop;

		public SaveShop(Shop shop) {
			config = new StoreConfig(CitiTrader.getInstance(), new File(STORE_DIR, shop.getId() + ".yml"));
			this.shop = shop;
		}

		@Override
		public void run() {
			ConfigurationSection sellconfig = config.getConfig().createSection("sellprices");

			for (ItemPrice ip : shop.getSellprices().values()) {
				sellconfig.set(ip.getId() + ".amount", ip.getAmount());
				sellconfig.set(ip.getId() + ".description", ip.getDescription());
				sellconfig.set(ip.getId() + ".itemStack", ip.getItemStack());
				sellconfig.set(ip.getId() + ".price", ip.getPrice());
				sellconfig.set(ip.getId() + ".random", ip.getId());
			}

			ConfigurationSection buyconfig = config.getConfig().createSection("buyprices");

			for (ItemPrice ip : shop.getBuyprices().values()) {
				buyconfig.set(ip.getId() + ".amount", ip.getAmount());
				buyconfig.set(ip.getId() + ".description", ip.getDescription());
				buyconfig.set(ip.getId() + ".itemStack", ip.getItemStack());
				buyconfig.set(ip.getId() + ".price", ip.getPrice());
				buyconfig.set(ip.getId() + ".random", ip.getId());
			}

			ConfigurationSection invconfig = config.getConfig().getConfigurationSection("inventory");
			if (invconfig == null) {
				invconfig = config.getConfig().createSection("inventory");
			}

			// TODO: Save Inventory TYPE

			ConfigurationSection invconf = invconfig.createSection("items");

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
				ConfigValue<String> defaultname = new ConfigValue(L.getString("general.newshopname"));
				infoconfig.set("name", defaultname);
			}

			for (Entry<String, ConfigValue> info : shop.getInfo().entrySet()) {
				infoconfig.set(info.getKey(), info.getValue().getValue());
			}
			/*for (String key : walletconfig.getKeys(false)) {
			 System.out.println(key);
			 shop.getInfo().put(key, new ConfigValue(walletconfig.get(key)));
			 }

			 for (Map.Entry<String, ConfigValue> i : shop.getInfo().entrySet()) {
			 System.out.println(i.getKey() + " -- " + i.getValue().getValue() + " -- " + i.getValue().getValue().getClass());
			 }*/

			config.saveConfig();
		}
	}
}
