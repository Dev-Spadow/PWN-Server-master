package com.arlania.world.content.invansionminigame;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.arlania.model.Position;
import com.arlania.util.RandomUtility;
import com.arlania.world.World;
import com.arlania.world.entity.impl.npc.NPC;
import com.arlania.world.entity.impl.player.Player;
import com.arlania.world.entity.impl.player.PlayerLoading;

public class InvasionGame {

	private Player player;

	public InvasionGame(Player player) {
		this.player = player;
	}

	private int cityIndex;

	public int getCityIndex() {
		return cityIndex;
	}

	private final int[] TOP1_REWARDS = new int[] { 14484, 11694, 1050, 1048 };
	private final int[] TOP2_REWARDS = new int[] { 1046, 1044, 1042 };
	private final int[] TOP3_REWARDS = new int[] { 1040, 1053, 4565 };

	private final int[] OTHER_REWARDS = new int[] { 4151, 6585, 6570 };

	private List<Position> positionData = new ArrayList<>();
	// private List<Position> positionData2 = new ArrayList<>();
	// private List<Position> positionData3 = new ArrayList<>();
	int index = 0;

	public void parsePositionData() {

		Path path = Paths.get("./src/com/arlania/world/content/invansionminigame/posdata.txt");
		try (Stream<String> lines = Files.lines(path)) {

			lines.forEach(line -> {

				String[] data = line.split(",");
				if (data == null) {
					return;
				}
				int x = Integer.parseInt(data[0].trim());
				int y = Integer.parseInt(data[1].trim());
				positionData.add(new Position(x, y));
			});

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setCityIndex(int index) {
		this.cityIndex = index;
	}

	private void chooseCity() {

		int random = RandomUtility.inclusiveRandom(1, 3);

		this.setCityIndex(random);

	}
	
	private Map<Player, Integer> top3;
	
	private void fetchHiscores() {
		Map<Player, Integer> kcMap = new HashMap<>();
		for (File file : new File("data/saves/characters/").listFiles()) {
			Player player = new Player(null);
			player.setUsername(file.getName().substring(0, file.getName().length() - 5));

			PlayerLoading.getResult(player);

			//kcMap.put(player, player.getInvasionKc());
			kcMap.put(player, player.getInvasionKc());

		}
		
		top3 = kcMap.entrySet().stream()
				.sorted(Collections.reverseOrder(Map.Entry.comparingByValue())).limit(3)
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));

		for (Map.Entry<Player, Integer> map : top3.entrySet()) {
			System.out.println("Key: " + map.getKey().getUsername() + " | Value: " + map.getValue());
		}

	}

	private List<NPC> spawnedNpcs = new ArrayList<>();

	public void initializeEvent() {

		chooseCity();

		System.out.println("Chosen: " + cityIndex);

		switch (cityIndex) {

		case 1:
			initializeLumbridge();
			break;
		case 2:
			initializeVarrock();
			break;

		case 3:
			initializeFalador();
			break;
		}
	}

	public void initAll() {
		initializeLumbridge();
		initializeVarrock();
		initializeFalador();
	}

	private void initializeLumbridge() {

		for (int i = 0; i < 93; i++) {
			Position pos = positionData.get(i);
			NPC npc = new NPC(131, pos);
			spawnedNpcs.add(npc);
			World.register(npc);

		}
	}

	private void initializeVarrock() {

		for (int i = 93; i < 282; i++) {
			Position pos = positionData.get(i);
			NPC npc = new NPC(131, pos);
			spawnedNpcs.add(npc);
			World.register(npc);
		}

	}

	private void initializeFalador() {

		for (int i = 282; i < positionData.size(); i++) {
			Position pos = positionData.get(i);
			NPC npc = new NPC(131, pos);
			spawnedNpcs.add(npc);
			World.register(npc);
		}
	}

	public void stopEvent() {
		spawnedNpcs.forEach(x -> {
			World.deregister(x);
		});

		spawnedNpcs.clear();
	}



}
