package com.thedemgel.ultratrader.conversation.admin.category;

import com.thedemgel.ultratrader.L;
import com.thedemgel.ultratrader.shop.CategoryItem;
import org.apache.commons.lang.WordUtils;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;

import java.util.ArrayList;
import java.util.List;

public class AdminCategoryDescriptionPrompt extends StringPrompt {

	@Override
	public Prompt acceptInput(ConversationContext context, String input) {
		CategoryItem categoryItem = (CategoryItem) context.getSessionData(AdminListCategoryPrompt.SESSION_LIST_SELECTION);
		if (!input.equalsIgnoreCase(L.getString("conversation.itemadmin.none"))) {
            String wrapped = WordUtils.wrap(input, 25, System.lineSeparator(), false);
            String[] broken = wrapped.split(System.lineSeparator());
            List<String> unbroken = new ArrayList<>();
            for (String string : broken) {
                unbroken.add(string);
            }
			categoryItem.setLore(unbroken);
		}
		return new AdminEditCategoryPrompt();
	}

	@Override
	public String getPromptText(ConversationContext context) {
		CategoryItem item = (CategoryItem) context.getSessionData(AdminListCategoryPrompt.SESSION_LIST_SELECTION);
		return "Set description: (" + L.getString("conversation.itemadmin.none") + ")";
	}
}
