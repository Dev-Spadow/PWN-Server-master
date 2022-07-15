package com.arlania.world.content.combat;

import com.arlania.model.Item;
import com.arlania.model.Skill;
import com.arlania.model.container.impl.Equipment;
import com.arlania.util.Misc;
import com.arlania.world.content.combat.prayer.CurseHandler;
import com.arlania.world.content.combat.prayer.PrayerHandler;
import com.arlania.world.content.combat.weapon.FightStyle;
import com.arlania.world.entity.impl.Character;
import com.arlania.world.entity.impl.npc.NPC;
import com.arlania.world.entity.impl.player.Player;

public class Maxhits {

    public static int melee(Character entity, Character victim) {
        double maxHit = 0;
        if (entity.isNpc()) {
            NPC npc = (NPC) entity;
            maxHit = npc.getDefinition().getMaxHit();
            if (npc.getStrengthWeakened()[0]) {
                maxHit -= (int) (0.10 * maxHit);
            } else if (npc.getStrengthWeakened()[1]) {
                maxHit -= (int) (0.20 * maxHit);
            } else if (npc.getStrengthWeakened()[2]) {
                maxHit -= (int) (0.30 * maxHit);
            }
        } else if (entity.isPlayer()) {
            Player player = (Player) entity;

            double base = 0;
            double effective = getEffectiveStr(player);
            double strengthBonus = player.getBonusManager().getOtherBonus()[0];
            double specialBonus = 1;

            // Use our multipliers to adjust the maxhit...

            base = 1.3 + effective / 10 + strengthBonus / 80 + effective * strengthBonus / 640;

            // Special effects also affect maxhit
            if (player.isSpecialActivated() && player.getCombatSpecial().getCombatType() == CombatType.MELEE) {
                specialBonus = player.getCombatSpecial().getStrengthBonus();
            }

            if (specialBonus > 1) {
                base = Math.round(base) * specialBonus;
            } else {
                base = (int) base;
            }

            if (victim.isNpc()) {
                if (((NPC) victim).getId() == player.getSlayer().getSlayerTask().getNpcId()) {
                    if (player.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() == 23071
                            || player.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() == 23069) {
                        base *= 1.05;
                    } else if (player.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() == 23070) {
                        base *= 1.07;
                    } else if (player.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() == 23074) {
                        base *= 1.10;
                    } else if (player.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() == 23072) {
                        base *= 1.15;
                    } else if (player.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() == 23073) {
                        base *= 1.25;
                    }

                }
            }

            maxHit = base * 10;



            if (victim.isNpc()) {
                if (((NPC) victim).getId() == player.getSlayer().getSlayerTask().getNpcId()
                        && victim.getConstitution() >= ((NPC) victim).getDefaultConstitution()) {
                    int percent = -1;
                    if (player.getEquipment().contains(22000)) {
                        percent = 2;
                    } else if (player.getEquipment().contains(22001)) {
                        percent = 4;
                    } else if (player.getEquipment().contains(22002)) {
                        percent = 6;
                    } else if (player.getEquipment().contains(22003)) {
                        percent = 8;
                    } else if (player.getEquipment().contains(22004)) {
                        percent = 10;
                    }

                    if (Misc.random(99) + 1 <= percent) {
                        maxHit = victim.getConstitution() * 10;
                    }
                }
            }
            maxHit = damageBoosted(player, maxHit);
        }

        // Dharoks effect
        if (CombatFactory.fullDharoks(entity)) {
            int hitpoints = entity.getConstitution() / 10;
            if (entity.isNpc()) {
                int missingHealth = ((NPC) entity).getDefinition().getHitpoints() - hitpoints;
                double addToHit = missingHealth * 0.01 + 1;
                maxHit *= addToHit;
            } else {
                int missingHealth = ((Player) entity).getSkillManager().getMaxLevel(Skill.CONSTITUTION) - hitpoints;
                double addToHit = missingHealth * 0.01 + 1;
                maxHit *= addToHit;
                if (maxHit >= 990)
                    maxHit = 990;
            }
        }

        if (victim != null && victim.isNpc()) {
            maxHit = NpcMaxHitLimit.limit((NPC) victim, maxHit, CombatType.MELEE);
        }

        return (int) Math.floor(maxHit);
    }

    public static int ranged(Character entity, Character victim) {
        double maxHit = 0;

        if (entity.isNpc()) {
            NPC npc = (NPC) entity;
            maxHit = npc.getDefinition().getMaxHit() / 10;
        } else if (entity.isPlayer()) {
            Player player = (Player) entity;

            double rangedStrength = player.getBonusManager().getOtherBonus()[1];
            double rangeLevel = player.getSkillManager().getCurrentLevel(Skill.RANGED);

            // Prayers
            double prayerMod = 1.0;
            if (PrayerHandler.isActivated(player, PrayerHandler.SHARP_EYE) || CurseHandler.isActivated(player, CurseHandler.LEECH_RANGED)) {
                prayerMod = 1.05;
            } else if (PrayerHandler.isActivated(player, PrayerHandler.HAWK_EYE)) {
                prayerMod = 1.10;
            } else if (PrayerHandler.isActivated(player, PrayerHandler.EAGLE_EYE)) {
                prayerMod = 1.15;
            } else if (PrayerHandler.isActivated(player, PrayerHandler.RIGOUR)) {
                prayerMod = 1.23;
            } else if (CurseHandler.isActivated(player, CurseHandler.TURMOIL)) {
                prayerMod = 1.25;
            /*} else if (PrayerHandler.isActivated(player,PrayerHandler.SOUL_LEECH)) {
                prayerMod = 1.15 + +(player.getLeechedBonuses()[2] * 0.01);
                if (Misc.getRandom(100) <= 1) {
                    player.setDoubleDMGTimer(1);
                    player.getPacketSender().sendMessage("Soul leech activated");
                }*/
            }

            double otherBonuses = 1;


            // Do calculations of maxhit...
            double effectiveRangeDamage = (int) (rangeLevel * prayerMod * otherBonuses);

            double baseDamage = 1.3 + effectiveRangeDamage / 10 + rangedStrength / 80 + effectiveRangeDamage * rangedStrength / 640;

            double specialBonus = 1;
            // Special attacks!
            if (player.isSpecialActivated() && player.getCombatSpecial().getCombatType() == CombatType.RANGED) {
                specialBonus = player.getCombatSpecial().getStrengthBonus();
            }

            maxHit = (int) baseDamage * specialBonus;

            if (victim.isNpc()) {
                if (((NPC) victim).getId() == player.getSlayer().getSlayerTask().getNpcId()) {
                    if (player.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() == 23071
                            || player.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() == 23069) {
                        maxHit *= 1.05;
                    } else if (player.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() == 23070) {
                        maxHit *= 1.07;
                    } else if (player.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() == 23074) {
                        maxHit *= 1.10;
                    } else if (player.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() == 23072) {
                        maxHit *= 1.15;
                    } else if (player.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() == 23073) {
                        maxHit *= 1.25;
                    }
                }
            }


            if (victim.isNpc()) {
                if (((NPC) victim).getId() == player.getSlayer().getSlayerTask().getNpcId()
                        && victim.getConstitution() >= ((NPC) victim).getDefaultConstitution()) {
                    int percent = -1;
                    if (player.getEquipment().contains(22000)) {
                        percent = 2;
                    } else if (player.getEquipment().contains(22001)) {
                        percent = 4;
                    } else if (player.getEquipment().contains(22002)) {
                        percent = 6;
                    } else if (player.getEquipment().contains(22003)) {
                        percent = 8;
                    } else if (player.getEquipment().contains(22004)) {
                        percent = 10;
                    }
                    if (Misc.random(99) + 1 <= percent) {
                        maxHit = victim.getConstitution();
                    }
                }
            }
            maxHit = damageBoosted(player, maxHit);
        }

        maxHit *= 10;

        if (victim != null && victim.isNpc()) {
            maxHit = (int) NpcMaxHitLimit.limit((NPC) victim, maxHit, CombatType.RANGED);
        }
        return (int) Math.floor(maxHit);
    }

    public static int magic(Character entity, Character victim) {
        double maxHit = 0;

        if (entity.isNpc()) {
            NPC npc = (NPC) entity;
            maxHit = npc.getDefinition().getMaxHit() / 10;
        } else if (entity.isPlayer()) {
            Player player = (Player) entity;

            double magicStrength = player.getBonusManager().getOtherBonus()[3];
            double magicLevel = player.getSkillManager().getCurrentLevel(Skill.MAGIC);

            // Prayers
            double prayerMod = 1.0;
            if (PrayerHandler.isActivated(player, PrayerHandler.MYSTIC_WILL) || CurseHandler.isActivated(player, CurseHandler.LEECH_MAGIC)) {
                prayerMod = 1.05;
            } else if (PrayerHandler.isActivated(player, PrayerHandler.MYSTIC_LORE)) {
                prayerMod = 1.10;
            } else if (PrayerHandler.isActivated(player, PrayerHandler.MYSTIC_MIGHT)) {
                prayerMod = 1.15;
            } else if (PrayerHandler.isActivated(player, PrayerHandler.AUGURY)) {
                prayerMod = 1.23;
            } else if (CurseHandler.isActivated(player, CurseHandler.TURMOIL)) {
                prayerMod = 1.25;
            }
            double otherBonuses = 1;

            // Void hits 10% more
            // Do calculations of maxhit...
            double effectiveMagicDamage = (int) (magicLevel * prayerMod * otherBonuses);


            double baseDamage = 1.3 + effectiveMagicDamage / 10 + magicStrength / 80 + effectiveMagicDamage * magicStrength / 640;

            double specialBonus = 1;
            // Special attacks!
            if (player.isSpecialActivated() && player.getCombatSpecial().getCombatType() == CombatType.MAGIC) {
                specialBonus = player.getCombatSpecial().getStrengthBonus();
            }

            maxHit = (int) baseDamage * specialBonus;

            if (victim.isNpc()) {
                if (((NPC) victim).getId() == player.getSlayer().getSlayerTask().getNpcId()) {
                    if (player.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() == 23071
                            || player.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() == 23069) {
                        maxHit *= 1.05;
                    } else if (player.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() == 23070) {
                        maxHit *= 1.07;
                    } else if (player.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() == 23074) {
                        maxHit *= 1.10;
                    } else if (player.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() == 23072) {
                        maxHit *= 1.15;
                    } else if (player.getEquipment().getItems()[Equipment.HEAD_SLOT].getId() == 23073) {
                        maxHit *= 1.25;
                    }
                }
            }

            if (victim.isNpc()) {
                if (((NPC) victim).getId() == player.getSlayer().getSlayerTask().getNpcId()
                        && victim.getConstitution() >= ((NPC) victim).getDefaultConstitution()) {
                    int percent = -1;
                    if (player.getEquipment().contains(22000)) {
                        percent = 2;
                    } else if (player.getEquipment().contains(22001)) {
                        percent = 4;
                    } else if (player.getEquipment().contains(22002)) {
                        percent = 6;
                    } else if (player.getEquipment().contains(22003)) {
                        percent = 8;
                    } else if (player.getEquipment().contains(22004)) {
                        percent = 10;
                    }
                    if (Misc.random(99) + 1 <= percent) {
                        maxHit = victim.getConstitution();
                    }
                }
            }
            maxHit = damageBoosted(player, maxHit);
        }

        maxHit *= 10;


        if (victim != null && victim.isNpc()) {
            maxHit = (int) NpcMaxHitLimit.limit((NPC) victim, maxHit, CombatType.RANGED);
        }
        return (int) Math.floor(maxHit);
    }

    public static double getEffectiveStr(Player player) {
        double styleBonus = 0;
        FightStyle style = player.getFightType().getStyle();

        double otherBonus = 1;

        double prayerMod = 1.0;
        double random = Math.random() * 10;
        if (PrayerHandler.isActivated(player, PrayerHandler.BURST_OF_STRENGTH) || CurseHandler.isActivated(player, CurseHandler.LEECH_STRENGTH)) {
            prayerMod = 1.05;
        } else if (PrayerHandler.isActivated(player, PrayerHandler.SUPERHUMAN_STRENGTH)) {
            prayerMod = 1.1;
        } else if (PrayerHandler.isActivated(player, PrayerHandler.ULTIMATE_STRENGTH)) {
            prayerMod = 1.15;
        } else if (PrayerHandler.isActivated(player, PrayerHandler.CHIVALRY)) {
            prayerMod = 1.18;
        } else if (PrayerHandler.isActivated(player, PrayerHandler.PIETY)) {
            prayerMod = 1.23;
        } else if (CurseHandler.isActivated(player, CurseHandler.TURMOIL)) {
            prayerMod = 1.25;
        }

        int number = (int) (player.getSkillManager().getCurrentLevel(Skill.STRENGTH) * prayerMod * otherBonus + styleBonus);
        return number;
    }

    public static double damageBoosted(Player plr, double number) {
        if (plr.getInventory().contains(22010)) {
            number *= 1.75;
        } else if (plr.getInventory().contains(22008)) {
            number *= 1.5;
        }
        if (plr.getEquipment().contains(22011) || plr.getEquipment().contains(22012)) {
            number *= 2;
        }
        return number;
    }

}

