package com.arlania.world.content.invansionminigame;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import com.arlania.util.RandomUtility;
import com.arlania.world.entity.impl.player.Player;
import com.arlania.world.entity.impl.player.PlayerLoading;

public class Testing {

	public static void main(String[] args) {

		Map<Player, Integer> kcMap = new HashMap<>();
		for (File file : new File("data/saves/characters/").listFiles()) {
			Player player = new Player(null);
			player.setUsername(file.getName().substring(0, file.getName().length() - 5));

			PlayerLoading.getResult(player, true);

			kcMap.put(player, RandomUtility.inclusiveRandom(0, 100));

		}

		final Map<Player, Integer> top3 = kcMap.entrySet().stream()
				.sorted(Collections.reverseOrder(Map.Entry.comparingByValue())).limit(3)
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));

		for (Map.Entry<Player, Integer> map : top3.entrySet()) {
			System.out.println("Key: " + map.getKey().getUsername() + " | Value: " + map.getValue());
		}

	}

}
