package com.arlania.world.content.keepsake;

public class KeepSakePreset {
    public String presetName;
    public int[] overrideItems;

    public KeepSakePreset(String presetName, int[] overrideItems) {
        this.presetName = presetName;
        this.overrideItems = overrideItems;
    }

    public void setPresetName(String presetName) {
        this.presetName = presetName;
    }
}
