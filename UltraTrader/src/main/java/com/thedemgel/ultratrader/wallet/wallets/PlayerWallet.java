package com.thedemgel.ultratrader.wallet.wallets;

import com.thedemgel.ultratrader.UltraTrader;
import com.thedemgel.ultratrader.L;
import com.thedemgel.ultratrader.conversation.admin.bank.player.PlayerWalletPrompt;
import com.thedemgel.ultratrader.shop.Shop;
import com.thedemgel.ultratrader.util.ConfigValue;
import com.thedemgel.ultratrader.util.Permissions;
import com.thedemgel.ultratrader.wallet.Wallet;
import com.thedemgel.ultratrader.wallet.annotation.AssignConversation;
import com.thedemgel.ultratrader.wallet.annotation.WalletPermission;
import com.thedemgel.ultratrader.wallet.annotation.WalletTypeName;
import java.math.BigDecimal;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

/**
 * Player wallets are wallets that use the players physical in-game wallet.
 *
 * This does not effect Inventory in the least.
 */
@WalletTypeName("player")
@WalletPermission(Permissions.WALLET_PLAYER)
@AssignConversation(PlayerWalletPrompt.class)
public class PlayerWallet extends Wallet {

	private Economy economy;

	public PlayerWallet(Shop shop) {
		super(shop);
		economy = UltraTrader.getEconomy();
	}

	@Override
	public boolean hasFunds(BigDecimal amount) {
		return economy.has(getPlayer(), amount.doubleValue());
	}

	@Override
	public BigDecimal getBalance() {
		double response = economy.getBalance(getPlayer());
		return BigDecimal.valueOf(response);
	}

	@Override
	public EconomyResponse removeFunds(BigDecimal amount) {
		return economy.withdrawPlayer(getPlayer(), amount.doubleValue());
	}

	@Override
	public EconomyResponse addFunds(BigDecimal amount) {
		return economy.depositPlayer(getPlayer(), amount.doubleValue());
	}

	public String getPlayer() {
		return ((ConfigValue<String>) getShop().getWalletinfo().get("player")).getValue();
	}

	@Override
	public String getDisplayName() {
		return L.getString("general.wallet.player");
	}
}
