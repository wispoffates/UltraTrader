
package com.thedemgel.ultratrader.conversation.addcategoryitem;

import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationPrefix;


public class AddCategoryItemConversationPrefix implements ConversationPrefix {

	@Override
	public String getPrefix(ConversationContext context) {
        // TODO: language addition
		return ChatColor.BLUE + "[" + "ADD CATEGORY" + "] " + ChatColor.YELLOW;
	}

}
