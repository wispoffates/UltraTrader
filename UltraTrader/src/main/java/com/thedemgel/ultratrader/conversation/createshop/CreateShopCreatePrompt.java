package com.thedemgel.ultratrader.conversation.createshop;

import com.thedemgel.ultratrader.UltraTrader;
import com.thedemgel.ultratrader.L;
import com.thedemgel.ultratrader.citizens.TraderTrait;
import com.thedemgel.ultratrader.conversation.ConversationHandler;
import com.thedemgel.ultratrader.shop.Shop;
import java.util.ResourceBundle;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationPrefix;
import org.bukkit.conversations.MessagePrompt;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;

public class CreateShopCreatePrompt extends MessagePrompt {

	private ConversationPrefix prefix;

	public CreateShopCreatePrompt() {
		prefix = new CreateShopConversationPrefix();
	}

	@Override
	protected Prompt getNextPrompt(ConversationContext context) {
		Player player = (Player) context.getForWhom();

		Shop shop;
		try {
		shop = UltraTrader.getStoreHandler().createShop(player);
		} catch (Exception e) {
			e.printStackTrace();
			return Prompt.END_OF_CONVERSATION;
		}

		if (shop != null) {
			// TODO: check for blocks and items (or change this to NPC conversation only)
			NPC npc = (NPC) context.getSessionData(ConversationHandler.CONVERSATION_SESSION_NPC);
			npc.getTrait(TraderTrait.class).setShopId(shop.getId());
			player.sendRawMessage(prefix.getPrefix(context) + ChatColor.GREEN + L.getString("conversation.createshop.create.created"));
			return Prompt.END_OF_CONVERSATION;
		} else {
			player.sendRawMessage(prefix.getPrefix(context) + ChatColor.RED + L.getString("conversation.createshop.create.error"));
			return new CreateShopMenuPrompt();
		}
	}

	@Override
	public String getPromptText(ConversationContext context) {
		return L.getString("conversation.createshop.create.creating");
	}
}
