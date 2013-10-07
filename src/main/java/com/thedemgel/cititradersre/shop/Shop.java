package com.thedemgel.cititradersre.shop;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import com.thedemgel.cititradersre.CitiTrader;
import com.thedemgel.cititradersre.StoreConfig;
import com.thedemgel.cititradersre.inventory.InventoryInterface;
import com.thedemgel.cititradersre.inventory.ShopInventoryInterface;
import com.thedemgel.cititradersre.util.ConfigValue;
import com.thedemgel.cititradersre.wallet.Wallet;
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
	//private StoreConfig shopConfig;
	private Wallet wallet;
	private InventoryInterface inv;

	public Shop(StoreConfig shopconfig) {
		//shopConfig = shopconfig;
		inv = new ShopInventoryInterface(this);

		//initWallet();
		//loadSellPrice();
		//loadInventory();


		//setMetaData();
		//save();
	}

	public void save() {
		CitiTrader.getDbObj().save(this);
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

	public void addBuyItem(ItemStack item, Double price, Integer quantity, String description) {
		ItemPrice invItem = new ItemPrice(item, BigDecimal.valueOf(price), quantity, description);
		String random = invItem.setRandom();
		while (getBuyprices().containsKey(random)) {
			random = invItem.setRandom();
		}
		getBuyprices().put(random, invItem);
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

	/*public final void loadSellPrice() {
		ConfigurationSection sellconfig = shopConfig.getConfig().getConfigurationSection("sellprices");
		if (sellconfig == null) {
			sellconfig = shopConfig.getConfig().createSection("sellprices");
		}
		for (String id : sellconfig.getKeys(false)) {
			ConfigurationSection itemconfig = sellconfig.getConfigurationSection(id);
			ItemPrice item = new ItemPrice();
			item.setAmount(itemconfig.getInt("amount"));
			item.setDescription(itemconfig.getString("description"));
			item.setItemStack(itemconfig.getItemStack("itemStack"));
			item.setPrice(BigDecimal.valueOf(itemconfig.getDouble("price")));
			item.setRandom(itemconfig.getString("random"));
			getSellprices().put(item.getId(), item);

			/*info.put(id + "amount", new ConfigValue(item.getAmount()));
			 info.put(id + "description", new ConfigValue(item.getDescription()));
			
			 ConfigValue<Integer> in = info.get(id + "amount");
			 ConfigValue<String> ind = info.get(id + "description");
			
			 System.out.println((in.getValue() + 100) + " -- " + (ind.getValue() + 100));
		}


	}*/

	/*public ConfigurationSection getInventoryConfig() {
		ConfigurationSection invconfig = shopConfig.getConfig().getConfigurationSection("inventory");
		if (invconfig == null) {
			invconfig = shopConfig.getConfig().createSection("inventory");
		}
		return invconfig;
	}*/

	public InventoryInterface getInventoryInterface() {
		return inv;
	}

	/*public final void loadInventory() {
		ConfigurationSection invconfig = getInventoryConfig().getConfigurationSection("items");
		if (invconfig == null) {
			invconfig = getInventoryConfig().createSection("items");
		}
		for (String num : invconfig.getKeys(false)) {
			ItemStack item = invconfig.getItemStack(num + ".itemstack");
			Integer amount = invconfig.getInt(num + ".amount");
			getInventory().put(item, amount);
		}
	}*/

	public final void initWallet() {
		/*ConfigurationSection walletconfig = shopConfig.getConfig().getConfigurationSection("wallet");
		if (walletconfig == null) {
			walletconfig = shopConfig.getConfig().createSection("wallet");
		}

		String type = walletconfig.getString("type", "SHOP");

		for (String key : walletconfig.getKeys(false)) {
			getWalletinfo().put(key, new ConfigValue(walletconfig.get(key)));
		}*/

		ConfigValue<String> wallettype = getWalletinfo().get("type");
		
		//WalletType walletType = WalletType.valueOf(wallettype.getValue());

		setWallet(CitiTrader.getWallethandler().getWalletInstance(wallettype.getValue(), this));
	}

	public Wallet getWallet() {
		return wallet;
	}

	public void setWalletType(String type) {
		/*ConfigurationSection walletconfig = shopConfig.getConfig().getConfigurationSection("wallet");
		if (walletconfig == null) {
			walletconfig = shopConfig.getConfig().createSection("wallet");
			walletconfig.set("type", WalletType.SHOP.name());
		}*/

		walletinfo.put("type", new ConfigValue(type));
		//walletconfig.set("type", type.name());

		initWallet();
	}

	public String getWalletType() {
		/*ConfigurationSection walletconfig = shopConfig.getConfig().getConfigurationSection("wallet");
		if (walletconfig == null) {
			walletconfig = shopConfig.getConfig().createSection("wallet");
			walletconfig.set("type", WalletType.SHOP.name());
		}*/
		
		ConfigValue<String> type = walletinfo.get("type");
		
		if (type == null) {
			type = new ConfigValue("shop");
			walletinfo.put("type", type);
		}
		
		return type.getValue();
	}

	public String getOwner() {
		ConfigValue<String> owner = info.get("owner");
		return owner.getValue();
		//return shopConfig.getConfig().getString("info.owner", "");
	}

	public void setOwner(Player player) {
		setOwner(player.getName());
	}

	public void setOwner(String value) {
		ConfigValue<String> owner = new ConfigValue(value);
		info.put("owner", owner);
		//shopConfig.getConfig().set("info.owner", value);
	}

	public boolean isOwner(Player player) {
		return getOwner().equals(player.getName());
	}

	public String getName() {
		ConfigValue<String> name = info.get("name");
		return name.getValue();
		//return shopConfig.getConfig().getString("info.name");
	}

	public void setName(String value) {
		ConfigValue<String> name = new ConfigValue(value);
		info.put("name", name);
		//shopConfig.getConfig().set("info.name", value);
	}

	public Integer getId() {
		ConfigValue<Integer> id = info.get("id");
		return id.getValue();
		//return Integer.valueOf(shopConfig.getConfig().getInt("info.id"));
	}

	public void setId(int value) {
		ConfigValue<Integer> id = new ConfigValue(value);
		info.put("id", id);
		//shopConfig.getConfig().set("info.id", id);
	}

	public ConcurrentMap<String, ConfigValue> getInfo() {
		return info;
	}

	/*public void setInfo(ConcurrentMap<String, ConfigValue> info) {
		this.info = info;
	}*/

	/*public void setInventory(ConcurrentMap<ItemStack, Integer> inventory) {
		this.inventory = inventory;
	}*/

	/*public void setBuyprices(ConcurrentMap<String, ItemPrice> buyprices) {
		this.buyprices = buyprices;
	}*/

	/*public void setSellprices(ConcurrentMap<String, ItemPrice> sellprices) {
		this.sellprices = sellprices;
	}*/

	public void setWallet(Wallet wallet) {
		this.wallet = wallet;
	}

	public ConcurrentMap<String, ConfigValue> getWalletinfo() {
		return walletinfo;
	}
}
