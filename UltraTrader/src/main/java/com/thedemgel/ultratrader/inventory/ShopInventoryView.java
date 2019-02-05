package com.thedemgel.ultratrader.inventory;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.thedemgel.ultratrader.InventoryHandler;
import com.thedemgel.ultratrader.Lang;
import com.thedemgel.ultratrader.UltraTrader;
import com.thedemgel.ultratrader.shop.*;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.conversations.Conversation;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ShopInventoryView extends InventoryView {

	private final Inventory top;
	private final Player player;
	private final InventoryType type = InventoryType.CHEST;
	private boolean keepAlive = false;
	private final Shop shop;
	private Status current = Status.NULL;
	private Conversation convo;
	private Object target;

    // The id of the currently displayed Category or null for main category display
    private String category = null;

	public ShopInventoryView(Inventory invTop, Player player, Shop shop) {
		top = invTop;
		this.player = player;
		this.shop = shop;
		buildCategoryView();
	}

	public Status getStatus() {
		return current;
	}

	@Override
	public Inventory getTopInventory() {
		return top;
	}

	@Override
	public HumanEntity getPlayer() {
		return player;
	}

	@Override
	public InventoryType getType() {
		return type;
	}

	@Override
	public Inventory getBottomInventory() {
		return getPlayer().getInventory();
	}

	public final void refreshView() {
		switch (current) {
			case CATEGORY_SCREEN:
				buildCategoryView();
				break;
            case ITEM_SCREEN:
                buildCategoryItemView();
                break;
		}
	}

    public final void buildCategoryView() {
        current = Status.CATEGORY_SCREEN;

        top.clear();

        boolean displayAdmin = shop.getInventoryInterface().displayItemToPlayer(player);

        if (shop.getCategoryItem().size() == 1 && !shop.getOwner().equals(player.getName())) {
            CategoryItem item = shop.getCategoryItem().values().iterator().next();

            category = item.getCategoryId();

            buildCategoryItemView();
            return;
        }

        List<CategoryItem> itemQueue = new ArrayList<>();

        for (CategoryItem item : shop.getCategoryItem().values()) {
            if (item.getSlot() == -1) {
                itemQueue.add(item);
            } else {
                ItemStack slot = getTopInventory().getItem(item.getSlot());

                if (slot != null) {
                    itemQueue.add(item);
                } else {
                    getTopInventory().setItem(item.getSlot(), item);
                }
            }
        }

        for (CategoryItem item : itemQueue) {
            int slot = getTopInventory().firstEmpty();
            item.setSlot(slot);
            this.getTopInventory().setItem(item.getSlot(), item);
        }

        // Check if limits allow for remote access...
        if (shop.getCanRemote()) {
            ItemStack doItemShop = new ItemStack(StoreItem.STORE_ITEM_MATERIAL);
            StoreItem.linkToShop(shop, doItemShop);
            ItemMeta meta = doItemShop.getItemMeta();
            List<String> lore = meta.getLore();
            lore.set(0, ChatColor.AQUA + "Click to buy Remote Shop Item");
            lore.add(ChatColor.YELLOW + "Price: " + UltraTrader.getEconomy().format(shop.getRemoteItemCost()));
            meta.setLore(lore);
            meta.setDisplayName("Remote Store Item");
            doItemShop.setItemMeta(meta);
            this.setItem(InventoryHandler.INVENTORY_CREATE_ITEM_SLOT, doItemShop);
        }

        if (displayAdmin) {
            ItemStack doAdmin = new ItemStack(Material.WRITABLE_BOOK);
            ItemMeta setPriceMeta = doAdmin.getItemMeta();
            List<String> doAdminText = new ArrayList<>();
            // TODO: add to language
            doAdminText.add(Lang.getString("inventory.admin.lore"));
            setPriceMeta.setLore(doAdminText);
            setPriceMeta.setDisplayName(Lang.getString("inventory.admin.display"));
            doAdmin.setItemMeta(setPriceMeta);
            this.setItem(InventoryHandler.INVENTORY_ADMIN_SLOT, doAdmin);

            ItemStack doArrange = new ItemStack(Material.BOOKSHELF);
            ItemMeta setArrangeMeta = doArrange.getItemMeta();
            List<String> doArrangeText = new ArrayList<>();
            doArrangeText.add(Lang.getString("inventory.arrange.lore"));
            setArrangeMeta.setLore(doArrangeText);
            setArrangeMeta.setDisplayName(Lang.getString("inventory.arrange.display"));
            doArrange.setItemMeta(setArrangeMeta);
            this.setItem(InventoryHandler.INVENTORY_ARRANGE_SLOT, doArrange);
        }
    }

	public final void buildCategoryItemView() {
		current = Status.ITEM_SCREEN;

		top.clear();
		boolean displayAdmin = shop.getInventoryInterface().displayItemToPlayer(player);
		List<ItemPrice> itemQueue = new ArrayList<>();

        Collection<ItemPrice> items = shop.getItemsInCategory(category);

		for (ItemPrice item : items) {
			int currentInvAmount = shop.getInventoryInterface().getInventoryStock(item);

			if (!item.getSellPrice().equals(BigDecimal.valueOf(-1)) || !item.getBuyPrice().equals(BigDecimal.valueOf(-1)) || shop.isOwner(player)) {
				if (displayAdmin) {
					if (item.getSlot() == -1) {
						itemQueue.add(item);
					} else {
						ItemStack slot = getTopInventory().getItem(item.getSlot());

						if (slot != null) {
							itemQueue.add(item);
						} else {
							getTopInventory().setItem(item.getSlot(), item.generateLore(1, true, currentInvAmount, current));
						}
					}
				} else {
					if (item.getSlot() == -1) {
						itemQueue.add(item);
					} else {
						ItemStack slot = getTopInventory().getItem(item.getSlot());

						if (slot != null) {
							itemQueue.add(item);
						} else {
							this.getTopInventory().setItem(item.getSlot(), item.generateLore(current));
						}
					}
				}
			}
		}

		for (ItemPrice item : itemQueue) {
			int currentInvAmount = shop.getInventoryInterface().getInventoryStock(item);

			if (!item.getSellPrice().equals(BigDecimal.valueOf(-1)) || !item.getBuyPrice().equals(BigDecimal.valueOf(-1)) || shop.isOwner(player)) {
				if (displayAdmin) {
					int slot = getTopInventory().firstEmpty();
					item.setSlot(slot);
					this.getTopInventory().setItem(item.getSlot(), item.generateLore(1, true, currentInvAmount, current));
				} else {
					int slot = getTopInventory().firstEmpty();
					item.setSlot(slot);
					this.getTopInventory().setItem(item.getSlot(), item.generateLore(current));
				}
			}
		}

        if (shop.getCategoryItem().values().size() > 1 || shop.isOwner(player)) {
		    ItemStack toSell = new ItemStack(Material.ARROW);
		    ItemMeta toSellMeta = toSell.getItemMeta();
		    List<String> toSellText = new ArrayList<>();
		    toSellText.add(Lang.getString("inventory.tocategoryscreen.lore"));
		    toSellMeta.setLore(toSellText);
		    toSellMeta.setDisplayName(Lang.getString("inventory.tocategoryscreen.display"));
		    toSell.setItemMeta(toSellMeta);
		    this.setItem(InventoryHandler.INVENTORY_BACK_ARROW_SLOT, toSell);
        }

		if (displayAdmin) {
			ItemStack doArrange = new ItemStack(Material.BOOKSHELF);
			ItemMeta setArrangeMeta = doArrange.getItemMeta();
			List<String> doArrangeText = new ArrayList<>();
			doArrangeText.add(Lang.getString("inventory.arrange.lore"));
			setArrangeMeta.setLore(doArrangeText);
			setArrangeMeta.setDisplayName(Lang.getString("inventory.arrange.display"));
			doArrange.setItemMeta(setArrangeMeta);
			this.setItem(InventoryHandler.INVENTORY_ARRANGE_SLOT, doArrange);
		}

		// Check if limits allow for remote access...
		if (shop.getCanRemote()) {
			ItemStack doItemShop = new ItemStack(StoreItem.STORE_ITEM_MATERIAL);
			StoreItem.linkToShop(shop, doItemShop);
			ItemMeta meta = doItemShop.getItemMeta();
			List<String> lore = meta.getLore();
			lore.set(0, ChatColor.AQUA + "Click to buy Remote Shop Item");
			lore.add(ChatColor.YELLOW + "Price: " + UltraTrader.getEconomy().format(shop.getRemoteItemCost()));
			meta.setLore(lore);
			meta.setDisplayName("Remote Store Item");
			doItemShop.setItemMeta(meta);
			this.setItem(InventoryHandler.INVENTORY_CREATE_ITEM_SLOT, doItemShop);
		}
	}

	public void buildBuyItemView(ItemStack item) {
		current = Status.BUY_ITEM_SCREEN;
		top.clear();

		String id = getShop().getItemId(item);

		ItemPrice invItem;
		if (getShop().getPriceList().containsKey(id)) {
			invItem = getShop().getPriceList().get(id);
		} else {
			buildCategoryItemView();
			return;
		}

        if (invItem.getBuyPrice().doubleValue() < 0 && !shop.isOwner(player)) {
            buildCategoryItemView();
            return;
        }

		int invCount = shop.getInventoryInterface().getInventoryStock(invItem);

        Map<Integer, ? extends ItemStack> itemStackHashMap = player.getInventory().all(invItem.getItemStack().getType());

        int max = 0;

        for (ItemStack stack : itemStackHashMap.values()) {
            if (stack.isSimilar(invItem.getItemStack())) {
                max += stack.getAmount();
            }
        }

		int count = 1;

		while (count <= max && count <= 64) {
            top.addItem(invItem.generateLore(count, current));
			count = count * 2;
		}

        if (max < 64 && (count / 2) < max) {
            top.addItem(invItem.generateLore(max, current));
        }

		ItemStack arrow = new ItemStack(Material.ARROW);
		ItemMeta arrowMeta = arrow.getItemMeta();
		List<String> arrowText = new ArrayList<>();
		arrowText.add(Lang.getString("inventory.back.lore"));
		arrowMeta.setLore(arrowText);
		arrowMeta.setDisplayName(Lang.getString("inventory.back.display"));
		arrow.setItemMeta(arrowMeta);
		this.setItem(InventoryHandler.INVENTORY_BACK_ARROW_SLOT, arrow);

		if (shop.isOwner(player)) {
			ItemStack setPrice = new ItemStack(Material.WRITABLE_BOOK);
			ItemMeta setPriceMeta = setPrice.getItemMeta();
			List<String> setPriceText = new ArrayList<>();
			setPriceText.add(Lang.getString("inventory.itemadmin.lore"));
			setPriceText.add(ChatColor.DARK_GRAY + id);
			setPriceMeta.setLore(setPriceText);
			setPriceMeta.setDisplayName(Lang.getString("inventory.itemadmin.display"));
			setPrice.setItemMeta(setPriceMeta);
			this.setItem(InventoryHandler.INVENTORY_ADMIN_SLOT, setPrice);

			ItemStack removeStock;
			if (invCount > 0) {
				removeStock = new ItemStack(Material.WATER_BUCKET);
			} else {
				removeStock = new ItemStack(Material.BUCKET);
			}
			ItemMeta setRemoveStockMeta = removeStock.getItemMeta();
			List<String> setRemoveStockText = new ArrayList<>();
			if (invCount > 0) {
				setRemoveStockText.add(Lang.getString("inventory.removestock.lore.full"));
			} else {
				setRemoveStockText.add(Lang.getString("inventory.removestock.lore.empty"));
			}
			setRemoveStockText.add(ChatColor.DARK_GRAY + id);
			setRemoveStockMeta.setLore(setRemoveStockText);
			setRemoveStockMeta.setDisplayName(Lang.getString("inventory.removestock.display"));
			removeStock.setItemMeta(setRemoveStockMeta);
			this.setItem(InventoryHandler.INVENTORY_TAKE_ALL_SLOT, removeStock);
		}
	}

	public void buildItemView(ItemStack item) {
		current = Status.SELL_ITEM_SCREEN;
		top.clear();

		String id = getShop().getItemId(item);

		ItemPrice invItem;
		if (getShop().getPriceList().containsKey(id)) {
			invItem = getShop().getPriceList().get(id);
		} else {
			buildCategoryItemView();
			return;
		}

        if (invItem.getSellPrice().doubleValue() < 0 && !shop.isOwner(player)) {
            buildCategoryItemView();
            return;
        }

		int invCount;

		invCount = shop.getInventoryInterface().getInventoryStock(invItem);

		if (invCount < 1 && !shop.getInventoryInterface().displayItemToPlayer(player)) {
			buildCategoryItemView();
			return;
		}

		int max = invItem.getItemStack().getMaxStackSize();
		int count = 1;
		while (count <= max && count <= invCount) {
			top.addItem(invItem.generateLore(count, current));
			count = count * 2;
		}

		if ((count / 2) < invCount && (count / 2) < max) {
			top.addItem(invItem.generateLore(invCount, current));
		}

		ItemStack arrow = new ItemStack(Material.ARROW);
		ItemMeta arrowMeta = arrow.getItemMeta();
		List<String> arrowText = new ArrayList<>();
		arrowText.add(Lang.getString("inventory.back.lore"));
		arrowMeta.setLore(arrowText);
		arrowMeta.setDisplayName(Lang.getString("inventory.back.display"));
		arrow.setItemMeta(arrowMeta);
		this.setItem(InventoryHandler.INVENTORY_BACK_ARROW_SLOT, arrow);

		if (shop.isOwner(player)) {
			ItemStack setPrice = new ItemStack(Material.WRITABLE_BOOK);
			ItemMeta setPriceMeta = setPrice.getItemMeta();
			List<String> setPriceText = new ArrayList<>();
			setPriceText.add(Lang.getString("inventory.itemadmin.lore"));
			setPriceText.add(ChatColor.DARK_GRAY + id);
			setPriceMeta.setLore(setPriceText);
			setPriceMeta.setDisplayName(Lang.getString("inventory.itemadmin.display"));
			setPrice.setItemMeta(setPriceMeta);
			this.setItem(InventoryHandler.INVENTORY_ADMIN_SLOT, setPrice);

			ItemStack removeStock;
			if (invCount > 0) {
				removeStock = new ItemStack(Material.WATER_BUCKET);
			} else {
				removeStock = new ItemStack(Material.BUCKET);
			}
			ItemMeta setRemoveStockMeta = removeStock.getItemMeta();
			List<String> setRemoveStockText = new ArrayList<>();
			if (invCount > 0) {
				setRemoveStockText.add(Lang.getString("inventory.removestock.lore.full"));
			} else {
				setRemoveStockText.add(Lang.getString("inventory.removestock.lore.empty"));
			}
			setRemoveStockText.add(ChatColor.DARK_GRAY + id);
			setRemoveStockMeta.setLore(setRemoveStockText);
			setRemoveStockMeta.setDisplayName(Lang.getString("inventory.removestock.display"));
			removeStock.setItemMeta(setRemoveStockMeta);
			this.setItem(InventoryHandler.INVENTORY_TAKE_ALL_SLOT, removeStock);
		}
	}

	public Shop getShop() {
		return shop;
	}

	public boolean isKeepAlive() {
		return keepAlive;
	}

	public void setKeepAlive(boolean keepAlive) {
		this.keepAlive = keepAlive;
	}

	public Conversation getConvo() {
		return convo;
	}

	public void setConvo(Conversation value) {
		convo = value;
	}

	public Object getTarget() {
		return target;
	}

	public void setTarget(Object value) {
		target = value;
	}

    public void setCategory(final int slot) {
        Predicate<CategoryItem> categoryItemPredicate = new Predicate<CategoryItem>() {
            @Override
            public boolean apply(CategoryItem categoryItem) {
                return categoryItem.getSlot() == slot;
            }
        };

        Collection<CategoryItem> categoryItems = Collections2.filter(shop.getCategoryItem().values(), categoryItemPredicate);

        if (categoryItems.size() > 0) {
            category = categoryItems.iterator().next().getCategoryId();
        }
    }

    public void setCategory(String id) {
        category = id;
    }

    public String getCategory() {
        return category;
    }
}
