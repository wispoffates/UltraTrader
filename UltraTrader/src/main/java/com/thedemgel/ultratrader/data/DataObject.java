package com.thedemgel.ultratrader.data;

import com.thedemgel.ultratrader.UltraTrader;
import com.thedemgel.ultratrader.shop.Shop;
import com.thedemgel.ultratrader.util.ShopAction;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.entity.Player;

public abstract class DataObject {

	private final ExecutorService pool;

	public DataObject() {
		pool = Executors.newSingleThreadExecutor();
		System.out.println("Pool created");
	}

	public abstract void save(Shop shop, boolean async);

	public abstract void load(int shopid);

	public abstract void initShops();

	public ExecutorService getPool() {
		return pool;
	}

	public abstract void initLogger(UltraTrader plugin);
	public abstract void doLog(Shop shop, Player player, EconomyResponse resp, ShopAction action, String message);

    public abstract void removeShopFile(int shopId);
}
