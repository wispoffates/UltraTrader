package com.thedemgel.cititradersre.conversation.createshop;

import com.thedemgel.cititradersre.L;
import com.thedemgel.cititradersre.conversation.FixedIgnoreCaseSetPrompt;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationPrefix;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;

public class CreateShopMenuPrompt extends FixedIgnoreCaseSetPrompt {

	private ConversationPrefix prefix;

	public CreateShopMenuPrompt() {
		prefix = new CreateShopConversationPrefix();
		addOption(L.getString("conversation.createshop.menu.options.create"), new CreateShopCreatePrompt());
		addOption(L.getString("conversation.createshop.menu.options.assign"), new CreateShopAssignPrompt());
		addOption(L.getString("general.exit"), Prompt.END_OF_CONVERSATION);
	}

	@Override
	protected Prompt acceptValidatedInput(ConversationContext context, String input) {
		return getValidatedPrompt(new CreateShopMenuPrompt());
	}

	@Override
	public String getPromptText(ConversationContext context) {
		Player p = (Player) context.getForWhom();
		p.sendRawMessage(prefix.getPrefix(context) + "------<[ " + ChatColor.BLUE + "CREATE/ASSIGN" + ChatColor.YELLOW + " ]>------");
		p.sendRawMessage(prefix.getPrefix(context) + L.getString("conversation.createshop.menu.text.begin"));
		p.sendRawMessage(prefix.getPrefix(context) + L.getString("conversation.createshop.menu.text.create"));
		p.sendRawMessage(prefix.getPrefix(context) + L.getString("conversation.createshop.menu.text.assign"));
		return L.getString("conversation.options") + ": " + this.formatFixedSet();
	}
}
