package com.thedemgel.ultratrader.wallet.wallets;

import com.thedemgel.ultratrader.UltraTrader;
import com.thedemgel.ultratrader.Lang;
import com.thedemgel.ultratrader.conversation.admin.bank.bank.BankWalletPrompt;
import com.thedemgel.ultratrader.shop.Shop;
import com.thedemgel.ultratrader.util.ConfigValue;
import com.thedemgel.ultratrader.util.Permissions;
import com.thedemgel.ultratrader.wallet.Wallet;
import com.thedemgel.ultratrader.wallet.annotation.AssignConversation;
import com.thedemgel.ultratrader.wallet.annotation.WalletPermission;
import com.thedemgel.ultratrader.wallet.annotation.WalletTypeName;
import java.math.BigDecimal;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.economy.EconomyResponse.ResponseType;

/**
 * Bank wallets will use a Bank if the Economy plugin supports them.
 *
 * If the Economy plugin does not support them, it will default to ShopWallet.
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
		economy = UltraTrader.getEconomy();
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
	public BigDecimal getBalance() {
		EconomyResponse response = economy.bankBalance(getBank());

		BigDecimal balance = BigDecimal.valueOf(response.balance);

		return balance;
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
		return Lang.getString("general.wallet.bank");
	}
}
