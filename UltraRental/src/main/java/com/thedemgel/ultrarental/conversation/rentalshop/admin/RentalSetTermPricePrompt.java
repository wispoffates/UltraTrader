package com.thedemgel.ultrarental.conversation.rentalshop.admin;

import com.thedemgel.ultrarental.L;
import com.thedemgel.ultrarental.RentalShop;
import com.thedemgel.ultratrader.conversation.ConversationHandler;
import com.thedemgel.ultratrader.conversation.NotADoublePrompt;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;

public class RentalSetTermPricePrompt extends StringPrompt {

	@Override
	public Prompt acceptInput(ConversationContext context, String input) {

		Double price;
		try {
			price = Double.valueOf(input);
		} catch (NumberFormatException ex) {
			context.setSessionData(ConversationHandler.CONVERSATION_SESSION_RETURN, this);
			return new NotADoublePrompt();
		}
		NPC npc = (NPC) context.getSessionData(ConversationHandler.CONVERSATION_SESSION_NPC);
		RentalShop rent = npc.getTrait(RentalShop.class);
		rent.setCost(price);
		return new RentalAdminPrompt();
	}

	@Override
	public String getPromptText(ConversationContext context) {
		return L.getString("conversation.rental.admin.periodcost");
	}
}
