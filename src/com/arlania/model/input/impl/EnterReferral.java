package com.arlania.model.input.impl;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

import com.arlania.model.input.Input;
import com.arlania.world.content.PlayerPunishment;
import com.arlania.world.entity.impl.player.Player;

public class EnterReferral extends Input {
    public static final String[] rewardableUsers = new String[]{
            "I Pk Max JR",
            "Katie",
            "Cant Believe",


    };

    private static final String REFERRAL_DIR = "./data/referrals/";

    private static final String FILE = "man_referral_data.txt";

    public static ArrayList<String> MANUAL_REFERRALS = new ArrayList<String>();

    public static void initializeList(String directory, String file, ArrayList<String> list) {
        try {
            BufferedReader in = new BufferedReader(new FileReader(directory+file));
            String data = null;
            while ((data = in.readLine()) != null) {
                list.add(data);
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Fill the list with users who have claimed a referral previously
     * This is called when the server launches
     */
    public static void init() {
        initializeList(REFERRAL_DIR, FILE, MANUAL_REFERRALS);
        System.out.println("Referrals successfully initialised");
    }

    public static void addReferralClaim(String IP, String Username, String MAC) {
        if (!MANUAL_REFERRALS.contains(IP) || !MANUAL_REFERRALS.contains(MAC) || !MANUAL_REFERRALS.contains(Username)) {
            PlayerPunishment.addToFile(REFERRAL_DIR + FILE, IP + "\n" + Username + "\n" + MAC);
            MANUAL_REFERRALS.add(IP + "\n" + Username + "\n" + MAC);
        }
    }

    public static boolean claimedReferral(String IP, String Username, String MAC) {
        return MANUAL_REFERRALS.contains(IP) || MANUAL_REFERRALS.contains(Username) || MANUAL_REFERRALS.contains(MAC);
    }

    @Override
    public void handleSyntax(Player player, String syntax) {
        player.hasReferral = true;
        referralResponse(player, syntax);

        try {
            BufferedWriter w = new BufferedWriter(new FileWriter(REFERRAL_DIR + FILE, true));
            w.write(player.getUsername().toUpperCase() + " - " + syntax + " - CLAIM ATTEMPT");
            w.newLine();
            w.flush();
            w.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void referralResponse(Player player, String username) {
        if (claimedReferral(player.getHostAddress(), player.getUsername().toUpperCase(), player.getMacAddress())) {
            System.out.println("User " + player.getUsername().toUpperCase() + " Tried to claim another referral!");
            return;
        }
        if (Arrays.stream(rewardableUsers).anyMatch(username::equalsIgnoreCase)) {
            player.getInventory().add(15003, 1);
            player.getInventory().add(989, 1);
            player.getInventory().add(10852, 20);
            player.sendMessage("@red@Congratz! Because you used the code " + username + " You have gotten a reward!");
            addReferralClaim(player.getHostAddress(), player.getUsername().toUpperCase(), player.getMacAddress());
            System.out.println(player.getUsername() + " has just claimed a referral. Adding data to referral claims log.");
            System.out.println("DATA: " + player.getHostAddress() + player.getUsername().toUpperCase() + player.getMacAddress());
        }
    }
}
