package com.arlania.world;

import com.arlania.model.LocalDateAdapter;
import com.arlania.model.definitions.ItemDefinition;
import com.arlania.world.entity.impl.player.Player;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;

public class DonationData {

    public static void writeFile(Player plr, HashMap<Integer, Integer> item, int priceDonated, float truePriceDonated) throws IOException {
        // Create the path and file objects.
        Path path = Paths.get("./data/donations/", plr.getUsername() + " " + LocalDateTime.now().getMonth().toString() + " " +
                LocalDateTime.now().getDayOfMonth() + " " + LocalDateTime.now().getHour() + "h " + LocalDateTime.now().getMinute() + "m" + ".json");
        File file = path.toFile();
        file.getParentFile().setWritable(true);

        // Attempt to make the player save directory if it doesn't
        // exist.
        if (!file.getParentFile().exists()) {
            try {
                file.getParentFile().mkdirs();
            } catch (SecurityException e) {
                System.out.println("Unable to create directory for player data!");
            }
        }
        try (FileWriter writer = new FileWriter(file)) {
            Gson builder = new GsonBuilder().enableComplexMapKeySerialization()
                    .registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).setPrettyPrinting().create();
            JsonObject object = new JsonObject();
            for (int i : item.keySet()) {
                object.addProperty("Item Purchased - ", ItemDefinition.forId(i).getName() + " Quantity: " + item.get(i));
            }
            object.addProperty("Price: ", priceDonated);
            object.addProperty("True Price Paid: ", truePriceDonated);
            writer.write(builder.toJson(object));
        }
    }
}
