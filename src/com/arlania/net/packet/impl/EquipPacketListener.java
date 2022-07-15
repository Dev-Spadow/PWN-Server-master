package com.arlania.net.packet.impl;

import com.arlania.engine.task.TaskManager;
import com.arlania.engine.task.impl.CivilRainTask;
import com.arlania.engine.task.impl.RainbowObjectTask;
import com.arlania.model.Flag;
import com.arlania.model.GameMode;
import com.arlania.model.Item;
import com.arlania.model.Skill;
import com.arlania.model.Locations.Location;
import com.arlania.model.container.impl.Equipment;
import com.arlania.model.container.impl.Inventory;
import com.arlania.model.definitions.WeaponAnimations;
import com.arlania.model.definitions.WeaponInterfaces;
import com.arlania.model.input.impl.ConvertBloodBag;
import com.arlania.model.input.impl.ConvertTaxBags;
import com.arlania.model.input.impl.NamePreset;
import com.arlania.net.packet.Packet;
import com.arlania.net.packet.PacketListener;
import com.arlania.util.Misc;
import com.arlania.world.content.BonusManager;
import com.arlania.world.content.LootingBag;
import com.arlania.world.content.Sounds;
import com.arlania.world.content.StarterTasks;
import com.arlania.world.content.Sounds.Sound;
import com.arlania.world.content.StarterTasks.StarterTaskData;
import com.arlania.world.content.combat.magic.Autocasting;
import com.arlania.world.content.combat.weapon.CombatSpecial;
import com.arlania.world.content.minigames.impl.Dueling;
import com.arlania.world.content.minigames.impl.Dueling.DuelRule;
import com.arlania.world.entity.impl.player.Player;
import com.arlania.world.content.combat.magic.CombatSpells;

/**
 * This packet listener manages the equip action a player executes when wielding
 * or equipping an item.
 * 
 * @author relex lawl
 */

public class EquipPacketListener implements PacketListener {

	@Override
	public void handleMessage(Player player, Packet packet) {
		if (player.getConstitution() <= 0)
			return;
		int id = packet.readShort();
		int slot = packet.readShortA();
		int interfaceId = packet.readShortA();
		if(id == 6500) {
			LootingBag.withdrawLootingBag(player);
			return;
		}
		if(id == 10835) {
			int totalTaxBags = player.getInventory().getAmount(10835);
			int totalBloodBagsAllowed = totalTaxBags / 250;
			if(totalBloodBagsAllowed < 1) {
				player.getPA().sendMessage("You need at least 250 tax bags to convert.");
				return;
			}
			player.setInputHandling(new ConvertTaxBags());
			player.getPacketSender().sendEnterInputPrompt("How many would you like to convert: (1-"+totalBloodBagsAllowed+")");
			/*final int size = player.getInventory().getAmount(10835);
			player.getInventory().delete(new Item(10835, size));
			player.getInventory().add(new Item(17750, size / 250));
			player.getPacketSender().sendMessage("You exchange " + Misc.format(size) + " Taxbags For " + Misc.format(player.getInventory().getAmount(17750)) + " Blood bags");*/
			//player.getPacketSender().sendMessage("BLa1");
			return;
		}
		if(id == 17750) {
			int totalBloodBags = player.getInventory().getAmount(17750);
			player.setInputHandling(new ConvertBloodBag());
			player.getPacketSender().sendEnterInputPrompt("How many would you like to convert: (1-"+totalBloodBags+")");
			return;
		}
		if (player.getInterfaceId() > 0 && player.getInterfaceId() != 21172 /* EQUIP SCREEN */) {
			player.getPacketSender().sendInterfaceRemoval();
			// return;
		}
		switch (interfaceId) {
		case Inventory.INTERFACE_ID:
			/*
			 * Making sure slot is valid.
			 */
			if (slot >= 0 && slot <= 28) {
				Item item = player.getInventory().getItems()[slot].copy();
				if (!player.getInventory().contains(item.getId()))
					return;
				/*
				 * Making sure item exists and that id is consistent.
				 */
				if (item != null && id == item.getId()) {
					for (Skill skill : Skill.values()) {
						if (skill == Skill.ARCHEOLOGY)
							continue;
						if (item.getDefinition().getRequirement()[skill.ordinal()] > player.getSkillManager()
								.getMaxLevel(skill)) {
							StringBuilder vowel = new StringBuilder();
							if (skill.getName().startsWith("a") || skill.getName().startsWith("e")
									|| skill.getName().startsWith("i") || skill.getName().startsWith("o")
									|| skill.getName().startsWith("u")) {
								vowel.append("an ");
							} else {
								vowel.append("a ");
							}
							player.getPacketSender().sendMessage("You need " + vowel.toString()
									+ Misc.formatText(skill.getName()) + " level of at least "
									+ item.getDefinition().getRequirement()[skill.ordinal()] + " to wear this.");
							return;
						}
					}
					int equipmentSlot = item.getDefinition().getEquipmentSlot();
					Item equipItem = player.getEquipment().forSlot(equipmentSlot).copy();
					if (player.getLocation() == Location.DUEL_ARENA) {
						for (int i = 10; i < player.getDueling().selectedDuelRules.length; i++) {
							if (player.getDueling().selectedDuelRules[i]) {
								DuelRule duelRule = DuelRule.forId(i);
								if (equipmentSlot == duelRule.getEquipmentSlot()
										|| duelRule == Dueling.DuelRule.NO_SHIELD
												&& item.getDefinition().isTwoHanded()) {
									player.getPacketSender().sendMessage(
											"The rules that were set do not allow this item to be equipped.");
									return;
								}
							}
						}
						if (player.getDueling().selectedDuelRules[DuelRule.LOCK_WEAPON.ordinal()]) {
							if (equipmentSlot == Equipment.WEAPON_SLOT || item.getDefinition().isTwoHanded()) {
								player.getPacketSender().sendMessage("Weapons have been locked during this duel!");
								return;
							}
						}
					}
					if (player.hasStaffOfLightEffect()
							&& equipItem.getDefinition().getName().toLowerCase().contains("staff of light")) {
						player.setStaffOfLightEffect(-1);
						player.getPacketSender()
								.sendMessage("You feel the spirit of the Staff of Light begin to fade away...");
					}
					/*if (item.getId() == 19886 && player.isDoubleDropsActive()) {
                        player.getPacketSender().sendMessage("Tryna do it the other way around? nah won't work boi");
                        return;
                    }*/
					
					if(item.getId() == 6570) {
						//StarterTasks.finishTask(player, StarterTaskData.WEAR_A_FIRE_CAPE);
					}
					
					if(item.getId() == 6482) {
						TaskManager.submit(new CivilRainTask(player));
					}
					
					if(item.getId() == 19943) {
						if(player.getSkillManager().getTotalLevel() < 2475) {
							player.sendMessage("@red@You are not maxed, therefore u cannot wear this cool cape :C");
							return;
						}
					}
					
					if(item.getId() == 5092 && player.getGameMode() != GameMode.IRONMAN && player.getGameMode() != GameMode.HARDCORE_IRONMAN && player.getGameMode() != GameMode.GROUP_IRONMAN) {
						player.sendMessage("@red@You need to be an Ironman or ultimate ironman to wear this cape.");
						return;
					}
					
					/*if(item.getId() == 3991) {
						TaskManager.submit(new RainbowObjectTask(player));
					}*/
					
					if (equipItem.getDefinition().isStackable() && equipItem.getId() == item.getId()) {
						int amount = equipItem.getAmount() + item.getAmount() <= Integer.MAX_VALUE
								? equipItem.getAmount() + item.getAmount()
								: Integer.MAX_VALUE;
						player.getInventory().delete(item);
						player.getEquipment().getItems()[equipmentSlot].setAmount(amount);
						equipItem.setAmount(amount);
						player.getEquipment().refreshItems();
					} else {
						if (item.getDefinition().isTwoHanded()
								&& item.getDefinition().getEquipmentSlot() == Equipment.WEAPON_SLOT) {
							int slotsNeeded = 0;
							if (player.getEquipment().isSlotOccupied(Equipment.SHIELD_SLOT)
									&& player.getEquipment().isSlotOccupied(Equipment.WEAPON_SLOT)) {
								slotsNeeded++;
							}
							if (player.getInventory().getFreeSlots() >= slotsNeeded) {
								Item shield = player.getEquipment().getItems()[Equipment.SHIELD_SLOT];
								player.getInventory().add(shield);
								player.getInventory().delete(item);
								player.getEquipment().delete(shield);
								player.getInventory().add(equipItem);
								player.getInventory().add(shield);
								player.getEquipment().setItem(equipmentSlot, item);
							} else {
								player.getInventory().full();
								return;
							}
						} else if (equipmentSlot == Equipment.SHIELD_SLOT
								&& player.getEquipment().getItems()[Equipment.WEAPON_SLOT].getDefinition()
										.isTwoHanded()) {
							player.getInventory().setItem(slot,
									player.getEquipment().getItems()[Equipment.WEAPON_SLOT]);
							player.getEquipment().setItem(Equipment.WEAPON_SLOT, new Item(-1));
							player.getEquipment().setItem(Equipment.SHIELD_SLOT, item);
							resetWeapon(player);
						} else {
							if (item.getDefinition().getEquipmentSlot() == equipItem.getDefinition().getEquipmentSlot()
									&& equipItem.getId() != -1) {
								if (player.getInventory().contains(equipItem.getId())) {
									player.getInventory().delete(item);
									player.getInventory().add(equipItem);
								} else
									player.getInventory().setItem(slot, equipItem);
								player.getEquipment().setItem(equipmentSlot, item);
							} else {
								player.getInventory().setItem(slot, new Item(-1, 0));
								player.getEquipment().setItem(item.getDefinition().getEquipmentSlot(), item);
							}
						}
					}
					if (equipmentSlot == Equipment.WEAPON_SLOT) {
						resetWeapon(player);
					} else if (equipmentSlot == Equipment.RING_SLOT && item.getId() == 2570) {
						player.getPacketSender().sendMessage(
								"<img=10> <col=996633>Warning! The Ring of Life special effect does not work in the Wilderness or")
								.sendMessage("<col=996633> Duel Arena.");
					}

					if (player.getEquipment().get(Equipment.WEAPON_SLOT).getId() != 4153) {
						player.getCombatBuilder().cooldown(false);
					}

					player.setCastSpell(null);
					BonusManager.update(player);
					player.getEquipment().refreshItems();
					player.getInventory().refreshItems();
					player.getUpdateFlag().flag(Flag.APPEARANCE);
					Sounds.sendSound(player, Sound.EQUIP_ITEM);
				}
			}
			break;
		}
	}

	public static void resetWeapon(Player player) {
		Item weapon = player.getEquipment().get(Equipment.WEAPON_SLOT);
		WeaponInterfaces.assign(player, weapon);
		WeaponAnimations.assign(player, weapon);

		if (weapon.getId() == 11605) {
			player.setAutocastSpell(CombatSpells.SEISMIC.getSpell());
		} else if(weapon.getId() == 11609) {
			player.setAutocastSpell(CombatSpells.DEATHBLUE.getSpell());
		} else if(weapon.getId() == 13867) {
			player.setAutocastSpell(CombatSpells.SHADOW_BARRAGE.getSpell());
		} else if(weapon.getId() == 3951) {
			player.setAutocastSpell(CombatSpells.BATTLESTAFF.getSpell());
		} else if(weapon.getId() == 5129) {
			player.setAutocastSpell(CombatSpells.DRAGONLAVA.getSpell());
		} else if(weapon.getId() == 5179) {
			player.setAutocastSpell(CombatSpells.HELLFIRE.getSpell());
		} else if(weapon.getId() == 3911) {
			player.setAutocastSpell(CombatSpells.PURPLESTAFF.getSpell());
		} else {
			if (player.getAutocastSpell() != null || player.isAutocast()) {
				Autocasting.resetAutocast(player, true);
				player.getPacketSender().sendMessage("Autocast spell cleared.");
			}
		}
		player.setSpecialActivated(false);
		player.getPacketSender().sendSpriteChange(41006, 945);
		CombatSpecial.updateBar(player);
	}

	public static final int OPCODE = 41;
}