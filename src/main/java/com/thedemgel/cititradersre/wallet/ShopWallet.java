package com.thedemgel.cititradersre.wallet;

import java.math.BigDecimal;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.economy.EconomyResponse.ResponseType;
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
	public boolean hasFunds(BigDecimal amount) {
		BigDecimal balance = getBalance();
		if (balance.compareTo(amount) > 0) {
			return true;
		}
		return false;
	}

	@Override
	public EconomyResponse removeFunds(BigDecimal amount) {
		BigDecimal balance = getBalance();
		if (hasFunds(amount)) {
			config.set("balance", balance.subtract(amount));
			return new EconomyResponse(amount.doubleValue(), getBalance().doubleValue(), ResponseType.SUCCESS, "");
		}
		
		return new EconomyResponse(amount.doubleValue(), getBalance().doubleValue(), ResponseType.FAILURE, "Not enough funds");
	}

	@Override
	public EconomyResponse addFunds(BigDecimal amount) {
		BigDecimal balance = getBalance();
		config.set("balance", balance.add(amount));
		return new EconomyResponse(amount.doubleValue(), getBalance().doubleValue(), ResponseType.SUCCESS, "");
	}

	private BigDecimal getBalance() {
		BigDecimal balance = BigDecimal.valueOf(config.getDouble("balance", 0D));
		return balance;
	}
}
