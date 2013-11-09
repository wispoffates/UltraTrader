package com.thedemgel.ultrarental.conversation.rentalshop;

import com.thedemgel.ultrarental.RentalShop;
import com.thedemgel.ultratrader.L;
import com.thedemgel.ultratrader.conversation.ConversationHandler;
import com.thedemgel.ultrarental.conversation.rentalshop.admin.RentalAdminPrompt;
import com.thedemgel.ultratrader.util.Permissions;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.trait.Owner;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationPrefix;
import org.bukkit.conversations.MessagePrompt;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;

/**
 * The first helpful message to administrate your shop.
 */
public class RentalBeginPrompt extends MessagePrompt {

	private ConversationPrefix prefix;
	private Player p;
	private NPC n;

	public RentalBeginPrompt() {
		prefix = new RentalConversationPrefix();
	}

	@Override
	protected final Prompt getNextPrompt(ConversationContext context) {
		Owner owner = n.getTrait(Owner.class);

		if (owner.isOwnedBy(p)) {
			return new RentalAdminPrompt();
		}

		if (p.hasPermission(Permissions.NPC_RENT)) {
			if (!n.getTrait(RentalShop.class).isRentingEnabled()) {
				// GOTO admin interface if owner...
				return Prompt.END_OF_CONVERSATION;
			}
			return new RentalConfirmPrompt();
		} else {
			return Prompt.END_OF_CONVERSATION;
		}
	}

	@Override
	public final String getPromptText(ConversationContext context) {
		p = (Player) context.getForWhom();
		n = (NPC) context.getSessionData(ConversationHandler.CONVERSATION_SESSION_NPC);

		RentalShop shop = n.getTrait(RentalShop.class);

		Owner owner = n.getTrait(Owner.class);

		if (owner.isOwnedBy(p)) {
			p.sendRawMessage(prefix.getPrefix(context) + L.getString("conversation.rental.admin.begin"));
			return L.getString("conversation.toquit");
		}

		if (p.hasPermission(Permissions.NPC_RENT)) {
			if (!shop.isRentingEnabled()) {
				return ChatColor.RED + L.getString("conversation.rental.closed");
			} else {
				p.sendRawMessage(prefix.getPrefix(context) + L.getString("conversation.rental.begin"));
				return L.getString("conversation.toquit");
			}
		} else {
			return L.getString("permission.rent.deny");
		}


	}
}
