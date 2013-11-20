package com.thedemgel.ultratrader.conversation.admin.bank.player;

import com.thedemgel.ultratrader.conversation.ConversationHandler;
import com.thedemgel.ultratrader.inventory.ShopInventoryView;
import com.thedemgel.ultratrader.util.ConfigValue;
import com.thedemgel.ultratrader.util.Permissions;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.MessagePrompt;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;

public class PlayerWalletPrompt extends MessagePrompt {
	private Player p;

	@Override
	protected Prompt getNextPrompt(ConversationContext context) {
		if (p.hasPermission(Permissions.WALLET_PLAYER_SETOTHER)) {
			return new PlayerWalletMenuPrompt();
		} else {
			return new PlayerWalletSelfPrompt();
		}
	}

	@Override
	public String getPromptText(ConversationContext context) {
		p = (Player) context.getForWhom();
		if (p.hasPermission(Permissions.WALLET_PLAYER_SETOTHER)) {
			return "Set wallet to yourself or another?";
		} else {
			return "You only have permission to set to Yourself.";
		}
		
	}
}
