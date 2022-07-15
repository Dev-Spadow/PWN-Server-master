package com.arlania.world.content.interfaces;

import com.arlania.GameSettings;
import com.arlania.util.Misc;
import com.arlania.world.content.EvilTrees;
import com.arlania.world.content.ShootingStar;
import com.arlania.world.content.WellOfGoodwill;
import com.arlania.world.content.battlepass.BattlePass;
import com.arlania.world.content.customcollectionlog.CollectionLog;
import com.arlania.world.content.event.SpecialEvents;
import com.arlania.world.entity.impl.player.Player;

public class QuestTab {

    public enum UpdateData {
        RANK, KILL_LEVEL, KILL_PRESTIGE, NPC_KILLS, BATTLEPASS, ACHIEVEMENTS, COLLECTIONLOG, PLAYTIME
    }

    public static void showCharacterSummary(Player plr) {
        plr.getPacketSender().sendString(45012, plr.getUsername());
        plr.getPacketSender().sendString(45026, Integer.toString(plr.getKillLevel()));
        plr.getPacketSender().sendString(45041, Integer.toString(plr.getKillPrestige()));
        plr.getPacketSender().sendString(45071, Integer.toString(plr.getNpcKills()));
        plr.getPacketSender().sendString(45086, BattlePass.INSTANCE.getBattlePassLevel(plr) + "/100");
        plr.getPacketSender().sendString(45101, plr.getAchievementTracker().totalCompleted() + "/" + plr.getAchievementTracker().totalAchievements());
        plr.getPacketSender().sendString(45116, plr.getCollectedItems().size() + "/" + CollectionLog.getInstance().totalDropsToCollect);
        plr.getPacketSender().sendString(45130, "Play Time: @gr1@" + determineTotalPlayTime(plr));
    }

    public static void showQuestTab(Player plr) {
        plr.getPacketSender().sendString(45303, "@or2@Server Time: @gre@"+ Misc.getCurrentServerTime());
    }

    public static void showEventsTab(Player plr) {
        String[] Messages = new String[] {
                "@or2@Evil Tree: @or1@"+(EvilTrees.getLocation() != null ? EvilTrees.getLocation().playerPanelFrame : "N/A"),
                "@or2@Well of Goodwill:@or1@" +(WellOfGoodwill.isActive() ? WellOfGoodwill.getMinutesRemaining() + " mins" : "N/A"),
                "@or2@Crashed Star:@or1@" +(ShootingStar.getLocation() != null ?ShootingStar.getLocation().playerPanelFrame : "N/A"),
                "@or2@Daily Bonus:@gre@" + SpecialEvents.getSpecialDay(),
        };

        for (int i = 0; i < Messages.length; i++) {
            plr.getPacketSender().sendString(45502 + i, Messages[i]);

        }
    }

    public static void updateCharacterSummaryTab(Player plr, UpdateData data) {
        switch (data) {
            case RANK:
                plr.getPacketSender().sendString(45012, plr.getRights().getRank() + plr.getUsername() + plr.getRights().getRank());
            break;
            case KILL_LEVEL:
                plr.getPacketSender().sendString(45026, Integer.toString(plr.getKillLevel()));
            break;
            case KILL_PRESTIGE:
                plr.getPacketSender().sendString(45041, Integer.toString(plr.getKillPrestige()));
            break;
            case NPC_KILLS:
                plr.getPacketSender().sendString(45071, Integer.toString(plr.getNpcKills()));
            break;
            case BATTLEPASS:
                plr.getPacketSender().sendString(45086, BattlePass.INSTANCE.getBattlePassLevel(plr) + "/100");
            break;
            case ACHIEVEMENTS:
                plr.getPacketSender().sendString(45101, plr.getAchievementTracker().totalCompleted() + "/" + plr.getAchievementTracker().totalAchievements());
            break;
            case COLLECTIONLOG:
                plr.getPacketSender().sendString(45116, plr.getCollectedItems().size() + "/" + CollectionLog.getInstance().totalDropsToCollect);
            break;
            case PLAYTIME:
                plr.getPacketSender().sendString(45131, determineTotalPlayTime(plr));
            break;
        }
    }

    public static void sendSideBar(Player plr, int id) {
        switch (id) {
            case 45011:
                showCharacterSummary(plr);
                break;
            case 45300:
               showQuestTab(plr);
            break;
            case 45500:
                showEventsTab(plr);
            break;

        }
        plr.getPacketSender().sendTabInterface(GameSettings.QUESTS_TAB, id);
    }

    public static String determineTotalPlayTime(Player plr) {
        long currentPlayTime = plr.getTotalPlayTime() + plr.getRecordedLogin().elapsed();
        final int sec = (int) (currentPlayTime / 1000), d = sec / 86400, h = sec / 3600, m = sec / 60 % 60, s = sec % 60;
        if (d > 0) {
            return d + " days, " + (h > 0 ? h : "0" + h)  + " hours";
        }
        if (h > 0) {
            return (h > 0 ? h : "0" + h) + " hours, " + (m > 0 ? m : "0" + m)  + " minutes";
        }
        return (m > 0 ? m : "0" + m) + " minutes ";
    }
}
