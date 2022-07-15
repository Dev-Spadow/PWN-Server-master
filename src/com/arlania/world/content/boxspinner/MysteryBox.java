package com.arlania.world.content.boxspinner;

import com.arlania.model.Item;

import java.util.List;

public abstract class MysteryBox {
    public abstract List<Item> commonRewards();
    public abstract List<Item> uncommonRewards();
    public abstract List<Item> rareRewards();

    public abstract int commonCopies();
    public abstract int uncommonCopies();
    public abstract int rareCopies();
}
