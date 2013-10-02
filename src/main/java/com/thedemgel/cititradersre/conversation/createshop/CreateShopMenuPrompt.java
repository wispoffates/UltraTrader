
package com.thedemgel.cititradersre.conversation.createshop;

import com.thedemgel.cititradersre.conversation.admin.*;
import com.thedemgel.cititradersre.CitiTrader;
import com.thedemgel.cititradersre.conversation.FixedIgnoreCaseSetPrompt;
import com.thedemgel.cititradersre.util.ShopInventoryView;
import java.util.ResourceBundle;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationPrefix;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;


public class CreateShopMenuPrompt extends FixedIgnoreCaseSetPrompt {
	private ResourceBundle rb;
	private ConversationPrefix prefix;
	
	public CreateShopMenuPrompt() {
		rb = CitiTrader.getResourceBundle();
		prefix = new CreateShopConversationPrefix();
		addOption(rb.getString("conversation.createshop.menu.options.create"), new AdminSetNamePrompt());
		addOption(rb.getString("conversation.createshop.menu.options.assign"), new AdminBankMenuPrompt());
		addOption(rb.getString("general.exit"), Prompt.END_OF_CONVERSATION);
	}

	@Override
	protected Prompt acceptValidatedInput(ConversationContext context, String input) {
		return getValidatedPrompt(new CreateShopMenuPrompt());
	}

	@Override
	public String getPromptText(ConversationContext context) {
		Player p = (Player) context.getForWhom();
		p.sendRawMessage(prefix.getPrefix(context) + "------<[ " + ChatColor.BLUE + "CREATE/ASSIGN" + ChatColor.YELLOW + " ]>------");
		p.sendRawMessage(prefix.getPrefix(context) + rb.getString("conversation.createshop.menu.text.begin"));
		p.sendRawMessage(prefix.getPrefix(context) + rb.getString("conversation.createshop.menu.text.create"));
		p.sendRawMessage(prefix.getPrefix(context) + rb.getString("conversation.createshop.menu.text.assign"));
		return  rb.getString("conversation.options") + ": " + this.formatFixedSet();
	}

}
