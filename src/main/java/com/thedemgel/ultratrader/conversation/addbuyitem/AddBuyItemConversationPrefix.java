
package com.thedemgel.ultratrader.conversation.addbuyitem;

import com.thedemgel.ultratrader.L;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationPrefix;


public class AddBuyItemConversationPrefix implements ConversationPrefix {

	@Override
	public String getPrefix(ConversationContext context) {
		return ChatColor.BLUE + "[" + L.getString("conversation.addbuyitem.prefix") + "] " + ChatColor.YELLOW;
	}

}
