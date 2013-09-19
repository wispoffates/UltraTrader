
package com.thedemgel.cititradersre;

import com.thedemgel.cititradersre.command.commands.ShopCommands;
import java.util.logging.Level;
import org.bukkit.plugin.java.JavaPlugin;

public class CitiTrader extends JavaPlugin {
	@Override
	public void onEnable() {
		getCommand("trader").setExecutor(new ShopCommands(this));
		getServer().getPluginManager().registerEvents(new ShopListener(), this);
		this.getLogger().log(Level.INFO, "CitiTraders Enabled...");
	}
	
	@Override
	public void onDisable() {
		this.getLogger().log(Level.INFO, "CitiTraders Disabled...");
	}
}
