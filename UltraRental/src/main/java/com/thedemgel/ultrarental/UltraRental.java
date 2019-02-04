
package com.thedemgel.ultrarental;

import com.thedemgel.ultrarental.conversation.rentalshop.RentalBeginPrompt;
import com.thedemgel.ultrarental.conversation.rentalshop.RentalConversationPrefix;
import com.thedemgel.ultratrader.UltraTrader;
import com.thedemgel.ultratrader.conversation.AbandonConvo;
import com.thedemgel.ultratrader.conversation.ConversationHandler;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.plugin.java.JavaPlugin;


public class UltraRental extends JavaPlugin {
	private static ConversationFactory rentalTrader;
	private static RentalHandler rentalHandler;
	private static UltraRental instance;

	public static RentalHandler getRentalHandler() {
		return rentalHandler;
	}

	public static UltraRental getInstance() {
		return instance;
	}

	@Override
	public void onEnable() {
		instance = this;

		rentalHandler = new RentalHandler();

		rentalTrader = new ConversationFactory(this)
			.withModality(true)
			.withPrefix(new RentalConversationPrefix())
			.withFirstPrompt(new RentalBeginPrompt())
			.withEscapeSequence("/quit")
			.withTimeout(ConversationHandler.CONVERSATION_TIMEOUT)
			.addConversationAbandonedListener(new AbandonConvo())
			.thatExcludesNonPlayersWithMessage("No Console Please");

		UltraTrader.getTraitHandler().registerTrait(RentalShop.class, "rentalshop");
	}

	public static ConversationFactory getRentalTraderConvo() {
		return rentalTrader;
	}
}
