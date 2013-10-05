
package com.thedemgel.cititradersre.wallet.wallets;

import com.thedemgel.cititradersre.CitiTrader;
import com.thedemgel.cititradersre.conversation.admin.bank.bank.BankWalletPrompt;
import com.thedemgel.cititradersre.shop.Shop;
import com.thedemgel.cititradersre.util.ConfigValue;
import com.thedemgel.cititradersre.util.Permissions;
import com.thedemgel.cititradersre.wallet.Wallet;
import com.thedemgel.cititradersre.wallet.annotation.AssignConversation;
import com.thedemgel.cititradersre.wallet.annotation.WalletPermission;
import com.thedemgel.cititradersre.wallet.annotation.WalletTypeName;
import java.math.BigDecimal;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.economy.EconomyResponse.ResponseType;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.conversations.Prompt;


/**
 * Bank wallets will use a Bank if the Economy plugin supports them.
 * 
 * If the Economy plugin does not support them, it will default to
 * ShopWallet.
 * 
 * This does not effect Inventory in the least.
 */
@WalletTypeName("bank")
@WalletPermission(Permissions.WALLET_BANK)
@AssignConversation(BankWalletPrompt.class)
public class BankWallet extends Wallet {
	//private ConfigurationSection config;
	private Economy economy;

	public BankWallet(Shop shop) {
		super(shop);
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
	
	public String getBank() {
		ConfigValue<String> bank = getShop().getWalletinfo().get("bank");
		return bank.getValue();
	}
	
	@Override
	public String getDisplayName() {
		return CitiTrader.getResourceBundle().getString("general.wallet.bank");
	}
}
