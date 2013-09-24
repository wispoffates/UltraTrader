package com.thedemgel.cititradersre.conversation.setsellprice;

import com.thedemgel.cititradersre.CitiTrader;
import com.thedemgel.cititradersre.util.ShopInventoryView;
import java.math.BigDecimal;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.NumericPrompt;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SetPricePrompt extends NumericPrompt {

	@Override
	protected Prompt acceptValidatedInput(ConversationContext context, Number input) {

		context.setSessionData("price", BigDecimal.valueOf(input.doubleValue()));
		return new SetPriceFinishPrompt();
	}

	@Override
	public String getPromptText(ConversationContext context) {
		Player player = (Player) context.getForWhom();
		((ShopInventoryView) CitiTrader.getStoreHandler().getInventoryHandler().getInventoryView(player)).setKeepAlive(true);
		player.closeInventory();
		ItemStack item = (ItemStack) context.getSessionData("item");
		return "What would you like to charge for " + item.getType().name() + "?";
	}
}
