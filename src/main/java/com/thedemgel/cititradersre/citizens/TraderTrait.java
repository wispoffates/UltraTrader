
package com.thedemgel.cititradersre.citizens;

import net.citizensnpcs.api.persistence.Persist;
import net.citizensnpcs.api.trait.Trait;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;


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
