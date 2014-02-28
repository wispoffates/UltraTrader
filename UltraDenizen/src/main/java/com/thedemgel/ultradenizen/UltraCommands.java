package com.thedemgel.ultradenizen;

import com.thedemgel.ultratrader.citizens.TraderTrait;
import net.aufdemrand.denizen.exceptions.CommandExecutionException;
import net.aufdemrand.denizen.exceptions.InvalidArgumentsException;
import net.aufdemrand.denizen.objects.aH;
import net.aufdemrand.denizen.objects.dNPC;
import net.aufdemrand.denizen.scripts.ScriptEntry;
import net.aufdemrand.denizen.scripts.commands.AbstractCommand;
import net.aufdemrand.denizen.utilities.debugging.dB;

import java.math.BigDecimal;

public class UltraCommands extends AbstractCommand {

    private enum Action { SETSELLPRICE, SETBUYPRICE, SETNAME, OPEN, CLOSE }

    // Syntax ultratrader [setprice] [shop[<shopid>] [item:shop[<id>].item[<itemid>]] price
    // Syntax ultratrader [open/close] dNPC

    @Override
    public void execute(ScriptEntry scriptEntry) throws CommandExecutionException {
        Action action = (Action) scriptEntry.getObject("action");
        dShop shop = scriptEntry.hasObject("shop") ? (dShop) scriptEntry.getObject("shop") : null;
        dNPC npc = scriptEntry.hasObject("npc") ? (dNPC) scriptEntry.getObject("npc") : null;
        String itemId = scriptEntry.hasObject("itemid") ? (String) scriptEntry.getObject("itemid") : null;
        double price = scriptEntry.hasObject("price") ? (double) scriptEntry.getObject("price") : 0;
        String name = scriptEntry.hasObject("name") ? (String) scriptEntry.getObject("name") : null;

        // Catch the Errors
        switch (action) {
            case SETSELLPRICE:
            case SETBUYPRICE:
                if (shop == null) {
                    dB.echoError("Shop is required to set a price.");
                    return;
                }
                if (itemId == null) {
                    dB.echoError("Item is required to set a price.");
                    return;
                }
                break;
            case OPEN:
            case CLOSE:
                if (npc == null) {
                    dB.echoError("NPC is required to open or close a shop.");
                    return;
                }

                if (!npc.getCitizen().hasTrait(TraderTrait.class)) {
                    dB.echoError("NPC needs to have the TraderTrait to open or close.");
                    return;
                }
                break;
            case SETNAME:
                if (shop == null) {
                    dB.echoError("Shop is required to set a name.");
                    return;
                }

                if (name == null) {
                    dB.echoError("A String is required to set a name.");
                    return;
                }
                break;
        }
        // Do the Actions
        switch (action) {
            case SETSELLPRICE:
                if (!shop.getShop().getPriceList().containsKey(itemId)) {
                    dB.echoError("Shop does not contain item " + itemId);
                    return;
                }
                shop.getShop().getPriceList().get(itemId).setSellPrice(BigDecimal.valueOf(price));
                break;
            case SETBUYPRICE:
                if (!shop.getShop().getPriceList().containsKey(itemId)) {
                    dB.echoError("Shop does not contain item " + itemId);
                    return;
                }
                shop.getShop().getPriceList().get(itemId).setBuyPrice(BigDecimal.valueOf(price));
                break;
            case OPEN:
                TraderTrait traito = npc.getCitizen().getTrait(TraderTrait.class);
                traito.setOpen(true);
                break;
            case CLOSE:
                TraderTrait traitc = npc.getCitizen().getTrait(TraderTrait.class);
                traitc.setOpen(false);
                break;
            case SETNAME:
                shop.getShop().setName(name);
                break;
        }
    }

    @Override
    public void parseArgs(ScriptEntry scriptEntry) throws InvalidArgumentsException {
        // Iterate through arguments
        for (aH.Argument arg : aH.interpret(scriptEntry.getArguments())) {
            if (!scriptEntry.hasObject("action") && arg.matchesEnum(Action.values())) {
                scriptEntry.addObject("action", Action.valueOf(arg.getValue()));
            } else if (!scriptEntry.hasObject("shop") && arg.matchesArgumentType(dShop.class)) {
                scriptEntry.addObject("shop", dShop.valueOf(arg.toString()));
            } else if (!scriptEntry.hasObject("npc") && arg.matchesArgumentType(dNPC.class)) {
                scriptEntry.addObject("npc", dNPC.valueOf(arg.toString()));
            } else if (!scriptEntry.hasObject("itemid") && arg.getPrefix().matches("item")) {
                scriptEntry.addObject("itemid", arg.getValue());
            } else if (!scriptEntry.hasObject("price") && arg.matchesPrimitive(aH.PrimitiveType.Double)) {
                scriptEntry.addObject("price", Double.valueOf(arg.getValue()));
            }
        }
    }
}
