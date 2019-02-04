package com.thedemgel.ultratrader.wallet;

import com.thedemgel.ultratrader.shop.Shop;
import com.thedemgel.ultratrader.util.ConfigValue;
import java.math.BigDecimal;
import net.milkbowl.vault.economy.EconomyResponse;

public abstract class Wallet {

	private Shop shop;

	public Wallet(Shop shop) {
		this.shop = shop;
	}

	public abstract boolean hasFunds(BigDecimal amount);

	public abstract BigDecimal getBalance();

	public abstract EconomyResponse removeFunds(BigDecimal amount);

	public abstract EconomyResponse addFunds(BigDecimal amount);

	public void setInfo(String key, ConfigValue<?> value) {
		shop.getWalletinfo().put(key, value);
	}

	public <T> ConfigValue<T> getInfo(String key, Class<T> clazz) {
		ConfigValue<T> ret = shop.getWalletinfo().get(key);
		return ret;
	}

	protected Shop getShop() {
		return shop;
	}

	public abstract String getDisplayName();
}
