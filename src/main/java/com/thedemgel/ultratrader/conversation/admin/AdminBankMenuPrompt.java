package com.thedemgel.ultratrader.conversation.admin;

import com.thedemgel.ultratrader.UltraTrader;
import com.thedemgel.ultratrader.L;
import com.thedemgel.ultratrader.conversation.FixedIgnoreCaseSetPrompt;
import com.thedemgel.ultratrader.conversation.admin.bank.AdminBankPrompt;
import com.thedemgel.ultratrader.wallet.Wallet;
import com.thedemgel.ultratrader.wallet.WalletHandler;
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
		WalletHandler handler = UltraTrader.getWallethandler();

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
