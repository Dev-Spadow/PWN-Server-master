package com.arlania.world.content.achievements;

import com.arlania.model.Item;
import org.apache.commons.lang3.text.WordUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public enum AchievementData {
    KILL_A_MONSTER_USING_MELEE(0, AchievementType.EASY, 1, "Kill a Monster Using Melee",
            new Item[]{new Item(10835, 25), new Item(744, 25)},
            new BossPointReward(10), new DrReward(1)),
    KILL_A_MONSTER_USING_RANGED(1, AchievementType.EASY, 1, "Kill a Monster Using Ranged",
            new Item[]{new Item(10835, 25), new Item(744, 25)},
            new BossPointReward(10), new DrReward(1)),
    KILL_A_MONSTER_USING_MAGIC(2, AchievementType.EASY, 1, "Kill a Monster Using Magic",
            new Item[]{new Item(10835, 25), new Item(744, 25)},
            new BossPointReward(10), new DrReward(1)),
    OPEN_THE_STARTER_CHEST(3, AchievementType.EASY, 1, "Open the starter chest",
            new Item[]{new Item(10835, 250)},
            new BossPointReward(10), new DrReward(1)),
    OPEN_THE_MEDIUM_CHEST(4, AchievementType.EASY, 1, "Open the medium chest",
            new Item[]{new Item(989, 1)},
            new BossPointReward(10), new DrReward(1)),
    KILL_A_ZOMBIE_KID(5, AchievementType.EASY, 1, "Kill a Zombie Hillgiant",
            new Item[]{new Item(15373, 1)},
            new BossPointReward(10), new DrReward(1)),
    VISIT_KALVOTHS_LAIR(6, AchievementType.EASY, 1, "Visit Kalvoths Lair",
            new Item[]{new Item(10835, 10)},
            new BossPointReward(10), new DrReward(1)),
    VISIT_POS(7, AchievementType.EASY, 1, "Access ::pos",
            new Item[]{new Item(744, 100)},
            new BossPointReward(10), new DrReward(1)),
    COMPLETE_A_SLAYER_TASK(8, AchievementType.EASY, 1, "Complete A Slayer Task",
            new Item[]{new Item(12852, 30), new Item(10835, 500)},
            new BossPointReward(10), new DrReward(1)),
    REACH_MAX_EXP_IN_A_SKILL(9, AchievementType.EASY, 1, "Reach Max Exp In A Skill",
            new Item[]{new Item(10835, 500), new Item(989, 1)},
            new BossPointReward(10), new DrReward(1)),
    DEAL_EASY_DAMAGE_USING_MELEE(10, AchievementType.EASY, 50000000, "Deal 50M Melee Damage",
            new Item[]{new Item(10835, 250), new Item(989, 1)},
            new BossPointReward(10), new DrReward(1)),
    DEAL_EASY_DAMAGE_USING_RANGED(11, AchievementType.EASY, 50000000, "Deal 50M Ranged Damage",
            new Item[]{new Item(10835, 250), new Item(989, 1)},
            new BossPointReward(10), new DrReward(1)),
    DEAL_EASY_DAMAGE_USING_MAGIC(12, AchievementType.EASY, 50000000, "Deal 50M Magic Damage",
            new Item[]{new Item(10835, 250), new Item(989, 1)},
            new BossPointReward(10), new DrReward(1)),
    PERFORM_A_SPECIAL_ATTACK(13, AchievementType.EASY, 1, "Perform a Special Attack",
            new Item[]{new Item(15373, 1)},
            new BossPointReward(10), new DrReward(1)),
    CLAIM_A_BOND(14, AchievementType.EASY, 1, "Claim a bond",
            new Item[]{new Item(10835, 750)},
            new BossPointReward(10), new DrReward(1)),
    DRINK_A_DELUGE_POTION(15, AchievementType.EASY, 1, "Drink a Deluge Potion",
            new Item[]{new Item(744, 250)},
            new BossPointReward(10), new DrReward(1)),
    KILL_ZEUS_50_TIMES(16, AchievementType.EASY, 50, "Kill Zeus 50 Times",
            new Item[]{new Item(1543, 2)},
            new BossPointReward(10), new DrReward(1)),
    //Medium
    KILL_NARUTO_100_TIMES(17, AchievementType.MEDIUM, 100, "Kill Naruto 100 Time",
            new Item[]{new Item(10835, 2000), new Item(6199, 1)},
            new BossPointReward(10), new DrReward(3)),
    KILL_ZEUS_250_TIMES(18, AchievementType.MEDIUM, 250, "Kill Zeus 250 Times",
            new Item[]{new Item(10835, 3000), new Item(1464, 1)},
            new BossPointReward(10), new DrReward(3)),
    OPEN_25_MYSTERY_BOXES(19, AchievementType.MEDIUM, 25, "Open 25 Mystery boxes",
            new Item[]{new Item(10835, 1000), new Item(19935, 1)},
            new BossPointReward(10), new DrReward(3)),
    CLAIM_25_BONDS(20, AchievementType.MEDIUM, 25, "Claim 25 Bonds",
            new Item[]{new Item(10835, 3000), new Item(19935, 1)},
            new BossPointReward(10), new DrReward(3)),
    COMPLETE_THE_DBZ_MINIGAME(21, AchievementType.MEDIUM, 1, "Complete a DBZ Minigame",
            new Item[]{new Item(6199, 1)},
            new BossPointReward(10), new DrReward(3)),
    ENTER_THE_PORTAL_ZONE(22, AchievementType.MEDIUM, 1, "Enter the portal Zone",
            new Item[]{new Item(6640, 10)},
            new BossPointReward(10), new DrReward(3)),
    EXCHANGE_SALVAGE_FOR_DR(23, AchievementType.MEDIUM, 1, "Exchange Salvage for DR",
            new Item[]{new Item(12852, 250), new Item(1464, 1)},
            new BossPointReward(10), new DrReward(3)),
    COMPLETE_A_HARD_SLAYER_TASK(24, AchievementType.MEDIUM, 1, "Complete A Hard Slayer Task",
            new Item[]{new Item(17750, 250), new Item(1543, 1)},
            new BossPointReward(10), new DrReward(3)),
    FORGE_A_ITEM(25, AchievementType.MEDIUM, 1, "Forge an item",
            new Item[]{new Item(10835, 3000), new Item(1464, 1)},
            new BossPointReward(10), new DrReward(3)),
    DEAL_MEDIUM_DAMAGE_USING_MELEE(26, AchievementType.MEDIUM, 100000000, "Deal 100M Melee Damage",
            new Item[]{new Item(10835, 1000), new Item(1543, 1)},
            new BossPointReward(10), new DrReward(3)),
    DEAL_MEDIUM_DAMAGE_USING_RANGED(27, AchievementType.MEDIUM, 100000000, "Deal 100M Ranged Damage",
            new Item[]{new Item(10835, 1000), new Item(1543, 1)},
            new BossPointReward(10), new DrReward(3)),
    DEAL_MEDIUM_DAMAGE_USING_MAGIC(28, AchievementType.MEDIUM, 100000000, "Deal 100M Magic Damage",
            new Item[]{new Item(10835, 1000), new Item(1543, 1)},
            new BossPointReward(10), new DrReward(3)),
    DEFEAT_THE_KING_BLACK_DRAGON(29, AchievementType.MEDIUM, 1, "Defeat The King Black Dragon",
            new Item[]{new Item(17750, 250), new Item(19935, 1)},
            new BossPointReward(10), new DrReward(3)),
    DEFEAT_THE_CORPOREAL_BEAST(30, AchievementType.MEDIUM, 1, "Defeat The Corporeal Beast",
            new Item[]{new Item(10835, 500), new Item(6507, 1)},
            new BossPointReward(10), new DrReward(3)),
    KILL_NARUTO_500_TIMES(31, AchievementType.HARD, 500, "Kill Naruto 500 Times",
            new Item[]{new Item(10835, 5000), new Item(19936, 1)},
            new BossPointReward(10), new DrReward(5)),
    OPEN_250_MYSTERY_BOXES(32, AchievementType.HARD, 250, "Open 250 Mystery boxes",
            new Item[]{new Item(10168, 1)},
            new BossPointReward(10), new DrReward(5)),
    CLAIM_100_BONDS(33, AchievementType.HARD, 100, "Claim 100 Bonds",
            new Item[]{new Item(10835, 2500), new Item(16455, 1)},
            new BossPointReward(10), new DrReward(5)),
    COMPLETE_DBZ_MINIGAME_50_TIMES(34, AchievementType.HARD, 50, "Complete 50 DBZ Minigames",
            new Item[]{new Item(5266, 1), new Item(1464, 1)},
            new BossPointReward(10), new DrReward(5)),
    OPEN_10_SEPHIROTH_CHESTS(35, AchievementType.HARD, 10, "Open 10 Sephiroth Chests",
            new Item[]{new Item(10835, 500), new Item(13998, 1)},
            new BossPointReward(10), new DrReward(5)),
    OPEN_5_BLOODSLAYER_CHESTS(36, AchievementType.HARD, 5, "Open 5 Bloodslayer Chests",
            new Item[]{new Item(10835, 750), new Item(5205, 1)},
            new BossPointReward(10), new DrReward(5)),
    ENTER_THE_PORTAL_ZONE_10_TIMES(37, AchievementType.HARD, 10, "Enter Portals 10 times",
            new Item[]{new Item(19936, 1)},
            new BossPointReward(10), new DrReward(5)),
    DRINK_100_DELUGE_POTIONS(38, AchievementType.HARD, 100, "Drink 100 Deluge Potions",
            new Item[]{new Item(5185, 1)},
            new BossPointReward(10), new DrReward(5)),
    COMPLETE_AN_BLOODSLAYER_TASK(39, AchievementType.HARD, 1, "Complete A Bloodslayer Task",
            new Item[]{new Item(5205, 1)},
            new BossPointReward(10), new DrReward(5)),
    FORGE_5_ITEMS(40, AchievementType.HARD, 5, "Forge 5 Items",
            new Item[]{new Item(10835, 10000), new Item(1464, 1)},
            new BossPointReward(10), new DrReward(5)),
    DEAL_HARD_DAMAGE_USING_MELEE(41, AchievementType.HARD, 200000000, "Deal 200M Melee Damage",
            new Item[]{new Item(10835, 2500), new Item(12852, 100)},
            new BossPointReward(10), new DrReward(5)),
    DEAL_HARD_DAMAGE_USING_RANGED(42, AchievementType.HARD, 200000000, "Deal 200M Ranged Damage",
            new Item[]{new Item(10835, 2500), new Item(12852, 100)},
            new BossPointReward(10), new DrReward(5)),
    DEAL_HARD_DAMAGE_USING_MAGIC(43, AchievementType.HARD, 200000000, "Deal 200M Magic Damage",
            new Item[]{new Item(10835, 2500), new Item(12852, 100)},
            new BossPointReward(10), new DrReward(5)),
    DEFEAT_NEX(44, AchievementType.HARD, 1, "Defeat Nex",
            new Item[]{new Item(10835, 2500), new Item(12852, 100)},
            new BossPointReward(10), new DrReward(5)),
    OPEN_100_SEPHIROTH_CHESTS(45, AchievementType.HARD, 100, "Open 100 Sephiroth Chests",
            new Item[]{new Item(10835, 7500), new Item(19936, 1)},
            new BossPointReward(10), new DrReward(5)),
    OPEN_1000_MYSTERY_BOXES(46, AchievementType.HARD, 1000, "Open 1000 Mystery Boxes",
            new Item[]{new Item(10835, 7500), new Item(19936, 1)},
            new BossPointReward(10), new DrReward(5)),
    DEFEAT_10000_MONSTERS(47, AchievementType.HARD, 10_000, "Defeat 10,000 Monsters",
            new Item[]{new Item(10835, 7500), new Item(19936, 1)},
            new BossPointReward(10), new DrReward(5)),

//daily

    DAILY_VOTER(48, AchievementType.DAILY, 1, "Vote today",
            new Item[]{new Item(19670, 3), new Item(10835, 250)},
            new BossPointReward(10), new DrReward(2)),
    KILL_1000_MONSTERS(49, AchievementType.DAILY, 1000, "Kill 1,000 Monsters",
            new Item[]{new Item(10835, 2500)},
            new BossPointReward(10), new DrReward(2)),
    OPEN_10_BOXES(50, AchievementType.DAILY, 10, "Open 10x of any Mystery Box",
            new Item[]{new Item(10835, 500), new Item(1464, 1)},
            new BossPointReward(10), new DrReward(2)),
    COMPLETE_10_SLAYER(51, AchievementType.DAILY, 10, "Complete 10 Slayer Tasks",
            new Item[]{new Item(5205, 1)},
            new BossPointReward(10), new DrReward(2)),
    KILL_MASSBOSS(52, AchievementType.DAILY, 1, "Kill Diablo [::mass]",
            new Item[]{new Item(10835, 1000)},
            new BossPointReward(10), new DrReward(2)),
    MINE_10000_TICKETS(53, AchievementType.DAILY, 1, "Mine 10,000 afk rocks [::afk] ",
            new Item[]{new Item(10835, 10_000)},
            new DrReward(2)),


    ;

    public static final AchievementData[] values = AchievementData.values();
    public static final AchievementData[][] arraysByType = new AchievementData[AchievementType.values().length][];
    final int id;
    final AchievementType type;
    final String description;
    final int progressAmount;
    final Item[] itemRewards;
    final NonItemReward[] nonItemRewards;
    AchievementData(int id, AchievementType type, int progressAmount, String description, Item[] itemRewards, NonItemReward... nonItemRewards) {
        this.id = id;
        this.type = type;
        this.progressAmount = progressAmount;
        this.description = description;
        this.itemRewards = itemRewards;
        this.nonItemRewards = nonItemRewards;
    }

    public static void checkDuplicateIds() {
        Set<Integer> ids = new HashSet<>();
        for (AchievementData achievement : values) {
            if (ids.contains(achievement.id)) {
                System.err.println("AchievementData sharing the same id!!! Shutting Down. Each achievement must have a unique id.");
                for (AchievementData data : values) {
                    if (data.id == achievement.id) {
                        System.out.println(data.name() + " id: " + data.id);
                    }
                }
                System.exit(0);
            }
            ids.add(achievement.id);
        }
    }

    public static AchievementData[] getAchievementsOfType(AchievementType type) {
        int index = type.ordinal();
        if (arraysByType[index] != null) {
            return arraysByType[index];
        }
        arraysByType[index] = Arrays.stream(AchievementData.values).filter(a -> a.type.equals(type)).toArray(AchievementData[]::new);
        return arraysByType[index];
    }

    @Override
    public String toString() {
        return WordUtils.capitalize(this.name().toLowerCase().replaceAll("_", " "));
    }
}
