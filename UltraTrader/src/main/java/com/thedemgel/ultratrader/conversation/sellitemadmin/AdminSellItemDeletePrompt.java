package com.thedemgel.ultratrader.conversation.sellitemadmin;

import com.thedemgel.ultratrader.Lang;
import com.thedemgel.ultratrader.conversation.ConversationHandler;
import com.thedemgel.ultratrader.conversation.FixedIgnoreCaseSetPrompt;
import com.thedemgel.ultratrader.shop.ItemPrice;
import com.thedemgel.ultratrader.inventory.ShopInventoryView;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationPrefix;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;

public class AdminSellItemDeletePrompt extends FixedIgnoreCaseSetPrompt {

	private ConversationPrefix prefix;

	public AdminSellItemDeletePrompt() {
		prefix = new AdminSellItemConversationPrefix();
		addOption(Lang.getString("general.accept"), new AdminSellItemDeleteConfirmPrompt(true));
		addOption(Lang.getString("general.decline"), new AdminSellItemDeleteConfirmPrompt(false));
	}

	@Override
	protected Prompt acceptValidatedInput(ConversationContext context, String input) {
		return getValidatedPrompt(new AdminSellItemMenuPrompt());
	}

	@Override
	public String getPromptText(ConversationContext context) {
		ItemPrice itemprice = (ItemPrice) context.getSessionData(ConversationHandler.CONVERSATION_SESSION_ITEMPRICE);
		Player p = (Player) context.getForWhom();
		ShopInventoryView view = (ShopInventoryView) context.getSessionData(ConversationHandler.CONVERSATION_SESSION_VIEW);
		int invCount = 0;
		if (view.getShop().getInventoryInterface().containsItem(itemprice)) {
			invCount = view.getShop().getInventoryInterface().getInventoryStock(itemprice.getItemStack());
		}

		if (invCount > 0 && !view.getShop().hasItem(itemprice)) {
			p.sendRawMessage(prefix.getPrefix(context) + ChatColor.RED + Lang.getString("conversation.itemadmin.delete.full"));
			p.sendRawMessage(prefix.getPrefix(context) + ChatColor.RED + Lang.getString("conversation.itemadmin.delete.warn"));
		}

		p.sendRawMessage(prefix.getPrefix(context) + Lang.getString("conversation.itemadmin.delete.confirm") + ": " + ChatColor.WHITE + itemprice.getItemStack().getType().name());
		return Lang.getString("conversation.options") + ": " + this.formatFixedSet();
	}
}
