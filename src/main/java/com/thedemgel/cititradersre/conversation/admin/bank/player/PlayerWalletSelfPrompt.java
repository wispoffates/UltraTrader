package com.thedemgel.cititradersre.conversation.admin.bank.player;

import com.thedemgel.cititradersre.conversation.ConversationHandler;
import com.thedemgel.cititradersre.shop.ShopInventoryView;
import com.thedemgel.cititradersre.util.ConfigValue;
import com.thedemgel.cititradersre.util.Permissions;
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
		return "Setting wallet to yourself";
	}
}
