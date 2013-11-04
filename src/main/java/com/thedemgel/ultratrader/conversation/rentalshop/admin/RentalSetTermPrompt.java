
package com.thedemgel.ultratrader.conversation.rentalshop.admin;

import com.thedemgel.ultratrader.L;
import com.thedemgel.ultratrader.UltraTrader;
import com.thedemgel.ultratrader.citizens.RentalShop;
import com.thedemgel.ultratrader.conversation.ConversationHandler;
import com.thedemgel.ultratrader.conversation.FixedIgnoreCaseSetPrompt;
import com.thedemgel.ultratrader.conversation.rentalshop.RentalConversationPrefix;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationPrefix;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;


public class RentalSetTermPrompt extends FixedIgnoreCaseSetPrompt {
	private ConversationPrefix prefix;
	private Player p;
	private NPC n;

	public RentalSetTermPrompt() {
		prefix = new RentalConversationPrefix();
	}

	@Override
	protected Prompt acceptValidatedInput(ConversationContext cc, String string) {
		return getValidatedPrompt(this);
	}

	@Override
	public String getPromptText(ConversationContext cc) {
		p = (Player) cc.getForWhom();
		n = (NPC) cc.getSessionData(ConversationHandler.CONVERSATION_SESSION_NPC);
		RentalShop rent = n.getTrait(RentalShop.class);

		addOption("length", new RentalSetTermLengthPrompt());
		addOption("type", new RentalSetTermTypePrompt());
		addOption(L.getString("general.exit"), new RentalAdminPrompt());

		p.sendRawMessage(prefix.getPrefix(cc) + "------<" + ChatColor.BLUE + prefix.getPrefix(cc) + ChatColor.YELLOW + ">------");
		p.sendRawMessage(prefix.getPrefix(cc) + "Term Length: " + rent.getTerm());
		p.sendRawMessage(prefix.getPrefix(cc) + "Term Type: " + rent.getTermType().name());
		p.sendRawMessage(prefix.getPrefix(cc) + "Rental Period: " + rent.getFormatedTerm());
		return L.getString("conversation.options") + ": " + this.formatFixedSet();
	}

}
