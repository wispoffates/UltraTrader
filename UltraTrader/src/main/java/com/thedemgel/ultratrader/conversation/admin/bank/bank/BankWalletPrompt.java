
package com.thedemgel.ultratrader.conversation.admin.bank.bank;

import com.thedemgel.ultratrader.Lang;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.MessagePrompt;
import org.bukkit.conversations.Prompt;


public class BankWalletPrompt extends MessagePrompt {

	@Override
	protected Prompt getNextPrompt(ConversationContext context) {
		return new BankSetBankPrompt();
	}

	@Override
	public String getPromptText(ConversationContext context) {
		return Lang.getString("conversation.admin.wallet.bank.settingbank");
	}

}
