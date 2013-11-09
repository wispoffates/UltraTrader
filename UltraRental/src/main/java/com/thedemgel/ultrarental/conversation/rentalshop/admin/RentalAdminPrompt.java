
package com.thedemgel.ultrarental.conversation.rentalshop.admin;

import com.thedemgel.ultrarental.RentalShop;
import com.thedemgel.ultrarental.conversation.rentalshop.RentalConversationPrefix;
import com.thedemgel.ultratrader.L;
import com.thedemgel.ultratrader.UltraTrader;
import com.thedemgel.ultratrader.conversation.ConversationHandler;
import com.thedemgel.ultratrader.conversation.FixedIgnoreCaseSetPrompt;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationPrefix;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;


public class RentalAdminPrompt extends FixedIgnoreCaseSetPrompt {
	private ConversationPrefix prefix;
	private Player p;
	private NPC n;

	public RentalAdminPrompt() {
		prefix = new RentalConversationPrefix();
	}

	@Override
	public String getPromptText(ConversationContext cc) {
		p = (Player) cc.getForWhom();
		n = (NPC) cc.getSessionData(ConversationHandler.CONVERSATION_SESSION_NPC);
		RentalShop rent = n.getTrait(RentalShop.class);

		addOption("term", new RentalSetTermPrompt());
		addOption("price", new RentalSetTermPricePrompt());
		if (rent.isRentingEnabled()) {
			addOption("disable", new RentalEnableDisablePrompt(false));
		} else {
			addOption("enable", new RentalEnableDisablePrompt(true));
		}

		addOption(L.getString("general.exit"), Prompt.END_OF_CONVERSATION);

		p.sendRawMessage(prefix.getPrefix(cc) + "------<[ " + ChatColor.BLUE + prefix.getPrefix(cc) + ChatColor.YELLOW + " ]>------");
		p.sendRawMessage(prefix.getPrefix(cc) + "Cost per Period: " + UltraTrader.getEconomy().format(rent.getCost()));
		p.sendRawMessage(prefix.getPrefix(cc) + "Rental Period: " + rent.getFormatedTerm());
		return L.getString("conversation.options") + ": " + this.formatFixedSet();
	}

	@Override
	protected Prompt acceptValidatedInput(ConversationContext cc, String string) {
		return getValidatedPrompt(this);
	}

}
