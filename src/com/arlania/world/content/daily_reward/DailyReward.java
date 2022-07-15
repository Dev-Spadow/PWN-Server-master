package com.arlania.world.content.daily_reward;

import com.arlania.model.Item;
import com.arlania.world.entity.impl.player.Player;

import java.util.ArrayList;

public class DailyReward {
    Player player;

    private Item todaysItem;
    private Item tomorrowsItem;

    private long nextRewardTime;
    private int day = 0;

    public DailyReward(Player player) {
        this.player = player;
    }

    public void openInterface() {
        String message = "The 31 Days of Rewards contains a bunch of rewards \\n" + "for you to claim! Login every day to claim your reward. \\n \\n \\n" +
                "Note that this is limited to one per user, which means, \\n" + "you cannot make multiple accounts to claim rewards. \\n \\n \\n" +
                "Every month has a new reward list, so make sure to \\n" + "stick around!";
        player.getPA().sendString(DailyRewardConstants.DESCRIPTION, message);

        for (int i = 0; i < Rewards.loot.length; i++) {
            Item item = Rewards.loot[i];
            player.getPA().sendItemOnInterface(DailyRewardConstants.REWARD_CONTAINER, item.getId(), i, item.getAmount());
        }

        if (System.currentTimeMillis() >= nextRewardTime && player.getClaimedTodays()) {
            player.setClaimedTodays(false);
            day++;
        }

        int index = getDay();
        todaysItem = Rewards.loot[index];
        if ((index + 1) >= Rewards.loot.length) {
            tomorrowsItem = Rewards.loot[0];
        } else {
            tomorrowsItem = Rewards.loot[index + 1];
        }

        player.getPA().sendItemOnInterface(DailyRewardConstants.TODAYS_AWARD, todaysItem.getId(), 0, todaysItem.getAmount());
        player.getPA().sendItemOnInterface(DailyRewardConstants.TOMORROWS_AWARD, tomorrowsItem.getId(), 0, tomorrowsItem.getAmount());

        long hostRewardTime = getHost();

        if (System.currentTimeMillis() >= hostRewardTime) {
            player.getPA().sendString(DailyRewardConstants.BUTTON_DESCRIPTION, "Claim Reward");
        } else {
            player.getPA().sendString(DailyRewardConstants.BUTTON_DESCRIPTION, "@red@      " + timeLeft(hostRewardTime));
        }

        player.getPA().sendInterface(DailyRewardConstants.MAIN_INTERFACE);

    }

    public String timeLeft(long time) {
        long durationInMillis = time - System.currentTimeMillis();
        long minute = (durationInMillis / (1000 * 60)) % 60;
        long hour = (durationInMillis / (1000 * 60 * 60)) % 24;

        if (durationInMillis <= 0) {
            return "Claim Reward";
        } else {
            return String.format("%02d:%02d", hour, minute);
        }
    }

    public void claimReward() {
        openInterface();
        long hostRewardTime = getHost();
        if (!(System.currentTimeMillis() >= nextRewardTime)) {
            player.sendMessage("You have already claimed your reward for today! Please wait @red@" + timeLeft(hostRewardTime) + "@bla@.");
            return;
        }

        if (hostRewardTime != -1 && System.currentTimeMillis() < hostRewardTime) {
            player.sendMessage("You have already claimed your reward for today!");
            return;
        }

        player.getInventory().add(todaysItem.getId(), todaysItem.getAmount());
        nextRewardTime = System.currentTimeMillis() + (1000 * 60 * 60 * 24);
        player.setClaimedTodays(true);
        writeHost();
        player.sendMessage("Thank you for all of your support! <col=8505C4>Enjoy your reward</col>.");
    }

    private final String SAVING_DIRECTORY = "./data/rewards/hosts.txt";

    public void writeHost() {
        ArrayList<String> toRead = Util.readFile(SAVING_DIRECTORY);

        for (String line : new ArrayList<>(toRead)) {
            if (line.startsWith(player.getHostAddress() + "#=#")) {
                toRead.remove(line);
            }
        }

        toRead.add(player.getHostAddress() + "#=#" + nextRewardTime);

        Util.deleteAllLines(SAVING_DIRECTORY);
        Util.saveArrayContentsSilent(SAVING_DIRECTORY, toRead);
    }

    public long getHost() {
        ArrayList<String> toRead = Util.readFile(SAVING_DIRECTORY);
        for (int index = 0; index < toRead.size(); index++) {
            String parse[] = toRead.get(index).split("#=#");
            String hostAddress = parse[0];
            long rewardTime = Long.valueOf(parse[1]);

            if (hostAddress.equalsIgnoreCase(player.getHostAddress())) {
                return rewardTime;
            }
        }
        return -1;
    }

    public void checkToResetDay() {
        if (day >= 31) {
            day = 0;
        }
    }

    public long getNextRewardTime() {
        return nextRewardTime;
    }

    public void setNextRewardTime(long nextRewardTime) {
        this.nextRewardTime = nextRewardTime;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getDay() {
        checkToResetDay();
        return day;
    }

    public boolean hasClaimedTodaysReward() {
        if (player.getClaimedTodays() == false) {
            return false;
        } else {
            return true;
        }
    }

}