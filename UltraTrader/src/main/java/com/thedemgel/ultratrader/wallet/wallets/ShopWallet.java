package com.thedemgel.ultratrader.wallet.wallets;

import com.thedemgel.ultratrader.L;
import com.thedemgel.ultratrader.shop.Shop;
import com.thedemgel.ultratrader.util.ConfigValue;
import com.thedemgel.ultratrader.util.Permissions;
import com.thedemgel.ultratrader.wallet.Wallet;
import com.thedemgel.ultratrader.wallet.annotation.WalletPermission;
import com.thedemgel.ultratrader.wallet.annotation.WalletTypeName;
import java.math.BigDecimal;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.economy.EconomyResponse.ResponseType;

/**
 * ShopWallets are internal wallets, the value is stored in the Shop.yml.
 *
 * This does not effect Inventory in the least.
 */
@WalletTypeName("shop")
@WalletPermission(Permissions.WALLET_SHOP)
public class ShopWallet extends Wallet {

	private Shop shop;

	public ShopWallet(Shop shop) {
		super(shop);
		this.shop = shop;
	}

	@Override
	public boolean hasFunds(BigDecimal amount) {
		BigDecimal balance = getBalance();
		if (balance.compareTo(amount) >= 0) {
			return true;
		}
		return false;
	}

	@Override
	public EconomyResponse removeFunds(BigDecimal amount) {
		BigDecimal balance = getBalance();
		if (hasFunds(amount)) {
			balance = balance.subtract(amount);
			double value = balance.doubleValue();
			shop.getWalletinfo().put("balance", new ConfigValue(value));
			return new EconomyResponse(amount.doubleValue(), getBalance().doubleValue(), ResponseType.SUCCESS, "");
		}

		return new EconomyResponse(amount.doubleValue(), getBalance().doubleValue(), ResponseType.FAILURE, "Not enough funds");
	}

	@Override
	public EconomyResponse addFunds(BigDecimal amount) {
		BigDecimal balance = getBalance();
		balance = balance.add(amount);
		double value = balance.doubleValue();
		shop.getWalletinfo().put("balance", new ConfigValue(value));
		return new EconomyResponse(amount.doubleValue(), getBalance().doubleValue(), ResponseType.SUCCESS, "");
	}

	@Override
	public BigDecimal getBalance() {
		ConfigValue<Double> get = shop.getWalletinfo().get("balance");
		if (get == null) {
			get = new ConfigValue<>(0D);
		}
		BigDecimal balance = BigDecimal.valueOf(get.getValue());
		return balance;
	}

	@Override
	public String getDisplayName() {
		return L.getString("general.wallet.shop");
	}
}
