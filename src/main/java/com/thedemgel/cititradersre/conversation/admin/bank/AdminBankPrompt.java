
package com.thedemgel.cititradersre.conversation.admin.bank;

import com.thedemgel.cititradersre.CitiTrader;
import com.thedemgel.cititradersre.conversation.ConversationHandler;
import com.thedemgel.cititradersre.conversation.admin.AdminMenuPrompt;
import com.thedemgel.cititradersre.shop.ShopInventoryView;
import com.thedemgel.cititradersre.wallet.WalletType;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.MessagePrompt;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;


public class AdminBankPrompt extends MessagePrompt {

	private WalletType type;
	
	public AdminBankPrompt(WalletType walletType) {
		type = walletType;
	}
	
	@Override
	protected Prompt getNextPrompt(ConversationContext context) {
		return new AdminMenuPrompt();
	}

	@Override
	public String getPromptText(ConversationContext context) {
		ShopInventoryView view = (ShopInventoryView) context.getSessionData(ConversationHandler.CONVERSATION_SESSION_VIEW);
		view.getShop().setWalletType(type);
		return ChatColor.GREEN + CitiTrader.getResourceBundle().getString("conversation.admin.setbank") + ": " + type.name();
	}

}
