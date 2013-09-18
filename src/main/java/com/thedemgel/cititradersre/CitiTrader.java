
package com.thedemgel.cititradersre;

import java.util.logging.Level;
import org.bukkit.plugin.java.JavaPlugin;

public class CitiTrader extends JavaPlugin {
	@Override
	public void onEnable() {
		this.getLogger().log(Level.INFO, "CitiTraders Enabled...");
	}
	
	@Override
	public void onDisable() {
		this.getLogger().log(Level.INFO, "CitiTraders Disabled...");
	}
}
