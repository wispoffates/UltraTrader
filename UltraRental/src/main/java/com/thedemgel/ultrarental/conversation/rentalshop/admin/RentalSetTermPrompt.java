
package com.thedemgel.ultrarental.conversation.rentalshop.admin;

import com.thedemgel.ultrarental.L;
import com.thedemgel.ultrarental.RentalShop;
import com.thedemgel.ultrarental.conversation.rentalshop.RentalConversationPrefix;
import com.thedemgel.ultratrader.conversation.ConversationHandler;
import com.thedemgel.ultratrader.conversation.FixedIgnoreCaseSetPrompt;
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

		addOption(L.getString("conversation.rental.admin.options.length"), new RentalSetTermLengthPrompt());
		addOption(L.getString("conversation.rental.admin.options.type"), new RentalSetTermTypePrompt());
		addOption(L.getString("general.exit"), new RentalAdminPrompt());

		p.sendRawMessage(prefix.getPrefix(cc) + "------<" + ChatColor.BLUE + prefix.getPrefix(cc) + ChatColor.YELLOW + ">------");
		p.sendRawMessage(prefix.getPrefix(cc) + L.getString("conversation.rental.termlength") + ": " + ChatColor.WHITE + rent.getTerm());
		p.sendRawMessage(prefix.getPrefix(cc) + L.getString("conversation.rental.termtype") + ": " + ChatColor.WHITE + rent.getTermType().name());
		p.sendRawMessage(prefix.getPrefix(cc) + L.getString("conversation.rental.periodlength") + ": " + ChatColor.WHITE + rent.getFormatedTerm());
		return L.getString("conversation.options") + ": " + this.formatFixedSet();
	}

}
