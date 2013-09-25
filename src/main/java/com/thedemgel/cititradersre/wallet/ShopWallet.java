
package com.thedemgel.cititradersre.wallet;

import java.math.BigDecimal;
import org.bukkit.configuration.ConfigurationSection;


/**
 * ShopWallets are internal wallets, the value is stored in the Shop.yml.
 * 
 * This does not effect Inventory in the least.
 */
public class ShopWallet implements Wallet {
	
	private ConfigurationSection config;

	public ShopWallet(ConfigurationSection walletConfig) {
		config = walletConfig;
	}
	
	@Override
	public boolean hasFunds() {
		return true;
	}

	@Override
	public boolean removeFunds(BigDecimal amount) {
		return true;
	}

	@Override
	public boolean addFunds(BigDecimal amount) {
		return true;
	}

}
