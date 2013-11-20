package com.thedemgel.ultratrader.conversation.admin.remote;

import com.thedemgel.ultratrader.L;
import com.thedemgel.ultratrader.LimitHandler;
import com.thedemgel.ultratrader.UltraTrader;
import com.thedemgel.ultratrader.conversation.ConversationHandler;
import com.thedemgel.ultratrader.conversation.admin.AdminConversationPrefix;
import com.thedemgel.ultratrader.conversation.admin.AdminRemotePrompt;
import com.thedemgel.ultratrader.inventory.ShopInventoryView;
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
				p.sendRawMessage(prefix.getPrefix(cc) + L.getString("conversation.admin.remote.disable"));
			} else {
				// Charge the player (will later)
				if (LimitHandler.canEnableRemoteAccess(p)) {
					EconomyResponse resp = UltraTrader.getEconomy().withdrawPlayer(p.getName(), p.getWorld().getName(), cost);
					if (resp.type.equals(ResponseType.SUCCESS)) {
						p.sendRawMessage(prefix.getPrefix(cc) + L.getString("conversation.admin.remote.enable"));
						view.getShop().setCanRemote(true);
					} else {
						p.sendRawMessage(prefix.getPrefix(cc) + L.getString("conversation.admin.remote.nofunds"));
					}
				} else {
					p.sendRawMessage(prefix.getPrefix(cc) + L.getString("conversation.admin.remote.deny"));
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
			return L.getString("conversation.admin.remote.prompt.disable");
		} else {
			cost = LimitHandler.getRemoteActivateCost(p);
			return L.getFormatString("conversation.admin.remote.prompt.enable", UltraTrader.getEconomy().format(cost));
		}
	}
}
