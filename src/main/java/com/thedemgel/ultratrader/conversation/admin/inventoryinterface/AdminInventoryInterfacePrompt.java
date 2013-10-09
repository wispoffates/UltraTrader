package com.thedemgel.ultratrader.conversation.admin.inventoryinterface;

import com.thedemgel.ultratrader.L;
import com.thedemgel.ultratrader.conversation.ConversationHandler;
import com.thedemgel.ultratrader.conversation.admin.AdminMenuPrompt;
import com.thedemgel.ultratrader.inventory.InventoryInterface;
import com.thedemgel.ultratrader.shop.ShopInventoryView;
import com.thedemgel.ultratrader.wallet.Wallet;
import com.thedemgel.ultratrader.wallet.annotation.AssignConversation;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.MessagePrompt;
import org.bukkit.conversations.Prompt;

public class AdminInventoryInterfacePrompt extends MessagePrompt {

	private String type;
	private InventoryInterface inventoryInterface;

	public AdminInventoryInterfacePrompt(String inventoryType) {
		type = inventoryType;
	}

	@Override
	protected Prompt getNextPrompt(ConversationContext context) {
		/*context.setSessionData(ConversationHandler.CONVERSATION_SESSION_RETURN, new AdminMenuPrompt());
		Class<?> inventoryclass = inventoryInterface.getClass();

		if (inventoryclass.isAnnotationPresent(AssignConversation.class)) {
			AssignConversation convo = walletclass.getAnnotation(AssignConversation.class);
			try {
				Prompt prompt = convo.value().newInstance();
				return prompt;
			} catch (InstantiationException | IllegalAccessException ex) {
				System.out.println("Conversation Prompt construction failed: " + convo.value());
			}
		}*/

		return new AdminMenuPrompt();
	}

	@Override
	public String getPromptText(ConversationContext context) {
		ShopInventoryView view = (ShopInventoryView) context.getSessionData(ConversationHandler.CONVERSATION_SESSION_VIEW);
		view.getShop().setInventoryInterfaceType(type);
		inventoryInterface = view.getShop().getInventoryInterface();

		return ChatColor.GREEN + L.getString("conversation.admin.setinventory") + ": " + type;
	}
}
