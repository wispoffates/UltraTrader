
package com.thedemgel.ultrarental.conversation.rentalshop.admin;

import com.thedemgel.ultrarental.L;
import com.thedemgel.ultrarental.RentalShop;
import com.thedemgel.ultratrader.conversation.ConversationHandler;
import java.util.concurrent.TimeUnit;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.MessagePrompt;
import org.bukkit.conversations.Prompt;


public class RentalSetTermTypeDoPrompt extends MessagePrompt {

	private TimeUnit unit;

	public RentalSetTermTypeDoPrompt(TimeUnit unit) {
		this.unit = unit;
	}

	@Override
	protected Prompt getNextPrompt(ConversationContext cc) {
		return new RentalSetTermPrompt();
	}

	@Override
	public String getPromptText(ConversationContext cc) {
		NPC n = (NPC) cc.getSessionData(ConversationHandler.CONVERSATION_SESSION_NPC);
		RentalShop rent = n.getTrait(RentalShop.class);

		rent.setTermType(unit);

		return L.getString("conversation.rental.admin.setterm") + ": " + ChatColor.WHITE + unit.name();
	}

}
