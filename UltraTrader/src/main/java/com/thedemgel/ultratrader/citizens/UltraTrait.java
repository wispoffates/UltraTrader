
package com.thedemgel.ultratrader.citizens;

import net.citizensnpcs.api.trait.Trait;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;


public abstract class UltraTrait extends Trait {
	private String menuOption = "optionnotset";

	public UltraTrait(String traitname) {
		super(traitname);
	}

	public boolean onClick(Player player) {
		return true;
	}

	public boolean onAssign(Player player) {
		return true;
	}

	public boolean onAssign() {
		return true;
	}

	public abstract boolean hasMenuOption();

	public String getMenuOption() {
		return menuOption;
	}

	public void setMenuOption(String value) {
		menuOption = value;
	}

	public Prompt getMenuPrompt() {
		return Prompt.END_OF_CONVERSATION;
	}
}
