
package com.thedemgel.ultrarental.conversation.rentalshop.admin;

import com.thedemgel.ultrarental.L;
import com.thedemgel.ultrarental.conversation.rentalshop.RentalConversationPrefix;
import com.thedemgel.ultratrader.conversation.ConversationHandler;
import com.thedemgel.ultratrader.conversation.FixedIgnoreCaseSetPrompt;
import java.util.concurrent.TimeUnit;
import net.citizensnpcs.api.npc.NPC;
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
		//RentalShop rent = n.getTrait(RentalShop.class);

		addOption(L.getString("conversation.rental.admin.options.minutes"), new RentalSetTermTypeDoPrompt(TimeUnit.MINUTES));
		addOption(L.getString("conversation.rental.admin.options.hours"), new RentalSetTermTypeDoPrompt(TimeUnit.HOURS));
		addOption(L.getString("conversation.rental.admin.options.days"), new RentalSetTermTypeDoPrompt(TimeUnit.DAYS));
		addOption(L.getString("general.exit"), new RentalAdminPrompt());

		p.sendRawMessage(prefix.getPrefix(cc) + L.getString("conversation.rental.admin.termchoose"));
		return L.getString("conversation.options") + ": " + this.formatFixedSet();
	}

}
