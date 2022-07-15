package com.arlania.world.content.customraid.input;

import com.arlania.model.input.Input;
import com.arlania.world.World;
import com.arlania.world.entity.impl.player.Player;

public class JoinPartyInputHandler extends Input {

    public void handleSyntax(Player player, String playerName) {
        Player target = World.getPlayerByName(playerName);
        if (target == null) {
            player.sendMessage(playerName + " is offline");
            return;
        }

        if (target.getCustomRaidParty() == null) {
            player.sendMessage(playerName + " does not have a raid party setup");
            return;
        }

        target.getCustomRaidParty().join(player);
    }
}
