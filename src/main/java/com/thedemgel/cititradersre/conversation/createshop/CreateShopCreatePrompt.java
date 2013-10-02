package com.thedemgel.cititradersre.conversation.createshop;

import com.thedemgel.cititradersre.CitiTrader;
import com.thedemgel.cititradersre.citizens.TraderTrait;
import com.thedemgel.cititradersre.conversation.ConversationHandler;
import com.thedemgel.cititradersre.shop.Shop;
import java.util.ResourceBundle;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationPrefix;
import org.bukkit.conversations.MessagePrompt;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;

public class CreateShopCreatePrompt extends MessagePrompt {

	private ResourceBundle rb;
	private ConversationPrefix prefix;

	public CreateShopCreatePrompt() {
		rb = CitiTrader.getResourceBundle();
		prefix = new CreateShopConversationPrefix();
	}

	@Override
	protected Prompt getNextPrompt(ConversationContext context) {
		Player player = (Player) context.getForWhom();

		Shop shop = CitiTrader.getStoreHandler().createShop(player);

		if (shop != null) {
			// TODO: check for blocks and items (or change this to NPC conversation only)
			NPC npc = (NPC) context.getSessionData(ConversationHandler.CONVERSATION_SESSION_NPC);
			npc.getTrait(TraderTrait.class).setShopId(shop.getId());
			player.sendRawMessage(prefix.getPrefix(context) + ChatColor.GREEN + rb.getString("conversation.createshop.create.created"));
			return Prompt.END_OF_CONVERSATION;
		} else {
			player.sendRawMessage(prefix.getPrefix(context) + ChatColor.RED + rb.getString("conversation.createshop.create.error"));
			return new CreateShopMenuPrompt();
		}
	}

	@Override
	public String getPromptText(ConversationContext context) {
		return rb.getString("conversation.createshop.create.creating");
	}
}
