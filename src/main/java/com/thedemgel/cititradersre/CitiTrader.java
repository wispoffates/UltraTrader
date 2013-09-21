package com.thedemgel.cititradersre;

import com.thedemgel.cititradersre.citizens.TraderTrait;
import com.thedemgel.cititradersre.command.commands.ShopCommands;
import com.thedemgel.cititradersre.shop.ShopHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.trait.TraitInfo;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class CitiTrader extends JavaPlugin {

	private StoreConfig shopsConfig;
	private ShopHandler shopHandler;
	private boolean citizens;
	private boolean vault;
	
	private Economy economy;

	@Override
	public void onEnable() {
		// Verify resources (Vault/CitizensAPI)
		checkCitizens();
		checkVault();

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
		for (int i = 0; i < 10; i++) {
			ItemStack item = new ItemStack(Material.WOOD, 25, (short) ((int) i / 3));
			items.add(item);
		}

		shopsConfig.getConfig().set("items", items);
		shopsConfig.saveConfig();
		return items;
	}

	private void checkCitizens() {
		if (getServer().getPluginManager().getPlugin("Citizens") == null || getServer().getPluginManager().getPlugin("Citizens").isEnabled() == false) {
			getLogger().log(Level.WARNING, "Citizens 2.0 not found or not enabled");
			citizens = false;
		} else {
			citizens = true;
		}

		if (citizens) {
			CitizensAPI.getTraitFactory().registerTrait(TraitInfo.create(TraderTrait.class).withName("cititrader"));
		}
	}

	public boolean isCitizens() {
		return citizens;
	}

	public void checkVault() {
		if (getServer().getPluginManager().getPlugin("Vault") == null || getServer().getPluginManager().getPlugin("Citizens").isEnabled() == false) {
			getLogger().log(Level.WARNING, "Vault not found or not enabled");
			vault = false;
		} else {
			vault = true;
		}

		if (vault) {
			RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(Economy.class);
			if (economyProvider != null) {
				economy = economyProvider.getProvider();
			}
		}
	}

	public boolean isVault() {
		return vault;
	}
}
