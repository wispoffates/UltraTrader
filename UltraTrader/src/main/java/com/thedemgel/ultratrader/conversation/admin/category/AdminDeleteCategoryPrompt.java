package com.thedemgel.ultratrader.conversation.admin.category;

import com.thedemgel.ultratrader.Lang;
import com.thedemgel.ultratrader.conversation.FixedIgnoreCaseSetPrompt;
import com.thedemgel.ultratrader.conversation.admin.AdminConversationPrefix;
import com.thedemgel.ultratrader.shop.CategoryItem;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationPrefix;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;

public class AdminDeleteCategoryPrompt extends FixedIgnoreCaseSetPrompt {

    private ConversationPrefix prefix;

    public AdminDeleteCategoryPrompt() {
        prefix = new AdminConversationPrefix();
    }

    @Override
    public String getPromptText(ConversationContext conversationContext) {
        Player p = (Player) conversationContext.getForWhom();

        addOption("select", new AdminListCategoryPrompt(this));

        CategoryItem item = null;
        if (conversationContext.getSessionData(AdminListCategoryPrompt.SESSION_LIST_SELECTION) != null) {
            item = (CategoryItem) conversationContext.getSessionData(AdminListCategoryPrompt.SESSION_LIST_SELECTION);
            addOption(item.getCategoryId(), new AdminCategoryItemDeletedPrompt());
        }

        addOption(Lang.getString("general.exit"), new AdminCategoryMenuPrompt());

        if (item == null) {
            p.sendRawMessage(prefix.getPrefix(conversationContext) + "Select a Category (type select)");
        } else {
            // Display item information
            p.sendRawMessage(prefix.getPrefix(conversationContext) + "Category ID: " + ChatColor.WHITE + item.getCategoryId());
            p.sendRawMessage(prefix.getPrefix(conversationContext) + "Display Name: " + ChatColor.WHITE + item.getDisplayName());
            p.sendRawMessage(prefix.getPrefix(conversationContext) + "Description:");

            for (String line : item.getLore()) {
                p.sendRawMessage(prefix.getPrefix(conversationContext) + ChatColor.WHITE + line);
            }

            p.sendRawMessage(prefix.getPrefix(conversationContext) + ChatColor.RED + "This will delete all Items/Inventories in this Category!");
            p.sendRawMessage(prefix.getPrefix(conversationContext) + ChatColor.RED + "Type the name to delete Category.");
        }



        return Lang.getString("conversation.options") + ": " + this.formatFixedSet();
    }

    @Override
    protected Prompt acceptValidatedInput(ConversationContext conversationContext, String s) {
        return getValidatedPrompt(this);
    }
}
