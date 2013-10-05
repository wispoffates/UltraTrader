package com.thedemgel.cititradersre.wallet.wallets;

import com.thedemgel.cititradersre.CitiTrader;
import com.thedemgel.cititradersre.conversation.admin.bank.player.PlayerWalletPrompt;
import com.thedemgel.cititradersre.shop.Shop;
import com.thedemgel.cititradersre.util.ConfigValue;
import com.thedemgel.cititradersre.util.Permissions;
import com.thedemgel.cititradersre.wallet.Wallet;
import com.thedemgel.cititradersre.wallet.annotation.AssignConversation;
import com.thedemgel.cititradersre.wallet.annotation.WalletPermission;
import com.thedemgel.cititradersre.wallet.annotation.WalletTypeName;
import java.math.BigDecimal;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

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
		economy = CitiTrader.getEconomy();
	}

	@Override
	public boolean hasFunds(BigDecimal amount) {
		return economy.has(getPlayer(), amount.doubleValue());
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
		return CitiTrader.getResourceBundle().getString("general.wallet.player");
	}
}
