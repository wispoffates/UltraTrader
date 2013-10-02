package com.thedemgel.cititradersre.conversation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.FixedSetPrompt;
import org.bukkit.conversations.Prompt;

public abstract class FixedIgnoreCaseSetPrompt extends FixedSetPrompt {

	protected Map<String, Prompt> prompts = new HashMap<>();
	protected String fixedInput = "";

	public FixedIgnoreCaseSetPrompt() {
		fixedSet = new ArrayList<>();
	}

	public final Prompt getValidatedPrompt(Prompt defaultPrompt) {
		if (prompts.containsKey(fixedInput)) {
			return prompts.get(fixedInput);
		}

		return defaultPrompt;
	}

	protected final void addOption(String option, Prompt prompt) {
		fixedSet.add(option);
		prompts.put(option, prompt);
	}

	@Override
	public boolean isInputValid(ConversationContext context, String input) {
		for (String s : fixedSet) {
			if (input.equalsIgnoreCase(s)) {
				fixedInput = s;
				return true;
			}
		}
		return false;
	}

	/**
	 * Utility function to create a formatted string containing all the
	 * options declared in the constructor.
	 *
	 * @return the options formatted like "[bar, cheese, panda]" if bar,
	 * cheese, and panda were the options used
	 */
	@Override
	protected String formatFixedSet() {
		return "[" + ChatColor.WHITE + StringUtils.join(fixedSet, ChatColor.YELLOW + ", " + ChatColor.WHITE) + ChatColor.YELLOW + "]";
	}
}
