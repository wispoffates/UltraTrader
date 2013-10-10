package com.thedemgel.ultratrader;

import com.thedemgel.ultratrader.shop.ItemPrice;
import com.thedemgel.ultratrader.shop.Shop;
import com.thedemgel.ultratrader.wallet.Wallet;
import java.math.BigDecimal;
import java.util.HashMap;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.economy.EconomyResponse.ResponseType;
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
		}

		BigDecimal traderDeposit = BigDecimal.valueOf(heldFunds.amount).subtract(refund);

		// Deposit into trader when interface is ready.
		if (!charge) {
			player.sendMessage(L.getFormatString("transaction.sale.shop.totalpurchase", "FREE"));
		} else if (shop.getWallet().addFunds(traderDeposit).type.equals(ResponseType.SUCCESS)) {
			//player.sendMessage(MessageFormat.format(UltraTrader.getResourceBundle().getString("transaction.sale.shop.totalpurchase"), traderDeposit));
			player.sendMessage(L.getFormatString("transaction.sale.shop.totalpurchase", UltraTrader.getEconomy().format(traderDeposit.doubleValue())));
		} else {
			player.sendMessage(L.getString("transaction.error.fundstoshop"));
		}

		shop.save();
	}

	/**
	 * Process a Sale TO A Shop from a Player.
	 *
	 * @param shop The shop the sale is going to
	 * @param player The player the sale is coming from
	 * @param item The itemstack of the item being purchased.
	 */
	public static void processSale(Shop shop, Player player, ItemStack item) {
		ItemPrice invItem = shop.getBuyItem(item);
		BigDecimal buyStackPriceEach = invItem.getPrice();
		BigDecimal buyStackPrice = buyStackPriceEach.multiply(BigDecimal.valueOf(item.getAmount()));

		Wallet wallet = shop.getWallet();

		EconomyResponse heldFunds;
		if (wallet.hasFunds(buyStackPrice)) {
			heldFunds = wallet.removeFunds(buyStackPrice);
		} else {
			player.sendMessage(L.getString("transaction.sale.player.notenoughfunds"));
			return;
		}

		if (heldFunds == null) {
			player.sendMessage(L.getString("transaction.error.economy") + 1);
			return;
		}

		if (!heldFunds.type.equals(ResponseType.SUCCESS)) {
			player.sendMessage(L.getString("transaction.error.economy") + 2);
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

		player.sendMessage(L.getFormatString("transaction.sale.shop.totalpurchase", UltraTrader.getEconomy().format(heldFunds.amount)));

		shop.save();
	}
}
