
package com.thedemgel.ultratrader.citizens;

import net.citizensnpcs.api.persistence.Persist;
import net.citizensnpcs.api.trait.Trait;


public class RentalShop extends Trait {

	@Persist private String player = "";
	
	public RentalShop() {
		super("rentalshop");
	}
	
	public String getPlayer() {
		return player;
	}
	
	public void setPlayer(String value) {
		player = value;
	}
}
