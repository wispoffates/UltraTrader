
package com.thedemgel.cititradersre.wallet;

import java.math.BigDecimal;


/**
 * Player wallets are wallets that use the players physical in-game
 * wallet.
 * 
 * This does not effect Inventory in the least.
 */
public class PlayerWallet implements Wallet {

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
