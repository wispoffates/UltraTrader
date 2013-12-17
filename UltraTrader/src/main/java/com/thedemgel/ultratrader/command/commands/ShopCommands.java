package com.thedemgel.ultratrader.command.commands;

import com.thedemgel.ultratrader.L;
import com.thedemgel.ultratrader.LimitHandler;
import com.thedemgel.ultratrader.UltraTrader;
import com.thedemgel.ultratrader.citizens.TraderTrait;
import com.thedemgel.ultratrader.citizens.UltraTrait;
import com.thedemgel.ultratrader.command.BukkitCommand;
import com.thedemgel.ultratrader.command.Commands;
import com.thedemgel.ultratrader.shop.Shop;
import com.thedemgel.ultratrader.util.Permissions;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.Trait;
import net.citizensnpcs.api.trait.trait.Owner;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;

public class ShopCommands extends Commands implements CommandExecutor {

	public ShopCommands(UltraTrader instance) {
	}

	@BukkitCommand(name = "myshops")
	public boolean getShops(CommandSender sender, Command cmd, String label, String[] args) {
		Collection<Shop> shops = UltraTrader.getStoreHandler().getShopsByOwner((Player) sender);

		if (shops.isEmpty()) {
			sender.sendMessage(ChatColor.GREEN + L.getString("shops.list.noshops"));
		} else {
			sender.sendMessage(ChatColor.YELLOW + L.getString("shops.list.yourshops"));
			for (Shop shop : shops) {
				sender.sendMessage(shop.getId() + ": " + shop.getName());
			}
		}

		return true;
	}

	@BukkitCommand(name = "create")
	public boolean createTrader(CommandSender sender, Command cmd, String label, String[] args) {
        if (!UltraTrader.getInstance().isCitizens()) {
            sender.sendMessage(ChatColor.RED + "This command only works with Citizens.");
            return true;
        }

		if (!sender.hasPermission(Permissions.NPC_CREATE)) {
			sender.sendMessage(ChatColor.RED + L.getString("general.create.nopermission"));
            return true;
		}

		NPC npc = CitizensAPI.getDefaultNPCSelector().getSelected(sender);

		if (npc == null) {
			sender.sendMessage(ChatColor.RED + L.getString("general.create.noselected"));
			return true;
		}

		if (npc.hasTrait(TraderTrait.class)) {
			sender.sendMessage(ChatColor.RED + L.getString("general.create.alreadytrader"));
			return true;
		}

		if (!npc.getTrait(Owner.class).isOwnedBy(sender)) {
			sender.sendMessage(ChatColor.RED + L.getString("general.create.notowner"));
			return true;
		}

		List<String> traits;
		if (args.length > 1) {
			traits = LimitHandler.getRequiredTraits((Player) sender, args[1]);
		} else {
			traits = LimitHandler.getRequiredTraits((Player) sender);
		}

		boolean canceled = false;

		for (String trait : traits) {
			Class<? extends Trait> traitClass = CitizensAPI.getTraitFactory().getTraitClass(trait);

			if (traitClass != null && traitClass.getSuperclass().equals(UltraTrait.class)) {

				npc.addTrait(traitClass);
				UltraTrait ultratrait = (UltraTrait) npc.getTrait(traitClass);
				if (!ultratrait.onAssign((Player) sender)) {
					canceled = true;
				}
			}
		}

		if (canceled) {
			for (String trait : traits) {
				Class<? extends Trait> traitClass = CitizensAPI.getTraitFactory().getTraitClass(trait);
				if (traitClass != null && traitClass.getSuperclass().equals(UltraTrait.class)) {
					if (npc.hasTrait(traitClass)) {
                        npc.removeTrait(traitClass);
                    }
				}
			}
		} else {
			if (!npc.hasTrait(TraderTrait.class)) {
				npc.addTrait(TraderTrait.class);
			}
			sender.sendMessage(ChatColor.GREEN + L.getString("general.create.success"));
		}

		return true;
	}
}
