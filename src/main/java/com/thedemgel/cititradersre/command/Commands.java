package com.thedemgel.cititradersre.command;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Method[] methods = this.getClass().getDeclaredMethods();

		for (Method method : methods) {
			if (method.isAnnotationPresent(BukkitCommand.class)) {
				BukkitCommand bcmd = method.getAnnotation(BukkitCommand.class);
				if (args.length > 0) {
					if (bcmd.name().equalsIgnoreCase(args[0])) {
						if (bcmd.isPlayer()) {
							if (!(sender instanceof Player)) {
								sender.sendMessage("Must be a player to use this command.");
								return false;
							}
						}
						try {
							return (Boolean) method.invoke(this, sender, cmd, label, args);
						} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
							System.out.println(ex);
						}
					} else {
						sender.sendMessage("Command not found.");
					}
				} else {
					sender.sendMessage("Requires a sub-command.");
				}
			}
		}
		return false;
	}
}
