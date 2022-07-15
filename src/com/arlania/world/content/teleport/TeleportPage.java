package com.arlania.world.content.teleport;

import java.util.HashMap;

public enum TeleportPage {

    BEGINNER(-2523),
    MEDIUM(-2522),
    HARDENED(-2521),
    EXPERT(-2520),
    MINIGAMES(-2519),
    ZONES(-2518);

    private final int button;

    private TeleportPage(int button) {
        this.button = button;
    }

    private static HashMap<Integer, TeleportPage> pages;

    public static TeleportPage forButton(int button) {
        return pages.get(button);
    }

    static {
        pages = new HashMap<>();
        for (TeleportPage page : values()) {
            pages.put(page.getButton(), page);
        }
    }

    public int getButton() {
        return this.button;
    }

}

