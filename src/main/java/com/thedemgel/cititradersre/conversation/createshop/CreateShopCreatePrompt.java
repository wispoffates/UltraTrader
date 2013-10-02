
package com.thedemgel.cititradersre.conversation.createshop;

import com.thedemgel.cititradersre.CitiTrader;
import com.thedemgel.cititradersre.citizens.TraderTrait;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.MessagePrompt;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;


public class CreateShopCreatePrompt extends MessagePrompt {

	@Override
	protected Prompt getNextPrompt(ConversationContext context) {
		Player player = (Player) context.getForWhom();
		
		NPC npc = (NPC) context.getSessionData("npc");
		npc.getTrait(TraderTrait.class).setShopId(1);
		context.setSessionData("create", true);
		context.setSessionData("id", 1);
		return new CreateShopMenuPrompt();
	}

	@Override
	public String getPromptText(ConversationContext context) {		
		return CitiTrader.getResourceBundle().getString("conversation.createshop.begin");
	}

}
