package com.thedemgel.ultratrader.command.commands;

import com.thedemgel.ultratrader.UltraTrader;
import com.thedemgel.ultratrader.InventoryHandler;
import com.thedemgel.ultratrader.L;
import com.thedemgel.ultratrader.command.BukkitCommand;
import com.thedemgel.ultratrader.command.Commands;
import com.thedemgel.ultratrader.shop.Shop;
import com.thedemgel.ultratrader.shop.StoreItem;
import com.thedemgel.ultratrader.util.PermissionPredicate;
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
import org.bukkit.permissions.PermissionAttachmentInfo;

public class ShopCommands extends Commands implements CommandExecutor {

	//private UltraTrader plugin;

	public ShopCommands(UltraTrader instance) {
		//plugin = instance;
	}

	@BukkitCommand(name = "test")
	public boolean test(final CommandSender sender, Command cmd, String label, String[] args) {
		PermissionPredicate perm = new PermissionPredicate();

		for (PermissionAttachmentInfo info : perm.getPermissions("trader.limit.test", (Player) sender)) {
			System.out.println(info.getPermission());
		}

		System.out.println(perm.getHighestPermissionSet("trader.limit.test", (Player) sender));

		return true;
	}

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
