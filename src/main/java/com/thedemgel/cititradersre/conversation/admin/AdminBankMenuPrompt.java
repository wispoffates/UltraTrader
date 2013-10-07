package com.thedemgel.cititradersre.conversation.admin;

import com.thedemgel.cititradersre.CitiTrader;
import com.thedemgel.cititradersre.L;
import com.thedemgel.cititradersre.conversation.FixedIgnoreCaseSetPrompt;
import com.thedemgel.cititradersre.conversation.admin.bank.AdminBankPrompt;
import com.thedemgel.cititradersre.wallet.Wallet;
import com.thedemgel.cititradersre.wallet.WalletHandler;
import java.util.Map.Entry;
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
		return getValidatedPrompt(new AdminBankMenuPrompt());
	}

	@Override
	public String getPromptText(ConversationContext context) {
		Player player = (Player) context.getForWhom();
		WalletHandler handler = CitiTrader.getWallethandler();

		for (Entry<String, Class<? extends Wallet>> wallets : handler.getWallets().entrySet()) {
			try {
				if (player.hasPermission(handler.getPermission(wallets.getKey()))) {
					addOption(handler.getDisplayName(wallets.getKey()), new AdminBankPrompt(wallets.getKey()));
				}
			} catch (Exception ex) {
				continue;
			}
		}

		addOption(L.getString("conversation.admin.menu.options.exit"), new AdminMenuPrompt());

		Player p = (Player) context.getForWhom();
		
		p.sendRawMessage(prefix.getPrefix(context) + L.getString("conversation.admin.bankmenu"));
		return L.getString("conversation.options") + ": " + this.formatFixedSet();
	}
}
