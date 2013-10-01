package com.thedemgel.cititradersre;

import com.thedemgel.cititradersre.shop.ItemPrice;
import com.thedemgel.cititradersre.shop.Shop;
import java.math.BigDecimal;
import java.text.MessageFormat;
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
		String buyStackId = shop.getItemId(buyStack);
		//ItemPrice invItem = shop.getInventory().get(buyStackId);
		ItemPrice invItem = shop.getSellprices().get(buyStackId);
		BigDecimal buyStackPriceEach = invItem.getPrice();

		BigDecimal buyStackPrice = buyStackPriceEach.multiply(BigDecimal.valueOf(buyStack.getAmount()));

		EconomyResponse heldFunds;
		if (CitiTrader.getEconomy().has(player.getName(), player.getWorld().getName(), buyStackPrice.doubleValue())) {
			heldFunds = CitiTrader.getEconomy().withdrawPlayer(player.getName(), player.getWorld().getName(), buyStackPrice.doubleValue());
		} else {
			player.sendMessage(CitiTrader.getResourceBundle().getString("transaction.sale.player.notenoughfunds"));
			return;
		}

		if (heldFunds == null) {
			player.sendMessage(CitiTrader.getResourceBundle().getString("transaction.error.economy"));
			return;
		}

		if (!heldFunds.type.equals(ResponseType.SUCCESS)) {
			player.sendMessage(CitiTrader.getResourceBundle().getString("transaction.error.economy"));
			return;
		}

		// Check for availability
		ItemStack baseItem = invItem.getItemStack();
		//Integer currentInvAmount = shop.getInventory().get(baseItem);
		Integer currentInvAmount = shop.getInventoryInterface().getInventoryAmount(baseItem);
		
		if (buyStack.getAmount() > currentInvAmount) {
			CitiTrader.getEconomy().depositPlayer(player.getName(), player.getWorld().getName(), heldFunds.amount);
			// Some error checks for deposit
			player.sendMessage(CitiTrader.getResourceBundle().getString("transaction.sale.shop.notenoughitems"));
			return;
		} else {

			//shop.getInventory().put(baseItem, currentInvAmount - buyStack.getAmount());
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

			refund = buyStackPriceEach.multiply(BigDecimal.valueOf(returned));

			CitiTrader.getEconomy().depositPlayer(player.getName(), player.getWorld().getName(), refund.doubleValue());

			//invItem.setAmount(invItem.getAmount() + returned);
			//shop.getInventory().put(baseItem, currentInvAmount - returned);
			shop.getInventoryInterface().removeInventory(baseItem, returned);
		}

		BigDecimal traderDeposit = BigDecimal.valueOf(heldFunds.amount).subtract(refund);

		// Deposit into trader when interface is ready.
		if (shop.getWallet().addFunds(traderDeposit).type.equals(ResponseType.SUCCESS)) {
			player.sendMessage(MessageFormat.format(CitiTrader.getResourceBundle().getString("transaction.sale.shop.totalpurchase"), traderDeposit));
		} else {
			player.sendMessage(CitiTrader.getResourceBundle().getString("transaction.error.fundstoshop"));
		}
		
		shop.save();
	}

	/**
	 * Process a Sale TO A Shop from a Player.
	 *
	 * @param shop The shop the sale is going to
	 * @param player The player the sale is coming from
	 * @param buyStack The itemstack of the item being purchased.
	 */
	public void processSale(Shop shop, Player player, ItemStack item) {
	}
}
