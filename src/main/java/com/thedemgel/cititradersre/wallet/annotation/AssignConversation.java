
package com.thedemgel.cititradersre.wallet.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.bukkit.conversations.Prompt;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface AssignConversation {
	Class<? extends Prompt> value();
}
