
package com.thedemgel.ultratrader.conversation.admin.bank.player;

import com.thedemgel.ultratrader.UltraTrader;
import com.thedemgel.ultratrader.conversation.ConversationHandler;
import com.thedemgel.ultratrader.inventory.ShopInventoryView;
import com.thedemgel.ultratrader.util.ConfigValue;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.PlayerNamePrompt;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;


public class PlayerWalletAnotherPrompt extends PlayerNamePrompt {

	public PlayerWalletAnotherPrompt() {
		super(UltraTrader.getInstance());
	}

	@Override
	protected Prompt acceptValidatedInput(ConversationContext cc, Player player) {
		Player p = (Player) cc.getForWhom();
		ShopInventoryView view = (ShopInventoryView) cc.getSessionData(ConversationHandler.CONVERSATION_SESSION_VIEW);
		view.getShop().getWallet().setInfo("player", new ConfigValue<String>(p.getName()));
		String type = (String) cc.getSessionData("wallettype");
		view.getShop().setWalletType(type);

		return (Prompt) cc.getSessionData(ConversationHandler.CONVERSATION_SESSION_RETURN);
	}

	@Override
	public String getPromptText(ConversationContext cc) {
		return "What Player would you like to set bank to?";
	}

}
