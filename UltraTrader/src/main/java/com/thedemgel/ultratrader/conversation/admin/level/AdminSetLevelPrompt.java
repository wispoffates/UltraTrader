
package com.thedemgel.ultratrader.conversation.admin.level;

import com.thedemgel.ultratrader.L;
import com.thedemgel.ultratrader.LimitHandler;
import com.thedemgel.ultratrader.conversation.ConversationHandler;
import com.thedemgel.ultratrader.conversation.FixedIgnoreCaseSetPrompt;
import com.thedemgel.ultratrader.conversation.admin.AdminMenuPrompt;
import com.thedemgel.ultratrader.inventory.ShopInventoryView;
import com.thedemgel.ultratrader.util.Permissions;

import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;


public class AdminSetLevelPrompt extends FixedIgnoreCaseSetPrompt {

	@Override
	protected Prompt acceptValidatedInput(ConversationContext context, String input) {
		return getValidatedPrompt(this);
	}

	@Override
	public String getPromptText(ConversationContext context) {
		ShopInventoryView view = (ShopInventoryView) context.getSessionData(ConversationHandler.CONVERSATION_SESSION_VIEW);
		Player player = (Player) context.getForWhom();

		if (view.getShop().getLevel() < LimitHandler.getMaxLevel(player) && player.hasPermission(Permissions.LEVEL_INCREASE)) {
			addOption(L.getString("conversation.admin.level.options.increase"), new AdminSetLevelIncreasePrompt());
		}
		if (view.getShop().getLevel() > 1 && player.hasPermission(Permissions.LEVEL_DECREASE)) {
			addOption(L.getString("conversation.admin.level.options.decrease"), new AdminSetLevelDecreasePrompt());
		}
		if (player.hasPermission(Permissions.LEVEL_SET)) {
			//addOption(L.getString("conversation.admin.level.options.set"), this);
		}

		addOption(L.getString("general.exit"), new AdminMenuPrompt());
		return L.getString("conversation.options") + ": " + this.formatFixedSet();
	}

}
