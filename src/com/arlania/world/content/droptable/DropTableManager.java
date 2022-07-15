package com.arlania.world.content.droptable;
 
import com.arlania.model.Item;
import com.arlania.model.definitions.DropUtils;
import com.arlania.model.definitions.NPCDrops;
import com.arlania.model.definitions.NpcDefinition;
import com.arlania.model.input.impl.EnterClanChatToJoin;
import com.arlania.model.input.impl.EnterSearchDropTable;
import com.arlania.world.entity.impl.player.Player;
 
import java.util.*;
 
public class DropTableManager {
 
    private Player player;
    private ArrayList<Integer> viewableNpcs = new ArrayList<>();
 
    private long lastClick;
 
    public DropTableManager(Player player) {
        this.player = player;
    }
 
    public void open() {
        resetInterface();
        showNpcList(false);
        player.getPacketSender().sendInterface(35000);
    }
 
    public void handleSearch(String value) {
        if (value.length() < 3) {
            player.sendMessage("You need to provide at-least 3 characters for a search.");
            return;
        }
 
        viewableNpcs.clear();
 
        value.toLowerCase();
 
        for (int possibleNpcId : DropTablePreloading.possibleNPCs) {
            String currentPossibleNPC = NpcDefinition.forId(possibleNpcId).getName().toLowerCase();
            if (currentPossibleNPC.contains(value)) {
                viewableNpcs.add(possibleNpcId);
            }
        }
 
        showNpcList(true);
 
        if (viewableNpcs.size() == 1) {
            showDropTable(viewableNpcs.get(0), false);
        } else {
            resetInterface();
        }
 
        if (viewableNpcs.size() <= 0) {
            player.sendMessage("Sorry, we couldn't find that query.");
        } else {
            player.sendMessage("The search results are shown above.");
        }
    }
 
    private void handleNpcClick(int buttonId) {
        int index = buttonId + 30485;
 
        if (index < 0 || (viewableNpcs.size() - 1) < index) {
            return;
        }
 
        int npcId = viewableNpcs.get(index);
 
        showDropTable(npcId, false);
    }
 
    private void showNpcList(boolean afterSearch) {
        for (int i = 0; i < 50; i++) {
            player.getPacketSender().sendString(35151 + i, "");
        }
 
        if (!afterSearch) {
            DropTableSavedNPCs[] savedNPCs = DropTableSavedNPCs.values();
            for (int i = 0; i < savedNPCs.length; i++) {
                viewableNpcs.add(savedNPCs[i].getNpcId());
                player.getPacketSender().sendString(35151 + i, NpcDefinition.forId(savedNPCs[i].getNpcId()).getName());
            }
        } else {
            for (int i = 0; i < viewableNpcs.size(); i++) {
                player.getPacketSender().sendString(35151 + i, NpcDefinition.forId(viewableNpcs.get(i)).getName());
            }
        }
    }
 
    public void showDropTable(int npcId, boolean fromRightClick) {
        if (fromRightClick) {
            open();
        }
 
        Map<NPCDrops.DropChance, List<Item>> dropTables = new TreeMap<>(Collections.reverseOrder());
        NPCDrops drops = NPCDrops.forId(npcId);
        NPCDrops.NpcDropItem[] items = drops.getDropList();
 
        Arrays.sort(items, new Comparator<NPCDrops.NpcDropItem>() {
            @Override
            public int compare(NPCDrops.NpcDropItem first, NPCDrops.NpcDropItem second) {
                if (second.getChance().getRandom() != first.getChance().getRandom()) {
                    return second.getChance().getRandom() - first.getChance().getRandom();
                }
 
                return first.getItem().getDefinition().getName().compareTo(second.getItem().getDefinition().getName());
            }
        });
 
        for (NPCDrops.NpcDropItem droppedItem : items) {
            NPCDrops.DropChance chance = droppedItem.getChance();
            List<Item> table = dropTables.get(chance);
 
            if (table == null) {
                dropTables.put(chance, table = new ArrayList<>());
            }
 
            boolean found = false;
            for (Item drop : table) {
                if (drop != null && drop.getId() == droppedItem.getId()) {
                    found = true;
                    break;
                }
            }
 
            if (!found) {
                int amount = arrayMax(droppedItem.getCount());
                if (amount >= 1 && amount <= Integer.MAX_VALUE) {
                    table.add(new Item(droppedItem.getId(), amount));
                }
            }
        }
 
        resetInterface();
        player.getPacketSender().sendString(35002, "Drop Table - @yel@" + NpcDefinition.forId(npcId).getName());
 
        int myDropRate = DropUtils.drBonus(player, true);
 
        int index = 0;
        for (Map.Entry<NPCDrops.DropChance, List<Item>> entry : dropTables.entrySet()) {
            NPCDrops.DropChance chance = entry.getKey();
            List<Item> dropTable = entry.getValue();
 
            if (dropTable.size() == 0) {
                continue;
            }
 
            int randomChance = chance.getRandom();
 
            for (Item droppedItem : dropTable) {
                String quantityText = Integer.toString(droppedItem.getAmount());
                String rateText = "1/1";
 
                if (randomChance > 0) {
                    rateText = "1/" + randomChance;
                }
 
                String myRateText;
                double newDropRate = randomChance / (DropUtils.drBonus(player, true)> 0 ? ((DropUtils.drBonus(player, true) / 100.0) + 1.0) : 1);
 
                if (newDropRate >= 1.0) {
                    myRateText = "1/" + (int) newDropRate;
                } else {
                    myRateText = "1/1";
                }
 
                String itemName = droppedItem.getDefinition().getName();
 
                player.getPacketSender().sendString(35751 + index, quantityText);
                player.getPacketSender().sendString(35802 + index, rateText);
                player.getPacketSender().sendString(35853 + index, myRateText);
                player.getPacketSender().sendString(35906 + index, itemName);
 
                player.getPacketSender().sendItemOnInterface(35905, droppedItem.getId(), index, droppedItem.getAmount());
 
                index++;
            }
        }
 
        if (fromRightClick) {
            player.getPacketSender().sendInterface(35000);
        }
    }
 
    public boolean handleButtonInteraction(int buttonId) {
        if (System.currentTimeMillis() - lastClick < 2000)
            return false;
 
        if (buttonId >= -30485 && buttonId <= -30436) {
            lastClick = System.currentTimeMillis();
            handleNpcClick(buttonId);
            return true;
        }
 
        if (buttonId == -30531) {
            player.setInputHandling(new EnterSearchDropTable());
            player.getPacketSender().sendEnterInputPrompt("Enter the name of the NPC:");
            return true;
        }
 
        if (buttonId == -29566) {
            player.getPacketSender().closeAllWindows();
            return true;
        }
 
        return false;
    }
 
    private void resetInterface() {
        player.getPacketSender().sendString(35002, "Drop Table");
 
        for (int i = 0; i < 50; i++) {
            player.getPacketSender().sendString(35751 + i, "");
            player.getPacketSender().sendString(35802 + i, "");
            player.getPacketSender().sendString(35853 + i, "");
            player.getPacketSender().sendString(35906 + i, "");
            player.getPacketSender().sendItemOnInterface(35905, -1, i, 0);
        }
    }
 
    private int arrayMax(int[] arr) {
        int max = Integer.MIN_VALUE;
 
        for (int cur : arr) {
            max = Math.max(max, cur);
        }
 
        return max;
    }
}
 