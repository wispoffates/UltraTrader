package com.thedemgel.ultratrader.command.commands;

import com.thedemgel.ultratrader.Lang;
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
        String player = sender.getName();

        if (args.length == 2) {
            player = args[1];
        } else if (args.length > 2) {
            return true;
        }

		Collection<Shop> shops = UltraTrader.getStoreHandler().getShopsByOwner(player);

		if (shops.isEmpty()) {
			sender.sendMessage(ChatColor.GREEN + Lang.getString("shops.list.noshops"));
		} else {
			sender.sendMessage(ChatColor.YELLOW + Lang.getString("shops.list.yourshops"));
			for (Shop shop : shops) {
				sender.sendMessage(ChatColor.GRAY + "[" + ChatColor.WHITE + shop.getId() + ChatColor.GRAY + "] " + ChatColor.WHITE + shop.getName());
			}
		}

		return true;
	}

	@BukkitCommand(name = "create")
	public boolean createTrader(CommandSender sender, Command cmd, String label, String[] args) {
        if (!UltraTrader.getInstance().isCitizens()) {
            sender.sendMessage(ChatColor.RED + Lang.getString("general.nocitizens"));
            return true;
        }

		if (!sender.hasPermission(Permissions.NPC_CREATE)) {
			sender.sendMessage(ChatColor.RED + Lang.getString("general.create.nopermission"));
            return true;
		}

		NPC npc = CitizensAPI.getDefaultNPCSelector().getSelected(sender);

		if (npc == null) {
			sender.sendMessage(ChatColor.RED + Lang.getString("general.create.noselected"));
			return true;
		}

		if (npc.hasTrait(TraderTrait.class)) {
			sender.sendMessage(ChatColor.RED + Lang.getString("general.create.alreadytrader"));
			return true;
		}

		if (!npc.getTrait(Owner.class).isOwnedBy(sender)) {
			sender.sendMessage(ChatColor.RED + Lang.getString("general.create.notowner"));
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
			sender.sendMessage(ChatColor.GREEN + Lang.getString("general.create.success"));
		}

		return true;
	}

    @BukkitCommand(name = "delete")
    public boolean deleteShop(CommandSender sender, Command cmd, String label, String[] args) {
        int shopId;

        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + Lang.getString("commands.delete.idrequired"));
            return true;
        } else {
            try {
                shopId = Integer.valueOf(args[1]);
            } catch (Exception e) {
                e.printStackTrace();
                sender.sendMessage(ChatColor.RED + Lang.getString("commands.delete.idisanumber"));
                return true;
            }
        }

        Shop shop = UltraTrader.getStoreHandler().getShop(shopId);

        if (shop == null) {
            sender.sendMessage(ChatColor.RED + Lang.getFormatString("commands.delete.doesnotexist", shopId));
            return true;
        } else if (!shop.getOwner().equals(sender.getName()) && !sender.isOp()) {
            sender.sendMessage(ChatColor.RED + Lang.getString("commands.delete.notowner"));
            return true;
        }

        UltraTrader.getStoreHandler().deleteShop(shop);
        return true;
    }

    @BukkitCommand(name = "info")
    public boolean npcInfo(CommandSender sender, Command cmd, String label, String[] args) {
        return true;
    }
}
