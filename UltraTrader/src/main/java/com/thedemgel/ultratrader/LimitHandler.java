
package com.thedemgel.ultratrader;

import com.thedemgel.ultratrader.shop.Shop;
import com.thedemgel.ultratrader.util.ConfigValue;
import com.thedemgel.ultratrader.util.PermissionPredicate;
import com.thedemgel.ultratrader.util.Permissions;
import java.util.List;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;


public class LimitHandler {
	public static final int INV_INCREASE_PER_LEVEL = 9;
	private static final ConfigAccessor config;

	static {
		config = new ConfigAccessor(UltraTrader.getInstance(), "limits.yml");
		config.getConfig().options().copyDefaults(true);
		config.saveConfig();
	}

	public static void init() {}

	public static ConfigurationSection getLimitsConfigSection() {
		if (config.getConfig().isConfigurationSection("limits")) {
			return config.getConfig().getConfigurationSection("limits");
		} else {
			return config.getConfig().createSection("limits");
		}
	}

	public static ConfigurationSection getDefaultsSection() {
		if (config.getConfig().isConfigurationSection("default")) {
			return config.getConfig().getConfigurationSection("default");
		} else {
			return config.getConfig().createSection("default");
		}
	}

	public static void reloadConfig() {
		config.reloadConfig();
	}

	private static ConfigurationSection getLimit(Player player) {
		for (String limit : getLimitsConfigSection().getKeys(false)) {
			if (player.hasPermission("trader.limit." + limit) && !limit.equals("default")) {
				//System.out.println(limit);
				return getLimitsConfigSection().getConfigurationSection(limit);
			}
		}

		// Always return default section, even if it doesn't exist
		return getDefaultsSection();
	}

	public static int getMaxBuySellSize(Shop shop) {
		int size = shop.getLevel() * LimitHandler.INV_INCREASE_PER_LEVEL;
		return size;
	}

	public static int getMaxLevel(Player player) {

		// Start player limit override.
		PermissionPredicate pred = new PermissionPredicate();

		int maxl = pred.getHighestPermissionSet(Permissions.SHOP_LIMIT_MAXLEVEL, player);

		if (maxl > 0) {
			return maxl;
		}
		// End player limit override

		ConfigurationSection section = getLimit(player);
		ConfigValue<Integer> maxlevel = getConfigValue(section, "maxlevel");

		if (maxlevel == null) {
			maxlevel = new ConfigValue(1);
		}

		return maxlevel.getValue();
	}

	public static int getLevelAtCreate(Player player) {
		// Start player limit override.
		PermissionPredicate pred = new PermissionPredicate();

		int createlevel = pred.getHighestPermissionSet(Permissions.SHOP_LIMIT_CREATE_LEVEL, player);

		if (createlevel > 0) {
			return createlevel;
		}
		// End player limit override

		ConfigurationSection section = getLimit(player);
		ConfigValue<Integer> level = getConfigValue(section, "defaults.level");

		if (level == null) {
			level = new ConfigValue(1);
		}

		return level.getValue();
	}

	public static int getMaxShops(Player player) {
		ConfigurationSection section = getLimit(player);
		ConfigValue<Integer> maxshops = getConfigValue(section, "maxshops");

		if (maxshops == null) {
			maxshops = new ConfigValue(1);
		}

		return maxshops.getValue();
	}

	public static double getRemoteActivateCost(Player player) {
		ConfigurationSection section = getLimit(player);
		ConfigValue remoteCost = getConfigValue(section, "costs.remote.activate");

		if (remoteCost == null) {
			remoteCost = new ConfigValue(10000);
		}

		if (remoteCost.getValue() instanceof Integer) {
			Integer cost = (Integer) remoteCost.getValue();
			return cost.doubleValue();
		} else {
			Double cost = (Double) remoteCost.getValue();
			return cost;
		}
	}

	public static double getRemoteItemCost(Player player) {
		ConfigurationSection section = getLimit(player);
		ConfigValue remoteCost = getConfigValue(section, "costs.remote.item");

		if (remoteCost == null) {
			remoteCost = new ConfigValue(10000);
		}

		if (remoteCost.getValue() instanceof Integer) {
			Integer cost = (Integer) remoteCost.getValue();
			return cost.doubleValue();
		} else {
			Double cost = (Double) remoteCost.getValue();
			return cost;
		}
	}

	public static boolean canEnableRemoteAccess(Player player) {
		ConfigurationSection section = getLimit(player);
		ConfigValue<Boolean> canRemote = getConfigValue(section, "remote");
		return canRemote.getValue();
	}

	public static double getCreateCost(Player player) {
		ConfigurationSection section = getLimit(player);
		ConfigValue createCost = getConfigValue(section, "costs.shop.create");

		if (createCost == null) {
			createCost = new ConfigValue(10000);
		}

		if (createCost.getValue() instanceof Integer) {
			Integer cost = (Integer) createCost.getValue();
			return cost.doubleValue();
		} else {
			Double cost = (Double) createCost.getValue();
			return cost;
		}
	}

	public static double getLevelCost(Player player, int level) {
		ConfigurationSection section = getLimit(player);
		ConfigValue levelCost = getConfigValue(section, "costs.level." + level);

		if (levelCost == null) {
			levelCost = new ConfigValue(10000);
		}

		if (levelCost.getValue() instanceof Integer) {
			Integer cost = (Integer) levelCost.getValue();
			return cost.doubleValue();
		} else {
			Double cost = (Double) levelCost.getValue();
			return cost;
		}
	}

	public static boolean canCreate(Player player) {
		int shopcount = UltraTrader.getStoreHandler().getShopsByOwner(player).size();

		if (getMaxShops(player) == -1) {
			return true;
		} else if (shopcount < getMaxShops(player)) {
			return true;
		} else {
			return false;
		}
	}

	private static ConfigValue getConfigValue(ConfigurationSection section, String search) {
		if (section.contains(search)) {
			return new ConfigValue(section.get(search));
		}

		// Attempt to get the default config.
		// If not set, throw error and return null.
		section = getDefaultsSection();

		if (section.contains(search)) {
			return new ConfigValue(section.get(search));
		}

		Bukkit.getLogger().log(Level.WARNING, "Config value not found in limits.yml in default section: " + search);
		return null;
	}

	public static boolean canOwnShop(Shop shop, Player player) {
		int maxlevel = getMaxLevel(player);

		if (canCreate(player) && shop.getLevel() <= maxlevel) {
			return true;
		}

		return false;
	}

	public static List<String> getRequiredTraits(Player player) {
		// Start player limit override.
		PermissionPredicate pred = new PermissionPredicate();

		List<String> traits = pred.getPermissionValues(Permissions.SHOP_LIMIT_TRAITS_REQUIRED, player);

		if (player.hasPermission(Permissions.SHOP_LIMIT_TRAITS_REQUIRED_OVERRIDE)) {
			return traits;
		}
		// End player limit override
		ConfigurationSection section = getLimit(player);

		return traits;
	}
}
