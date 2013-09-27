package com.thedemgel.cititradersre.conversation.setsellprice;

import com.thedemgel.cititradersre.CitiTrader;
import com.thedemgel.cititradersre.conversation.NotADoublePrompt;
import com.thedemgel.cititradersre.util.ShopInventoryView;
import java.math.BigDecimal;
import java.text.MessageFormat;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.NumericPrompt;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SetPricePrompt extends StringPrompt {

	@Override
	public Prompt acceptInput(ConversationContext context, String input) {

		Double price;
		try {
			price = Double.valueOf(input);
		} catch (NumberFormatException ex) {
			context.setSessionData("return", new SetPricePrompt());
			return new NotADoublePrompt();
		}
		context.setSessionData("price", BigDecimal.valueOf(price));
		return new SetPriceFinishPrompt();
	}

	@Override
	public String getPromptText(ConversationContext context) {
		Player player = (Player) context.getForWhom();
		ShopInventoryView view = (ShopInventoryView) CitiTrader.getStoreHandler().getInventoryHandler().getInventoryView(player);
		view.setKeepAlive(true);
		player.closeInventory();
		ItemStack clickitem = (ItemStack) context.getSessionData("item");
		String id = view.getShop().getItemId(clickitem);
		ItemStack item = view.getShop().getSellprices().get(id).getItemStack();
		return MessageFormat.format(CitiTrader.getResourceBundle().getString("conversation.setsellprice.setprice"), item.getType().name());
	}
}
