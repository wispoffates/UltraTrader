
package com.thedemgel.ultratrader.command.commands;

import com.thedemgel.ultratrader.command.BukkitCommand;
import com.thedemgel.ultratrader.command.Commands;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;


public class PlayerCommands extends Commands implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		return super.onCommand(sender, cmd, label, args);
	}

    @BukkitCommand(name = "credits")
    public boolean credits(CommandSender sender, Command cmd, String label, String[] args) {
        sender.sendMessage(ChatColor.AQUA + "Authors:");
        sender.sendMessage(ChatColor.GREEN + "  Tenowg - Creator, Author");
        sender.sendMessage(ChatColor.AQUA + "Testers:");
        sender.sendMessage(ChatColor.GREEN + "  No one to add yet...");
        sender.sendMessage(ChatColor.AQUA + "Special Thanks:");
        sender.sendMessage(ChatColor.GREEN + "  Whiteescape.com - hosting my first test server.");
        return true;
    }
}
