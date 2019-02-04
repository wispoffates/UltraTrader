package com.thedemgel.ultradenizen;

import com.thedemgel.ultratrader.UltraTrader;
import com.thedemgel.ultratrader.citizens.TraderTrait;
import com.thedemgel.ultratrader.shop.ItemPrice;
import com.thedemgel.ultratrader.shop.Shop;
import net.aufdemrand.denizen.objects.*;
import net.aufdemrand.denizen.tags.Attribute;
import net.aufdemrand.denizen.utilities.debugging.dB;
import net.citizensnpcs.api.npc.NPC;

import java.math.BigDecimal;

public class dShop implements dObject {

    Shop shop = null;
    private String prefix = "Shop";

    @Fetchable("shop")
    public static dShop valueOf(String string) {
        if (string == null) return null;

        string = string.replace("shop@", "");

        dNPC npc = dNPC.valueOf(string);

        if (npc != null) {
            NPC citiNpc = npc.getCitizen();

            if (citiNpc.hasTrait(TraderTrait.class)) {
                TraderTrait trait = citiNpc.getTrait(TraderTrait.class);
                int id = trait.getShopId();
                return new dShop(UltraTrader.getStoreHandler().getShop(id));
            } else {
                dB.log("NPC doesn't have tradertrait.");
                return null;
            }
        }

        try {
            //if (UltraTrader.getStoreHandler().getShops().containsKey(Integer.valueOf(string))){
                return new dShop(UltraTrader.getStoreHandler().getShop(Integer.valueOf(string)));
            //}
        } catch (NumberFormatException e) {
            dB.echoError("Parameter passed is not a Number or Shop.");
        }

        return null;
    }

    public static boolean matches(String arg) {
        if (valueOf(arg) != null)
            return true;

        return false;
    }

    public dShop(Shop shop) {
        if (shop != null) {
            this.shop = shop;
        } else {
            dB.echoError("Shop referenced is null!");
        }
    }

    public Shop getShop() {
        return shop;
    }

    @Override
    public String getPrefix() {
        return prefix;
    }

    @Override
    public String debug() {
        return (prefix + "='<A>" + identify() + "<G>' ");
    }

    @Override
    public boolean isUnique() {
        return true;
    }

    @Override
    public String getObjectType() {
        return "Shop";
    }

    @Override
    public String identify() {
        return "shop@" + shop.getId();
    }

    /*
    @Override
    public String identifySimple() {
        return identify();
    }*/

    @Override
    public dObject setPrefix(String s) {
        this.prefix = s;
        return this;
    }

    @Override
    public String getAttribute(Attribute attribute) {
        // <--[tag]
        // @attribute <shop@shop.name>
        // @returns Element(String)
        // @description
        // Returns The current name of the shop.
        // @plugin UltraTrader
        // -->
        if (attribute.startsWith("name")) {
            return new Element(shop.getName()).getAttribute(attribute.fulfill(1));
        }

        if (attribute.startsWith("id")) {
            return new Element(shop.getId()).getAttribute(attribute.fulfill(1));
        }

        if (attribute.startsWith("owner")) {
            return new Element(shop.getOwner()).getAttribute(attribute.fulfill(1));
        }

        /**
         * [START: tag]
         * ## shop@#.item[dItem]
         * + example: `shop@3.item[i@glass]`
         * + returns: dItem
         *
         * `Returns an item from a shop if both the Shop and the Item exist.`
         *
         * `This can also be used with the Item ID (faster and less resource intensive). To find the ID of the item
         * either narrate the with shop@3.item[i@glass].id or look in the shops YML file, or is you have excellent
         * vision, you can find it listed on each item in the shop in-game.`
         * [END: tag]
         */
        if (attribute.startsWith("item")) {
            String arg = attribute.getContext(1);

            // Check if arg is dItem
            dItem check = dItem.valueOf(arg);

            ItemPrice itemPrice;
            dItem item;

            if (check == null) {
                if (shop.getPriceList().containsKey(arg)) {
                    itemPrice = shop.getPriceList().get(arg);
                    item = new dItem(itemPrice.getItemStack());
                } else {
                    return Element.NULL.getAttribute(attribute.fulfill(1));
                }
            } else {
                itemPrice = shop.getItemPriceIfInStore(check.getItemStack());

                if (itemPrice != null) {
                    item = new dItem(itemPrice.getItemStack());
                } else {
                    return Element.NULL.getAttribute(attribute.fulfill(1));
                }
            }

            attribute.fulfill(1);

            if (attribute.startsWith("inventory")) {
                int inv = 0;

                if (shop.getInventory().containsKey(item.getItemStack())) {
                    inv = shop.getInventory().get(item.getItemStack());
                }

                return new Element(inv).getAttribute(attribute.fulfill(1));
            }

            if (attribute.startsWith("setsellprice")) {
                double price = attribute.getDoubleContext(1);

                //shop.getPriceList().get(id).setSellPrice(BigDecimal.valueOf(price));
                itemPrice.setSellPrice(BigDecimal.valueOf(price));

                return new Element(price).getAttribute(attribute.fulfill(1));
            }

            if (attribute.startsWith("setbuyprice")) {
                double price = attribute.getDoubleContext(1);

                itemPrice.setBuyPrice(BigDecimal.valueOf(price));

                return new Element(price).getAttribute(attribute.fulfill(1));
            }

            if (attribute.startsWith("sellprice")) {
                BigDecimal price = itemPrice.getSellPrice();

                return new Element(price.doubleValue()).getAttribute(attribute.fulfill(1));
            }

            if (attribute.startsWith("buyprice")) {
                BigDecimal price = itemPrice.getBuyPrice();

                return new Element(price.doubleValue()).getAttribute(attribute.fulfill(1));
            }

            return item.getAttribute(attribute);
        }

        // <--[tag]
        // @attribute <shop@<shop>.inventory[<dItem>]>
        // @returns Element(Integer)
        // @description
        // Returns The current owner of the shop.
        // @plugin UltraTrader
        // -->
        if (attribute.startsWith("inventory")) {
            dItem item = dItem.valueOf(attribute.getContext(1));

            int inv = 0;

            if (shop.getInventory().containsKey(item.getItemStack())) {
                inv = shop.getInventory().get(item.getItemStack());
            }

            for (String att : attribute.attributes) {
                attribute.getAttribute(2);
            }

            return new Element(inv).getAttribute(attribute.fulfill(1));
        }

        return new Element(identify()).getAttribute(attribute);
    }
}
