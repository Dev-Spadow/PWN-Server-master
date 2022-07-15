package com.arlania.world.content;

import com.google.common.collect.ImmutableSet;

import java.util.Optional;
import java.util.Set;

public enum PetPerk {

    PET_ABYZOU_HEARTWRENCHER(6315, 25, 1.2, 1.1, true, 1.5, 2.0),
    KILLERCHUCKY(2322, 0, 1.1, 1.2, false, 1.1, 1.1),
    BABYYODA(230, 0, 1.2, 1.0, true, 1.25, 1.2),
    ABBADON(6304, 40, 2, 2.0, true, 2.0, 2.0),
    ANTMAN(811, 2, 0.0, 1.25, false, 1.0, 1.0),
    DATBOI(13463, 0, 0.0, 1.4, false, 1.0, 1.0),
    RAINBOW_EEVEE(5960, 0, 0.0, 1.3, false, 1.0, 1.0),
    YOSHI_PET(12812, 25, 1, 1, true, 1.0, 1.0),
    SHENRON(12213, 0, 0.0, 1.7, false, 1.0, 1.0);
    private static final Set<PetPerk> cachedValues = ImmutableSet.copyOf(values());
    private final int npcId;
    private final int dropRateBonus;
    private final double xpBonus;
    private final double damageBonus;
    private final boolean lootEffect;
    private final double prayDrainRate;
    private final double hpDrainRate;

    PetPerk(final int npcId, final int dropRateBonus, final double xpBonus, final double damageBonus, final boolean lootEffect, final double prayDrainRate, final double hpDrainRate) {
        this.npcId = npcId;
        this.dropRateBonus = dropRateBonus;
        this.xpBonus = xpBonus;
        this.damageBonus = damageBonus;
        this.lootEffect = lootEffect;
        this.prayDrainRate = prayDrainRate;
        this.hpDrainRate = hpDrainRate;
    }

    public static Optional<PetPerk> forId(final int npcId) {
        for (final PetPerk petPerk : cachedValues) {
            if (petPerk.npcId == npcId) {
                return Optional.of(petPerk);
            }
        }
        return Optional.empty();
    }

    public static Set<PetPerk> getCachedValues() {
        return cachedValues;
    }

    public int getNpcId() {
        return npcId;
    }

    public int getDropRateBonus() {
        return dropRateBonus;
    }

    public double getXpBonus() {
        return xpBonus;
    }

    public double getDamageBonus() {
        return damageBonus;
    }

    public boolean hasLootEffect() {
        return lootEffect;
    }

    public double getPrayDrainRate() {
        return prayDrainRate;
    }

    public double getHpDrainRate() {
        return hpDrainRate;
    }
}
