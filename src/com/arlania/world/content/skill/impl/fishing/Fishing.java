package com.arlania.world.content.skill.impl.fishing;

import com.arlania.engine.task.Task;
import com.arlania.engine.task.TaskManager;
import com.arlania.model.Animation;
import com.arlania.model.Direction;
import com.arlania.model.GameMode;
import com.arlania.model.Skill;
import com.arlania.model.definitions.ItemDefinition;
import com.arlania.util.Misc;
import com.arlania.world.World;
import com.arlania.world.content.Achievements;
import com.arlania.world.content.StarterTasks;
import com.arlania.world.content.Achievements.AchievementData;
import com.arlania.world.content.StarterTasks.StarterTaskData;
import com.arlania.world.content.dailytasks.DailyTasks;
import com.arlania.world.content.dailytasks.TaskType;
import com.arlania.world.entity.impl.player.Player;

public class Fishing {

	public enum Spot {
		
	    LURE(318, new int[]{6200}, 309, 314, new int[]{90}, true, new int[]{1550, 2120}, 623),//raids fish
		
		CAGE(312, new int[]{377}, 301, -1, new int[]{40}, false, new int[]{2980}, 619),
		
		BIGNET(313, new int[]{353, 341, 363}, 305, -1, new int[]{16, 23, 46}, false, new int[]{780, 1130, 2120}, 620),
		
		SMALLNET(316, new int[]{317, 321}, 303, -1, new int[]{1, 15}, false, new int[]{290, 550}, 621),
		
		MONK_FISH(318, new int[]{7944, 389}, 305, -1, new int[]{62, 81}, false, new int[]{6985, 10122}, 621),
		
		HARPOON(312, new int[]{359, 371}, 311, -1, new int[]{35, 50}, true, new int[]{2530, 5100}, 618),
		
		HARPOON2(313, new int[]{383}, 311, -1, new int[]{76}, true, new int[]{8547}, 618),
		
		BAIT(316, new int[]{327, 345}, 307, 313, new int[]{5, 10}, true, new int[]{650, 980}, 623),
		
		ROCKTAIL(10091, new int[]{15270}, 309, 25, new int[]{91}, false, new int[]{18010}, 623),

		AFKFISH(310, new int[]{15264}, 14101, -1, new int[]{91}, false, new int[]{18010}, 618);

		
		
		int npcId, equipment, bait, anim;
		int[] rawFish, fishingReqs, xp;
		boolean second;
		private Spot(int npcId, int[] rawFish, int equipment, int bait, int[] fishingReqs, boolean second, int[] xp, int anim) {
			this.npcId = npcId;
			this.rawFish = rawFish;
			this.equipment = equipment;
			this.bait = bait;
			this.fishingReqs = fishingReqs;
			this.second = second;
			this.xp = xp;
			this.anim = anim;
		}

		public int getNPCId() {
			return npcId;
		}

		public int[] getRawFish() {
			return rawFish;
		}

		public int getEquipment() {
			return equipment;
		}

		public int getBait() {
			return bait;
		}

		public int[] getLevelReq() {
			return fishingReqs;
		}

		public boolean getSecond() {
			return second;
		}

		public int[] getXp() {
			return xp;
		}

		public int getAnim() {
			return anim;
		}
	}

	public static Spot forSpot(int npcId, boolean secondClick) {
		for (Spot s : Spot.values()) {
			if (secondClick) {
				if (s.getSecond()) {
					if (s.getNPCId() == npcId) {
						if (s != null) {
							return s;
						}
					}
				}
			} else {
				if (s.getNPCId() == npcId && !s.getSecond()) {
					if (s != null) {
						return s;
					}
				}
			}
		}
		return null;
	}

	public static void setupFishing(Player p, Spot s) {
		if(s == null)
			return;
		if(p.getInventory().getFreeSlots() <= 0) {
			p.getPacketSender().sendMessage("You do not have any free inventory space.");
			p.getSkillManager().stopSkilling();
			return;
		}
		if (p.getSkillManager().getCurrentLevel(Skill.FISHING) >= s.getLevelReq()[0]) {
			if (p.getInventory().contains(s.getEquipment())) {
				if (s.getBait() != -1) {
					if (p.getInventory().contains(s.getBait())) {
						startFishing(p, s);
					} else {
						String baitName = ItemDefinition.forId(s.getBait()).getName();
						if(baitName.contains("Feather") || baitName.contains("worm"))
							baitName += "s";
						p.getPacketSender().sendMessage("You need some "+baitName+" to fish here.");
						p.performAnimation(new Animation(65535));
					}
				} else {
					startFishing(p, s);
				}
			} else {
				String def = ItemDefinition.forId(s.getEquipment()).getName().toLowerCase();
				p.getPacketSender().sendMessage("You need "+Misc.anOrA(def)+" "+def+" to fish here.");
			}
		} else {
			p.getPacketSender().sendMessage("You need a fishing level of at least "+s.getLevelReq()[0]+" to fish here.");
		}
	}

	public static void startFishing(final Player p, final Spot s) {
		p.getSkillManager().stopSkilling();
		final int fishIndex = Misc.getRandom(100) >= 70 ? getMax(p, s.fishingReqs) : (getMax(p, s.fishingReqs) != 0 ? getMax(p, s.fishingReqs) - 1 : 0);
		if(p.getInteractingObject() != null && p.getInteractingObject().getId() != 8702)
			p.setDirection(s == Spot.MONK_FISH ? Direction.WEST : Direction.NORTH);
		p.performAnimation(new Animation(s.getAnim()));
		if(Misc.getRandom(25000) == 3) {
			p.getInventory().add(10168, 1);
			World.sendMessage("@blu@<img=10>[FISHING] "+p.getUsername()+" has received an @mag@Owner Box @blu@from Fishing!");
			p.getPacketSender().sendMessage("@red@You have received an Owner Box from Fishing!");
				}
		if(Misc.getRandom(40000) == 3) {
			p.getInventory().add(18338, 1);
			World.sendMessage("@blu@<img=10>[FISHING] "+p.getUsername()+" has received a @mag@goodiebag @blu@from fishing!");
			p.getPacketSender().sendMessage("@red@You have received a goodiebag from fishing!");
			}
		if(Misc.getRandom(10000) == 3) {
			p.getInventory().add(10478, 1);
			World.sendMessage("@blu@<img=10>[FISHING] "+p.getUsername()+" has received a @mag@Diamond chest @blu@from fishing!");
			p.getPacketSender().sendMessage("@red@You have received a Diamond chest from fishing!");
		}	
		if(Misc.getRandom(16000) == 3) {
			p.getInventory().add(13320, 1);
			World.sendMessage("@blu@<img=10>[FISHING] "+p.getUsername()+" has received the Heron pet!");
			p.getPacketSender().sendMessage("@red@You have received a skilling pet!");
		}

		p.setCurrentTask(new Task(2, p, false) {
			int cycle = 0, reqCycle = Fishing.getDelay(s.getLevelReq()[fishIndex]);
			@Override
			public void execute() {
				if(p.getInventory().getFreeSlots() == 0) {
					p.getPacketSender().sendMessage("You have run out of inventory space.");
					stop();
					return;
				}
				if(!p.getInventory().contains(s.getBait())) {
					stop();
					return;
				}
				cycle++;
				p.performAnimation(new Animation(s.getAnim()));
				if (cycle >= Misc.getRandom(1) + reqCycle) {
					String def = ItemDefinition.forId(s.getRawFish()[fishIndex]).getName();
					if(def.endsWith("s"))
						def = def.substring(0, def.length()-1);
					p.getPacketSender().sendMessage("You catch "+Misc.anOrA(def)+" "+def.toLowerCase().replace("_", " ")+".");
					if (s.getBait() != -1)
						p.getInventory().delete(s.getBait(), 1);
					
						p.getInventory().add(s.getRawFish()[fishIndex], 1);
                    DailyTasks.INSTANCE.updateTaskProgress(TaskType.FISHING, p, s.getRawFish()[fishIndex], 1);

					
					if(s.getRawFish()[fishIndex] == 331) {
						//Achievements.finishAchievement(p, AchievementData.FISH_A_SALMON);
					} else if(s.getRawFish()[fishIndex] == 15270) {
					} else if(s.getRawFish()[fishIndex] == 383) {
						StarterTasks.doProgress(p, StarterTaskData.FISH_250_SHARKS);
					}
					p.getSkillManager().addExperience(Skill.FISHING, s.getXp()[fishIndex]);
					p.getInventory().add(10835, 1);
				//	p.getPacketSender().sendMessage("@red@You've just received " + 10 + " skilling points.");
					p.getPointsHandler().incrementSkillPoints(10);
					setupFishing(p, s);
					setEventRunning(false);
				}
			}
			@Override
			public void stop() {
				setEventRunning(false);
				p.performAnimation(new Animation(65535));
			}
		});
		
		TaskManager.submit(p.getCurrentTask());
	}

	public static int getMax(Player p, int[] reqs) {
		int tempInt = -1;
		for (int i : reqs) {
			if (p.getSkillManager().getCurrentLevel(Skill.FISHING) >= i) {
				tempInt++;
			}
		}
		return tempInt;
	}

	private static int getDelay(int req) {
		int timer = 1;
		timer += (int) req * 0.08;
		return timer;
	}

}
