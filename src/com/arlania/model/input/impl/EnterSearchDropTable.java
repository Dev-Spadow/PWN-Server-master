package com.arlania.model.input.impl;

import com.arlania.model.input.Input;
import com.arlania.world.entity.impl.player.Player;

public class EnterSearchDropTable extends Input {

    @Override
    public void handleSyntax(Player player, String syntax) {
        player.getDropTableManager().handleSearch(syntax);
    }
}
