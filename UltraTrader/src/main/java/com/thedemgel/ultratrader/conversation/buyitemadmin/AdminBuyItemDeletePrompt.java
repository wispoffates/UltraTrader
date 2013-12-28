package com.thedemgel.ultratrader.conversation.buyitemadmin;

import com.thedemgel.ultratrader.L;
import com.thedemgel.ultratrader.conversation.ConversationHandler;
import com.thedemgel.ultratrader.conversation.FixedIgnoreCaseSetPrompt;
import com.thedemgel.ultratrader.shop.ItemPrice;
import com.thedemgel.ultratrader.inventory.ShopInventoryView;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationPrefix;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;

public class AdminBuyItemDeletePrompt extends FixedIgnoreCaseSetPrompt {

	private ConversationPrefix prefix;

	public AdminBuyItemDeletePrompt() {
		prefix = new AdminBuyItemConversationPrefix();
		addOption(L.getString("general.accept"), new AdminBuyItemDeleteConfirmPrompt(true));
		addOption(L.getString("general.decline"), new AdminBuyItemDeleteConfirmPrompt(false));
	}

	@Override
	protected Prompt acceptValidatedInput(ConversationContext context, String input) {
		return getValidatedPrompt(new AdminBuyItemMenuPrompt());
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
			p.sendRawMessage(prefix.getPrefix(context) + ChatColor.RED + L.getString("conversation.itemadmin.delete.full"));
			p.sendRawMessage(prefix.getPrefix(context) + ChatColor.RED + L.getString("conversation.itemadmin.delete.warn"));
		}

		p.sendRawMessage(prefix.getPrefix(context) + L.getString("conversation.itemadmin.delete.confirm") + ": " + ChatColor.WHITE + itemprice.getItemStack().getType().name());
		return L.getString("conversation.options") + ": " + this.formatFixedSet();
	}
}
