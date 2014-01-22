package com.thedemgel.ultradenizen;

import com.thedemgel.ultratrader.UltraTrader;
import com.thedemgel.ultratrader.shop.Shop;
import net.aufdemrand.denizen.events.bukkit.ReplaceableTagEvent;
import net.aufdemrand.denizen.tags.Attribute;
import net.aufdemrand.denizen.utilities.debugging.dB;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class DTraderTags implements Listener {

    @EventHandler(priority = EventPriority.LOW)
    public void traderTags(ReplaceableTagEvent event) {
        // Build a new attribute out of the raw_tag
        Attribute attribute = new Attribute(event.raw_tag, event.getScriptEntry());

        // Shop tags
        Shop shop;

        if (event.matches("shop")) {
            // Shop <shop[id]...>
            if (attribute.hasContext(1)) {
                // Check for valid shop
                if (UltraTrader.getStoreHandler().getShops().containsKey(attribute.getIntContext(1))) {
                    shop = UltraTrader.getStoreHandler().getShop(attribute.getIntContext(1));
                    event.setReplaced(new dShop(shop).getAttribute(attribute.fulfill(1)));
                } else {
                    dB.echoError("Shop id " + attribute.getIntContext(1) + " was not found.");
                    return;
                }
            } else {
                dB.echoError("Shop id is required.");
                return;
            }
        }

        //attribute = attribute.fulfill(1);

        //if (attribute.startsWith("shop")) {
        //    System.out.println("SHOP ATTRIBUTE");
        //}
    }
}
