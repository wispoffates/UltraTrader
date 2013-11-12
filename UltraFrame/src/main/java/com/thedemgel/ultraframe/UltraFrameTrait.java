package com.thedemgel.ultraframe;

import com.thedemgel.ultratrader.UltraTrader;
import com.thedemgel.ultratrader.citizens.TraderTrait;
import com.thedemgel.ultratrader.citizens.UltraTrait;
import com.thedemgel.ultratrader.shop.ItemPrice;
import com.thedemgel.ultratrader.shop.Shop;
import com.thedemgel.ultratrader.shop.ShopHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import net.citizensnpcs.api.exception.NPCLoadException;
import net.citizensnpcs.api.persistence.LocationPersister;
import net.citizensnpcs.api.trait.trait.Owner;
import net.citizensnpcs.api.util.DataKey;
import net.citizensnpcs.api.util.ItemStorage;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;

public class UltraFrameTrait extends UltraTrait {

	private List<ItemFrame> frames;
	private int count = 0;

	public UltraFrameTrait() {
		super("ultraframe");
	}

	@EventHandler
	public void onHangingBreak(HangingBreakByEntityEvent event) {

		if (!(event.getEntity() instanceof ItemFrame)) {
			return;
		}

		Player player = null;
		Owner owner = npc.getTrait(Owner.class);

		if (event.getRemover() instanceof Player) {
			player = (Player) event.getRemover();
		}

		ItemFrame frame = (ItemFrame) event.getEntity();

		if (frames.contains(frame)) {
			if (player != null) {
				if (!owner.isOwnedBy(player.getName())) {
					event.setCancelled(true);
				}
			}

			if (!event.isCancelled()) {
				frames.remove(frame);
			}
		}
	}

	@Override
	public boolean hasMenuOption() {
		return false;
	}

	@Override
	public void onAttach() {
		frames = new ArrayList<>();
	}

	@Override
	public void run() {
		if ((count % 500) == 0) {
			if (npc.hasTrait(TraderTrait.class)) {
				TraderTrait trader = npc.getTrait(TraderTrait.class);

				Random rnd = new Random();

				if (!trader.getShopId().equals(ShopHandler.SHOP_NULL)) {
					Shop shop = UltraTrader.getStoreHandler().getShop(trader.getShopId());
					Object[] items = shop.getSellprices().values().toArray();

					for (ItemFrame frame : frames) {
						ItemPrice item = (ItemPrice) items[rnd.nextInt(items.length)];
						frame.setItem(item.getItemStack());
					}
				}
			}
			count = 0;
		}
		count++;
	}

	@Override
	public void load(DataKey key) throws NPCLoadException {
		try {
			frames = parseFrames(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private List<ItemFrame> parseFrames(DataKey key) throws NPCLoadException {
		List<ItemFrame> frms = new ArrayList<>();

		DataKey frameKey = key.getRelative("frames");

		LocationPersister locpersist = new LocationPersister();

		for (DataKey slotKey : frameKey.getIntegerSubKeys()) {

			Location loc = locpersist.create(slotKey.getRelative("location"));
			UUID entity = UUID.fromString(slotKey.getString("entity"));
			for (Entity ent : loc.getChunk().getEntities()) {
				if (ent.getUniqueId().equals(entity)) {
					if (ent instanceof ItemFrame) {
						ItemFrame frame = (ItemFrame) ent;
						frms.add(frame);
					}
				}
			}
		}

		return frms;
	}

	@Override
	public void save(DataKey key) {
		key.removeKey("frames");

		DataKey frameKey = key.getRelative("frames");

		LocationPersister locpersist = new LocationPersister();

		int slot = 0;
		for (ItemFrame frame : frames) {
			DataKey frameDataKey = frameKey.getRelative(String.valueOf(slot));
			locpersist.save(frame.getLocation(), frameDataKey.getRelative("location"));
			frameDataKey.setString("entity", frame.getUniqueId().toString());
			ItemStorage.saveItem(frameDataKey.getRelative("item"), frame.getItem());
			slot++;
		}
	}

	public void addFrame(ItemFrame frame) {
		frames.add(frame);
	}
}
