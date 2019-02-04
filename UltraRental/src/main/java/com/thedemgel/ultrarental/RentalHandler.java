
package com.thedemgel.ultrarental;

import com.thedemgel.ultratrader.UltraTrader;
import com.thedemgel.ultratrader.citizens.TraderTrait;
import com.thedemgel.ultratrader.shop.ShopHandler;
import com.thedemgel.ultratrader.util.ResponseObject;
import com.thedemgel.ultratrader.util.ResponseObjectType;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import net.citizensnpcs.api.npc.NPC;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;


public class RentalHandler {
	private ScheduledExecutorService ecs;
	private static List<NPC> rentalNPCs;

	static {
		rentalNPCs = new ArrayList<>();
	}

	public RentalHandler() {
		ecs = Executors.newScheduledThreadPool(3);
		ecs.scheduleAtFixedRate(new RentCheck(), 10, 10, TimeUnit.SECONDS);
	}

    public static int getNumberRentalNPCs() {
        return rentalNPCs.size();
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

	public static ResponseObject<?> rentNPC(NPC npc, OfflinePlayer player) {
		return rentNPC(npc, player, false);
	}

	public static ResponseObject<?> rentNPC(NPC npc, OfflinePlayer player, boolean update) {
		if (!npc.hasTrait(RentalShop.class) || !npc.hasTrait(TraderTrait.class)) {
			Bukkit.getLogger().log(Level.SEVERE, "Missing Trait from NPC: " + npc.getId());
			return new ResponseObject<Void>("This NPC is missing a trait.", ResponseObjectType.FAILURE);
		}

		RentalShop rent = npc.getTrait(RentalShop.class);

		if (rent.isRented() && !update) {
			return new ResponseObject<Void>("This NPC is already rented", ResponseObjectType.FAILURE);
		}

		EconomyResponse resp = UltraTrader.getEconomy().withdrawPlayer(player, npc.getStoredLocation().getWorld().getName(), rent.getCost());

		if (!resp.transactionSuccess()) {
			return new ResponseObject<Void>("Sorry you don't have the funds to rent this npc.", ResponseObjectType.FAILURE);
		}

		rent.setRenter(player);
		rent.setRentedOn(System.currentTimeMillis());

		return new ResponseObject<Void>("You have successfully rented this npc.", ResponseObjectType.SUCCESS);
	}

	public static void clearNPC(NPC npc) {
		if (!npc.hasTrait(RentalShop.class) || !npc.hasTrait(TraderTrait.class)) {
			Bukkit.getLogger().log(Level.SEVERE, "Missing Trait from NPC: " + npc.getId());
			return;
		}

		RentalShop rent = npc.getTrait(RentalShop.class);
		TraderTrait trader = npc.getTrait(TraderTrait.class);

		trader.setShopId(ShopHandler.SHOP_NULL);
		rent.setRenter((UUID)null);
		rent.setRentedOn(0);
	}

	private class RentCheck implements Runnable {
		@Override
		public void run() {
			//System.out.println("do this every minute");
			for (NPC npc : rentalNPCs) {
				if (npc.hasTrait(RentalShop.class)) {
					RentalShop rental = npc.getTrait(RentalShop.class);
					if (!rental.isRented()) {
						continue;
					}
					//System.out.println("Checking if rental period is up for " + npc.getFullName());
					long current = System.currentTimeMillis();
					long expire = rental.getRentedOn() + rental.getTermInMilliseconds();
					//System.out.println(expire + "  " + current);
					if (expire <= current) {
						//System.out.println("rent has expired");
						if (rental.getRenterPlayer().get().isOnline()) {
							Bukkit.getPlayer(rental.getRenter().get()).sendMessage(ChatColor.GREEN + "[UltraTrader] " + ChatColor.YELLOW + "Your rent for " + npc.getFullName() + " has been paid.");
						}
						rentNPC(npc, rental.getRenterPlayer().get(), true);
					}
				} else {
					rentalNPCs.remove(npc);
				}
			}

		}
	}
}
