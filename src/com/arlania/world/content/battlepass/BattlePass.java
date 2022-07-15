package com.arlania.world.content.battlepass;

import com.arlania.model.Item;
import com.arlania.world.entity.impl.player.Player;
import com.google.common.collect.ImmutableList;
import com.moandjiezana.toml.Toml;

import java.io.File;
import java.util.List;

public enum BattlePass {
    INSTANCE;

    public static final int NEXT_PAGE_INTERFACE = 18880;
    public static final int PREVIOUS_PAGE_INTERFACE = 18876;
    public static final int BUY_BUTTON_INTERFACE = 18895;
    public static final int CLOSE_BUTTON_INTERFACE = 18788;
    private static final int BATTLEPASS_INTERFACE = 18786;
    private static final int BATTLEPASS_VARP = 3600;
    private static final int BATTLEPASS_PROGRESS_VARP = 3601;
    private static final int BATTLEPASS_CARD_VARP = 3602;
    private static final int PAGE_TEXT_INTERFACE = 18792;
    private static final int SEASON_ENDS_TEXT_INTERFACE = 18793;
    private static final int SEASON_TEXT_INTERFACE = 18885;
    private static final int PROGRESS_TEXT_INTERFACE = 18889;
    private static final int CURRENT_LEVEL_TEXT_INTERFACE = 18890;
    private static final int NEXT_LEVEL_TEXT_INTERFACE = 18891;
    private static final int CARD_LEVEL_TEXT_INTERFACE = 18798;
    private static final int CARD_INVENTORY_INTERFACE = 18799;
    private static final int CLAIM_BUTTON_INTERFACE = 18796;
    private final String SEASON_ENDS_STRING = "";
    private final int[] EXP_FOR_LEVEL = new int[100];
    public int LEVEL_1_XP = 100;
    public int LEVEL_100_XP = 100_000;
    private String buyPassUrl;
    private String buySkipsUrl;
    private int CURRENT_SEASON;
    private long SEASON_END;
    private double xpPerMinute;
    private double skillingXpRatio;
    private double combatXpRatio;
    private int skips;
    private Item[] rewards = new Item[]{};
    private BattlePassNPC[] npcs = new BattlePassNPC[]{};

    public double getXpPerMinute() {
        return xpPerMinute;
    }

    public double getSkillingXpRatio() {
        return skillingXpRatio;
    }

    public double getCombatXpRatio() {
        return combatXpRatio;
    }

    public void awardPlayTimeExperience(final Player player, final long playtime) {
        player.incrementBattlePassExp((long) ((playtime / 1000 / 60) * xpPerMinute));
    }

    public void awardSkillingExperience(final Player player, final int xpGained, final boolean combat) {
      //  player.incrementBattlePassExp((long) (1);
    }

    public void awardNpcKillExperience(final Player player, final BattlePassNPC npc) {
        player.incrementBattlePassExp(npc.getXp());
    }

    public void updateSeason(final Player player, int season) {
        if (season != CURRENT_SEASON) {
            player.setBattlePassExp(0);
            player.setBattlePassClaimedRewards(new boolean[100]);
            player.setBattlePass(false);
            season = CURRENT_SEASON;
        }
        player.setBattlePassSeason(season);
    }

    public BattlePassNPC[] getNpcs() {
        return npcs;
    }

    public void openStore(final Player player) {
        if (player.hasBattlePass()) {
            player.getPacketSender().openUrl(buySkipsUrl);
        } else {
            player.getPacketSender().openUrl(buyPassUrl);
        }
    }

    public boolean claimReward(final Player player, final int buttonId) {
        final int tile;
        switch (buttonId) {
            case CLAIM_BUTTON_INTERFACE:
                tile = 0;
                break;
            case CLAIM_BUTTON_INTERFACE + 8:
                tile = 1;
                break;
            case CLAIM_BUTTON_INTERFACE + 16:
                tile = 2;
                break;
            case CLAIM_BUTTON_INTERFACE + 24:
                tile = 3;
                break;
            case CLAIM_BUTTON_INTERFACE + 32:
                tile = 4;
                break;
            case CLAIM_BUTTON_INTERFACE + 40:
                tile = 5;
                break;
            case CLAIM_BUTTON_INTERFACE + 48:
                tile = 6;
                break;
            case CLAIM_BUTTON_INTERFACE + 56:
                tile = 7;
                break;
            case CLAIM_BUTTON_INTERFACE + 64:
                tile = 8;
                break;
            case CLAIM_BUTTON_INTERFACE + 72:
                tile = 9;
                break;
            default:
                tile = -1;
                break;
        }
        if (tile == -1) {
            return false;
        }
        final int page = player.getAttribute("battlepass_page", 0);
        final int slot = tile + (10 * page);
        if (page > getPageCount() || slot >= rewards.length) {
            return false;
        }
        if (!player.hasBattlePass() || getBattlePassLevel(player) - 1 < slot || player.hasClaimedBattlePassReward(slot)) {
            return true;
        }
        final Item reward = rewards[slot];
        final boolean stackable = reward.getDefinition().isStackable();
        final int requiredSlots = stackable ? 1 : reward.getAmount();
        if (player.getInventory().getFreeSlots() < requiredSlots) {
            final int invSlot = player.getInventory().getSlot(reward.getId());
            if (stackable && invSlot != -1 && Integer.MAX_VALUE <= player.getInventory().get(invSlot).getAmount() + reward.getAmount()) {
                final int amount = player.getInventory().get(invSlot).getAmount() + reward.getAmount();
                player.getInventory().delete(reward.getId(), invSlot);
                player.getInventory().add(reward.getId(), amount);
                player.sendMessage("Your reward has been added to your inventory!");
                player.markClaimedBattlePassReward(slot);
                player.getPacketSender().sendConfig(BATTLEPASS_CARD_VARP + tile, 2);
            } else {
                player.sendMessage("Please free up some space before attempting to claim your reward.");
            }
        } else {
            player.getInventory().addItem(reward);
            player.sendMessage("Your reward has been added to your inventory!");
            player.markClaimedBattlePassReward(slot);
            player.getPacketSender().sendConfig(BATTLEPASS_CARD_VARP + tile, 2);
        }
        return true;
    }

    private int getPageCount() {
        return rewards.length / 10;
    }

    public int getBattlePassLevel(final Player player) {
        int level = 0;
        for (int i = 0; i < 100; i++) {
            if (player.getBattlePassExp() >= EXP_FOR_LEVEL[i]) {
                level = i + 1;
            }
        }
        return level;
    }

    public void initialize() {
        final Toml config = new Toml().read(new File("./data/def/battlepass.toml"));
        CURRENT_SEASON = Math.toIntExact(config.getLong("current_season"));
        SEASON_END = config.getLong("season_end");
        LEVEL_1_XP = Math.toIntExact(config.getLong("level_1_xp"));
        LEVEL_100_XP = Math.toIntExact(config.getLong("level_100_xp"));
        xpPerMinute = config.getDouble("xp_per_minute");
        skillingXpRatio = config.getDouble("skillxp_ratio");
        combatXpRatio = config.getDouble("combatxp_ratio");
        buyPassUrl = config.getString("buy_pass_url");
        buySkipsUrl = config.getString("buy_skips_url");
        skips = Math.toIntExact(config.getLong("skips"));
        rewards = config.getTables("reward").stream().map(toml -> {
            final BattlePassReward reward = toml.to(BattlePassReward.class);
            return new Item(reward.getIndex(), reward.getAmount());
        }).toArray(Item[]::new);
        npcs = config.getTables("npc").stream().map(npc -> npc.to(BattlePassNPC.class)).toArray(BattlePassNPC[]::new);
        double b = Math.log((double) LEVEL_100_XP / LEVEL_1_XP) / 99;
        double a = (double) LEVEL_1_XP / (Math.exp(b) - 1.0);
        for (int i = 1; i <= 100; i++) {
            int oldXp = (int) Math.round(a * Math.exp(b * (i - 1)));
            int newXp = (int) Math.round(a * Math.exp(b * i));
            EXP_FOR_LEVEL[i - 1] = newXp - oldXp;
        }
    }

    public void applySkips(final Player player) {
        final int level = getBattlePassLevel(player);
        if (getBattlePassLevel(player) >= 100) {
            player.sendMessage("On second thought, it would be silly to use this.");
            return;
        }
        player.setBattlePassExp(EXP_FOR_LEVEL[Math.min(99, level + skips - 1)]);
    }

    public List<Item> getRewards() {
        return ImmutableList.copyOf(rewards);
    }

    public void openInterface(final Player player) {
        player.setAttribute("battlepass_page", 0);
        sendPage(player);
    }

    private void sendPage(final Player player) {
        final int page = player.getAttribute("battlepass_page", 0);
        updateInterface(player, page);
        player.getPacketSender().sendInterfaceRemoval();
        player.getSkillManager().stopSkilling();
        player.getPacketSender().sendInterface(BATTLEPASS_INTERFACE);
    }

    private void updateInterface(final Player player, final int page) {
        final int level = getBattlePassLevel(player);
        // Disable/enable pass
        player.getPacketSender().sendConfig(BATTLEPASS_VARP, player.hasBattlePass() ? 1 : 0);
        if (page >= 9) {
            System.out.println(BATTLEPASS_VARP + "?lol" + player.hasBattlePass());
        }
        // Pass level
        final int xpForNextLevel = EXP_FOR_LEVEL[level < 100 ? level : level - 1];
        final int xpForLevel = EXP_FOR_LEVEL[level > 0 ? level - 1 : level];
        final int xpDiff = xpForNextLevel - xpForLevel;
        final int xpTowardsNextLevel = player.getBattlePassExp() - xpForLevel;
        System.out.println((int) ((250 / (double) 100) * (xpDiff > 0 ? ((xpTowardsNextLevel / (double) xpDiff) * 100) : 0)));
        player.getPacketSender().sendString(CURRENT_LEVEL_TEXT_INTERFACE, String.valueOf(level));
        player.getPacketSender().sendString(PROGRESS_TEXT_INTERFACE, "Experience: " + (player.getBattlePassExp() + " / " + xpForNextLevel));
        player.getPacketSender().sendString(NEXT_LEVEL_TEXT_INTERFACE, String.valueOf(Math.min(level + 1, 100)));
        player.getPacketSender().sendToggle(BATTLEPASS_PROGRESS_VARP, (int) (250 / (double) 100 * (xpDiff > 0 ? xpTowardsNextLevel / (double) xpDiff * 100 : 0)));
        // Cards
        for (int i = 0; i < 10; i++) {
            player.getPacketSender().sendString(CARD_LEVEL_TEXT_INTERFACE + i * 8, String.valueOf(1 + i + page * 10));
            player.getPacketSender().sendItemOnInterface(CARD_INVENTORY_INTERFACE + i * 8, rewards[i + page * 10].getId(), rewards[i + page * 10].getAmount());
            player.getPacketSender().sendConfig(BATTLEPASS_CARD_VARP + i, getVarpState(player, level, i, page));
        }
        player.getPacketSender().sendString(PAGE_TEXT_INTERFACE, "Page: @whi@" + (page + 1) + " / " + getPageCount());
        player.getPacketSender().sendString(SEASON_TEXT_INTERFACE, "Season @whi@" + CURRENT_SEASON);
        player.getPacketSender().sendString(SEASON_ENDS_TEXT_INTERFACE, SEASON_ENDS_STRING);
    }

    private int getVarpState(final Player player, final int level, final int index, final int page) {
        if (!player.hasBattlePass() || level < index + (10 * page) + 1) {
            return 0;
        } else if (!player.getBattlePassClaimedRewards()[index + (10 * page)]) {
            return 1;
        } else {
            return 2;
        }
    }

    public void sendPage(final Player player, final boolean forward) {
        int page = player.getAttribute("battlepass_page", 0);
        if (forward) {
            page++;
        } else {
            page--;
        }
        if (page > 9) {
            page = 0;
        } else if (page < 0) {
            page = 9;
        }
        player.setAttribute("battlepass_page", page);
        sendPage(player);
    }
}
