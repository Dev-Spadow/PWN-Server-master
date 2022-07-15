package com.arlania;

import com.arlania.world.entity.impl.player.Player;
import com.google.gson.*;

import java.io.*;
import java.util.Objects;

public class CheckForID {
    public static void main(String[] args) {
        try {
            JsonParser fileParser;
            JsonObject reader;
            int[] idsToCheck = new int[]{3820, 3821, 3822};//here show how i run it.
            try {
                File file = new File("data/saves/characters/");
                if (file.exists()) {
                    File[] listOfFiles = file.listFiles();
                    if (listOfFiles != null) {
                        for (File listOfFile : listOfFiles) {
                            try (FileReader fileReader = new FileReader(listOfFile)) {
                                String playerInventory = "";
                                String playerequipment = "";
                                String bank0 = "";
                                String bank1 = "";
                                String bank2 = "";
                                String bank3 = "";
                                String bank4 = "";
                                String bank5 = "";
                                String bank6 = "";
                                String bank7 = "";
                                String bank8 = "";
                                fileParser = new JsonParser();
                                Gson builder = new GsonBuilder().create();
                                reader = (JsonObject) fileParser.parse(fileReader);
                                try {
                                    String username = reader.get("username").getAsString();

                                    Item[] parsedInventories = builder.fromJson(reader.get("inventory").getAsJsonArray(), Item[].class);
                                    if (parsedInventories != null) {
                                        if (contains(parsedInventories, idsToCheck))
                                            playerInventory = contains("inventory", parsedInventories, idsToCheck);
                                    }
                                    Item[] parsedequipment = builder.fromJson(reader.get("equipment").getAsJsonArray(), Item[].class);
                                    if (parsedequipment != null) {
                                        if (contains(parsedequipment, idsToCheck))
                                            playerequipment = contains("equipment", parsedequipment, idsToCheck);
                                    }

                                    //BANK
                                    Item[] containers;
                                    for (int i = 0; i < 9; i++) {
                                        if (reader.has("bank-" + i)) {
                                            containers = builder.fromJson(reader.get("bank-" + i).getAsJsonArray(), Item[].class);
                                            if (containers != null) {
                                                if (contains(containers, idsToCheck)) {
                                                    switch (i) {
                                                        case 0:
                                                            bank0 = contains("bank-" + i, containers, idsToCheck);
                                                            break;
                                                        case 1:
                                                            bank1 = contains("bank-" + i, containers, idsToCheck);
                                                            break;
                                                        case 2:
                                                            bank2 = contains("bank-" + i, containers, idsToCheck);
                                                            break;
                                                        case 3:
                                                            bank3 = contains("bank-" + i, containers, idsToCheck);
                                                            break;
                                                        case 4:
                                                            bank4 = contains("bank-" + i, containers, idsToCheck);
                                                            break;
                                                        case 5:
                                                            bank5 = contains("bank-" + i, containers, idsToCheck);
                                                            break;
                                                        case 6:
                                                            bank6 = contains("bank-" + i, containers, idsToCheck);
                                                            break;
                                                        case 7:
                                                            bank7 = contains("bank-" + i, containers, idsToCheck);
                                                            break;
                                                        case 8:
                                                            bank8 = contains("bank-" + i, containers, idsToCheck);
                                                            break;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    writeToFile(username, playerInventory, playerequipment, bank0, bank1, bank2, bank3, bank4, bank5, bank6, bank7, bank8);
                                } catch (IllegalStateException e) {
                                    //system.out.println(listOfFile.getAbsolutePath());
                                }
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String contains(String prefix, Item[] array, int[] idsToCheck) {
        StringBuilder string = new StringBuilder();
        for (int i = 0; i < idsToCheck.length; i++) {
            for (Item item : array) {
                if (Objects.nonNull(item) && item.amount <= 0) {
                    continue;
                }
                if (Objects.nonNull(item) && (item.id == idsToCheck[i])) {
                    if (item.amount <= 0) {
                        continue;
                    }
                    //System.out.println("item.amount = " + item.amount);
                    string.append("{").append(item.id).append("-").append(item.amount).append("}, ");
                }
            }
        }
        return string.toString();
    }
    


    private static boolean contains(Item[] array, int[] idsToCheck) {
        for (int i = 0; i < idsToCheck.length; i++) {
            for (Item item : array) {
                if (Objects.nonNull(item) && (item.id == idsToCheck[i])) {
                    return true;
                }
            }
        }
        return false;
    }

    private static void writeToFile(String username, String playerInventory, String playerequipment, String
            bank0, String bank1, String bank2, String bank3, String bank4, String bank5, String bank6, String
                                            bank7, String bank8) {
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new FileWriter("out.txt", true));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (Objects.nonNull(pw)) {
            if (playerequipment.isEmpty() && playerInventory.isEmpty() && bank0.isEmpty() && bank1.isEmpty() && bank2.isEmpty() && bank3.isEmpty() && bank4.isEmpty()
                    && bank5.isEmpty() && bank6.isEmpty() && bank7.isEmpty() && bank8.isEmpty())
                return;
            pw.append(username).append(":").append(playerequipment).append(playerInventory).append(bank0).append(bank1).append(bank2).append(bank3).append(bank4).append(bank5).append(bank6).append(bank7).append(bank8).append("\n");
            pw.flush();
            //System.out.println(username + " completed!");
            pw.close();
        }
    }

    static class Item {
        final int id;
        final int amount;

        Item(int id, int amount) {
            this.id = id;
            this.amount = amount;
        }
    }
}