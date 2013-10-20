
package com.thedemgel.ultratrader.conversation.admin.level;

import com.thedemgel.ultratrader.L;
import com.thedemgel.ultratrader.conversation.FixedIgnoreCaseSetPrompt;
import com.thedemgel.ultratrader.conversation.admin.AdminFinishPrompt;
import com.thedemgel.ultratrader.conversation.admin.AdminInventoryInterfaceMenuPrompt;
import com.thedemgel.ultratrader.conversation.admin.AdminMenuPrompt;
import com.thedemgel.ultratrader.conversation.admin.AdminSetNamePrompt;
import com.thedemgel.ultratrader.conversation.admin.AdminTransferPrompt;
import com.thedemgel.ultratrader.conversation.admin.bank.AdminBankMenuPrompt;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;


public class AdminSetLevelPrompt extends FixedIgnoreCaseSetPrompt {

	@Override
	protected Prompt acceptValidatedInput(ConversationContext context, String input) {
		return getValidatedPrompt(this);
	}

	@Override
	public String getPromptText(ConversationContext context) {
		addOption(L.getString("conversation.admin.level.options.increase"), this);
		addOption(L.getString("conversation.admin.level.options.decrease"), this);
		addOption(L.getString("conversation.admin.level.options.set"), this);
		addOption(L.getString("general.exit"), new AdminMenuPrompt());
		return L.getString("conversation.options") + ": " + this.formatFixedSet();
	}

}
