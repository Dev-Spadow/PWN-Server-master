package com.arlania.world.content.boxspinner.impl;

import com.arlania.model.Item;
import com.arlania.world.content.boxspinner.MysteryBox;

import java.util.List;

public class TestMysteryBox extends MysteryBox {
    @Override
    public List<Item> commonRewards() {
        return List.of(new Item(298), new Item(4566), new Item(9925), new Item(9924), new Item(9923), new Item(9921), new Item(9922), new Item(1419));
    }

    @Override
    public List<Item> uncommonRewards() {
        return List.of( new Item(8026), new Item(8027), new Item(8028), new Item(8029), new Item(8030), new Item(8073),new Item(14987), new Item(14988), new Item(14989), new Item(14990), new Item(14991), new Item(14992), new Item(14994), new Item(14995), new Item(14996), new Item(14997), new Item(14998), new Item(15000), new Item(15001), new Item(15002), new Item(15004), new Item(15005), new Item(15006));
    }

    @Override
    public List<Item> rareRewards() {
        return List.of(new Item(14993), new Item(14999), new Item(15007), new Item(15011));
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
