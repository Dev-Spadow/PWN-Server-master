package com.arlania.world.content.achievements;

import com.arlania.world.entity.impl.player.Player;

public class DrReward implements NonItemReward{

    private double DR;

    public DrReward(double DR) {
        this.DR = DR;
    }

    @Override
    public void giveReward(Player player) {
        double currentDR = player.getAchievementDRBoost();
        player.setAchievementDRBoost(currentDR+DR);
    }

    @Override
    public String rewardDescription() {
        return "-" + DR + " DR increase.";
    }
}
