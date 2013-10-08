package com.thedemgel.cititradersre.conversation;

import com.thedemgel.cititradersre.CitiTrader;
import com.thedemgel.cititradersre.conversation.addbuyitem.AddBuyItemBeginPrompt;
import com.thedemgel.cititradersre.conversation.addbuyitem.AddBuyItemConversationPrefix;
import com.thedemgel.cititradersre.conversation.addsellitem.AddItemBeginPrompt;
import com.thedemgel.cititradersre.conversation.addsellitem.AddItemConversationPrefix;
import com.thedemgel.cititradersre.conversation.admin.AdminBeginPrompt;
import com.thedemgel.cititradersre.conversation.admin.AdminConversationPrefix;
import com.thedemgel.cititradersre.conversation.buyitemadmin.AdminBuyItemBeginPrompt;
import com.thedemgel.cititradersre.conversation.buyitemadmin.AdminBuyItemConversationPrefix;
import com.thedemgel.cititradersre.conversation.createshop.CreateShopBeginPrompt;
import com.thedemgel.cititradersre.conversation.createshop.CreateShopConversationPrefix;
import com.thedemgel.cititradersre.conversation.sellitemadmin.AdminSellItemBeginPrompt;
import com.thedemgel.cititradersre.conversation.sellitemadmin.AdminSellItemConversationPrefix;
import org.bukkit.conversations.ConversationFactory;

public class ConversationHandler {

	public static final int CONVERSATION_TIMEOUT = 20;
	public static final String CONVERSATION_SESSION_ITEM = "item";
	public static final String CONVERSATION_SESSION_ITEMPRICE = "itemprice";
	public static final String CONVERSATION_SESSION_VIEW = "view";
	public static final String CONVERSATION_SESSION_DESCRIPTION = "description";
	public static final String CONVERSATION_SESSION_NPC = "npc";
	public static final String CONVERSATION_SESSION_PRICE = "price";
	public static final String CONVERSATION_SESSION_RETURN = "return";
	public static final String CONVERSATION_SESSION_SLOT = "slot";
	public static final int CONVERSATION_MAX_SHOP_NAME = 32;
	private ConversationFactory setSellPrice;
	private ConversationFactory addSellItem;
	private ConversationFactory adminConversation;
	private ConversationFactory createShop;
	private ConversationFactory buyItemAdmin;
	private ConversationFactory addBuyItem;

	public ConversationHandler(CitiTrader instance) {
		setSellPrice = new ConversationFactory(instance)
			.withModality(true)
			.withPrefix(new AdminSellItemConversationPrefix())
			.withFirstPrompt(new AdminSellItemBeginPrompt())
			.withEscapeSequence("/quit")
			.withTimeout(ConversationHandler.CONVERSATION_TIMEOUT)
			.addConversationAbandonedListener(new AbandonConvo())
			.thatExcludesNonPlayersWithMessage("No Console Please");

		buyItemAdmin = new ConversationFactory(instance)
			.withModality(true)
			.withPrefix(new AdminBuyItemConversationPrefix())
			.withFirstPrompt(new AdminBuyItemBeginPrompt())
			.withEscapeSequence("/quit")
			.withTimeout(ConversationHandler.CONVERSATION_TIMEOUT)
			.addConversationAbandonedListener(new AbandonConvo())
			.thatExcludesNonPlayersWithMessage("No Console Please");

		addSellItem = new ConversationFactory(instance)
			.withModality(true)
			.withPrefix(new AddItemConversationPrefix())
			.withFirstPrompt(new AddItemBeginPrompt())
			.withEscapeSequence("/quit")
			.withTimeout(ConversationHandler.CONVERSATION_TIMEOUT)
			.addConversationAbandonedListener(new AbandonConvo())
			.thatExcludesNonPlayersWithMessage("No Console Please");

		addBuyItem = new ConversationFactory(instance)
			.withModality(true)
			.withPrefix(new AddBuyItemConversationPrefix())
			.withFirstPrompt(new AddBuyItemBeginPrompt())
			.withEscapeSequence("/quit")
			.withTimeout(ConversationHandler.CONVERSATION_TIMEOUT)
			.addConversationAbandonedListener(new AbandonConvo())
			.thatExcludesNonPlayersWithMessage("No Console Please");

		adminConversation = new ConversationFactory(instance)
			.withModality(true)
			.withPrefix(new AdminConversationPrefix())
			.withFirstPrompt(new AdminBeginPrompt())
			.withEscapeSequence("/quit")
			.withTimeout(ConversationHandler.CONVERSATION_TIMEOUT)
			.addConversationAbandonedListener(new AbandonConvo())
			.thatExcludesNonPlayersWithMessage("No Console Please");

		createShop = new ConversationFactory(instance)
			.withModality(true)
			.withPrefix(new CreateShopConversationPrefix())
			.withFirstPrompt(new CreateShopBeginPrompt())
			.withEscapeSequence("/quit")
			.withTimeout(ConversationHandler.CONVERSATION_TIMEOUT)
			.addConversationAbandonedListener(new AbandonConvo())
			.thatExcludesNonPlayersWithMessage("No Console Please");
	}

	public ConversationFactory getSetSellPrice() {
		return setSellPrice;
	}

	public ConversationFactory getAddSellItem() {
		return addSellItem;
	}

	public ConversationFactory getAdminConversation() {
		return adminConversation;
	}

	public ConversationFactory getCreateShop() {
		return createShop;
	}

	public ConversationFactory getBuyItemAdmin() {
		return buyItemAdmin;
	}

	public ConversationFactory getAddBuyItem() {
		return addBuyItem;
	}
}
