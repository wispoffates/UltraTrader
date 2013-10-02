package com.thedemgel.cititradersre.conversation.admin;

import com.thedemgel.cititradersre.CitiTrader;
import com.thedemgel.cititradersre.conversation.FixedIgnoreCaseSetPrompt;
import com.thedemgel.cititradersre.conversation.admin.bank.AdminBankPrompt;
import com.thedemgel.cititradersre.util.Permissions;
import com.thedemgel.cititradersre.wallet.WalletType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.FixedSetPrompt;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;

public class AdminBankMenuPrompt extends FixedIgnoreCaseSetPrompt {

	private ResourceBundle rb;

	public AdminBankMenuPrompt() {
		rb = CitiTrader.getResourceBundle();
	}

	@Override
	protected Prompt acceptValidatedInput(ConversationContext context, String input) {
		return getValidatedPrompt(new AdminBankMenuPrompt());
	}

	@Override
	public String getPromptText(ConversationContext context) {
		rb = CitiTrader.getResourceBundle();
		Player player = (Player) context.getForWhom();

		if (player.hasPermission(Permissions.WALLET_ADMIN)) {
			addOption(rb.getString("general.wallet.admin"), new AdminBankPrompt(WalletType.ADMIN));
		}
		if (player.hasPermission(Permissions.WALLET_PLAYER)) {
			addOption(rb.getString("general.wallet.player"), new AdminBankPrompt(WalletType.PLAYER));
		}
		if (player.hasPermission(Permissions.WALLET_BANK)) {
			addOption(rb.getString("general.wallet.bank"), new AdminBankPrompt(WalletType.BANK));
		}
		if (player.hasPermission(Permissions.WALLET_SHOP)) {
			addOption(rb.getString("general.wallet.shop"), new AdminBankPrompt(WalletType.SHOP));
		}
		
		addOption(rb.getString("conversation.admin.menu.options.exit"), new AdminMenuPrompt());

		return CitiTrader.getResourceBundle().getString("conversation.admin.bankmenu") + "\nOptions: " + this.formatFixedSet();
	}
}
