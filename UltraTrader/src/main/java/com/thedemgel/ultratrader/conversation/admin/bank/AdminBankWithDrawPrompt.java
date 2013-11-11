package com.thedemgel.ultratrader.conversation.admin.bank;

import com.thedemgel.ultratrader.L;
import com.thedemgel.ultratrader.UltraTrader;
import com.thedemgel.ultratrader.conversation.ConversationHandler;
import com.thedemgel.ultratrader.conversation.NotADoublePrompt;
import com.thedemgel.ultratrader.shop.ShopInventoryView;
import java.math.BigDecimal;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;

public class AdminBankWithDrawPrompt extends StringPrompt {

	@Override
	public Prompt acceptInput(ConversationContext context, String input) {

		Double withdraw;
		try {
			withdraw = Double.valueOf(input);
		} catch (NumberFormatException ex) {
			context.setSessionData(ConversationHandler.CONVERSATION_SESSION_RETURN, this);
			return new NotADoublePrompt();
		}
		Economy econ = UltraTrader.getEconomy();
		Player p = (Player) context.getForWhom();
		ShopInventoryView view = (ShopInventoryView) context.getSessionData(ConversationHandler.CONVERSATION_SESSION_VIEW);
		if (view.getShop().getWallet().hasFunds(BigDecimal.valueOf(withdraw))) {
			EconomyResponse res = view.getShop().getWallet().removeFunds(BigDecimal.valueOf(withdraw));
			if (res.type.equals(EconomyResponse.ResponseType.SUCCESS)) {
				econ.depositPlayer(p.getName(), p.getWorld().getName(), res.amount);
			}
		}
		return new AdminBankMenuPrompt();
	}

	@Override
	public String getPromptText(ConversationContext context) {
		return L.getString("conversation.admin.withdraw");
	}
}
