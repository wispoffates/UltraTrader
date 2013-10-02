
package com.thedemgel.cititradersre.conversation.createshop;

import com.thedemgel.cititradersre.conversation.additem.*;
import com.thedemgel.cititradersre.CitiTrader;
import com.thedemgel.cititradersre.citizens.TraderTrait;
import com.thedemgel.cititradersre.shop.ItemPrice;
import com.thedemgel.cititradersre.util.ShopInventoryView;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.MessagePrompt;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public class CreateShopBeginPrompt extends MessagePrompt {

	@Override
	protected Prompt getNextPrompt(ConversationContext context) {
		return new CreateShopMenuPrompt();
	}

	@Override
	public String getPromptText(ConversationContext context) {		
		return CitiTrader.getResourceBundle().getString("conversation.createshop.begin");
	}

}
