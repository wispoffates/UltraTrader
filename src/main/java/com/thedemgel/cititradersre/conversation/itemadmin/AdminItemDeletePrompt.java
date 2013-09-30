
package com.thedemgel.cititradersre.conversation.itemadmin;

import com.thedemgel.cititradersre.conversation.admin.*;
import com.google.common.base.Strings;
import com.thedemgel.cititradersre.CitiTrader;
import com.thedemgel.cititradersre.conversation.FixedIgnoreCaseSetPrompt;
import com.thedemgel.cititradersre.shop.ItemPrice;
import com.thedemgel.cititradersre.util.ShopInventoryView;
import java.util.ArrayList;
import java.util.ResourceBundle;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationPrefix;
import org.bukkit.conversations.FixedSetPrompt;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;


public class AdminItemDeletePrompt extends FixedIgnoreCaseSetPrompt {
	private ResourceBundle rb;
	private ConversationPrefix prefix;
	
	public AdminItemDeletePrompt() {
		rb = CitiTrader.getResourceBundle();
		prefix = new AdminItemConversationPrefix();
		addOption(rb.getString("general.accept"), new AdminItemDeleteConfirmPrompt(true));
		addOption(rb.getString("general.decline"), new AdminItemDeleteConfirmPrompt(false));
	}

	@Override
	protected Prompt acceptValidatedInput(ConversationContext context, String input) {
		return getValidatedPrompt(new AdminItemMenuPrompt());
	}

	@Override
	public String getPromptText(ConversationContext context) {
		ItemPrice itemprice = (ItemPrice) context.getSessionData("itemprice");
		Player p = (Player) context.getForWhom();
		ShopInventoryView view = (ShopInventoryView) context.getSessionData("view");
		int invCount = 0;
		if (view.getShop().getInventoryInterface().hasItem(itemprice)) {
			invCount = view.getShop().getInventoryInterface().getInventoryAmount(itemprice.getItemStack());
		}
		
		if (invCount > 0) {
			p.sendRawMessage(prefix.getPrefix(context) + ChatColor.RED + rb.getString("conversation.itemadmin.delete.full"));
			p.sendRawMessage(prefix.getPrefix(context) + ChatColor.RED + rb.getString("conversation.itemadmin.delete.warn"));
		}
		
		p.sendRawMessage(prefix.getPrefix(context) + rb.getString("conversation.itemadmin.delete.confirm") + ": " + ChatColor.WHITE + itemprice.getItemStack().getType().name());
		return  rb.getString("conversation.options") + ": " + this.formatFixedSet();
	}

}
