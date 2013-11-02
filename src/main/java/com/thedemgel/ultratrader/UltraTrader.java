package com.thedemgel.ultratrader;

import com.thedemgel.ultratrader.citizens.RentalShop;
import com.thedemgel.ultratrader.citizens.TraderTrait;
import com.thedemgel.ultratrader.command.commands.DebugCommands;
import com.thedemgel.ultratrader.command.commands.ShopCommands;
import com.thedemgel.ultratrader.conversation.ConversationHandler;
import com.thedemgel.ultratrader.data.DataObject;
import com.thedemgel.ultratrader.data.YamlDataObject;
import com.thedemgel.ultratrader.inventory.AdminInventoryInterface;
import com.thedemgel.ultratrader.inventory.InventoryInterfaceHandler;
import com.thedemgel.ultratrader.inventory.ShopInventoryInterface;
import com.thedemgel.ultratrader.shop.ShopHandler;
import com.thedemgel.ultratrader.wallet.WalletHandler;
import com.thedemgel.ultratrader.wallet.wallets.AdminWallet;
import com.thedemgel.ultratrader.wallet.wallets.BankWallet;
import com.thedemgel.ultratrader.wallet.wallets.PlayerWallet;
import com.thedemgel.ultratrader.wallet.wallets.ShopWallet;
import com.thedemgel.yamlresourcebundle.YamlResourceBundle;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.trait.TraitInfo;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.Metrics;

public class UltraTrader extends JavaPlugin {

	public static final String STORE_DIR = "stores";
	public static final int STORE_ID_RAND_BASE = 10;
	public static final int STORE_ID_RAND_INCREMENT = 50;
	public static final long BUKKIT_SCHEDULER_DELAY = 2;
	private static ConversationHandler conversationHandler;
	private static UltraTrader plugin;
	private static Economy economy;
	private static DataObject dbObj;
	private static WalletHandler wallethandler;
	private static InventoryInterfaceHandler inventoryInterfaceHandler;
	private static ShopHandler shopHandler;
	private static RentalHandler rentalHandler;

	public static Economy getEconomy() {
		return economy;
	}

	public static ConversationHandler getConversationHandler() {
		return conversationHandler;
	}

	public static UltraTrader getInstance() {
		return plugin;
	}

	public static DataObject getDbObj() {
		return dbObj;
	}

	public static WalletHandler getWallethandler() {
		return wallethandler;
	}

	public static InventoryInterfaceHandler getInventoryInterfaceHandler() {
		return inventoryInterfaceHandler;
	}

	public static RentalHandler getRentalHandler() {
		return rentalHandler;
	}

	private boolean citizens;
	private boolean vault;
	private boolean debug = false;

	public void toggleDebug() {
		debug = !debug;
	}

	public boolean isDebug() {
		return debug;
	}

	@Override
	public final void onEnable() {
		try {
			Metrics metrics = new Metrics(this);
			metrics.start();
		} catch (IOException e) {
			// Failed to submit the stats :-(
			getLogger().info("Metrics has failed to load...");
		}
		plugin = this;

		rentalHandler = new RentalHandler();

		LimitHandler.init();
		// Populate config.yml
		getConfig().options().copyDefaults(true);
		this.saveConfig();

		wallethandler = new WalletHandler();

		getWallethandler().registerWallet(AdminWallet.class, L.getString("general.wallet.admin"))
			.registerWallet(PlayerWallet.class, L.getString("general.wallet.player"))
			.registerWallet(BankWallet.class, L.getString("general.wallet.bank"))
			.registerWallet(ShopWallet.class, L.getString("general.wallet.shop"));

		inventoryInterfaceHandler = new InventoryInterfaceHandler();

		getInventoryInterfaceHandler().registerInventoryInterface(AdminInventoryInterface.class, "admin")
			.registerInventoryInterface(ShopInventoryInterface.class, "shop");

		dbObj = new YamlDataObject();

		// Verify resources (Vault/CitizensAPI)
		checkCitizens();
		checkVault();

		shopHandler = new ShopHandler(this);
		conversationHandler = new ConversationHandler(this);

		shopHandler.initShops();

		getCommand("trader").setExecutor(new ShopCommands(this));
		getCommand("traderadmin").setExecutor(new DebugCommands());
		getServer().getPluginManager().registerEvents(new ShopListener(this), this);
		this.getLogger().log(Level.INFO, "UltraTrader Enabled...");
	}

	@Override
	public final void onDisable() {
		getStoreHandler().saveShops();
		this.saveConfig();
		this.getLogger().log(Level.INFO, "UltraTrader Disabled...");
	}

	public static ShopHandler getStoreHandler() {
		return shopHandler;
	}

	private void checkCitizens() {
		if (getServer().getPluginManager().getPlugin("Citizens") == null || getServer().getPluginManager().getPlugin("Citizens").isEnabled() == false) {
			getLogger().log(Level.WARNING, "Citizens 2.0 not found or not enabled");
			citizens = false;
		} else {
			citizens = true;
		}

		if (citizens) {
			CitizensAPI.getTraitFactory().registerTrait(TraitInfo.create(TraderTrait.class).withName("ultratrader"));
			CitizensAPI.getTraitFactory().registerTrait(TraitInfo.create(RentalShop.class).withName("rentalshop"));
		}
	}

	public final boolean isCitizens() {
		return citizens;
	}

	private void checkVault() {
		if (getServer().getPluginManager().getPlugin("Vault") == null || getServer().getPluginManager().getPlugin("Vault").isEnabled() == false) {
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

	public final boolean isVault() {
		return vault;
	}
}
