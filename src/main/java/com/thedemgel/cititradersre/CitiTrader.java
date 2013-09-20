
package com.thedemgel.cititradersre;

import com.thedemgel.cititradersre.command.commands.ShopCommands;
import com.thedemgel.cititradersre.shop.ShopHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class CitiTrader extends JavaPlugin {
	private StoreConfig shopsConfig;
	private ShopHandler shopHandler;

	@Override
	public void onEnable() {
		shopsConfig = new StoreConfig(this, "shops.yml");
		shopHandler = new ShopHandler(this);
		
		shopHandler.initShops();
		
		getCommand("trader").setExecutor(new ShopCommands(this));
		getServer().getPluginManager().registerEvents(new ShopListener(this), this);
		this.getLogger().log(Level.INFO, "CitiTraders Enabled...");
	}
	
	@Override
	public void onDisable() {
		shopsConfig.saveConfig();
		this.getLogger().log(Level.INFO, "CitiTraders Disabled...");
	}
	
	public StoreConfig getStoreConfig() {
		return shopsConfig;
	}
	
	public ShopHandler getStoreHandler() {
		return shopHandler;
	}
	
	/// TESTING
	private List<ItemStack> items = new ArrayList<>();
	
	public List<ItemStack> test() {
		for (int i=0;i<10;i++) {
			ItemStack item = new ItemStack(Material.WOOD, 25, (short)((int)i/3));
			items.add(item);
		}
		
		shopsConfig.getConfig().set("items", items);
		shopsConfig.saveConfig();
		return items;
	}
}
