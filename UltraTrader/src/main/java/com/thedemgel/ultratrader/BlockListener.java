package com.thedemgel.ultratrader;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockListener implements Listener {
    // TODO: Protect shop blocks...
    @EventHandler
    public void onBlockPlaceEvent(BlockPlaceEvent event) {
        Block against = event.getBlockAgainst();

        if (against.hasMetadata(BlockShopHandler.SHOP_METADATA_KEY)) {
            event.setBuild(false);
        }
    }

    @EventHandler
    public void onBlockDamageEvent(BlockDamageEvent event) {
        Block against = event.getBlock();
        Player player = event.getPlayer();

        if (against.hasMetadata(BlockShopHandler.SHOP_METADATA_KEY)) {
            if (against.hasMetadata(BlockShopHandler.SHOP_OWNER_KEY)) {
                String owner = against.getMetadata(BlockShopHandler.SHOP_OWNER_KEY).get(0).asString();

                if (player != null) {
                    if (owner.equals(player.getName())) {
                        player.sendMessage(ChatColor.RED + "[WARNING] You are Damaging your Shop");
                    }
                }
            }
            event.setCancelled(true);
        }
    }
}
