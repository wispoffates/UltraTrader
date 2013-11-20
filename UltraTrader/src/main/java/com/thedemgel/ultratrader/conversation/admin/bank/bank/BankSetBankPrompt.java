
package com.thedemgel.ultratrader.conversation.admin.bank.bank;

import com.thedemgel.ultratrader.L;
import com.thedemgel.ultratrader.UltraTrader;
import com.thedemgel.ultratrader.conversation.ConversationHandler;
import com.thedemgel.ultratrader.conversation.admin.bank.AdminBankMenuPrompt;
import com.thedemgel.ultratrader.inventory.ShopInventoryView;
import com.thedemgel.ultratrader.util.ConfigValue;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.economy.EconomyResponse.ResponseType;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;


public class BankSetBankPrompt extends StringPrompt {

	@Override
	public String getPromptText(ConversationContext context) {
		return L.getString("conversation.admin.wallet.bank.choosebank");
	}

	@Override
	public Prompt acceptInput(ConversationContext context, String input) {
		Economy econ = UltraTrader.getEconomy();
		Player player = (Player) context.getForWhom();

		EconomyResponse resp = econ.isBankMember(input, player.getName());

		String type = (String) context.getSessionData("wallettype");

		if (resp.type.equals(ResponseType.SUCCESS)) {
			ShopInventoryView view = (ShopInventoryView) context.getSessionData(ConversationHandler.CONVERSATION_SESSION_VIEW);
			view.getShop().getWallet().setInfo("bank", new ConfigValue(input));
			view.getShop().setWalletType(type);
		} else {
			player.sendRawMessage(ChatColor.RED + L.getString("conversation.admin.wallet.bank.noaccess"));
		}
		return (Prompt) context.getSessionData(ConversationHandler.CONVERSATION_SESSION_RETURN);
	}
}
