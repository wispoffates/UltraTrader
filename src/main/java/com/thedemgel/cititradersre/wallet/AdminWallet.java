
package com.thedemgel.cititradersre.wallet;

import java.math.BigDecimal;


/**
 * Admin wallets will contain no funds, will accept all funds
 * will always have funds. These are Server shops that have unlimited
 * funds.
 * 
 * This does not effect Inventory in the least.
 */
public class AdminWallet implements Wallet {

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
