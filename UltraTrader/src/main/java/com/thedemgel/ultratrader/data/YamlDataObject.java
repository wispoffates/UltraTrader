package com.thedemgel.ultratrader.data;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.thedemgel.ultratrader.L;
import com.thedemgel.ultratrader.StoreConfig;
import com.thedemgel.ultratrader.UltraTrader;
import com.thedemgel.ultratrader.inventory.InventoryInterfaceHandler;
import com.thedemgel.ultratrader.shop.CategoryItem;
import com.thedemgel.ultratrader.shop.ItemPrice;
import com.thedemgel.ultratrader.shop.Shop;
import com.thedemgel.ultratrader.util.ConfigValue;
import com.thedemgel.ultratrader.util.ShopAction;
import com.thedemgel.ultratrader.util.YamlFilenameFilter;
import com.thedemgel.ultratrader.wallet.WalletHandler;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.milkbowl.vault.economy.EconomyResponse;

public class YamlDataObject extends DataObject {

	private File STORE_DIR = new File(UltraTrader.getInstance().getDataFolder() + File.separator + UltraTrader.STORE_DIR + File.separator);

	public YamlDataObject() {
		STORE_DIR.mkdirs();
	}

	@Override
	public void save(Shop shop, boolean async) {
        if (async) {
		    getPool().execute(new SaveShop(shop));
        } else {
            SaveShop save = new SaveShop(shop);
            save.run();
        }
	}

    @Override
    public void removeShopFile(int shopId) {
        File file = new File(STORE_DIR, shopId + ".yml");

        if (!file.exists()) {
            return;
        }

        file.delete();
    }

	@Override
	public void load(int shopid) {
		File loadFile = new File(STORE_DIR, shopid + ".yml");
		StoreConfig config = new StoreConfig(UltraTrader.getInstance(), loadFile);

		Shop shop = new Shop();

		// Find or create Inventory Section
		ConfigurationSection invconfig = config.getConfig().getConfigurationSection("inventory");
		if (invconfig == null) {
			invconfig = config.getConfig().createSection("inventory");
		}

		// Load the Inventory Interface
		// Find or create inventory interface "interface" Configuration Section
		ConfigurationSection interfaceconfig = invconfig.getConfigurationSection("info");
		if (interfaceconfig == null) {
			interfaceconfig = invconfig.createSection("info");
		}

		for (String key : interfaceconfig.getKeys(false)) {
			shop.getInventoryinfo().put(key, new ConfigValue(interfaceconfig.get(key)));
		}

		if (!shop.getInventoryinfo().containsKey("type")) {
			System.out.println("Setting to Default Inventory Interface!!!!!!");
			shop.getInventoryinfo().put("type", new ConfigValue(InventoryInterfaceHandler.DEFAULT_INVENTORY_TYPE));
		}

		ConfigValue<String> inventorytype = shop.getInventoryinfo().get("type");

		shop.setInventoryInterface(UltraTrader.getInventoryInterfaceHandler().getInventoryInterfaceInstance(inventorytype.getValue(), shop));

		// Find or create Inventory Item Section
		ConfigurationSection invitemconfig = invconfig.getConfigurationSection("items");
		if (invitemconfig == null) {
			invitemconfig = invconfig.createSection("items");
		}

		// Load all the items
		for (String num : invitemconfig.getKeys(false)) {
			ItemStack item = invitemconfig.getItemStack(num + ".itemstack");
			Integer amount = invitemconfig.getInt(num + ".amount");
			shop.getInventory().put(item, amount);
		}

        ConfigurationSection categoryConfigSection = config.getConfig().getConfigurationSection("category");
        if (categoryConfigSection == null) {
            categoryConfigSection = config.getConfig().createSection("category");
        }

        for (String id : categoryConfigSection.getKeys(false)) {
            ConfigurationSection catConfig = categoryConfigSection.getConfigurationSection(id);
            //CategoryItem categoryItem = new CategoryItem();
            CategoryItem categoryItem = new CategoryItem((CategoryItem) catConfig.get("itemStack"));

            categoryItem.setItemStack(((CategoryItem) catConfig.get("itemStack")).getType());
            categoryItem.setCategoryId(catConfig.getString("id"));
            categoryItem.setSlot(catConfig.getInt("slot"));
            categoryItem.setLore(catConfig.getStringList("lore"));

            shop.getCategoryItem().put(categoryItem.getCategoryId(), categoryItem);
        }

        ConfigurationSection itemPricesSection = config.getConfig().getConfigurationSection("priceList");
        if (itemPricesSection == null) {
            itemPricesSection = config.getConfig().createSection("priceList");
        }

        // Load all sell prices
        for (String id : itemPricesSection.getKeys(false)) {
            ConfigurationSection itemConfig = itemPricesSection.getConfigurationSection(id);
            ItemPrice itemPrice = new ItemPrice();

            itemPrice.setDescription(itemConfig.getString("description"));
            itemPrice.setCategoryId(itemConfig.getString("category"));
            itemPrice.setItemStack(itemConfig.getItemStack("itemStack"));
            itemPrice.setSellPrice(BigDecimal.valueOf(itemConfig.getDouble("sellPrice")));
            itemPrice.setBuyPrice(BigDecimal.valueOf(itemConfig.getDouble("buyPrice")));
            itemPrice.setRandom(itemConfig.getString("random"));
            itemPrice.setSlot(itemConfig.getInt("slot"));

            shop.getPriceList().put(itemPrice.getId(), itemPrice);
        }

		ConfigurationSection sellConfig = config.getConfig().getConfigurationSection("sellprices");
		if (sellConfig != null) {
		    // Load all sell prices
		    for (String id : sellConfig.getKeys(false)) {
			    ConfigurationSection itemConfig = sellConfig.getConfigurationSection(id);
			    ItemPrice item = new ItemPrice();

			    item.setDescription(itemConfig.getString("description"));
			    item.setItemStack(itemConfig.getItemStack("itemStack"));
			    item.setSellPrice(BigDecimal.valueOf(itemConfig.getDouble("price")));
			    item.setRandom(itemConfig.getString("random"));
			    item.setSlot(itemConfig.getInt("slot"));

                ItemPrice itemPrice = shop.getItemPrice(item.getItemStack());

                if (itemPrice == null) {
                    item.setCategoryId(CategoryItem.DEFAULT_CAT_SELL);
			        shop.getPriceList().put(item.getId(), item);

                    if (!shop.getCategoryItem().containsKey(CategoryItem.DEFAULT_CAT_SELL)) {
                        CategoryItem categoryItem = new CategoryItem(new ItemStack(Material.GOLD_INGOT));
                        categoryItem.setDisplayName("Default Category (Old Sell Items)");
                        categoryItem.setCategoryId(CategoryItem.DEFAULT_CAT_SELL);

                        shop.getCategoryItem().put(categoryItem.getCategoryId(), categoryItem);
                    }
                } else if (itemPrice.getSellPrice().equals(BigDecimal.ZERO)){
                    itemPrice.setSellPrice(item.getSellPrice());
                }
		    }
        }

		// Load all BuyPrices
		// Find or create configuration section
		ConfigurationSection buyconfig = config.getConfig().getConfigurationSection("buyprices");
		if (buyconfig != null) {

    		// Load all buy prices
		    for (String id : buyconfig.getKeys(false)) {
			    ConfigurationSection itemConfig = buyconfig.getConfigurationSection(id);
			    ItemPrice item = new ItemPrice();

			    item.setDescription(itemConfig.getString("description"));
			    item.setItemStack(itemConfig.getItemStack("itemStack"));
			    item.setBuyPrice(BigDecimal.valueOf(itemConfig.getDouble("price")));
			    item.setRandom(itemConfig.getString("random"));
			    item.setSlot(itemConfig.getInt("slot"));
			    //shop.getBuyPrices().put(item.getId(), item);

                ItemPrice itemPrice = shop.getItemPrice(item.getItemStack());
                if (itemPrice == null) {
                    item.setCategoryId(CategoryItem.DEFAULT_CAT_BUY);
                    shop.getPriceList().put(item.getId(), item);

                    if (!shop.getCategoryItem().containsKey(CategoryItem.DEFAULT_CAT_BUY)) {
                        CategoryItem categoryItem = new CategoryItem(new ItemStack(Material.IRON_INGOT));
                        categoryItem.setDisplayName("Default Category (Old Buy Items)");
                        categoryItem.setCategoryId(CategoryItem.DEFAULT_CAT_BUY);

                        shop.getCategoryItem().put(categoryItem.getCategoryId(), categoryItem);
                    }
                } else if (itemPrice.getBuyPrice().equals(BigDecimal.ZERO)){
                    itemPrice.setBuyPrice(item.getBuyPrice());
				}
				System.out.println(itemPrice.getBuyPrice());
		    }
        }

		// Load the Wallet
		// Find or create wallet Configuration Section
		ConfigurationSection walletconfig = config.getConfig().getConfigurationSection("wallet");
		if (walletconfig == null) {
			walletconfig = config.getConfig().createSection("wallet");
		}

		for (String key : walletconfig.getKeys(false)) {
			shop.getWalletinfo().put(key, new ConfigValue(walletconfig.get(key)));
		}

		if (!shop.getWalletinfo().containsKey("type")) {
			shop.getWalletinfo().put("type", new ConfigValue(WalletHandler.DEFAULT_WALLET_TYPE));
		}

		ConfigValue<String> wallettype = shop.getWalletinfo().get("type");

		shop.setWallet(UltraTrader.getWallethandler().getWalletInstance(wallettype.getValue(), shop));

		//Load the Info section
		// Find or create info Configuration Section
		ConfigurationSection infoconfig = config.getConfig().getConfigurationSection("info");
		if (infoconfig == null) {
			infoconfig = config.getConfig().createSection("info");
		}

		for (String key : infoconfig.getKeys(false)) {
			shop.getInfo().put(key, new ConfigValue(infoconfig.get(key)));
		}

        ConfigurationSection blockConfig = config.getConfig().getConfigurationSection("blocks");
        if (blockConfig == null) {
            blockConfig = config.getConfig().createSection("blocks");
        }

        ConfigurationSection blockLoc = blockConfig.getConfigurationSection("blocklocation");

        if (blockLoc == null) {
            blockLoc = blockConfig.createSection("blocklocation");
        }

        Set<String> keys = blockLoc.getKeys(false);

        for (String key : keys) {
            int x = blockLoc.getInt(key + ".x");
            int y = blockLoc.getInt(key + ".y");
            int z = blockLoc.getInt(key + ".z");
            String world = blockLoc.getString(key + ".world");

            World world1 = Bukkit.getWorld(world);
            Location loc = new Location(world1, x, y, z);

            shop.getBlockShops().add(loc);
        }

		UltraTrader.getStoreHandler().addShop(shop);
	}

	@Override
	public void initShops() {
		for (int id : getShopIds()) {
            try {
			    load(id);
            } catch (Exception e) {
                System.out.println("Store could not be loaded (" + e.getMessage() + ")");
            }
		}
	}

	public List<Integer> getShopIds() {
		List<Integer> ids = new ArrayList<>();
		File[] files = STORE_DIR.listFiles(new YamlFilenameFilter());

		for (File file : files) {
			String id = file.getName().replace(".yml", "");
			ids.add(Integer.parseInt(id));
		}

		return ids;
	}

	@Override
	public void initLogger(UltraTrader plugin) {
	}

	@Override
	public void doLog(Shop shop, Player player, EconomyResponse resp, ShopAction action, String message) {
	}

	private class SaveShop implements Runnable {

		private StoreConfig config;
		private Shop shop;

		public SaveShop(Shop shop) {
			config = new StoreConfig(UltraTrader.getInstance(), new File(STORE_DIR, shop.getId() + ".yml"));
			this.shop = shop;
		}

		@Override
		public void run() {
            try {
                ConfigurationSection catSection = config.getConfig().createSection("category");

                for (CategoryItem categoryItem : shop.getCategoryItem().values()) {
                    catSection.set(categoryItem.getCategoryId() + ".id", categoryItem.getCategoryId());
                    catSection.set(categoryItem.getCategoryId() + ".itemStack", categoryItem);
                    catSection.set(categoryItem.getCategoryId() + ".slot", categoryItem.getSlot());
                    catSection.set(categoryItem.getCategoryId() + ".lore", categoryItem.getLore());
                }

                ConfigurationSection priceConfig = config.getConfig().createSection("priceList");

                for (ItemPrice priceList : shop.getPriceList().values()) {
                    priceConfig.set(priceList.getId() + ".description", priceList.getDescription());
                    priceConfig.set(priceList.getId() + ".category", priceList.getCategoryId());
                    priceConfig.set(priceList.getId() + ".itemStack", priceList.getItemStack());
                    priceConfig.set(priceList.getId() + ".sellPrice", priceList.getSellPrice());
                    priceConfig.set(priceList.getId() + ".buyPrice", priceList.getBuyPrice());
                    priceConfig.set(priceList.getId() + ".random", priceList.getId());
                    priceConfig.set(priceList.getId() + ".slot", priceList.getSlot());

                }

			    ConfigurationSection invConfig = config.getConfig().getConfigurationSection("inventory");
			    if (invConfig == null) {
				    invConfig = config.getConfig().createSection("inventory");
			    }

			    // Save Inventory INFO
			    ConfigurationSection inventoryInfoConfig = invConfig.getConfigurationSection("info");
			    if (inventoryInfoConfig == null) {
				    inventoryInfoConfig = invConfig.createSection("info");
				    ConfigValue<String> defaultinventory = new ConfigValue<String>(InventoryInterfaceHandler.DEFAULT_INVENTORY_TYPE);
				    inventoryInfoConfig.set("type", defaultinventory.getValue());
			    }

			    for (Entry<String, ConfigValue> inventoryinfo : shop.getInventoryinfo().entrySet()) {
				    inventoryInfoConfig.set(inventoryinfo.getKey(), inventoryinfo.getValue().getValue());
			    }

			    // Save Inventory items
			    ConfigurationSection invconf = invConfig.createSection("items");

			    Integer count = 0;

			    for (Map.Entry<ItemStack, Integer> entry : shop.getInventory().entrySet()) {
				    invconf.set(count.toString() + ".itemstack", entry.getKey());
				    invconf.set(count.toString() + ".amount", entry.getValue());
				    count++;
			    }

			    // Get wallet information
			    ConfigurationSection walletconfig = config.getConfig().getConfigurationSection("wallet");
			    if (walletconfig == null) {
				    walletconfig = config.getConfig().createSection("wallet");
				    walletconfig.set("type", WalletHandler.DEFAULT_WALLET_TYPE);
			    }

			    for (Entry<String, ConfigValue> walletinfo : shop.getWalletinfo().entrySet()) {
				    walletconfig.set(walletinfo.getKey(), walletinfo.getValue().getValue());
			    }

			    ConfigurationSection infoconfig = config.getConfig().getConfigurationSection("info");
			    if (infoconfig == null) {
				    infoconfig = config.getConfig().createSection("info");
				    //ConfigValue<String> defaultname = new ConfigValue(L.getString("general.newshopname"));
				    infoconfig.set("name", new ConfigValue(L.getString("general.newshopname")));
			    }

			    for (Entry<String, ConfigValue> info : shop.getInfo().entrySet()) {
				    infoconfig.set(info.getKey(), info.getValue().getValue());
			    }

			    ConfigurationSection blockConfig = config.getConfig().getConfigurationSection("blocks");
                if (blockConfig == null) {
                    blockConfig = config.getConfig().createSection("blocks");
                }

                ConfigurationSection blocks = blockConfig.createSection("blocklocation");

                int i = 0;
                for (Location loc : shop.getBlockShops()) {
                    blocks.set(i + ".world", loc.getWorld().getName());
                    blocks.set(i + ".x", loc.getBlockX());
                    blocks.set(i + ".y", loc.getBlockY());
                    blocks.set(i + ".z", loc.getBlockZ());
                    i++;
                }

			    config.saveConfig();
            } catch (Exception e) {
                System.out.println("Store Config cannot be saved (" + e.getMessage() + ")");
                e.printStackTrace();
            }
		}
	}
}
