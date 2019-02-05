
package com.thedemgel.ultratrader.command.commands;

import com.thedemgel.ultratrader.Lang;
import com.thedemgel.ultratrader.UltraTrader;
import com.thedemgel.ultratrader.command.BukkitCommand;
import com.thedemgel.ultratrader.command.Commands;
import com.thedemgel.ultratrader.util.Permissions;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;


public class DebugCommands extends Commands implements CommandExecutor {

	@BukkitCommand(name = "debug")
	public boolean debugOn(CommandSender sender, Command cmd, String label, String[] args) {
		if (!sender.hasPermission(Permissions.ADMIN_DEBUG)) {
			sender.sendMessage(ChatColor.RED + Lang.getString("permission.debug.deny"));
			return true;
		}

		UltraTrader.getInstance().toggleDebug();
		if (UltraTrader.getInstance().isDebug()) {
			sender.sendMessage(ChatColor.GREEN + Lang.getString("debug.enable"));
		} else {
			sender.sendMessage(ChatColor.GREEN + Lang.getString("debug.disable"));
		}

		return true;
	}

    @BukkitCommand(name = "reload")
    public boolean reloadConfig(CommandSender sender, Command cmd, String label, String[] args) {
        return true;
    }

}
