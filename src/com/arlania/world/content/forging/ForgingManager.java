package com.arlania.world.content.forging;

import com.arlania.model.Item;
import com.arlania.model.definitions.ItemDefinition;
import com.arlania.world.World;
import com.arlania.world.content.Achievements;
import com.arlania.world.content.Achievements.AchievementData;
import com.arlania.world.entity.impl.player.Player;

public class ForgingManager {

    private Player player;
    private ForgingItemsData currentItem;

    public ForgingManager(Player player) {
        this.player = player;
    }

    public void open() {
        resetResult();
        resetRequiredItems();
        sendPossibleItems();
        player.getPacketSender().sendInterface(ForgingConstants.INTERFACE_ID);
    }

    private void sendPossibleItems() {
        for (int i = 0; i < ForgingItemsData.values().length; i++) {
            ForgingItemsData item = ForgingItemsData.values()[i];

            player.getPacketSender().sendItemOnInterface(ForgingConstants.POSSIBLE_ITEMS_CONTAINER_ID, item.getProduct().getId(), i, 1);
        }
    }

    public void handlePossibleItemClick(int itemId) {
        if (player.getInterfaceId() != 34000) {
            return;
        }

        ForgingItemsData clickedItem = null;
        for (ForgingItemsData item : ForgingItemsData.values()) {
            if (item.getProduct().getId() == itemId) {
                clickedItem = item;
                break;
            }
        }

        if (clickedItem == null) {
            return;
        }

        currentItem = clickedItem;

        resetRequiredItems();

        Item[] requiredItems = clickedItem.getRequiredItems();
        for (int i = 0; i < requiredItems.length; i++) {
            Item item = requiredItems[i];
            player.getPacketSender().sendItemOnInterface(ForgingConstants.REQUIRED_ITEMS_CONTAINER_ID, item.getId(), i, item.getAmount());
        }

        player.getPacketSender().sendItemOnInterface(ForgingConstants.RESULT_ITEM_CONTAINER_ID, itemId, 0, 1);
    }

    public void forge() {
        if (currentItem == null) {
            player.sendMessage("You haven't selected an item to forge yet.");
            return;
        }

        Item[] requiredItems = currentItem.getRequiredItems();

        if (player.getInventory().containsWithAmount(requiredItems)) {
            for (Item item : requiredItems) {
                player.getInventory().delete(item);
            }

            player.getInventory().add(currentItem.getProduct());
			player.getAchievementTracker().progress(com.arlania.world.content.achievements.AchievementData.FORGE_A_ITEM, 1);
			player.getAchievementTracker().progress(com.arlania.world.content.achievements.AchievementData.FORGE_5_ITEMS, 1);

            World.sendMessage ("<shad=df7018>@blu@" + player.getUsername().toString() + " @bla@<shad=df7018>has successfully forged - @blu@" + ItemDefinition.forId(currentItem.getProduct().getId()).getName() + ".");
        } else {
            player.sendMessage("You don't have the required items to do this.");
        }
    }

    private void resetRequiredItems() {
        for (int i = 0; i < 60; i++) {
            player.getPacketSender().sendItemOnInterface(ForgingConstants.REQUIRED_ITEMS_CONTAINER_ID, -1, i, 0);
        }
    }

    private void resetResult() {
        player.getPacketSender().sendItemOnInterface(ForgingConstants.RESULT_ITEM_CONTAINER_ID, -1, 0, 0);

    }
}
