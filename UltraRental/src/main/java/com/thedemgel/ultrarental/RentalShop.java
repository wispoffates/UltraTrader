package com.thedemgel.ultrarental;

import com.thedemgel.ultrarental.conversation.rentalshop.RentalEndRentingPrompt;
import com.thedemgel.ultratrader.UltraTrader;
import com.thedemgel.ultratrader.citizens.TraderTrait;
import com.thedemgel.ultratrader.citizens.UltraTrait;
import com.thedemgel.ultratrader.conversation.ConversationHandler;
import com.thedemgel.ultratrader.shop.ShopHandler;
import com.thedemgel.ultratrader.util.TimeFormat;
import java.util.concurrent.TimeUnit;
import net.citizensnpcs.api.persistence.Persist;
import net.citizensnpcs.api.trait.trait.Owner;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;

public class RentalShop extends UltraTrait {

	// Player renting shop
	@Persist
	private String renter = "";

	// Time shop was rented
	// 0 if not rented
	@Persist
	private long rentedOn = 0;
	// Settings
	// Time in milliseconds that the trader can be rented
	@Persist
	private int term;
	@Persist
	private TimeUnit unit = TimeUnit.MINUTES;
	// Cost per term for the rented shop
	@Persist
	private double cost = 0D;
	// Enable or Disable the shop (so admins can shut them off)
	@Persist
	private boolean isRentingEnabled = false;

	public RentalShop() {
		super("rentalshop");
		setMenuOption("rental");
	}

	public String getRenter() {
		return renter;
	}

	public boolean isRented() {
		if ("".equals(renter)) {
			return false;
		}

		return true;
	}

	@Override
	public void onSpawn() {
		RentalHandler.registerRentalNPC(npc);
		if (!npc.hasTrait(TraderTrait.class)) {
			npc.addTrait(TraderTrait.class);
		}
	}

	@Override
	public void onDespawn() {
		RentalHandler.unregisterRentalNPC(npc);
	}

	@Override
	public void onAttach() {
		if (!npc.hasTrait(TraderTrait.class)) {
			npc.addTrait(TraderTrait.class);
		}
	}

	public void setRenter(String value) {
		renter = value;
	}

	public void setRenter(Player player) {
		renter = player.getName();
	}

	public long getRentedOn() {
		return rentedOn;
	}

	public void setRentedOn(long time) {
		rentedOn = time;
	}

	public int getTerm() {
		return term;
	}

	public long getTermInMilliseconds() {
		return term == 0 ? 0 : unit.toMillis(term);
	}

	public TimeUnit getTermType() {
		return unit;
	}

	public void setTermType(TimeUnit unit) {
		this.unit = unit;
	}

	public String getFormatedTerm() {
		return TimeFormat.getDurationBreakdown(getTermInMilliseconds());
	}

	public void setTerm(int term) {
		this.term = term;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	public boolean isRentingEnabled() {
		return isRentingEnabled;
	}

	public void setRentingEnabled(boolean enabled) {
		this.isRentingEnabled = enabled;
	}

	@Override
	public boolean onClick(Player player) {
		Owner owner = npc.getTrait(Owner.class);

		if (owner.isOwnedBy(player) && player.getItemInHand().getType().equals(Material.STICK)) {
			if (!player.isConversing()) {
				// Open up rental conversation
				Conversation convo = UltraRental.getRentalTraderConvo().buildConversation(player);
				convo.getContext().setSessionData(ConversationHandler.CONVERSATION_SESSION_NPC, npc);
				convo.begin();
			} else {
				player.sendRawMessage(ChatColor.RED + L.getString("conversation.error.inconvo"));
			}
			return false;
		}

		if (!isRented()) {
			if (!player.isConversing()) {
				Conversation convo = UltraRental.getRentalTraderConvo().buildConversation(player);
				convo.getContext().setSessionData(ConversationHandler.CONVERSATION_SESSION_NPC, npc);
				convo.begin();
			} else {
				player.sendRawMessage(ChatColor.RED + L.getString("conversation.error.inconvo"));
			}
			return false;
		}

		/*TraderTrait trader = npc.getTrait(TraderTrait.class);

		if (trader.getShopId().equals(ShopHandler.SHOP_NULL)) {
			if (getRenter().equals(player.getName())) {
				Conversation convo = UltraTrader.getConversationHandler().getCreateShop().buildConversation(player);
				convo.getContext().setSessionData(ConversationHandler.CONVERSATION_SESSION_NPC, npc);
				convo.begin();
				return false;
			} else {
				// send message shop isn't open yet.
			}
		}*/

		return true;
	}

	@Override
	public boolean hasMenuOption() {
		return true;
	}

	@Override
	public Prompt getMenuPrompt() {
		return new RentalEndRentingPrompt();
	}
}
