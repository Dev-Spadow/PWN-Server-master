package com.arlania.net.packet;

import com.arlania.world.entity.impl.player.Player;

public class WheelOfFortuneEndListener implements PacketListener {

    @Override
    public void handleMessage(Player player, Packet packet) {
        byte index = packet.readByte();
        player.getWheelOfFortune().onFinish(index);
    }
}
