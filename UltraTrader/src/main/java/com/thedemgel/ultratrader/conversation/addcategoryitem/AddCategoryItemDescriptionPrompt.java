package com.thedemgel.ultratrader.conversation.addcategoryitem;

import com.thedemgel.ultratrader.L;
import com.thedemgel.ultratrader.conversation.ConversationHandler;
import com.thedemgel.ultratrader.shop.CategoryItem;
import org.apache.commons.lang.WordUtils;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class AddCategoryItemDescriptionPrompt extends StringPrompt {

	@Override
	public Prompt acceptInput(ConversationContext context, String input) {
        CategoryItem categoryItem = (CategoryItem) context.getSessionData(ConversationHandler.CONVERSATION_SESSION_CATEGORYITEM);
        if (!input.equalsIgnoreCase(L.getString("conversation.itemadmin.none"))) {
            String wrapped = WordUtils.wrap(input, 25, System.lineSeparator(), false);
            String[] broken = wrapped.split(System.lineSeparator());
            List<String> unbroken = new ArrayList<>();
            for (String string : broken) {
                unbroken.add(string);
            }
            categoryItem.setLore(unbroken);
        }
		return new AddCategoryItemFinishPrompt();
	}

	@Override
	public String getPromptText(ConversationContext context) {
		ItemStack item = (ItemStack) context.getSessionData(ConversationHandler.CONVERSATION_SESSION_ITEM);
		return L.getFormatString("conversation.addbuyitem.setdescription", item.getType().name(), L.getString("conversation.addbuyitem.none"));
	}
}
