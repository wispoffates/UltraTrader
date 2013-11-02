package com.thedemgel.ultratrader.citizens;

import com.thedemgel.ultratrader.RentalHandler;
import net.citizensnpcs.api.persistence.Persist;
import net.citizensnpcs.api.trait.Trait;
import org.bukkit.entity.Player;


public class RentalShop extends Trait {

	// Player renting shop
	//@Persist
	private String renter = "";
	// Shop id of rented shop
	//@Persist private Integer shopid = -1;
	// Time shop was rented
	// 0 if not rented
	@Persist private long rentedOn = 0;

	// Settings
	// Time in milliseconds that the trader can be rented
	@Persist private long term = 0L;
	// Cost per term for the rented shop
	@Persist private double cost = 0D;
	// Enable or Disable the shop (so admins can shut them off)
	@Persist private boolean enabled = false;

	public RentalShop() {
		super("rentalshop");
	}

	public String getRenter() {
		return renter;
	}

	public boolean isRented() {
		if ("".equals(renter)) {
			return false;
		}

		return true;
	}

	@Override
	public void onSpawn() {
		RentalHandler.registerRentalNPC(npc);
	}

	@Override
	public void onDespawn() {
		RentalHandler.unregisterRentalNPC(npc);
	}

	public void setRenter(String value) {
		renter = value;
	}

	public void setRenter(Player player) {
		renter = player.getName();
	}

	public long getRentedOn() {
		return rentedOn;
	}

	public long getTerm() {
		return term;
	}

	/*public String getFormatedTerm() {
		return TimeFormat.getDurationBreakdown(term);
	}*/

	public void setTerm(long term) {
		this.term = term;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
