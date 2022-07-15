package com.arlania.world.content.instances;

import com.arlania.model.Position;
import com.arlania.model.RegionInstance;
import com.arlania.model.RegionInstance.RegionInstanceType;
import com.arlania.model.definitions.ItemDefinition;
import com.arlania.model.definitions.NpcDefinition;
import com.arlania.util.Misc;
import com.arlania.world.World;
import com.arlania.world.entity.impl.npc.NPC;
import com.arlania.world.entity.impl.player.Player;

public class InstanceManager {

    private final Player player;

    public InstanceManager(Player player) {
        this.player = player;
    }

    int index = 60;

    private static final InstanceData[] values = InstanceData.values();

    public void createInstance(int npcId, RegionInstanceType type) {
        if (player.getInventory().contains(5606)) {
            player.getInventory().delete(5606, 1);
        } else {
            player.getPA().sendMessage("You need an instance token, these can be obtained from killing any npc ingame!");
            return;
        }
        if (player.getRegionInstance() != null) {
            for (NPC n : player.getRegionInstance().getNpcsList()) {
                if (n != null) {
                    World.deregister(n);
                }
            }
            player.getRegionInstance().getNpcsList().clear();
        } else {
            for (NPC n : World.getNpcs()) {
                if (n != null) {
                    if (n.getPosition().getRegionId() == 11082 && n.getPosition().getZ() == index + (player.getIndex() * 4)) {
                        World.deregister(n);
                    }
                }
            }
        }
        player.setRegionInstance(new RegionInstance(player, type));
        player.lastInstanceNpc = npcId;
        player.moveTo(new Position(3040, 2913,
                index + (player.getIndex() * 4)));
        for (int i = 0; i < 4; i++) {
            NPC npc_ = new NPC(npcId, new Position(player.getPosition().getX() - 4 + (i * 2),
                    player.getPosition().getY() + 8 , index + (player.getIndex() * 4)));
            npc_.setSpawnedFor(player);
            player.getRegionInstance().getNpcsList().add(npc_);
            World.register(npc_);
        }
        for (int i = 0; i < 4; i++) {
            NPC npc_ = new NPC(npcId, new Position(player.getPosition().getX() - 4 + (i * 2),
                    player.getPosition().getY() + 6 , index + (player.getIndex() * 4)));
            npc_.setSpawnedFor(player);
            player.getRegionInstance().getNpcsList().add(npc_);
            World.register(npc_);
        }
        for (int i = 0; i < 4; i++) {
            NPC npc_ = new NPC(npcId, new Position(player.getPosition().getX() - 4 + (i * 2),
                    player.getPosition().getY() + 4 , index + (player.getIndex() * 4)));
            npc_.setSpawnedFor(player);
            player.getRegionInstance().getNpcsList().add(npc_);
            World.register(npc_);
        }
        for (int i = 0; i < 4; i++) {
            NPC npc_ = new NPC(npcId, new Position(player.getPosition().getX() - 4 + (i * 2),
                    player.getPosition().getY() + 2 , index + (player.getIndex() * 4)));
            npc_.setSpawnedFor(player);
            player.getRegionInstance().getNpcsList().add(npc_);
            World.register(npc_);
        }
        for (InstanceData data : values) {
            if (npcId == data.getNpcid() || NpcDefinition.forId(npcId).getName() == data.getName()) {
                player.setCurrentInstanceAmount(data.getEndamount());
                player.setCurrentInstanceNpcId(data.getNpcid());
                player.setCurrentInstanceNpcName(data.getName());
            }
        }
        player.getPA().sendMessage("You have instanced yourself " + player.getCurrentInstanceAmount() + " "
                + player.getCurrentInstanceNpcName());
        player.getPA().sendInterfaceRemoval();
    }

    public void death(Player player, NPC npc, String NpcName) {
        if (npc.getId() != player.getCurrentInstanceNpcId()) {
            return;
        }
        if (player.currentInstanceNpcId == -1 || player.currentInstanceNpcName == "") {
            return;
        }
        player.setCurrentInstanceAmount(player.getCurrentInstanceAmount() - 1);
        int leftToKill = player.getCurrentInstanceAmount();
        boolean sendMessage = false;
        switch (leftToKill) {
            case 4500:
            case 4000:
            case 3500:
            case 3000:
            case 2500:
            case 2000:
            case 1500:
            case 1000:
            case 750:
            case 500:
            case 250:
            case 100:
            case 90:
            case 80:
            case 70:
            case 60:
            case 50:
            case 40:
            case 30:
            case 20:
            case 10:
            case 9:
            case 8:
            case 7:
            case 6:
            case 5:
            case 4:
            case 3:
            case 2:
            case 1:
                sendMessage = true;
        }
        if (sendMessage)
            player.getPA().sendMessage("You currently need to kill " + player.getCurrentInstanceAmount() + " " + NpcName);
        if (player.getCurrentInstanceAmount() <= 0) {
            player.getPA().sendMessage("You have used up the total instance count!");
            finish();
            return;
        }
    }

    public void finish() {
        player.getPA().sendMessage("You have used up all your kills inside the instance.");
        player.getPA().sendMessage("to leave the instance simply teleport out.");
        if (player != null) {
            onLogout();
        }
    }

    public void onLogout() {
        if (player.getRegionInstance() != null)
            player.getRegionInstance().destruct();
        player.setData(null);
        player.setCurrentInstanceAmount(-1);
        player.setCurrentInstanceNpcId(-1);
        player.setCurrentInstanceNpcName("");
    }
}
