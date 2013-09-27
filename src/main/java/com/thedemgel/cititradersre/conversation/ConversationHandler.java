
package com.thedemgel.cititradersre.conversation;

import com.thedemgel.cititradersre.CitiTrader;
import com.thedemgel.cititradersre.conversation.additem.AddItemBeginPrompt;
import com.thedemgel.cititradersre.conversation.additem.AddItemConversationPrefix;
import com.thedemgel.cititradersre.conversation.admin.AdminBeginPrompt;
import com.thedemgel.cititradersre.conversation.admin.AdminConversationPrefix;
import com.thedemgel.cititradersre.conversation.setsellprice.SetPriceConversationPrefix;
import com.thedemgel.cititradersre.conversation.setsellprice.SetPricePrompt;
import org.bukkit.conversations.ConversationFactory;


public class ConversationHandler {
	public final static Integer CONVERSATION_TIMEOUT = 15;
	private ConversationFactory setSellPrice;
	private ConversationFactory addSellItem;
	private ConversationFactory adminConversation;
	
	public ConversationHandler(CitiTrader instance) {
		setSellPrice = new ConversationFactory(instance)
			.withModality(true)
			.withPrefix(new SetPriceConversationPrefix())
			.withFirstPrompt(new SetPricePrompt())
			.withEscapeSequence("/quit")
			.withTimeout(ConversationHandler.CONVERSATION_TIMEOUT)
			.addConversationAbandonedListener(new AbandonConvo())
			.thatExcludesNonPlayersWithMessage("No Console Please");
		
		addSellItem = new ConversationFactory(instance)
			.withModality(true)
			.withPrefix(new AddItemConversationPrefix())
			.withFirstPrompt(new AddItemBeginPrompt())
			.withEscapeSequence("/quit")
			.withTimeout(ConversationHandler.CONVERSATION_TIMEOUT)
			.addConversationAbandonedListener(new AbandonConvo())
			.thatExcludesNonPlayersWithMessage("No Console Please");
		
		adminConversation = new ConversationFactory(instance)
			.withModality(true)
			.withPrefix(new AdminConversationPrefix())
			.withFirstPrompt(new AdminBeginPrompt())
			.withEscapeSequence("/quit")
			.withTimeout(ConversationHandler.CONVERSATION_TIMEOUT)
			.addConversationAbandonedListener(new AbandonConvo())
			.thatExcludesNonPlayersWithMessage("No Console Please");
	}
	
	public ConversationFactory getSetSellPrice() {
		return setSellPrice;
	}

	public ConversationFactory getAddSellItem() {
		return addSellItem;
	}
	
	public ConversationFactory getAdminConversation() {
		return adminConversation;
	}
}
