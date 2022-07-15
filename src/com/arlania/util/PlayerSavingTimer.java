package com.arlania.util;

import com.arlania.GameSettings;
import com.arlania.model.container.impl.Shop;
import com.arlania.world.World;
import com.arlania.world.content.WellOfGoodwill;
import com.arlania.world.content.clan.ClanChatManager;

public class PlayerSavingTimer {

    public static long massSaveTimer = System.currentTimeMillis();

    public static void massSaving() {
        if (System.currentTimeMillis() - massSaveTimer > GameSettings.charcterSavingInterval) {
            World.savePlayers();
            WellOfGoodwill.save();
            ClanChatManager.save();
            Shop.ShopManager.saveTaxShop();
            Shop.ShopManager.saveTaxShop1();
            massSaveTimer = System.currentTimeMillis();
        }
    }

}
