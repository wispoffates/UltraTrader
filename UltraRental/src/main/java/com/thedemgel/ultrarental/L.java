
package com.thedemgel.ultrarental;

import com.thedemgel.ultratrader.UltraTrader;
import com.thedemgel.yamlresourcebundle.YamlResourceBundle;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;


public class L {
	private static ResourceBundle rb;

	static {
		UltraRental trader = UltraRental.getInstance();
		Locale locale = new Locale(trader.getConfig().getString("language", "en"));
		rb = YamlResourceBundle.getBundle("lang.default", locale, trader.getDataFolder());
	}

	public static String getString(String search) {
		String retString;
		try {
			retString = rb.getString(search);
			if (UltraTrader.getInstance().isDebug()) {
				Bukkit.getLogger().log(Level.INFO, "(" + search + ") " + retString);
			}
			return retString;
		} catch (Exception e) {
			Bukkit.getLogger().log(Level.WARNING, "'" + search + "'" + " Not found in language file.");
			return "[error]";
		}
	}

	public static String getFormatString(String search, Object... args) {
		String returnval;
		try {
			returnval = MessageFormat.format(getString(search), args);
		} catch (Exception e) {
			returnval = "[formaterror]";
		}
		return returnval;
	}
}
