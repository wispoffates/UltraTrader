
package com.thedemgel.cititradersre.wallet;

import java.math.BigDecimal;


/**
 * Bank wallets will use a Bank if the Economy plugin supports them.
 * 
 * If the Economy plugin does not support them, it will default to
 * ShopWallet.
 * 
 * This does not effect Inventory in the least.
 */
public class BankWallet implements Wallet {

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
