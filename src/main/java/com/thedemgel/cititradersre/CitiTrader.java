package com.thedemgel.cititradersre;

import com.thedemgel.cititradersre.citizens.TraderTrait;
import com.thedemgel.cititradersre.command.commands.ShopCommands;
import com.thedemgel.cititradersre.conversation.ConversationHandler;
import com.thedemgel.cititradersre.data.DataObject;
import com.thedemgel.cititradersre.data.YamlDataObject;
import com.thedemgel.cititradersre.shop.ShopHandler;
import com.thedemgel.cititradersre.wallet.WalletHandler;
import com.thedemgel.cititradersre.wallet.wallets.AdminWallet;
import com.thedemgel.cititradersre.wallet.wallets.BankWallet;
import com.thedemgel.cititradersre.wallet.wallets.PlayerWallet;
import com.thedemgel.cititradersre.wallet.wallets.ShopWallet;
import com.thedemgel.yamlresourcebundle.YamlResourceBundle;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.trait.TraitInfo;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class CitiTrader extends JavaPlugin {

	public static final String STORE_DIR = "stores";
	public static final int STORE_ID_RAND_BASE = 10;
	public static final int STORE_ID_RAND_INCREMENT = 50;
	public static final long BUKKIT_SCHEDULER_DELAY = 2;
	//private static ResourceBundle rb;
	private static ConversationHandler conversationHandler;
	private static CitiTrader plugin;
	private static Economy economy;
	private static DataObject dbObj;
	private static WalletHandler wallethandler;
	private static ShopHandler shopHandler;

	public static Economy getEconomy() {
		return economy;
	}

	public static ConversationHandler getConversationHandler() {
		return conversationHandler;
	}

	public static CitiTrader getInstance() {
		return plugin;
	}

	public static DataObject getDbObj() {
		return dbObj;
	}

	public static WalletHandler getWallethandler() {
		return wallethandler;
	}
	
	private boolean citizens;
	private boolean vault;

	@Override
	public final void onLoad() {
		// Assign ResourceBundle (using YAMLResourceBundle)
		//Locale locale = new Locale(getConfig().getString("language", "en"));
		//rb = YamlResourceBundle.getBundle("lang.default", locale, getDataFolder());
	}
	@Override
	public final void onEnable() {
		plugin = this;
		wallethandler = new WalletHandler();

		getWallethandler().registerWallet(AdminWallet.class, L.getString("general.wallet.admin"))
			.registerWallet(PlayerWallet.class, L.getString("general.wallet.player"))
			.registerWallet(BankWallet.class, L.getString("general.wallet.bank"))
			.registerWallet(ShopWallet.class, L.getString("general.wallet.shop"));

		dbObj = new YamlDataObject();
		
		// Verify resources (Vault/CitizensAPI)
		checkCitizens();
		checkVault();

		shopHandler = new ShopHandler(this);
		conversationHandler = new ConversationHandler(this);

		shopHandler.initShops();

		getCommand("trader").setExecutor(new ShopCommands(this));
		getServer().getPluginManager().registerEvents(new ShopListener(this), this);
		this.getLogger().log(Level.INFO, "CitiTraders Enabled...");
	}

	@Override
	public final void onDisable() {
		this.getLogger().log(Level.INFO, "CitiTraders Disabled...");
	}

	//public static ResourceBundle getResourceBundle() {
	//	return rb;
	//}

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
			CitizensAPI.getTraitFactory().registerTrait(TraitInfo.create(TraderTrait.class).withName("cititrader"));
			CitizensAPI.getTraitFactory().registerTrait(TraitInfo.create(TraderTrait.class).withName("rentalshop"));
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
