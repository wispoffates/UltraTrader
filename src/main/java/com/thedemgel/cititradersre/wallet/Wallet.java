
package com.thedemgel.cititradersre.wallet;

import java.math.BigDecimal;
import net.milkbowl.vault.economy.EconomyResponse;


public interface Wallet {
	public boolean hasFunds(BigDecimal amount);
	public EconomyResponse removeFunds(BigDecimal amount);
	public EconomyResponse addFunds(BigDecimal amount);
}
