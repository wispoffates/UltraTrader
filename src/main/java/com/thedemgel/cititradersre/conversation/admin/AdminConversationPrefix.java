
package com.thedemgel.cititradersre.conversation.admin;

import com.thedemgel.cititradersre.conversation.additem.*;
import com.thedemgel.cititradersre.conversation.setsellprice.*;
import com.thedemgel.cititradersre.CitiTrader;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationPrefix;


public class AdminConversationPrefix implements ConversationPrefix {

	@Override
	public String getPrefix(ConversationContext context) {
		return ChatColor.BLUE + "[" + CitiTrader.getResourceBundle().getString("conversation.admin.prefix") + "] " + ChatColor.YELLOW;
	}

}
