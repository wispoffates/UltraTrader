package com.thedemgel.ultratrader.conversation.createshop;

import com.thedemgel.ultratrader.Lang;
import com.thedemgel.ultratrader.conversation.FixedIgnoreCaseSetPrompt;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationPrefix;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;

public class CreateShopMenuPrompt extends FixedIgnoreCaseSetPrompt {

	private ConversationPrefix prefix;

	public CreateShopMenuPrompt() {
		prefix = new CreateShopConversationPrefix();
		addOption(Lang.getString("conversation.createshop.menu.options.create"), new CreateShopCreatePrompt());
		addOption(Lang.getString("conversation.createshop.menu.options.assign"), new CreateShopAssignPrompt());
		addOption(Lang.getString("general.exit"), Prompt.END_OF_CONVERSATION);
	}

	@Override
	protected Prompt acceptValidatedInput(ConversationContext context, String input) {
		return getValidatedPrompt(new CreateShopMenuPrompt());
	}

	@Override
	public String getPromptText(ConversationContext context) {
		Player p = (Player) context.getForWhom();
		p.sendRawMessage(prefix.getPrefix(context) + "------<" + ChatColor.BLUE + "CREATE/ASSIGN" + ChatColor.YELLOW + ">------");
		p.sendRawMessage(prefix.getPrefix(context) + Lang.getString("conversation.createshop.menu.text.begin"));
		p.sendRawMessage(prefix.getPrefix(context) + Lang.getString("conversation.createshop.menu.text.create"));
		p.sendRawMessage(prefix.getPrefix(context) + Lang.getString("conversation.createshop.menu.text.assign"));
		return Lang.getString("conversation.options") + ": " + this.formatFixedSet();
	}
}
