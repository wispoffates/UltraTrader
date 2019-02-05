package com.thedemgel.ultratrader.conversation.createshop;

import com.thedemgel.ultratrader.Lang;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationPrefix;

public class CreateShopConversationPrefix implements ConversationPrefix {

	@Override
	public String getPrefix(ConversationContext context) {
		return ChatColor.BLUE + "[" + Lang.getString("conversation.createshop.prefix") + "] " + ChatColor.YELLOW;
	}
}
