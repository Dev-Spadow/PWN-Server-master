package com.arlania.world.content.dailytasks;

import com.google.common.base.Objects;

public final class StreakReward {
    private final int points;
    private final int days;

    public StreakReward(final int points, final int days) {
        this.points = points;
        this.days = days;
    }

    public int getPoints() {
        return points;
    }

    public int getDays() {
        return days;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(points, days);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final StreakReward that = (StreakReward) o;
        return points == that.points && days == that.days;
    }
}
