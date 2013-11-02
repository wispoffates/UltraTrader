
package com.thedemgel.ultratrader;

import com.thedemgel.ultratrader.citizens.RentalShop;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionService;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import net.citizensnpcs.api.npc.NPC;


public class RentalHandler {
	private ScheduledExecutorService ecs;
	private static List<NPC> rentalNPCs;

	static {
		rentalNPCs = new ArrayList<>();
	}

	public RentalHandler() {
		ecs = Executors.newScheduledThreadPool(3);
		ecs.scheduleAtFixedRate(new RentCheck(), 1, 1, TimeUnit.MINUTES);
	}

	public static boolean unregisterRentalNPC(NPC npc) {
		if (rentalNPCs.contains(npc)) {
			rentalNPCs.remove(npc);
		}

		return true;
	}

	public static boolean registerRentalNPC(NPC npc) {
		if (!npc.hasTrait(RentalShop.class)) {
			return false;
		}

		if (!rentalNPCs.contains(npc)) {
			rentalNPCs.add(npc);
		}
		return true;
	}

	private class RentCheck implements Runnable {
		@Override
		public void run() {
			System.out.println("do this every minute");
			for (NPC npc : rentalNPCs) {
				if (npc.hasTrait(RentalShop.class)) {
					RentalShop rental = npc.getTrait(RentalShop.class);
					System.out.println("Checking if rental period is up for " + npc.getFullName());
				} else {
					rentalNPCs.remove(npc);
				}
			}

		}
	}
}
