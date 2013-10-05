package com.thedemgel.cititradersre.data;

import com.thedemgel.cititradersre.shop.Shop;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class DataObject {

	private final ExecutorService pool;

	public DataObject() {
		pool = Executors.newSingleThreadExecutor();
		System.out.println("Pool created");
	}

	public abstract void save(Shop shop);
	
	public abstract void load(int shopid);
	
	public abstract void initShops();
	
	public ExecutorService getPool() {
		return pool;
	}
}
