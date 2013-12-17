package com.thedemgel.ultratrader.conversation.createshop;

import com.thedemgel.ultratrader.BlockShopHandler;
import com.thedemgel.ultratrader.L;
import com.thedemgel.ultratrader.UltraTrader;
import com.thedemgel.ultratrader.citizens.TraderTrait;
import com.thedemgel.ultratrader.conversation.ConversationHandler;
import com.thedemgel.ultratrader.conversation.FixedIgnoreCaseSetPrompt;
import com.thedemgel.ultratrader.shop.Shop;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationPrefix;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;

public class CreateShopAssignPrompt extends FixedIgnoreCaseSetPrompt {
	private ConversationPrefix prefix;

	public CreateShopAssignPrompt() {
		prefix = new CreateShopConversationPrefix();
	}

	@Override
	public String getPromptText(ConversationContext context) {
		fixedSet = new ArrayList<>();
		Player p = (Player) context.getForWhom();

		Collection<Shop> shops = UltraTrader.getStoreHandler().getShopsByOwner(p);
		List<Shop> shoplist = new ArrayList<>();
		shoplist.addAll(shops);
		Collections.sort(shoplist, new ShopComparator());

		for (Shop shop : shoplist) {
			p.sendRawMessage(prefix.getPrefix(context) + "[" + ChatColor.WHITE + shop.getId() + ChatColor.YELLOW + "]" + " " + shop.getName());
			addOption("" + shop.getId(), this);
		}

		addOption(L.getString("conversation.admin.menu.options.exit"), new CreateShopMenuPrompt());

		return L.getString("conversation.options") + ": " + this.formatFixedSet();
	}

	@Override
	protected Prompt acceptValidatedInput(ConversationContext context, String input) {
		Player p = (Player) context.getForWhom();
		try {
			Shop shop = UltraTrader.getStoreHandler().getShop(Integer.valueOf(input));
			if (shop != null) {
                boolean isBlock = (boolean) context.getSessionData(ConversationHandler.CONVERSATION_SESSION_IS_BLOCK);

                if (isBlock) {
                    Block block = (Block) context.getSessionData(ConversationHandler.CONVERSATION_SESSION_BLOCK);
                    BlockShopHandler.assignShop(p, shop, block);
                } else if (UltraTrader.getInstance().isCitizens()) {
				    NPC npc = (NPC) context.getSessionData(ConversationHandler.CONVERSATION_SESSION_NPC);
				    if (npc.hasTrait(TraderTrait.class) && shop.isOwner(p)) {
					    TraderTrait trait = npc.getTrait(TraderTrait.class);
					    trait.setShopId(Integer.valueOf(input));
                    }
                } else {
                    // TODO: language addition
                    p.sendRawMessage(prefix.getPrefix(context) + ChatColor.RED + "Attempting to assign to NPC without citizens.");
                    return Prompt.END_OF_CONVERSATION;
                }
                // TODO: language addition
                p.sendRawMessage(prefix.getPrefix(context) + "Shop (" + shop.getId() + ") assigned.");
				return Prompt.END_OF_CONVERSATION;
			}
		} catch (Exception e) {}
		return getValidatedPrompt(this);
	}

	private class ShopComparator implements Comparator<Shop> {

		@Override
		public int compare(Shop o1, Shop o2) {
			return o1.getId().compareTo(o2.getId());
		}

	}
}
