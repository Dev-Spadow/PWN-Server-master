package com.arlania.world.content;

import com.arlania.model.Flag;
import com.arlania.model.Position;
import com.arlania.model.definitions.NpcDefinition;
import com.arlania.util.Misc;
import com.arlania.world.World;
import com.arlania.world.entity.impl.npc.NPC;
import com.arlania.world.entity.impl.player.Player;

/*
 * @author Arlania - Arlania rsps
 */
public class BublleGum {

	
	public static int answerCount;
	public static String firstPlace;
	//public static String secondPlace;
	//public static String thirdPlace;

	//public static List<String> attempts = new ArrayList<>(); 

	public static void sequence(Player p) {

		boolean done = false;

		if(p.getTimer() > 0 && p.getTransform() > 0) {
			p.setTimer(p.getTimer() - 1);
			p.sendParallellInterfaceVisibility(25347, true);
			p.getPacketSender().sendString(25354, "");
			p.getPacketSender().sendString(25353, "");
			p.getPacketSender().sendString(25355, "");
			p.getPacketSender().sendString(25356, "");
			p.getPacketSender().sendString(25349, "Name:");
			p.getPacketSender().sendString(25350, NpcDefinition.forId(p.getTransform()).getName() + "");

			p.getPacketSender().sendString(25351, "Timeleft:");
				p.getPacketSender().sendString(25352, p.getTimer() / 60 + " Min");

			//	p.set
		} else if(p.getTimer() == 0 && p.getTransform() > 0) {
			p.sendParallellInterfaceVisibility(25347, false);
			p.sendMessage("Your 30 mins is over!");
			p.setNpcTransformationId(0);
			p.setTransform(0);
			p.getUpdateFlag().flag(Flag.APPEARANCE);
			p.setConstitution(990);

		}
	}

	public static void getHealth(Player p, int npcId){

    }
}