
package com.thedemgel.ultraframe;

import com.thedemgel.ultratrader.L;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.ItemFrame;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;


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

System.out.println(event.getEntity());
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
