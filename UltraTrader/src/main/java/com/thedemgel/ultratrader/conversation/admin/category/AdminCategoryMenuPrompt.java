package com.thedemgel.ultratrader.conversation.admin.category;

import com.thedemgel.ultratrader.L;
import com.thedemgel.ultratrader.conversation.ConversationHandler;
import com.thedemgel.ultratrader.conversation.FixedIgnoreCaseSetPrompt;
import com.thedemgel.ultratrader.conversation.admin.AdminConversationPrefix;
import com.thedemgel.ultratrader.conversation.admin.AdminMenuPrompt;
import com.thedemgel.ultratrader.inventory.ShopInventoryView;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationPrefix;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;

public class AdminCategoryMenuPrompt extends FixedIgnoreCaseSetPrompt {
    private ConversationPrefix prefix;

    public AdminCategoryMenuPrompt() {
        prefix = new AdminConversationPrefix();
    }

    @Override
    protected Prompt acceptValidatedInput(ConversationContext conversationContext, String s) {
        return getValidatedPrompt(this);
    }

    @Override
    public String getPromptText(ConversationContext conversationContext) {
        Player p = (Player) conversationContext.getForWhom();
        ShopInventoryView view = (ShopInventoryView) conversationContext.getSessionData(ConversationHandler.CONVERSATION_SESSION_VIEW);

        // TODO: fix category deletion
        addOption("delete", new AdminDeleteCategoryPrompt());
        addOption("edit", new AdminEditCategoryPrompt());
        addOption(L.getString("general.exit"), new AdminMenuPrompt());

        p.sendRawMessage(prefix.getPrefix(conversationContext) + "------<[ " + ChatColor.BLUE + "CATEGORY" + ChatColor.YELLOW + " ]>------");

        return L.getString("conversation.options") + ": " + this.formatFixedSet();
    }
}
