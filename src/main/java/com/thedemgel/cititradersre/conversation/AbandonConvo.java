package com.thedemgel.cititradersre.conversation;

import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.conversations.ConversationAbandonedListener;

public class AbandonConvo implements ConversationAbandonedListener {

	public void conversationAbandoned(ConversationAbandonedEvent abandonedEvent) {
		if (abandonedEvent.gracefulExit()) {
			abandonedEvent.getContext().getForWhom().sendRawMessage("Conversation exited gracefully.");
		} else {
			abandonedEvent.getContext().getForWhom().sendRawMessage("Conversation abandoned by" + abandonedEvent.getCanceller().getClass().getName());
		}
	}
}
