package com.thedemgel.ultrarental.conversation.rentalshop;

import com.thedemgel.ultratrader.L;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationPrefix;

public class RentalConversationPrefix implements ConversationPrefix {

	@Override
	public String getPrefix(ConversationContext context) {
		return ChatColor.BLUE + "[" + L.getString("conversation.rental.prefix") + "] " + ChatColor.YELLOW;
	}
}
