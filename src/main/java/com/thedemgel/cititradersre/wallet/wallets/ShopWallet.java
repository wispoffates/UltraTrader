package com.thedemgel.cititradersre.wallet.wallets;

import com.thedemgel.cititradersre.CitiTrader;
import com.thedemgel.cititradersre.shop.Shop;
import com.thedemgel.cititradersre.util.ConfigValue;
import com.thedemgel.cititradersre.util.Permissions;
import com.thedemgel.cititradersre.wallet.Wallet;
import com.thedemgel.cititradersre.wallet.annotation.WalletPermission;
import com.thedemgel.cititradersre.wallet.annotation.WalletTypeName;
import java.math.BigDecimal;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.economy.EconomyResponse.ResponseType;
import org.bukkit.configuration.ConfigurationSection;

/**
 * ShopWallets are internal wallets, the value is stored in the Shop.yml.
 *
 * This does not effect Inventory in the least.
 */
@WalletTypeName("shop")
@WalletPermission(Permissions.WALLET_SHOP)
public class ShopWallet extends Wallet {

	//private ConfigurationSection config;
	private Shop shop;

	public ShopWallet(Shop shop) {
		super(shop);
		this.shop = shop;
	}

	@Override
	public boolean hasFunds(BigDecimal amount) {
		BigDecimal balance = getBalance();
		if (balance.compareTo(amount) > 0) {
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

	private BigDecimal getBalance() {
		//BigDecimal balance = BigDecimal.valueOf(config.getDouble("balance", 0D));
		ConfigValue<Double> get = shop.getWalletinfo().get("balance");
		if (get == null) {
			get = new ConfigValue<>(0D);
		}
		BigDecimal balance = BigDecimal.valueOf(get.getValue());
		return balance;
	}
	
	@Override
	public String getDisplayName() {
		return CitiTrader.getResourceBundle().getString("general.wallet.shop");
	}
}
