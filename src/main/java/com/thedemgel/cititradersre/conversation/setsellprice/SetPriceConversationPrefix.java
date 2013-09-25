
package com.thedemgel.cititradersre.conversation.setsellprice;

import com.thedemgel.cititradersre.CitiTrader;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationPrefix;


public class SetPriceConversationPrefix implements ConversationPrefix {

	@Override
	public String getPrefix(ConversationContext context) {
		return ChatColor.BLUE + "[" + CitiTrader.getResourceBundle().getString("conversation.setsellprice.prefix") + "] " + ChatColor.YELLOW;
	}

}
