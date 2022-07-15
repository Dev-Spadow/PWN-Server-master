package com.arlania.world.content.battlepass;

public final class BattlePassNPC {
    private final int index;
    private final int xp;

    public BattlePassNPC(final int index, final int xp) {
        this.index = index;
        this.xp = xp;
    }

    public int getIndex() {
        return index;
    }

    public int getXp() {
        return xp;
    }
}
