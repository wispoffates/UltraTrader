
package com.thedemgel.cititradersre.wallet;

import java.math.BigDecimal;


public interface Wallet {
	public boolean hasFunds();
	public boolean removeFunds(BigDecimal amount);
	public boolean addFunds(BigDecimal amount);
}
