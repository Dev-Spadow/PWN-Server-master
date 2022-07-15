package com.arlania.world.content.daily_reward;

import java.io.*;
import java.util.ArrayList;

public class Util {
    public static boolean fileExists(String location) {
        File firstFolder = new File(location);
        if (firstFolder.exists()) {
            return true;
        }
        return false;
    }

    public static void deleteAllLines(String fileLocation) {
        if (!fileExists(fileLocation)) {
            return;
        }
        try {
            FileOutputStream writer = new FileOutputStream(fileLocation);
            writer.write((new String()).getBytes());
            writer.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void saveArrayContentsSilent(String location, ArrayList<?> arraylist) {
        if (arraylist.isEmpty()) {
            return;
        }
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(location, true));

            for (int index = 0; index < arraylist.size(); index++) {
                bw.write("" + arraylist.get(index));
                bw.newLine();
            }

            bw.flush();
            bw.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public static ArrayList<String> readFile(String location) {
        ArrayList<String> arraylist = new ArrayList<String>();
        if (!new File(location).exists()) {
            return arraylist;
        }
        try {
            BufferedReader file = new BufferedReader(new FileReader(location));
            String line;
            while ((line = file.readLine()) != null) {
                if (!line.isEmpty()) {
                    arraylist.add(line);
                }
            }
            file.close();
        } catch (Exception e) {
        }
        return arraylist;
    }
}

