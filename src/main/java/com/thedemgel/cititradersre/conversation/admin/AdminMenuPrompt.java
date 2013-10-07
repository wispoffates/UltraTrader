package com.thedemgel.cititradersre.conversation.admin;

import com.thedemgel.cititradersre.L;
import com.thedemgel.cititradersre.conversation.ConversationHandler;
import com.thedemgel.cititradersre.conversation.FixedIgnoreCaseSetPrompt;
import com.thedemgel.cititradersre.shop.ShopInventoryView;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationPrefix;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;

public class AdminMenuPrompt extends FixedIgnoreCaseSetPrompt {

	private ConversationPrefix prefix;

	public AdminMenuPrompt() {
		prefix = new AdminConversationPrefix();
		addOption(L.getString("conversation.admin.menu.options.storename"), new AdminSetNamePrompt());
		addOption(L.getString("conversation.admin.menu.options.bank"), new AdminBankMenuPrompt());
		addOption(L.getString("general.exit"), new AdminFinishPrompt());
	}

	@Override
	protected Prompt acceptValidatedInput(ConversationContext context, String input) {
		return getValidatedPrompt(new AdminMenuPrompt());
	}

	@Override
	public String getPromptText(ConversationContext context) {
		ShopInventoryView view = (ShopInventoryView) context.getSessionData(ConversationHandler.CONVERSATION_SESSION_VIEW);
		Player p = (Player) context.getForWhom();
		p.sendRawMessage(prefix.getPrefix(context) + "------<[ " + ChatColor.BLUE + "ADMIN" + ChatColor.YELLOW + " ]>------");
		p.sendRawMessage(prefix.getPrefix(context) + L.getString("general.name") + ": " + ChatColor.WHITE + view.getShop().getName());
		p.sendRawMessage(prefix.getPrefix(context) + L.getString("general.bank") + ": " + ChatColor.WHITE + view.getShop().getWalletType());
		p.sendRawMessage(prefix.getPrefix(context) + L.getString("conversation.admin.menutext"));
		return L.getString("conversation.options") + ": " + this.formatFixedSet();
	}
}
