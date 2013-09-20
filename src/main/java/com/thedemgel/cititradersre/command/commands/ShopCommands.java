
package com.thedemgel.cititradersre.command.commands;

import com.thedemgel.cititradersre.CitiTrader;
import com.thedemgel.cititradersre.InventoryHandler;
import com.thedemgel.cititradersre.command.BukkitCommand;
import com.thedemgel.cititradersre.command.Commands;
import com.thedemgel.cititradersre.shop.Shop;
import com.thedemgel.cititradersre.shop.StoreItem;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


public class ShopCommands extends Commands implements CommandExecutor {
	private CitiTrader plugin;
	
	public ShopCommands(CitiTrader instance) {
		plugin = instance;
	}
	
	@BukkitCommand(name = "test")
	public boolean test(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = (Player) sender;
		InventoryHandler handler = new InventoryHandler(plugin);
		handler.createBuyInventoryView(player, plugin.getStoreHandler().getShop(1));
		handler.openInventory(player);
		return true;
		
	}
	@BukkitCommand(name = "shopitem")
	public boolean something(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = (Player) sender;

		/*List<String> lore = new ArrayList<>();
		lore.add("Use to create a store,");
		lore.add("click on any Block or Entity");
		lore.add("100 uses left.");*/

		ItemStack item = new ItemStack(Material.WOOD_AXE, 1);
		StoreItem storeItem = new StoreItem();
		storeItem.createLinkedToShop(plugin.getStoreHandler().getShop(1), item);
		/* ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("Store Item");
		meta.setLore(lore);
		item.setItemMeta(meta); */
		player.getInventory().addItem(item);
		
		/*List<ItemStack> items = plugin.test();
		for (ItemStack iteme : items) {
			player.getInventory().addItem(iteme);
		}*/
		return true;
	}

}
