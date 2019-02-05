
package com.thedemgel.ultratrader.conversation.admin.level;

import com.thedemgel.ultratrader.Lang;
import com.thedemgel.ultratrader.LimitHandler;
import com.thedemgel.ultratrader.UltraTrader;
import com.thedemgel.ultratrader.conversation.ConversationHandler;
import com.thedemgel.ultratrader.conversation.admin.AdminConversationPrefix;
import com.thedemgel.ultratrader.shop.Shop;
import com.thedemgel.ultratrader.inventory.ShopInventoryView;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.economy.EconomyResponse.ResponseType;
import org.bukkit.conversations.BooleanPrompt;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationPrefix;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;


public class AdminSetLevelIncreasePrompt extends BooleanPrompt {
	private Player player;
	private double price;
	private Shop shop;
	private int nextlevel;
	private ConversationPrefix prefix;

	public AdminSetLevelIncreasePrompt() {
		prefix = new AdminConversationPrefix();
	}

	@Override
	protected Prompt acceptValidatedInput(ConversationContext context, boolean input) {
		if (!input) {
			return new AdminSetLevelPrompt();
		}

		// Take the money
		EconomyResponse resp = UltraTrader.getEconomy().withdrawPlayer(player, player.getWorld().getName(), price);

		if (resp.type.equals(ResponseType.SUCCESS)) {
		// Increase the level
			shop.setLevel(nextlevel);
			player.sendRawMessage(prefix.getPrefix(context) + Lang.getFormatString("conversation.admin.level.raised", nextlevel));
		} else {
			player.sendRawMessage(prefix.getPrefix(context) + Lang.getString("conversation.admin.level.notenoughfunds"));
		}
		return new AdminSetLevelPrompt();
	}

	@Override
	public String getPromptText(ConversationContext context) {
		ShopInventoryView view = (ShopInventoryView) context.getSessionData(ConversationHandler.CONVERSATION_SESSION_VIEW);

		shop = view.getShop();
		player = (Player) context.getForWhom();
		nextlevel = shop.getLevel() + 1;

		price = LimitHandler.getLevelCost(player, nextlevel);

		return Lang.getFormatString("conversation.admin.level.proceed", price, nextlevel);
	}

}
