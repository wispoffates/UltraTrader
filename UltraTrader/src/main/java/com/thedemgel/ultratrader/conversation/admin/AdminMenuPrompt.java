package com.thedemgel.ultratrader.conversation.admin;

import com.thedemgel.ultratrader.Lang;
import com.thedemgel.ultratrader.LimitHandler;
import com.thedemgel.ultratrader.UltraTrader;
import com.thedemgel.ultratrader.citizens.UltraTrait;
import com.thedemgel.ultratrader.conversation.ConversationHandler;
import com.thedemgel.ultratrader.conversation.FixedIgnoreCaseSetPrompt;
import com.thedemgel.ultratrader.conversation.admin.bank.AdminBankMenuPrompt;
import com.thedemgel.ultratrader.conversation.admin.category.AdminCategoryMenuPrompt;
import com.thedemgel.ultratrader.conversation.admin.level.AdminSetLevelPrompt;
//import com.thedemgel.ultratrader.conversation.rentalshop.RentalEndRentingPrompt;
import com.thedemgel.ultratrader.inventory.ShopInventoryView;
import com.thedemgel.ultratrader.util.Permissions;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.Trait;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationPrefix;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;

public class AdminMenuPrompt extends FixedIgnoreCaseSetPrompt {

	private ConversationPrefix prefix;

	public AdminMenuPrompt() {
		prefix = new AdminConversationPrefix();
	}

	@Override
	protected Prompt acceptValidatedInput(ConversationContext context, String input) {
		return getValidatedPrompt(this);
	}

	@Override
	public String getPromptText(ConversationContext context) {
		Player p = (Player) context.getForWhom();
		ShopInventoryView view = (ShopInventoryView) context.getSessionData(ConversationHandler.CONVERSATION_SESSION_VIEW);

		addOption(Lang.getString("conversation.admin.menu.options.storename"), new AdminSetNamePrompt());
		addOption(Lang.getString("conversation.admin.menu.options.bank"), new AdminBankMenuPrompt());
		addOption(Lang.getString("conversation.admin.menu.options.inventoryinterface"), new AdminInventoryInterfaceMenuPrompt());
        // TODO: add language
        addOption("category", new AdminCategoryMenuPrompt());
		if (p.hasPermission(Permissions.LEVEL_TRANSFER)) {
			addOption(Lang.getString("conversation.admin.menu.options.transfer"), new AdminTransferPrompt());
		}
		addOption(Lang.getString("conversation.admin.menu.options.level"), new AdminSetLevelPrompt());
		addOption("remote", new AdminRemotePrompt());
		if (UltraTrader.getInstance().isCitizens()) {
			if (view.getTarget() instanceof NPC) {
				NPC npc = (NPC) view.getTarget();
				for (Trait trait: npc.getTraits()) {
					if (trait instanceof UltraTrait) {
						UltraTrait ultratrait = (UltraTrait) trait;
						if (ultratrait.hasMenuOption()) {
							addOption(ultratrait.getMenuOption(), ultratrait.getMenuPrompt());
						}
					}
				}
			}
		}
		addOption(Lang.getString("general.exit"), new AdminFinishPrompt());

		p.sendRawMessage(prefix.getPrefix(context) + "------<[ " + ChatColor.BLUE + "ADMIN" + ChatColor.YELLOW + " ]>------");
		p.sendRawMessage(prefix.getPrefix(context) + Lang.getString("general.name") + ": " + ChatColor.WHITE + view.getShop().getName());
		p.sendRawMessage(prefix.getPrefix(context) + Lang.getString("general.bank") + ": " + ChatColor.WHITE + view.getShop().getWalletType());
		p.sendRawMessage(prefix.getPrefix(context) + Lang.getString("general.inventory") + ": " + ChatColor.WHITE + view.getShop().getInventoryInterfaceType());
        // TODO: add language
		p.sendRawMessage(prefix.getPrefix(context) + "Level: " + ChatColor.WHITE + view.getShop().getLevel() + ChatColor.YELLOW + " Shops: " + ChatColor.WHITE + UltraTrader.getStoreHandler().getShopsByOwner(p).size() + ChatColor.YELLOW + "/" + ChatColor.WHITE + LimitHandler.getMaxShops(p) + ChatColor.YELLOW + " MaxBuySell: " + ChatColor.WHITE + LimitHandler.getMaxBuySellSize(view.getShop()));
		p.sendRawMessage(prefix.getPrefix(context) + Lang.getString("conversation.admin.menutext"));
		return Lang.getString("conversation.options") + ": " + this.formatFixedSet();
	}
}
