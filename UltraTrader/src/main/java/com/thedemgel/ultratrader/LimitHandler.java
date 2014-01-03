
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
	private static final int INV_INCREASE_PER_LEVEL = 9;
    private static final String BASE_CONFIG_LIMITS = "defaults";

	public static int getMaxBuySellSize(Shop shop) {
		return shop.getLevel() * LimitHandler.INV_INCREASE_PER_LEVEL;
	}

	public static int getMaxLevel(Player player) {
		PermissionPredicate pred = new PermissionPredicate();

		Integer maxLevel = pred.getHighestPermissionSet(Permissions.SHOP_LIMIT_MAXLEVEL, player);

		if (maxLevel != null) {
			return maxLevel;
		}

        return UltraTrader.getInstance().getConfig().getInt(BASE_CONFIG_LIMITS + "." + Permissions.SHOP_LIMIT_MAXLEVEL);
	}

    public static int getMaxCategory(Player player) {
        PermissionPredicate pred = new PermissionPredicate();

        Integer maxCategory = pred.getHighestPermissionSet(Permissions.SHOP_LIMIT_CATEGORY, player);

        if (maxCategory != null) {
            return maxCategory;
        }

        return UltraTrader.getInstance().getConfig().getInt(BASE_CONFIG_LIMITS + "." + Permissions.SHOP_LIMIT_CATEGORY);
    }

	public static int getLevelAtCreate(Player player) {
		PermissionPredicate pred = new PermissionPredicate();

		Integer createLevel = pred.getHighestPermissionSet(Permissions.SHOP_LIMIT_CREATE_LEVEL, player);

		if (createLevel != null) {
			return createLevel;
		}

        return UltraTrader.getInstance().getConfig().getInt(BASE_CONFIG_LIMITS + "." + Permissions.SHOP_LIMIT_CREATE_LEVEL);
	}

	public static int getMaxShops(Player player) {
        PermissionPredicate pred = new PermissionPredicate();

        Integer maxShop = pred.getHighestPermissionSet(Permissions.SHOP_LIMIT_MAXSHOPS, player);

        if (maxShop != null) {
            return maxShop;
        }

        return UltraTrader.getInstance().getConfig().getInt(BASE_CONFIG_LIMITS + "." + Permissions.SHOP_LIMIT_MAXSHOPS);
	}

	public static double getRemoteActivateCost(Player player) {
        PermissionPredicate pred = new PermissionPredicate();

        Integer remoteItemCost = pred.getHighestPermissionSet(Permissions.SHOP_LIMIT_ITEM_ACTIVATE_COST, player);

        if (remoteItemCost != null) {
            return remoteItemCost.doubleValue();
        }

        return UltraTrader.getInstance().getConfig().getDouble(BASE_CONFIG_LIMITS + "." + Permissions.SHOP_LIMIT_ITEM_ACTIVATE_COST);
	}

	public static double getRemoteItemCost(Player player) {
        // Start player limit override.
        PermissionPredicate pred = new PermissionPredicate();

        Integer itemCost = pred.getHighestPermissionSet(Permissions.SHOP_LIMIT_REMOTE_ITEM_COST, player);

        if (itemCost != null) {
            return itemCost.doubleValue();
        }
        // End player limit override

        // Begin new Defaults check
        return UltraTrader.getInstance().getConfig().getDouble(BASE_CONFIG_LIMITS + "." + Permissions.SHOP_LIMIT_REMOTE_ITEM_COST);
	}

	public static boolean canEnableRemoteAccess(Player player) {
        // Start player limit override.
        PermissionPredicate pred = new PermissionPredicate();

        Boolean enableRemote = pred.getBooleanPermission(Permissions.SHOP_LIMIT_ENABLE_REMOTE, player);

        if (enableRemote != null) {
            return enableRemote;
        }
        // End player limit override

        // Begin new Defaults check
        return UltraTrader.getInstance().getConfig().getBoolean(BASE_CONFIG_LIMITS + "." + Permissions.SHOP_LIMIT_ENABLE_REMOTE);
	}

	public static double getCreateCost(Player player) {
        // Start player limit override.
        PermissionPredicate pred = new PermissionPredicate();

        Integer storeCreateCost = pred.getHighestPermissionSet(Permissions.SHOP_LIMIT_CREATE_COST, player);

        if (storeCreateCost != null) {
            return storeCreateCost.doubleValue();
        }
        // End player limit override

        // Begin new Defaults check
        return UltraTrader.getInstance().getConfig().getDouble(BASE_CONFIG_LIMITS + "." + Permissions.SHOP_LIMIT_CREATE_COST);
	}

	public static double getLevelCost(Player player, int level) {
        // Start player limit override.
        PermissionPredicate pred = new PermissionPredicate();

        Integer storeLevelCost = pred.getHighestPermissionSet(Permissions.SHOP_LIMIT_LEVEL_COST + "." + level, player);

        if (storeLevelCost != null) {
            return storeLevelCost.doubleValue();
        }
        // End player limit override

        // Begin new Defaults check
        return UltraTrader.getInstance().getConfig().getDouble(BASE_CONFIG_LIMITS + "." + Permissions.SHOP_LIMIT_LEVEL_COST + "." + level);
	}

	public static boolean canCreate(Player player) {
		int shopCount = UltraTrader.getStoreHandler().getShopsByOwner(player).size();

        return getMaxShops(player) == -1 || shopCount < getMaxShops(player);
    }

	public static boolean canOwnShop(Shop shop, Player player) {
		int maxLevel = getMaxLevel(player);

        return canCreate(player) && (shop.getLevel() <= maxLevel);
    }

	public static List<String> getRequiredTraits(Player player) {
		return getRequiredTraits(player, "default");
	}

	public static List<String> getRequiredTraits(Player player, String group) {
		// Start player limit override.
		PermissionPredicate pred = new PermissionPredicate();

		List<String> traits = pred.getPermissionValues(Permissions.SHOP_LIMIT_TRAITS_REQUIRED + "." + group, player);

		return traits;
	}
}
