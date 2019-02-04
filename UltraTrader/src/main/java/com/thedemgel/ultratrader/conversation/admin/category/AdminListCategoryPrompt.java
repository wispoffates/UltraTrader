package com.thedemgel.ultratrader.conversation.admin.category;

import com.thedemgel.ultratrader.conversation.ConversationHandler;
import com.thedemgel.ultratrader.conversation.admin.AdminConversationPrefix;
import com.thedemgel.ultratrader.inventory.ShopInventoryView;
import com.thedemgel.ultratrader.shop.CategoryItem;
import com.thedemgel.ultratrader.shop.Shop;

import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationPrefix;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;

public class AdminListCategoryPrompt extends StringPrompt {
    public static final String SESSION_LIST_SELECTION = "categoryListSelection";

    private Prompt parent;
    private ConversationPrefix prefix;
    private ShopInventoryView view;

    public AdminListCategoryPrompt(Prompt prompt) {
        parent = prompt;
        prefix = new AdminConversationPrefix();
    }

    @Override
    public Prompt acceptInput(ConversationContext context, String input) {
        if (view.getShop().getCategoryItem().containsKey(input)) {
            CategoryItem item = view.getShop().getCategoryItem().get(input);
            context.setSessionData(SESSION_LIST_SELECTION, item);
            return parent;
        }
        return this;
    }

    @Override
    public String getPromptText(ConversationContext conversationContext) {
        Player p = (Player) conversationContext.getForWhom();
        view = (ShopInventoryView) conversationContext.getSessionData(ConversationHandler.CONVERSATION_SESSION_VIEW);
        Shop shop = view.getShop();

        for (CategoryItem item : shop.getCategoryItem().values()) {
            // [prefix] (id) displayName
            p.sendRawMessage(prefix.getPrefix(conversationContext) + "(" + ChatColor.WHITE + item.getCategoryId() + ChatColor.YELLOW + ")" + " " + ChatColor.WHITE + item.getCategoryId());
        }

        // TODO: language addition
        return "Select from available Categories:";
    }
}
