
package com.thedemgel.ultratrader.wallet;

import com.thedemgel.ultratrader.shop.Shop;
import com.thedemgel.ultratrader.wallet.annotation.WalletPermission;
import com.thedemgel.ultratrader.wallet.annotation.WalletTypeName;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;


public class WalletHandler {
	public static final String DEFAULT_WALLET_TYPE = "shop";
	
	private ConcurrentMap<String, Class<? extends Wallet>> wallets;
	private ConcurrentMap<String, String> displaynames;
	
	public WalletHandler() {
		wallets = new ConcurrentHashMap<>();
		displaynames = new ConcurrentHashMap<>();
	}
	
	public WalletHandler registerWallet(Class<? extends Wallet> walletclass, String displayname) {
		if (walletclass.isAnnotationPresent(WalletTypeName.class)) {
			WalletTypeName typename = walletclass.getAnnotation(WalletTypeName.class);
			if (getWallets().containsKey(typename.value())) {
				// Throw exception
				return this;
			}
			
			getWallets().put(typename.value(), walletclass);
			displaynames.put(typename.value(), displayname);
		}
		
		return this;
	}
	
	public String getPermission(String walletname) throws Exception {
		if (!wallets.containsKey(walletname)) {
			// Throw Exception
			throw new Exception("Wallet not found");
		}
		
		Class<? extends Wallet> walletclass = getWallets().get(walletname);
		
		return getPermission(walletclass);
	}
	
	public String getPermission(Class<? extends Wallet> walletclass) throws Exception {
		if (!walletclass.isAnnotationPresent(WalletPermission.class)) {
			throw new Exception("Wallet is not Annotated properly." + walletclass);
		}
		
		WalletPermission perm = walletclass.getAnnotation(WalletPermission.class);
		
		return perm.value();
	}
	
	public Wallet getWalletInstance(String walletname, Shop shop) {
		if (!wallets.containsKey(walletname)) {
			// Throw Exception
			// Return default wallet.
			return null;
		}
		
		Class<? extends Wallet> walletclass = getWallets().get(walletname);
		Wallet wallet;
		try {
			wallet = walletclass.getConstructor(Shop.class).newInstance(shop);
		} catch (InvocationTargetException | IllegalAccessException | InstantiationException | NoSuchMethodException | SecurityException ex) {
			Bukkit.getLogger().log(Level.SEVERE, "Error Accessing or Instantiating Wallet.");
			// Return default wallet.
			return null;
		}
		
		return wallet;
	}

	public ConcurrentMap<String, Class<? extends Wallet>> getWallets() {
		return wallets;
	}
	
	public String getDisplayName(String walletname) {
		if (!displaynames.containsKey(walletname)) {
			return walletname;
		}
		
		return displaynames.get(walletname);
	}
}
