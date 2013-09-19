
package com.thedemgel.cititradersre;

import com.thedemgel.cititradersre.command.commands.ShopCommands;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class CitiTrader extends JavaPlugin {
	private StoreConfig shopsConfig;

	@Override
	public void onEnable() {
		shopsConfig = new StoreConfig(this, "shops.yml");
		getCommand("trader").setExecutor(new ShopCommands(this));
		getServer().getPluginManager().registerEvents(new ShopListener(), this);
		this.getLogger().log(Level.INFO, "CitiTraders Enabled...");
		
		test();
	}
	
	@Override
	public void onDisable() {
		shopsConfig.saveConfig();
		this.getLogger().log(Level.INFO, "CitiTraders Disabled...");
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
