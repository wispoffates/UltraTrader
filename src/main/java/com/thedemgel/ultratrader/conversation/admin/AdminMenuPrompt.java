package com.thedemgel.ultratrader.conversation.admin;

import com.thedemgel.ultratrader.L;
import com.thedemgel.ultratrader.LimitHandler;
import com.thedemgel.ultratrader.UltraTrader;
import com.thedemgel.ultratrader.conversation.ConversationHandler;
import com.thedemgel.ultratrader.conversation.FixedIgnoreCaseSetPrompt;
import com.thedemgel.ultratrader.conversation.admin.bank.AdminBankMenuPrompt;
import com.thedemgel.ultratrader.conversation.admin.level.AdminSetLevelPrompt;
import com.thedemgel.ultratrader.shop.ShopInventoryView;
import com.thedemgel.ultratrader.util.Permissions;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationPrefix;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;

public class AdminMenuPrompt extends FixedIgnoreCaseSetPrompt {

	private ConversationPrefix prefix;

	public AdminMenuPrompt() {
		prefix = new AdminConversationPrefix();
	}

	@Override
	protected Prompt acceptValidatedInput(ConversationContext context, String input) {
		return getValidatedPrompt(this);
	}

	@Override
	public String getPromptText(ConversationContext context) {
		Player p = (Player) context.getForWhom();

		addOption(L.getString("conversation.admin.menu.options.storename"), new AdminSetNamePrompt());
		addOption(L.getString("conversation.admin.menu.options.bank"), new AdminBankMenuPrompt());
		addOption(L.getString("conversation.admin.menu.options.inventoryinterface"), new AdminInventoryInterfaceMenuPrompt());
		if (p.hasPermission(Permissions.LEVEL_TRANSFER)) {
			addOption(L.getString("conversation.admin.menu.options.transfer"), new AdminTransferPrompt());
		}
		addOption(L.getString("conversation.admin.menu.options.level"), new AdminSetLevelPrompt());
		addOption("remote", new AdminRemotePrompt());
		addOption(L.getString("general.exit"), new AdminFinishPrompt());

		ShopInventoryView view = (ShopInventoryView) context.getSessionData(ConversationHandler.CONVERSATION_SESSION_VIEW);

		p.sendRawMessage(prefix.getPrefix(context) + "------<[ " + ChatColor.BLUE + "ADMIN" + ChatColor.YELLOW + " ]>------");
		p.sendRawMessage(prefix.getPrefix(context) + L.getString("general.name") + ": " + ChatColor.WHITE + view.getShop().getName());
		p.sendRawMessage(prefix.getPrefix(context) + L.getString("general.bank") + ": " + ChatColor.WHITE + view.getShop().getWalletType());
		p.sendRawMessage(prefix.getPrefix(context) + L.getString("general.inventory") + ": " + ChatColor.WHITE + view.getShop().getInventoryInterfaceType());
		p.sendRawMessage(prefix.getPrefix(context) + "Level: " + ChatColor.WHITE + view.getShop().getLevel() + ChatColor.YELLOW + " Shops: " + ChatColor.WHITE + UltraTrader.getStoreHandler().getShopsByOwner(p).size() + ChatColor.YELLOW + "/" + ChatColor.WHITE + LimitHandler.getMaxShops(p) + ChatColor.YELLOW + " MaxBuySell: " + ChatColor.WHITE + LimitHandler.getMaxBuySellSize(view.getShop()));
		p.sendRawMessage(prefix.getPrefix(context) + L.getString("conversation.admin.menutext"));
		return L.getString("conversation.options") + ": " + this.formatFixedSet();
	}
}
