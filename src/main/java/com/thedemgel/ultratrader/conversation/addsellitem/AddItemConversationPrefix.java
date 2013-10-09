
package com.thedemgel.ultratrader.conversation.addsellitem;

import com.thedemgel.ultratrader.L;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationPrefix;


public class AddItemConversationPrefix implements ConversationPrefix {

	@Override
	public String getPrefix(ConversationContext context) {
		return ChatColor.BLUE + "[" + L.getString("conversation.additem.prefix") + "] " + ChatColor.YELLOW;
	}

}
