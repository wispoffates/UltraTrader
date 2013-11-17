package com.thedemgel.ultratrader.conversation.admin.bank.player;

import com.thedemgel.ultratrader.conversation.admin.AdminMenuPrompt;
import com.thedemgel.ultratrader.UltraTrader;
import com.thedemgel.ultratrader.L;
import com.thedemgel.ultratrader.conversation.ConversationHandler;
import com.thedemgel.ultratrader.conversation.FixedIgnoreCaseSetPrompt;
import java.util.ResourceBundle;
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
		addOption(L.getString("conversation.admin.playerwallet.options.anotherplayer"), new PlayerWalletAnotherPrompt());
		addOption(L.getString("conversation.admin.playerwallet.options.yourself"), new PlayerWalletSelfPrompt());
		addOption(L.getString("general.exit"), ret);

		return L.getString("conversation.options") + ": " + this.formatFixedSet();
	}
}
