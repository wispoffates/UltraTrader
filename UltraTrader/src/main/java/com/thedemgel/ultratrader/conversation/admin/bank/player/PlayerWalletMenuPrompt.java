package com.thedemgel.ultratrader.conversation.admin.bank.player;

import com.thedemgel.ultratrader.Lang;
import com.thedemgel.ultratrader.conversation.ConversationHandler;
import com.thedemgel.ultratrader.conversation.FixedIgnoreCaseSetPrompt;
import com.thedemgel.ultratrader.conversation.admin.AdminMenuPrompt;

import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;

public class PlayerWalletMenuPrompt extends FixedIgnoreCaseSetPrompt {

	@Override
	protected Prompt acceptValidatedInput(ConversationContext context, String input) {
		return getValidatedPrompt(new AdminMenuPrompt());
	}

	@Override
	public String getPromptText(ConversationContext context) {
		Prompt ret = (Prompt) context.getSessionData(ConversationHandler.CONVERSATION_SESSION_RETURN);
		addOption(Lang.getString("conversation.admin.playerwallet.options.anotherplayer"), new PlayerWalletAnotherPrompt());
		addOption(Lang.getString("conversation.admin.playerwallet.options.yourself"), new PlayerWalletSelfPrompt());
		addOption(Lang.getString("general.exit"), ret);

		return Lang.getString("conversation.options") + ": " + this.formatFixedSet();
	}
}
