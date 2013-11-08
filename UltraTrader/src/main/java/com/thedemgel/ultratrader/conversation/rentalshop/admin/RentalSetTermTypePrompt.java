
package com.thedemgel.ultratrader.conversation.rentalshop.admin;

import com.thedemgel.ultratrader.L;
import com.thedemgel.ultratrader.UltraTrader;
import com.thedemgel.ultratrader.citizens.RentalShop;
import com.thedemgel.ultratrader.conversation.ConversationHandler;
import com.thedemgel.ultratrader.conversation.FixedIgnoreCaseSetPrompt;
import com.thedemgel.ultratrader.conversation.rentalshop.RentalConversationPrefix;
import java.util.concurrent.TimeUnit;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationPrefix;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;


public class RentalSetTermTypePrompt extends FixedIgnoreCaseSetPrompt {
	private ConversationPrefix prefix;
	private Player p;
	private NPC n;

	public RentalSetTermTypePrompt() {
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

		addOption("minutes", new RentalSetTermTypeDoPrompt(TimeUnit.MINUTES));
		addOption("hours", new RentalSetTermTypeDoPrompt(TimeUnit.HOURS));
		addOption("days", new RentalSetTermTypeDoPrompt(TimeUnit.DAYS));
		addOption(L.getString("general.exit"), new RentalAdminPrompt());

		p.sendRawMessage(prefix.getPrefix(cc) + "Choose length of each Term.");
		return L.getString("conversation.options") + ": " + this.formatFixedSet();
	}

}
