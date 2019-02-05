package com.thedemgel.ultratrader.conversation.admin.bank;

import com.thedemgel.ultratrader.Lang;
import com.thedemgel.ultratrader.UltraTrader;
import com.thedemgel.ultratrader.conversation.ConversationHandler;
import com.thedemgel.ultratrader.conversation.admin.AdminMenuPrompt;
import com.thedemgel.ultratrader.inventory.ShopInventoryView;
import com.thedemgel.ultratrader.wallet.Wallet;
import com.thedemgel.ultratrader.wallet.annotation.AssignConversation;
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
				return Prompt.END_OF_CONVERSATION;
			}
		} else {
			ShopInventoryView view = (ShopInventoryView) context.getSessionData(ConversationHandler.CONVERSATION_SESSION_VIEW);
			view.getShop().setWalletType(type);
		}

		return new AdminMenuPrompt();
	}

	@Override
	public String getPromptText(ConversationContext context) {
		ShopInventoryView view = (ShopInventoryView) context.getSessionData(ConversationHandler.CONVERSATION_SESSION_VIEW);
		wallet = UltraTrader.getWallethandler().getWalletInstance(type, view.getShop());
		context.setSessionData("wallettype", type);

		return ChatColor.GREEN + Lang.getString("conversation.admin.setbank") + ": " + type;
	}
}
