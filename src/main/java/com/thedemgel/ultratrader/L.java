
package com.thedemgel.ultratrader;

import com.thedemgel.yamlresourcebundle.YamlResourceBundle;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import org.bukkit.Bukkit;


public class L {
	private static ResourceBundle rb;
	
	static {
		UltraTrader trader = UltraTrader.getInstance();
		Locale locale = new Locale(trader.getConfig().getString("language", "en"));
		rb = YamlResourceBundle.getBundle("lang.default", locale, trader.getDataFolder());
	}
	
	public static String getString(String search) {
		try {
			return rb.getString(search);
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
