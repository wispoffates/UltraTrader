package com.thedemgel.ultratrader;

import com.thedemgel.ultratrader.citizens.TraderTrait;
import com.thedemgel.ultratrader.conversation.ConversationHandler;
import com.thedemgel.ultratrader.shop.ShopHandler;
import com.thedemgel.ultratrader.util.Permissions;
import com.thedemgel.ultratrader.shop.ShopInventoryView;
import java.util.List;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.trait.Owner;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.conversations.Conversation;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ShopListener implements Listener {

	private final UltraTrader plugin;

	public ShopListener(UltraTrader instance) {
		plugin = instance;
	}

	/**
	 * Handle dragging and dropping items to inventory here. Inventory Click
	 * event is unreliable for this.
	 *
	 * @param event
	 */
	@EventHandler
	public void onInventoryDragEvent(final InventoryDragEvent event) {
		if (event.getView() instanceof ShopInventoryView) {
			ShopInventoryView view = (ShopInventoryView) event.getView();
			for (Integer slot : event.getRawSlots()) {
				if (slot < view.getTopInventory().getSize()) {
					event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	public void onInventoryClickEvent(final InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();

		if (!(event.getView() instanceof ShopInventoryView)) {
			return;
		}
		// Clear out event types we don't want to handle
		if (event.getAction().equals(InventoryAction.MOVE_TO_OTHER_INVENTORY)) {
			event.setCancelled(true);
			return;
		}

		final ShopInventoryView view = (ShopInventoryView) event.getView();

		// Nothing needs to be dragged anymore...
		if (!(event.getRawSlot() < view.getTopInventory().getSize())) {
			return;
		} else {
			event.setCancelled(true);
		}

		switch (view.getStatus()) {
			case MAIN_SCREEN:
				if (event.getRawSlot() == InventoryHandler.INVENTORY_BACK_ARROW_SLOT) {
					view.buildBuyView();
					return;
				}

				if (event.getRawSlot() == InventoryHandler.INVENTORY_ADMIN_SLOT && view.getShop().getOwner().equals(player.getName())) {
					Conversation convo = UltraTrader.getConversationHandler().getAdminConversation().buildConversation(player);
					view.setConvo(convo);
					convo.begin();
					return;
				} else if (event.getRawSlot() == InventoryHandler.INVENTORY_ADMIN_SLOT && !view.getShop().getOwner().equals(player.getName())) {
					return;
				}

				if (event.getCurrentItem() != null) {
					if (!event.getCursor().getType().equals(Material.AIR)) {
						player.getInventory().addItem(event.getCursor());
						player.setItemOnCursor(new ItemStack(Material.AIR));
					}
					view.buildItemView(event.getCurrentItem());
				} else if (event.getCursor().getData().getItemType().equals(Material.AIR)) {
				} else if (view.getShop().isOwner(player)) {
					event.setCancelled(false);
					ItemStack inhand = event.getCursor().clone();
					Conversation convo = UltraTrader.getConversationHandler().getAddSellItem().buildConversation(player);
					convo.getContext().setSessionData(ConversationHandler.CONVERSATION_SESSION_ITEM, inhand);
					convo.getContext().setSessionData(ConversationHandler.CONVERSATION_SESSION_SLOT, event.getRawSlot());
					view.setConvo(convo);
					convo.begin();
				} else {
					player.sendMessage(L.getString("shops.gotobuy.line1"));
					player.sendMessage(L.getString("shops.gotobuy.line2"));
				}
				break;
			case SELL_SCREEN:
				if (event.getRawSlot() == InventoryHandler.INVENTORY_BACK_ARROW_SLOT) {
					view.buildSellView();
					return;
				}

				if (event.getRawSlot() == InventoryHandler.INVENTORY_ADMIN_SLOT && view.getShop().getOwner().equals(player.getName())) {
					Conversation convo = UltraTrader.getConversationHandler().getSetSellPrice().buildConversation(player);
					convo.getContext().setSessionData(ConversationHandler.CONVERSATION_SESSION_ITEM, event.getCurrentItem());
					view.setConvo(convo);
					convo.begin();
					return;
				} else if (event.getRawSlot() == InventoryHandler.INVENTORY_ADMIN_SLOT && !view.getShop().getOwner().equals(player.getName())) {
					return;
				}

				if (event.getRawSlot() == InventoryHandler.INVENTORY_TAKE_ALL_SLOT && view.getShop().getOwner().equals(player.getName())) {
					// Put all items into inventory
					return;
				} else if (event.getRawSlot() == InventoryHandler.INVENTORY_TAKE_ALL_SLOT && !view.getShop().getOwner().equals(player.getName())) {
					return;
				}

				if (event.getCurrentItem() != null) {
					PurchaseHandler.processPurchase(view.getShop(), (Player) event.getWhoClicked(), event.getCurrentItem());
					plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
						@Override
						public void run() {
							view.buildItemView(event.getCurrentItem());
						}
					}, UltraTrader.BUKKIT_SCHEDULER_DELAY);
				}
				break;
			case BUY_SCREEN:
				if (event.getRawSlot() == InventoryHandler.INVENTORY_BACK_ARROW_SLOT) {
					view.buildSellView();
					return;
				}

				if (event.getRawSlot() == InventoryHandler.INVENTORY_ADMIN_SLOT && view.getShop().getOwner().equals(player.getName())) {
					Conversation convo = UltraTrader.getConversationHandler().getAdminConversation().buildConversation(player);
					view.setConvo(convo);
					convo.begin();
					return;
				} else if (event.getRawSlot() == InventoryHandler.INVENTORY_ADMIN_SLOT && !view.getShop().getOwner().equals(player.getName())) {
					return;
				}

				if (event.getCurrentItem() != null && view.getShop().isOwner(player)) {
					if (!event.getCursor().getType().equals(Material.AIR)) {
						player.getInventory().addItem(event.getCursor());
						player.setItemOnCursor(new ItemStack(Material.AIR));
					}
					view.buildBuyItemView(event.getCurrentItem());
				} else if (event.getCurrentItem() != null) {
					if (!event.getCursor().getType().equals(Material.AIR)) {
						player.sendMessage("Place in Empty Slot.");
						event.setCancelled(true);
					}
				} else if (view.getShop().isOwner(player)) {
					ItemStack inhand = event.getCursor().clone();
					if (!inhand.getType().equals(Material.AIR)) {
						event.setCancelled(false);
						Conversation convo = UltraTrader.getConversationHandler().getAddBuyItem().buildConversation(player);
						convo.getContext().setSessionData(ConversationHandler.CONVERSATION_SESSION_ITEM, inhand);
						convo.getContext().setSessionData(ConversationHandler.CONVERSATION_SESSION_SLOT, event.getRawSlot());
						view.setConvo(convo);
						convo.begin();
					}
				} else if (!event.getCursor().getData().getItemType().equals(Material.AIR)) {
					// Buy the items if you want them...
					ItemStack inhand = event.getCursor().clone();
					if (view.getShop().hasBuyItem(inhand)) {
						event.setCancelled(false);
						PurchaseHandler.processSale(view.getShop(), (Player) event.getWhoClicked(), inhand);
						plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
							@Override
							public void run() {
								view.buildBuyView();
							}
						}, UltraTrader.BUKKIT_SCHEDULER_DELAY);
					}
				}
				break;
			case BUY_ITEM_SCREEN:
				if (event.getRawSlot() == InventoryHandler.INVENTORY_BACK_ARROW_SLOT) {
					view.buildBuyView();
					return;
				}

				if (event.getRawSlot() == InventoryHandler.INVENTORY_ADMIN_SLOT && view.getShop().getOwner().equals(player.getName())) {
					Conversation convo = UltraTrader.getConversationHandler().getBuyItemAdmin().buildConversation(player);
					convo.getContext().setSessionData(ConversationHandler.CONVERSATION_SESSION_ITEM, event.getCurrentItem());
					view.setConvo(convo);
					convo.begin();
					return;
				} else if (event.getRawSlot() == InventoryHandler.INVENTORY_ADMIN_SLOT && !view.getShop().getOwner().equals(player.getName())) {
					return;
				}

				if (event.getRawSlot() == InventoryHandler.INVENTORY_TAKE_ALL_SLOT && view.getShop().getOwner().equals(player.getName())) {
					// Put all items into inventory
					return;
				}

				break;
			default:
		}
	}

	@EventHandler
	public void onInventoryCloseEvent(InventoryCloseEvent event) {
		if (event.getView() instanceof ShopInventoryView) {
			ShopInventoryView view = (ShopInventoryView) event.getView();
			if (!view.isKeepAlive()) {
				UltraTrader.getStoreHandler().getInventoryHandler().removeInventoryView((Player) event.getPlayer());
			}
		}
	}

	@EventHandler
	public void onPlayerInteractEntityEvent(PlayerInteractEntityEvent event) {
		Entity entity = event.getRightClicked();
		Player player = event.getPlayer();

		// Check if player can even access stores
		if (!player.hasPermission(Permissions.USE_STORES)) {
			return;
		}

		if (plugin.isCitizens()) {
			if (!CitizensAPI.getNPCRegistry().isNPC(entity)) {
				return;
			}

			NPC npc = CitizensAPI.getNPCRegistry().getNPC(entity);

			if (npc.hasTrait(TraderTrait.class)) {
				TraderTrait trait = npc.getTrait(TraderTrait.class);

				Integer shopid = trait.getShopId();



				if (shopid == ShopHandler.SHOP_NULL) {
					if (npc.getTrait(Owner.class).isOwnedBy(player)) {
						Conversation convo = UltraTrader.getConversationHandler().getCreateShop().buildConversation(player);
						convo.getContext().setSessionData(ConversationHandler.CONVERSATION_SESSION_NPC, npc);
						convo.begin();
						return;
					} else {
						player.sendMessage(L.getString("general.notopen.unassigned"));
						return;
					}
				} else if (UltraTrader.getStoreHandler().getShop(shopid) == null) {
					player.sendMessage(L.getString("general.notopen.errorloading"));
					return;
				}

				InventoryHandler handler = UltraTrader.getStoreHandler().getInventoryHandler();
				// Open Store
				if (handler.hasInventoryView(player)) {
					ShopInventoryView view = (ShopInventoryView) handler.getInventoryView(player);
					if (!view.getShop().getId().equals(shopid)) {
						handler.createBuyInventoryView(player, UltraTrader.getStoreHandler().getShop(shopid));
					}
				} else {
					handler.createBuyInventoryView(player, UltraTrader.getStoreHandler().getShop(shopid));
				}

				handler.openInventory(player);
			}
		}
	}

	/**
	 * Checks when a player interacts with a block or item (in hand) to see
	 * if it is a store.
	 *
	 * @param event
	 */
	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent event) {
		Player player = event.getPlayer();

		// Check if player can even access stores
		if (!player.hasPermission(Permissions.USE_STORES)) {
			return;
		}

		if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			Block block = event.getClickedBlock();
			if (block.hasMetadata("shop")) {
				return;
			}
		}

		if (event.getAction().equals(Action.LEFT_CLICK_AIR) || event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
			ItemStack item = event.getItem();

			// after here is all itemsinhand -- if no item in hand. return.
			if (item == null) {
				return;
			}

			if (!item.hasItemMeta()) {
				return;
			}

			/*
			switch (item.getItemMeta().getDisplayName()) {
				case "Store":
					// Open Store
					UltraTrader.getStoreHandler().getInventoryHandler().createBuyInventoryView(player, UltraTrader.getStoreHandler().getShop(1));
					UltraTrader.getStoreHandler().getInventoryHandler().openInventory(player);
					break;
				case "Create Shop":
					createShop(player, item, event.getClickedBlock());
					break;
				default:
			}*/
		}
	}

	public void createShop(Player player, ItemStack item, Block block) {
		if (!player.hasPermission(Permissions.CREATE_STORES)) {
			player.sendMessage(L.getString("permission.create.deny"));
			return;
		}
		if (item.getItemMeta().hasLore()) {
			ItemMeta meta = item.getItemMeta();
			Integer uses = Integer.valueOf(meta.getLore().get(2).split(" ")[0]) - 1;
			List<String> metas = meta.getLore();
			metas.set(2, String.valueOf(uses) + " uses left.");
			meta.setLore(metas);
			item.setItemMeta(meta);
		}
	}
}
