
package com.thedemgel.ultratrader.conversation.admin;

import com.thedemgel.ultratrader.L;
import com.thedemgel.ultratrader.LimitHandler;
import com.thedemgel.ultratrader.UltraTrader;
import com.thedemgel.ultratrader.conversation.ConversationHandler;
import com.thedemgel.ultratrader.conversation.FixedIgnoreCaseSetPrompt;
import com.thedemgel.ultratrader.conversation.admin.remote.AdminRemoteTogglePrompt;
import com.thedemgel.ultratrader.shop.ShopInventoryView;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationPrefix;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;


public class AdminRemotePrompt extends FixedIgnoreCaseSetPrompt {
	private ConversationPrefix prefix;

	public AdminRemotePrompt() {
		prefix = new AdminConversationPrefix();
	}

	@Override
	public String getPromptText(ConversationContext context) {
		ShopInventoryView view = (ShopInventoryView) context.getSessionData(ConversationHandler.CONVERSATION_SESSION_VIEW);
		Player p = (Player) context.getForWhom();

		p.sendRawMessage(prefix.getPrefix(context) + "------<[ " + ChatColor.BLUE + "REMOTE ACCESS" + ChatColor.YELLOW + " ]>------");

		// Give remote access information here
		if (view.getShop().getCanRemote()) {
			p.sendRawMessage(prefix.getPrefix(context) + "Access: Enabled");
			p.sendRawMessage(prefix.getPrefix(context) + "Item Cost: " + UltraTrader.getEconomy().format(view.getShop().getRemoteItemCost()));
			addOption("disable", new AdminRemoteTogglePrompt(true));
		} else {
			p.sendRawMessage(prefix.getPrefix(context) + "Access: Disabled");
			p.sendRawMessage(prefix.getPrefix(context) + "Activation Cost: " + UltraTrader.getEconomy().format(LimitHandler.getRemoteActivateCost(p)));
			addOption("enable", new AdminRemoteTogglePrompt(false));
		}

		addOption(L.getString("general.exit"), new AdminMenuPrompt());

		return L.getString("conversation.options") + ": " + this.formatFixedSet();
	}

	@Override
	protected Prompt acceptValidatedInput(ConversationContext context, String string) {
		return getValidatedPrompt(this);
	}

}
