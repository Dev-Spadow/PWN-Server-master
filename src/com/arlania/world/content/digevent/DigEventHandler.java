package com.arlania.world.content.digevent;

import com.arlania.model.GroundItem;
import com.arlania.model.Item;
import com.arlania.model.Position;
import com.arlania.model.container.impl.Inventory;
import com.arlania.util.Misc;
import com.arlania.world.World;
import com.arlania.world.entity.impl.GroundItemManager;
import com.arlania.world.entity.impl.player.Player;

import java.util.*;

public class DigEventHandler {

    public static boolean gameActive;

    public static int SALVAGE_TOKEN = 12852;

    private static final long MAX_GAME_TIME = 1800000;

    /**
     * Logs start time of the game
     **/
    private static long START_TIME = System.currentTimeMillis();

    private static Map<Player, Integer> leaderboard = new HashMap<>();

    public static void startGame(int digSites) {
        generateSites(digSites);
        gameActive = true;
        World.sendMessage("@blu@A new digging event has started at home! There are " + digSites + " zones to find!");
        World.sendMessage("@blu@Use ::spade or ::shovel to receive one in your inventory!");
    }

    public static void stopGame() {
        gameActive = false;
        findWinner();
        int i = 0;
        for (DigSite site : digSites) {
            if (!site.found)
                i++;
        }
        digSites.clear();
        World.sendMessage("@blu@The digging event is now OVER! There were @red@" + i + "@blu@ undiscovered sites!");
    }

    /**
     * Runs each second to check if we
     * should stop the game yet
     */
    public static void process() {
        if (!gameActive) {
            return;
        }
        if (System.currentTimeMillis() - START_TIME > MAX_GAME_TIME) {
            START_TIME = System.currentTimeMillis();
            stopGame();
        }
        //System.out.println("Seconds remaining: " + (System.currentTimeMillis() - START_TIME) / 1000);
    }


    public static void checkSites(Player player) {
        if (digSites.isEmpty()) {
            player.getPA().sendMessage("@red@No dig-sites loaded.");
            return;
        }
        for (DigSite site : digSites) {
            System.out.println("TOTAL SITES: " + digSites.size() + " X:" + site.getPos().getX() + " Y:" + site.getPos().getY() + " TOKENS: " + site.tokenQuantity);
        }
    }

    public static void handleDigAttempt(Player player) {
        if (!gameActive) {
            return;
        }
        Position playerPos = player.getPosition();
        for (DigSite site : digSites) {
            if (site.getPos().getX() == playerPos.getX() &&
                    site.getPos().getY() == playerPos.getY() &&
                    !site.found) {
                player.getPA().sendMessage("Congratulations! You found a dig-site worth @blu@" + site.getTokenQuantity() + " tokens!");
                Inventory inv = player.getInventory();
                if (inv.getFreeSlots() >= 1 || inv.contains(SALVAGE_TOKEN)) {
                    inv.add(SALVAGE_TOKEN, site.tokenQuantity);
                    //System.out.println("Adding " + site.tokenQuantity + " to inv of " + player.getUsername());
                } else {
                    GroundItemManager.spawnGroundItem(player, new GroundItem(new Item(SALVAGE_TOKEN, site.tokenQuantity), playerPos, player.getUsername(), false, 200, true, 200));
                }
                incrementDigs(player);
                site.found = true;
            }
        }
    }

    private static void incrementDigs(Player player) {
        if (leaderboard.containsKey(player)) {
            leaderboard.replace(player, leaderboard.get(player) + 1);
        } else {
            leaderboard.put(player, 1);
        }
    }

    public static void checkLeaderBoard() {
        if (leaderboard.isEmpty())
            return;
        else
            System.out.println("Leaderboard contains " + leaderboard.size() + " Diggers");
    }

    private static void findWinner() {
        if (leaderboard.isEmpty()) {
            return;
        }

        List<Map.Entry<Player, Integer>> result = sortEntries(leaderboard);

        for (Map.Entry<Player, Integer> entry : result) {

            Player winner = entry.getKey();
            int digs = entry.getValue();
            winner.getPA().sendMessage("@red@Congratulations on winning the digging event! We've added a 300 bonus!");
            World.sendMessage("@blu@The winner of the digging event is @red@" + winner.getUsername() + " @blu@with @red@" + digs + " @blu@sites found!");
            winner.getInventory().add(SALVAGE_TOKEN, 300);
            break;

        }
        leaderboard.clear();
    }

    static <K, V extends Comparable<? super V>> List<Map.Entry<K, V>> sortEntries(Map<K, V> map) {

        List<Map.Entry<K, V>> sortedEntries = new ArrayList<Map.Entry<K, V>>(map.entrySet());

        Collections.sort(sortedEntries, new Comparator<Map.Entry<K, V>>() {

            @Override
            public int compare(Map.Entry<K, V> e1, Map.Entry<K, V> e2) {
                return e2.getValue().compareTo(e1.getValue());
            }

        });

        return sortedEntries;

    }

    public static ArrayList<DigSite> digSites = new ArrayList<>();


    private static void generateSites(int amount) {

        int minX = 3246;
        int maxX = 3345;
        int minY = 4016;
        int maxY = 4112;

        for (int i = 0; i < amount; i++) {
            int randomX = Misc.random(minX, maxX);
            int randomY = Misc.random(minY, maxY);
            int salvageTokens = Misc.random(80); //This is the maximum amount of tokens that a site can have!

            if (salvageTokens == 80) { //1 in 500 chance to get 2k tokens
                salvageTokens *= 4;
            }

            digSites.add(new DigSite(new Position(randomX, randomY), salvageTokens, false));
            //System.out.println("NEW DIG-SITE AT X:" + randomX + " Y:" + randomY + " Tokens: " + salvageTokens);
        }

    }

}
