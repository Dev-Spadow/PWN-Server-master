package com.arlania.world.content.dailytasks;

import com.google.common.base.Objects;

public final class TaskShopItem {
    private final int index;
    private final int amount;
    private final int cost;
    private final String name;
    private final String description;

    public TaskShopItem(final int index, final int amount, final int cost, final String name, final String description) {
        this.index = index;
        this.amount = amount;
        this.cost = cost;
        this.name = name;
        this.description = description;
    }

    public int getIndex() {
        return index;
    }

    public int getAmount() {
        return amount;
    }

    public int getCost() {
        return cost;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(index, amount, cost, name, description);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final TaskShopItem that = (TaskShopItem) o;
        return index == that.index && amount == that.amount && cost == that.cost && Objects.equal(name, that.name) && Objects.equal(description, that.description);
    }
}
