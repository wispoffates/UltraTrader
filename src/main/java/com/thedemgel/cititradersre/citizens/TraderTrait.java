
package com.thedemgel.cititradersre.citizens;

import net.citizensnpcs.api.persistence.Persist;
import net.citizensnpcs.api.trait.Trait;


public class TraderTrait extends Trait {

	@Persist private Integer shopid = -1;
	
	public TraderTrait() {
		super("cititrader");
	}
	
	public Integer getShopId() {
		return shopid;
	}
	
	public void setShopId(Integer value) {
		shopid = value;
	}
}
