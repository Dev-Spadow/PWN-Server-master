package com.arlania.world.content.perk_system;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.arlania.model.definitions.ItemDefinition;
import com.arlania.util.Misc;
import com.arlania.world.content.dialogue.Dialogue;
import com.arlania.world.content.dialogue.DialogueExpression;
import com.arlania.world.content.dialogue.DialogueManager;
import com.arlania.world.content.dialogue.DialogueType;
import com.arlania.world.entity.impl.player.Player;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

public class PerkHandler {

	private static String DIR = "./data/saves/perks/";

	public List<Perks> PERKS = new ArrayList<>();

	public void openPerkHandler(Player player) {

		player.getPacketSender().sendInterface(51500);
	}

	public void saveAllPerkHandler(Player player) {
		Path path = Paths.get(DIR + player.getUsername() + "json");
		File file = path.toFile();
		file.getParentFile().setWritable(true);

		if (!file.getParentFile().exists()) {
			try {
				file.getParentFile().mkdirs();
			} catch (SecurityException e) {
				System.out.println("Unable to create directory for perk data!");
			}
		}

		try (FileWriter writer = new FileWriter(file)) {
			Gson builder = new GsonBuilder().setPrettyPrinting().create();
			String json = builder.toJson(player.getPerkHandler().PERKS);
			writer.write(json);

			System.out.println("Saved " + player.getUsername() + " perks..");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void loadAllPerkHandler(Player player) {
		Path path = Paths.get(DIR + player.getUsername() + "json");
		File file = path.toFile();

		if (player.isMiniPlayer())
			return;

		if (!file.exists())
			return;

		try (FileReader fileReader = new FileReader(file)) {
			Gson builder = new GsonBuilder().create();
			JsonParser parser = new JsonParser();
			JsonArray reader = (JsonArray) parser.parse(fileReader);

			Type collectionType = new TypeToken<Collection<Perks>>() {
			}.getType();

			Collection<Perks> perks = builder.fromJson(reader, collectionType);

			player.getPerkHandler().PERKS.addAll(perks);

			System.out.println(
					"Loaded " + player.getPerkHandler().PERKS.size() + "perks to " + player.getUsername() + " .json");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Dialogue getDialogue(Player player) {
		return new Dialogue() {

			@Override
			public DialogueType type() {
				return DialogueType.OPTION;
			}

			@Override
			public DialogueExpression animation() {
				return DialogueExpression.NORMAL;
			}

			@Override
			public int npcId() {
				return -1;
			}

			@Override
			public String[] dialogue() {
				return new String[] { "Purchace "
						+ Misc.capitalize(player.getPerkHandler().getObtainablePerks().name().replace("_", " "))
						+ " for " + player.getPerkHandler().getObtainablePerks().getCurrencyAmount() + " "
						+ ItemDefinition.forId(player.getPerkHandler().getObtainablePerks().getCurrency()).getName() };
			}
		};
	}

	private ObtainablePerks perk;

	public ObtainablePerks getObtainablePerks() {
		return perk;
	}

	public void setObtainablePerks(ObtainablePerks perk) {
		this.perk = perk;
	}
}
