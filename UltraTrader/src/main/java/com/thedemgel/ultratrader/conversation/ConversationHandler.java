package com.thedemgel.ultratrader.conversation;

import com.thedemgel.ultratrader.UltraTrader;
import com.thedemgel.ultratrader.conversation.addcategoryitem.AddCategoryItemBeginPrompt;
import com.thedemgel.ultratrader.conversation.addcategoryitem.AddCategoryItemConversationPrefix;
import com.thedemgel.ultratrader.conversation.addsellitem.AddItemBeginPrompt;
import com.thedemgel.ultratrader.conversation.addsellitem.AddItemConversationPrefix;
import com.thedemgel.ultratrader.conversation.admin.AdminBeginPrompt;
import com.thedemgel.ultratrader.conversation.admin.AdminConversationPrefix;
import com.thedemgel.ultratrader.conversation.createshop.CreateShopBeginPrompt;
import com.thedemgel.ultratrader.conversation.createshop.CreateShopConversationPrefix;
//import com.thedemgel.ultratrader.conversation.rentalshop.RentalBeginPrompt;
//import com.thedemgel.ultratrader.conversation.rentalshop.RentalConversationPrefix;
import com.thedemgel.ultratrader.conversation.sellitemadmin.AdminSellItemBeginPrompt;
import com.thedemgel.ultratrader.conversation.sellitemadmin.AdminSellItemConversationPrefix;

import org.bukkit.conversations.ConversationFactory;

public class ConversationHandler {

	public static final int CONVERSATION_TIMEOUT = 30;
	public static final String CONVERSATION_SESSION_ITEM = "item";
	public static final String CONVERSATION_SESSION_ITEMPRICE = "itemprice";
    public static final String CONVERSATION_SESSION_CATEGORYITEM = "categoryitem";
	public static final String CONVERSATION_SESSION_VIEW = "view";
	public static final String CONVERSATION_SESSION_DESCRIPTION = "description";
	public static final String CONVERSATION_SESSION_NPC = "npc";
	public static final String CONVERSATION_SESSION_BUYPRICE = "buyprice";
    public static final String CONVERSATION_SESSION_SELLPRICE = "sellprice";
	public static final String CONVERSATION_SESSION_RETURN = "return";
	public static final String CONVERSATION_SESSION_SLOT = "slot";
    public static final String CONVERSATION_SESSION_IS_BLOCK = "isblock";
    public static final String CONVERSATION_SESSION_BLOCK = "block";
	public static final int CONVERSATION_MAX_SHOP_NAME = 32;
	private ConversationFactory setSellPrice;
	private ConversationFactory addSellItem;
	private ConversationFactory adminConversation;
	private ConversationFactory createShop;
	//private ConversationFactory buyItemAdmin;
	private ConversationFactory addCategoryItem;

	public ConversationHandler(UltraTrader instance) {
		setSellPrice = new ConversationFactory(instance)
			.withModality(true)
			.withPrefix(new AdminSellItemConversationPrefix())
			.withFirstPrompt(new AdminSellItemBeginPrompt())
			.withEscapeSequence("/quit")
			.withTimeout(ConversationHandler.CONVERSATION_TIMEOUT)
			.addConversationAbandonedListener(new AbandonConvo())
			.thatExcludesNonPlayersWithMessage("No Console Please");

		/*buyItemAdmin = new ConversationFactory(instance)
			.withModality(true)
			.withPrefix(new AdminBuyItemConversationPrefix())
			.withFirstPrompt(new AdminBuyItemBeginPrompt())
			.withEscapeSequence("/quit")
			.withTimeout(ConversationHandler.CONVERSATION_TIMEOUT)
			.addConversationAbandonedListener(new AbandonConvo())
			.thatExcludesNonPlayersWithMessage("No Console Please"); */

		addSellItem = new ConversationFactory(instance)
			.withModality(true)
			.withPrefix(new AddItemConversationPrefix())
			.withFirstPrompt(new AddItemBeginPrompt())
			.withEscapeSequence("/quit")
			.withTimeout(ConversationHandler.CONVERSATION_TIMEOUT)
			.addConversationAbandonedListener(new AbandonConvo())
			.thatExcludesNonPlayersWithMessage("No Console Please");

		addCategoryItem = new ConversationFactory(instance)
			.withModality(true)
			.withPrefix(new AddCategoryItemConversationPrefix())
			.withFirstPrompt(new AddCategoryItemBeginPrompt())
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

	public ConversationFactory getAddCategoryItem() {
		return addCategoryItem;
	}

}
