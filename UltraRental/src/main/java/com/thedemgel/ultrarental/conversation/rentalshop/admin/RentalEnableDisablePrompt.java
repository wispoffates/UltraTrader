
package com.thedemgel.ultrarental.conversation.rentalshop.admin;

import com.thedemgel.ultrarental.RentalShop;
import com.thedemgel.ultratrader.conversation.ConversationHandler;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.conversations.BooleanPrompt;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;


public class RentalEnableDisablePrompt extends BooleanPrompt {
	private boolean enabled;

	public RentalEnableDisablePrompt(boolean bln) {
		enabled = bln;
	}

	@Override
	protected Prompt acceptValidatedInput(ConversationContext cc, boolean bln) {
		NPC npc = (NPC) cc.getSessionData(ConversationHandler.CONVERSATION_SESSION_NPC);
		RentalShop rent = npc.getTrait(RentalShop.class);
		if (bln) {
			rent.setRentingEnabled(enabled);
		}

		return new RentalAdminPrompt();
	}

	@Override
	public String getPromptText(ConversationContext cc) {
		return "Are you sure you wish to Enable/Disable this RentalShop? (Yes or No)";
	}

}
