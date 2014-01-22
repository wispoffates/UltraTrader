package com.thedemgel.ultradenizen;

import com.thedemgel.ultratrader.UltraTrader;
import com.thedemgel.ultratrader.shop.Shop;
import net.aufdemrand.denizen.objects.Element;
import net.aufdemrand.denizen.objects.Fetchable;
import net.aufdemrand.denizen.objects.dItem;
import net.aufdemrand.denizen.objects.dObject;
import net.aufdemrand.denizen.tags.Attribute;
import net.aufdemrand.denizen.utilities.debugging.dB;

import java.math.BigDecimal;

public class dShop implements dObject {

    Shop shop = null;
    private String prefix = "Shop";

    @Fetchable("shop")
    public static dShop valueOf(String string) {
        if (string == null) return null;

        string = string.replace("shop@", "");

        if (UltraTrader.getStoreHandler().getShops().containsKey(Integer.valueOf(string))){
            return new dShop(UltraTrader.getStoreHandler().getShops().get(Integer.valueOf(string)));
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

    @Override
    public String identifySimple() {
        return identify();
    }

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

        // <--[tag]
        // @attribute <shop@shop.item[<id>]>
        // @returns Element(dItem)
        // @description
        // Returns Item type from item id
        // @plugin UltraTrader
        // -->
        if (attribute.startsWith("item")) {
            String id = attribute.getContext(1);

            dItem item;

            if (shop.getPriceList().containsKey(id)) {
                item = new dItem(shop.getPriceList().get(id).getItemStack());
            } else {
                return Element.NULL.getAttribute(attribute.fulfill(1));
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

                shop.getPriceList().get(id).setSellPrice(BigDecimal.valueOf(price));

                return new Element(price).getAttribute(attribute.fulfill(1));
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
