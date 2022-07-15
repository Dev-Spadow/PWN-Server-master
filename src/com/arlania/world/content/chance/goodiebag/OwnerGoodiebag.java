package com.arlania.world.content.chance.goodiebag;

import com.arlania.model.definitions.ItemDefinition;
import com.arlania.world.World;
import com.arlania.world.entity.impl.player.Player;

import java.util.Random;

public class OwnerGoodiebag {

    private Player player;

    public OwnerGoodiebag(Player player) {
        this.player = player;
    }
    private boolean claimed = false;

    public int boxId = -1;


    public int[] rewards = {22000, 22001, 22002, 22004, 16455, 16455, 16455, 16455, 16455, 16455,
            16455, 16455, 16455, 16455, 16455, 16455, 16455, 16455, 16455, 16455};

    public void setBoxId(int toSet) {
        this.boxId = toSet;
        System.out.println("Box Id Test: " + this.boxId + " Whats thrown: " + toSet);
    }

    public int getBoxId() {
        return this.boxId;
    }

    public void setRewards(int[] rewards) {
        this.rewards = rewards;
    }

    public void open() {
        player.getPacketSender().sendInterface(60500);
        player.getPacketSender().resetItemsOnInterface(60570, 20);
        shuffle(rewards);
        claimed = false;
        player.selectedGoodieBag = -1;
        for (int i = 1; i <= 20; i++) {
            player.getPacketSender().sendString(60532 + i, String.valueOf(i));
        }
    }

    private void shuffle(int[] array) {
        Random rnd = new Random();
        for (int i = array.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            int a = array[index];
            array[index] = array[i];
            array[i] = a;
        }
    }

    private void showRewards() {

        for (int i = 1; i <= 20; i++) {
            player.getPacketSender().sendString(60532 + i, "");
        }

        for (int i = 0; i < rewards.length; i++) {
            player.getPacketSender().sendItemOnInterface(60570, rewards[i], i, 1);
        }
    }

    public boolean handleClick(int buttonId) {
        if (!(buttonId >= -5025 && buttonId <= -5006)) {
            return false;
        }

        if(claimed) {
            return false;
        }

        int index = -1;

        if (buttonId >= -5025) {
            index = 5025 + buttonId;
        }
        player.getPacketSender().sendString(60532 + player.selectedGoodieBag + 1,
                String.valueOf(player.selectedGoodieBag + 1));
        player.selectedGoodieBag = index;
        player.getPacketSender().sendString(60532 + index + 1, "Pick");

        return true;
    }


    public void claim() {
        if (player.selectedGoodieBag == -1) {
            player.sendMessage("@red@You haven't picked a number yet");
            return;
        }

        if (getBoxId() == -1) {
            player.sendMessage("You already opened this box");
            return;
        }
        if (!claimed) {
            if (player.getInventory().contains(boxId)) {
                showRewards();
                player.getInventory().delete(boxId, 1);
                player.getInventory().add(rewards[player.selectedGoodieBag], 1);
                World.sendChannelMessage("Goodiebag", player.getUsername() + " has just received a " + ItemDefinition.forId(rewards[player.selectedGoodieBag]).getName());
                claimed = true;
                boxId = -1;
            } else {
                player.sendMessage("@red@You need a goodiebag box to claim the reward");
            }
        } else {
            player.sendMessage("@red@You've already claimed the reward for this box");
        }
    }
}
