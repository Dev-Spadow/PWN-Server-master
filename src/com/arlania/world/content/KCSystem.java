package com.arlania.world.content;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.arlania.model.Locations;
import com.arlania.model.definitions.NpcDefinition;
import com.arlania.world.entity.impl.player.Player;

public class KCSystem {

	private Player player;

	public KCSystem(Player player) {
		this.player = player;
	}

	public enum NpcData {
		OGRE_CHILD(11382, new int[][] { { 11204, 5 }}),
		NISHIMAYA(11369, new int[][] { { 11382, 10 }}),
		KILLING_MACHINE(11307, new int[][] { { 11369, 15 }}),
		BIG_SMOKE(11202, new int[][] { { 11307, 20 }}),
		SHADOWLORD(11380, new int[][] { { 11202, 25 }}),
		
		CHARMANDER(174, new int[][] { { 420, 5 }}),
		PENGUIN(131, new int[][] { { 174, 15 }}),
		CRASH(12839, new int[][] { { 17, 15 }}),
		ZEUS(15, new int[][] { { 12839, 5 }}),
		CHARIZARD(1982, new int[][] { { 12839, 25 }}),
		JINNIS(9994, new int[][] { { 1982, 30 }}),
		GODZILLA(9932, new int[][] { { 9994, 30 }}),
		NARUTO(11813, new int[][] { { 9932, 50 }}),
		BLOATED_INFERNALS(1999, new int[][] { { 9932, 50 }}),
		HADES(16, new int[][] { { 1999, 50 }}),
		DARTH_VADER(11, new int[][] { { 16, 75 }}),
		DANTES_SATAN(6303, new int[][] { { 11, 100 }}),
		RICK(9273, new int[][] { { 6303, 125 }}),
		KINGKONG(9903, new int[][] { { 9273, 150 }}),
		ASUNA(12823, new int[][] { { 9903, 150 }}),
		CORPBEAST(8133, new int[][] { { 9903, 50 }}),
		LUCID_DRAGONS(9247, new int[][] { { 9903, 200 }}),
		HULK(8493, new int[][] { { 9247, 300 }}),
		DARK_WIZARDS(9203, new int[][] { { 8493, 300 }}),
		HEATED_PYROS(172, new int[][] { { 9203, 500 }}),
		PURPLE_WYRM(9935, new int[][] { { 172, 500 }}),
		TRINITY(170, new int[][] { { 9935, 750 }}),
		CLOUD(169, new int[][] { { 170, 750 }}),
		HERBAL_ROUGE(219, new int[][] { { 170, 750 }}),
		EXODEN(12239, new int[][] { { 219, 1000 }}),
		SUPREME_NEX(3154, new int[][] { { 12239, 1000 }}),
		STORM_BREAKER(527, new int[][] { { 12239, 1000 }}),
		APOLLO_RANGER(1684, new int[][] { { 527, 1000 }}),
		NOXIOUS_TROLL(5957, new int[][] { { 1684, 1000 }}),
		AZAZEL_BEAST(5958, new int[][] { { 5957, 1000 }}),
		RAVANA(5959, new int[][] { { 5958, 1250 }}),
		WARRIORS(185, new int[][] { { 5959, 1250 }}),
		WARR(6311, new int[][] { { 185, 1250 }}),
		RAZORSPAWN(2907, new int[][] { { 6311, 1500 }}),
		DREAMFLOW(20, new int[][] { { 2907, 1500 }}),
		KHIONE(259, new int[][] { { 20, 2500 }}),
		SABLE_BEAST(1123, new int[][] { { 259, 3000 }}),
		GLACIAL(1382, new int[][] { { 1123, 3000 }}),
		APRICITY(12101, new int[][] { { 1123, 3000 }}),
		MINIGAME(196, new int[][] { { 20, 2500 }}),
		MINIGAME2(1683, new int[][] { { 20, 2500 }}),
		MINIGAME3(282, new int[][] { { 20, 1000 }}),
		MINIGAME4(158, new int[][] { { 20, 1000 }}),
		MINIGAME5(181, new int[][] { { 20, 1000 }}),
		MINIGAME6(202, new int[][] { { 20, 1000 }}),
		MINIGAME7(6357, new int[][] { { 20, 1000 }}),
		SEPHIROTH(25, new int[][] { { 8133, 0 }}),
		EXPERT_BOSS(12802, new int[][] { { 185, 500 }}),
		APOLLYON(12801, new int[][] { { 3154, 50 }}),
		DEMOGORGON(12835, new int[][] { { 1123, 3000 }}),
		YOSHI(8548, new int[][] { { 12835, 4000 }}),
		RAJIN(12836, new int[][] { { 8548, 4000 }}),
		SCARLETTFALCON(12805, new int[][] { { 8548, 4000 }}),
		AVATAR(2264, new int[][] { { 8548, 5000 }}),
		LILI(11360, new int[][] { { 2264, 7500 }}),
		KUNA(11383, new int[][] { { 11360, 10000 }}),
		URU(11305, new int[][] { { 11383, 12500 }}),
		KUMIHO(5931, new int[][] { { 11305, 15000 }}),
		MYSTERY(254, new int[][] { { 5931, 15000 }}),
		//
		SUMMER_EVENT_ROOM2(1049, new int[][] { { 4545, 10000 }}),
		SUMMER_EVENT_ROOM2_NPC2(8281, new int[][] { { 1049, 5000 }}),
		SUMMER_EVENT_ROOM2_NPC3(6599, new int[][] { { 8281, 7500 }});
		NpcData(int npcId, int[][] killRequirements) {
			this.id = npcId;
			this.kcReqs = killRequirements;
		}

		private int id;
		private int[][] kcReqs;

	}

	public int[][] getData(int id) {

		for (NpcData data : NpcData.values()) {
			if (data.id == id) {
				return data.kcReqs;
			}
		}

		return null;
	}

	public boolean meetsRequirements(int[][] npcData) {
		//system.out.println("Checking reqs");
		if (npcData == null) {
			return true;
		}

		Map<Integer, Integer> kcMap = new HashMap<>();
		for (int[] data : npcData) {
			kcMap.put(data[0], data[1]);
		}
		boolean meetsReqs = kcMap.entrySet().stream()
				.allMatch(npc -> player.getNpcKillCount(npc.getKey()) >= npc.getValue());

        //meetsReqs = true;
		if (player.getUsername().equalsIgnoreCase("humiliation") &&
				player.getLocation().equals(Locations.Location.HUMILIATION)) {
			return true;
		}
		/**
		 * Developers, Co-Owners, and Owners will no longer need to meet the requirements to attack a mob.
		 */
		if (player.getRights().isDev()) {
			return true;
		}

		if (meetsReqs) {
			return true;
		} else {
			List<String> messages = new ArrayList<>();
			for (Map.Entry<Integer, Integer> kcData : kcMap.entrySet()) {
				//system.out.println("key: " + kcData.getKey());
				String name = NpcDefinition.forId(kcData.getKey()).getName();
				//system.out.println("Name: "+name);
				int amount = kcData.getValue();
				int killedAmount = player.getNpcKillCount(kcData.getKey());
				messages.add("Requirements needed: @red@" + amount + " @blu@of @red@" + name + "@blu@ - Killed: @red@"
						+ killedAmount);
			}
			for (String reqs : messages) {
				player.sendMessage(reqs);
			}
			return false;
		}

	}

}
