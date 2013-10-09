package com.thedemgel.cititradersre.shop;

import com.thedemgel.cititradersre.InventoryHandler;
import com.thedemgel.cititradersre.L;
import com.thedemgel.cititradersre.inventory.AdminInventoryInterface;
import java.util.ArrayList;
import java.util.List;
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

public class ShopInventoryView extends InventoryView {

	private Inventory top;
	private Player player;
	private InventoryType type = InventoryType.CHEST;
	private boolean keepAlive = false;
	private Shop shop;
	private Status current = Status.NULL;
	private Conversation convo;

	public ShopInventoryView(Inventory invTop, Player player, Shop shop) {
		top = invTop;
		this.player = player;
		this.shop = shop;
		buildSellView();
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

	public final void buildSellView() {
		current = Status.MAIN_SCREEN;
		top.clear();
		boolean displayAdmin = shop.getInventoryInterface().displayItemToPlayer(player);
		for (ItemPrice item : getShop().getSellprices().values()) {
			int currentInvAmount = shop.getInventoryInterface().getInventoryStock(item);

			if (currentInvAmount > 0 || shop.isOwner(player)) {
				if (displayAdmin) {
					this.getTopInventory().addItem(item.generateLore(1, true, currentInvAmount));
				} else {
					this.getTopInventory().addItem(item.generateLore());
				}
			}
		}

		ItemStack toSell = new ItemStack(Material.ENDER_PEARL);
		ItemMeta toSellMeta = toSell.getItemMeta();
		List<String> toSellText = new ArrayList<>();
		toSellText.add(L.getString("inventory.tobuyscreen.lore"));
		toSellMeta.setLore(toSellText);
		toSellMeta.setDisplayName(L.getString("inventory.tobuyscreen.display"));
		toSell.setItemMeta(toSellMeta);
		this.setItem(InventoryHandler.INVENTORY_BACK_ARROW_SLOT, toSell);

		if (displayAdmin) {
			ItemStack doAdmin = new ItemStack(Material.BOOK_AND_QUILL);
			ItemMeta setPriceMeta = doAdmin.getItemMeta();
			List<String> doAdminText = new ArrayList<>();
			doAdminText.add(L.getString("inventory.admin.lore"));
			setPriceMeta.setLore(doAdminText);
			setPriceMeta.setDisplayName(L.getString("inventory.admin.display"));
			doAdmin.setItemMeta(setPriceMeta);
			this.setItem(InventoryHandler.INVENTORY_ADMIN_SLOT, doAdmin);
		}
	}

	public void buildBuyView() {
		current = Status.BUY_SCREEN;
		top.clear();

		boolean displayAdmin = shop.getInventoryInterface().displayItemToPlayer(player);
		for (ItemPrice item : getShop().getBuyprices().values()) {
			int currentInvAmount = shop.getInventoryInterface().getInventoryStock(item);

			if (displayAdmin) {
				this.getTopInventory().addItem(item.generateLore(1, true, currentInvAmount));
			} else {
				this.getTopInventory().addItem(item.generateLore());
			}
		}

		ItemStack toSell = new ItemStack(Material.SLIME_BALL);
		ItemMeta toSellMeta = toSell.getItemMeta();
		List<String> toSellText = new ArrayList<>();
		toSellText.add(L.getString("inventory.tosellscreen.lore"));
		toSellMeta.setLore(toSellText);
		toSellMeta.setDisplayName(L.getString("inventory.tosellscreen.display"));
		toSell.setItemMeta(toSellMeta);
		this.setItem(InventoryHandler.INVENTORY_BACK_ARROW_SLOT, toSell);

		if (displayAdmin) {
			ItemStack doAdmin = new ItemStack(Material.BOOK_AND_QUILL);
			ItemMeta setPriceMeta = doAdmin.getItemMeta();
			List<String> doAdminText = new ArrayList<>();
			doAdminText.add(L.getString("inventory.buyadmin.lore"));
			setPriceMeta.setLore(doAdminText);
			setPriceMeta.setDisplayName(L.getString("inventory.buyadmin.display"));
			doAdmin.setItemMeta(setPriceMeta);
			this.setItem(InventoryHandler.INVENTORY_ADMIN_SLOT, doAdmin);
		}
	}

	public void buildBuyItemView(ItemStack item) {
		current = Status.BUY_ITEM_SCREEN;
		top.clear();

		String id = getShop().getItemId(item);

		ItemPrice invItem;
		if (getShop().getBuyprices().containsKey(id)) {
			invItem = getShop().getBuyprices().get(id);
		} else {
			buildBuyView();
			return;
		}

		int invCount;

		invCount = shop.getInventoryInterface().getInventoryStock(invItem);

		if (invCount < 1 && !shop.getInventoryInterface().displayItemToPlayer(player)) {
			buildBuyView();
			return;
		}

		int max = AdminInventoryInterface.ADMIN_INVENTORY_STOCK;
		int count = 1;
		while (count <= max && count <= invCount) {
			top.addItem(invItem.generateLore(count));
			count = count * 2;
		}

		ItemStack arrow = new ItemStack(Material.ARROW);
		ItemMeta arrowMeta = arrow.getItemMeta();
		List<String> arrowText = new ArrayList<>();
		arrowText.add(L.getString("inventory.back.lore"));
		arrowMeta.setLore(arrowText);
		arrowMeta.setDisplayName(L.getString("inventory.back.display"));
		arrow.setItemMeta(arrowMeta);
		this.setItem(InventoryHandler.INVENTORY_BACK_ARROW_SLOT, arrow);

		if (shop.isOwner(player)) {
			ItemStack setPrice = new ItemStack(Material.BOOK_AND_QUILL);
			ItemMeta setPriceMeta = setPrice.getItemMeta();
			List<String> setPriceText = new ArrayList<>();
			setPriceText.add(L.getString("inventory.itemadmin.lore"));
			setPriceText.add(ChatColor.DARK_GRAY + id);
			setPriceMeta.setLore(setPriceText);
			setPriceMeta.setDisplayName(L.getString("inventory.itemadmin.display"));
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
				setRemoveStockText.add(L.getString("inventory.removestock.lore.full"));
			} else {
				setRemoveStockText.add(L.getString("inventory.removestock.lore.empty"));
			}
			setRemoveStockText.add(ChatColor.DARK_GRAY + id);
			setRemoveStockMeta.setLore(setRemoveStockText);
			setRemoveStockMeta.setDisplayName(L.getString("inventory.removestock.display"));
			removeStock.setItemMeta(setRemoveStockMeta);
			this.setItem(InventoryHandler.INVENTORY_TAKE_ALL_SLOT, removeStock);
		}
	}

	public void buildItemView(ItemStack item) {
		current = Status.SELL_SCREEN;
		top.clear();

		String id = getShop().getItemId(item);

		ItemPrice invItem;
		if (getShop().getSellprices().containsKey(id)) {
			invItem = getShop().getSellprices().get(id);
		} else {
			buildSellView();
			return;
		}

		int invCount;

		invCount = shop.getInventoryInterface().getInventoryStock(invItem);

		if (invCount < 1 && !shop.getInventoryInterface().displayItemToPlayer(player)) {
			buildSellView();
			return;
		}

		int max = invItem.getItemStack().getMaxStackSize();
		int count = 1;
		while (count <= max && count <= invCount) {
			top.addItem(invItem.generateLore(count));
			count = count * 2;
		}

		if ((count / 2) < invCount && (count / 2) < max) {
			top.addItem(invItem.generateLore(invCount));
		}

		ItemStack arrow = new ItemStack(Material.ARROW);
		ItemMeta arrowMeta = arrow.getItemMeta();
		List<String> arrowText = new ArrayList<>();
		arrowText.add(L.getString("inventory.back.lore"));
		arrowMeta.setLore(arrowText);
		arrowMeta.setDisplayName(L.getString("inventory.back.display"));
		arrow.setItemMeta(arrowMeta);
		this.setItem(InventoryHandler.INVENTORY_BACK_ARROW_SLOT, arrow);

		if (shop.isOwner(player)) {
			ItemStack setPrice = new ItemStack(Material.BOOK_AND_QUILL);
			ItemMeta setPriceMeta = setPrice.getItemMeta();
			List<String> setPriceText = new ArrayList<>();
			setPriceText.add(L.getString("inventory.itemadmin.lore"));
			setPriceText.add(ChatColor.DARK_GRAY + id);
			setPriceMeta.setLore(setPriceText);
			setPriceMeta.setDisplayName(L.getString("inventory.itemadmin.display"));
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
				setRemoveStockText.add(L.getString("inventory.removestock.lore.full"));
			} else {
				setRemoveStockText.add(L.getString("inventory.removestock.lore.empty"));
			}
			setRemoveStockText.add(ChatColor.DARK_GRAY + id);
			setRemoveStockMeta.setLore(setRemoveStockText);
			setRemoveStockMeta.setDisplayName(L.getString("inventory.removestock.display"));
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
}
