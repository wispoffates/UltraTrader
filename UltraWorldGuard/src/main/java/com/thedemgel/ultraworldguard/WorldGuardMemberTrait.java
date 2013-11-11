
package com.thedemgel.ultraworldguard;

import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.thedemgel.ultratrader.citizens.UltraTrait;
import net.citizensnpcs.api.trait.trait.Owner;
import org.bukkit.Location;
import org.bukkit.entity.Player;


public class WorldGuardMemberTrait extends UltraTrait {

	public WorldGuardMemberTrait() {
		super("worldguardmember");
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
			if (region.isMember(owner.getOwner())) {
				return true;
			}
		}

		player.sendMessage("NPC is not in proper region.");

		return false;
	}
}
