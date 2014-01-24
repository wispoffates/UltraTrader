package com.thedemgel.ultradenizen.event;

import com.thedemgel.ultradenizen.dShop;
import com.thedemgel.ultratrader.events.PlayerPurchaseFromShopEvent;
import com.thedemgel.ultratrader.events.ShopPurchaseFromPlayerEvent;
import net.aufdemrand.denizen.events.EventManager;
import net.aufdemrand.denizen.events.SmartEvent;
import net.aufdemrand.denizen.objects.Element;
import net.aufdemrand.denizen.objects.dItem;
import net.aufdemrand.denizen.objects.dObject;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizen.utilities.DenizenAPI;
import net.aufdemrand.denizen.utilities.debugging.dB;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ItemSellEvent implements SmartEvent, Listener {

    @Override
    public boolean shouldInitialize(Set<String> events) {
        for (String event : events) {
            Matcher m = Pattern.compile("on item sell", Pattern.CASE_INSENSITIVE).matcher(event);

            if (m.matches()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void _initialize() {
        //Bukkit.getServer().getPluginManager().registerEvents(this, UltraDenizen.instance);
        DenizenAPI.getCurrentInstance().getServer().getPluginManager()
                .registerEvents(this, DenizenAPI.getCurrentInstance());
        dB.log("Loaded item sell SmartEvent.");
    }

    @Override
    public void breakDown() {
        PlayerPurchaseFromShopEvent.getHandlerList().unregister(this);
    }

    // <--[event]
    // @events shop purchase
    // @triggers when a player purchases from a shop
    // @context
    // <context.shop> dShop purchased from
    // <context.player> dPlayer who purchased the item
    // <context.item> dItem of the purchased item
    // <context.itemid> itemid of the item purchased
    // <context.price> price of the final purchase
    // @plugin UltraTrader
    // -->
    @EventHandler
    public void onPlayerPurchase(ShopPurchaseFromPlayerEvent event) {
        Map<String, dObject> context = new HashMap<>();
        dShop shop = new dShop(event.getShop());

        context.put("shop", shop);
        context.put("player", new dPlayer(event.getPlayer()));
        context.put("item", new dItem(event.getItemStack()));
        context.put("itemid", new Element(event.getItem().getId()));
        context.put("price", new Element(event.getPrice().doubleValue()));

        String determination = EventManager.doEvents(Arrays.asList("item sell"), null, event.getPlayer(), context).toUpperCase();

        if (determination.equals("CANCELLED")) {
            event.setCancelled(true);
        }

        event.setPrice(BigDecimal.valueOf(((Element) context.get("price")).asDouble()));
    }
}
