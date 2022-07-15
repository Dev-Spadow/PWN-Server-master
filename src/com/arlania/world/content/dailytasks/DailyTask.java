package com.arlania.world.content.dailytasks;

import com.google.common.base.Objects;

public final class DailyTask {

    private final TaskType type;
    private final TaskCategory category;
    private final String key;
    private final String description;
    private final String name;
    private final int amount;
    private final int reward;
    private final int index;
    private final int displayIndex;
    private final int displayAmount;
    private final int broadcastAmount;

    public DailyTask(final TaskType type, final TaskCategory category, final String key, final String description, final String name, final int amount, final int reward, final int index, final int displayIndex, final int displayAmount, final int broadcastAmount) {
        this.type = type;
        this.category = category;
        this.key = key;
        this.description = description;
        this.name = name;
        this.amount = amount;
        this.reward = reward;
        this.index = index;
        this.displayIndex = displayIndex;
        this.displayAmount = displayAmount;
        this.broadcastAmount = broadcastAmount;
    }

    public TaskType getType() {
        return type;
    }

    public TaskCategory getCategory() {
        return category;
    }

    public String getKey() {
        return key;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public int getAmount() {
        return amount;
    }

    public int getReward() {
        return reward;
    }

    public int getIndex() {
        return index;
    }

    public int getDisplayIndex() {
        return displayIndex;
    }

    public int getDisplayAmount() {
        return displayAmount;
    }

    public int getBroadcastAmount() {
        return broadcastAmount;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final DailyTask dailyTask = (DailyTask) o;
        return amount == dailyTask.amount && reward == dailyTask.reward && index == dailyTask.index && displayIndex == dailyTask.displayIndex && displayAmount == dailyTask.displayAmount && broadcastAmount == dailyTask.broadcastAmount && type == dailyTask.type && category == dailyTask.category && Objects.equal(key, dailyTask.key) && Objects.equal(description, dailyTask.description) && Objects.equal(name, dailyTask.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(type, category, key, description, name, amount, reward, index, displayIndex, displayAmount, broadcastAmount);
    }
}
