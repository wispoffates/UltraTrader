
package com.thedemgel.cititradersre.shop;

import com.thedemgel.cititradersre.CitiTrader;
import com.thedemgel.cititradersre.StoreConfig;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.configuration.ConfigurationSection;


public class ShopHandler {
	private final CitiTrader plugin;
	private Map<Integer, Shop> shops = new ConcurrentHashMap<>();
	private StoreConfig config;
	
	public ShopHandler(CitiTrader instance) {
		plugin = instance;
		config = plugin.getStoreConfig();
	}
	
	public void initShops() {
		ConfigurationSection shopsConfig = config.getConfig().getConfigurationSection("shops");
		
		for (String shopid : shopsConfig.getKeys(false)) {
			ConfigurationSection shop = shopsConfig.getConfigurationSection(shopid);
			System.out.println("Initializing shop " + shop.getString("name") + "(" + shopid + ")");
			shops.put(Integer.valueOf(shopid), new Shop(shop));
		}
	}
	
	public Shop getShop(Integer shopId) {
		return shops.get(shopId);
	}
}
