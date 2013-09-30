
package com.thedemgel.cititradersre.conversation.itemadmin;

import com.thedemgel.cititradersre.CitiTrader;
import com.thedemgel.cititradersre.conversation.FixedIgnoreCaseSetPrompt;
import com.thedemgel.cititradersre.shop.ItemPrice;
import java.util.ResourceBundle;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationPrefix;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;


public class AdminItemMenuPrompt extends FixedIgnoreCaseSetPrompt {
	private ResourceBundle rb;
	private ConversationPrefix prefix;
	
	public AdminItemMenuPrompt() {
		rb = CitiTrader.getResourceBundle();
		prefix = new AdminItemConversationPrefix();
		addOption(rb.getString("conversation.itemadmin.menu.options.price"), new AdminItemSetPricePrompt());
		addOption(rb.getString("conversation.itemadmin.menu.options.description"), new AdminItemDescriptionPrompt());
		addOption(rb.getString("conversation.itemadmin.menu.options.delete"), new AdminItemDeletePrompt());
		addOption(rb.getString("conversation.itemadmin.menu.options.exit"), new AdminItemFinishPrompt());
	}

	@Override
	protected Prompt acceptValidatedInput(ConversationContext context, String input) {
		return getValidatedPrompt(new AdminItemMenuPrompt());
	}

	@Override
	public String getPromptText(ConversationContext context) {
		ItemPrice itemprice = (ItemPrice) context.getSessionData("itemprice");
		Player p = (Player) context.getForWhom();
		p.sendRawMessage(prefix.getPrefix(context) + "------<[ " + ChatColor.BLUE + "ITEM ADMIN" + ChatColor.YELLOW + " ]>------");
		p.sendRawMessage(prefix.getPrefix(context) + rb.getString("general.price") +": " + ChatColor.WHITE + CitiTrader.getEconomy().format(itemprice.getPrice().doubleValue()));
		p.sendRawMessage(prefix.getPrefix(context) + rb.getString("general.description") + ": " + ChatColor.WHITE + itemprice.getDescription());
		return  rb.getString("conversation.options") + ": " + this.formatFixedSet();
	}
}
