
package com.thedemgel.cititradersre.conversation.admin.bank.bank;

import com.thedemgel.cititradersre.conversation.ConversationHandler;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.MessagePrompt;
import org.bukkit.conversations.Prompt;


public class BankWalletPrompt extends MessagePrompt {

	@Override
	protected Prompt getNextPrompt(ConversationContext context) {
		return (Prompt) context.getSessionData(ConversationHandler.CONVERSATION_SESSION_RETURN);
	}

	@Override
	public String getPromptText(ConversationContext context) {
		return "(Assign RB)Setting bank account.";
	}

}
