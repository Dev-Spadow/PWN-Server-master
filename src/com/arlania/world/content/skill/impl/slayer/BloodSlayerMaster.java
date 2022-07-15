package com.arlania.world.content.skill.impl.slayer;

import com.arlania.model.Position;
import com.arlania.model.Skill;
import com.arlania.util.Misc;
import com.arlania.world.content.PlayerPanel;
import com.arlania.world.entity.impl.player.Player;

public enum BloodSlayerMaster {
	NONE(1, -1, new Position(3296, 4044)),
	
	IMP(1, 1531, new Position(3296, 4044));
	
	private BloodSlayerMaster(int slayerReq, int npcId, Position telePosition) {
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
	
	public static BloodSlayerMaster forId(int id) {
		for (BloodSlayerMaster master : BloodSlayerMaster.values()) {
			if (master.ordinal() == id) {
				return master;
			}
		}
		return null;
	}
	
	public static void changeBloodSlayerMaster(Player player, BloodSlayerMaster master) {
		player.getPacketSender().sendInterfaceRemoval();
		if(player.getSkillManager().getCurrentLevel(Skill.SLAYER) < master.getSlayerReq()) {
			player.getPacketSender().sendMessage("This Slayer master does not teach noobies. You need a Slayer level of at least "+master.getSlayerReq()+".");
			return;
		}
		if(player.getSlayer().getSlayerTask() != SlayerTasks.NO_TASK) {
			player.getPacketSender().sendMessage("Please finish your current task before changing Slayer master.");
			return;
		}
		if(player.getBloodSlayer().getBloodSlayerMaster() == IMP) {
			player.getPacketSender().sendMessage(""+Misc.formatText(IMP.toString().toLowerCase())+" is already your Slayer master.");
			return;
		}
		player.getBloodSlayer().setBloodSlayerMaster(IMP);
		PlayerPanel.refreshPanel(player);
		player.getPacketSender().sendMessage("You've sucessfully changed Slayer master.");
	}
	
	@Override
	public String toString() {
		return Misc.ucFirst(name().toLowerCase().replaceAll("_", " "));
	}
}
