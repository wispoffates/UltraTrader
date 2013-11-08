
package com.thedemgel.ultratrader.conversation.rentalshop;

import com.thedemgel.ultratrader.RentalHandler;
import com.thedemgel.ultratrader.citizens.RentalShop;
import com.thedemgel.ultratrader.conversation.ConversationHandler;
import com.thedemgel.ultratrader.conversation.admin.AdminMenuPrompt;
import com.thedemgel.ultratrader.shop.ShopInventoryView;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.conversations.BooleanPrompt;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;


public class RentalEndRentingPrompt extends BooleanPrompt {

	@Override
	protected Prompt acceptValidatedInput(ConversationContext cc, boolean bln) {
		ShopInventoryView view = (ShopInventoryView) cc.getSessionData(ConversationHandler.CONVERSATION_SESSION_VIEW);
		NPC npc = (NPC) view.getTarget();

		if (bln) {
			RentalHandler.clearNPC(npc);
			return Prompt.END_OF_CONVERSATION;
		} else {
			return new AdminMenuPrompt();
		}
	}

	@Override
	public String getPromptText(ConversationContext cc) {
		return "Do you want to stop renting this NPC? (It will remove this shop from this NPC)";
	}

}
