package com.arlania.world.content;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.arlania.model.Item;
import com.arlania.model.definitions.NPCDrops;
import com.arlania.util.Misc;
import com.arlania.world.content.bosses.Bork;
import com.arlania.world.content.bosses.Apollyon;
import com.arlania.world.content.bosses.Customwrencher;
import com.arlania.world.content.bosses.DailyNpc;
import com.arlania.world.content.bosses.ElementalJad;
import com.arlania.world.content.bosses.FrostBeast;
import com.arlania.world.content.bosses.General;
import com.arlania.world.content.bosses.Newabbadon;
import com.arlania.world.content.bosses.SummerladyBoss;
import com.arlania.world.content.bosses.TheHarambe;
import com.arlania.world.content.bosses.TheVortex;
import com.arlania.world.content.bosses.TheZamorakLefosh;
import com.arlania.world.content.bosses.VeigarBoss;
import com.arlania.world.content.combat.CombatBuilder.CombatDamageCache;
import com.arlania.world.content.combat.CombatFactory;
import com.arlania.world.entity.impl.npc.NPC;
import com.arlania.world.entity.impl.player.Player;

public class NpcMultiDrops {

    private static final Map<Integer, Integer> validNpcs = new HashMap<>();
    
    static {
    	
        //9993, 10140, 10021, 10018
        validNpcs.put(4972, 25);
        validNpcs.put(68, 25);
        validNpcs.put(10010, 50); // npc id, amount of drops(max) here
        validNpcs.put(25, 50);
        validNpcs.put(4595, 50);
        validNpcs.put(2509, 50);
        validNpcs.put(12808, 50);
        validNpcs.put(12801, 50);
        validNpcs.put(12802, 50);
        validNpcs.put(2005, 50);
        validNpcs.put(11362, 50);
        validNpcs.put(2000, 5);
        validNpcs.put(2006, 5);
        validNpcs.put(11361, 3);
        validNpcs.put(8133, 25);
        validNpcs.put(15, 5);
        validNpcs.put(12823, 25);
        validNpcs.put(12101, 10);
        validNpcs.put(1382, 10);
        validNpcs.put(3154, 10);
        validNpcs.put(169, 10);
        validNpcs.put(9286, 3);
        validNpcs.put(11813, 5);
        validNpcs.put(7286, 5);
        validNpcs.put(50, 5);
        validNpcs.put(12836, 5);
        validNpcs.put(12805, 10);
        validNpcs.put(4543, 50);
        validNpcs.put(254, 25);
    }
    
    // now u need to define the drop table and then itll multi drop it multi dropped anyway :D
    // cause of ur old hardcoded system

    public static boolean isMultiDropNpc(NPC npc) {
        return validNpcs.keySet().stream().anyMatch(id -> id == npc.getId());
    }

    private static final SecureRandom random = new SecureRandom();

    public static boolean handleDrop(Player player, NPC npc) {

        if (npc.getCombatBuilder().getDamageMap().size() == 0) {
            return false;
        }

        if (!isMultiDropNpc(npc)) {
            return false;
        }

        Map<Player, Integer> killers = new HashMap<>();

        int dropCount = validNpcs.get(npc.getId());
        for (Entry<Player, CombatDamageCache> entry : npc.getCombatBuilder()
                .getDamageMap()
                .entrySet()) {

            if (entry == null) {
                continue;
            }

            long timeout = entry.getValue().getStopwatch().elapsed();

            if (timeout > CombatFactory.DAMAGE_CACHE_TIMEOUT) {
                continue;
            }

            player = entry.getKey();

            if (player.getConstitution() <= 0 || !player.isRegistered()) {
                continue;
            }

            killers.put(player, entry.getValue().getDamage());

        }

        npc.getCombatBuilder().getDamageMap().clear();

        List<Entry<Player, Integer>> result = sortEntries(killers);
        int count = 0;
        int highestDamage = result.get(0).getValue();
        for (Entry<Player, Integer> entry : result) {

            Player killer = entry.getKey();
            int damage = entry.getValue();
            
            System.out.println("killer="+ killer.getUsername());

            int percent = getPercent(damage, highestDamage);
                handleDrops(npc, killer, damage);

            if (++count >= dropCount) {
                break;
            }

        }
        
        if(npc.getId() == 10010)//Diablo world boss thingy
    		ElementalJad.setCurrent(null);
        if(npc.getId() == 12801)//Apollyon boss
    		Apollyon.setCurrent(null);
        if(npc.getId() == 12802)//Arctic frost beast
        	FrostBeast.setCurrent(null);
        if(npc.getId() == 4543)//Summer Event boss
        	SummerladyBoss.setCurrent(null);
        if(npc.getId() == 9911)//World boss
        	General.setCurrent(null);
        if(npc.getId() == 4595)//The Vip Skeleton
        	DailyNpc.setCurrent(null);
        if(npc.getId() == 2005)//Trainer May
        	TheVortex.setCurrent(null);
        if(npc.getId() == 11362)//LoL Veigar boss
        	VeigarBoss.setCurrent(null);
        if(npc.getId() == 12800)//Obsidian boss
        	Bork.setCurrent(null);
        if(npc.getId() == 68)//Starter boss Kalvoth
        	Customwrencher.setCurrent(null);
        if(npc.getId() == 2509)//Ironman boss
        	TheZamorakLefosh.setCurrent(null);
        if(npc.getId() == 25)//Sephiorth boss
        	TheHarambe.setCurrent(null);
        if(npc.getId() == 4972)//Virulent dragon boss
        	Newabbadon.setCurrent(null);
        return true;
    }

    private static int getPercent(int currentNumber, int maxNumber) {
        return Math.min((currentNumber * 100) / maxNumber, 100);
    }


    private static void handleDrops(NPC npc, Player player, int damage) {
        Item item = NPCDrops.dropItemsMulti(player, npc);
        if (item == null) { // if it's null somehow
            return;
        }
        if(player.isMiniPlayer()) {
        	player = player.getOwner();
        }
        String itemName = item.getDefinition().getName();
        String itemMessage = Misc.anOrA(itemName) + " " + itemName;
        if (!player.getLocalPlayers().contains(player)) {
    //        player.sendMessage("<img=10><col=FF0000>" + player.getUsername() + " received " + itemMessage + " from the " + npc.getDefinition().getName());
        }
        player.getLocalPlayers().forEach(localPlayer -> {
    //        localPlayer.sendMessage("<img=10><col=FF0000>" + localPlayer.getUsername() + " received " + itemMessage + " from the " + npc.getDefinition().getName());
        });
    }

    private static <K, V extends Comparable<? super V>> List<Entry<K, V>> sortEntries(Map<K, V> map) {

        List<Entry<K, V>> sortedEntries = new ArrayList<>(map.entrySet());

        sortedEntries.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));

        return sortedEntries;

    }


}

