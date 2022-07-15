package com.arlania.world.content.levelsystem;

import com.arlania.model.Item;
import com.arlania.world.content.interfaces.QuestTab;
import com.arlania.world.entity.impl.player.Player;

/**
 * @author Brad July 9, 2022 @6:16pm
 *
 * Handles the level up system revolving around NPC Kills.
 */

public class LevelSystem {
    static int maxLevel = 100;
    public static int determineKillsToNextLevel(Player plr) {
        int baseKills = 100;
        int level = plr.getKillLevel();
        int prestige = plr.getKillPrestige();
        return (baseKills * level) * (prestige + 1);
    }

    public static void appendExperience(Player plr) {
        if (plr.getKillLevel() == (maxLevel * (plr.getKillPrestige() + 1))) {
            prestige(plr);
            return;
        }
        plr.addKillExperience();
        int experience = plr.getKillExperience();
        if (experience >= determineKillsToNextLevel(plr)) {
            levelUp(plr);
        }
    }

    public static void levelUp(Player plr) {
        plr.setKillExperience(0);
        plr.addKillLevel();
        plr.getPacketSender().sendMessage("Congratulations you have just reached a kill level of: @red@" + plr.getKillLevel());
        appendLevelReward(plr);
        QuestTab.updateCharacterSummaryTab(plr, QuestTab.UpdateData.KILL_LEVEL);
    }

    public static void prestige(Player plr) {
        int deteremineLevel = maxLevel * (plr.getKillPrestige() + 1);
        if (plr.getKillLevel() != deteremineLevel) {
            plr.getPacketSender().sendMessage("You require a level of " + deteremineLevel + " to prestige!");
            return;
        }
        plr.setKillExperience(0);
        plr.setKillLevel(1);
        plr.addKillPrestige();
        plr.getPacketSender().sendMessage("Congratulations you have just reached a kill prestige of: @red@" + plr.getKillPrestige());
        QuestTab.updateCharacterSummaryTab(plr, QuestTab.UpdateData.KILL_PRESTIGE);
    }

    public static void appendLevelReward(Player plr) {
        int quantity = 100 * plr.getKillLevel();
        if (plr.getInventory().getFreeSlots() > 0) {
            plr.getInventory().add(new Item(10835, quantity));
        } else {
            plr.getBank().add(new Item(10835, quantity));
        }
    }

    public static void appendPrestigeReward(Player plr) {
        //TODO: Implement prestige rewards
    }
}
