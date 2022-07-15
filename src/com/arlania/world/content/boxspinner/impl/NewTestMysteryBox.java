package com.arlania.world.content.boxspinner.impl;

import com.arlania.model.Item;
import com.arlania.world.content.boxspinner.MysteryBox;

import java.util.List;

public class NewTestMysteryBox extends MysteryBox {
    @Override
    public List<Item> commonRewards() {
        return List.of(new Item(14084));
    }

    @Override
    public List<Item> uncommonRewards() {
        return List.of(new Item(16434), new Item(15255), new Item(15257), new Item(15256), new Item(15258), new Item(15268));
    }

    @Override
    public List<Item> rareRewards() {
        return List.of(new Item(13094), new Item(13095), new Item(4774), new Item(4776), new Item(4766), new Item(15008), new Item(15009), new Item(15010), new Item(11809));
    }

    @Override
    public int commonCopies() {
        return 25;
    }

    @Override
    public int uncommonCopies() {
        return 7;
    }

    @Override
    public int rareCopies() {
        return 3;
    }
}
