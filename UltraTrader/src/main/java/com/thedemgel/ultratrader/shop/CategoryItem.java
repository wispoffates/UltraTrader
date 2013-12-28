package com.thedemgel.ultratrader.shop;

import com.google.common.collect.Iterables;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CategoryItem extends ItemStack implements ConfigurationSerializable {

    public static final String DEFAULT_CAT_SELL = "noneSell";
    public static final String DEFAULT_CAT_BUY = "noneBuy";

    //private ItemStack itemStack;
    private String categoryId;
    private int slot = -1;

    private List<String> lore = new ArrayList<>();

    public CategoryItem() {
        categoryId = "none";
    }

    public CategoryItem(ItemStack itemStack1) {
        super(itemStack1);
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int value) {
        slot = value;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String value) {
        categoryId = value;
        generateLore();
    }

    public ItemStack getItemStack() {
        return this;
    }

    public void setItemStack(Material value) {
        this.setType(value);
    }

    public void setLore(List<String> lore) {
        this.lore = lore;
        generateLore();
    }

    public List<String> getLore() {
        return lore;
    }

    public void setDisplayName(String displayName1) {
        ItemMeta meta = getItemMeta();

        meta.setDisplayName(displayName1);

        setItemMeta(meta);
    }

    public String getDisplayName() {
        if (getItemMeta().hasDisplayName()) {
            return getItemMeta().getDisplayName();
        }

        return "";
    }

    public static CategoryItem deserialize(Map<String, Object> args) {
        return new CategoryItem(ItemStack.deserialize(args));
    }

    public Map<String, Object> serialize() {
        return super.serialize();
    }

    public void generateLore() {
        ItemMeta itemMeta = getItemMeta();

        List<String> genLore = new ArrayList<>();

        genLore.addAll(lore);
        genLore.add(ChatColor.BLACK + getCategoryId());

        itemMeta.setLore(genLore);

        setItemMeta(itemMeta);
    }

    public static String getCategoryId(ItemStack item) {
        if (!item.hasItemMeta()) {
            return "000000";
        }

        if (!item.getItemMeta().hasLore()) {
            return "000000";
        }

        String id = Iterables.getLast(item.getItemMeta().getLore());
        return id.substring(2);
    }
}
