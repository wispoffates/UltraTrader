
package com.thedemgel.ultratrader.citizens;

import java.util.ArrayList;
import java.util.List;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.TraitInfo;
import org.bukkit.entity.Player;


public class TraitHandler {
	private List<Class<? extends UltraTrait>> ultratraits;

	public TraitHandler() {
		ultratraits = new ArrayList<>();
	}

	public void registerTrait(Class<? extends UltraTrait> trait, String name) {
		if (ultratraits.contains(trait)) {
			return;
		}

		ultratraits.add(trait);

		CitizensAPI.getTraitFactory().registerTrait(TraitInfo.create(trait).withName(name));
	}

	public boolean processClick(NPC npc, Player player) {
		for (Class<? extends UltraTrait> trait : ultratraits) {
			System.out.println(trait);
			if (npc.hasTrait(trait)) {
				System.out.println("Checking click...");
				if (!npc.getTrait(trait).onClick(player)) {
					return false;
				}
			}
		}

		return true;
	}

	/**
	 * Processed when a shop is assigned (not when a trait is attached)
	 * @param npc
	 * @return
	 */
	public boolean processAssign(NPC npc)  {
		for (Class<? extends UltraTrait> trait : ultratraits) {
			if (npc.hasTrait(trait)) {
				if (!npc.getTrait(trait).onAssign()) {
					return false;
				}
			}
		}

		return true;
	}
}
