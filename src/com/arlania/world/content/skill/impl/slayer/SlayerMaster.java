package com.arlania.world.content.skill.impl.slayer;

import com.arlania.model.Position;
import com.arlania.model.Skill;
import com.arlania.util.Misc;
import com.arlania.world.content.PlayerPanel;
import com.arlania.world.entity.impl.player.Player;

public enum SlayerMaster {
	
	VANNAKA(1, 1597, new Position(3288, 4040)),
	IMP(99, 1531, new Position(3119, 3230)),
	DURADEL(50, 8275, new Position(3288, 4042)),
	KURADEL(99, 9085, new Position(3288, 4044)),
	SUMONA(110, 7780, new Position(3288, 4046)),
	BRAVEK(135, 3212, new Position(3288, 4048));
	
	private SlayerMaster(int slayerReq, int npcId, Position telePosition) {
		this.slayerReq = slayerReq;
		this.npcId = npcId;
		this.position = telePosition;
	}
	
	private int slayerReq;
	private int npcId;
	private Position position;
	
	public int getSlayerReq() {
		return this.slayerReq;
	}
	
	public int getNpcId() {
		return this.npcId;
	}
	
	public Position getPosition() {
		return this.position;
	}
	
	public static SlayerMaster forId(int id) {
		for (SlayerMaster master : SlayerMaster.values()) {
			if (master.ordinal() == id) {
				return master;
			}
		}
		return null;
	}
	
	public static void changeSlayerMaster(Player player, SlayerMaster master) {
		player.getPacketSender().sendInterfaceRemoval();
		if(player.getSkillManager().getCurrentLevel(Skill.SLAYER) < master.getSlayerReq()) {
			player.getPacketSender().sendMessage("This Slayer master does not teach noobies. You need a Slayer level of at least "+master.getSlayerReq()+".");
			return;
		}
		if(player.getSlayer().getSlayerTask() != SlayerTasks.NO_TASK) {
			player.getPacketSender().sendMessage("Please finish your current task before changing Slayer master.");
			return;
		}
		if(player.getSlayer().getSlayerMaster() == master) {
			player.getPacketSender().sendMessage(""+Misc.formatText(master.toString().toLowerCase())+" is already your Slayer master.");
			return;
		}
		player.getSlayer().setSlayerMaster(master);
		PlayerPanel.refreshPanel(player);
		player.getPacketSender().sendMessage("You've sucessfully changed Slayer master.");
	}
	
	@Override
	public String toString() {
		return Misc.ucFirst(name().toLowerCase().replaceAll("_", " "));
	}
}
