
package com.thedemgel.ultraframe;

import com.thedemgel.ultratrader.UltraTrader;
import org.bukkit.plugin.java.JavaPlugin;


public class UltraFrame extends JavaPlugin {
	@Override
	public void onEnable() {
		UltraTrader.getTraitHandler().registerTrait(UltraFrameTrait.class, "ultraframe");

		getServer().getPluginManager().registerEvents(new FrameListener(), this);
	}
}
