package com.arlania.world.content.wheel;

import com.arlania.model.definitions.ItemDefinition;
import com.arlania.util.Misc;
import com.arlania.world.World;
import com.arlania.world.entity.impl.player.Player;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Author Suic
 */

public class WheelOfFortune {

    private final int INTERFACE_ID = 21350;
    private final int WHEEL_INTERFACE_ID = 21352;
    private final int MODEL_COMPONENT_ID = 21377;
    private int[] rewards = {18985, 21812, 21813, 21814, 19901, 21694, 21868, 18782, 21692};
    private int[] rares = {21877, 5079, 3914, 21691, 19101, 4777, 3912};
    private int itemId;


    private final Player player;
    //unused for now
    private final int segments = 10;
    private final SecureRandom secureRandom = new SecureRandom();

    private WheelOfFortuneGame game = null;

    public WheelOfFortune(Player player) {
        this.player = player;
    }

    public void open(int[] normalRewards, int[] rareRewards, int itemId) {
        this.rewards = normalRewards;
        this.rares = rareRewards;
        this.itemId = itemId;
        player.getPacketSender().sendInterface(INTERFACE_ID);
        player.getPacketSender().updateInterfaceVisibility(21370, false);
        player.getPacketSender().updateInterfaceVisibility(21362, false);
    }
    
    public void spinsLeft(Player player) {
    	int spins = player.getInventory().getAmount(6638);
    	player.getPacketSender().sendString(47565, "" + spins);
    }
    
    String[] squeelMessage = {" - Visit Forums", " - Visit Guide list", " - Buy spins!"};
    
    public void sendText(Player player) {
    	int start = 47514;
    	for(String text : squeelMessage) {
    		player.getPacketSender().sendString(start++, text);
    	}
    	start = 47514;
    }

    public void start() {
        if(!player.getInventory().contains(itemId)) {
            return;
        }
        if (game != null) {
            player.sendMessage("@red@The wheel is already spinning, wait for it to finish before spinning again");
            return;
        }
        initGame();
    }

    private void initGame() {
        List<Integer> left = Arrays.stream(rewards).boxed().collect(Collectors.toList());
        int[] result = Stream.iterate(0, Integer::intValue)
            .limit(rewards.length)
            .mapToInt(i -> left.remove(secureRandom.nextInt(left.size())))
            .toArray();

        int randomRare = rares[secureRandom.nextInt(rares.length)];
        int[] newRewards = new int[segments];
        System.arraycopy(result, 0, newRewards, 0, result.length);
        if (Misc.random(3) == 2) {
            newRewards[newRewards.length - 1] = randomRare;
        }
        game = new WheelOfFortuneGame(newRewards);
        player.getPacketSender()
            .initWheelOfFortune(WHEEL_INTERFACE_ID, game.getWinningIndex(), game.getItems());
        player.getInventory().delete(itemId, 1);


    }

    public void onFinish(int index) {
        if (index != game.getWinningIndex()) {
            return;
        }
        for (int i : rares) {
            if (game.getReward().getId() == i) {
                World.sendMessage("<shad=bf0000>[Rare Reward]</shad>@bla@: "+player.getUsername().toString() +
                        " has just received a <col=FFFF64><shad=ebf217>" + ItemDefinition.forId(game.getReward().getId()).getName() +
                        " </shad>@bla@from the @red@ Squel of Fortune");
            }
        }
        player.getPacketSender()
            .sendInterfaceItemModel(MODEL_COMPONENT_ID, game.getReward().getId());
        player.getPacketSender().updateInterfaceVisibility(21370, true);
        player.getPacketSender().updateInterfaceVisibility(21362, true);
        player.getInventory().add(game.getReward());
        game = null;
    }
}
