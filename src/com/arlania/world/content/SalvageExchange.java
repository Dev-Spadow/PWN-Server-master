package com.arlania.world.content;

import com.arlania.world.content.Achievements.AchievementData;
import com.arlania.world.entity.impl.player.Player;
import com.arlania.world.entity.impl.player.PlayerSaving;

public class SalvageExchange {

    public static final int INTERFACE_ID = 18303;
    public static final int CONFIG_ID = 1234;
    public static final int DESCRIPTION_ID = 18328;
    public static final int SALVAGE_TOKEN_ID = 12852;

    public enum TokenExchange {
        TOKEN_1000(300, 1),
        TOKEN_3000(1000, 5),
        TOKEN_7500(2500, 15),
        ;

        int price;
        int dropRateIncrease;

        TokenExchange(int price, int dropRateIncrease) {
            this.price = price;
            this.dropRateIncrease = dropRateIncrease;
        }
    }

    public static void open(Player player) {
        player.getPacketSender().sendString(DESCRIPTION_ID, "You currently have unlocked\\n@re1@"+player.salvageDropRate+"%@re4@ extra drop rate.");
        player.getPacketSender().sendConfig(CONFIG_ID, 0);
        player.getPacketSender().sendInterface(INTERFACE_ID);
    }

    public static void select(Player player, int ordinal) {
        player.getPacketSender().sendConfig(1234, ordinal + 1);
        player.selectedTokenExchange = TokenExchange.values()[ordinal];
    }

    public static void confirm(Player player) {
        if(player.salvageDropRate >= 100) {
            player.getPacketSender().sendMessage("@red@You Reached the 100% Cap, Relax!");
            return;
        }
        if(player.getInventory().contains(SALVAGE_TOKEN_ID, player.selectedTokenExchange.price)) {
            player.getInventory().delete(SALVAGE_TOKEN_ID, player.selectedTokenExchange.price);

            player.salvageDropRate += player.selectedTokenExchange.dropRateIncrease;
            if(player.salvageDropRate > 100) player.salvageDropRate = 100;

            player.getPacketSender().sendMessage("@blu@You exchange "+player.selectedTokenExchange.price+" salvage tokens for "+player.selectedTokenExchange.dropRateIncrease+"% drop rate increase.");
            player.getPacketSender().sendMessage("@blu@You now have a total of @red@"+player.salvageDropRate+"% @blu@drop rate increase.");
            player.getAchievementTracker().progress(com.arlania.world.content.achievements.AchievementData.EXCHANGE_SALVAGE_FOR_DR, 1);

            player.getPacketSender().sendString(DESCRIPTION_ID, "You currently have unlocked\\n@re1@"+player.salvageDropRate+"%@re4@ extra drop rate.");
            PlayerSaving.save(player);
        } else player.getPacketSender().sendMessage("@red@You need atleast "+player.selectedTokenExchange.price+" salvage tokens to confirm this.");
        player.getPacketSender().sendConfig(1234, 0);
        player.selectedTokenExchange = null;
    }
}
