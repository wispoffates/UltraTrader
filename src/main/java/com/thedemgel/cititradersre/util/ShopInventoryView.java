package com.thedemgel.cititradersre.util;

import com.thedemgel.cititradersre.shop.ItemPrice;
import com.thedemgel.cititradersre.shop.Shop;
import java.util.ArrayList;
import java.util.List;
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
	private boolean open = true;
	private boolean keepAlive = false;
	private List<Integer> reservedSlot = new ArrayList<>();
	private Shop shop;
	public Conversation convo;
	private Status current = Status.NULL;

	public ShopInventoryView(Inventory invTop, Inventory invBottom, Player player, Shop shop) {
		top = invTop;
		this.player = player;
		this.shop = shop;
		buildView();
	}

	public Status getStatus() {
		return current;
	}

	public void setOpen() {
		open = false;
	}

	public boolean isOpen() {
		return open;
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

	public List<Integer> getReservedSlots() {
		return reservedSlot;
	}

	public final void buildView() {
		current = Status.MAIN_SCREEN;
		top.clear();
		for (ItemPrice item : getShop().getSellprices().values()) {
			Integer currentInvAmount = getShop().getInventory().get(item.getItemStack());
			if (currentInvAmount > 0 || shop.getOwner().equals(player.getName())) {
				this.getTopInventory().addItem(item.generateLore());
			}
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
			buildView();
			return;
		}

		Integer invCount = shop.getInventory().get(invItem.getItemStack());

		if (invCount < 1 && !shop.getOwner().equals(player.getName())) {
			buildView();
			return;
		}

		Integer max = invItem.getItemStack().getMaxStackSize();
		Integer count = 1;
		while (count <= max && count <= invCount) {
			//System.out.println("Count: " + count);
			top.addItem(invItem.generateLore(count));
			count = count * 2;
		}

		if ((count / 2) < invCount && (count / 2) < max) {
			//System.out.println("Adding last full stack");
			top.addItem(invItem.generateLore(invCount));
		}

		ItemStack arrow = new ItemStack(Material.ARROW, 1);
		ItemMeta arrowMeta = arrow.getItemMeta();
		List<String> arrowText = new ArrayList<>();
		arrowText.add("Click to go Back");
		arrowMeta.setLore(arrowText);
		arrowMeta.setDisplayName("Back");
		arrow.setItemMeta(arrowMeta);
		this.setItem(45, arrow);

		if (shop.getOwner().equals(player.getName())) {
			ItemStack setPrice = item.clone();
			setPrice.setAmount(1);
			ItemMeta setPriceMeta = setPrice.getItemMeta();
			List<String> setPriceText = new ArrayList<>();
			setPriceText.add("Click to set price");
			setPriceText.add(id);
			setPriceMeta.setLore(setPriceText);
			setPriceMeta.setDisplayName("Set Price");
			setPrice.setItemMeta(setPriceMeta);
			this.setItem(53, setPrice);
		}

		reservedSlot.add(45);
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
}
