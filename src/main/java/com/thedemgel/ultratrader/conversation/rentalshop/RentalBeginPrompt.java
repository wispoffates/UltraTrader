package com.thedemgel.ultratrader.conversation.rentalshop;

import com.thedemgel.ultratrader.conversation.admin.*;
import com.thedemgel.ultratrader.UltraTrader;
import com.thedemgel.ultratrader.L;
import com.thedemgel.ultratrader.citizens.RentalShop;
import com.thedemgel.ultratrader.conversation.ConversationHandler;
import com.thedemgel.ultratrader.shop.ShopInventoryView;
import com.thedemgel.ultratrader.util.Permissions;
import java.util.ResourceBundle;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
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

		if (p.hasPermission(Permissions.NPC_RENT)) {
			if (!shop.isRentingEnabled()) {
				p.sendRawMessage(prefix.getPrefix(context) + "Npc is not open for renting.");
			} else {
				p.sendRawMessage(prefix.getPrefix(context) + L.getString("conversation.rental.begin"));
			}
		} else {
			p.sendRawMessage(prefix.getPrefix(context) + "You don't have permission to rent shops.");
		}

		return L.getString("conversation.toquit");
	}
}
