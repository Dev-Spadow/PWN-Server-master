package com.arlania.world.content.teleport;

import com.arlania.model.Item;
import com.arlania.model.Position;
import com.arlania.world.entity.impl.player.Player;

import java.util.ArrayList;

public class TeleportController {

    public static void open(Player player) {
        player.getPA().sendInterface(63000);
        if (player.teleportPageOpen != null) {
            loadPage(player, player.teleportPageOpen);
        } else {
            loadPage(player, TeleportPage.BEGINNER);
        }
        if (player.teleportView != null) {
            selectLog(player, player.teleportView);
        } else {
            selectLog(player, TeleportData.NOOBS);
        }
    }

    public static void selectLog(Player player, TeleportData selected) {
        player.teleportView = selected;

        Item[] items = selected.getDrops();

        player.getPacketSender().sendPetId(selected.getNpcId());

        for (int i = 0; i < 6; i++) {
            player.getPacketSender().sendString(63026 + i, "");
        }

        for (int i = 0; i < selected.getDescription().length; i++) {
            player.getPacketSender().sendString(63026 + i, selected.getDescription()[i]);
        }

        for (int i = 0; i < 10; i++) {
            player.getPacketSender().sendItemOnInterface(63032, -1, i, 0);
        }

        for (int i = 0; i < items.length; i++) {
            player.getPacketSender().sendItemOnInterface(63032, items[i].getId(), i, items[i].getAmount());
        }

    }

    public static void loadPage(Player player, TeleportPage page) {
        player.teleportPageOpen = page;
        player.getPA().sendConfig(1106, page.ordinal());
        sendButtons(player, page);
    }

    public static void teleport(Player player, TeleportData selected) {
        if (player.teleportView == null) {
            player.sendMessage("You must select a destination before teleporting.");
            return;
        }

        player.getPacketSender().closeAllWindows();
        player.moveTo(selected.getDestination());

        player.previousTeleport = selected;
    }

    public static void sendButtons(Player player, TeleportPage page) {
        ArrayList<TeleportData> list = TeleportData.getPageList(page);

        for (int i = 0; i < 50; i++) {
            player.getPA().sendString(63151 + i, "");
        }

        for (int i = 0; i < list.size(); i++) {
            player.getPA().sendString(63151 + i, list.get(i).getName());
        }
    }

    public static void selectLogButton(Player player, int slot) {
        ArrayList<TeleportData> list = TeleportData.getPageList(player.teleportPageOpen);
        if (slot > list.size() - 1) {
            return;
        }
        TeleportData selected = list.get(slot);
        selectLog(player, selected);
    }

    public static boolean clickButton(Player player, int button) {
        TeleportPage page = TeleportPage.forButton(button);
        if (page != null) {
            loadPage(player, page);
            return true;
        }
        if (button <= -2447 && button >= -2485) {
            selectLogButton(player, button + 2485);
            return true;
        }
        if (button == -2531) {
            teleport(player, player.teleportView);
            return true;
        }
        if (button == -2527) {
            teleport(player, player.previousTeleport);
            return true;
        }
        if (button == 10004) {
            teleport(player, player.previousTeleport);
            return true;
        }
        return false;
    }

}

