package com.thedemgel.ultratrader;

import com.thedemgel.ultratrader.shop.ItemPrice;
import com.thedemgel.ultratrader.shop.Shop;
import com.thedemgel.ultratrader.inventory.ShopInventoryView;
import com.thedemgel.ultratrader.shop.StoreItem;
import com.thedemgel.ultratrader.util.ShopAction;
import com.thedemgel.ultratrader.wallet.Wallet;
import java.math.BigDecimal;
import java.util.HashMap;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.economy.EconomyResponse.ResponseType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * If there is anything left (no order is required) a reverse order will be
 * done, and price will be reduced by anything that remains.
 *
 * Order for secure transactions:
 *
 * 1) figure price for all items in "Buy Box" - "Buy Stack" 2) Check if player
 * has available funds 3) Withdraw funds - hold funds in temp variable * 4)
 * Remove items (also check for availability) from traders inventory * 5) If
 * difference, remove from players "Buy Box" 6) Attempt to put all items in
 * players inventory 7) Any remaining items not put into inventory - figure
 * price value 8) subtract remaining value from holding 9) refund funds to
 * player account 10) deposit funds into trader account 11) return any remaining
 * items to trader inventory.
 */
public class PurchaseHandler {

	public static void processShopItemPurchase(Shop shop, Player player, ItemStack shopitem) {
		System.out.println("Attempting to buy shop item" + shopitem);

		double cost = shop.getRemoteItemCost();

		EconomyResponse resp = UltraTrader.getEconomy().withdrawPlayer(player.getName(), player.getWorld().getName(), cost);

		if (!resp.type.equals(ResponseType.SUCCESS)) {
			player.sendMessage("(Assign RB) Not Enough Funds");
			return;
		}

		StoreItem.linkToShop(shop, shopitem);

		player.getInventory().addItem(shopitem);

		if (UltraTrader.isLoggingEnabled()) {
			UltraTrader.getLogDbObj().doLog(shop, player, resp, ShopAction.SELL, "Player purchased Shop Item.");
		}
	}

	/**
	 * Process a Sale TO A Player from a Shop.
	 *
	 * @param shop The shop the sale is coming from
	 * @param player The player the sale is going to
	 * @param buyStack The itemstack of the item clicked.
	 */
	public static void processPurchase(Shop shop, Player player, ItemStack buyStack) {
		boolean charge = true;
		// Should we charge
		if (shop.isOwner(player)) {
			charge = false;
		}

		String buyStackId = shop.getItemId(buyStack);
		ItemPrice invItem = shop.getSellprices().get(buyStackId);
		BigDecimal buyStackPriceEach = invItem.getPrice();

		BigDecimal buyStackPrice = buyStackPriceEach.multiply(BigDecimal.valueOf(buyStack.getAmount()));

		EconomyResponse heldFunds;
		if (!charge) {
			heldFunds = new EconomyResponse(0, 0, ResponseType.SUCCESS, "");
		} else if (UltraTrader.getEconomy().has(player.getName(), player.getWorld().getName(), buyStackPrice.doubleValue())) {
			heldFunds = UltraTrader.getEconomy().withdrawPlayer(player.getName(), player.getWorld().getName(), buyStackPrice.doubleValue());
		} else {
			player.sendMessage(L.getString("transaction.sale.player.notenoughfunds"));
			return;
		}

		if (heldFunds == null) {
			player.sendMessage(L.getString("transaction.error.economy"));
			return;
		}

		if (!heldFunds.type.equals(ResponseType.SUCCESS)) {
			player.sendMessage(L.getString("transaction.error.economy"));
			return;
		}

		// Check for availability
		ItemStack baseItem = invItem.getItemStack();
		Integer currentInvAmount = shop.getInventoryInterface().getInventoryStock(baseItem);

		if (buyStack.getAmount() > currentInvAmount) {
			if (charge) {
				UltraTrader.getEconomy().depositPlayer(player.getName(), player.getWorld().getName(), heldFunds.amount);
			}
			// Some error checks for deposit
			player.sendMessage(L.getString("transaction.sale.shop.notenoughitems"));
			return;
		} else {
			shop.getInventoryInterface().removeInventory(baseItem, buyStack.getAmount());
		}

		ItemStack placedStack = invItem.getItemStack().clone();
		placedStack.setAmount(buyStack.getAmount());
		HashMap<Integer, ItemStack> returnedItems = player.getInventory().addItem(placedStack);

		BigDecimal refund = BigDecimal.ZERO;
		Integer finalamount = buyStack.getAmount();

		if (!returnedItems.isEmpty()) {
			// calculate returned funds... readd to merchant
			Integer returned = 0;
			for (ItemStack returnedItem : returnedItems.values()) {
				returned = returned + returnedItem.getAmount();
			}

			if (charge) {
				refund = buyStackPriceEach.multiply(BigDecimal.valueOf(returned));

				UltraTrader.getEconomy().depositPlayer(player.getName(), player.getWorld().getName(), refund.doubleValue());
			}

			ItemStack itemReturned = baseItem.clone();
			itemReturned.setAmount(returned);
			shop.getInventoryInterface().addInventory(itemReturned);
			finalamount =- returned;
		}

		BigDecimal traderDeposit = BigDecimal.valueOf(heldFunds.amount).subtract(refund);

		// Deposit into trader when interface is ready.
		if (!charge) {
			player.sendMessage(L.getFormatString("transaction.sale.shop.purchase", buyStack.getType().name(), finalamount));
			player.sendMessage(L.getFormatString("transaction.sale.shop.totalpurchase", "FREE"));
		} else if (shop.getWallet().addFunds(traderDeposit).type.equals(ResponseType.SUCCESS)) {
			player.sendMessage(L.getFormatString("transaction.sale.shop.purchase", buyStack.getType().name(), finalamount));
			player.sendMessage(L.getFormatString("transaction.sale.shop.totalpurchase", UltraTrader.getEconomy().format(traderDeposit.doubleValue())));
		} else {
			player.sendMessage(L.getString("transaction.error.fundstoshop"));
		}

		shop.save();

		if (UltraTrader.isLoggingEnabled()) {
			UltraTrader.getLogDbObj().doLog(shop, player, new EconomyResponse(traderDeposit.doubleValue(), 0, ResponseType.SUCCESS, ""), ShopAction.SELL, "Player pruchased " + finalamount + " " + buyStack.getType().name());
		}
	}

	/**
	 * Process a Sale TO A Shop from a Player.
	 *
	 * @param shop The shop the sale is going to
	 * @param player The player the sale is coming from
	 * @param item The itemstack of the item being purchased.
	 */
	public static void processSale(Shop shop, final Player player, final ItemStack item) {
		ItemPrice invItem = shop.getBuyItem(item);
		BigDecimal buyStackPriceEach = invItem.getPrice();
		BigDecimal buyStackPrice = buyStackPriceEach.multiply(BigDecimal.valueOf(item.getAmount()));

		boolean success = true;

		Wallet wallet = shop.getWallet();

		EconomyResponse heldFunds = wallet.removeFunds(buyStackPrice);

		if (!heldFunds.type.equals(ResponseType.SUCCESS)) {
			player.sendMessage(L.getString("transaction.sale.shop.notenoughfunds"));
			success = false;
		}

		if (!success) {
			Bukkit.getScheduler().scheduleSyncDelayedTask(UltraTrader.getInstance(), new Runnable() {

				@Override
				public void run() {
					player.getInventory().addItem(item);
				}
			}, UltraTrader.BUKKIT_SCHEDULER_DELAY);

			return;
		}

		// Add the Item to Trader Inventory
		shop.getInventoryInterface().addInventory(item);

		EconomyResponse depResponse = UltraTrader.getEconomy().depositPlayer(player.getName(), player.getWorld().getName(), heldFunds.amount);

		if (depResponse == null) {
			player.sendMessage(L.getString("transaction.error.economy") + 3);
			return;
		}

		if (!depResponse.type.equals(ResponseType.SUCCESS)) {
			player.sendMessage(L.getString("transaction.error.economy") + 4);
			return;
		}

		player.sendMessage(L.getFormatString("transaction.sale.shop.sale", item.getType().name(), item.getAmount()));
		player.sendMessage(L.getFormatString("transaction.sale.shop.totalpurchase", UltraTrader.getEconomy().format(heldFunds.amount)));

		shop.save();

		if (UltraTrader.isLoggingEnabled()) {
			UltraTrader.getLogDbObj().doLog(shop, player, heldFunds, ShopAction.BUY, "Sold " + item.getAmount() + " " + item.getType().name());
		}
	}

	public static void processTakeAllInventory(ShopInventoryView view, final Player player, final ItemStack item) {
		Shop shop = view.getShop();
		String buyStackId = shop.getItemId(item);

		ItemPrice invItem;
		switch (view.getStatus()) {
			case BUY_ITEM_SCREEN:
				invItem = shop.getBuyprices().get(buyStackId);
				break;
			case SELL_SCREEN:
				invItem = shop.getSellprices().get(buyStackId);
				break;
			default:
				return;
		}

		// Check for availability
		ItemStack baseItem = invItem.getItemStack();
		Integer currentInvAmount = shop.getInventoryInterface().getInventoryStock(baseItem);

		if (currentInvAmount <= 0) {
			player.sendMessage("not enough inventory");
			return;
		}

		ItemStack giveItem = baseItem.clone();
		giveItem.setAmount(currentInvAmount);

		player.getInventory().addItem(giveItem);
		shop.getInventoryInterface().removeInventory(baseItem, currentInvAmount);

		if (UltraTrader.isLoggingEnabled()) {
			UltraTrader.getLogDbObj().doLog(shop, player, new EconomyResponse(0, 0, ResponseType.SUCCESS, ""), ShopAction.REMOVE_STOCK, "Owner removed (ALL) " + giveItem.getAmount() + " " + giveItem.getType().name());
		}
	}

	public static void processTakeInventory(Shop shop, final Player player, final ItemStack item) {
		String buyStackId = shop.getItemId(item);
		ItemPrice invItem = shop.getBuyprices().get(buyStackId);
		//ItemPrice invItem = shop.getBuyItem(item);

		// Check for availability
		ItemStack baseItem = invItem.getItemStack();
		Integer currentInvAmount = shop.getInventoryInterface().getInventoryStock(baseItem);

		if (currentInvAmount <= 0) {
			player.sendMessage("not enough inventory");
			return;
		}

		ItemStack giveItem = baseItem.clone();
		giveItem.setAmount(item.getAmount());

		player.getInventory().addItem(giveItem);
		shop.getInventoryInterface().removeInventory(baseItem, item.getAmount());

		if (UltraTrader.isLoggingEnabled()) {
			UltraTrader.getLogDbObj().doLog(shop, player, new EconomyResponse(0, 0, ResponseType.SUCCESS, ""), ShopAction.REMOVE_STOCK, "Owner removed " + giveItem.getAmount() + " " + giveItem.getType().name());
		}
	}
}
