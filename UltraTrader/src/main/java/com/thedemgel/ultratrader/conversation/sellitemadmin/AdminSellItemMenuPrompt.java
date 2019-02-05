package com.thedemgel.ultratrader.conversation.sellitemadmin;

import com.thedemgel.ultratrader.UltraTrader;
import com.thedemgel.ultratrader.Lang;
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
		addOption(Lang.getString("conversation.itemadmin.menu.options.price"), new AdminSellItemSetPricePrompt());
        addOption(Lang.getString("conversation.itemadmin.menu.options.buyprice"), new AdminBuyItemSetPricePrompt());
		addOption(Lang.getString("conversation.itemadmin.menu.options.description"), new AdminSellItemDescriptionPrompt());
        addOption("transfer", new AdminSellItemTransferCategoryPrompt());
		addOption(Lang.getString("conversation.itemadmin.menu.options.delete"), new AdminSellItemDeletePrompt());
		addOption(Lang.getString("conversation.itemadmin.menu.options.exit"), new AdminSellItemFinishPrompt());

	}

	@Override
	protected Prompt acceptValidatedInput(ConversationContext context, String input) {
		return getValidatedPrompt(new AdminSellItemMenuPrompt());
	}

	@Override
	public String getPromptText(ConversationContext context) {
		ItemPrice itemprice = (ItemPrice) context.getSessionData(ConversationHandler.CONVERSATION_SESSION_ITEMPRICE);
		Player p = (Player) context.getForWhom();
        String buyprice = (itemprice.getBuyPrice().doubleValue() < 0) ? "Disabled" : UltraTrader.getEconomy().format(itemprice.getBuyPrice().doubleValue());
        String sellprice = (itemprice.getSellPrice().doubleValue() < 0) ? "Disabled" : UltraTrader.getEconomy().format(itemprice.getSellPrice().doubleValue());

		p.sendRawMessage(prefix.getPrefix(context) + "------<" + ChatColor.BLUE + "ITEM ADMIN" + ChatColor.YELLOW + ">------");
        p.sendRawMessage(prefix.getPrefix(context) + "Category: " + ChatColor.WHITE + itemprice.getCategoryId());
		p.sendRawMessage(prefix.getPrefix(context) + Lang.getString("general.price") + "(SELL (from Shop to player): " + ChatColor.WHITE + sellprice);
        p.sendRawMessage(prefix.getPrefix(context) + Lang.getString("general.price") + "(BUY (to shop from player)): " + ChatColor.WHITE + buyprice);
        p.sendRawMessage(prefix.getPrefix(context) + Lang.getString("general.description") + ": " + ChatColor.WHITE + itemprice.getDescription());
		return Lang.getString("conversation.options") + ": " + this.formatFixedSet();
	}
}
