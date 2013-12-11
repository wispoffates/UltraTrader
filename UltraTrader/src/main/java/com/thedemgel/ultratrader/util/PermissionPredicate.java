
package com.thedemgel.ultratrader.util;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;


public class PermissionPredicate implements Predicate<PermissionAttachmentInfo> {

		private String test;

		Collection<PermissionAttachmentInfo> getPermissions(String basePermission, Player player) {
			test = basePermission;
			return Collections2.filter(player.getEffectivePermissions(), this);
		}

		public List<String> getPermissionValues(String basePermission, Player player) {
			Collection<PermissionAttachmentInfo> perms = getPermissions(basePermission, player);

			List<String> values = new ArrayList<>();

			for (PermissionAttachmentInfo info : perms) {
                if (info.getValue()) {
				    String value = info.getPermission().replaceFirst(basePermission + ".", "");
				    values.add(value);
                }
			}

			return values;
		}

        /**
         * Returns null of not set, true of set and true, false if set and false
         */
        public Boolean getBooleanPermission(String basePermission, Player player) {
            test = basePermission;
            Collection<PermissionAttachmentInfo> perms = Collections2.filter(player.getEffectivePermissions(), this);

            if (!perms.isEmpty()) {
                 return perms.iterator().next().getValue();
            }

            return null;
        }

        /**
         * Return highest value, unless one is -1, then returns -1, if no setting then returns null.
         * @param basePermission The base permission to search for.
         * @param player The player that is being checked
         * @return return -1, highest value, or null
         */
		public Integer getHighestPermissionSet(String basePermission, Player player) {
			test = basePermission;
			Collection<PermissionAttachmentInfo> perms = Collections2.filter(player.getEffectivePermissions(), this);

			Integer highest = null;
            boolean negCheck = false;

			for (PermissionAttachmentInfo perm : perms) {
                try {
                    if (perm.getValue()) {
                        String string = perm.getPermission().substring(perm.getPermission().lastIndexOf(".") + 1);
                        int current = Integer.valueOf(string);
                        if (highest == null || current > highest) {
                            highest = current;
                        }
                        if (current == -1) {
                            negCheck = true;
                        }
                    }
                } catch (Exception e) {
                    Bukkit.getLogger().log(Level.WARNING, "Possible NOT A NUMBER error in parsing permissions (" + perm.getPermission() + ")");
                }
            }

            if (negCheck) {
                return -1;
            } else {
			    return highest;
            }
		}

		@Override
		public boolean apply(PermissionAttachmentInfo input) {
			return input.getPermission().startsWith(test);// && input.getValue() == true;
		}
}
