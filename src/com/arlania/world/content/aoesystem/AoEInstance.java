package com.arlania.world.content.aoesystem;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import com.arlania.GameSettings;
import com.arlania.engine.task.Task;
import com.arlania.engine.task.TaskManager;
import com.arlania.model.Position;
import com.arlania.model.container.impl.Equipment;
import com.arlania.util.Misc;
import com.arlania.world.World;
import com.arlania.world.content.dialogue.DialogueManager;
import com.arlania.world.content.dialogue.impl.AoEInstanceDialogue;
import com.arlania.world.content.skill.impl.summoning.BossPets.BossPet;
import com.arlania.world.entity.Entity;
import com.arlania.world.entity.impl.npc.InstancedNpc;
import com.arlania.world.entity.impl.npc.NPC;
import com.arlania.world.entity.impl.player.Player;

public class AoEInstance {

	private Player player;
	private int npcId;
	private int index = 8;

	private int timer = 120 * 360;

	private boolean respawning = false;
	private ArrayList<InstancedNpc> npcs = new ArrayList<InstancedNpc>();
	
	public LocalDateTime time = LocalDateTime.MIN;

	public AoEInstance(Player player) {
		this.player = player;
		Player.inAOEInstance = true;
		player.setAoEInstance(this);
		this.npcId = player.getAoENpc();
		index = 60 + (player.getIndex() * 4);
	}

	public void respawn() {
		respawning = true;
		player.moveTo(new Position(3041, 2901, index));
	}
	
	public String getTime() {
		LocalDateTime localTime = LocalDateTime.now();
		long minutes = localTime.until(time, ChronoUnit.MINUTES);
		long seconds = localTime.until(time, ChronoUnit.SECONDS);
		
		return minutes % 60 + ":" + seconds % 60;
	}

	public void start() {
		for (NPC npc : World.getNpcs()) {
			if (npc != null) {
				//Ignore any pets in the area cause we don't wanna affect them.
				BossPet pet = BossPet.forSpawnId(npc.getId());
				if(pet != null) {
					continue;
				}
				
				Position npcPos = npc.getPosition();
				if (inArea(npc) && npcPos.getZ() == index) {
					World.deregister(npc);
				}
			}
		}
		player.moveTo(new Position(3040, 2918, index));
		Position pos = new Position(0, 0, 0);
		int startX = 3034;
		int startY = 2917;
		for (int i = 0; i <= 15; i++) {
			pos = new Position(startX, startY, index);
			InstancedNpc npc = new InstancedNpc(npcId, pos);
			npcs.add(npc);
			npc.configure(player, 1);
			npc.spawnAndMonitor();
			if (startY == 2920) {
				startX ++;
				startY = 2917;
			}
			startY++;
		}
		runTimer();
	}

	private boolean inArea(Entity entity) {
		int x = entity.getPosition().getX();
		int y = entity.getPosition().getY();
//3041, 2901
		if (x >= 3031 && x <= 2913) {
			if (y >= 3050 && y <= 2924)
				return true;
		}
		return false;
	}

	private boolean isInAoeArea(Position pos) {
		if (pos.isWithinBounds(3050, 2924, 3031, 2913)) {
			return true;
		} else {
			return false;
		}
	}

	public boolean getRespawning() {
		return respawning;
	}
	
	public void setRespawning(boolean respawning) {
		this.respawning = respawning;
	}
	
	private void runTimer() {
		final long startTimer = System.nanoTime();
		TaskManager.submit(new Task(2, player, false) {
			int tick = 0;

			@Override
			public void execute() {
				
				player.getPacketSender().sendString(59803, getTime());
				
				if (player.getCombatBuilder().isAttacking()) {
					tick = 0;
				} else {
					tick++;
				}

				if (!isInAoeArea(player.getPosition())) {
					Player.inAOEInstance = false;
					time = LocalDateTime.MIN;
					player.sendParallellInterfaceVisibility(59800, false);
					player.sendMessage("@red@Your AoE instance has reset.");
					player.setAoeNpc(-1);
					player.setAoEInstance(null);
					stop();
				}

				final long elapsedTime = System.nanoTime() - startTimer;
				double seconds = (double) elapsedTime / 1_000_000_000.0;
				if ((int) seconds >= timer) {
					player.moveTo(GameSettings.DEFAULT_POSITION);
					Player.inAOEInstance = false;
					time = LocalDateTime.MIN;
					player.sendParallellInterfaceVisibility(59800, false);
					player.sendMessage("@red@Your instanced AoE session has ended!");
					player.setAoeNpc(-1);
					player.setAoEInstance(null);
					stop();
					return;
				}
			}
			
			@Override
			public void stop() {
				super.stop();
				for (NPC npc : World.getNpcs()) {
					if (npc != null) {
						//Ignore any pets in the area cause we don't wanna affect them.
						BossPet pet = BossPet.forSpawnId(npc.getId());
						if(pet != null) {
							continue;
						}
						
						Position npcPos = npc.getPosition();
						if (inArea(npc) && npcPos.getZ() == index) {
							World.deregister(npc);
						}
					}
				}
			}
		});
	}

	public static boolean checkWeapon(Player p) {
		AOEWeaponData aoeData = AOESystem.getSingleton()
				.getAOEData(p.getEquipment().get(Equipment.WEAPON_SLOT).getId());

		if (aoeData != null && aoeData.getRadius() > 0)
			return true;
		else
			return true;
	}

	private Position getRandomPos() {
		return new Position(Misc.inclusiveRandom(3035, 3048), Misc.inclusiveRandom(2899, 2902), index);
	}

	public static void requestInstance(Player player, String text) {
		System.out.println(text.toLowerCase());
		switch (text.toLowerCase()) {
		case "frost dragons":
		case "frost dragon":
			if (player.getInventory().getAmount(5606) < 1 ) {
				player.sendMessage("@red@You need " + Misc.format(5) + " Instance token to start an instance");
				player.getPacketSender().sendInterfaceRemoval();
				return;
			}
			DialogueManager.start(player, AoEInstanceDialogue.confirmPayment(player, 51, 5));
			break;
		case "sirenic ogre":
		case "sirenic ogres":
			if (player.getInventory().getAmount(5606) < 1 ) {
				player.sendMessage("@red@You need " + Misc.format(5) + " Instance token to start an instance");
				player.getPacketSender().sendInterfaceRemoval();
				return;
			}
			DialogueManager.start(player, AoEInstanceDialogue.confirmPayment(player, 2783, 5));
			break;
		case "hercule":
		case "hercules":
			if (player.getInventory().getAmount(5606) < 1 ) {
				player.sendMessage("@red@You need " + Misc.format(5) + " Instance token to start an instance");
				player.getPacketSender().sendInterfaceRemoval();
				return;
			}
			DialogueManager.start(player, AoEInstanceDialogue.confirmPayment(player, 17, 5));
			break;
		case "crash":
		case "crash bandicoot":
			if (player.getInventory().getAmount(5606) < 1 ) {
				player.sendMessage("@red@You need " + Misc.format(5) + " Instance token to start an instance");
				player.getPacketSender().sendInterfaceRemoval();
				return;
			}
			DialogueManager.start(player, AoEInstanceDialogue.confirmPayment(player, 12839, 5));
			break;
		case "charizard":
		case "chari":
			if (player.getInventory().getAmount(5606) < 1 ) {
				player.sendMessage("@red@You need " + Misc.format(5) + " Instance token to start an instance");
				player.getPacketSender().sendInterfaceRemoval();
				return;
			}
			DialogueManager.start(player, AoEInstanceDialogue.confirmPayment(player, 1982, 5));
			break;
		case "jinis":
		case "jini":
			if (player.getInventory().getAmount(5606) < 1 ) {
				player.sendMessage("@red@You need " + Misc.format(5) + " Instance token to start an instance");
				player.getPacketSender().sendInterfaceRemoval();
				return;
			}
			DialogueManager.start(player, AoEInstanceDialogue.confirmPayment(player, 9994, 5));
			break;
		case "godzilla":
		case "zilla":
			if (player.getInventory().getAmount(5606) < 1 ) {
				player.sendMessage("@red@You need " + Misc.format(5) + " Instance token to start an instance");
				player.getPacketSender().sendInterfaceRemoval();
				return;
			}
			DialogueManager.start(player, AoEInstanceDialogue.confirmPayment(player, 9932, 5));
			break;
		case "bloated":
		case "bloated infernal":
			if (player.getInventory().getAmount(5606) < 1 ) {
				player.sendMessage("@red@You need " + Misc.format(5) + " Instance token to start an instance");
				player.getPacketSender().sendInterfaceRemoval();
				return;
			}
			DialogueManager.start(player, AoEInstanceDialogue.confirmPayment(player, 1999, 5));
			break;	
		case "hade":
		case "hades":
			if (player.getInventory().getAmount(5606) < 1 ) {
				player.sendMessage("@red@You need " + Misc.format(5) + " Instance token to start an instance");
				player.getPacketSender().sendInterfaceRemoval();
				return;
			}
			DialogueManager.start(player, AoEInstanceDialogue.confirmPayment(player, 16, 5));
			break;	
		case "vader":
		case "darth vader":
			if (player.getInventory().getAmount(5606) < 1 ) {
				player.sendMessage("@red@You need " + Misc.format(5) + " Instance token to start an instance");
				player.getPacketSender().sendInterfaceRemoval();
				return;
			}
			DialogueManager.start(player, AoEInstanceDialogue.confirmPayment(player, 11, 5));
			break;	
		case "dantes satan":
		case "satan":
		case "abbadon":
			if (player.getInventory().getAmount(5606) < 1 ) {
				player.sendMessage("@red@You need " + Misc.format(5) + " Instance token to start an instance");
				player.getPacketSender().sendInterfaceRemoval();
				return;
			}
			DialogueManager.start(player, AoEInstanceDialogue.confirmPayment(player, 6303, 5));
			break;	
			
		case "rick":
		case "ric":
		case "rik":
			if (player.getInventory().getAmount(5606) < 1 ) {
				player.sendMessage("@red@You need " + Misc.format(5) + " Instance token to start an instance");
				player.getPacketSender().sendInterfaceRemoval();
				return;
			}
			DialogueManager.start(player, AoEInstanceDialogue.confirmPayment(player, 9273, 5));
			break;	
			
			
		case "kong":
		case "king kong":
		case "kin kong":
			if (player.getInventory().getAmount(5606) < 1 ) {
				player.sendMessage("@red@You need " + Misc.format(5) + " Instance token to start an instance");
				player.getPacketSender().sendInterfaceRemoval();
				return;
			}
			DialogueManager.start(player, AoEInstanceDialogue.confirmPayment(player, 9903, 5));
			break;	
			
			
		case "lucid":
		case "lucid dragons":
		case "lucid dragon":
		case "ld":
			if (player.getInventory().getAmount(5606) < 1 ) {
				player.sendMessage("@red@You need " + Misc.format(5) + " Instance token to start an instance");
				player.getPacketSender().sendInterfaceRemoval();
				return;
			}
			DialogueManager.start(player, AoEInstanceDialogue.confirmPayment(player, 9247, 5));
			break;	
			
			
		case "hulk":
		case "hul":
		case "huk":
			if (player.getInventory().getAmount(5606) < 1 ) {
				player.sendMessage("@red@You need " + Misc.format(5) + " Instance token to start an instance");
				player.getPacketSender().sendInterfaceRemoval();
				return;
			}
			DialogueManager.start(player, AoEInstanceDialogue.confirmPayment(player, 8493, 5));
			break;	
			
			
			
		case "dark wizards":
		case "dark wizard":
		case "darkwizard":
		case "darkwizards":
		case "dw":
		case "dark":
			if (player.getInventory().getAmount(5606) < 1 ) {
				player.sendMessage("@red@You need " + Misc.format(5) + " Instance token to start an instance");
				player.getPacketSender().sendInterfaceRemoval();
				return;
			}
			DialogueManager.start(player, AoEInstanceDialogue.confirmPayment(player, 9203, 5));
			break;	
			
		case "pyros":
		case "heated pyros":
		case "heated pyro":
		case "heated":
			if (player.getInventory().getAmount(5606) < 1 ) {
				player.sendMessage("@red@You need " + Misc.format(5) + " Instance token to start an instance");
				player.getPacketSender().sendInterfaceRemoval();
				return;
			}
			DialogueManager.start(player, AoEInstanceDialogue.confirmPayment(player, 172, 5));
			break;	
			
			
		case "wyrm":
		case "purple wyrm":
		case "purple":
		case "darkpurple wyrm":
			if (player.getInventory().getAmount(5606) < 1 ) {
				player.sendMessage("@red@You need " + Misc.format(5) + " Instance token to start an instance");
				player.getPacketSender().sendInterfaceRemoval();
				return;
			}
			DialogueManager.start(player, AoEInstanceDialogue.confirmPayment(player, 9935, 5));
			break;	
			
			
		case "trinity":
		case "trinity killer":
			if (player.getInventory().getAmount(5606) < 1 ) {
				player.sendMessage("@red@You need " + Misc.format(5) + " Instance token to start an instance");
				player.getPacketSender().sendInterfaceRemoval();
				return;
			}
			DialogueManager.start(player, AoEInstanceDialogue.confirmPayment(player, 170, 5));
			break;	
			
			
		case "herb":
		case "herbal rogue":
		case "herbal":
		case "rogue":
			if (player.getInventory().getAmount(5606) < 1 ) {
				player.sendMessage("@red@You need " + Misc.format(5) + " Instance token to start an instance");
				player.getPacketSender().sendInterfaceRemoval();
				return;
			}
			DialogueManager.start(player, AoEInstanceDialogue.confirmPayment(player, 219, 5));
			break;	
			
		case "ex":
		case "exo":
		case "exoden":
		case "exodn":
			if (player.getInventory().getAmount(5606) < 1 ) {
				player.sendMessage("@red@You need " + Misc.format(5) + " Instance token to start an instance");
				player.getPacketSender().sendInterfaceRemoval();
				return;
			}
			DialogueManager.start(player, AoEInstanceDialogue.confirmPayment(player, 12239, 5));
			break;	
			
		case "storm":
		case "breaker":
		case "storm breaker":
		case "strm breaker":
			if (player.getInventory().getAmount(5606) < 1 ) {
				player.sendMessage("@red@You need " + Misc.format(5) + " Instance token to start an instance");
				player.getPacketSender().sendInterfaceRemoval();
				return;
			}
			DialogueManager.start(player, AoEInstanceDialogue.confirmPayment(player, 527, 5));
			break;	
			
		case "apollo":
		case "apollo ranger":
		case "apollos":
		case "apollo rangers":
			if (player.getInventory().getAmount(5606) < 1 ) {
				player.sendMessage("@red@You need " + Misc.format(5) + " Instance token to start an instance");
				player.getPacketSender().sendInterfaceRemoval();
				return;
			}
			DialogueManager.start(player, AoEInstanceDialogue.confirmPayment(player, 1684, 5));
			break;	
			
			
			
		case "noxious":
		case "trolls":
		case "noxious troll":
		case "noxious trolls":
		case "nox troll":
			if (player.getInventory().getAmount(5606) < 1 ) {
				player.sendMessage("@red@You need " + Misc.format(5) + " Instance token to start an instance");
				player.getPacketSender().sendInterfaceRemoval();
				return;
			}
			DialogueManager.start(player, AoEInstanceDialogue.confirmPayment(player, 5957, 5));
			break;	
			
		case "azazel":
		case "azazel beasts":
		case "azaz":
		case "azazel beast":
			if (player.getInventory().getAmount(5606) < 1 ) {
				player.sendMessage("@red@You need " + Misc.format(5) + " Instance token to start an instance");
				player.getPacketSender().sendInterfaceRemoval();
				return;
			}
			DialogueManager.start(player, AoEInstanceDialogue.confirmPayment(player, 5958, 5));
			break;	
			
			
		case "rav":
		case "ravana":
			if (player.getInventory().getAmount(5606) < 1 ) {
				player.sendMessage("@red@You need " + Misc.format(5) + " Instance token to start an instance");
				player.getPacketSender().sendInterfaceRemoval();
				return;
			}
			DialogueManager.start(player, AoEInstanceDialogue.confirmPayment(player, 5959, 5));
			break;	
			
			
		case "lumin":
		case "lumin warriors":
		case "lumin warrior":
		case "luminitous warrior": 
		case "luminitious warriors":
			if (player.getInventory().getAmount(5606) < 1 ) {
				player.sendMessage("@red@You need " + Misc.format(5) + " Instance token to start an instance");
				player.getPacketSender().sendInterfaceRemoval();
				return;
			}
			DialogueManager.start(player, AoEInstanceDialogue.confirmPayment(player, 185, 5));
			break;	
			
			
		case "hellhounds":
		case "custom hellhounds":
		case "hellhound":
		case "hell hounds":
			if (player.getInventory().getAmount(5606) < 1 ) {
				player.sendMessage("@red@You need " + Misc.format(5) + " Instance token to start an instance");
				player.getPacketSender().sendInterfaceRemoval();
				return;
			}
			DialogueManager.start(player, AoEInstanceDialogue.confirmPayment(player, 6311, 5));
			break;	
			
			
			
		case "razorspawn":
		case "razor":
		case "rzorspawn":
		case "razors":
			if (player.getInventory().getAmount(5606) < 1 ) {
				player.sendMessage("@red@You need " + Misc.format(5) + " Instance token to start an instance");
				player.getPacketSender().sendInterfaceRemoval();
				return;
			}
			DialogueManager.start(player, AoEInstanceDialogue.confirmPayment(player, 2907, 5));
			break;	
			
		case "dreamflow":
		case "dreamflow assassins":
		case "dreamflows":
		case "dreamflow assassin":
		case "dreamflow assasin":
			if (player.getInventory().getAmount(5606) < 1 ) {
				player.sendMessage("@red@You need " + Misc.format(5) + " Instance token to start an instance");
				player.getPacketSender().sendInterfaceRemoval();
				return;
			}
			DialogueManager.start(player, AoEInstanceDialogue.confirmPayment(player, 20, 5));
			break;	
			
			
		case "khione":
		case "king khione":
		case "khiones":
		case "king khiones":
			if (player.getInventory().getAmount(5606) < 1 ) {
				player.sendMessage("@red@You need " + Misc.format(5) + " Instance token to start an instance");
				player.getPacketSender().sendInterfaceRemoval();
				return;
			}
			DialogueManager.start(player, AoEInstanceDialogue.confirmPayment(player, 259, 5));
			break;	
			
			
		case "sable":
		case "sable beasts":
		case "sable beast":
			if (player.getInventory().getAmount(5606) < 1 ) {
				player.sendMessage("@red@You need " + Misc.format(5) + " Instance token to start an instance");
				player.getPacketSender().sendInterfaceRemoval();
				return;
			}
			DialogueManager.start(player, AoEInstanceDialogue.confirmPayment(player, 1123, 5));
			break;	
			
			
		case "demi":
		case "gorgon":
		case "demogorgon":
			if (player.getInventory().getAmount(5606) < 1 ) {
				player.sendMessage("@red@You need " + Misc.format(5) + " Instance token to start an instance");
				player.getPacketSender().sendInterfaceRemoval();
				return;
			}
			DialogueManager.start(player, AoEInstanceDialogue.confirmPayment(player, 12835, 5));
			break;	
			
		case "yoshi":
		case "yoshis":
			if (player.getInventory().getAmount(5606) < 1 ) {
				player.sendMessage("@red@You need " + Misc.format(5) + " Instance token to start an instance");
				player.getPacketSender().sendInterfaceRemoval();
				return;
			}
			DialogueManager.start(player, AoEInstanceDialogue.confirmPayment(player, 8548, 5));
			break;	
			
		case "avatar airbender":
		case "avatar the airbender":
		case "avatar":
		case "airbender":
			if (player.getInventory().getAmount(5606) < 1 ) {
				player.sendMessage("@red@You need " + Misc.format(5) + " Instance token to start an instance");
				player.getPacketSender().sendInterfaceRemoval();
				return;
			}
			DialogueManager.start(player, AoEInstanceDialogue.confirmPayment(player, 2264, 5));
			break;	
			
			
		case "lili":
		case "lil":
			if (player.getInventory().getAmount(5606) < 1 ) {
				player.sendMessage("@red@You need " + Misc.format(5) + " Instance token to start an instance");
				player.getPacketSender().sendInterfaceRemoval();
				return;
			}
			DialogueManager.start(player, AoEInstanceDialogue.confirmPayment(player, 11360, 5));
			break;	
			
			
		case "obito":
		case "obito uchiha":
			if (player.getInventory().getAmount(5606) < 1 ) {
				player.sendMessage("@red@You need " + Misc.format(5) + " Instance token to start an instance");
				player.getPacketSender().sendInterfaceRemoval();
				return;
			}
			DialogueManager.start(player, AoEInstanceDialogue.confirmPayment(player, 11383, 5));
			break;	
			
			
		case "uru":
		case "uru isiha":
			if (player.getInventory().getAmount(5606) < 1 ) {
				player.sendMessage("@red@You need " + Misc.format(5) + " Instance token to start an instance");
				player.getPacketSender().sendInterfaceRemoval();
				return;
			}
			DialogueManager.start(player, AoEInstanceDialogue.confirmPayment(player, 11305, 5));
			break;	
			
			
		case "ahri":
		case "ninetails":
			if (player.getInventory().getAmount(5606) < 1 ) {
				player.sendMessage("@red@You need " + Misc.format(5) + " Instance token to start an instance");
				player.getPacketSender().sendInterfaceRemoval();
				return;
			}
			DialogueManager.start(player, AoEInstanceDialogue.confirmPayment(player, 5931, 5));
			break;	
			
		default:
			player.sendMessage("@red@This NPC has not been added. Please suggest it.");
			break;
		}
	}

	public void setTime(int i) {
		timer = 60 * i;
	}
	
	public int getHeight() {
		return index;
	}
}
