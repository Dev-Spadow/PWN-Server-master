package com.arlania.world.entity.impl.npc.Bosses.zulrah.impl;

import com.arlania.event.CycleEventContainer;
import com.arlania.model.Flag;
import com.arlania.world.content.combat.CombatType;
import com.arlania.world.entity.impl.npc.Bosses.zulrah.Zulrah;
import com.arlania.world.entity.impl.npc.Bosses.zulrah.ZulrahLocation;
import com.arlania.world.entity.impl.npc.Bosses.zulrah.ZulrahStage;
import com.arlania.world.entity.impl.player.Player;

public class MageStageThree extends ZulrahStage {

	public MageStageThree(Zulrah zulrah, Player player) {
		super(zulrah, player);
	}

	@Override
	public void execute(CycleEventContainer container) {
		if (container.getOwner() == null || zulrah == null || zulrah.getNpc() == null || zulrah.getNpc().isDying()
				|| player == null || player.isDying() || player.getRegionInstance()  == null) {
			container.stop();
			return;
		}
		zulrah.getNpc().getCombatBuilder().attack(player);
		zulrah.getNpc().setPosition(player.getPosition());
		zulrah.getNpc().getUpdateFlag().flag(Flag.FACE_POSITION);
		if (zulrah.getNpc().totalAttacks > 5) {
			player.getZulrahEvent().changeStage(4, CombatType.RANGED, ZulrahLocation.WEST);
			zulrah.getNpc().totalAttacks = 0;
			container.stop();
			return;
		}
	}

}
