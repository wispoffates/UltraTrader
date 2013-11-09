
package com.thedemgel.ultraworldguard;

import com.thedemgel.ultratrader.UltraTrader;
import org.bukkit.plugin.java.JavaPlugin;


public class UltraWorldGuard extends JavaPlugin {

	@Override
	public void onEnable() {
		UltraTrader.getTraitHandler().registerTrait(WorldGuardOwnerTrait.class, "worldguardowner");
	}
}
