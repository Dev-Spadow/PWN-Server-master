package com.arlania.world.content.playersettings;


import java.util.Arrays;

public enum PlayerSetting {
    MOVING_TEXTURES(0, 0), SHOW_PERK_OVERLAYS(0, 1),
    RANK_HEAD_ICONS(1, 2), SHOW_FOG(1, 3),
    SHOW_PARTICLES(1, 4), SHOW_PRAYER_OVERHEADS(1, 5),
    GROUND_ITEM_NAMES(1, 6), SMOOTH_SHADING(1, 7),
    SHOW_PROGRESSION_OVERLAY(1, 8), SHOW_GROUND_DECORATIONS(1, 9),
    VIEW_DISTANCE(25, 10), TEXTURE_ANIMATION_SPEED(3, 11),
    FOG_START_VALUE(2500, 12), FOG_COLOR(0, 13);

    private final int defaultValue;
    private final int index;

    PlayerSetting(int defaultValue, int index) {
        this.defaultValue = defaultValue;
        this.index = index;
    }

    public int getDefaultValue() {
        return defaultValue;
    }

    public int getIndex() {
        return index;
    }

    public static final PlayerSetting[] VALUES = values();

    public static PlayerSetting getSetting(int index) {
        return Arrays.stream(VALUES).filter(s -> s.getIndex() == index).findFirst().orElse(null);
    }

    public static final int TOGGLEABLES = 10;
}
