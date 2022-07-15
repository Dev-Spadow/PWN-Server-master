package com.arlania.model.definitions;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

import com.arlania.GameSettings;
import com.arlania.model.Graphic;
import com.arlania.model.GroundItem;
import com.arlania.model.Item;
import com.arlania.model.PlayerRights;
import com.arlania.model.Position;
import com.arlania.model.Skill;
import com.arlania.model.Locations.Location;
import com.arlania.model.container.impl.Bank;
import com.arlania.util.JsonLoader;
import com.arlania.util.Misc;
import com.arlania.util.RandomUtility;
import com.arlania.world.World;
import com.arlania.world.content.DropLog;
import com.arlania.world.content.NpcMultiDrops;
import com.arlania.world.content.PetPerk;
import com.arlania.world.content.PlayerLogs;
import com.arlania.world.content.WellOfWealth;
import com.arlania.world.content.DropLog.DropLogEntry;
import com.arlania.world.content.bosses.HourlyBoss;
import com.arlania.world.content.clan.ClanChatManager;
import com.arlania.world.content.customcollectionlog.CollectionLog;
import com.arlania.world.content.minigames.impl.WarriorsGuild;
import com.arlania.world.content.skill.impl.prayer.BonesData;
import com.arlania.world.content.skill.impl.summoning.CharmingImp;
import com.arlania.world.content.skill.impl.summoning.Familiar;
import com.arlania.world.entity.impl.GroundItemManager;
import com.arlania.world.entity.impl.npc.NPC;
import com.arlania.world.entity.impl.player.Player;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Controls the npc drops
 *
 * @author 2012 <http://www.rune-server.org/members/dexter+morgan/>, Gabbe &
 * Samy
 */
public class NPCDrops {

    /**
     * The map containing all the npc drops.
     */
    private static Map<Integer, NPCDrops> dropControllers = new HashMap<Integer, NPCDrops>();

    public static JsonLoader parseDrops() {
        // System.out.println("CALLED");
        ItemDropAnnouncer.init();

        return new JsonLoader() {

            @Override
            public void load(JsonObject reader, Gson builder) {
                int[] npcIds = builder.fromJson(reader.get("npcIds"), int[].class);
                NpcDropItem[] drops = builder.fromJson(reader.get("drops"), NpcDropItem[].class);

                NPCDrops d = new NPCDrops();
                d.npcIds = npcIds;
                d.drops = drops;
                for (int id : npcIds) {
                    dropControllers.put(id, d);
                    // System.out.println("Drops put: " + id + " . " + d);
                    // System.out.println("put: "+id+" "+d);
                }

            }

            @Override
            public String filePath() {
                return "./data/def/json/drops.json";
            }
        };
    }

    /**
     * The id's of the NPC's that "owns" this class.
     */
    private int[] npcIds;

    /**
     * All the drops that belongs to this class.
     */
    private NpcDropItem[] drops;

    /**
     * Gets the NPC drop controller by an id.
     *
     * @return The NPC drops associated with this id.
     */
    public static NPCDrops forId(int id) {
        return dropControllers.get(id);
    }

    public static Map<Integer, NPCDrops> getDrops() {
        return dropControllers;
    }

    /**
     * Gets the drop list
     *
     * @return the list
     */
    public NpcDropItem[] getDropList() {
        return drops;
    }

    /**
     * Gets the npcIds
     *
     * @return the npcIds
     */
    public int[] getNpcIds() {
        return npcIds;
    }

    /**
     * Represents a npc drop item
     */
    public static class NpcDropItem {

        /**
         * The id.
         */
        private final int id;

        /**
         * Array holding all the amounts of this item.
         */
        private final int[] count;

        /**
         * The chance of getting this item.
         */
        private final int chance;

        /**
         * New npc drop item
         *
         * @param id     the item
         * @param count  the count
         * @param chance the chance
         */
        public NpcDropItem(int id, int[] count, int chance) {
            this.id = id;
            this.count = count;
            this.chance = chance;
        }

        /**
         * Gets the item id.
         *
         * @return The item id.
         */
        public int getId() {
            return id;
        }

        /**
         * Gets the chance.
         *
         * @return The chance.
         */
        public int[] getCount() {
            return count;
        }

        /**
         * Gets the chance.
         *
         * @return The chance.
         */
        public DropChance getChance() {
            switch (chance) {

                case 1:
                    return DropChance.ALMOST_ALWAYS; // 50% <-> 1/2
                case 2:
                    return DropChance.VERY_COMMON; // 20% <-> 1/5
                case 3:
                    return DropChance.COMMON; // 5% <-> 1/20
                case 4:
                    return DropChance.UNCOMMON; // 2% <-> 1/50
                case 5:
                    return DropChance.RARE; // 0.5% <-> 1/200
                case 6:
                    return DropChance.LEGENDARY; // 0.2% <-> 1/500
                case 7:
                    return DropChance.LEGENDARY_2;
                case 8:
                    return DropChance.LEGENDARY_3;
                case 9:
                    return DropChance.LEGENDARY_4;
                case 10:
                    return DropChance.LEGENDARY_5;
                case 11:
                    return DropChance.LEGENDARY_6;
                case 12:
                    return DropChance.LEGENDARY_7;
                case 13:
                    return DropChance.LEGENDARY_8;
                case 14:
                    return DropChance.LEGENDARY_9;
                case 15:
                    return DropChance.LEGENDARY_10;
                case 16:
                    return DropChance.LEGEN;
                case 17:
                    return DropChance.INYUASHA;
                case 18:
                    return DropChance.BEGINNER_1;
                case 19:
                    return DropChance.BEGINNER_2;
                case 20:
                    return DropChance.BEGINNER_3;
                case 21:
                    return DropChance.BEGINNER_4;
                case 22:
                    return DropChance.BEGINNER_5;
                case 23:
                    return DropChance.BEGINNER_6;
                case 24:
                    return DropChance.BEGINNER_7;
                case 25:
                    return DropChance.BEGINNER_8;
                case 26:
                    return DropChance.BEGINNER_9;
                case 27:
                    return DropChance.BEGINNER_10;
                case 28:
                    return DropChance.BEGINNER_11;
                case 29:
                    return DropChance.BEGINNER_12;
                case 1000:
                    return DropChance.URU;
                case 1001:
                    return DropChance.DIABLO_COMMON;
                case 1002:
                    return DropChance.DIABLO_UNCOMMON;
                case 1003:
                    return DropChance.DIABLO_RARE;
                case 1004:
                    return DropChance.DIABLO_LUCKY;
                case 1005:
                    return DropChance.DIABLO_RNG;
                case 1006:
                    return DropChance.SEPHIROTH_UNCOMMON;
                case 1007:
                    return DropChance.SEPHIROTH_RARE;
                case 1008:
                    return DropChance.IRONMAN_UNCOMMON;
                case 1009:
                    return DropChance.IRONMAN_RARE;
                case 1010:
                    return DropChance.THANOS_UNCOMMON;
                case 1011:
                    return DropChance.THANOS_RARE;
                case 1012:
                    return DropChance.APOLLYON_UNCOMMON;
                case 1013:
                    return DropChance.APOLLYON_RARE;
                case 1014:
                    return DropChance.ARCTICBEAST_UNCOMMON;
                case 1015:
                    return DropChance.ARCTICBEAST_RARE;
                case 1016:
                    return DropChance.SUMMER_UNCOMMON;
                case 1017:
                    return DropChance.SUMMER_RARE;
                case 1018:
                    return DropChance.SUMMER_LUCKY;
                case 1019:
                    return DropChance.SUMMER_ECO;
                case 1020:
                    return DropChance.RAJIN_LUCKY;
                case 1021:
                    return DropChance.SZONE_UNCOMMON;
                case 1022:
                    return DropChance.SZONE_ECO;
                case 1023:
                    return DropChance.SZONE_LUCKY;
                case 1024:
                    return DropChance.ZEUS_UNCOMMON;
                case 1025:
                    return DropChance.ZEUS_RARE;
                case 1026:
                    return DropChance.ZEUS_LUCKY;
                case 1027:
                    return DropChance.NEX_UNCOMMON;
                case 1028:
                    return DropChance.NEX_RARE;
                case 1029:
                    return DropChance.NEX_LUCKY;
                default:
                    return DropChance.ALWAYS; // 100% <-> 1/1
            }
        }

        public WellChance getWellChance() {
            switch (chance) {
                case 1:
                    return WellChance.ALMOST_ALWAYS; // 50% <-> 1/2
                case 2:
                    return WellChance.VERY_COMMON; // 20% <-> 1/5
                case 3:
                    return WellChance.COMMON; // 5% <-> 1/20
                case 4:
                    return WellChance.UNCOMMON; // 2% <-> 1/50
                case 5:
                    return WellChance.RARE; // 0.5% <-> 1/200
                case 6:
                    return WellChance.LEGENDARY; // 0.2% <-> 1/500
                case 7:
                    return WellChance.LEGENDARY_2;
                case 8:
                    return WellChance.LEGENDARY_3;
                case 9:
                    return WellChance.LEGENDARY_4;
                case 10:
                    return WellChance.LEGENDARY_5;
                case 11:
                    return WellChance.LEGENDARY_6;
                case 12:
                    return WellChance.LEGENDARY_7;
                case 13:
                    return WellChance.LEGENDARY_8;
                case 14:
                    return WellChance.LEGENDARY_9;
                case 15:
                    return WellChance.LEGEN;
                case 16:
                    return WellChance.INYUASHA;
                case 1000:
                    return WellChance.URU;
                case 1001:
                    return WellChance.DIABLO_COMMON;
                case 1002:
                    return WellChance.DIABLO_UNCOMMON;
                case 1003:
                    return WellChance.DIABLO_RARE;
                case 1004:
                    return WellChance.DIABLO_LUCKY;
                case 1005:
                    return WellChance.DIABLO_RNG;
                case 1006:
                    return WellChance.SEPHIROTH_UNCOMMON;
                case 1007:
                    return WellChance.SEPHIROTH_RARE;
                case 1008:
                    return WellChance.IRONMAN_UNCOMMON;
                case 1009:
                    return WellChance.IRONMAN_RARE;
                case 1010:
                    return WellChance.THANOS_UNCOMMON;
                case 1011:
                    return WellChance.THANOS_RARE;
                case 1012:
                    return WellChance.APOLLYON_UNCOMMON;
                case 1013:
                    return WellChance.APOLLYON_RARE;
                case 1014:
                    return WellChance.ARCTICBEAST_UNCOMMON;
                case 1015:
                    return WellChance.ARCTICBEAST_RARE;
                case 1016:
                    return WellChance.SUMMER_UNCOMMON;
                case 1017:
                    return WellChance.SUMMER_RARE;
                case 1018:
                    return WellChance.SUMMER_LUCKY;
                case 1019:
                    return WellChance.SUMMER_ECO;
                case 1020:
                    return WellChance.RAJIN_LUCKY;
                case 1021:
                    return WellChance.SZONE_UNCOMMON;
                case 1022:
                    return WellChance.SZONE_ECO;
                case 1023:
                    return WellChance.SZONE_LUCKY;
                case 1024:
                    return WellChance.ZEUS_UNCOMMON;
                case 1025:
                    return WellChance.ZEUS_RARE;
                case 1026:
                    return WellChance.ZEUS_LUCKY;
                case 1027:
                    return WellChance.NEX_UNCOMMON;
                case 1028:
                    return WellChance.NEX_RARE;
                case 1029:
                    return WellChance.NEX_LUCKY;
                default:
                    return WellChance.ALWAYS; // 100% <-> 1/1
            }
        }

        /**
         * Gets the item
         *
         * @return the item
         */
        public Item getItem() {
            int amount = 0;
            for (int i = 0; i < count.length; i++)
                amount += count[i];
            if (amount > count[0])
                amount = count[0] + RandomUtility.getRandom(count[1]);
            return new Item(id, amount);
        }
    }

    
    public enum DropChance {
    //	 6                 7                8              9               10             11
    	//18               19               20            21               22             23
        BEGINNER_1(125),BEGINNER_2(250),BEGINNER_3(350),BEGINNER_4(550),BEGINNER_5(750),BEGINNER_6(900),
        
    	//24               25               26                 27                 28                    29
        BEGINNER_7(1000),BEGINNER_8(1500),BEGINNER_9(2000),BEGINNER_10(2500),BEGINNER_11(3000),BEGINNER_12(4000),
        NEX_UNCOMMON(150),NEX_RARE(250),NEX_LUCKY(750),
        ZEUS_UNCOMMON(25),ZEUS_RARE(75),ZEUS_LUCKY(150),
        SZONE_UNCOMMON(25),SZONE_RARE(35),SZONE_ECO(50),SZONE_LUCKY(100),
        SUMMER_UNCOMMON(50),SUMMER_RARE(100), SUMMER_LUCKY(250),SUMMER_ECO(500),RAJIN_LUCKY(1000),
        ARCTICBEAST_UNCOMMON(250),ARCTICBEAST_RARE(500),
    	APOLLYON_UNCOMMON(150),APOLLYON_RARE(400),
    	THANOS_UNCOMMON(100),THANOS_RARE(250),
    	SEPHIROTH_UNCOMMON(150),SEPHIROTH_RARE(250),  IRONMAN_UNCOMMON(150),IRONMAN_RARE(300),
        ALWAYS(0), ALMOST_ALWAYS(10), VERY_COMMON(25), COMMON(75), UNCOMMON(200), NOTTHATRARE(300), RARE(400), URU(15000),DIABLO_COMMON(10),DIABLO_UNCOMMON(250),DIABLO_RARE(500),DIABLO_LUCKY(850),DIABLO_RNG(1000),
        LEGENDARY(800), LEGENDARY_2(1000), LEGENDARY_3(1500), LEGENDARY_4(2000), LEGENDARY_5(4000), LEGENDARY_6(6000), LEGENDARY_7(8000), LEGENDARY_8(9000), LEGENDARY_9(10000),LEGEN(11000),INYUASHA(14000), LEGENDARY_10(12500);
    	
        DropChance(int randomModifier) {
            this.random = randomModifier;
        }

        private int random;

        public int getRandom() {
            return this.random;
        }
    }

    public enum WellChance {
        NEX_UNCOMMON(150),NEX_RARE(250),NEX_LUCKY(750),
        ZEUS_UNCOMMON(25),ZEUS_RARE(75),ZEUS_LUCKY(150),
        SZONE_UNCOMMON(25),SZONE_RARE(35),SZONE_ECO(50),SZONE_LUCKY(100),
        SUMMER_UNCOMMON(50),SUMMER_RARE(100), SUMMER_LUCKY(250),SUMMER_ECO(500),RAJIN_LUCKY(1000),
        ARCTICBEAST_UNCOMMON(250),ARCTICBEAST_RARE(500),
    	APOLLYON_UNCOMMON(150),APOLLYON_RARE(400),
    	THANOS_UNCOMMON(100),THANOS_RARE(250),
    	SEPHIROTH_UNCOMMON(150),SEPHIROTH_RARE(250),  IRONMAN_UNCOMMON(150),IRONMAN_RARE(300),
        ALWAYS(0), ALMOST_ALWAYS(2), VERY_COMMON(25), COMMON(75), UNCOMMON(200), NOTTHATRARE(300), RARE(400), URU(15000),DIABLO_COMMON(10),DIABLO_UNCOMMON(250),DIABLO_RARE(500),DIABLO_LUCKY(850),DIABLO_RNG(1000),
        LEGENDARY(800), LEGENDARY_2(1000), LEGENDARY_3(1500), LEGENDARY_4(2000), LEGENDARY_5(4000), LEGENDARY_6(6000), LEGENDARY_7(8000), LEGENDARY_8(9000), LEGENDARY_9(10000),LEGEN(11000),INYUASHA(14000), LEGENDARY_10(12500);
    	
        WellChance(int randomModifier) {
            this.random = randomModifier;
        }

        private int random;

        public int getRandom() {
            return this.random;
        }
    }

    /**
     * Drops items for a player after killing an npc. A player can max receive one
     * item per drop chance.
     *
     * @param p   Player to receive drop.
     * @param npc NPC to receive drop FROM.
     */
    public static void dropItems(Player p, NPC npc) {
        if (npc.getLocation() == Location.WARRIORS_GUILD)
            WarriorsGuild.handleDrop(p, npc);
        NPCDrops drops = NPCDrops.forId(npc.getId());
        if (drops == null)
            return;
        final boolean goGlobal = p.getPosition().getZ() >= 0 && p.getPosition().getZ() < 4;
        final Position npcPos = npc.getPosition().copy();
        boolean[] dropsReceived = new boolean[12];

        if (drops.getDropList().length > 0 && p.getPosition().getZ() >= 0 && p.getPosition()
            .getZ() < 4) {

            casketDrop(p, npc.getDefinition().getCombatLevel(), npcPos);
        }
        if (drops.getDropList().length > 0 && p.getPosition().getZ() >= 0 && p.getPosition()
            .getZ() < 4) {
            clueDrop(p, npc.getDefinition().getCombatLevel(), npcPos);

        }
		int npcId = npc.getId();


		
        if (npc.getDefaultConstitution() > 10000) {
            //System.out.println("DROPPING");
            dropScratchcard(p, p.getPosition());
        }

        rollDropTable(false, p, drops.getDropList().clone(), npc, npcPos, goGlobal);

        if (WellOfWealth.isActive())
            rollDropTable(true, p, drops.getDropList(), npc, npcPos, goGlobal);

    }
    
    public static Item dropItemsMulti(Player p, NPC npc) {
        if (npc.getLocation() == Location.WARRIORS_GUILD)
            WarriorsGuild.handleDrop(p, npc);
        NPCDrops drops = NPCDrops.forId(npc.getId());
        if (drops == null)
            return null;
        final boolean goGlobal = p.getPosition().getZ() >= 0 && p.getPosition().getZ() < 4;
        final Position npcPos = npc.getPosition().copy();
        boolean[] dropsReceived = new boolean[12];

        if (drops.getDropList().length > 0 && p.getPosition().getZ() >= 0 && p.getPosition()
            .getZ() < 4) {

            casketDrop(p, npc.getDefinition().getCombatLevel(), npcPos);
        }
        if (drops.getDropList().length > 0 && p.getPosition().getZ() >= 0 && p.getPosition()
            .getZ() < 4) {
            clueDrop(p, npc.getDefinition().getCombatLevel(), npcPos);

        }
		int npcId = npc.getId();

		
        if (npc.getDefaultConstitution() > 10000) {
            //System.out.println("DROPPING");
            dropScratchcard(p, p.getPosition());
        }
        
        Item drop = rollDropTable(false, p, drops.getDropList().clone(), npc, npcPos, goGlobal);

        if (WellOfWealth.isActive())
            drop = rollDropTable(true, p, drops.getDropList(), npc, npcPos, goGlobal);
        
        return drop;

    }

    public static Collection<Integer> getNpcList(String name) {
        List<Integer> items = new ArrayList<>();
        for (int index = 0; index < ItemDefinition.getDefinitions().length; index++) {
            ItemDefinition def = ItemDefinition.forId(index);
            if (def == null || !def.getName().toLowerCase().equals(name)) {
                continue;
            }
            items.add(def.getId());
            break;
        }
        if (items.isEmpty()) {
            System.out.println("No such item.");
            return new ArrayList<>();
        }
        Collection<Integer> npcs = new ArrayList<>();
        for (NPCDrops npc_drop : dropControllers.values()) {
            for (NpcDropItem dropped_item : npc_drop.drops) {
                for (Integer cached_item : items) {
                    if (dropped_item.getId() == cached_item) {
                        for (int npc_id : npc_drop.getNpcIds()) {
                            if (npcs.contains(npc_id)) {
                                continue;
                            }
                            npcs.add(npc_id);
                        }
                        continue;
                    }
                }
            }
        }
        return npcs;
    }
    
    public static ArrayList<Item> getAllAlwaysDrops(Player player, NpcDropItem[] drops, boolean isWell) {
    	ArrayList<Item> items = new ArrayList<Item>();
        int playerDr = DropUtils.drBonus(player, false);
    	
    	for (int i = 0; i < drops.length; i++) {
            int chance = isWell ? drops[i].getWellChance().getRandom() : drops[i].getChance().getRandom();
            int adjustedDr = (int) Math.floor(chance / (playerDr > 0 ? (DropUtils.drBonus(player, false) / 100.0) + 1 : 1)) + (playerDr > 0 ? 1 : 0);

    		if (drops[i].getChance() == DropChance.ALWAYS || adjustedDr == 1) {
                Item drop = drops[i].getItem();
                if (player.isDoubleDropsActive())
                    drop = new Item(drop.getId(), drop.getAmount() * getDropAmntMult(player));
                
                items.add(drop);
    		}
    	}
    	
    	return items;
    }

    public static Item rollDropTable(boolean isWell, Player player, NpcDropItem[] drops, NPC npc, Position npcPos,
        boolean goGlobal) {
        boolean hasRecievedDrop = false;
        int playerDr = DropUtils.drBonus(player, false);
		int playerKills = player.getNpcKills();

        if (npc.getId() == HourlyBoss.currentHourlyBoss) playerDr += 20;

        Arrays.sort(drops, (a, b) -> b.getChance().compareTo(a.getChance()));

        	       boolean forceLoot = 
           		npc.getId() == 10010 ||
                npc.getId() == 4595 ||
           		npc.getId() == 9911||
           		npc.getId() == 2005||
           		npc.getId() == 2509||
           		npc.getId() == 25||
           		npc.getId() == 11362||
           		npc.getId() == 68||
           		npc.getId() == 4543||
           		npc.getId() == 4972|| 
           		npc.getId() == 12802||
           		npc.getId() == 12808||
           		npc.getId() == 12801;
        	       
        ArrayList<Item> alwaysItems = getAllAlwaysDrops(player, drops, isWell);
        
        for(Item alwaysDrop : alwaysItems)
            drop(player, alwaysDrop, npc, npcPos, goGlobal);

        
        
        if(NpcMultiDrops.isMultiDropNpc(npc)) {
        	playerDr = 0;
        }
        
        if(forceLoot) {
        	while(hasRecievedDrop == false) {
        		for (int i = 0; i < drops.length; i++) {
    	            int chance = isWell ? drops[i].getWellChance().getRandom() : drops[i].getChance().getRandom(); // why is this the same im noto sure tbh let me show u whats the issue tho
    	            if(NpcMultiDrops.isMultiDropNpc(npc)) {
    	            	playerDr = 0;
    	            }
    	            
    	            int adjustedDr = (int) Math.floor(chance / (playerDr > 0 ? (DropUtils.drBonus(player, false) / 100.0) + 1 : 1)) + (playerDr > 0 ? 1 : 0);
    	        	
    	            if(drops[i].getChance() == DropChance.ALWAYS || adjustedDr == 1) continue;
    	            
    	            if (RandomUtility.getRandom(adjustedDr) == 1 && !hasRecievedDrop) {
    	                Item drop = drops[i].getItem();
    	                if (player.isDoubleDropsActive())
    	                    drop = new Item(drop.getId(), drop.getAmount() * getDropAmntMult(player));
    	                drop(player, drop, npc, npcPos, goGlobal);
    	                hasRecievedDrop = true;
    	                return drop;
    	         
    	            }
    	
    	        }
        	}
        } else {
	        for (int i = 0; i < drops.length; i++) {
	            int chance = isWell ? drops[i].getWellChance().getRandom() : drops[i].getChance().getRandom();
	            if(NpcMultiDrops.isMultiDropNpc(npc)) {
	            	playerDr = 0;
	            }

	            int adjustedDr = (int) Math.floor(chance / (playerDr > 0 ? (DropUtils.drBonus(player, false) / 100.0) + 1 : 1)) + (playerDr > 0 ? 1 : 0);

	            if(drops[i].getChance() == DropChance.ALWAYS || adjustedDr == 1) continue;
	
	            if (RandomUtility.getRandom(adjustedDr) == 1 && !hasRecievedDrop) {
	                Item drop = drops[i].getItem();
	                if (player.isDoubleDropsActive())
	                    drop = new Item(drop.getId(), drop.getAmount() * getDropAmntMult(player));
	                drop(player, drop, npc, npcPos, goGlobal);
	                hasRecievedDrop = true;
	                return drop;
	         
	            }
	
	        }
        }
        
        return null;

    }

    public static int getDropAmntMult(Player player) {
        int multiplier = 1;

        if (GameSettings.DOUBLE_DROPS)
            multiplier += 1;
        if (player.isDoubleDropsActive())
            multiplier += 1;

        return multiplier;
    }

    public static void drop(Player player, Item item, NPC npc, Position pos, boolean goGlobal) {

        if(!player.receiveDrop) {
            //Make sure its set back to true just incase...
            player.receiveDrop = true;
            System.out.println("Did not receive a drop because group...");
            return;
        }

        if(player.isMiniPlayer()) {
        	player = player.getOwner();
        }

        if (npc.getId() == 227 || npc.getId() == 2043 || npc.getId() == 2044) {
            pos = player.getPosition();
        }
        Familiar pet = player.getSummoning().getFamiliar();
        if (pet != null) {
            final Optional<PetPerk> perk = PetPerk.forId(pet.getSummonNpc().getId());
            if (perk.isPresent() && perk.get().hasLootEffect()) {
                if (item.getDefinition().isNoted())
                    item.setId(Item.getUnNoted(item.getId()));
                int tab = Bank.getTabForItem(player, item.getId());
                player.getBank(tab).add(item.getId(), item.getAmount());
                //player.sendMessage("Added x" +item.getAmount()+item+" to bank because of pet perk");
                return;
            }
        }
        boolean isWearingCollector = DropUtils.hasCollItemEquipped(player);
        
        player.recievedDD = true;

        if (isWearingCollector && !player.getBlockedCollectorsList().contains(item.getId())) {
            if (player.getInventory().isFull())
                player.getBank().add(item);
            else
                player.getInventory().add(item);

        } else {
        	//System.out.println("loot="+ item.getId() +", player="+ player.getUsername() +", pos="+ pos);
            GroundItemManager.spawnGroundItem(player, new GroundItem(item, pos, player.getUsername(), false, 150, goGlobal, 200));
        }

        if (player.getInventory().contains(18337) && BonesData.forId(item.getId()) != null) {
            player.getPacketSender().sendGlobalGraphic(new Graphic(777), pos);
            player.getSkillManager().addExperience(Skill.PRAYER, BonesData.forId(item.getId()).getBuryingXP());
            return;
        }
        int itemId = item.getId();
        int amount = item.getAmount();


        if (player.getInventory().contains(6821)) {
            int value = item.getDefinition().getValue();
            int formula = value * amount;

            player.getPacketSender().sendMessage("@or2@Your drop has been converted to coins!");
            player.getInventory().add(995, formula);
        }



        if (npc.getId() == 1999 || npc.getId() == 9993 || npc.getId() == 11 || npc.getId() == 9944 || npc
            .getId() == 9273 || npc.getId() == 9903 || npc.getId() == 8133 || npc.getId() == 9247 || npc
            .getId() == 8493 || npc.getId() == 9203 || npc.getId() == 172 || npc.getId() == 9935 || npc
            .getId() == 170)
            if (Misc.inclusiveRandom(1, 2000) == 44 || Misc.inclusiveRandom(1, 2000) == -44) {
                player.sendMessage("@blu@[RARE DROP]: You have received a $5 Bond");
                player.getInventory().add(19935, 1);
            }
        if (npc.getId() == 3154 || npc.getId() == 219 || npc.getId() == 169 || npc.getId() == 170)
            if (npc.getId() == 9944 || npc.getId() == 9273 || npc.getId() == 9247)
                if (Misc.inclusiveRandom(1, 2000) == 45 || Misc.inclusiveRandom(1, 2000) == -45) {
                    player.sendMessage("@blu@[RARE DROP]: You have received a $10 bond");
                    player.getInventory().add(19936, 1);
                }    
            
		if (Misc.inclusiveRandom(1, 750) == 42 || Misc.inclusiveRandom(1, 750) == -42) {
			player.sendMessage("@blu@[RARE DROP]: You have received an AoE Instance Voucher!");
            player.getInventory().add(5606, 1);
		}

		if (Misc.inclusiveRandom(1, 1500) == 100 || Misc.inclusiveRandom(1, 1500) == 100) {
			player.sendMessage("@blu@[RARE DROP]: You have received an Diamond box");
            player.getInventory().add(10478, 1);
		}
        Player toGive = player;

        boolean ccAnnounce = false;
        if (Location.inMulti(player)) {
            if (player.getCurrentClanChat() != null && player.getCurrentClanChat().getLootShare()) {
                CopyOnWriteArrayList<Player> playerList = new CopyOnWriteArrayList<Player>();
                for (Player member : player.getCurrentClanChat().getMembers()) {
                    if (member != null) {
                        if (member.getPosition().isWithinDistance(player.getPosition())) {
                            playerList.add(member);
                        }
                    }
                }
                if (playerList.size() > 0) {
                    toGive = playerList.get(RandomUtility.getRandom(playerList.size() - 1));
                    if (toGive == null || toGive.getCurrentClanChat() == null
                        || toGive.getCurrentClanChat() != player.getCurrentClanChat()) {
                        toGive = player;
                    }
                    ccAnnounce = true;
                }
            }
        }

        if (itemId == 18778) { // Effigy, don't drop one if player already has one
            if (toGive.getInventory().contains(18778) || toGive.getInventory().contains(18779)
                || toGive.getInventory().contains(18780) || toGive.getInventory().contains(18781)) {
                return;
            }
            for (Bank bank : toGive.getBanks()) {
                if (bank == null) {
                    continue;
                }
                if (bank.contains(18778) || bank.contains(18779) || bank.contains(18780) || bank.contains(18781)) {
                    return;
                }
            }
        }


        if (ItemDropAnnouncer.announce(itemId)) {
            String itemName = item.getDefinition().getName();
            String itemMessage = Misc.anOrA(itemName) + " " + itemName;
            String npcName = Misc.formatText(npc.getDefinition().getName());

            if (player.getRights() == PlayerRights.DEVELOPER) {
                GroundItemManager.spawnGroundItem(toGive,
                    new GroundItem(item, pos, toGive.getUsername(), false, 150, goGlobal, 200));
            }
            switch (itemId) {
                case 14484:
                    itemMessage = "a pair of Dragon Claws";
                    break;
                case 20000:
                case 20001:
                case 20002:
                    itemMessage = itemName;
                    break;
            }
            switch (npc.getId()) {
                case 81:
                    npcName = "a Cow";
                    break;
                case 50:
                case 3200:
                case 8133:
                case 4540:
                case 1160:
                case 8549:
                    npcName = "The " + npcName + "";
                    break;
                case 51:
                case 54:
                case 5363:
                case 8349:
                case 1592:
                case 1591:
                case 1590:
                case 1615:
                case 9463:
                case 9465:
                case 9467:
                case 1382:
                case 13659:
                case 11235:
                    npcName = "" + Misc.anOrA(npcName) + " " + npcName + "";
                    break;
            }

            String message = "<img=382><col=eaeaea>[<col=FF0000>RARE DROP<col=eaeaea>]<img=382><col=eaeaea> " + toGive
                .getUsername() + " has just received <img=386><col=07b481>" + itemMessage
                + "<img=386><col=eaeaea> from <col=FF0000>" + npcName + "!";

            World.sendMessage(message);

            if (ccAnnounce) {
                ClanChatManager.sendMessage(player.getCurrentClanChat(),
                    "<col=16777215>[<col=255>Lootshare<col=16777215>]<col=3300CC> " + toGive.getUsername()
                        + "  has just received <img=386><col=07b481> " + itemMessage + "<img=386><col=eaeaea> from <col=FF0000>" + npcName + "!");
            }

            PlayerLogs.log(toGive.getUsername(),
                "" + toGive.getUsername() + " received " + itemMessage + " from " + npcName + "");
        }
        CollectionLog.getInstance().handleDrop(player, npc.getId(), item);
        DropLog.submit(toGive, new DropLogEntry(itemId, item.getAmount()));
    }

    public static void casketDrop(Player player, int combat, Position pos) {
        if (Misc.inclusiveRandom(1, 25) == 10 || Misc.inclusiveRandom(1, 25) == -10) {
            player.getInventory().add(405, 1);

        }
    }

    private static final int[] CLUESBOY = new int[]{19626};

    public static void clueDrop(Player player, int combat, Position pos) {
        if (Misc.inclusiveRandom(1, 150) == 15 || Misc.inclusiveRandom(1, 150) == -15) {
            player.getInventory().add(19626, 1);
            player.getPacketSender()
                .sendMessage("<img=382> You have received @mag@<shad=9f199d>a mysterious scroll</shad>");
        }
    }

    private static void dropScratchcard(Player player, Position pos) {
        int chance = RandomUtility.inclusiveRandom(0, 1000);

        if (chance <= 999) {
            return;
        }

        GroundItemManager.spawnGroundItem(player,
            new GroundItem(new Item(19935, 1), pos, player.getUsername(), false, 150, true, 200));

        player.sendMessage("<img=382> You have received @mag@<shad=9f199d>a $5 bond</shad>");
    }

    public static class ItemDropAnnouncer {

        private static List<Integer> ITEM_LIST;

        private static final int[] TO_ANNOUNCE = new int[]{

            19999//Supreme Nex

        };

        private static void init() {
            ITEM_LIST = new ArrayList<Integer>();
            for (int i : TO_ANNOUNCE) {
                ITEM_LIST.add(i);
            }
        }

        public static boolean announce(int item) {
            return ITEM_LIST.contains(item);
        }
    }

    public void setDrops(NpcDropItem[] array) {
        // TODO Auto-generated method stub

    }

	public static double getDoubleDr(Player player, boolean b) {
		// TODO Auto-generated method stub
		return 0;
	}





	}
