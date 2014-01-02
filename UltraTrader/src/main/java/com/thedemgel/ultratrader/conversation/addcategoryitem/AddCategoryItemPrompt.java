package com.thedemgel.ultratrader.conversation.addcategoryitem;

import com.thedemgel.ultratrader.conversation.ConversationHandler;
import java.text.MessageFormat;

import com.thedemgel.ultratrader.inventory.ShopInventoryView;
import com.thedemgel.ultratrader.shop.CategoryItem;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.inventory.ItemStack;

public class AddCategoryItemPrompt extends StringPrompt {
    public static final String ADD_CATEGORY_SUCCESS = "addCategorySuccess";

	@Override
	public Prompt acceptInput(ConversationContext context, String input) {
        ShopInventoryView view = (ShopInventoryView) context.getSessionData(ConversationHandler.CONVERSATION_SESSION_VIEW);
        CategoryItem categoryItem = (CategoryItem) context.getSessionData(ConversationHandler.CONVERSATION_SESSION_CATEGORYITEM);
        categoryItem.setDisplayName(input);

        String shopId = input.replaceAll("[^a-zA-Z]", "");

        int i = 1;
        while (view.getShop().getCategoryItem().containsKey(shopId)) {
            shopId += i;
            i++;
        }

        categoryItem.setCategoryId(input.replaceAll("[^a-zA-Z]", ""));

		return new AddCategoryItemDescriptionPrompt();
	}

	@Override
	public String getPromptText(ConversationContext context) {
		ItemStack item = (ItemStack) context.getSessionData(ConversationHandler.CONVERSATION_SESSION_ITEM);

        // TODO: language addition
		return MessageFormat.format("What display name would you like for {0}?", item.getType().name());
	}
}
