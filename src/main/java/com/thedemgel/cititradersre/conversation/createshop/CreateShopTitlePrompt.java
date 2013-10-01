package com.thedemgel.cititradersre.conversation.createshop;

import com.thedemgel.cititradersre.conversation.additem.*;
import com.thedemgel.cititradersre.CitiTrader;
import java.text.MessageFormat;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.inventory.ItemStack;

public class CreateShopTitlePrompt extends StringPrompt {

	@Override
	public Prompt acceptInput(ConversationContext context, String input) {
		return Prompt.END_OF_CONVERSATION;
	}

	@Override
	public String getPromptText(ConversationContext context) {
		return "test";
	}
}
