package com.thedemgel.ultratrader.command;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.thedemgel.ultratrader.Lang;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Method[] methods = this.getClass().getDeclaredMethods();
		if (args.length == 0) {
			sender.sendMessage(ChatColor.RED + Lang.getString("general.requiresubcommand"));
			return false;
		}
		for (Method method : methods) {
			if (method.isAnnotationPresent(BukkitCommand.class)) {
				BukkitCommand bcmd = method.getAnnotation(BukkitCommand.class);

				if (bcmd.name().equalsIgnoreCase(args[0])) {
					if (bcmd.isPlayer()) {
						if (!(sender instanceof Player)) {
							sender.sendMessage(Lang.getString("general.consoleonly"));
							return false;
						}
					}
					try {
						return (Boolean) method.invoke(this, sender, cmd, label, args);
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
						System.out.println(ex);
						ex.printStackTrace();
					}
				}

			}
		}
		sender.sendMessage(ChatColor.RED + Lang.getString("general.commandnotfound"));
		return false;
	}
}