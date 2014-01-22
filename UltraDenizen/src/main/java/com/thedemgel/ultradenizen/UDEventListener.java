package com.thedemgel.ultradenizen;

import com.thedemgel.ultratrader.events.PlayerPurchaseFromShopEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class UDEventListener implements Listener {

    @EventHandler
    public void purchaseItem(PlayerPurchaseFromShopEvent event) {
        System.out.println(event.getPlayer() + " purchased " + event.getItemStack().getType() + " for " + event.getPrice().toPlainString() + " from " + event.getShop().getName());
    }
}
