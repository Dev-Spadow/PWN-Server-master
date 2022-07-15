package com.arlania.net.packet;

import com.arlania.world.content.playersettings.PlayerSetting;
import com.arlania.world.entity.impl.player.Player;

public class ClientSettingsChangePacketListener implements PacketListener {

    @Override
    public void handleMessage(Player player, Packet packet) {
        for (int i = 0; i < PlayerSetting.VALUES.length; i++) {
            PlayerSetting setting = PlayerSetting.getSetting(i);
            if (i < PlayerSetting.TOGGLEABLES) {
                player.getPlayerSettings()
                    .put(setting, packet.readUnsignedByte());
            } else {
                player.getPlayerSettings().put(setting, packet.readInt());
            }
        }

        if(player.getPlayerSettings().getOrDefault(PlayerSetting.SHOW_PROGRESSION_OVERLAY, 0) == 0) {
            player.getPacketSender().sendWalkableInterface(42001, false);
        }
    }
}
