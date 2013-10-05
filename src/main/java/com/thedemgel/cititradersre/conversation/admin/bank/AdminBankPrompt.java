
package com.thedemgel.cititradersre.conversation.admin.bank;

import com.thedemgel.cititradersre.CitiTrader;
import com.thedemgel.cititradersre.conversation.ConversationHandler;
import com.thedemgel.cititradersre.conversation.admin.AdminMenuPrompt;
import com.thedemgel.cititradersre.shop.ShopInventoryView;
import com.thedemgel.cititradersre.wallet.Wallet;
import com.thedemgel.cititradersre.wallet.annotation.AssignConversation;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.MessagePrompt;
import org.bukkit.conversations.Prompt;


public class AdminBankPrompt extends MessagePrompt {

	private String type;
	private Wallet wallet;
	
	public AdminBankPrompt(String walletType) {
		type = walletType;
	}
	
	@Override
	protected Prompt getNextPrompt(ConversationContext context) {
		context.setSessionData(ConversationHandler.CONVERSATION_SESSION_RETURN, new AdminMenuPrompt());
		Class<?> walletclass = wallet.getClass();
		if (walletclass.isAnnotationPresent(AssignConversation.class)) {
			AssignConversation convo = walletclass.getAnnotation(AssignConversation.class);
			try {
				Prompt prompt = convo.value().newInstance();
				return prompt;
			} catch (InstantiationException | IllegalAccessException ex) {
				System.out.println("Conversation Prompt construction failed: " + convo.value());
			}
		}
		
		return new AdminMenuPrompt();
	}

	@Override
	public String getPromptText(ConversationContext context) {
		//Player player = (Player) context.getForWhom();
		ShopInventoryView view = (ShopInventoryView) context.getSessionData(ConversationHandler.CONVERSATION_SESSION_VIEW);
		view.getShop().setWalletType(type);
		wallet = view.getShop().getWallet();
		/*if (wallet instanceof PlayerWallet) {
			wallet.setInfo("player", new ConfigValue(player.getName()));
		}*/
		return ChatColor.GREEN + CitiTrader.getResourceBundle().getString("conversation.admin.setbank") + ": " + type;
	}

}
