package com.thedemgel.ultratrader.command.commands;

import com.thedemgel.ultratrader.UltraTrader;
import com.thedemgel.ultratrader.InventoryHandler;
import com.thedemgel.ultratrader.L;
import com.thedemgel.ultratrader.command.BukkitCommand;
import com.thedemgel.ultratrader.command.Commands;
import com.thedemgel.ultratrader.shop.Shop;
import com.thedemgel.ultratrader.shop.StoreItem;
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

	//private UltraTrader plugin;

	public ShopCommands(UltraTrader instance) {
		//plugin = instance;
	}

	/*@BukkitCommand(name = "shopitem")
	public boolean something(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = (Player) sender;

		ItemStack item = new ItemStack(Material.WOOD_AXE, 1);
		StoreItem storeItem = new StoreItem();
		storeItem.createLinkedToShop(UltraTrader.getStoreHandler().getShop(1), item);

		player.getInventory().addItem(item);

		return true;
	}*/

	@BukkitCommand(name = "myshops")
	public boolean getShops(CommandSender sender, Command cmd, String label, String[] args) {
		Collection<Shop> shops = UltraTrader.getStoreHandler().getShopsByOwner((Player) sender);

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
