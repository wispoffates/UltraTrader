
package com.thedemgel.cititradersre.conversation;

import com.thedemgel.cititradersre.CitiTrader;
import com.thedemgel.cititradersre.conversation.setsellprice.SetPricePrompt;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.Prompt;


public class ConversationHandler {
	private ConversationFactory setSellPrice;
	
	public ConversationHandler(CitiTrader instance) {
		setSellPrice = new ConversationFactory(instance)
			.withModality(true)
			.withFirstPrompt(new SetPricePrompt())
			.withEscapeSequence("/quit")
			.withTimeout(60)
			.thatExcludesNonPlayersWithMessage("No Console Please");
	}
	
	public ConversationFactory getSetSellPrice() {
		return setSellPrice;
	}
}
