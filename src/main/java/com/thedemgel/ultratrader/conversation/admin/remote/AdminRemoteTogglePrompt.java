package com.thedemgel.ultratrader.conversation.admin.remote;

import com.thedemgel.ultratrader.LimitHandler;
import com.thedemgel.ultratrader.UltraTrader;
import com.thedemgel.ultratrader.conversation.ConversationHandler;
import com.thedemgel.ultratrader.conversation.admin.AdminConversationPrefix;
import com.thedemgel.ultratrader.conversation.admin.AdminRemotePrompt;
import com.thedemgel.ultratrader.shop.ShopInventoryView;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.economy.EconomyResponse.ResponseType;
import org.bukkit.conversations.BooleanPrompt;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationPrefix;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;

public class AdminRemoteTogglePrompt extends BooleanPrompt {

	private ShopInventoryView view;
	private Player p;
	private double cost;
	private boolean arg;
	private ConversationPrefix prefix;

	public AdminRemoteTogglePrompt(boolean arg) {
		prefix = new AdminConversationPrefix();
		this.arg = arg;
	}

	@Override
	protected Prompt acceptValidatedInput(ConversationContext cc, boolean bln) {
		if (bln) {
			if (arg) {
				view.getShop().setCanRemote(false);
				p.sendRawMessage(prefix.getPrefix(cc) + "Shop remove access disabled.");
			} else {
				// Charge the player (will later)
				if (LimitHandler.canEnableRemoteAccess(p)) {
					EconomyResponse resp = UltraTrader.getEconomy().withdrawPlayer(p.getName(), p.getWorld().getName(), cost);
					if (resp.type.equals(ResponseType.SUCCESS)) {
						p.sendRawMessage(prefix.getPrefix(cc) + "Shop remote access enabled.");
						view.getShop().setCanRemote(true);
					} else {
						p.sendRawMessage(prefix.getPrefix(cc) + "Insuffient Funds.");
					}
				} else {
					p.sendRawMessage(prefix.getPrefix(cc) + "You can't enable remote access.");
				}
			}
		}

		return new AdminRemotePrompt();
	}

	@Override
	public String getPromptText(ConversationContext cc) {
		view = (ShopInventoryView) cc.getSessionData(ConversationHandler.CONVERSATION_SESSION_VIEW);
		p = (Player) cc.getForWhom();

		if (arg) {
			return "Disable Remote Item access?";
		} else {
			cost = LimitHandler.getRemoteActivateCost(p);
			return "Enable Remote Item access for " + UltraTrader.getEconomy().format(cost) + "?";
		}
	}
}
