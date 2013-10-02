
package com.thedemgel.cititradersre.wallet.wallets;

import com.thedemgel.cititradersre.wallet.Wallet;
import java.math.BigDecimal;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.economy.EconomyResponse.ResponseType;


/**
 * Admin wallets will contain no funds, will accept all funds
 * will always have funds. These are Server shops that have unlimited
 * funds.
 * 
 * This does not effect Inventory in the least.
 */
public class AdminWallet implements Wallet {

	@Override
	public boolean hasFunds(BigDecimal amount) {
		return true;
	}

	@Override
	public EconomyResponse removeFunds(BigDecimal amount) {
		return new EconomyResponse(0, 0, ResponseType.SUCCESS, "");
	}

	@Override
	public EconomyResponse addFunds(BigDecimal amount) {
		return new EconomyResponse(0, 0, ResponseType.SUCCESS, "");
	}

}
