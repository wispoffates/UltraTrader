package com.thedemgel.ultratrader.conversation.admin.bank;

import com.thedemgel.ultratrader.UltraTrader;
import com.thedemgel.ultratrader.Lang;
import com.thedemgel.ultratrader.conversation.FixedIgnoreCaseSetPrompt;
import com.thedemgel.ultratrader.conversation.admin.AdminConversationPrefix;
import com.thedemgel.ultratrader.wallet.Wallet;
import com.thedemgel.ultratrader.wallet.WalletHandler;
import java.util.Map.Entry;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationPrefix;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;

public class AdminBankTypeMenuPrompt extends FixedIgnoreCaseSetPrompt {

	private ConversationPrefix prefix;

	public AdminBankTypeMenuPrompt() {
		prefix = new AdminConversationPrefix();
	}

	@Override
	protected Prompt acceptValidatedInput(ConversationContext context, String input) {
		return getValidatedPrompt(new AdminBankTypeMenuPrompt());
	}

	@Override
	public String getPromptText(ConversationContext context) {
		Player p = (Player) context.getForWhom();
		WalletHandler handler = UltraTrader.getWallethandler();

		for (Entry<String, Class<? extends Wallet>> wallets : handler.getWallets().entrySet()) {
			try {
				if (p.hasPermission(handler.getPermission(wallets.getKey()))) {
					addOption(handler.getDisplayName(wallets.getKey()), new AdminBankPrompt(wallets.getKey()));
				}
			} catch (Exception ex) {
				continue;
			}
		}

		addOption(Lang.getString("conversation.admin.menu.options.exit"), new AdminBankMenuPrompt());

		p.sendRawMessage(prefix.getPrefix(context) + Lang.getString("conversation.admin.banktypemenu"));
		return Lang.getString("conversation.options") + ": " + this.formatFixedSet();
	}
}
