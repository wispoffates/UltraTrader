
package com.thedemgel.cititradersre.shop;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import com.thedemgel.cititradersre.CitiTrader;
import com.thedemgel.cititradersre.InventoryHandler;
import com.thedemgel.cititradersre.StoreConfig;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;


public class ShopHandler {

	
	private final CitiTrader plugin;
	private Map<Integer, Shop> shops = new ConcurrentHashMap<>();
	private StoreConfig config;
	private final InventoryHandler inventoryHandler;
	
	public ShopHandler(CitiTrader instance) {
		plugin = instance;
		config = plugin.getStoreConfig();
		inventoryHandler = new InventoryHandler(plugin);
	}
	
	public void initShops() {
		ConfigurationSection shopsConfig = config.getConfig().getConfigurationSection("shops");
		
		for (String shopid : shopsConfig.getKeys(false)) {
			ConfigurationSection shop = shopsConfig.getConfigurationSection(shopid);
			System.out.println("Initializing shop " + shop.getString("name") + "(" + shopid + ")");
			shops.put(Integer.valueOf(shopid), new Shop(shop, plugin));
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
}
