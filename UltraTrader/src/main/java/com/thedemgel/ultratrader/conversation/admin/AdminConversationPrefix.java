package com.thedemgel.ultratrader.conversation.admin;

import com.thedemgel.ultratrader.Lang;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationPrefix;

public class AdminConversationPrefix implements ConversationPrefix {

	@Override
	public String getPrefix(ConversationContext context) {
		return ChatColor.BLUE + "[" + Lang.getString("conversation.admin.prefix") + "] " + ChatColor.YELLOW;
	}
}
