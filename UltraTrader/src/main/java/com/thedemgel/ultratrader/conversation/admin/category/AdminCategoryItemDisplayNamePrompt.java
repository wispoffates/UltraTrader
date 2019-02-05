package com.thedemgel.ultratrader.conversation.admin.category;

import com.thedemgel.ultratrader.L;
import com.thedemgel.ultratrader.shop.CategoryItem;

import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;

public class AdminCategoryItemDisplayNamePrompt extends StringPrompt {

	@Override
	public Prompt acceptInput(ConversationContext context, String input) {
		CategoryItem categoryItem = (CategoryItem) context.getSessionData(AdminListCategoryPrompt.SESSION_LIST_SELECTION);
		if (!input.equalsIgnoreCase(L.getString("conversation.itemadmin.none"))) {
			categoryItem.setDisplayName(input);
		}
		return new AdminEditCategoryPrompt();
	}

	@Override
	public String getPromptText(ConversationContext context) {
		CategoryItem item = (CategoryItem) context.getSessionData(AdminListCategoryPrompt.SESSION_LIST_SELECTION);
		return L.getFormatString("conversation.itemadmin.setdescription", item.getItemStack().getType().name(), L.getString("conversation.itemadmin.none"));
	}
}
