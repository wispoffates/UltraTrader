
package com.thedemgel.cititradersre;

import com.thedemgel.cititradersre.shop.ItemPrice;
import com.thedemgel.cititradersre.shop.Shop;
import java.math.BigDecimal;
import java.util.HashMap;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.economy.EconomyResponse.ResponseType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


/**
 * 
 * If there is anything left (no order is required) a reverse order will be
 * done, and price will be reduced by anything that remains.
 * 
 * Order for secure transactions:
 * 
 * 1) figure price for all items in "Buy Box" - "Buy Stack"
 * 2) Check if player has available funds
 * 3) Withdraw funds - hold funds in temp variable *
 * 4) Remove items (also check for availability) from traders inventory *
 * 5) If difference, remove from players "Buy Box"
 * 6) Attempt to put all items in players inventory
 * 7) Any remaining items not put into inventory - figure price value
 * 8) subtract remaining value from holding
 * 9) refund funds to player account
 * 10) deposit funds into trader account
 * 11) return any remaining items to trader inventory.
 */
public class PurchaseHandler {
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
			player.sendMessage("You do not have the funds for this Transaction.");
			return;
		}
		
		if (heldFunds == null) {
			player.sendMessage("There was an error with the economy, please report. (NULL)");
			return;
		}
		
		if (!heldFunds.type.equals(ResponseType.SUCCESS)) {
			player.sendMessage("There was an error with the economy, please report.");
			return;
		}
		
		// Check for availability
		ItemStack baseItem = invItem.getItemStack();
		Integer currentInvAmount = shop.getInventory().get(baseItem);
		if (buyStack.getAmount() > currentInvAmount) {
			CitiTrader.getEconomy().depositPlayer(player.getName(), player.getWorld().getName(), heldFunds.amount);
			// Some error checks for deposit
			player.sendMessage("There are not enough items to purchase. (Store is out of stock)");
			return;
		} else {
			
			shop.getInventory().put(baseItem, currentInvAmount - buyStack.getAmount());
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
			shop.getInventory().put(baseItem, currentInvAmount - returned);
		}
		
		BigDecimal traderDeposit = BigDecimal.valueOf(heldFunds.amount).subtract(refund);
		
		// Deposit into trader when interface is ready.
		player.sendMessage("Purchase Complete...");
		player.sendMessage("Trader recieved: " + traderDeposit);
	}
	
	public void processSale(Shop shop, Player player, ItemStack item) {
		
	}
}
