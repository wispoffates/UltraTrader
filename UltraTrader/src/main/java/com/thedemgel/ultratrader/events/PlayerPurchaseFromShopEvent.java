package com.thedemgel.ultratrader.events;

import com.thedemgel.ultratrader.shop.ItemPrice;
import com.thedemgel.ultratrader.shop.Shop;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

import java.math.BigDecimal;

public class PlayerPurchaseFromShopEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;
    private ItemPrice item;
    private ItemStack itemStack;
    private BigDecimal price;
    private Shop shop;
    private Player player;

    public PlayerPurchaseFromShopEvent(Player player, ItemPrice item, Shop shop, ItemStack itemStack, BigDecimal price) {
        this.item = item;
        this.player = player;
        this.shop = shop;
        this.itemStack = itemStack;
        this.price = price;
    }

    public ItemPrice getItem() {
        return item;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal value) {
        price = value;
    }

    public Shop getShop() {
        return shop;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }
}
