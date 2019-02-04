package com.thedemgel.ultratrader.conversation.admin.bank;

import java.math.BigDecimal;

import com.thedemgel.ultratrader.L;
import com.thedemgel.ultratrader.UltraTrader;
import com.thedemgel.ultratrader.conversation.ConversationHandler;
import com.thedemgel.ultratrader.conversation.NotADoublePrompt;
import com.thedemgel.ultratrader.inventory.ShopInventoryView;

import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.economy.EconomyResponse.ResponseType;

public class AdminBankDepositPrompt extends StringPrompt {

	@Override
	public Prompt acceptInput(ConversationContext context, String input) {

		Double deposit;
		try {
			deposit = Double.valueOf(input);
		} catch (NumberFormatException ex) {
			context.setSessionData(ConversationHandler.CONVERSATION_SESSION_RETURN, this);
			return new NotADoublePrompt();
		}
		Economy econ = UltraTrader.getEconomy();
		Player p = (Player) context.getForWhom();
		ShopInventoryView view = (ShopInventoryView) context.getSessionData(ConversationHandler.CONVERSATION_SESSION_VIEW);
		if (econ.has(p, deposit)) {
			EconomyResponse res = econ.withdrawPlayer(p, deposit);
			if (res.type.equals(ResponseType.SUCCESS)) {
				view.getShop().getWallet().addFunds(BigDecimal.valueOf(res.amount));
			}
		}
		return new AdminBankMenuPrompt();
	}

	@Override
	public String getPromptText(ConversationContext context) {
		return L.getString("conversation.admin.deposit");
	}
}
