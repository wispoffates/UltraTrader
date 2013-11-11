package com.thedemgel.ultratrader.conversation.sellitemadmin;

import com.thedemgel.ultratrader.UltraTrader;
import com.thedemgel.ultratrader.L;
import com.thedemgel.ultratrader.conversation.ConversationHandler;
import com.thedemgel.ultratrader.conversation.FixedIgnoreCaseSetPrompt;
import com.thedemgel.ultratrader.shop.ItemPrice;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationPrefix;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;

public class AdminSellItemMenuPrompt extends FixedIgnoreCaseSetPrompt {

	private ConversationPrefix prefix;

	public AdminSellItemMenuPrompt() {
		prefix = new AdminSellItemConversationPrefix();
		addOption(L.getString("conversation.itemadmin.menu.options.price"), new AdminSellItemSetPricePrompt());
		addOption(L.getString("conversation.itemadmin.menu.options.description"), new AdminSellItemDescriptionPrompt());
		addOption(L.getString("conversation.itemadmin.menu.options.delete"), new AdminSellItemDeletePrompt());
		addOption(L.getString("conversation.itemadmin.menu.options.exit"), new AdminSellItemFinishPrompt());
	}

	@Override
	protected Prompt acceptValidatedInput(ConversationContext context, String input) {
		return getValidatedPrompt(new AdminSellItemMenuPrompt());
	}

	@Override
	public String getPromptText(ConversationContext context) {
		ItemPrice itemprice = (ItemPrice) context.getSessionData(ConversationHandler.CONVERSATION_SESSION_ITEMPRICE);
		Player p = (Player) context.getForWhom();
		p.sendRawMessage(prefix.getPrefix(context) + "------<" + ChatColor.BLUE + "ITEM ADMIN" + ChatColor.YELLOW + ">------");
		p.sendRawMessage(prefix.getPrefix(context) + L.getString("general.price") + ": " + ChatColor.WHITE + UltraTrader.getEconomy().format(itemprice.getPrice().doubleValue()));
		p.sendRawMessage(prefix.getPrefix(context) + L.getString("general.description") + ": " + ChatColor.WHITE + itemprice.getDescription());
		return L.getString("conversation.options") + ": " + this.formatFixedSet();
	}
}
