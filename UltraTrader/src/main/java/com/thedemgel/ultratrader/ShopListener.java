package com.thedemgel.ultratrader;

import com.thedemgel.ultratrader.citizens.TraderTrait;
import com.thedemgel.ultratrader.conversation.ConversationHandler;
import com.thedemgel.ultratrader.inventory.AdminCategoryPlacementView;
import com.thedemgel.ultratrader.inventory.AdminItemPlacementView;
import com.thedemgel.ultratrader.inventory.ShopInventoryView;
import com.thedemgel.ultratrader.shop.CategoryItem;
import com.thedemgel.ultratrader.shop.ItemPrice;
import com.thedemgel.ultratrader.shop.ShopHandler;
import com.thedemgel.ultratrader.shop.StoreItem;
import com.thedemgel.ultratrader.util.Permissions;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.trait.Owner;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.conversations.Conversation;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.ConcurrentMap;

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
		final Player player = (Player) event.getWhoClicked();

		if (!(event.getView() instanceof ShopInventoryView)) {
			return;
		}

		switch (event.getAction()) {
			case MOVE_TO_OTHER_INVENTORY:
			case NOTHING:
				event.setCancelled(true);
				return;
			default:
		}

		final ShopInventoryView view = (ShopInventoryView) event.getView();

		// Nothing needs to be dragged anymore...
		if (!(event.getRawSlot() < view.getTopInventory().getSize()) || event.getRawSlot() == -999) {
			return;
		} else {
			event.setCancelled(true);
        }

		switch (view.getStatus()) {
			case CATEGORY_SCREEN:
				if (event.getRawSlot() == InventoryHandler.INVENTORY_CREATE_ITEM_SLOT) {
					// Make the item here (if the shop can do it)
					if (view.getShop().getCanRemote()) {
						PurchaseHandler.processShopItemPurchase(view.getShop(), player, event.getCurrentItem());
					}
					return;
				}

				if (event.getRawSlot() == InventoryHandler.INVENTORY_ARRANGE_SLOT && view.getShop().getOwner().equals(player.getName())) {
					view.setKeepAlive(true);
					player.closeInventory();
					AdminCategoryPlacementView newView = new AdminCategoryPlacementView(player, view.getShop(), view.getStatus());
					player.openInventory(newView);
					return;
				}

				if (event.getRawSlot() == InventoryHandler.INVENTORY_ADMIN_SLOT && view.getShop().getOwner().equals(player.getName())) {
					if (!player.isConversing()) {
						Conversation convo = UltraTrader.getConversationHandler().getAdminConversation().buildConversation(player);
						view.setConvo(convo);
						convo.begin();
					} else {
						player.sendRawMessage(ChatColor.RED + L.getString("conversation.error.inconvo"));
					}
					return;
				} else if (event.getRawSlot() == InventoryHandler.INVENTORY_ADMIN_SLOT && !view.getShop().getOwner().equals(player.getName())) {
					return;
				}

				if (event.getCurrentItem() != null) {
					if (!event.getCursor().getType().equals(Material.AIR)) {
						player.getInventory().addItem(event.getCursor());
						player.setItemOnCursor(new ItemStack(Material.AIR));
					}

                    String id = CategoryItem.getCategoryId(event.getCurrentItem());
                    if (!view.getShop().getCategoryItem().containsKey(id)) {
                        return;
                    }
                    view.setCategory(id);
					view.buildCategoryItemView();
				} else if (event.getCursor().getData().getItemType().equals(Material.AIR)) {
				} else if (view.getShop().isOwner(player)) {
					if (!player.isConversing()) {
						event.setCancelled(false);
						final ItemStack inHand = event.getCursor().clone();
                        Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
                            @Override
                            public void run() {
                                player.setItemOnCursor(new ItemStack(Material.AIR));

                                Conversation convo = UltraTrader.getConversationHandler().getAddCategoryItem().buildConversation(player);
                                convo.getContext().setSessionData(ConversationHandler.CONVERSATION_SESSION_ITEM, inHand);
                                convo.getContext().setSessionData(ConversationHandler.CONVERSATION_SESSION_SLOT, event.getRawSlot());
                                view.setConvo(convo);
                                convo.begin();

                            }
                        }, UltraTrader.BUKKIT_SCHEDULER_DELAY);

					} else {
						player.sendRawMessage(ChatColor.RED + L.getString("conversation.error.inconvo"));
					}
				}
				break;
            case ITEM_SCREEN:
                if (event.getRawSlot() == InventoryHandler.INVENTORY_BACK_ARROW_SLOT) {
                    view.buildCategoryView();
                    return;
                }

                if (event.getRawSlot() == InventoryHandler.INVENTORY_ARRANGE_SLOT && view.getShop().getOwner().equals(player.getName())) {
                    view.setKeepAlive(true);
                    player.closeInventory();
                    AdminItemPlacementView newView = new AdminItemPlacementView(player, view.getShop(), view.getCategory());
                    player.openInventory(newView);
                    return;
                }

                if (event.getCurrentItem() == null) {
                    if (!event.getCursor().getData().getItemType().equals(Material.AIR)) {
                        if (view.getShop().isOwner(player)) {
                            if (!player.isConversing()) {
                                event.setCancelled(false);
                                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                                @Override
                                public void run() {
                                    ItemStack inHand = view.getItem(event.getRawSlot()).clone();
                                    Conversation convo = UltraTrader.getConversationHandler().getAddSellItem().buildConversation(player);
                                    //convo.getContext().setSessionData(ConversationHandler.CONVERSATION_SESSION_VIEW, view);
                                    convo.getContext().setSessionData(ConversationHandler.CONVERSATION_SESSION_ITEM, inHand);
                                    convo.getContext().setSessionData(ConversationHandler.CONVERSATION_SESSION_SLOT, event.getRawSlot());
                                    view.setConvo(convo);
                                    convo.begin();
                                    }
                                }, UltraTrader.BUKKIT_SCHEDULER_DELAY);
                                return;
                            } else {
                                player.sendRawMessage(ChatColor.RED + L.getString("conversation.error.inconvo"));
                                return;
                            }
                        }
                    }
                } else {
                    switch (event.getClick()) {
                        case RIGHT:
                            view.buildBuyItemView(event.getCurrentItem());
                            break;
                        case LEFT:
                            view.buildItemView(event.getCurrentItem());
                            break;
                        default:
                            break;
                    }
                }
                break;
			case SELL_ITEM_SCREEN:
				if (event.getRawSlot() == InventoryHandler.INVENTORY_BACK_ARROW_SLOT) {
					view.buildCategoryItemView();
					return;
				}

				if (event.getRawSlot() == InventoryHandler.INVENTORY_ADMIN_SLOT && view.getShop().getOwner().equals(player.getName())) {
					if (!player.isConversing()) {
						Conversation convo = UltraTrader.getConversationHandler().getSetSellPrice().buildConversation(player);
						convo.getContext().setSessionData(ConversationHandler.CONVERSATION_SESSION_ITEM, event.getCurrentItem());
						view.setConvo(convo);
						convo.begin();
					} else {
						player.sendRawMessage(ChatColor.RED + L.getString("conversation.error.inconvo"));
					}
					return;
				} else if (event.getRawSlot() == InventoryHandler.INVENTORY_ADMIN_SLOT && !view.getShop().getOwner().equals(player.getName())) {
					return;
				}

				if (event.getRawSlot() == InventoryHandler.INVENTORY_TAKE_ALL_SLOT && view.getShop().getOwner().equals(player.getName())) {
					// Put all items into inventory
					if (event.getCurrentItem() != null) {
						PurchaseHandler.processTakeAllInventory(view, (Player) event.getWhoClicked(), event.getCurrentItem());
						plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
							@Override
							public void run() {
								view.buildItemView(event.getCurrentItem());
							}
						}, UltraTrader.BUKKIT_SCHEDULER_DELAY);
					}
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
			case BUY_ITEM_SCREEN:
				if (event.getRawSlot() == InventoryHandler.INVENTORY_BACK_ARROW_SLOT) {
                    view.buildCategoryItemView();
					return;
				}

				if (event.getRawSlot() == InventoryHandler.INVENTORY_ADMIN_SLOT && view.getShop().getOwner().equals(player.getName())) {
					if (!player.isConversing()) {
						Conversation convo = UltraTrader.getConversationHandler().getSetSellPrice().buildConversation(player);
						convo.getContext().setSessionData(ConversationHandler.CONVERSATION_SESSION_ITEM, event.getCurrentItem());
						view.setConvo(convo);
						convo.begin();
					} else {
						player.sendRawMessage(ChatColor.RED + L.getString("conversation.error.inconvo"));
					}
					return;
				} else if (event.getRawSlot() == InventoryHandler.INVENTORY_ADMIN_SLOT && !view.getShop().getOwner().equals(player.getName())) {
					return;
				}

				if (event.getRawSlot() == InventoryHandler.INVENTORY_TAKE_ALL_SLOT && view.getShop().getOwner().equals(player.getName())) {
					if (event.getCurrentItem() != null) {
						PurchaseHandler.processTakeAllInventory(view, (Player) event.getWhoClicked(), event.getCurrentItem());
						plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
							@Override
							public void run() {
								view.buildBuyItemView(event.getCurrentItem());
							}
						}, UltraTrader.BUKKIT_SCHEDULER_DELAY);
					}
					return;
				}

				if (event.getCurrentItem() != null) {
                    PurchaseHandler.processSale(view.getShop(), (Player) event.getWhoClicked(), event.getCurrentItem());
					//PurchaseHandler.processTakeInventory(view.getShop(), (Player) event.getWhoClicked(), event.getCurrentItem());
					plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
						@Override
						public void run() {
							view.buildBuyItemView(event.getCurrentItem());
						}
					}, UltraTrader.BUKKIT_SCHEDULER_DELAY);
				}
				break;
			default:
		}
	}

	@EventHandler
	public void onInventoryCloseEvent(final InventoryCloseEvent event) {
		if (event.getView() instanceof ShopInventoryView) {
			final ShopInventoryView view = (ShopInventoryView) event.getView();

            final HumanEntity player = event.getPlayer();

            Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
                @Override
                public void run() {
                    ConcurrentMap<String, ItemPrice> items = view.getShop().getPriceList();
                    //ConcurrentMap<String, ItemPrice> sellitems = view.getShop().getSellPrices();

                    for (ItemStack item : player.getInventory()) {
                        String id = view.getShop().getItemId(item);
                        boolean remove = false;
                        if (id != "000000") {
                            if (items.containsKey(id)) {
                                remove = true;
                            }
                            if (remove) {
                                player.getInventory().remove(item);
                            }
                        }
                    }

                    if (!view.isKeepAlive()) {
                        UltraTrader.getStoreHandler().getInventoryHandler().removeInventoryView((Player) event.getPlayer());
                    }
                }
            }, UltraTrader.BUKKIT_SCHEDULER_DELAY);
		}

		if (event.getView() instanceof AdminItemPlacementView || event.getView() instanceof AdminCategoryPlacementView) {
			final Player player = (Player) event.getPlayer();

			Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {

				@Override
				public void run() {
					InventoryHandler handler = UltraTrader.getStoreHandler().getInventoryHandler();

					if (handler.hasInventoryView(player)) {
						ShopInventoryView view = (ShopInventoryView) handler.getInventoryView(player);
						view.setKeepAlive(false);
						view.refreshView();
						player.openInventory(view);
					}
				}
			}, UltraTrader.BUKKIT_SCHEDULER_DELAY);
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

			if (!UltraTrader.getTraitHandler().processClick(npc, event.getPlayer())) {
				return;
			}

			if (npc.hasTrait(TraderTrait.class)) {

				TraderTrait trait = npc.getTrait(TraderTrait.class);

                Integer shopid = trait.getShopId();

                // TODO: info if info click material is in hand.

                if (event.getPlayer().getInventory().getItemInMainHand().getType().equals(UltraTrader.clickInfoMaterial)) {
                    UltraTrader.getStoreHandler().displayInfo(event.getPlayer(), shopid);
                    return;
                }

                if (shopid == ShopHandler.SHOP_NULL) {
					if (npc.getTrait(Owner.class).isOwnedBy(player)) {
						if (!player.isConversing()) {
							Conversation convo = UltraTrader.getConversationHandler().getCreateShop().buildConversation(player);
							convo.getContext().setSessionData(ConversationHandler.CONVERSATION_SESSION_NPC, npc);
                            convo.getContext().setSessionData(ConversationHandler.CONVERSATION_SESSION_IS_BLOCK, false);
							convo.begin();
						} else {
							player.sendRawMessage(ChatColor.RED + L.getString("conversation.error.inconvo"));
						}
						return;
					} else {
						player.sendMessage(ChatColor.YELLOW + L.getString("general.notopen.unassigned"));
						return;
					}
				} else if (UltraTrader.getStoreHandler().getShop(shopid) == null) {
					player.sendMessage(ChatColor.RED + L.getString("general.notopen.errorloading"));
					return;
				}

                if (event.getPlayer().getInventory().getItemInMainHand().getType().equals(UltraTrader.clickMaterial)) {
                    if (npc.getTrait(Owner.class).isOwnedBy(player)) {
                        if (!player.isConversing()) {
                            Conversation convo = UltraTrader.getConversationHandler().getCreateShop().buildConversation(player);
                            convo.getContext().setSessionData(ConversationHandler.CONVERSATION_SESSION_NPC, npc);
                            convo.getContext().setSessionData(ConversationHandler.CONVERSATION_SESSION_IS_BLOCK, false);
                            convo.begin();
                        } else {
                            player.sendRawMessage(ChatColor.RED + L.getString("conversation.error.inconvo"));
                        }
                        return;
                    }
                }

                if (!trait.getOpen()) {
                    player.sendRawMessage(ChatColor.RED + L.getString("general.notopen.closed"));
                    if (!npc.getTrait(Owner.class).isOwnedBy(player)) {
                        return;
                    }
                }

				InventoryHandler handler = UltraTrader.getStoreHandler().getInventoryHandler();
				// Open Store
				if (handler.hasInventoryView(player)) {
					ShopInventoryView view = (ShopInventoryView) handler.getInventoryView(player);
					if (!view.getShop().getId().equals(shopid)) {
						handler.createBuyInventoryView(player, UltraTrader.getStoreHandler().getShop(shopid));
						view.setTarget(npc);
					}
				} else {
					handler.createBuyInventoryView(player, UltraTrader.getStoreHandler().getShop(shopid))
						.setTarget(npc);
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

        ItemStack item = event.getItem();

        if (item == null) {
            item = new ItemStack(Material.AIR);
        }

		if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			Block block = event.getClickedBlock();

			if (block.hasMetadata(BlockShopHandler.SHOP_METADATA_KEY)) {
                // TODO: open shop
                int shopId = block.getMetadata(BlockShopHandler.SHOP_METADATA_KEY).get(0).asInt();

                if (event.getPlayer().getInventory().getItemInMainHand().getType().equals(UltraTrader.clickInfoMaterial)) {
                    UltraTrader.getStoreHandler().displayInfo(event.getPlayer(), shopId);
                    return;
                }

                if (shopId == ShopHandler.SHOP_NULL  || item.getType().equals(UltraTrader.clickMaterial)) {
                    if(BlockShopHandler.assignShopConvo(event.getPlayer(), block)) {
                        return;
                    }
                }

                InventoryHandler handler = UltraTrader.getStoreHandler().getInventoryHandler();
                // Open Store
                if (handler.hasInventoryView(player)) {
                    ShopInventoryView view = (ShopInventoryView) handler.getInventoryView(player);
                    if (!view.getShop().getId().equals(shopId)) {
                        handler.createBuyInventoryView(player, UltraTrader.getStoreHandler().getShop(shopId));
                        // TODO: change to BLOCK
                        //view.setTarget(npc);
                    }
                } else {
                    handler.createBuyInventoryView(player, UltraTrader.getStoreHandler().getShop(shopId));
                    // TODO: change to BLOCK
                    //.setTarget(npc);
                }

                handler.openInventory(player);
			}

            // Check if item in hand is create block shop item
            if (item != null && item.getType().equals(UltraTrader.clickMaterial)) {
                if (BlockShopHandler.createBlockShop(player, event.getClickedBlock())) {
                    return;
                }
            }
		}

		if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			item = event.getItem();

			// after here is all itemsinhand -- if no item in hand. return.
			if (item == null) {
				return;
			}

			/*if (!item.hasItemMeta()) {
			 return;
			 }*/
			if (StoreItem.isUltraTraderItem(item)) {
				event.setCancelled(true);
				String id = StoreItem.getItemShopId(item);
				UltraTrader.getStoreHandler().getInventoryHandler().createBuyInventoryView(player, UltraTrader.getStoreHandler().getShop(Integer.valueOf(id)));
				UltraTrader.getStoreHandler().getInventoryHandler().openInventory(player);
			}
		}
	}
}
