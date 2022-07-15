package com.arlania.world.content.battlepass;

public final class BattlePassReward {
    private final int index;
    private final int amount;

    public BattlePassReward(final int index, final int amount) {
        this.index = index;
        this.amount = amount;
    }

    public int getIndex() {
        return index;
    }

    public int getAmount() {
        return amount;
    }
}
