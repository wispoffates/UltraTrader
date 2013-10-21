
package com.thedemgel.ultratrader.citizens;

import net.citizensnpcs.api.persistence.Persist;
import net.citizensnpcs.api.trait.Trait;
import org.bukkit.entity.Player;


public class RentalShop extends Trait {

	// Player renting shop
	@Persist private String renter = "";
	// Shop id of rented shop
	@Persist private Integer shopid = -1;
	// Time shop was rented
	// 0 if not rented
	@Persist private long rentedOn = 0;

	// Settings
	// Time in milliseconds that the trader can be rented
	@Persist private long term;
	// Cost per term for the rented shop
	@Persist private double cost;

	public RentalShop() {
		super("rentalshop");
	}

	public String getRenter() {
		return renter;
	}

	public void setRenter(String value) {
		renter = value;
	}

	public void setRenter(Player player) {
		renter = player.getName();
	}
}
