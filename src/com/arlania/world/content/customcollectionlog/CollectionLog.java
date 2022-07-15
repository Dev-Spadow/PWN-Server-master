package com.arlania.world.content.customcollectionlog;

import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.stream.Collectors;

import com.arlania.world.content.interfaces.QuestTab;
import org.jetbrains.annotations.NotNull;

import com.arlania.model.Item;
import com.arlania.model.definitions.NPCDrops;
import com.arlania.model.definitions.NpcDefinition;
import com.arlania.world.entity.impl.player.Player;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

/**
 * @Author Suic
 * @Since 18.03.2020
 */
public class CollectionLog {

    private static CollectionLog instance = null;

    public static CollectionLog getInstance() {
        if (instance == null) {
            instance = new CollectionLog();
        }
        return instance;
    }
    
    public void load(Player player, JsonElement jsonElement) {
    	Gson gson = new Gson();
    	Type stringStringMap = new TypeToken<Map<Integer, Map<Integer, Integer>>>(){}.getType();
    	Map<Integer,Map<Integer, Integer>> collectedItems = gson.fromJson(jsonElement, stringStringMap);
    	for(Entry<Integer, Map<Integer, Integer>> entry : collectedItems.entrySet()) {
    	int npcId = entry.getKey();
        Map<Integer, Integer> item = entry.getValue();
        String itemKey = item.keySet().stream()
        	      .map(key -> key + "=" + item.get(key))
        	      .collect(Collectors.joining(", ", "{", "}"));

        for(int i = 0; i <= itemKey.split(",").length-1; i++) {
        String[] itemKeySplit = itemKey.split(",");
	    int id = Integer.parseInt(itemKeySplit[i].split("=")[0].replaceAll("[^A-Za-z0-9]", ""));
	    int amount = Integer.parseInt(itemKeySplit[i].split("=")[1].replaceAll("[^A-Za-z0-9]", ""));
	        
	    	if (player.collectedItems.get(npcId) == null) {
	            Map<Integer, Integer> itemData = new HashMap<>();
	            itemData.put(id, amount);
	            player.collectedItems.put(npcId, itemData);
	        } else {
	            player.collectedItems.get(npcId).merge(id, amount, Integer::sum);
	        }
	    	}
    	}
    }
    
    public static <T> List<T> stringToArray(String s, Class<T[]> clazz) {
        T[] arr = new Gson().fromJson(s, clazz);
        return Arrays.asList(arr); //or return Arrays.asList(new Gson().fromJson(s, clazz)); for a one-liner
    }

    private List<NpcDefinition> npcs = new ArrayList<>();
    private List<NpcDefinition> initialNpcs = new ArrayList<>();
    
    private List<NpcDefinition> customLog = new ArrayList<>();

    public int totalDropsToCollect = 0;
    


	public void checkCustomNpcs() {
		System.out.println("Loading custom npcs..");
		for(LogNpcs npc : LogNpcs.values()) {
			for(NpcDefinition npcz : NpcDefinition.getDefinitions()) {
				if(npcz == null)
					continue;
				if(npc.getNpcId() == npcz.getId()) {
					customLog.add(npcz);
				}
			}
		}
	}

    public void loadNpcs() {
    	System.out.println("Loading npcs.");
    	initialNpcs.addAll(customLog);
        /*Arrays.stream(NpcDefinition.getDefinitions())
                .filter(Objects::nonNull)
                .filter(def -> NPCDrops.forId(def.getId()) != null)
                //.filter(def -> def.getHitpoints() >= 1000)
                .forEach(initialNpcs::add);*/
        totalDropsToCollect = initialNpcs.size();
        System.out.println("Length: " + initialNpcs.size());
    }

    private int textStart = 30560;

    public void open(@NotNull Player player) {
        initNpcList();
        textStart = 30560;
        for(int i = 0; i < 150; i++) {
        	player.getPacketSender().sendString(textStart++, "");
        }
        textStart = 30560;
       // npcs.
        npcs.forEach(npc -> {
            player.getPacketSender().sendString(textStart++, npc.getName());
        });
        player.getPacketSender().sendInterface(30360);
    }

    public void handleDrop(@NotNull Player player, int npcId, Item item) {
        player.handleCollectedItem(npcId, item);
        QuestTab.updateCharacterSummaryTab(player, QuestTab.UpdateData.COLLECTIONLOG);
    }

    private void sendNpcData(@NotNull Player player, int index) {
        NpcDefinition npc = npcs.get(index);
        int id = npc.getId();
        player.getPacketSender().sendNpcOnInterface(30367, id, 0);
        NPCDrops.NpcDropItem[] drops = NPCDrops.forId(id).getDropList();
        int slot = 0;
        List<Integer> items = new ArrayList<>();
        player.getPacketSender().resetItemsOnInterface(30375, 20);
        player.getPacketSender().sendString(30369, "Killcount: " + player.getNpcKillCount(id));
        for (NPCDrops.NpcDropItem drop : drops) {
            int item = drop.getItem().getId();
            if (items.contains(item)) {
                continue;
            }

            if (drop.getChance()
                    .ordinal() >= 6) { // using ordinal because of how the DropChance enum is defined, theres really no better way.
          //System.out.println("amount for item " + item + ": " + getCollectedAmount(player, id, item));

            //ystem.out.println("amount for id " + id + ": " + getCollectedAmount(player, id, item));
                player.getPacketSender().sendItemOnInterface(30375, item, slot++, getCollectedAmount(player, id, item));
                items.add(item);
            }
        }
        if (!items.isEmpty()) {
           //player.getPacketSender().sendCollectedItems(getCollectedItems(player, id, items));
        }
    }
    

    private int getCollectedAmount(@NotNull Player player, int bossId, int itemId) {
        System.out.println("bossId "+bossId+" itemId "+itemId);
        System.out.println("collectedItems "+player.collectedItems);
        System.out.println("player.getCollectedItems() "+player.getCollectedItems());
        System.out.println("player.getCollectedItems().isEmpty() "+player.getCollectedItems().isEmpty());
        System.out.println("player.getCollectedItems().get(bossId) "+player.getCollectedItems().get(bossId));
        if (player.getCollectedItems().isEmpty() || player.getCollectedItems()
                .get(bossId) == null || player.getCollectedItems().get(bossId).get(itemId) == null) {
            return 0;
        }
        System.out.println("player.getCollectedItems().get(bossId).get(itemId)"+player.getCollectedItems().get(bossId).get(itemId));
        return player.getCollectedItems().get(bossId).get(itemId);
    }

    private List<Integer> getCollectedItems(@NotNull Player player, int id, List<Integer> drops) {
    	System.out.println("id "+id+" drops "+drops);
        List<Integer> collectedItems = new ArrayList<>();
        if (player.getCollectedItems().get(id) == null) {
            return Collections.emptyList();
        }
        Map<Integer, Integer> currentCollections = player.getCollectedItems().get(id);
        for (int drop : drops) {
            if (currentCollections.containsKey(drop)) {
                collectedItems.add(drop);
            }
        }
        return collectedItems;
    }

    private void initNpcList() {
        npcs.clear();
        npcs.addAll(initialNpcs);
    }

    public void search(Player player, String name) {
        initNpcList();
        npcs.removeIf(def -> !def.getName().toLowerCase().contains(name.toLowerCase()));

        textStart = 30560;
        for (int i = 0; i < 100; i++) {
            player.getPacketSender().sendString(textStart + i, "");
        }
        npcs.forEach(npc -> {
            player.getPacketSender().sendString(textStart++, npc.getName());
        });
    }

    public boolean handleButton(Player player, int buttonId) {
        if (!(buttonId >= 30560 && buttonId <= 30760)) {
            return false;
        }
        int index = -30560 + buttonId;
        if (npcs.size() > index) {
            sendNpcData(player, index);
        }
        return true;
    }
}
