
package com.thedemgel.cititradersre.conversation;

import com.thedemgel.cititradersre.CitiTrader;
import com.thedemgel.cititradersre.conversation.additem.AddItemConversationPrefix;
import com.thedemgel.cititradersre.conversation.additem.AddItemFinishPrompt;
import com.thedemgel.cititradersre.conversation.additem.AddItemPrompt;
import com.thedemgel.cititradersre.conversation.setsellprice.SetPriceConversationPrefix;
import com.thedemgel.cititradersre.conversation.setsellprice.SetPricePrompt;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.Prompt;


public class ConversationHandler {
	private ConversationFactory setSellPrice;
	private ConversationFactory addSellItem;
	
	public ConversationHandler(CitiTrader instance) {
		setSellPrice = new ConversationFactory(instance)
			.withModality(true)
			.withPrefix(new SetPriceConversationPrefix())
			.withFirstPrompt(new SetPricePrompt())
			.withEscapeSequence("/quit")
			.withTimeout(60)
			.addConversationAbandonedListener(new AbandonConvo())
			.thatExcludesNonPlayersWithMessage("No Console Please");
		
		addSellItem = new ConversationFactory(instance)
			.withModality(true)
			.withPrefix(new AddItemConversationPrefix())
			.withFirstPrompt(new AddItemPrompt())
			.withEscapeSequence("/quit")
			.withTimeout(60)
			.addConversationAbandonedListener(new AbandonConvo())
			.thatExcludesNonPlayersWithMessage("No Console Please");
	}
	
	public ConversationFactory getSetSellPrice() {
		return setSellPrice;
	}

	public ConversationFactory getAddSellItem() {
		return addSellItem;
	}
}
