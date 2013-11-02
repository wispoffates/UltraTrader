
package com.thedemgel.ultratrader.conversation.rentalshop;

import com.thedemgel.ultratrader.UltraTrader;
import com.thedemgel.ultratrader.citizens.RentalShop;
import com.thedemgel.ultratrader.conversation.ConversationHandler;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.ChatColor;
import org.bukkit.conversations.BooleanPrompt;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationPrefix;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;


public class RentalConfirmPrompt extends BooleanPrompt {
	private ConversationPrefix prefix;
	private Player p;
	private NPC n;

	public RentalConfirmPrompt() {
		prefix = new RentalConversationPrefix();
	}

	@Override
	protected Prompt acceptValidatedInput(ConversationContext cc, boolean bln) {
		// Do rental Logic here.
		return Prompt.END_OF_CONVERSATION;
	}

	@Override
	public String getPromptText(ConversationContext cc) {
		p = (Player) cc.getForWhom();
		n = (NPC) cc.getSessionData(ConversationHandler.CONVERSATION_SESSION_NPC);

		RentalShop rent = n.getTrait(RentalShop.class);

		p.sendRawMessage(prefix.getPrefix(cc) + "------<[ " + ChatColor.BLUE + prefix.getPrefix(cc) + ChatColor.YELLOW + " ]>------");
		p.sendRawMessage(prefix.getPrefix(cc) + "Cost per Period: " + UltraTrader.getEconomy().format(rent.getCost()));
		//p.sendRawMessage(prefix.getPrefix(cc) + "Rental Period: " + rent.getFormatedTerm());
		return "Are you sure you want to rent this NPC?";
	}

}
