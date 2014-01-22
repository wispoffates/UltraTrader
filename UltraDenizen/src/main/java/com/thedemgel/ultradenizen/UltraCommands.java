package com.thedemgel.ultradenizen;

import net.aufdemrand.denizen.exceptions.CommandExecutionException;
import net.aufdemrand.denizen.exceptions.InvalidArgumentsException;
import net.aufdemrand.denizen.objects.aH;
import net.aufdemrand.denizen.scripts.ScriptEntry;
import net.aufdemrand.denizen.scripts.commands.AbstractCommand;
import net.aufdemrand.denizen.utilities.debugging.dB;

import java.math.BigDecimal;

public class UltraCommands extends AbstractCommand {

    private enum Action { SETSELLPRICE, SETBUYPRICE }

    // Syntax ultratrader [setprice] [shop[<shopid>] [item:shop[<id>].item[<itemid>]] price

    @Override
    public void execute(ScriptEntry scriptEntry) throws CommandExecutionException {
        Action action = (Action) scriptEntry.getObject("action");
        dShop shop = (dShop) scriptEntry.getObject("shop");
        String itemid = scriptEntry.hasObject("itemid") ? (String) scriptEntry.getObject("itemid") : null;
        double price = scriptEntry.hasObject("price") ? (double) scriptEntry.getObject("price") : 0;

        switch (action) {
            case SETSELLPRICE:
                if (!shop.getShop().getPriceList().containsKey(itemid)) {
                    dB.echoError("Shop does not contain item " + itemid);
                    return;
                }
                shop.getShop().getPriceList().get(itemid).setSellPrice(BigDecimal.valueOf(price));
                break;
            case SETBUYPRICE:
                if (!shop.getShop().getPriceList().containsKey(itemid)) {
                    dB.echoError("Shop does not contain item " + itemid);
                    return;
                }
                shop.getShop().getPriceList().get(itemid).setBuyPrice(BigDecimal.valueOf(price));
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
            } else if (!scriptEntry.hasObject("itemid") && arg.getPrefix().matches("item")) {
                scriptEntry.addObject("itemid", arg.getValue());
            } else if (!scriptEntry.hasObject("price") && arg.matchesPrimitive(aH.PrimitiveType.Double)) {
                scriptEntry.addObject("price", Double.valueOf(arg.getValue()));
            }
        }
    }
}
