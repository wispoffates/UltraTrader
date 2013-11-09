package com.thedemgel.ultratrader.command.commands;

import com.thedemgel.ultratrader.UltraTrader;
import com.thedemgel.ultratrader.InventoryHandler;
import com.thedemgel.ultratrader.L;
import com.thedemgel.ultratrader.LimitHandler;
import com.thedemgel.ultratrader.citizens.TraderTrait;
import com.thedemgel.ultratrader.command.BukkitCommand;
import com.thedemgel.ultratrader.command.Commands;
import com.thedemgel.ultratrader.shop.Shop;
import com.thedemgel.ultratrader.shop.StoreItem;
import com.thedemgel.ultratrader.util.PermissionPredicate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.trait.Owner;
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

	@BukkitCommand(name = "create")
	public boolean createTrader(CommandSender sender, Command cmd, String label, String[] args) {
		NPC npc = CitizensAPI.getDefaultNPCSelector().getSelected(sender);

		if (npc == null) {
			sender.sendMessage("You must have an NPC selected.");
			return true;
		}

		if (npc.hasTrait(TraderTrait.class)) {
			sender.sendMessage("NPC is already a trader.");
			return true;
		}

		if (!npc.getTrait(Owner.class).isOwnedBy(sender)) {
			sender.sendMessage("You need to own the NPC to create a trader.");
			return true;
		}

		List<String> traits = LimitHandler.getRequiredTraits((Player) sender);

		for (String trait : traits) {
			System.out.println(trait);
			System.out.println(CitizensAPI.getTraitFactory().getTraitClass("rentalshop"));
		}

		return true;
	}
}
