package com.thedemgel.ultratrader.conversation.sellitemadmin;

import com.thedemgel.ultratrader.conversation.ConversationHandler;
import com.thedemgel.ultratrader.conversation.admin.category.AdminListCategoryPrompt;
import com.thedemgel.ultratrader.shop.CategoryItem;
import com.thedemgel.ultratrader.shop.ItemPrice;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.MessagePrompt;
import org.bukkit.conversations.Prompt;

public class AdminSellItemTransferCategoryPrompt extends MessagePrompt {
    @Override
    protected Prompt getNextPrompt(ConversationContext conversationContext) {
        CategoryItem categoryItem = (CategoryItem) conversationContext.getSessionData(AdminListCategoryPrompt.SESSION_LIST_SELECTION);

        if (categoryItem == null) {
            return new AdminListCategoryPrompt(this);
        }

        ItemPrice itemPrice = (ItemPrice) conversationContext.getSessionData(ConversationHandler.CONVERSATION_SESSION_ITEMPRICE);
        itemPrice.setCategoryId(categoryItem.getCategoryId());

        conversationContext.setSessionData(AdminListCategoryPrompt.SESSION_LIST_SELECTION, null);

        conversationContext.getForWhom().sendRawMessage("Item transferred to Category " + categoryItem.getDisplayName());
        return new AdminSellItemMenuPrompt();
    }

    @Override
    public String getPromptText(ConversationContext conversationContext) {
        return "";
    }
}
