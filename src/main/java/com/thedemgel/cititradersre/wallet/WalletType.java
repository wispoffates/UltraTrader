package com.thedemgel.cititradersre.wallet;

import com.thedemgel.cititradersre.wallet.wallets.AdminWallet;
import com.thedemgel.cititradersre.wallet.wallets.BankWallet;
import com.thedemgel.cititradersre.wallet.wallets.PlayerWallet;
import com.thedemgel.cititradersre.wallet.wallets.ShopWallet;
import com.thedemgel.cititradersre.wallet.Wallet;
import org.bukkit.configuration.ConfigurationSection;

public enum WalletType {

	ADMIN {
		@Override
		public Wallet getNewWallet(ConfigurationSection config) {
			return new AdminWallet();
		}
	},
	BANK {
		@Override
		public Wallet getNewWallet(ConfigurationSection config) {
			return new BankWallet(config);
		}
	},
	PLAYER {
		@Override
		public Wallet getNewWallet(ConfigurationSection config) {
			return new PlayerWallet(config);
		}
	},
	SHOP {
		@Override
		public Wallet getNewWallet(ConfigurationSection config) {
			return new ShopWallet(config);
		}
	};

	private WalletType() {
	}

	public abstract Wallet getNewWallet(ConfigurationSection config);
}
