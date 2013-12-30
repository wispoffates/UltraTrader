
package com.thedemgel.ultratrader.conversation.addcategoryitem;

import com.thedemgel.ultratrader.UltraTrader;
import com.thedemgel.ultratrader.L;
import com.thedemgel.ultratrader.conversation.ConversationHandler;
import com.thedemgel.ultratrader.shop.CategoryItem;
import com.thedemgel.ultratrader.inventory.ShopInventoryView;
import com.thedemgel.ultratrader.util.Permissions;
import org.bukkit.Bukkit;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.MessagePrompt;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public class AddCategoryItemBeginPrompt extends MessagePrompt {

	@Override
	protected Prompt getNextPrompt(ConversationContext context) {
		final Player player = (Player) context.getForWhom();

        if (!player.hasPermission(Permissions.SHOP_CATEGORY_CREATE)) {
            player.sendRawMessage("You don't have permission to create a category.");
            return Prompt.END_OF_CONVERSATION;
        }

		ShopInventoryView view = (ShopInventoryView) UltraTrader.getStoreHandler().getInventoryHandler().getInventoryView(player);
		context.setSessionData(ConversationHandler.CONVERSATION_SESSION_VIEW, view);
		ItemStack item = (ItemStack) context.getSessionData(ConversationHandler.CONVERSATION_SESSION_ITEM);
		CategoryItem categoryItem = new CategoryItem(item);
        categoryItem.setAmount(1);
        categoryItem.setSlot(-1);
        context.setSessionData(ConversationHandler.CONVERSATION_SESSION_CATEGORYITEM, categoryItem);

        // TODO: add category limit
		//if (view.getShop().getPriceList().size() >= LimitHandler.getMaxBuySellSize(view.getShop())) {
		//	return new AddItemShopFullPrompt();
		//}

		view.setKeepAlive(true);
		Bukkit.getScheduler().scheduleSyncDelayedTask(UltraTrader.getInstance(), new Runnable() {
			@Override
			public void run() {
				player.closeInventory();
			}
		}, UltraTrader.BUKKIT_SCHEDULER_DELAY);

		return new AddCategoryItemPrompt();
	}

	@Override
	public String getPromptText(ConversationContext context) {
        // TODO: language addition/change
		return L.getString("conversation.addbuyitem.begin");
	}

}
