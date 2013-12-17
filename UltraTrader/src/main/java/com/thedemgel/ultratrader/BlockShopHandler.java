package com.thedemgel.ultratrader;

import com.thedemgel.ultratrader.conversation.ConversationHandler;
import com.thedemgel.ultratrader.shop.Shop;
import com.thedemgel.ultratrader.shop.ShopHandler;
import com.thedemgel.ultratrader.util.Permissions;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.conversations.Conversation;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

public class BlockShopHandler {
    public static final String SHOP_METADATA_KEY = "ultrashop";
    public static final String SHOP_OWNER_KEY = "ultraowner";

    public static boolean createBlockShop(Player player, Block block) {
        if (!player.hasPermission(Permissions.BLOCK_CREATE)) {
            // TODO: language addition
            player.sendRawMessage(ChatColor.RED + "You don't have permission to do this.");
            return false;
        }

        // Setting initial Block metadata...
        block.setMetadata(SHOP_METADATA_KEY, new FixedMetadataValue(UltraTrader.getInstance(), -1));
        block.setMetadata(SHOP_OWNER_KEY, new FixedMetadataValue(UltraTrader.getInstance(), player.getName()));

        // Testing- setting to shop 4 for test
        //block.setMetadata(SHOP_METADATA_KEY, new FixedMetadataValue(UltraTrader.getInstance(), 4));
        return true;
    }

    public static boolean assignShop(Player player, Shop shop, Block block) {
        // TODO: check owner/permissions/limits
        // Check if shop already exists (if it does, remove it from the shop list)
        int shopId = block.getMetadata(SHOP_METADATA_KEY).get(0).asInt();

        if (shopId != ShopHandler.SHOP_NULL) {
            Shop oldShop = UltraTrader.getStoreHandler().getShop(shopId);
            if (oldShop != null) {
                oldShop.getBlockShops().remove(block.getLocation());
            }
        }
        block.setMetadata(SHOP_METADATA_KEY, new FixedMetadataValue(UltraTrader.getInstance(), shop.getId()));
        shop.getBlockShops().add(block.getLocation());
        return true;
    }

    public static boolean assignShopConvo(Player player, Block block) {
        // TODO: check owner/permissions
        String owner = getBlockOwner(block);

        if (!owner.equals(player.getName()) && !player.isOp()) {
            return false;
        }

        if (!player.isConversing()) {
            Conversation convo = UltraTrader.getConversationHandler().getCreateShop().buildConversation(player);
            convo.getContext().setSessionData(ConversationHandler.CONVERSATION_SESSION_BLOCK, block);
            convo.getContext().setSessionData(ConversationHandler.CONVERSATION_SESSION_IS_BLOCK, true);
            convo.begin();
            return true;
        } else {
            player.sendRawMessage(ChatColor.RED + L.getString("conversation.error.inconvo"));
            return true;
        }
    }

    public static String getBlockOwner(Block block) {
        if (block.hasMetadata(BlockShopHandler.SHOP_OWNER_KEY)) {
            return block.getMetadata(BlockShopHandler.SHOP_OWNER_KEY).get(0).asString();
        }

        return null;
    }
}
