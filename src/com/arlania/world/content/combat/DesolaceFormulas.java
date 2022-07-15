package com.arlania.world.content.combat;

import java.util.Optional;

import com.arlania.GameLoader;
import com.arlania.model.Graphic;
import com.arlania.model.PlayerRights;
import com.arlania.model.Skill;
import com.arlania.model.Locations.Location;
import com.arlania.model.container.impl.Equipment;
import com.arlania.model.definitions.ItemDefinition;
import com.arlania.util.Misc;
import com.arlania.util.RandomUtility;
import com.arlania.world.content.PetPerk;
import com.arlania.world.content.WellOfGoodwill;
import com.arlania.world.content.combat.effect.EquipmentBonus;
import com.arlania.world.content.combat.magic.CombatSpell;
import com.arlania.world.content.combat.prayer.CurseHandler;
import com.arlania.world.content.combat.prayer.PrayerHandler;
import com.arlania.world.content.combat.range.CombatRangedAmmo.RangedWeaponData;
import com.arlania.world.content.combat.strategy.FourthStrategy;
import com.arlania.world.content.combat.weapon.FightType;
import com.arlania.world.content.damage_cap.DamageCapHandler;
//import com.arlania.world.content.serverperks.GlobalPerks;
import com.arlania.world.content.skill.SkillManager;
import com.arlania.world.content.skill.impl.summoning.Familiar;
import com.arlania.world.entity.impl.Character;
import com.arlania.world.entity.impl.npc.NPC;
import com.arlania.world.entity.impl.player.Player;

public class DesolaceFormulas {

    /*==============================================================================*/
    /*===================================MELEE=====================================*/


    public static int calculateMaxMeleeHit(Character entity, Character victim) {
        double maxHit = 0;
        if (entity.isNpc()) {
            NPC npc = (NPC) entity;
            maxHit = npc.getDefinition().getMaxHit();
            if (npc.getStrengthWeakened()[0]) {
                maxHit -= (int) ((0.10) * (maxHit));
            } else if (npc.getStrengthWeakened()[1]) {
                maxHit -= (int) ((0.20) * (maxHit));
            } else if (npc.getStrengthWeakened()[2]) {
                maxHit -= (int) ((0.30) * (maxHit));
            }

            /** CUSTOM NPCS **/
            if (npc.getId() == 2026) { //Dharok the wretched
                maxHit += (int) ((int) (npc.getDefaultConstitution() - npc.getConstitution()) * 0.2);
            }
        } else {
            Player plr = (Player) entity;
            int aura = RandomUtility.exclusiveRandom(99);
            double base = 0;
            double effective = getEffectiveStr(plr);
            double specialBonus = 1;
            if (plr.isSpecialActivated()) {
                specialBonus = plr.getCombatSpecial().getStrengthBonus();
            }
            double strengthBonus = plr.getBonusManager().getOtherBonus()[0];
            base = (13 + effective + (strengthBonus / 8) + ((effective * strengthBonus) / 65)) / 11;
//            if (plr.getEquipment().getItems()[3].getId() == 5069 && plr.getEquipment().getItems()[5].getId() == 6505) 
//            	base = (base * 1.5);
            if (plr.getEquipment().getItems()[2].getId() == 15445 && aura <= 24) {
            	base *= 1.5;
            }
            if (plr.getEquipment().getItems()[2].getId() == 15446 && aura <= 24) {
            	base *= 2;
            }
            if (plr.getEquipment().getItems()[2].getId() == 15447 && aura <= 24) {
            	base *= 2.5;
            }
            if (plr.getEquipment().getItems()[2].getId() == 15448 && aura <= 19) {
            	base *= 3;
            }
            if (plr.getEquipment().getItems()[2].getId() == 15449 && aura <= 19) {
            	base *= 3.5;
            }
            if (plr.getEquipment().getItems()[3].getId() == 4718 &&
                    plr.getEquipment().getItems()[0].getId() == 4716 &&
                    plr.getEquipment().getItems()[4].getId() == 4720 &&
                    plr.getEquipment().getItems()[7].getId() == 4722)
                base += ((plr.getSkillManager().getMaxLevel(Skill.CONSTITUTION) - plr.getConstitution()) * .045) + 1;

            if (plr.getEquipment().getItems()[0].getId() == 15667
                    && plr.getEquipment().getItems()[1].getId() == 15664
                    && plr.getEquipment().getItems()[3].getId() == 15668
                    && plr.getEquipment().getItems()[4].getId() == 15662
                    && plr.getEquipment().getItems()[7].getId() == 15663
                    && plr.getEquipment().getItems()[9].getId() == 15666
                    && plr.getEquipment().getItems()[10].getId() == 15665)
                base += ((plr.getSkillManager().getMaxLevel(Skill.CONSTITUTION) - plr.getConstitution()) * .045) + 1;
            if (WellOfGoodwill.isActive()){
            	base *= 1.25;
            }

            if (specialBonus > 1) {
                base = (base * specialBonus);
            }
            if (hasObsidianEffect(plr) || EquipmentBonus.wearingVoid(plr, CombatType.MELEE)) {
                base *= 1.35;
            }

            if (victim.isNpc()) {
                NPC npc = (NPC) victim;
                if (npc.getDefenceWeakened()[0]) {
                    base += (int) ((0.10) * (base));
                } else if (npc.getDefenceWeakened()[1]) {
                    base += (int) ((0.20) * (base));
                } else if (npc.getDefenceWeakened()[2]) {
                    base += (int) ((0.30) * (base));
                }
                
                if (npc.getId() == plr.getSlayer().getSlayerTask().getNpcId()) {
                    if (plr.getEquipment().contains(14637)) {
                        base *= 2.5;
                    }
                }

                if (entity.isPlayer()) {

//                    if(plr.getSummoned() > 3053) {
//                        base *= 1.1;
//                    }
//                    if(plr.getSummoned() > 1302) {
//                        base *= 1.3;
//                    }
//                    if(plr.getSummoned() > 6301) {
//                        base *= 1.5;
//                    }

//                    if(plr.getTransform() >1 ){
//                        switch (plr.getTransform()) {
//                            case 4540:
//                                base *= 1.10;
//                                break;
//                            case 6303:
//                                base *= 1.15;
//                                break;
//                            case 1234:
//                                base *= 1.20;
//                                break;
//                            case 2236:
//                                base *= 1.25;
//                                break;
//                        }
//                    }
                    /*if (npc.getDefinition().getExamine() != null) {
                        String npcElemental = npc.getDefinition().getExamine();
                        ItemDefinition wep = ItemDefinition.forId(plr.getEquipment().get(Equipment.WEAPON_SLOT).getId());
                        int elemental = wep.getElemental();
                        switch (elemental) {
                            case 1: //fire wep
                                if (npcElemental.contains("Grass")) {
                                    base *= 1.10;
                                    if (plr.sendElementalMessage == true) {
                                        plr.setSendElementalMessage(false);

                                        plr.getPacketSender().sendMessage("Your weapon does extra damage against a grass type monster");
                                    }
                                } else if (npcElemental.contains("Dark")) {
                                    base *= 1.10;
                                    if (plr.sendElementalMessage == true) {
                                        plr.setSendElementalMessage(false);

                                        plr.getPacketSender().sendMessage("Your weapon does extra damage against a dark type monster");
                                    }
                                } else if (npcElemental.contains("Mythical")) {
                                    base *= 1.10;
                                    plr.getPacketSender().sendMessage("Your weapon does extra damage against a Mythical type monster");
                                } else if (npcElemental.contains("Earth")) {
                                    base *= 0.90;
                                    if (plr.sendElementalMessage == true) {
                                        plr.setSendElementalMessage(false);

                                        plr.getPacketSender().sendMessage("Your weapon does less damage against a earth type monster");
                                    }
                                } else if (npcElemental.contains("Water")) {

                                    base *= 0.90;

                                    if (plr.sendElementalMessage == true) {
                                        plr.setSendElementalMessage(false);

                                        plr.getPacketSender().sendMessage("Your weapon does less damage against a water type monster");
                                    }
                                }
                                break;
                            case 2: //water wep
                                if (npcElemental.contains("Fire")) {
                                    base *= 1.10;
                                    if (plr.sendElementalMessage == true) {
                                        plr.setSendElementalMessage(false);

                                        plr.getPacketSender().sendMessage("Your weapon does extra damage against a fire type monster");
                                    }
                                } else if (npcElemental.contains("Earth")) {
                                    base *= 1.10;
                                    if (plr.sendElementalMessage == true) {
                                        plr.setSendElementalMessage(false);

                                        plr.getPacketSender().sendMessage("Your weapon does extra damage against a Earth type monster");
                                    }
                                } else if (npcElemental.contains("Grass")) {
                                    base *= 0.90;
                                    if (plr.sendElementalMessage == true) {
                                        plr.setSendElementalMessage(false);
                                        plr.getPacketSender().sendMessage("Your weapon does less damage against a Grass type monster");
                                    }
                                }
                                break;
                            case 3: //earth wep
                                if (npcElemental.contains("Fire")) {
                                    base *= 1.10;
                                    if (plr.sendElementalMessage == true) {
                                        plr.setSendElementalMessage(false);

                                        plr.getPacketSender().sendMessage("Your weapon does extra damage against a fire type monster");
                                    }
                                } else if (npcElemental.contains("Earth")) {
                                    base *= 1.10;
                                    if (plr.sendElementalMessage == true) {
                                        plr.setSendElementalMessage(false);

                                        plr.getPacketSender().sendMessage("Your weapon does extra damage against a Earth type monster");
                                    }
                                } else if (npcElemental.contains("Grass")) {
                                    base *= 0.90;
                                    if (plr.sendElementalMessage == true) {
                                        plr.setSendElementalMessage(false);

                                        plr.getPacketSender().sendMessage("Your weapon does less damage against a grass type monster");
                                    }
                                } else if (npcElemental.contains("Water")) {
                                    base *= 0.90;
                                    if (plr.sendElementalMessage == true) {
                                        plr.setSendElementalMessage(false);

                                        plr.getPacketSender().sendMessage("Your weapon does less damage against a water type monster");
                                    }
                                }
                                break;
                            case 4: //grass wep
                                if (npcElemental.contains("Water")) {
                                    base *= 1.10;
                                    if (plr.sendElementalMessage == true) {
                                        plr.setSendElementalMessage(false);

                                        plr.getPacketSender().sendMessage("Your weapon does extra damage against a Water type monster");
                                    }
                                } else if (npcElemental.contains("Earth")) {
                                    base *= 1.10;
                                    if (plr.sendElementalMessage == true) {
                                        plr.setSendElementalMessage(false);

                                        plr.getPacketSender().sendMessage("Your weapon does extra damage against a Earth type monster");
                                    }
                                } else if (npcElemental.contains("Fire")) {
                                    base *= 0.90;
                                    if (plr.sendElementalMessage == true) {
                                        plr.setSendElementalMessage(false);

                                        plr.getPacketSender().sendMessage("Your weapon does less damage against a Fire type monster");
                                    }
                                } else if (npcElemental.contains("Mytical")) {
                                    base *= 0.90;
                                    if (plr.sendElementalMessage == true) {
                                        plr.setSendElementalMessage(false);

                                        plr.getPacketSender().sendMessage("Your weapon does less damage against a Mythical type monster");
                                    }
                                }
                                break;
                            case 5: //light wep
                                if (npcElemental.contains("Dark")) {
                                    base *= 1.10;
                                    if (plr.sendElementalMessage == true) {
                                        plr.setSendElementalMessage(false);

                                        plr.getPacketSender().sendMessage("Your weapon does extra damage against a dark type monster");
                                    }
                                } else if (npcElemental.contains("Fire")) {
                                    base *= 0.90;
                                    if (plr.sendElementalMessage == true) {
                                        plr.setSendElementalMessage(false);

                                        plr.getPacketSender().sendMessage("Your weapon does less damage against a Fire type monster");
                                    }
                                }
                                break;
                            case 6: //dark wep
                                if (npcElemental.contains("Light")) {
                                    base *= 1.10;
                                    if (plr.sendElementalMessage == true) {
                                        plr.setSendElementalMessage(false);

                                        plr.getPacketSender().sendMessage("Your weapon does extra damage against a dark type monster");
                                    }
                                } else if (npcElemental.contains("Earth")) {
                                    base *= 0.90;
                                    if (plr.sendElementalMessage == true) {
                                        plr.setSendElementalMessage(false);

                                        plr.getPacketSender().sendMessage("Your weapon does less damage against a Earth type monster");
                                    }
                                }
                                break;
                            case 7: //Mytical wep
                                if (npcElemental.contains("Fire")) {
                                    base *= 1.10;
                                    if (plr.sendElementalMessage == true) {
                                        plr.setSendElementalMessage(false);

                                        plr.getPacketSender().sendMessage("Your weapon does extra damage against a dark type monster");
                                    }
                                } else if (npcElemental.contains("Water")) {
                                    base *= 1.10;
                                    if (plr.sendElementalMessage == true) {
                                        plr.setSendElementalMessage(false);

                                        plr.getPacketSender().sendMessage("Your weapon does extra damage against a dark type monster");
                                    }
                                } else if (npcElemental.contains("Mythical")) {
                                    base *= 1.10;
                                    if (plr.sendElementalMessage == true) {
                                        plr.setSendElementalMessage(false);

                                        plr.getPacketSender().sendMessage("Your weapon does extra damage against a Mythical type monster");
                                    }
                                } else if (npcElemental.contains("Earth")) {
                                    base *= 0.90;
                                    if (plr.sendElementalMessage == true) {
                                        plr.setSendElementalMessage(false);

                                        plr.getPacketSender().sendMessage("Your weapon does less damage against a Earth type monster");
                                    }
                                }
                                break;
                        }
                    }*/
                }


                /** SLAYER HELMET **/
             // Familiar playerFamiliar = plr.getSummoning().getFamiliar();
                /** SLAYER HELMET **/
                
                if (npc.getId() == plr.getBloodSlayer().getBloodSlayerTask().getNpcId()) {
                    if (plr.getEquipment().contains(15497)) {
                        base *= 2.0;
                    }
                    
                }
                if (npc.getId() == plr.getSlayer().getSlayerTask().getNpcId()) {
                    if (plr.getEquipment().contains(15497)) {
                        base *= 2.0;
                    }
                    
                }
                
                if (npc.getId() == plr.getSlayer().getSlayerTask().getNpcId()) {
                    if (plr.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() == 15492) {
                        base *= 1.25;
                    }

                    if (plr.getEquipment().getItems()[Equipment.WEAPON_SLOT].getId() == 5271) {
                        base *= 1.25;
                    }
                    

                    if (plr.getEquipment().getItems()[Equipment.WEAPON_SLOT].getId() == 3971) {
                        base *= 2;
                    }           
 
                    if (plr.getEquipment().getItems()[Equipment.WEAPON_SLOT].getId() == 14921) {
                        base *= 4;
                    }
                    

                    if (plr.getRights() == PlayerRights.SUPREME_DONATOR) {
                        base *= 1.25;
                    }
                    if (plr.getRights() == PlayerRights.DIVINE_DONATOR) {
                        base *= 1.30;
                    }
                    if (plr.getRights() == PlayerRights.MODERATOR) {
                        base *= 1.25;
                    }
                    if (plr.getRights() == PlayerRights.ADMINISTRATOR) {
                        base *= 1.25;
                    }
                }
                if (plr.getEquipment().getItems()[Equipment.AMMUNITION_SLOT].getId() == 6759) {
                    base *= 1.25;
                }
                if (plr.getEquipment().getItems()[Equipment.AMMUNITION_SLOT].getId() == 6760) {
                    base *= 1.35;
                }
                if (plr.getEquipment().getItems()[Equipment.AMMUNITION_SLOT].getId() == 6762) {
                    base *= 1.50;
                }
                if (plr.isCandyRateActive()) {
                    base *= 1.50;
                }
                if (plr.getEquipment().getItems()[Equipment.WEAPON_SLOT].getId() == 17853) {
                    base *= 1.50;
                }
                if (plr.getEquipment().getItems()[Equipment.WEAPON_SLOT].getId() == 12185) {
                    base *= 1.50;
                }
                if (plr.getEquipment().getItems()[Equipment.WEAPON_SLOT].getId() == 11952) {
                    base *= 1.50;
                }
                if (plr.getEquipment().getItems()[Equipment.WEAPON_SLOT].getId() == 14287) {
                    base *= 1.30;
                }
                if (plr.getEquipment().getItems()[Equipment.WEAPON_SLOT].getId() == 5267) {
                    base *= 1.50;
                }
                if (plr.getEquipment().getItems()[Equipment.WEAPON_SLOT].getId() == 5263) {
                    base *= 1.50;
                }
                if (plr.getEquipment().getItems()[Equipment.WEAPON_SLOT].getId() == 13024) {
                    base *= 1.50;
                }
                if (plr.getEquipment().getItems()[Equipment.WEAPON_SLOT].getId() == 8040) {
                    base *= 1.50;
                }
                
            }
            
            
            if (plr.isMiniPlayer()) {
                base /= 3; // 3x less than normally
            }
            
            Familiar pet = plr.getSummoning().getFamiliar();

            if (pet != null) {
                final Optional<PetPerk> perk = PetPerk.forId(pet.getSummonNpc().getId());
                if (perk.isPresent()) {
                    base *= perk.get().getDamageBonus();
                }
            }
            
            maxHit = (base *= 10);
            if (plr.getUsername().equalsIgnoreCase("maccas mate")) 
            {
    			plr.sendMessage("@gre@Melee: @bla@"+maxHit);		
            }
            //maxHit = (GlobalPerks.getInstance().getActivePerk() == GlobalPerks.Perk.DAMAGE) ? (base *= 10)*2 : (base *= 10);
        }
        
        
        if (victim.isPlayer()) {
            Player p = (Player) victim;
            if (p.hasStaffOfLightEffect()) {
                maxHit = maxHit / 2;
                p.performGraphic(new Graphic(2319));
         
                if (victim.isNpc()) {
                    NPC npc = (NPC) victim;
                    
                    for(DamageCapHandler cap : DamageCapHandler.values()) {
                    	if(npc.getId() == cap.getNpcId()) {
                    		maxHit = cap.getMaxDamage();
                    	}
                    }
                    
                    if (npc.getId() == 10000) {
                        maxHit = 50000;
                    }

                    if (npc.getId() == 10001 && FourthStrategy.iconIndex == 0) {
                        maxHit = 50000;
                    }
                }}
        }
        return (int) Math.floor(maxHit);
    }

    /**
     * Calculates a player's Melee attack level (how likely that they're going to hit through defence)
     *
     * @param plr The player's Meelee attack level
     * @return The player's Melee attack level
     */
    @SuppressWarnings("incomplete-switch")
    public static int getMeleeAttack(Player plr) {
        int attackLevel = plr.getSkillManager().getCurrentLevel(Skill.ATTACK);
        switch (plr.getFightType().getStyle()) {
            case AGGRESSIVE:
                attackLevel += 3;
                break;
            case CONTROLLED:
                attackLevel += 1;
                break;
        }
        boolean hasVoid = EquipmentBonus.wearingVoid(plr, CombatType.MELEE);
       // boolean hasVoid2 = EquipmentBonus.wearingVoid2(plr, CombatType.MELEE);

        if (PrayerHandler.isActivated(plr,
                PrayerHandler.CLARITY_OF_THOUGHT)) {
            attackLevel += plr.getSkillManager().getMaxLevel(Skill.ATTACK) * 0.05;
        } else if (PrayerHandler.isActivated(plr,
                PrayerHandler.IMPROVED_REFLEXES)) {
            attackLevel += plr.getSkillManager().getMaxLevel(Skill.ATTACK) * 0.1;
        } else if (PrayerHandler.isActivated(plr,
                PrayerHandler.INCREDIBLE_REFLEXES)) {
            attackLevel += plr.getSkillManager().getMaxLevel(Skill.ATTACK) * 0.15;
        } else if (PrayerHandler.isActivated(plr,
                PrayerHandler.CHIVALRY)) {
            attackLevel += plr.getSkillManager().getMaxLevel(Skill.ATTACK) * 0.15;
        } else if (PrayerHandler.isActivated(plr, PrayerHandler.PIETY)) {
            attackLevel += plr.getSkillManager().getMaxLevel(Skill.ATTACK) * 0.2;
        } else if (CurseHandler.isActivated(plr, CurseHandler.LEECH_ATTACK)) {
            attackLevel += plr.getSkillManager().getMaxLevel(Skill.ATTACK) * 0.05 + plr.getLeechedBonuses()[2];
        } else if (CurseHandler.isActivated(plr, CurseHandler.TURMOIL)) {
            attackLevel += plr.getSkillManager().getMaxLevel(Skill.ATTACK)
                    * 0.3 + plr.getLeechedBonuses()[2];
        }

        if (hasVoid) {
            attackLevel += plr.getSkillManager().getMaxLevel(Skill.ATTACK) * 1.5;
        //}  if (hasVoid2) {
            //attackLevel += plr.getSkillManager().getMaxLevel(Skill.ATTACK) * 2.5;
        }
        attackLevel *= plr.isSpecialActivated() ? plr.getCombatSpecial().getAccuracyBonus() : 1;
        int i = (int) plr.getBonusManager().getAttackBonus()[bestMeleeAtk(plr)];

        if (hasObsidianEffect(plr) || hasVoid)
            i *= 1.20;
        //if (hasObsidianEffect(plr) || hasVoid2)
           // i *= 1.90;
        final Familiar pet = plr.getSummoning().getFamiliar();
        
        if (pet != null) {
            final Optional<PetPerk> perk = PetPerk.forId(pet.getSummonNpc().getId());
            if (perk.isPresent()) {
                attackLevel *= perk.get().getDamageBonus();
            }
        }
        return (int) (attackLevel + (attackLevel * 0.15) + (i + i * 0.04));
    }

    /**
     * Calculates a player's Melee Defence level
     *
     * @param plr The player to calculate Melee defence for
     * @return The player's Melee defence level
     */
    public static int getMeleeDefence(Player plr) {
        int defenceLevel = plr.getSkillManager().getCurrentLevel(Skill.DEFENCE);
        int i = (int) plr.getBonusManager().getDefenceBonus()[bestMeleeDef(plr)];
        if (plr.getPrayerActive()[PrayerHandler.THICK_SKIN]) {
            defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.05;
        } else if (plr.getPrayerActive()[PrayerHandler.ROCK_SKIN]) {
            defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.1;
        } else if (plr.getPrayerActive()[PrayerHandler.STEEL_SKIN]) {
            defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.15;
        } else if (plr.getPrayerActive()[PrayerHandler.CHIVALRY]) {
            defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.2;
        } else if (plr.getPrayerActive()[PrayerHandler.PIETY]) {
            defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.25;
        } else if (plr.getPrayerActive()[PrayerHandler.RIGOUR]) {
            defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.25;
        } else if (plr.getPrayerActive()[PrayerHandler.AUGURY]) {
            defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.25;
        } else if (plr.getCurseActive()[CurseHandler.TURMOIL]) { // turmoil
            defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.15;
        }
        return (int) (defenceLevel + (defenceLevel * 0.15) + (i + i * 1.0));
    }

    public static int bestMeleeDef(Player p) {
        if (p.getBonusManager().getDefenceBonus()[0] > p.getBonusManager().getDefenceBonus()[1] && p.getBonusManager().getDefenceBonus()[0] > p.getBonusManager().getDefenceBonus()[2]) {
            return 0;
        }
        if (p.getBonusManager().getDefenceBonus()[1] > p.getBonusManager().getDefenceBonus()[0] && p.getBonusManager().getDefenceBonus()[1] > p.getBonusManager().getDefenceBonus()[2]) {
            return 1;
        }
        return p.getBonusManager().getDefenceBonus()[2] <= p.getBonusManager().getDefenceBonus()[0] || p.getBonusManager().getDefenceBonus()[2] <= p.getBonusManager().getDefenceBonus()[1] ? 0 : 2;
    }

    public static int bestMeleeAtk(Player p) {
        if (p.getBonusManager().getAttackBonus()[0] > p.getBonusManager().getAttackBonus()[1] && p.getBonusManager().getAttackBonus()[0] > p.getBonusManager().getAttackBonus()[2]) {
            return 0;
        }
        if (p.getBonusManager().getAttackBonus()[1] > p.getBonusManager().getAttackBonus()[0] && p.getBonusManager().getAttackBonus()[1] > p.getBonusManager().getAttackBonus()[2]) {
            return 1;
        }
        return p.getBonusManager().getAttackBonus()[2] <= p.getBonusManager().getAttackBonus()[1] || p.getBonusManager().getAttackBonus()[2] <= p.getBonusManager().getAttackBonus()[0] ? 0 : 2;
    }


    /**
     * Obsidian items
     */

    public static final int[] obsidianWeapons = {
            746, 747, 6523, 6525, 6526, 6527, 6528
    };

    public static boolean hasObsidianEffect(Player plr) {
        if (plr.getEquipment().getItems()[2].getId() != 11128)
            return false;

        for (int weapon : obsidianWeapons) {
            if (plr.getEquipment().getItems()[3].getId() == weapon)
                return true;
        }
        return false;
    }

    @SuppressWarnings("incomplete-switch")
    public static int getStyleBonus(Player plr) {
        switch (plr.getFightType().getStyle()) {
            case AGGRESSIVE:
            case ACCURATE:
                return 3;
            case CONTROLLED:
                return 1;
        }
        return 0;
    }

    public static double getEffectiveStr(Player plr) {
        return ((plr.getSkillManager().getCurrentLevel(Skill.STRENGTH)) * getPrayerStr(plr)) + getStyleBonus(plr);
    }

    public static double getPrayerStr(Player plr) {
        if (plr.getPrayerActive()[1] || plr.getCurseActive()[CurseHandler.LEECH_STRENGTH])
            return 1.05;
        else if (plr.getPrayerActive()[6])
            return 1.1;
        else if (plr.getPrayerActive()[14])
            return 1.15;
        else if (plr.getPrayerActive()[24])
            return 1.18;
        else if (plr.getPrayerActive()[25])
            return 1.23;
        else if (plr.getCurseActive()[CurseHandler.TURMOIL])
            return 1.24;
        return 1;
    }

    /**
     * Calculates a player's Ranged attack (level).
     * Credits: Dexter Morgan
     *
     * @param plr The player to calculate Ranged attack level for
     * @return The player's Ranged attack level
     */
    public static int getRangedAttack(Player plr) {
        int rangeLevel = plr.getSkillManager().getCurrentLevel(Skill.RANGED);
        boolean hasVoid = EquipmentBonus.wearingVoid(plr, CombatType.RANGED);
        //boolean hasVoid2 = EquipmentBonus.wearingVoid2(plr, CombatType.RANGED);
        double accuracy = plr.isSpecialActivated() ? plr.getCombatSpecial().getAccuracyBonus() : 1;
        rangeLevel *= accuracy;
        if (hasVoid) {
            rangeLevel += SkillManager.getLevelForExperience(plr.getSkillManager().getExperience(Skill.RANGED)) * 1.5;
       // }   if (hasVoid2) {
          //  rangeLevel += SkillManager.getLevelForExperience(plr.getSkillManager().getExperience(Skill.RANGED)) * 2.5;
        }
        if (plr.getCurseActive()[PrayerHandler.SHARP_EYE] || plr.getCurseActive()[CurseHandler.SAP_RANGER]) {
            rangeLevel *= 1.05;
        }

        if (plr.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() == 15492) {
            rangeLevel *= 1.2;
        } else if (plr.getEquipment().getItems()[Equipment.AMMUNITION_SLOT].getId() == 6759) {
            rangeLevel *= 1.25;
        } else if (plr.getEquipment().getItems()[Equipment.AMMUNITION_SLOT].getId() == 6760) {
            rangeLevel *= 1.35;
        } else if (plr.getEquipment().getItems()[Equipment.AMMUNITION_SLOT].getId() == 6762) {
           rangeLevel *= 1.50;  
        } else if (plr.getEquipment().getItems()[Equipment.WEAPON_SLOT].getId() == 20427) {
            rangeLevel *= 1.25; 
        } else if (plr.getEquipment().getItems()[Equipment.WEAPON_SLOT].getId() == 5115) {
            rangeLevel *= 1.50; 
        } else if (plr.getEquipment().getItems()[Equipment.WEAPON_SLOT].getId() == 15010) {
            rangeLevel *= 1.75; 
        } else if (plr.getEquipment().getItems()[Equipment.WEAPON_SLOT].getId() == 8488) {
            rangeLevel *= 1.85;
        } else if (plr.getPrayerActive()[PrayerHandler.HAWK_EYE]) {
            rangeLevel *= 1.10;
        } else if (plr.getPrayerActive()[PrayerHandler.EAGLE_EYE]) {
            rangeLevel *= 1.15;
        } else if (plr.getPrayerActive()[PrayerHandler.RIGOUR]) {
            rangeLevel *= 1.22;
        } else if (plr.getCurseActive()[CurseHandler.LEECH_RANGED]) {
            rangeLevel *= 1.10;
        }
        else if (plr.getCurseActive()[CurseHandler.TURMOIL]) {
            rangeLevel *= 1.24;
        }
        
        Familiar pet = plr.getSummoning().getFamiliar();

        if (pet != null) {
            final Optional<PetPerk> perk = PetPerk.forId(pet.getSummonNpc().getId());
            if (perk.isPresent()) {
                rangeLevel *= perk.get().getDamageBonus();
            }
        }
        if (hasVoid && accuracy > 1.15)
            rangeLevel *= 1.4;
        //if (hasVoid2 && accuracy > 1.15)
           // rangeLevel *= 2.0;
        return (int) (rangeLevel + (plr.getBonusManager().getAttackBonus()[4] * 2));
    }

    /**
     * Calculates a player's Ranged defence level.
     *
     * @param plr The player to calculate the Ranged defence level for
     * @return The player's Ranged defence level
     */
    public static int getRangedDefence(Player plr) {
        int defenceLevel = plr.getSkillManager().getCurrentLevel(Skill.DEFENCE);
        if (plr.getPrayerActive()[PrayerHandler.THICK_SKIN]) {
            defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.05;
        } else if (plr.getPrayerActive()[PrayerHandler.ROCK_SKIN]) {
            defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.1;
        } else if (plr.getPrayerActive()[PrayerHandler.STEEL_SKIN]) {
            defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.15;
        } else if (plr.getPrayerActive()[PrayerHandler.CHIVALRY]) {
            defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.2;
        } else if (plr.getPrayerActive()[PrayerHandler.PIETY]) {
            defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.25;
        } else if (plr.getPrayerActive()[PrayerHandler.RIGOUR]) {
            defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.25;
        } else if (plr.getPrayerActive()[PrayerHandler.AUGURY]) {
            defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.25;
        } else if (plr.getCurseActive()[CurseHandler.TURMOIL]) { // turmoil
            defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE)
                    * 0.20 + plr.getLeechedBonuses()[0];
        }
        return (int) (defenceLevel + plr.getBonusManager().getDefenceBonus()[4] + (plr.getBonusManager().getDefenceBonus()[4] / 2));
    }

    public static int getMagicAttack(Player plr) {
        boolean HasVoid = EquipmentBonus.wearingVoid(plr, CombatType.MAGIC);
       // boolean HasVoid2 = EquipmentBonus.wearingVoid2(plr, CombatType.MAGIC);
        int attackLevel = plr.getSkillManager().getCurrentLevel(Skill.MAGIC);
        if (HasVoid)
            attackLevel += plr.getSkillManager().getCurrentLevel(Skill.MAGIC) * 2.0;
        //if (HasVoid2)
          //  attackLevel += plr.getSkillManager().getCurrentLevel(Skill.MAGIC) * 3.0;
        if (plr.getPrayerActive()[PrayerHandler.MYSTIC_WILL] || plr.getCurseActive()[CurseHandler.SAP_MAGE]) {
            attackLevel *= 1.05;
        } else if (plr.getPrayerActive()[PrayerHandler.MYSTIC_LORE]) {
            attackLevel *= 1.10;
        } else if (plr.getPrayerActive()[PrayerHandler.MYSTIC_MIGHT]) {
            attackLevel *= 1.15;
        } else if (plr.getPrayerActive()[PrayerHandler.AUGURY]) {
            attackLevel *= 1.22;
        } else if (plr.getCurseActive()[CurseHandler.LEECH_MAGIC]) {
            attackLevel *= 1.18;
        }
        else if (plr.getCurseActive()[CurseHandler.TURMOIL]) {
            attackLevel *= 1.24;
        }
        Familiar pet = plr.getSummoning().getFamiliar();

        if (pet != null) {
            final Optional<PetPerk> perk = PetPerk.forId(pet.getSummonNpc().getId());
            if (perk.isPresent()) {
                attackLevel *= perk.get().getDamageBonus();
            }
        }
        attackLevel *= plr.isSpecialActivated() ? plr.getCombatSpecial().getAccuracyBonus() : 1;

        return (int) (attackLevel + (plr.getBonusManager().getAttackBonus()[3] * 3));
    }

    /**
     * Calculates a player's magic defence level
     *
     * @param player The player to calculate magic defence level for
     * @return The player's magic defence level
     */
    public static int getMagicDefence(Player plr) {


        int defenceLevel = plr.getSkillManager().getCurrentLevel(Skill.DEFENCE) / 2 + plr.getSkillManager().getCurrentLevel(Skill.MAGIC) / 2;

        if (plr.getPrayerActive()[PrayerHandler.THICK_SKIN]) {
            defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.05;
        } else if (plr.getPrayerActive()[PrayerHandler.ROCK_SKIN]) {
            defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.1;
        } else if (plr.getPrayerActive()[PrayerHandler.STEEL_SKIN]) {
            defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.15;
        } else if (plr.getPrayerActive()[PrayerHandler.CHIVALRY]) {
            defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.2;
        } else if (plr.getPrayerActive()[PrayerHandler.PIETY]) {
            defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.25;
        } else if (plr.getPrayerActive()[PrayerHandler.RIGOUR]) {
            defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.25;
        } else if (plr.getPrayerActive()[PrayerHandler.AUGURY]) {
            defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE) * 0.25;
        } else if (plr.getCurseActive()[CurseHandler.TURMOIL]) { // turmoil
            defenceLevel += plr.getSkillManager().getMaxLevel(Skill.DEFENCE)
                    * 0.20 + plr.getLeechedBonuses()[0];
        }

        return (int) (defenceLevel + plr.getBonusManager().getDefenceBonus()[3] + (plr.getBonusManager().getDefenceBonus()[3] / 3));
    }

    /**
     * Calculates a player's magic max hit
     *
     * @param player The player to calculate magic max hit for
     * @return The player's magic max hit damage
     */
    public static double getMagicMaxhit(Character c, Character victim) {
        double damage = 0;
        CombatSpell spell = c.getCurrentlyCasting();
        if (spell != null) {
            if (spell.maximumHit() > 0) {
                damage += spell.maximumHit();
                for(DamageCapHandler cap : DamageCapHandler.values()) {
                	Character npc = c.getCombatBuilder().getVictim();
                	if(((NPC) npc).getId() == cap.getNpcId()) {
                		damage = cap.getMaxDamage();
                	}
                }
            } else {
                if (c.isNpc()) {
                    damage = ((NPC) c).getDefinition().getMaxHit();
                } else {
                    damage = 1;
                }
            }
        }
        if (c.isNpc()) {
            if (spell == null) {
                damage = Misc.getRandom(((NPC) c).getDefinition().getMaxHit());
            }
            return damage;
        }

        Player p = (Player) c;
        int aura = RandomUtility.exclusiveRandom(99);
		double base = 0;
        double damageMultiplier = 1;
		double effective = p.getSkillManager().getCurrentLevel(Skill.MAGIC);
        double magicBonus = ((int) p.getBonusManager().getAttackBonus()[3]); 
		base = (13 + effective + (magicBonus / 8) + ((effective * magicBonus) / 65)) / 11;

        switch (p.getEquipment().getItems()[Equipment.WEAPON_SLOT].getId()) { //damageMultiplier not in use
            case 4675:
            case 6914:
            case 15246:
                damageMultiplier += .10;
                break;
            case 18355:
                damageMultiplier += .20;
                break;
        }
           switch (p.getEquipment().getItems()[Equipment.SHIELD_SLOT].getId()) {
                case 19905:
                    damageMultiplier += 3.50;
                    break;
        }
     
           switch (p.getEquipment().getItems()[Equipment.AMMUNITION_SLOT].getId()) {
           case 6759:
               damageMultiplier += 0.25;
               break;
           }
           switch (p.getEquipment().getItems()[Equipment.AMMUNITION_SLOT].getId()) {
           case 6760:
               damageMultiplier += 0.35;
               break; 
           }
           switch (p.getEquipment().getItems()[Equipment.AMMUNITION_SLOT].getId()) {
           case 6762:
               damageMultiplier += 0.50;
           }
           
           switch (p.getEquipment().getItems()[Equipment.CAPE_SLOT].getId()) {
           case 15566:
               damageMultiplier += 0.10;
               break;
           case 6482:
               damageMultiplier += 0.10;
               break;
           case 16539:
               damageMultiplier += 0.10;
               break;
           case 13028:
               damageMultiplier += 0.10;
               break;
           case 7026:
               damageMultiplier += 0.10;
               break;
           case 14036:
               damageMultiplier += 0.10;
               break;
           }
           
           switch (p.getEquipment().getItems()[Equipment.WEAPON_SLOT].getId()) {
               case 9492:
               case 13201:
               case 6930:
               case 5115:
                   damageMultiplier += .01;
                   break;
           }
        
        boolean specialAttack = p.isSpecialActivated();

        int maxHit = -1;

        if (specialAttack) {
            switch (p.getEquipment().getItems()[Equipment.WEAPON_SLOT].getId()) {
                case 19780:
                    damage = maxHit = 750;
                    break;
                case 11730:
                    damage = maxHit = 310;
                    break;
                case 19028:
                    damage = maxHit = 310;
                    break;
            }
        } else {
            damageMultiplier += 0.25;
        }

        if (p.getEquipment().getItems()[Equipment.AMULET_SLOT].getId() == 18335) {
            damageMultiplier += .10;
        }
        if (p.getUsername().toLowerCase().contains("maccas") && p.getEquipment().get(Equipment.RING_SLOT).getId() == 18410) { //Maccas's Lunar Ring
			base *= 10;
		}
        final Familiar pet = p.getSummoning().getFamiliar();
        if (pet != null) {
            final Optional<PetPerk> perk = PetPerk.forId(pet.getSummonNpc().getId());
            if (perk.isPresent()) {
                damage *= perk.get().getDamageBonus();
            }
        }

        if (maxHit > 0) {
            if (damage > maxHit) {
                damage = maxHit;
            }
        }
        if (c.isNpc()) {
            NPC npc = (NPC) c;
            
            for(DamageCapHandler cap : DamageCapHandler.values()) {
            	if(npc.getId() == cap.getNpcId()) {
            		damage = cap.getMaxDamage();
            	}
            }
            
            if (npc.getId() == 10001) {
                damage = 0;
            }

            if (npc.getId() == 10000 && FourthStrategy.iconIndex == 2) {
                damage = 0;
            }
        }
        
        if (p.isMiniPlayer()) {
            damage /= 3; // 3x less than normally
        }
		damage = (base *=10);
		
        if (p.getUsername().equalsIgnoreCase("maccas mate")) 
        {
			p.sendMessage("@gre@Mage: @bla@"+damage);		
        }
        return (int) damage;        			
    }


    public static int getAttackDelay(Player plr) {
        int id = plr.getEquipment().getItems()[Equipment.WEAPON_SLOT].getId();
        String s = ItemDefinition.forId(id).getName().toLowerCase();
        if (id == -1)
            return 4;// unarmed
        if (id == 18357 || id == 14684 || id == 13051)
            return 3;
        RangedWeaponData rangedData = plr.getRangedWeaponData();
        if (rangedData != null) {
            int speed = rangedData.getType().getAttackDelay();
            if (plr.getFightType() == FightType.SHORTBOW_RAPID || plr.getFightType() == FightType.DART_RAPID || plr.getFightType() == FightType.KNIFE_RAPID || plr.getFightType() == FightType.THROWNAXE_RAPID || plr.getFightType() == FightType.JAVELIN_RAPID) {
                speed--;
            }
            return speed;
        }
        if (id == 18365)
            return 3;
        else if (id == 18349) //CCbow and rapier
            return 4;
        if (id == 18353) // cmaul
            return 7;// chaotic maul
        if (id == 3954) // Bandos maul
            return 6;// Bandos Maul

        if (id == 20000)
            return 4;// gs
        if (id == 20001)
            return 4;// gs
        if (id == 20002)
            return 4;// gs
        if (id == 20003)
            return 4;// gs
        if (id == 18349)
            return 5;// chaotic rapier
        if (id == 18353) // cmaul
            return 7;// chaotic maul
        if (id == 3954) // Bandos maul
            return 6;// Bandos Maul
        if (id == 16877)
            return 4;// dung 16877 shortbow
        if (id == 19143)
            return 3;// sara shortbow
        if (id == 19146)
            return 4;// guthix shortbow
        if (id == 19149)
            return 3;// zammy shortbow

        switch (id) {
            case 11235:
            case 13405: //dbow
            case 15701: // dark bow
            case 15702: // dark bow
            case 15703: // dark bow
            case 15704: // dark bow
            case 19146: // guthix bow
                return 9;
            case 13879:
                return 8;
            case 15241: // hand cannon
                return 8;
            case 11730:
                return 4;
            case 14484:
                return 5;
            case 19023:
                return 5;
            case 13883:
                return 6;
            case 10887:
            case 6528:
            case 15039:
                return 7;
            case 4450:
                return 5;
            case 13905:
                return 5;
            case 13907:
                return 5;
            case 18353:
                return 7;
            case 3954:
                return 6;
            case 18349:
                return 4;
            case 20000:
            case 20001:
            case 20002:
            case 20003:
                return 4;
            case 4706:
            case 18971:
            case 4212:
                return 4;

            case 16403: //long primal
                return 5;
        }

        if (s.endsWith("greataxe"))
            return 7;
        else if (s.equals("torags hammers"))
            return 5;
        else if (s.equals("guthans warspear"))
            return 5;
        else if (s.equals("veracs flail"))
            return 5;
        else if (s.equals("ahrims staff"))
            return 6;
        else if (s.equals("chaotic crossbow"))
            return 4;
        else if (s.contains("staff")) {
            if (s.contains("zamarok") || s.contains("guthix")
                    || s.contains("saradomian") || s.contains("slayer")
                    || s.contains("ancient"))
                return 4;
            else
                return 5;
        } else if (s.contains("aril")) {
            if (s.contains("composite") || s.equals("seercull"))
                return 5;
            else if (s.contains("Ogre"))
                return 8;
            else if (s.contains("short") || s.contains("hunt")
                    || s.contains("sword"))
                return 4;
            else if (s.contains("long") || s.contains("crystal"))
                return 6;
            else if (s.contains("'bow"))
                return 7;

            return 5;
        } else if (s.contains("dagger"))
            return 4;
        else if (s.contains("godsword") || s.contains("2h"))
            return 6;
        else if (s.contains("longsword"))
            return 5;
        else if (s.contains("sword"))
            return 4;
        else if (s.contains("scimitar") || s.contains("katana"))
            return 4;
        else if (s.contains("mace"))
            return 5;
        else if (s.contains("battleaxe"))
            return 6;
        else if (s.contains("pickaxe"))
            return 5;
        else if (s.contains("thrownaxe"))
            return 5;
        else if (s.contains("axe"))
            return 5;
        else if (s.contains("warhammer"))
            return 6;
        else if (s.contains("2h"))
            return 7;
        else if (s.contains("spear"))
            return 5;
        else if (s.contains("claw"))
            return 4;
        else if (s.contains("halberd"))
            return 7;

            // sara sword, 2400ms
        else if (s.equals("granite maul"))
            return 7;
        else if (s.equals("toktz-xil-ak"))// sword
            return 4;
        else if (s.equals("tzhaar-ket-em"))// mace
            return 5;
        else if (s.equals("tzhaar-ket-om"))// maul
            return 7;
        else if (s.equals("chaotic maul"))// maul
            return 7;
        else if (s.equals("toktz-xil-ek"))// knife
            return 4;
        else if (s.equals("toktz-xil-ul"))// rings
            return 4;
        else if (s.equals("toktz-mej-tal"))// staff
            return 6;
        else if (s.contains("whip"))
            return 4;
        else if (s.contains("dart"))
            return 3;
        else if (s.contains("knife"))
            return 3;
        else if (s.contains("javelin"))
            return 6;
        return 5;
    }
}
