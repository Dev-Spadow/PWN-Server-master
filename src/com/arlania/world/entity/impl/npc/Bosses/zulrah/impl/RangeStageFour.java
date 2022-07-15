package com.arlania.world.entity.impl.npc.Bosses.zulrah.impl;

import java.util.Arrays;

import com.arlania.event.CycleEventContainer;
import com.arlania.event.CycleEventHandler;
import com.arlania.model.Flag;
import com.arlania.world.content.combat.CombatType;
import com.arlania.world.entity.impl.npc.Bosses.zulrah.DangerousEntity;
import com.arlania.world.entity.impl.npc.Bosses.zulrah.DangerousLocation;
import com.arlania.world.entity.impl.npc.Bosses.zulrah.SpawnDangerousEntity;
import com.arlania.world.entity.impl.npc.Bosses.zulrah.Zulrah;
import com.arlania.world.entity.impl.npc.Bosses.zulrah.ZulrahLocation;
import com.arlania.world.entity.impl.npc.Bosses.zulrah.ZulrahStage;
import com.arlania.world.entity.impl.player.Player;

public class RangeStageFour extends ZulrahStage {

	public RangeStageFour(Zulrah zulrah, Player player) {
		super(zulrah, player);
	}

	@Override
	public void execute(CycleEventContainer container) {
		if (container.getOwner() == null || zulrah == null || zulrah.getNpc() == null || zulrah.getNpc().isDying()
				|| player == null || player.isDying() || player.getRegionInstance()  == null) {
			container.stop();
			return;
		}
		int ticks = container.getTotalTicks();
		zulrah.getNpc().getCombatBuilder().attack(player);
		if (ticks == 4) {
			//zulrah.getNpc().setFacePlayer(false);
			CycleEventHandler.getSingleton().addEvent(player, new SpawnDangerousEntity(zulrah, player, Arrays.asList(
					DangerousLocation.EAST, DangerousLocation.SOUTH_EAST, DangerousLocation.SOUTH_WEST), DangerousEntity.TOXIC_SMOKE, 20), 1);
		} else if (ticks == 16) {
			CycleEventHandler.getSingleton().addEvent(player, new SpawnDangerousEntity(zulrah, player, Arrays.asList(
					DangerousLocation.SOUTH_EAST, DangerousLocation.SOUTH_WEST), DangerousEntity.MINION_NPC), 1);
		} else if (ticks == 26) {
			zulrah.getNpc().setPositionToFace(player.getPosition());
			zulrah.getNpc().getUpdateFlag().flag(Flag.FACE_POSITION);
			zulrah.changeStage(5, CombatType.MAGIC, ZulrahLocation.SOUTH);
			zulrah.getNpc().totalAttacks = 0;
			container.stop();
		}
	}

}
