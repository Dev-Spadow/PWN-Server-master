package com.arlania.world.content;

import com.arlania.model.Animation;
import com.arlania.model.GameObject;
import com.arlania.model.Position;
import com.arlania.util.Misc;
import com.arlania.util.Stopwatch;
import com.arlania.world.World;
import com.arlania.world.entity.impl.player.Player;

public class ShootingStar {

	private static final int TIME = 1000000;
	public static final int MAXIMUM_MINING_AMOUNT = 10000;
	
	private static Stopwatch timer = new Stopwatch().reset();
	public static CrashedStar CRASHED_STAR = null;
	private static LocationData LAST_LOCATION = null;
	
	public static class CrashedStar {
		
		public CrashedStar(GameObject starObject, LocationData starLocation) {
			this.starObject = starObject;
			this.starLocation = starLocation;
		}
		
		private GameObject starObject;
		private LocationData starLocation;
		
		public GameObject getStarObject() {
			return starObject;
		}
		
		public LocationData getStarLocation() {
			return starLocation;
		}
	}

	public static enum LocationData {
		LOCATION_1(new Position(3311, 4124), "<col=30043e>At ::Afk", "");
		
		private LocationData(Position spawnPos, String clue, String playerPanelFrame) {
			this.spawnPos = spawnPos;
			this.clue = clue;
			this.playerPanelFrame = playerPanelFrame;
		}

		private Position spawnPos;
		private String clue;
		public String playerPanelFrame;
	}
	public static LocationData getLocation() {
		return LAST_LOCATION;
	}

	public static LocationData getRandom() {
		LocationData star = LocationData.values()[Misc.getRandom(LocationData.values().length - 1)];
		return star;
	}

	public static void sequence() {
		if(CRASHED_STAR == null) {
			if(timer.elapsed(TIME)) {
				LocationData locationData = getRandom();
				if(LAST_LOCATION != null) {
					if(locationData == LAST_LOCATION) {
						locationData = getRandom();
					}
				}
				LAST_LOCATION = locationData;
				CRASHED_STAR = new CrashedStar(new GameObject(38660, locationData.spawnPos), locationData);
				CustomObjects.spawnGlobalObject(CRASHED_STAR.starObject);
				World.sendMessage("<shad=960fc3><img=453>[Afk]</shad>@bla@: Afk star has respawned "+locationData.clue+"");
				World.getPlayers().forEach(p -> p.getPacketSender().sendString(26623, "@or2@Crashed star: @gre@"+ShootingStar.CRASHED_STAR.getStarLocation().playerPanelFrame+""));
				timer.reset();
			}
		} else {
			if(CRASHED_STAR.starObject.getPickAmount() >= MAXIMUM_MINING_AMOUNT) {
				despawn(false);
				timer.reset();
			}
		}
	}

	public static void despawn(boolean respawn) {
		if(respawn) {
			timer.reset(0);
		} else {
			timer.reset();
		}
		if(CRASHED_STAR != null) {
			for(Player p : World.getPlayers()) {
				if(p == null) {
					continue;
				}
				p.getPacketSender().sendString(26623, "@or2@Crashed star: @gre@N/A ");
				if(p.getInteractingObject() != null && p.getInteractingObject().getId() == CRASHED_STAR.starObject.getId()) {
					p.performAnimation(new Animation(65535));
					p.getPacketSender().sendClientRightClickRemoval();
					p.getSkillManager().stopSkilling();
					p.getPacketSender().sendMessage("The star has been fully mined.");
				}
			}
			CustomObjects.deleteGlobalObject(CRASHED_STAR.starObject);
			CRASHED_STAR = null;
		}
	}
}

