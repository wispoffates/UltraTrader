
package com.thedemgel.cititradersre.conversation.admin.bank;

import com.thedemgel.cititradersre.CitiTrader;
import com.thedemgel.cititradersre.conversation.admin.AdminMenuPrompt;
import com.thedemgel.cititradersre.util.ShopInventoryView;
import com.thedemgel.cititradersre.util.WalletType;
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
		final Player player = (Player) context.getForWhom();
		final ShopInventoryView view = (ShopInventoryView) CitiTrader.getStoreHandler().getInventoryHandler().getInventoryView(player);
		view.getShop().setWalletType(type);
		return ChatColor.GREEN + CitiTrader.getResourceBundle().getString("conversation.admin.setbank") + ": " + type.name();
	}

}
