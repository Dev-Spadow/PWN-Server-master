package com.arlania.world.content.achievements;

import com.arlania.net.packet.PacketSender;
import com.arlania.world.entity.impl.player.Player;

import java.util.Arrays;

public class AchievementInterface {
    private static final int INTERFACE_ID = 11290;
    private static final int COLLECT_BUTTON = 11299;
    private static final int CLOSE_BUTTON = 11296;
    private static final int EASY_BUTTON = 11304;
    private static final int MED_BUTTON = 11305;
    private static final int HARD_BUTTON = 11306;
    private static final int DAILY_BUTTON = 11307;
    private static final int TAB_CONFIG_ID = 1086;
    private static final int SLOTS = 80;
    private static final int START_LIST_ID = 11691;
    private static final int NAME_ID = 11302;
    private static final int SUB_NAME_ID = 11303;
    private static final int DESCRIPTION_ID = 11312;
    private static final int POINTS_ID = 11313;
    private static final int ITEMS_CONTAINER_ID = 11314;
    private static final int PROGRESS_BAR_WIDTH = 274;
    private static final int PROGRESS_BAR_ID = 11791;
    private static final int PROGRESS_TEXT_ID = 11316;


    private final Player player;
    private AchievementType achievementType = AchievementType.EASY;
    private AchievementData selected = Arrays.stream(AchievementData.values).filter(a -> a.type.equals(achievementType)).findFirst().get();

    private AchievementInterface(Player player) {
        this.player = player;
    }

    public static void open(Player player) {
        AchievementInterface achievementInterface = new AchievementInterface(player);
        player.setAchievementInterface(achievementInterface);
        achievementInterface.open();
    }

    private void open() {
        player.getPacketSender().sendInterface(INTERFACE_ID);
        refresh();
    }

    private void refresh() {
        PacketSender out = player.getPacketSender();
        out.sendConfig(TAB_CONFIG_ID, achievementType.ordinal());
        AchievementData[] achievements = AchievementData.getAchievementsOfType(achievementType);

        for (int i = 0; i < SLOTS; i++) {
            if (i < achievements.length) {
                AchievementData achievement = achievements[i];
                out.sendString(START_LIST_ID + 1 + i * 2, getAchievementColor(achievement) + achievement.toString());
            } else {
                out.sendString(START_LIST_ID + 1 + i * 2, "");
            }
        }

        out.sendString(NAME_ID, selected.toString());
        out.sendString(SUB_NAME_ID, selected.type.toString());
        System.out.println("selected.description: " + selected.description);
        out.sendString(DESCRIPTION_ID, selected.description);
        int done = player.getAchievementTracker().getProgressFor(selected);
        int total = selected.progressAmount;
        int percent = 100 * done / total;
        int width = PROGRESS_BAR_WIDTH * done / total;
        out.sendString(PROGRESS_TEXT_ID, percent + "%" + " | " + done + "/" + total);
        out.sendInterfaceComponentMoval(width - PROGRESS_BAR_WIDTH, 0, PROGRESS_BAR_ID);
        out.resetItemsOnInterface(ITEMS_CONTAINER_ID, 9);
        out.sendCombinerItemsOnInterface(ITEMS_CONTAINER_ID, selected.itemRewards);
        System.out.println("getPointsString(selected.nonItemRewards): " + getPointsString(selected.nonItemRewards));
        out.sendString(POINTS_ID, getPointsString(selected.nonItemRewards));
    }

    private String getAchievementColor(AchievementData achievement) {
        int done = player.getAchievementTracker().getProgressFor(achievement);
        if (done == 0) {
            return "@red@";
        }

        if (done != achievement.progressAmount) {
            return "@yel@";
        }

        if (player.getAchievementTracker().hasCollected(achievement)) {
            return "@gre@";
        }
        return "@gr1@";
    }

    private String getPointsString(NonItemReward[] nonItemRewards) {
        StringBuilder sb = new StringBuilder();
        for (NonItemReward reward : nonItemRewards) {
            sb.append(reward.rewardDescription()).append("\\n");
        }
        return sb.toString();
    }

    public boolean handleButton(int id) {
        if (id == CLOSE_BUTTON) {
            player.getPacketSender().removeAllWindows();
            player.setAchievementInterface(null);
            return true;
        }
        if (id == COLLECT_BUTTON) {
            tryCollectReward();
            return true;
        }
        if (id == EASY_BUTTON) {
            achievementType = AchievementType.EASY;
            refresh();
            return true;
        }
        if (id == MED_BUTTON) {
            achievementType = AchievementType.MEDIUM;
            refresh();
            return true;
        }
        if (id == HARD_BUTTON) {
            achievementType = AchievementType.HARD;
            refresh();
            return true;
        }
        if (id == DAILY_BUTTON) {
            achievementType = AchievementType.DAILY;
            refresh();
            return true;
        }
        if (id >= START_LIST_ID && id <= START_LIST_ID + SLOTS * 2) {
            trySelectAchievement((id - START_LIST_ID) / 2);
            return true;
        }
        return false;
    }

    private void trySelectAchievement(int index) {
        AchievementData[] achievements = AchievementData.getAchievementsOfType(achievementType);
        if (index >= achievements.length)
            return;
        selected = achievements[index];
        refresh();
    }

    private void tryCollectReward() {
        int done = player.getAchievementTracker().getProgressFor(selected);
        if (done < selected.progressAmount) {
            player.sendMessage("You have not completed this achievement yet.");
            return;
        }
        if (player.getAchievementTracker().hasCollected(selected)) {
            player.sendMessage("You have already collected the reward for this achievement.");
            return;
        }
        player.getAchievementTracker().setCollected(selected);
        player.getInventory().add(selected.itemRewards);
        for (NonItemReward reward : selected.nonItemRewards) {
            reward.giveReward(player);
        }
        refresh();
    }
}
