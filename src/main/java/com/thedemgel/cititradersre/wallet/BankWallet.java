
package com.thedemgel.cititradersre.wallet;

import com.thedemgel.cititradersre.CitiTrader;
import java.math.BigDecimal;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.economy.EconomyResponse.ResponseType;
import org.bukkit.configuration.ConfigurationSection;


/**
 * Bank wallets will use a Bank if the Economy plugin supports them.
 * 
 * If the Economy plugin does not support them, it will default to
 * ShopWallet.
 * 
 * This does not effect Inventory in the least.
 */
public class BankWallet implements Wallet {
	private ConfigurationSection config;
	private Economy economy;

	public BankWallet(ConfigurationSection walletConfig) {
		config = walletConfig;
		economy = CitiTrader.getEconomy();
	}

	@Override
	public boolean hasFunds(BigDecimal amount) {
		String bank = getBank();
		if (bank == null) {
			return false;
		}
		
		EconomyResponse response = economy.bankHas(bank, amount.doubleValue());
		
		return response.type.equals(ResponseType.SUCCESS);
	}

	@Override
	public EconomyResponse removeFunds(BigDecimal amount) {
		String bank = getBank();
		if (bank == null) {
			return new EconomyResponse(0, 0, ResponseType.FAILURE, "Bank account is missing or not set.");
		}
		
		EconomyResponse response = economy.bankWithdraw(bank, amount.doubleValue());
		
		return response;
	}

	@Override
	public EconomyResponse addFunds(BigDecimal amount) {
		String bank = getBank();
		if (bank == null) {
			return new EconomyResponse(0, 0, ResponseType.FAILURE, "Bank account is missing or not set.");
		}
		
		EconomyResponse response = economy.bankDeposit(bank, amount.doubleValue());
		
		return response;
	}

	public void setBank(String string) {
		config.set("bank", string);
		
	}
	
	public String getBank() {
		return config.getString("bank");
	}
}
