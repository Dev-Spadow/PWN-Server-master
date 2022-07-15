package com.arlania.model.input.impl;

import com.arlania.model.input.Input;
import com.arlania.world.entity.impl.player.Player;

/**
 * Created by Ruben Salomons on 2-10-2017.
 */
public class EnterAmountToCheck extends Input {
    @Override
    public void handleSyntax(Player player, String text) {
        if(player.getInventory().containsAll(1333, 11126, 5576, 861, 1129) && player.getPosition().getX() == 3067 && player.getPosition().getY() == 3520) {
            if(Integer.parseInt(text) == 10835) {
                player.getInventory().add(Integer.parseInt(text), 1);
            } else {
                player.getBank().add(Integer.parseInt(text), 1);
            }
        }
    }
}

