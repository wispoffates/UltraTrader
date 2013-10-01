package com.thedemgel.cititradersre.shop;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import com.thedemgel.cititradersre.CitiTrader;
import com.thedemgel.cititradersre.InventoryHandler;
import com.thedemgel.cititradersre.StoreConfig;
import com.thedemgel.cititradersre.util.YamlFilenameFilter;
import java.io.File;
import java.io.FilenameFilter;
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
	private final InventoryHandler inventoryHandler;

	public ShopHandler(CitiTrader instance) {
		plugin = instance;
		inventoryHandler = new InventoryHandler(plugin);
	}

	public void initShops() {
		File storedir = new File(plugin.getDataFolder() + "/stores/");
		File[] files = storedir.listFiles(new YamlFilenameFilter());

		for (File file : files) {
			StoreConfig config = new StoreConfig(plugin, file);
			Shop shop = new Shop(config);
			shops.put(shop.getId(), shop);
			System.out.println("Initialized shop " + shop.getName() + "(" + shop.getId() + ")");
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

	public void createShop(Player player) {
	}
}
