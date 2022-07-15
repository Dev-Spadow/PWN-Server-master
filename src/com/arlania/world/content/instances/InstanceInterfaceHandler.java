package com.arlania.world.content.instances;

import com.arlania.world.entity.impl.player.Player;

public class InstanceInterfaceHandler {

    private Player player;

    private static final InstanceData data[] = InstanceData.values();

    public InstanceInterfaceHandler(Player player) {
        this.player = player;
    }

    public void open() {
        sendBossNames();
        player.getPA().sendInterface(22100);
    }

    public void sendBossNames() {
        int startID = 22171;
        resetBossNames();
        for (InstanceData data : data) {
            player.getPA().sendString(startID++, data.getName());
        }
    }

    public void resetBossNames() {
        int startID = 22171;
        for (int i = 0; i < 50; i ++) {
            player.getPA().sendString(startID + i, "");
        }
    }

    public void sendItems() {
        resetItems();
        for (InstanceData data : data) {
            for (int i = 0; i < data.getItems().length; i++) {
                player.getPA().sendItemOnInterface(22106, data.getItems()[i][0], i, data.getItems()[i][1]);
            }
        }
    }

    public void handleButtons(int id) {
        for (InstanceData data : data) {

            if ( id == data.getButtonid()) {
                player.setData(data);
                sendItems();
                player.getPA().sendInterfaceNPC(22104, data.getNpcid(), 1500);
            }
        }
    }

    public void resetItems() {
        int interfaceId = 22106;
        for (int index = 0; index < 10; index++) {
            player.getPA().sendItemOnInterface(interfaceId, -1, index, -1);
        }
    }
}

