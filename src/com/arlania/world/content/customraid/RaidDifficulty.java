package com.arlania.world.content.customraid;

public enum RaidDifficulty {
    EASY(2825, 3795, "Easy Yugioh Raids\\n 4 Bosses With different \\n Mechanics"),
    HARD(2825, 3795, "NOT YET AVAILABLE");

    RaidDifficulty(int x, int y, String description) {
        this.x = x;
        this.y = y;
        this.description = description;
    }

    private final int x;
    private final int y;
    private final String description;


    public String getDescription() {
        return description;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
