package com.thedemgel.ultratrader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class StoreConfig {

	private final JavaPlugin plugin;
	private File configFile;
	private FileConfiguration fileConfiguration;
	
	public StoreConfig(JavaPlugin plugin, File file) {
		this.plugin = plugin;
		
		this.configFile = file;
	}
	
	public StoreConfig(JavaPlugin plugin, String fileName) {
		if (plugin == null) {
			throw new IllegalArgumentException("plugin cannot be null");
		}
		if (!plugin.isInitialized()) {
			throw new IllegalArgumentException("plugin must be initiaized");
		}
		this.plugin = plugin;
		File dataFolder = plugin.getDataFolder();
		if (dataFolder == null) {
			throw new IllegalStateException();
		}
		this.configFile = new File(plugin.getDataFolder(), fileName);
	}

	public void reloadConfig() {
		fileConfiguration = YamlConfiguration.loadConfiguration(configFile);
	}

	public FileConfiguration getConfig() {
		if (fileConfiguration == null) {
			this.reloadConfig();
		}
		return fileConfiguration;
	}

	public void saveConfig() {
		if (fileConfiguration != null && configFile != null) {
			try {
				getConfig().save(configFile);
			} catch (IOException ex) {
				plugin.getLogger().log(Level.SEVERE, "Could not save config to " + configFile, ex);
			}
		}
	}
}
