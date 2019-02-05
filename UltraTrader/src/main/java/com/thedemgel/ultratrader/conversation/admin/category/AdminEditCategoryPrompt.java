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

public class AdminEditCategoryPrompt extends FixedIgnoreCaseSetPrompt {

    private ConversationPrefix prefix;

    public AdminEditCategoryPrompt() {
        prefix = new AdminConversationPrefix();
    }

    @Override
    public String getPromptText(ConversationContext conversationContext) {
        Player p = (Player) conversationContext.getForWhom();

        addOption("select", new AdminListCategoryPrompt(this));

        CategoryItem item = null;
        if (conversationContext.getSessionData(AdminListCategoryPrompt.SESSION_LIST_SELECTION) != null) {
            item = (CategoryItem) conversationContext.getSessionData(AdminListCategoryPrompt.SESSION_LIST_SELECTION);
            addOption("description", new AdminCategoryDescriptionPrompt());
            addOption("display", new AdminCategoryItemDisplayNamePrompt());
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
                p.sendRawMessage(prefix.getPrefix(conversationContext) + line);
            }
        }

        return Lang.getString("conversation.options") + ": " + this.formatFixedSet();
    }

    @Override
    protected Prompt acceptValidatedInput(ConversationContext conversationContext, String s) {
        return getValidatedPrompt(this);
    }
}
