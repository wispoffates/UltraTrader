package com.thedemgel.cititradersre.command.commands;

import com.thedemgel.cititradersre.CitiTrader;
import com.thedemgel.cititradersre.InventoryHandler;
import com.thedemgel.cititradersre.L;
import com.thedemgel.cititradersre.command.BukkitCommand;
import com.thedemgel.cititradersre.command.Commands;
import com.thedemgel.cititradersre.shop.Shop;
import com.thedemgel.cititradersre.shop.StoreItem;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.LazyMetadataValue;

public class ShopCommands extends Commands implements CommandExecutor {

	//private CitiTrader plugin;

	public ShopCommands(CitiTrader instance) {
		//plugin = instance;
	}

	/*@BukkitCommand(name = "shopitem")
	public boolean something(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = (Player) sender;

		ItemStack item = new ItemStack(Material.WOOD_AXE, 1);
		StoreItem storeItem = new StoreItem();
		storeItem.createLinkedToShop(CitiTrader.getStoreHandler().getShop(1), item);

		player.getInventory().addItem(item);

		return true;
	}*/

	@BukkitCommand(name = "myshops")
	public boolean getShops(CommandSender sender, Command cmd, String label, String[] args) {
		Collection<Shop> shops = CitiTrader.getStoreHandler().getShopsByOwner((Player) sender);

		if (shops.isEmpty()) {
			sender.sendMessage(L.getString("shops.list.noshops"));
		} else {
			sender.sendMessage(L.getString("shops.list.yourshops"));
			for (Shop shop : shops) {
				sender.sendMessage(shop.getId() + ": " + shop.getName());
			}
		}

		return true;
	}
}
