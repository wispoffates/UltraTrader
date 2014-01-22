package com.thedemgel.ultradenizen;

import net.aufdemrand.denizen.Denizen;
import net.aufdemrand.denizen.objects.ObjectFetcher;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.logging.Level;

public class UltraDenizen extends JavaPlugin {
    private Denizen denizens;

    @Override
    public void onEnable() {
        checkDenizen();

        getServer().getPluginManager().registerEvents(new UDEventListener(), this);

        Bukkit.getServer().getPluginManager().registerEvents(new DTraderTags(), this);
        new UltraCommands().activate().as("ultratrader").withOptions("see documentation", 2);

        try {
            ObjectFetcher.registerWithObjectFetcher(dShop.class);
            ObjectFetcher._initialize();
        } catch (NoSuchMethodException | ClassNotFoundException | IOException e) {
            Bukkit.getLogger().severe("[UltraDenizen] Error loading Denizen ObjectFetcher. UltraDenizen tags may not function correctly.");
            e.printStackTrace();
        }

        getLogger().log(Level.INFO, "Fully Enabled...");
    }

    @Override
    public void onDisable() {
        getLogger().log(Level.INFO, "Disabled...");
    }

    private void checkDenizen() {
        denizens = (Denizen) getServer().getPluginManager().getPlugin("Denizen");

        if (denizens == null) {
            getLogger().log(Level.SEVERE, "Denizen not found, Disabling.");
            this.getPluginLoader().disablePlugin(this);
        } else {
            getLogger().log(Level.INFO, "Denizen found, Hooking Denizen");
        }
    }
}
