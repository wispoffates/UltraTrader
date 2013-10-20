
package com.thedemgel.ultratrader.util;


public class Permissions {
	/* START SNIPPET: perm
	 * ### Admin
	 * END SNIPPET: perm
	 */

	/**
	 * START SNIPPET: perm
	 * ####trader.admin.debug
	 * `Allows a player to use the "/traderdebug" command`
	 * END SNIPPET: perm
	 */
	public static final String ADMIN_DEBUG = "trader.admin.debug";

	/* START SNIPPET: perm
	 * ### Stores or Shops
	 * END SNIPPET: perm
	 */

	/**
	 * START SNIPPET: perm
	 * ####trader.store.use
	 * `Allows the use of stores`
	 * END SNIPPET: perm
	 */
	public static final String USE_STORES = "trader.store.use";
	/**
	 * START SNIPPET: perm
	 * ####trader.store.create
	 * `Allows a user to create stores`
	 * END SNIPPET: perm
	 */
	public static final String CREATE_STORES = "trader.store.create";
	/**
	 * START SNIPPET: perm
	 * ####trader.store.level.increase
	 * `Allows a user to increase his/her shop level`
	 * END SNIPPET: perm
	 */
	public static final String LEVEL_INCREASE = "trader.store.level.increase";
	/**
	 * START SNIPPET: perm
	 * ####trader.store.level.decrease
	 * `Allows a user to decrease his/her shop level`
	 * END SNIPPET: perm
	 */
	public static final String LEVEL_DECREASE = "trader.store.level.decrease";
	/**
	 * START SNIPPET: perm
	 * ####trader.store.level.set
	 * `Allows a user to set his/her shop level. This will not charge a player the level change fee.`
	 * END SNIPPET: perm
	 */
	public static final String LEVEL_SET = "trader.store.level.set";

	/* START SNIPPET: perm
	 * ### Wallets
	 * END SNIPPET: perm
	 */

	/**
	 * START SNIPPET: perm
	 * ####trader.wallet.admin
	 * `Allows a user to set a store's wallet to ADMIN.
	 * Allowing infinite money and infinite items.`
	 * END SNIPPET: perm
	 */
	public static final String WALLET_ADMIN = "trader.wallet.admin";
	/**
	 * START SNIPPET: perm
	 * ####trader.wallet.shop
	 * `Allows a user to set a store's wallet to SHOP.
	 * Uses the internal Wallet of the shop to store and keep balance.`
	 * END SNIPPET: perm
	 */
	public static final String WALLET_SHOP = "trader.wallet.shop";
	/**
	 * START SNIPPET: perm
	 * ####trader.wallet.player.setother
	 * `Player with this permission is allowed to set other players to
	 * a wallet in any store.`
	 * END SNIPPET: perm
	 */
	public static final String WALLET_PLAYER_SETOTHER = "trader.wallet.player.setother";
	/**
	 * START SNIPPET: perm
	 * ####trader.wallet.player
	 * `Allows a user to set a store's wallet to PLAYER.
	 * Uses the economy plugin to use a players account.`
	 * END SNIPPET: perm
	 */
	public static final String WALLET_PLAYER = "trader.wallet.player";
	/**
	 * START SNIPPET: perm
	 * ####trader.wallet.bank
	 * `Allows a user to set a store's wallet to BANK.
	 * Uses the economy plugin if it supports banks.`
	 *
	 * `If the plugin does not support bank this option will not show.
	 * Shops set to this option after a economy is removed that supported
	 * banks will disable this bank until manual editing.`
	 * END SNIPPET: perm
	 */
	public static final String WALLET_BANK = "trader.wallet.bank";

	/* START SNIPPET: perm
	 * ### Inventories
	 * END SNIPPET: perm
	 */

	/**
	 * START SNIPPET: perm
	 * ####trader.inventory.shop
	 * `Allows a user to set a store's inventory to SHOP.
	 *
	 * The shop inventory is also the default inventory type`
	 * END SNIPPET: perm
	 */
	public static final String INVENTORY_SHOP = "trader.inventory.shop";
	/**
	 * START SNIPPET: perm
	 * ####trader.inventory.admin
	 * `Allows a user to set a store's inventory to ADMIN.
	 *
	 * This means that inventory is never checked, there is always
	 * items, and items are never received.`
	 * END SNIPPET: perm
	 */
	public static final String INVENTORY_ADMIN = "trader.inventory.admin";
}
