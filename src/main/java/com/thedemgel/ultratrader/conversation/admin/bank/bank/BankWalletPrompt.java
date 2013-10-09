
package com.thedemgel.ultratrader.conversation.admin.bank.bank;

import com.thedemgel.ultratrader.conversation.ConversationHandler;
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
