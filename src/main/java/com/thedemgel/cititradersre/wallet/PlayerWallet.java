package com.thedemgel.cititradersre.wallet;

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
public class PlayerWallet implements Wallet {

	private ConfigurationSection config;
	private Economy economy;

	public PlayerWallet(ConfigurationSection walletConfig) {
		config = walletConfig;
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
	
	public void setPlayer(Player player) {
		setPlayer(player.getName());
	}
	
	public void setPlayer(String player) {
		config.set("player", player);
	}
	
	public String getPlayer() {
		return config.getString("player");
	}
}
