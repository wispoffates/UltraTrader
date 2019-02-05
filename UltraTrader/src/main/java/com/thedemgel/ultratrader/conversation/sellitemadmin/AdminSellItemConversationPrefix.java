package com.thedemgel.ultratrader.conversation.sellitemadmin;

import com.thedemgel.ultratrader.Lang;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationPrefix;

public class AdminSellItemConversationPrefix implements ConversationPrefix {

	@Override
	public String getPrefix(ConversationContext context) {
		return ChatColor.BLUE + "[" + Lang.getString("conversation.itemadmin.prefix") + "] " + ChatColor.YELLOW;
	}
}
