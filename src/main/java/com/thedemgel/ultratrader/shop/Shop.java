package com.thedemgel.ultratrader.shop;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import com.thedemgel.ultratrader.UltraTrader;
import com.thedemgel.ultratrader.StoreConfig;
import com.thedemgel.ultratrader.inventory.InventoryInterface;
import com.thedemgel.ultratrader.inventory.InventoryInterfaceHandler;
import com.thedemgel.ultratrader.inventory.ShopInventoryInterface;
import com.thedemgel.ultratrader.util.ConfigValue;
import com.thedemgel.ultratrader.wallet.Wallet;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Shop {

	private ConcurrentMap<String, ItemPrice> buyprices = new ConcurrentHashMap<>();
	private ConcurrentMap<String, ItemPrice> sellprices = new ConcurrentHashMap<>();
	private ConcurrentMap<ItemStack, Integer> inventory = new ConcurrentHashMap<>();
	private ConcurrentMap<String, ConfigValue> info = new ConcurrentHashMap<>();
	private ConcurrentMap<String, ConfigValue> walletinfo = new ConcurrentHashMap<>();
	private ConcurrentMap<String, ConfigValue> inventoryinfo = new ConcurrentHashMap<>();
	private Wallet wallet;
	private InventoryInterface inv;

	public Shop(StoreConfig shopconfig) {
		//inv = new ShopInventoryInterface(this);
	}

	public void save() {
		UltraTrader.getDbObj().save(this);
	}

	public ConcurrentMap<ItemStack, Integer> getInventory() {
		return inventory;
	}

	public boolean hasSellItem(final ItemPrice checkItem) {
		Predicate<ItemPrice> itemPredicate = new Predicate<ItemPrice>() {
			@Override
			public boolean apply(ItemPrice item) {
				return item.getItemStack().equals(checkItem.getItemStack());
			}
		};
		Collection<ItemPrice> sellItems = Collections2.filter(getSellprices().values(), itemPredicate);

		return sellItems.size() > 0;
	}

	public boolean hasBuyItem(final ItemStack checkItem) {
		return getBuyItem(checkItem) != null;
	}

	public ItemPrice getBuyItem(final ItemStack checkItem) {
		final ItemStack check = checkItem.clone();
		check.setAmount(1);

		Predicate<ItemPrice> itemPredicate = new Predicate<ItemPrice>() {
			@Override
			public boolean apply(ItemPrice item) {
				return item.getItemStack().equals(check);
			}
		};
		Collection<ItemPrice> buyItems = Collections2.filter(getBuyprices().values(), itemPredicate);

		if (buyItems.size() > 0) {
			return (ItemPrice) buyItems.toArray()[0];
		} else {
			return null;
		}
	}

	public boolean hasBuyItem(final ItemPrice checkItem) {
		Predicate<ItemPrice> itemPredicate = new Predicate<ItemPrice>() {
			@Override
			public boolean apply(ItemPrice item) {
				return item.getItemStack().equals(checkItem.getItemStack());
			}
		};
		Collection<ItemPrice> buyItems = Collections2.filter(getBuyprices().values(), itemPredicate);

		return buyItems.size() > 0;
	}

	public boolean addSellItem(ItemStack item, BigDecimal price, Integer quantity, String description) {

		ItemPrice invItem = new ItemPrice(item, price, quantity, description);

		if (hasSellItem(invItem)) {
			return false;
		}

		String random = invItem.setRandom();
		while (getSellprices().containsKey(random)) {
			random = invItem.setRandom();
		}
		getSellprices().put(random, invItem);
		return true;
	}

	public boolean addBuyItem(ItemStack item, BigDecimal price, Integer quantity, String description) {
		ItemPrice invItem = new ItemPrice(item, price, quantity, description);

		if (hasBuyItem(invItem)) {
			return false;
		}

		String random = invItem.setRandom();
		while (getBuyprices().containsKey(random)) {
			random = invItem.setRandom();
		}
		getBuyprices().put(random, invItem);
		return true;
	}

	private void setMetaData() {
		// Decide whether to set metadata (not if item store) and then set it.
	}

	public String getItemId(ItemStack item) {
		String id = Iterables.getLast(item.getItemMeta().getLore());
		id = id.substring(id.length() - 8);
		return id;
	}

	public ConcurrentMap<String, ItemPrice> getBuyprices() {
		return buyprices;
	}

	public ConcurrentMap<String, ItemPrice> getSellprices() {
		return sellprices;
	}

	/*public InventoryInterface getInventoryInterface() {
		return inv;
	}*/

	public final void initWallet() {
		ConfigValue<String> wallettype = getWalletinfo().get("type");
		setWallet(UltraTrader.getWallethandler().getWalletInstance(wallettype.getValue(), this));
	}

	public Wallet getWallet() {
		return wallet;
	}

	public void setWalletType(String type) {
		walletinfo.put("type", new ConfigValue(type));
		initWallet();
	}

	public String getWalletType() {
		ConfigValue<String> type = walletinfo.get("type");

		if (type == null) {
			type = new ConfigValue("shop");
			walletinfo.put("type", type);
		}

		return type.getValue();
	}

	public final void initInventoryInterface() {
		ConfigValue<String> interfacetype = getInventoryinfo().get("type");
		setInventoryInterface(UltraTrader.getInventoryInterfaceHandler().getInventoryInterfaceInstance(interfacetype.getValue(), this));
	}

	public void setInventoryInterface(InventoryInterface inventoryInterface) {
		inv = inventoryInterface;
	}

	public ConcurrentMap<String, ConfigValue> getInventoryinfo() {
		return inventoryinfo;
	}

	public InventoryInterface getInventoryInterface() {
		return inv;
	}

	public void setInventoryInterfaceType(String type) {
		inventoryinfo.put("type", new ConfigValue(type));
		initInventoryInterface();
	}

	public String getInventoryInterfaceType() {
		ConfigValue<String> type = inventoryinfo.get("type");

		if (type == null) {
			type = new ConfigValue(InventoryInterfaceHandler.DEFAULT_INVENTORY_TYPE);
			inventoryinfo.put("type", type);
		}

		return type.getValue();
	}


	public String getOwner() {
		ConfigValue<String> owner = info.get("owner");
		return owner.getValue();
	}

	public void setOwner(Player player) {
		setOwner(player.getName());
	}

	public void setOwner(String value) {
		ConfigValue<String> owner = new ConfigValue(value);
		info.put("owner", owner);
	}

	public boolean isOwner(Player player) {
		return getOwner().equals(player.getName());
	}

	public String getName() {
		ConfigValue<String> name = info.get("name");
		return name.getValue();
	}

	public void setName(String value) {
		ConfigValue<String> name = new ConfigValue(value);
		info.put("name", name);
	}

	public Integer getId() {
		ConfigValue<Integer> id = info.get("id");
		return id.getValue();
	}

	public void setId(int value) {
		ConfigValue<Integer> id = new ConfigValue(value);
		info.put("id", id);
	}

	public int getLevel() {
		ConfigValue<Integer> level = info.get("level");
		if (level == null) {
			info.put("level", new ConfigValue(1));
			return 1;
		}
		return level.getValue();
	}

	public void setLevel(int value) {
		ConfigValue<Integer> level = new ConfigValue(value);
		info.put("level", level);
	}

	public ConcurrentMap<String, ConfigValue> getInfo() {
		return info;
	}

	public void setWallet(Wallet wallet) {
		this.wallet = wallet;
	}

	public ConcurrentMap<String, ConfigValue> getWalletinfo() {
		return walletinfo;
	}
}
