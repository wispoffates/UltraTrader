package com.thedemgel.cititradersre.shop;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import com.thedemgel.cititradersre.StoreConfig;
import com.thedemgel.cititradersre.inventory.InventoryInterface;
import com.thedemgel.cititradersre.inventory.ShopInventoryInterface;
import com.thedemgel.cititradersre.wallet.WalletType;
import com.thedemgel.cititradersre.wallet.Wallet;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Shop {

	private ConcurrentMap<String, ItemPrice> buyprices = new ConcurrentHashMap<>();
	private ConcurrentMap<String, ItemPrice> sellprices = new ConcurrentHashMap<>();
	private ConcurrentMap<ItemStack, Integer> inventory = new ConcurrentHashMap<>();
	private StoreConfig shopConfig;
	private Wallet wallet;
	private InventoryInterface inv;

	public Shop(StoreConfig shopconfig) {
		shopConfig = shopconfig;
		inv = new ShopInventoryInterface(this);

		initWallet();
		loadSellPrice();
		loadInventory();

		setMetaData();
		save();
	}

	public void save() {
		saveInventory();
		saveSellPrice();
		shopConfig.saveConfig();
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
			System.out.println("Item Found!");
			//addInventory(item);
			return false;
		}

		String random = invItem.setRandom();
		while (getSellprices().containsKey(random)) {
			random = invItem.setRandom();
		}
		getSellprices().put(random, invItem);
		return true;
		//addInventory(item);
	}

	public void addBuyItem(ItemStack item, Double price, Integer quantity, String description) {
		ItemPrice invItem = new ItemPrice(item, BigDecimal.valueOf(price), quantity, description);
		String random = invItem.setRandom();
		while (getBuyprices().containsKey(random)) {
			random = invItem.setRandom();
		}
		getBuyprices().put(random, invItem);
	}

	/*public void addInventory(ItemStack item) {
	 ItemStack invItem = item.clone();
	 invItem.setAmount(1);

	 if (inventory.containsKey(invItem)) {
	 Integer amount = inventory.get(invItem).intValue() + item.getAmount();
	 inventory.put(invItem, amount);
	 } else {
	 inventory.put(invItem, item.getAmount());
	 }
	 }*/
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

	public final void saveSellPrice() {
		ConfigurationSection sellconfig = shopConfig.getConfig().createSection("sellprices");

		for (ItemPrice ip : sellprices.values()) {
			sellconfig.set(ip.getId() + ".amount", ip.getAmount());
			sellconfig.set(ip.getId() + ".description", ip.getDescription());
			sellconfig.set(ip.getId() + ".itemStack", ip.getItemStack());
			sellconfig.set(ip.getId() + ".price", ip.getPrice());
			sellconfig.set(ip.getId() + ".random", ip.getId());
		}
	}

	public final void loadSellPrice() {
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
		}
	}

	public ConfigurationSection getInventoryConfig() {
		ConfigurationSection invconfig = shopConfig.getConfig().getConfigurationSection("inventory");
		if (invconfig == null) {
			invconfig = shopConfig.getConfig().createSection("inventory");
		}
		return invconfig;
	}

	public InventoryInterface getInventoryInterface() {
		return inv;
	}

	public final void saveInventory() {
		ConfigurationSection invconfig = getInventoryConfig().createSection("items");

		Integer count = 0;

		for (Entry<ItemStack, Integer> entry : inventory.entrySet()) {
			invconfig.set(count.toString() + ".itemstack", entry.getKey());
			invconfig.set(count.toString() + ".amount", entry.getValue());
			count++;
		}
	}

	public final void loadInventory() {
		ConfigurationSection invconfig = getInventoryConfig().getConfigurationSection("items");
		if (invconfig == null) {
			invconfig = getInventoryConfig().createSection("items");
		}
		for (String num : invconfig.getKeys(false)) {
			ItemStack item = invconfig.getItemStack(num + ".itemstack");
			Integer amount = invconfig.getInt(num + ".amount");
			getInventory().put(item, amount);
		}
	}

	public final void initWallet() {
		ConfigurationSection walletconfig = shopConfig.getConfig().getConfigurationSection("wallet");
		if (walletconfig == null) {
			walletconfig = shopConfig.getConfig().createSection("wallet");
			walletconfig.set("type", WalletType.SHOP.name());
		}

		String type = walletconfig.getString("type", "SHOP");

		WalletType walletType = WalletType.valueOf(type);

		wallet = walletType.getNewWallet(walletconfig);
	}

	public Wallet getWallet() {
		return wallet;
	}

	public void setWalletType(WalletType type) {
		ConfigurationSection walletconfig = shopConfig.getConfig().getConfigurationSection("wallet");
		if (walletconfig == null) {
			walletconfig = shopConfig.getConfig().createSection("wallet");
			walletconfig.set("type", WalletType.SHOP.name());
		}

		walletconfig.set("type", type.name());

		initWallet();
	}

	public WalletType getWalletType() {
		ConfigurationSection walletconfig = shopConfig.getConfig().getConfigurationSection("wallet");
		if (walletconfig == null) {
			walletconfig = shopConfig.getConfig().createSection("wallet");
			walletconfig.set("type", WalletType.SHOP.name());
		}
		return WalletType.valueOf(walletconfig.getString("type"));
	}

	public String getOwner() {
		return shopConfig.getConfig().getString("info.owner", "");
	}

	public void setOwner(Player player) {
		setOwner(player.getName());
	}

	public void setOwner(String value) {
		shopConfig.getConfig().set("info.owner", value);
	}

	public boolean isOwner(Player player) {
		return getOwner().equals(player.getName());
	}

	public String getName() {
		return shopConfig.getConfig().getString("info.name");
	}

	public void setName(String value) {
		shopConfig.getConfig().set("info.name", value);
	}

	public Integer getId() {
		return Integer.valueOf(shopConfig.getConfig().getInt("info.id"));
	}
	
	public void setId(int id) {
		shopConfig.getConfig().set("info.id", id);
	}
}
