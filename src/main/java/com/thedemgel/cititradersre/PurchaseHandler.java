
package com.thedemgel.cititradersre;


/**
 * 
 * If there is anything left (no order is required) a reverse order will be
 * done, and price will be reduced by anything that remains.
 * 
 * Order for secure transactions:
 * 
 * 1) figure price for all items in "Buy Box"
 * 2) Check if player has available funds
 * 3) Withdraw funds - hold funds in temp variable
 * 4) Remove items (also check for availability) from traders inventory
 * 5) If difference, remove from players "Buy Box"
 * 6) Attempt to put all items in players inventory
 * 7) Any remaining items not put into inventory - figure price value
 * 8) subtract remaining value from holding
 * 9) refund funds to player account
 * 10) deposit funds into trader account
 * 11) return any remaining items to trader inventory.
 */
public class PurchaseHandler {
	
}
