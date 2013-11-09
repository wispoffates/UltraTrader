package com.thedemgel.ultratrader.conversation.admin.bank.player;

import com.thedemgel.ultratrader.conversation.ConversationHandler;
import com.thedemgel.ultratrader.shop.ShopInventoryView;
import com.thedemgel.ultratrader.util.ConfigValue;
import com.thedemgel.ultratrader.util.Permissions;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.MessagePrompt;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;

public class PlayerWalletSelfPrompt extends MessagePrompt {

	private Player p;

	@Override
	protected Prompt getNextPrompt(ConversationContext context) {
		return (Prompt) context.getSessionData(ConversationHandler.CONVERSATION_SESSION_RETURN);
	}

	@Override
	public String getPromptText(ConversationContext context) {
		p = (Player) context.getForWhom();
		ShopInventoryView view = (ShopInventoryView) context.getSessionData(ConversationHandler.CONVERSATION_SESSION_VIEW);
		view.getShop().getWallet().setInfo("player", new ConfigValue(p.getName()));
		String type = (String) context.getSessionData("wallettype");
		view.getShop().setWalletType(type);
		return "Setting wallet to yourself";
	}
}
