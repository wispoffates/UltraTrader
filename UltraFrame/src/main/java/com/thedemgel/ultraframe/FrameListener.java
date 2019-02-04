
package com.thedemgel.ultraframe;

import org.bukkit.ChatColor;
import org.bukkit.entity.ItemFrame;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.hanging.HangingPlaceEvent;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;


public class FrameListener implements Listener {

	@EventHandler
	public void onFrameClick(HangingPlaceEvent event) {
		NPC npc = CitizensAPI.getDefaultNPCSelector().getSelected(event.getPlayer());

		if (npc == null) {
			event.getPlayer().sendMessage(ChatColor.RED + "No NPC was selected");
			return;
		}

		if (!npc.hasTrait(UltraFrameTrait.class)) {
			event.getPlayer().sendMessage(ChatColor.RED + "Trader doesn't have trait");
			return;
		}

		if (event.getEntity() instanceof ItemFrame) {
			ItemFrame frame = (ItemFrame) event.getEntity();

			System.out.println("YAY");

			if (FrameHandler.isFrameOwned(frame)) {
				event.setCancelled(true);
			} else {
				npc.getTrait(UltraFrameTrait.class).addFrame(frame);
			}
		}
	}
}
