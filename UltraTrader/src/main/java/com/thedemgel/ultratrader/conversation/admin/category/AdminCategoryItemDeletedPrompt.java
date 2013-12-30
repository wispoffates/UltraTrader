package com.thedemgel.ultratrader.conversation.admin.category;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.thedemgel.ultratrader.conversation.ConversationHandler;
import com.thedemgel.ultratrader.inventory.ShopInventoryView;
import com.thedemgel.ultratrader.shop.CategoryItem;
import com.thedemgel.ultratrader.shop.ItemPrice;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.MessagePrompt;
import org.bukkit.conversations.Prompt;

import javax.annotation.Nullable;
import java.util.Collection;

public class AdminCategoryItemDeletedPrompt extends MessagePrompt {
    @Override
    protected Prompt getNextPrompt(ConversationContext conversationContext) {
        CategoryItem categoryItem = (CategoryItem) conversationContext.getSessionData(AdminListCategoryPrompt.SESSION_LIST_SELECTION);
        final String catId = categoryItem.getCategoryId();
        ShopInventoryView view = (ShopInventoryView) conversationContext.getSessionData(ConversationHandler.CONVERSATION_SESSION_VIEW);

        view.getShop().getCategoryItem().remove(categoryItem.getCategoryId());

        Predicate<ItemPrice> itemPricePredicate = new Predicate<ItemPrice>() {
            @Override
            public boolean apply(@Nullable ItemPrice itemPrice) {
                return itemPrice.getCategoryId().equals(catId);
            }
        };

        Collection<ItemPrice> prices = Collections2.filter(view.getShop().getPriceList().values(), itemPricePredicate);

        for (ItemPrice price : prices) {
            view.getShop().getPriceList().remove(price.getId());
        }

        conversationContext.setSessionData(AdminListCategoryPrompt.SESSION_LIST_SELECTION, null);

        return new AdminCategoryMenuPrompt();
    }

    @Override
    public String getPromptText(ConversationContext conversationContext) {
        return "Category Deleted";
    }
}
