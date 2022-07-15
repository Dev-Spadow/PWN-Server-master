package com.arlania.world.content.boxspinner;

import com.arlania.model.Item;
import com.arlania.world.content.boxspinner.impl.NewTestMysteryBox;
import com.arlania.world.content.boxspinner.impl.TestMysteryBox;
import com.arlania.world.entity.impl.player.Player;

import java.util.*;
import java.util.stream.IntStream;

public class MysteryBoxHandler {

    private final Map<Integer, MysteryBox> mysteryBoxes = new HashMap<>();

    public void load() {
        mysteryBoxes.put(6504, new TestMysteryBox());
        mysteryBoxes.put(6503, new NewTestMysteryBox());
    }

    public boolean open(Player player, int id, boolean spin) {
        MysteryBox box = mysteryBoxes.get(id);
        if (box == null) {
            return false;
        }

        if (spin) {
            player.getCustomBoxSpinner().open(id, box);
        }

        return true;
    }

    public List<Item> getItems(MysteryBox box) {
        List<Item> items = new ArrayList<>();
        List<Item> common = new ArrayList<>();
        List<Item> uncommon = new ArrayList<>();
        List<Item> rare = new ArrayList<>();

        IntStream.range(0, box.commonCopies()).forEach(x -> common.addAll(box.commonRewards()));
        IntStream.range(0, box.uncommonCopies())
            .forEach(x -> uncommon.addAll(box.uncommonRewards()));
        IntStream.range(0, box.rareCopies()).forEach(x -> rare.addAll(box.rareRewards()));

        items.addAll(common);
        items.addAll(uncommon);
        items.addAll(rare);

        Collections.shuffle(items);
        return items;
    }

    private static MysteryBoxHandler instance = null;

    public static MysteryBoxHandler getInstance() {
        if (instance == null) {
            instance = new MysteryBoxHandler();
        }

        return instance;
    }

    private MysteryBoxHandler() {

    }
}
