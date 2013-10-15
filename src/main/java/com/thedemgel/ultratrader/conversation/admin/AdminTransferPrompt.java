
package com.thedemgel.ultratrader.conversation.admin;

import com.thedemgel.ultratrader.L;
import com.thedemgel.ultratrader.LimitHandler;
import com.thedemgel.ultratrader.UltraTrader;
import com.thedemgel.ultratrader.conversation.ConversationHandler;
import com.thedemgel.ultratrader.shop.ShopInventoryView;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationPrefix;
import org.bukkit.conversations.PlayerNamePrompt;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;


public class AdminTransferPrompt extends PlayerNamePrompt {
	private ConversationPrefix prefix;

	public AdminTransferPrompt() {
		super(UltraTrader.getInstance());
		prefix = new AdminConversationPrefix();
	}

	@Override
	protected Prompt acceptValidatedInput(ConversationContext context, Player input) {
		if (LimitHandler.canCreate(input)) {
			ShopInventoryView view = (ShopInventoryView) context.getSessionData(ConversationHandler.CONVERSATION_SESSION_VIEW);
			view.getShop().setOwner(input);
			context.getForWhom().sendRawMessage(prefix.getPrefix(context) + L.getString("conversation.admin.transfer.transfered"));
			return new AdminFinishPrompt();
		} else {
			context.getForWhom().sendRawMessage(prefix.getPrefix(context) + L.getString("conversation.admin.transfer.playercantaccept"));
			return this;
		}
	}

	@Override
	public String getPromptText(ConversationContext context) {
		Player p = (Player) context.getForWhom();

		p.sendRawMessage(prefix.getPrefix(context) + L.getString("conversation.admin.transfer.towhom"));
		return L.getString("conversation.admin.transfer.needstobeonline");
	}

}
