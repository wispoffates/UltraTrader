
package com.thedemgel.ultratrader.conversation.admin.bank;

import java.math.BigDecimal;

import com.thedemgel.ultratrader.L;
import com.thedemgel.ultratrader.UltraTrader;
import com.thedemgel.ultratrader.conversation.ConversationHandler;
import com.thedemgel.ultratrader.conversation.FixedIgnoreCaseSetPrompt;
import com.thedemgel.ultratrader.conversation.admin.AdminConversationPrefix;
import com.thedemgel.ultratrader.conversation.admin.AdminMenuPrompt;
import com.thedemgel.ultratrader.inventory.ShopInventoryView;
import com.thedemgel.ultratrader.wallet.Wallet;

import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationPrefix;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;


public class AdminBankMenuPrompt extends FixedIgnoreCaseSetPrompt {

	private ConversationPrefix prefix;

	public AdminBankMenuPrompt() {
		prefix = new AdminConversationPrefix();
	}

	@Override
	protected Prompt acceptValidatedInput(ConversationContext context, String input) {
		return getValidatedPrompt(this);
	}

	@Override
	public String getPromptText(ConversationContext context) {
		addOption(L.getString("conversation.admin.bankmenu.settype"), new AdminBankTypeMenuPrompt());
		addOption(L.getString("conversation.admin.bankmenu.withdraw"), new AdminBankWithDrawPrompt());
		addOption(L.getString("conversation.admin.bankmenu.deposit"), new AdminBankDepositPrompt());
		addOption(L.getString("conversation.admin.menu.options.exit"), new AdminMenuPrompt());

		Player p = (Player) context.getForWhom();
		ShopInventoryView view = (ShopInventoryView) context.getSessionData(ConversationHandler.CONVERSATION_SESSION_VIEW);
		Wallet wallet = view.getShop().getWallet();
		BigDecimal balance = wallet.getBalance();
		p.sendRawMessage(prefix.getPrefix(context) + L.getFormatString("conversation.admin.bankmenu.balance", ChatColor.WHITE + UltraTrader.getEconomy().format(balance.doubleValue())));
		p.sendRawMessage(prefix.getPrefix(context) + L.getFormatString("conversation.admin.bankmenu.type", ChatColor.WHITE + view.getShop().getWalletType()));
		return L.getString("conversation.options") + ": " + this.formatFixedSet();
	}

}
