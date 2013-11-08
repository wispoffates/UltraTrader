package com.thedemgel.ultratrader.conversation.buyitemadmin;

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

public class AdminBuyItemMenuPrompt extends FixedIgnoreCaseSetPrompt {

	private ConversationPrefix prefix;

	public AdminBuyItemMenuPrompt() {
		prefix = new AdminBuyItemConversationPrefix();
		addOption(L.getString("conversation.itemadmin.menu.options.price"), new AdminBuyItemSetPricePrompt());
		addOption(L.getString("conversation.itemadmin.menu.options.description"), new AdminBuyItemDescriptionPrompt());
		addOption(L.getString("conversation.itemadmin.menu.options.delete"), new AdminBuyItemDeletePrompt());
		addOption(L.getString("conversation.itemadmin.menu.options.exit"), new AdminBuyItemFinishPrompt());
	}

	@Override
	protected Prompt acceptValidatedInput(ConversationContext context, String input) {
		return getValidatedPrompt(new AdminBuyItemMenuPrompt());
	}

	@Override
	public String getPromptText(ConversationContext context) {
		ItemPrice itemprice = (ItemPrice) context.getSessionData(ConversationHandler.CONVERSATION_SESSION_ITEMPRICE);
		Player p = (Player) context.getForWhom();
		p.sendRawMessage(prefix.getPrefix(context) + "------<[ " + ChatColor.BLUE + "ITEM ADMIN" + ChatColor.YELLOW + " ]>------");
		p.sendRawMessage(prefix.getPrefix(context) + L.getString("general.price") + ": " + ChatColor.WHITE + UltraTrader.getEconomy().format(itemprice.getPrice().doubleValue()));
		p.sendRawMessage(prefix.getPrefix(context) + L.getString("general.description") + ": " + ChatColor.WHITE + itemprice.getDescription());
		return L.getString("conversation.options") + ": " + this.formatFixedSet();
	}
}
