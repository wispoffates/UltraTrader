
package com.thedemgel.cititradersre;

import com.thedemgel.cititradersre.shop.InventoryItem;
import com.thedemgel.cititradersre.shop.Shop;
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
		InventoryItem invItem = shop.getInventory().get(buyStackId);
		Double buyStackPriceEach = invItem.getSellprice();
		
		Double buyStackPrice = buyStackPriceEach * buyStack.getAmount();
		
		EconomyResponse heldFunds;
		if (CitiTrader.getEconomy().has(player.getName(), player.getWorld().getName(), buyStackPrice)) {
			heldFunds = CitiTrader.getEconomy().withdrawPlayer(player.getName(), player.getWorld().getName(), buyStackPrice);
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
		if (buyStack.getAmount() > invItem.getAmount()) {
			CitiTrader.getEconomy().depositPlayer(player.getName(), player.getWorld().getName(), heldFunds.amount);
			// Some error checks for deposit
			player.sendMessage("There are not enough items to purchase. (Store is out of stock)");
			return;
		} else {
			invItem.setAmount(invItem.getAmount() - buyStack.getAmount());
		}
		
		ItemStack placedStack = invItem.getItemStack();
		placedStack.setAmount(buyStack.getAmount());
		HashMap<Integer, ItemStack> returnedItems = player.getInventory().addItem(placedStack);
		
		Double refund = 0D;
		if (!returnedItems.isEmpty()) {
			// calculate returned funds... readd to merchant
			Integer returned = 0;
			for (ItemStack returnedItem : returnedItems.values()) {
				returned = returned + returnedItem.getAmount();
			}
			
			refund = returned * buyStackPriceEach;
			
			CitiTrader.getEconomy().depositPlayer(player.getName(), player.getWorld().getName(), refund);
			
			invItem.setAmount(invItem.getAmount() + returned);
		}
		
		Double traderDeposit = heldFunds.amount - refund;
		
		// Deposit into trader when interface is ready.
		player.sendMessage("Purchase Complete...");
		player.sendMessage("Trader recieved: " + traderDeposit);
	}
	
	public void processSale(Shop shop, Player player, ItemStack item) {
		
	}
}
