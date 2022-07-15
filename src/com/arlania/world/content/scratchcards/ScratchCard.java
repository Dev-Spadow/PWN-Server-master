package com.arlania.world.content.scratchcards;

import com.arlania.model.Item;
import com.arlania.model.definitions.ItemDefinition;
import com.arlania.util.RandomUtility;
import com.arlania.world.World;
import com.arlania.world.entity.impl.player.Player;

public class ScratchCard {

	private Player player;

	public boolean scratching = false;
	final int[] COMMON_ITEMS = new int[] { 10168,5266,5170,19935,19936, };
	final int[] RARE_ITEMS = new int[] { 15566,6934,3267,4652,3277,3310,3309,3307,5132,6990,5197,8654,774,773,16455,19938};

	public ScratchCard(Player player) {
		this.player = player;
	}

	private int item1, item2, item3;

	public void scratch() {
		if (scratching) {
			player.sendMessage("@red@Please wait till current game is finished");
			return;
		}
		int random = RandomUtility.exclusiveRandom(3);
		if (random != 2) {
			item1 = COMMON_ITEMS[RandomUtility.inclusiveRandom(0, COMMON_ITEMS.length - 1)];
			item2 = COMMON_ITEMS[RandomUtility.inclusiveRandom(0, COMMON_ITEMS.length - 1)];
			item3 = COMMON_ITEMS[RandomUtility.inclusiveRandom(0, COMMON_ITEMS.length - 1)];
		} else if(random != 3)  {
			item1 = RARE_ITEMS[RandomUtility.inclusiveRandom(0, RARE_ITEMS.length - 1)];
			item2 = RARE_ITEMS[RandomUtility.inclusiveRandom(0, RARE_ITEMS.length - 1)];
			item3 = RARE_ITEMS[RandomUtility.inclusiveRandom(0, RARE_ITEMS.length - 1)];
		}

		player.getPacketSender().sendScratchcardItems(item1, item2, item3);
		scratching = true;
	}

	public void open() {
		player.getPacketSender().sendInterface(23630);
		for (int i = 0; i < COMMON_ITEMS.length; i++)
			player.getPacketSender().sendItemOnInterface(23642, COMMON_ITEMS[i], i, 1);
		for (int x = 0; x < RARE_ITEMS.length; x++)
			player.getPacketSender().sendItemOnInterface(23645, RARE_ITEMS[x], x, 1);
	}

	public void getWinnings() {
		int count = 0;

		if (item1 == item2 && item1 == item3) {
			System.out.println("A TRIPLE!");
			count = 2;
		} else if (item1 == item2 || item1 == item3) {
			count = 1;
		} else if (item2 == item3) {
			count = 1;
		}

		if (count == 0) {
			player.sendMessage("You scratch the card but have no luck");
		} else if (count == 1) {
			player.sendMessage("You scratch the card but have no luck");
		} else if (count == 2) {
			player.sendMessage("Congrats! You won!");
			player.getInventory().add(item1, 1);
			String name = ItemDefinition.forId(item1).getName();
			World.sendMessage("@blu@<img=382>[SCRATCHCARD]<img=382> @red@" + player.getUsername() + " has got an " + name + " From scratchcards!");
		}
	

	if (player.getInventory().getFreeSlots() >=1) { // checks if player has 3 or more slots available, if true, executes the method boxInfo
	} else {
		player.sendMessage("@red@You need atleast 1 free spaces in order to scratch the card"); // if not sends player a msg.
	}
	
	
}
	

}
