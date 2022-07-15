package com.arlania.world.content.combat;

import com.arlania.world.entity.impl.npc.NPC;

public class NpcMaxHitLimit {

    public static double limit(NPC npc, double damage, CombatType type) {
        int maxLimit;
        switch (npc.getId()) {
            // case 187:  // goku
            //case 8009: // hulk
            case 99999999:
                maxLimit = 500;
                break;
            default:
                return damage;
        }
        return Math.min(maxLimit, damage);
    }
}