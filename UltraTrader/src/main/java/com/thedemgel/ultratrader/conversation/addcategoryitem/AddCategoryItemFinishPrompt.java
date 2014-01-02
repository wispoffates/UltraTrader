package com.thedemgel.ultratrader.conversation.addcategoryitem;

import com.thedemgel.ultratrader.UltraTrader;
import com.thedemgel.ultratrader.L;
import com.thedemgel.ultratrader.conversation.ConversationHandler;
import com.thedemgel.ultratrader.inventory.ShopInventoryView;
import java.math.BigDecimal;

import com.thedemgel.ultratrader.shop.CategoryItem;
import org.bukkit.Bukkit;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.MessagePrompt;
import org.bukkit.conversations.Prompt;
import org.bukkit.inventory.ItemStack;

public class AddCategoryItemFinishPrompt extends MessagePrompt {

	@Override
	protected Prompt getNextPrompt(ConversationContext context) {
		return Prompt.END_OF_CONVERSATION;
	}

	@Override
	public String getPromptText(ConversationContext context) {
        try {
		    final ShopInventoryView view = (ShopInventoryView) context.getSessionData(ConversationHandler.CONVERSATION_SESSION_VIEW);
            final ItemStack item = (ItemStack) context.getSessionData(ConversationHandler.CONVERSATION_SESSION_ITEM);

            boolean success = (boolean) context.getSessionData(AddCategoryItemPrompt.ADD_CATEGORY_SUCCESS);

            if (success) {
                CategoryItem categoryItem = (CategoryItem) context.getSessionData(ConversationHandler.CONVERSATION_SESSION_CATEGORYITEM);
                view.getShop().getCategoryItem().put(categoryItem.getCategoryId(), categoryItem);
            }

		    view.buildCategoryView();

		    Bukkit.getScheduler().scheduleSyncDelayedTask(UltraTrader.getInstance(), new Runnable() {
			    @Override
			    public void run() {
				    view.getBottomInventory().addItem(item);
			    }
		    }, UltraTrader.BUKKIT_SCHEDULER_DELAY);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "End Category Addition.";
	}
}
