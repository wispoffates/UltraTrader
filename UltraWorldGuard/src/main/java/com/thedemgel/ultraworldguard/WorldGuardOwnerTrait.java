
package com.thedemgel.ultraworldguard;

import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.thedemgel.ultratrader.citizens.UltraTrait;
import net.citizensnpcs.api.trait.trait.Owner;
import org.bukkit.Location;
import org.bukkit.entity.Player;


public class WorldGuardOwnerTrait extends UltraTrait {

	public WorldGuardOwnerTrait() {
		super("worldguardowner");
	}

	@Override
	public boolean hasMenuOption() {
		return false;
	}

	@Override
	public boolean onClick(Player player) {
		Location npcloc = npc.getEntity().getLocation();

		Owner owner = npc.getTrait(Owner.class);

		ApplicableRegionSet regions = WGBukkit.getRegionManager(npcloc.getWorld()).getApplicableRegions(npcloc);

		for (ProtectedRegion region : regions) {
			if (region.isOwner(owner.getOwner())) {
				return true;
			}
		}

		player.sendMessage("NPC is not in proper region.");

		return false;
	}
}
