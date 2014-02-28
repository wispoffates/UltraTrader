
package com.thedemgel.ultratrader.citizens;

import net.citizensnpcs.api.persistence.Persist;
import net.citizensnpcs.api.trait.Trait;


public class TraderTrait extends Trait {

	@Persist private Integer shopid = -1;
    @Persist private boolean open = true;

	public TraderTrait() {
		super("ultratrader");
	}

	public Integer getShopId() {
		return shopid;
	}

	public void setShopId(Integer value) {
		shopid = value;
	}

    public boolean getOpen() {
        return open;
    }

    public void setOpen(boolean value) {
        open = value;
    }
}
