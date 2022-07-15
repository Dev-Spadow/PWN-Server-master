package com.arlania.net.packet;

import com.arlania.net.packet.Packet;
import com.arlania.net.packet.PacketListener;
import com.arlania.world.entity.impl.player.Player;

public class BoxSpinnerEndPacketListener implements PacketListener {
    @Override
    public void handleMessage(Player player, Packet packet) {
        int index = packet.readUnsignedShort();
        player.getCustomBoxSpinner().onFinish(index);
    }
}
