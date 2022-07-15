package com.arlania.world.content.skill.impl.woodcutting;
import com.arlania.util.Misc;
import com.arlania.world.entity.impl.player.Player;


/**
 * @author Optimum
 * I do not give permission to 
 * release this anywhere else
 */

public class BirdNests {

	/**
	 * Ints.
	 */

	public static final int[] BIRD_NEST_IDS = {5070, 5071, 5072};
	public static final int EMPTY = 5075;
	public static final int RED = 5076;
	public static final int BLUE = 5077;
	public static final int GREEN = 5078;
	public static final int AMOUNT = 1;


	/**
	 * Check if the item is a nest
	 *
	 */
	public static boolean isNest(final int itemId) {
		for(int nest : BIRD_NEST_IDS) {
			if(nest == itemId) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Generates the random drop and creates a ground item
	 * where the player is standing
	 */

	public static void dropNest(Player p){
		if(p.getPosition().getZ() > 0) {
			return;
		}
		if(Misc.getRandom(60) == 1) {
			int r = Misc.getRandom(1000);
			if(r >= 0 && r <= 780){
			p.getInventory().add(3912,1);
			}
			else if(r >= 780 && r <= 999){
				p.getInventory().add(3912,1);
			}
			else if(r >= 961){
				int random = Misc.getRandom(2);
				if(random == 1){
				
					p.getInventory().add(3912,1);
				}else if (random == 2){
				
					p.getInventory().add(3912,1);
				}else{
				
					
					p.getInventory().add(3912,1);
				}
			}
		}
	}

	/**
	 * 
	 * Searches the nest.
	 * 
	 */

	public static final void searchNest(Player p, int itemId) {
		if(p.getInventory().getFreeSlots() <= 0) {
			p.getPacketSender().sendMessage("You do not have enough free inventory slots to do this.");
			return;
		}
		p.getInventory().delete(itemId, 1);
		eggNest(p, itemId);

		p.getInventory().add(EMPTY, AMOUNT);
	}

	/**
	 * 
	 * Determines what loot you get
	 *  from ring bird nests
	 *  
	 */


	/**
	 * 
	 * Egg nests
	 * 
	 */

	public static final void eggNest(Player p, int itemId){
		if(itemId == 5070){
			p.getInventory().add(RED, AMOUNT);
		}
		if(itemId == 5071){
			p.getInventory().add(GREEN, AMOUNT);
		}
		if(itemId == 5072){
			p.getInventory().add(BLUE, AMOUNT);
		}
	}

}