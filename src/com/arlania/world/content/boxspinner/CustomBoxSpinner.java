package com.arlania.world.content.boxspinner;

import com.arlania.model.Item;
import com.arlania.model.definitions.ItemDefinition;
import com.arlania.world.entity.impl.player.Player;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public class CustomBoxSpinner {

    private List<Item> items = new ArrayList<>();

    private final SecureRandom random = new SecureRandom();

    private final int STARTING_POINT = 32024;

    private final Player player;

    public CustomBoxSpinner(Player player) {
        this.player = player;
    }

    private int requiredId = 6638;

    public void open(int id, MysteryBox box) {
        player.getPacketSender().sendInterface(STARTING_POINT);
        requiredId = id;
        sendItems(box);
    }

    private int winningIndex = -1;

    private boolean spinning = false;

    public boolean isSpinning() {
        return spinning;
    }

    public int getWinningIndex() {
        return winningIndex;
    }

    public void onFinish(int index) {
        if(!spinning) {
            return;
        }
        if (index != winningIndex) {
            System.out.println("Wrong index " + index + " | " + winningIndex);
            return;
        }

        Item reward = items.get(index + 5);
        player.getInventory().add(reward);
        player.sendMessage("@blu@You've received a " + reward.getDefinition().getName());
        spinning = false;
    }

    public void spin() {
        if (spinning || items.size() < 24) {
            return;
        }

        if (player.getInventory().getFreeSlots() == 0) {
            player.sendMessage("@red@You have no free inventory slots");
            return;
        }

        if (player.getInventory().contains(requiredId)) {
            player.getInventory().delete(requiredId);
            winningIndex = 12 + random.nextInt((int) ((items.size() / 2.5D)));
            player.getPacketSender().sendSpinNotification(winningIndex);
            spinning = true;
        } else {
            player.sendMessage("@red@You need a " + ItemDefinition.forId(requiredId)
                .getName() + " to spin");
        }
    }



    private void sendItems(MysteryBox box) {
        if(spinning) {
            onFinish(winningIndex);
            spinning = false;
            winningIndex = -1;
        }
        player.getPacketSender().sendItemsOnInterfaceNew(STARTING_POINT + 21, box.commonRewards());
        player.getPacketSender()
            .sendItemsOnInterfaceNew(STARTING_POINT + 31, box.uncommonRewards());
        player.getPacketSender().sendItemsOnInterfaceNew(STARTING_POINT + 41, box.rareRewards());

        items = MysteryBoxHandler.getInstance().getItems(box);

        player.getPacketSender().sendItemsOnInterfaceNew(STARTING_POINT + 115, items);
    }
}
