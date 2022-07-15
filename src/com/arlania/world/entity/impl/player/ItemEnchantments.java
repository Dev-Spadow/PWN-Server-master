package com.arlania.world.entity.impl.player;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;

import com.arlania.GameServer;
import com.arlania.model.Item;
import com.arlania.model.definitions.ItemDefinition;
import com.arlania.world.entity.impl.player.Player;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ItemEnchantments {

	private Path filePath;
	private File file;

	private Player player;
	private Integer[] loadedIceSlot = new Integer[] {}, loadedSmokeSlot = new Integer[] {}, loadedBloodSlot = new Integer[] {}, loadedShadowSlot = new Integer[] {};

	public ItemEnchantments(Player player) {
		this.player = player;
		filePath = Paths.get("./data/saves/itemenchantments/" + player.getUsername() + ".json");
		file = filePath.toFile();

		// Attempt to make the player save directory if it doesn't
		// exist.
		if (!file.getAbsoluteFile().exists()) {
			try {
				try {
					file.getParentFile().createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				initialize();
			} catch (SecurityException e) {
				System.out.println("Unable to create directory for player data!");
			}
		} else {
			load();
		}
	}

	private void initialize() {
		file.getParentFile().setWritable(true);
		Integer[] iceSlot = {}, bloodSlot = {}, smokeSlot = {}, shadowSlot = {};
		loadedIceSlot = iceSlot;
		loadedBloodSlot = bloodSlot;
		loadedSmokeSlot = smokeSlot;
		loadedShadowSlot = shadowSlot;

		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		try (FileWriter writer = new FileWriter(file)) {
			Gson builder = new GsonBuilder().setPrettyPrinting().create();
			JsonObject object = new JsonObject();
			object.add("ice-diamond", builder.toJsonTree(iceSlot));
			object.add("blood-diamond", builder.toJsonTree(bloodSlot));
			object.add("smoke-diamond", builder.toJsonTree(smokeSlot));
			object.add("shadow-diamond", builder.toJsonTree(shadowSlot));
			writer.write(builder.toJson(object));
			writer.close();
			//System.out.println("Created item enchantments file: " + file.getPath());
		} catch (Exception e) {
			// An error happened while saving.
			GameServer.getLogger().log(Level.WARNING, "An error has occured while creating item enchantment file!", e);
		}
	}

	public void load() {
		try (FileReader fileReader = new FileReader(file)) {
			//System.out.println("Now attempting to read file from Item Enchantments");
			//System.out.println(file.getAbsolutePath());
			JsonParser fileParser = new JsonParser();
			Gson builder = new GsonBuilder().create();
			JsonObject reader = (JsonObject) fileParser.parse(fileReader);

			if (reader.has("ice-diamond")) {
				loadedIceSlot = builder.fromJson(reader.get("ice-diamond").getAsJsonArray(), Integer[].class);
			}

			if (reader.has("blood-diamond")) {
				loadedBloodSlot = builder.fromJson(reader.get("blood-diamond").getAsJsonArray(), Integer[].class);
			}

			if (reader.has("smoke-diamond")) {
				loadedSmokeSlot = builder.fromJson(reader.get("smoke-diamond").getAsJsonArray(), Integer[].class);
			}

			if (reader.has("shadow-diamond")) {
				loadedShadowSlot = builder.fromJson(reader.get("shadow-diamond").getAsJsonArray(), Integer[].class);
			}

			fileReader.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public boolean checkEnchantment(int diamondId, int weaponId) {
		switch (diamondId) {
		case 4670: // blood diamond
			for (int i = 0; i < loadedBloodSlot.length; i++) {
				if (loadedBloodSlot[i] != null) {
					if (loadedBloodSlot[i] == weaponId)
						return true;
				}
			}
			break;
		case 4671: // ice diamond
			for (int i = 0; i < loadedIceSlot.length; i++) {
				if (loadedIceSlot[i] != null) {
					if (loadedIceSlot[i] == weaponId)
						return true;
				}
			}
			break;
		case 4672: // smoke diamond
			for (int i = 0; i < loadedSmokeSlot.length; i++) {
				if (loadedSmokeSlot[i] != null) {
					if (loadedSmokeSlot[i] == weaponId)
						return true;
				}
			}
			break;
		case 4673: // shadow diamond
			for (int i = 0; i < loadedShadowSlot.length; i++) {
				if (loadedShadowSlot[i] != null) {
					if (loadedShadowSlot[i] == weaponId)
						return true;
				}
			}
			break;
		}

		return false;
	}

	public void addEnchantment(Player player, int diamondId, int weaponId) {
		switch (diamondId) {
		case 4670:
			if (checkEnchantment(diamondId, weaponId)) {
				player.sendMessage("You already enchanted that type of item with Blood Diamonds.");
				return;
			}
			ArrayList<Integer> currentItems0 = new ArrayList<Integer>();
			for (Integer i : loadedBloodSlot) {
				currentItems0.add(i);
			}
			currentItems0.add(weaponId);
			player.getInventory().delete(diamondId, 50);
			loadedBloodSlot = currentItems0.toArray(new Integer[currentItems0.size()]);
			save();
			break;
		case 4671:
			if (checkEnchantment(diamondId, weaponId)) {
				player.sendMessage("You already enchanted that type of item with Ice Diamonds.");
				return;
			}
			ArrayList<Integer> currentItems = new ArrayList<Integer>();
			for (Integer i : loadedIceSlot) {
				currentItems.add(i);
			}
			currentItems.add(weaponId);
			player.getInventory().delete(diamondId, 50);
			loadedIceSlot = currentItems.toArray(new Integer[currentItems.size()]);
			save();
			break;
		case 4672: 
			if (checkEnchantment(diamondId, weaponId)) {
				player.sendMessage("You already enchanted that type of item with Smoke Diamonds.");
				return;
			}
			ArrayList<Integer> currentItems2 = new ArrayList<Integer>();
			for (Integer i : loadedSmokeSlot) {
				currentItems2.add(i);
			}
			currentItems2.add(weaponId);
			player.getInventory().delete(diamondId, 50);
			loadedSmokeSlot = currentItems2.toArray(new Integer[currentItems2.size()]);
			save();
			break;
		case 4673: 
			if (checkEnchantment(diamondId, weaponId)) {
				player.sendMessage("You already enchanted that type of item with Shadow Diamonds.");
				return;
			}
			ArrayList<Integer> currentItems3 = new ArrayList<Integer>();
			for (Integer i : loadedShadowSlot) {
				currentItems3.add(i);
			}
			currentItems3.add(weaponId);
			player.getInventory().delete(diamondId, 50);
			loadedShadowSlot = currentItems3.toArray(new Integer[currentItems3.size()]);
			save();
			break;
		}
	}

	public void save() {
		file.getParentFile().setWritable(true);

		// Attempt to make the player save directory if it doesn't
		// exist.
		if (!file.getAbsoluteFile().exists()) {
			try {
				try {
					file.getParentFile().createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (SecurityException e) {
				System.out.println("Unable to create directory for player data!");
			}
		}

		JsonObject reader = null;

		try (FileReader fileReader = new FileReader(file)) {

			//System.out.println("Now attempting to read file from " + file.toPath());
			JsonElement fileParser = new Gson().fromJson(fileReader, JsonElement.class);
			Gson builder = new GsonBuilder().create();
			reader = fileParser.getAsJsonObject();

			if (reader.has("blood-diamond")) {
				reader.remove("blood-diamond");
			}
			reader.add("blood-diamond", builder.toJsonTree(loadedBloodSlot));

			if (reader.has("ice-diamond")) {
				reader.remove("ice-diamond");
			}
			reader.add("ice-diamond", builder.toJsonTree(loadedIceSlot));

			if (reader.has("smoke-diamond")) {
				reader.remove("smoke-diamond");
			}
			reader.add("smoke-diamond", builder.toJsonTree(loadedSmokeSlot));
			
			if (reader.has("shadow-diamond")) {
				reader.remove("shadow-diamond");
			}
			reader.add("shadow-diamond", builder.toJsonTree(loadedShadowSlot));
			
			fileReader.close();
		} catch (Exception e) {
			System.out.print(e.getMessage());
		}

		try (FileWriter writer = new FileWriter(file)) {
			Gson builder = new GsonBuilder().setPrettyPrinting().create();
			writer.write(builder.toJson(reader));
			writer.close();
			//System.out.println("Updated file: " + file.getPath());
		} catch (Exception e) {
			// An error happened while saving.
			GameServer.getLogger().log(Level.WARNING, "An error has occured while saving a file file!", e);
		}
	}

}
