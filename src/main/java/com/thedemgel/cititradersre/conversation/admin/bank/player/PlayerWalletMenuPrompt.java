package com.thedemgel.cititradersre.conversation.admin.bank.player;

import com.thedemgel.cititradersre.conversation.admin.*;
import com.thedemgel.cititradersre.CitiTrader;
import com.thedemgel.cititradersre.conversation.ConversationHandler;
import com.thedemgel.cititradersre.conversation.FixedIgnoreCaseSetPrompt;
import java.util.ResourceBundle;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;

public class PlayerWalletMenuPrompt extends FixedIgnoreCaseSetPrompt {

	private ResourceBundle rb;

	public PlayerWalletMenuPrompt() {
		rb = CitiTrader.getResourceBundle();
	}

	@Override
	protected Prompt acceptValidatedInput(ConversationContext context, String input) {
		return getValidatedPrompt(new AdminMenuPrompt());
	}

	@Override
	public String getPromptText(ConversationContext context) {
		Prompt ret = (Prompt) context.getSessionData(ConversationHandler.CONVERSATION_SESSION_RETURN);
		addOption(rb.getString("conversation.admin.playerwallet.options.anotherplayer"), new PlayerWalletSelfPrompt());
		addOption(rb.getString("conversation.admin.playerwallet.options.yourself"), new PlayerWalletSelfPrompt());
		addOption(rb.getString("general.exit"), ret);

		return rb.getString("conversation.options") + ": " + this.formatFixedSet();
	}
}
