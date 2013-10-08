package com.thedemgel.cititradersre.wallet.wallets;

import com.thedemgel.cititradersre.L;
import com.thedemgel.cititradersre.shop.Shop;
import com.thedemgel.cititradersre.util.Permissions;
import com.thedemgel.cititradersre.wallet.Wallet;
import com.thedemgel.cititradersre.wallet.annotation.WalletPermission;
import com.thedemgel.cititradersre.wallet.annotation.WalletTypeName;
import java.math.BigDecimal;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.economy.EconomyResponse.ResponseType;

/**
 * Admin wallets will contain no funds, will accept all funds will always have
 * funds. These are Server shops that have unlimited funds.
 *
 * This does not effect Inventory in the least.
 */
@WalletTypeName("admin")
@WalletPermission(Permissions.WALLET_ADMIN)
public class AdminWallet extends Wallet {

	public AdminWallet(Shop shop) {
		super(shop);
	}

	@Override
	public boolean hasFunds(BigDecimal amount) {
		return true;
	}

	@Override
	public EconomyResponse removeFunds(BigDecimal amount) {
		return new EconomyResponse(amount.doubleValue(), 0, ResponseType.SUCCESS, "");
	}

	@Override
	public EconomyResponse addFunds(BigDecimal amount) {
		return new EconomyResponse(amount.doubleValue(), 0, ResponseType.SUCCESS, "");
	}

	@Override
	public String getDisplayName() {
		return L.getString("general.wallet.admin");
	}
}
