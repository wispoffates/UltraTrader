
package com.thedemgel.cititradersre;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;


public class WorldListener implements Listener {

	/**
	 * Checks when a player interacts with a block or item (in hand) to see
	 * if it is a store.
	 * @param event
	 */
	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		// Check if player has permissions
		// Check if is a store
			// If not
				// Check if item is Store Creation Item
				// Check player permissions to create a store
					// if not exit
		// if is a store, is it the owner?
			// if yes
				// is the owner in manager mode
					// if yes goto store management
					// if not continue to store display
			// if not
				// continue to store display
	}
}
