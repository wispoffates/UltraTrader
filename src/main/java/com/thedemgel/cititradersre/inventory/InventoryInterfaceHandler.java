
package com.thedemgel.cititradersre.inventory;

import com.thedemgel.cititradersre.inventory.annotation.InventoryPermission;
import com.thedemgel.cititradersre.inventory.annotation.InventoryTypeName;
import com.thedemgel.cititradersre.shop.Shop;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Level;
import org.bukkit.Bukkit;


public class InventoryInterfaceHandler {
	public static final String DEFAULT_INVENTORY_TYPE = "shop";

	private ConcurrentMap<String, Class<? extends InventoryInterface>> interfaces;
	private ConcurrentMap<String, String> displaynames;

	public InventoryInterfaceHandler() {
		interfaces = new ConcurrentHashMap<>();
		displaynames = new ConcurrentHashMap<>();
	}

	public InventoryInterfaceHandler registerInventoryInterface(Class<? extends InventoryInterface> interfaceclass, String displayname) {
		if (interfaceclass.isAnnotationPresent(InventoryTypeName.class)) {
			InventoryTypeName typename = interfaceclass.getAnnotation(InventoryTypeName.class);
			if (interfaces.containsKey(typename.value())) {
				// Throw exception
				return this;
			}

			interfaces.put(typename.value(), interfaceclass);
			displaynames.put(typename.value(), displayname);
		}

		return this;
	}

	public String getPermission(String interfacename) throws Exception {
		if (!interfaces.containsKey(interfacename)) {
			// Throw Exception
			throw new Exception("Interface not found");
		}

		Class<? extends InventoryInterface> interfaceclass = interfaces.get(interfacename);

		return getPermission(interfaceclass);
	}

	public String getPermission(Class<? extends InventoryInterface> interfaceclass) throws Exception {
		if (!interfaceclass.isAnnotationPresent(InventoryPermission.class)) {
			throw new Exception("InventoryInterface is not Annotated properly." + interfaceclass);
		}

		InventoryPermission perm = interfaceclass.getAnnotation(InventoryPermission.class);

		return perm.value();
	}

	public InventoryInterface getInventoryInterfaceInstance(String interfacename, Shop shop) {
		if (!interfaces.containsKey(interfacename)) {
			// Throw Exception
			// Return default wallet.
			return null;
		}

		Class<? extends InventoryInterface> interfaceclass = interfaces.get(interfacename);
		InventoryInterface inventoryInterface;
		try {
			inventoryInterface = interfaceclass.getConstructor(Shop.class).newInstance(shop);
		} catch (InvocationTargetException | IllegalAccessException | InstantiationException | NoSuchMethodException | SecurityException ex) {
			Bukkit.getLogger().log(Level.SEVERE, "Error Accessing or Instantiating InventoryInterface.");
			// Return default wallet.
			return null;
		}

		return inventoryInterface;
	}

	public ConcurrentMap<String, Class<? extends InventoryInterface>> getInterfaces() {
		return interfaces;
	}

	public String getDisplayName(String interfacename) {
		if (!displaynames.containsKey(interfacename)) {
			return interfacename;
		}

		return displaynames.get(interfacename);
	}
}
