package com.thedemgel.ultratrader.shop;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import com.thedemgel.ultratrader.BlockShopHandler;
import com.thedemgel.ultratrader.StoreConfig;
import com.thedemgel.ultratrader.UltraTrader;
import com.thedemgel.ultratrader.inventory.InventoryInterface;
import com.thedemgel.ultratrader.inventory.InventoryInterfaceHandler;
import com.thedemgel.ultratrader.util.ConfigValue;
import com.thedemgel.ultratrader.wallet.Wallet;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Shop {

    private ConcurrentMap<String, ItemPrice> priceList = new ConcurrentHashMap<>();
	private ConcurrentMap<ItemStack, Integer> inventory = new ConcurrentHashMap<>();
	private ConcurrentMap<String, ConfigValue> info = new ConcurrentHashMap<>();
	private ConcurrentMap<String, ConfigValue> walletinfo = new ConcurrentHashMap<>();
	private ConcurrentMap<String, ConfigValue> inventoryinfo = new ConcurrentHashMap<>();
    private List<Location> blockShops = new ArrayList<>();
    private Wallet wallet;
	private InventoryInterface inv;

    private ConcurrentMap<String, CategoryItem> categoryItems = new ConcurrentHashMap<>();

	public Shop(StoreConfig shopconfig) {
		//inv = new ShopInventoryInterface(this);
	}

	public void save(boolean async) {
		UltraTrader.getDbObj().save(this, async);
	}

	public ConcurrentMap<ItemStack, Integer> getInventory() {
		return inventory;
	}

    public ItemPrice getItemPrice(ItemStack checkItem) {
        String id = getItemId(checkItem);

        return getPriceList().get(id);
    }

	public boolean hasItem(final ItemPrice checkItem) {
		Predicate<ItemPrice> itemPredicate = new Predicate<ItemPrice>() {
			@Override
			public boolean apply(ItemPrice item) {
				return item.getItemStack().equals(checkItem.getItemStack());
			}
		};
		Collection<ItemPrice> buyItems = Collections2.filter(getPriceList().values(), itemPredicate);

		return buyItems.size() > 0;
	}

	public boolean addItem(ItemStack item, BigDecimal sellPrice, BigDecimal buyPrice, String description, String category) {
		ItemPrice invItem = new ItemPrice(item, sellPrice, buyPrice, description);

		if (hasItem(invItem)) {
			return false;
		}

		String random = invItem.setRandom();
		while (getPriceList().containsKey(random)) {
			random = invItem.setRandom();
		}

        invItem.setCategoryId(category);

		getPriceList().put(random, invItem);
		return true;
	}

	public void setMetaData() {
		// Decide whether to set metadata (not if item store) and then set it.
        if (!blockShops.isEmpty()) {
            Iterator<Location> blocks = blockShops.iterator();
            while (blocks.hasNext()) {
                Location loc = blocks.next();

                loc.getBlock().setMetadata(BlockShopHandler.SHOP_METADATA_KEY, new FixedMetadataValue(UltraTrader.getInstance(), getId()));
                loc.getBlock().setMetadata(BlockShopHandler.SHOP_OWNER_KEY, new FixedMetadataValue(UltraTrader.getInstance(), getOwner()));
            }
        }
	}

	public String getItemId(ItemStack item) {
        if (item == null) {
            return "000000";
        }

        if (item.hasItemMeta()) {
            if(!item.getItemMeta().hasLore()) {
                return "000000";
            }

            if (item.getItemMeta().getLore().size() == 0) {
                return "000000";
            }
        } else {
            return "000000";
        }

		String id = Iterables.getLast(item.getItemMeta().getLore());

        try {
		    id = id.substring(id.length() - 8);
        } catch (StringIndexOutOfBoundsException e) {
            return "000000";
        }

		return id;
	}

	public final void initWallet() {
		ConfigValue<String> walletType = getWalletinfo().get("type");
		setWallet(UltraTrader.getWallethandler().getWalletInstance(walletType.getValue(), this));
	}

	public Wallet getWallet() {
		return wallet;
	}

	public void setWalletType(String type) {
		walletinfo.put("type", new ConfigValue<>(type));
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
		ConfigValue<String> interfaceType = getInventoryinfo().get("type");
		setInventoryInterface(UltraTrader.getInventoryInterfaceHandler().getInventoryInterfaceInstance(interfaceType.getValue(), this));
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

	public boolean getCanRemote() {
		ConfigValue<Boolean> remote = info.get("remote");
		if (remote == null) {
			info.put("remote", new ConfigValue(false));
			return false;
		}
		return remote.getValue();
	}

	public void setCanRemote(boolean value) {
		ConfigValue<Boolean> remote = new ConfigValue(value);
		info.put("remote", remote);
	}

	public double getRemoteItemCost() {
		ConfigValue price = info.get("remoteprice");
		if (price == null) {
			price = new ConfigValue(50d);
			info.put("remoteprice", price);
		}

		if (price.getValue() instanceof Integer) {
			Integer cost = (Integer) price.getValue();
			return cost.doubleValue();
		} else {
			Double cost = (Double) price.getValue();
			return cost;
		}
	}

	public void setRemoteItemCost(double value) {
		ConfigValue<Double> price = new ConfigValue(value);
		info.put("remoteprice", price);
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

    public List<Location> getBlockShops() {
        return blockShops;
    }

    public ConcurrentMap<String, ItemPrice> getPriceList() {
        return priceList;
    }

    public ConcurrentMap<String, CategoryItem> getCategoryItem() {
        return categoryItems;
    }

    public Collection<ItemPrice> getItemsInCategory(final String category) {

        Predicate<ItemPrice> itemPricePredicate = new Predicate<ItemPrice>() {
            @Override
            public boolean apply(@Nullable ItemPrice itemPrice) {
                return itemPrice.getCategoryId().equals(category);
            }
        };

        Collection<ItemPrice> items = Collections2.filter(priceList.values(), itemPricePredicate);

        return items;
    }
}
