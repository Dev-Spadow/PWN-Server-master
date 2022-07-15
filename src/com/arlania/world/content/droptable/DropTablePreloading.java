package com.arlania.world.content.droptable;

import com.arlania.model.definitions.NPCDrops;
import com.arlania.model.definitions.NpcDefinition;

import java.util.*;

public class DropTablePreloading {
    public static List<Integer> possibleNPCs = new ArrayList<>();

    public static void loadPossibleNPCs() {
        for (int npcId : NPCDrops.getDrops().keySet()) {
            possibleNPCs.add(npcId);
        }
    }
}
