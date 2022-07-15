package com.arlania.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import com.arlania.GameServer;
import com.arlania.world.entity.impl.player.Player;
import com.arlania.world.entity.impl.player.PlayerHandler;

public final class MACBanL {

	private static List<String> macList = new ArrayList<>();
	private static final String PATH = "./data/saves/macbans.txt";
	
	private static boolean noMacFound(String mac) {
		return mac == null || mac.equalsIgnoreCase("Could not find hardware Address") || mac.equalsIgnoreCase("No MAC Found");
	}
	
	public static void init() {
		macList.clear();
		File file = new File(PATH);
		if (!file.exists())
			return;
		try {
			List<String> lines = Files.readAllLines(file.toPath());
			for (String line : lines) {
				line = line.trim();
				if (!line.isEmpty())
					macList.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void save() {
		GameServer.getLoader().getEngine().submit(() -> {
			try (FileWriter fw = new FileWriter(PATH, false)) {
				for (String mac : macList) {
					if (fw != null) {
						fw.write(mac);
						fw.write(System.lineSeparator());
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	public static boolean isBanned(String mac) {
		return macList.contains(mac);
	}

	public static boolean ban(Player player, boolean loggedIn) {
		if (noMacFound(player.getMacAddress())) {
			return false;
		}
		macList.add(player.getMacAddress());
		if (loggedIn)
			PlayerHandler.handleLogout(player, false, true);
		save();
		return true;
	}

	
	public static boolean remove(String mac) {
		boolean result = macList.remove(mac);
		return result;
	}
	
	public static void unban(String mac) {
		macList.remove(mac);
		save();
	}
}
