package com.arlania.world.content.customraid;

import com.arlania.engine.task.Task;
import com.arlania.engine.task.TaskManager;
import com.arlania.model.Position;
import com.arlania.model.definitions.ItemDefinition;
import com.arlania.util.Misc;
import com.arlania.world.World;
import com.arlania.world.entity.impl.npc.NPC;
import com.arlania.world.entity.impl.player.Player;

import java.security.SecureRandom;
import java.util.*;
//zulu 11 might work, suic added raids with java 9 _
public class CustomRaid {

    private final Player player;

    public CustomRaid(Player player) {
        this.player = player;
    }

    private final Map<RaidDifficulty, List<List<NPC>>> waves = new HashMap<>();

    private final Map<String, Long> damageMap = new HashMap<>();

    public Map<String, Long> getDamageMap() {
        return damageMap;
    }

    public void incrementDamage(String username, long damage) {
        damageMap.merge(username, damage, Long::sum);
        player.getCustomRaidParty().getMembers().forEach(this::updateOverlay);
    }

    private final Random random = new SecureRandom();

    private final int OVERLAY_ID = 27800;

    private final Position RAID_LOBBY_POSITION = new Position(2828, 3892);
    private final Position RAID_CHEST_POSITION = new Position(2938, 3901);

    public void open(Player player) {
        player.getPacketSender().sendInterface(27680);
        clearOverlay();
        updateOverlay(player);
    }

    private void updateOverlay(Player player) {
        List<Map.Entry<String, Long>> sortedDamageMap = sortEntries(damageMap);
        for (int i = 0; i < 5; i++) {
            player.getPacketSender().sendString(OVERLAY_ID + 2 + i, "");
        }
        int index = 0;
        System.out.println("damageMapSize: " + sortedDamageMap.size());
        for (Map.Entry<String, Long> entry : sortedDamageMap) {
            player.getPacketSender()
                .sendString(OVERLAY_ID + 2 + index, "@red@" + entry.getKey() + ": " + Misc
                    .formatNumber(entry.getValue()));
            index++;
        }
    }

    private <K, V extends Comparable<? super V>> List<Map.Entry<K, V>> sortEntries(Map<K, V> map) {
        List<Map.Entry<K, V>> sortedEntries = new ArrayList<>(map.entrySet());
        sortedEntries.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));
        return sortedEntries;
    }

    private int currentWave = 0;
    private int count = 0;
    private int targetCount = 0;

    public void init(Player other) {
        if (!player.getCustomRaidParty().isOwner(other)) {
            other.sendMessage("Only the party owner can start the raid");
            return;
        }
        if (player.isInRaid()) {
            return;
        }
        RaidDifficulty raidDifficulty = player.getCustomRaidParty().getRaidDifficulty();
        initNpcs();
        List<List<NPC>> npcs = waves.get(raidDifficulty);
        npcs.stream().flatMap(List::stream).forEach(npc -> {
            npc.setPosition(new Position(npc.getPosition().getX(), npc.getPosition()
                .getY(), player.getIndex() * 4));
        });
        damageMap.clear();
        player.getCustomRaidParty().getMembers().forEach(member -> {
            member.moveTo(raidDifficulty.getX(), raidDifficulty.getY(), player.getIndex() * 4);
            member.setInRaid(true);
            player.getCustomRaidParty().getMembers().forEach(this::updateOverlay);
        });
        initWave();
    }

    private void initNpcs() {
        int height = player.getIndex() * 4;
        waves.clear();
        waves.put(
            RaidDifficulty.EASY,
            List.of(
                List.of(new NPC(10001, new Position(2832, 3795, height))),
                List.of(new NPC(10002, new Position(2869, 3829, height))),
                List.of(new NPC(10003, new Position(2908, 3797, height))),
                List.of(new NPC(10000, new Position(2931, 3829, height)))
            )

        );

        waves.put(
            RaidDifficulty.HARD,
            List.of(
                List.of(new NPC(9273, new Position(3333, 3333, height)), new NPC(97, new Position(3334, 3334, height))),
                List.of(new NPC(131, new Position(3333, 3333, height)), new NPC(97, new Position(3333, 3335, height)), new NPC(9273, new Position(2611, 3970, height)))
            )
        );
    }

    private void initWave() {
        RaidDifficulty raidDifficulty = player.getCustomRaidParty().getRaidDifficulty();
        if (currentWave >= waves.get(raidDifficulty).size()) {
            handleFinish(false);
            return;
        }
        initNpcs();
        List<NPC> npcs = waves.get(raidDifficulty).get(currentWave);
        count = 0;
        targetCount = npcs.size();
        if (currentWave == 0) {
            player.getCustomRaidParty()
                .getMembers()
                .forEach(member -> member.getPacketSender().closeAllWindows());
            player.getCustomRaidParty()
                .getMembers()
                .forEach(member -> member.sendMessage("Starting first wave in 3 seconds"));
        } else {
            player.getCustomRaidParty()
                .getMembers()
                .forEach(member -> member.sendMessage("Wave " + currentWave + " finished, next wave starting in 3 seconds"));
        }
        TaskManager.submit(new Task(5) {
            @Override
            protected void execute() {
                player.getCustomRaidParty()
                    .getMembers()
                    .forEach(member -> {
                        member.getPacketSender().sendInterfaceRemoval();
                        member.getPacketSender().sendWalkableInterface(OVERLAY_ID, true);
                    });
                npcs.forEach(npc -> {
                    npc.setShouldRespawn(false);
                    World.register(npc);
                });
                stop();
            }
        });
    }

    public void clearOverlay() {
        player.getPacketSender().sendWalkableInterface(OVERLAY_ID, false);
        for (int i = 0; i < 5; i++) {
            player.getPacketSender().sendString(OVERLAY_ID + 2 + i, "");
        }

    }

    public void handleFinish(boolean onOwnerLeave) {
        System.out.println("finished: " + onOwnerLeave);
        waves.values()
            .stream()
            .flatMap(List::stream)
            .flatMap(List::stream)
            .forEach(World::deregister);
        player.getCustomRaidParty()
            .getMembers()
            .forEach(member -> {
                member.setInRaid(false);
                currentWave = 0;
                count = 0;
                targetCount = 0;
                member.sendMessage("Raid has finished");
                member.getPacketSender().sendWalkableInterface(OVERLAY_ID, false);
                member.moveTo(RAID_LOBBY_POSITION);
                if (!onOwnerLeave) {
                    member.getInventory()
                        .add(member.getCustomRaidParty()
                            .getRaidDifficulty() == RaidDifficulty.EASY ? EASY_KEY_ID : HARD_KEY_ID, 1);
                    handleReward(member);
                }
            });
        damageMap.clear();
    }

    public boolean highestDamager(String name) {
        if (damageMap.get(name) == null) {
            return false;
        }
        return damageMap.get(name)
            .equals(damageMap.values().stream().max(Comparator.naturalOrder()).orElse(-1L));
    }

    public void handleKill() {
        count++;
        if (count == targetCount) {
            currentWave++;
            initWave();
        }
    }

    public void handleDeath(Player player) {
        if (player.getCustomRaidParty().isOwner(player)) {
            handleFinish(true);
        } else {
            System.out.println("from " + damageMap + " removing " + player.getUsername());
            damageMap.remove(player.getUsername());
            player.getCustomRaidParty().getMembers().forEach(this::updateOverlay);
            player.getCustomRaidParty().leave(player);
            player.getPacketSender().sendWalkableInterface(OVERLAY_ID, false);
        }
    }

    public void handleLogout(Player player) {
        if (player.getCustomRaidParty().isOwner(player)) {
            handleFinish(true);
        } else {
            System.out.println("from " + damageMap + " removing " + player.getUsername());
            damageMap.remove(player.getUsername());
            player.getCustomRaidParty().getMembers().forEach(this::updateOverlay);
            player.getCustomRaidParty().leave(player);
            player.moveTo(RAID_LOBBY_POSITION);
            player.getPacketSender().sendWalkableInterface(OVERLAY_ID, false);
        }
    }

    private final int[] EASY_COMMON_REWARDS = {19935, 3912, 3988, 1543, 3980, 3999, 4000, 4001, 18955, 18956, 18957, 5167, 15649, 15650, 15651, 15652, 15653, 15654, 15655, 4761, 4762, 4763, 4764, 4765, 5089, 930, 931, 5211, 926, 5210, 3820, 3821, 3822, 20054, 4781, 4782, 20240, 4785, 5195, 15032, 3985, 5082, 5083, 5084, 15656, 17151, 5129, 19470, 19471, 19472, 19473, 19474, 19619, 4641, 4642, 4643, 3983, 3064, 19618, 19620, 19691, 19692, 19693, 19694, 19695, 19696, 19159, 19160, 19161, 19163, 19164, 19165, 19166, 9492, 9493, 9494, 9495, 14490, 14492, 14494};
    private final int[] EASY_RARE_REWARDS = {10168, 7036, 13999, 4652, 14546, 3969, 3970, 3968, 3967, 3966, 19936, 16455, 3891, 5266,11425,10500,10502};

    private final int[] HARD_COMMON_REWARDS = {11694, 11696, 11698};
    private final int[] HARD_RARE_REWARDS = {14484};

    private final int EASY_KEY_ID = 1;
    private final int HARD_KEY_ID = 1;

    public void handleReward(Player player) {
        if (!player.getInventory().contains(EASY_KEY_ID) && !player.getInventory()
            .contains(HARD_KEY_ID)) {
            player.sendMessage("You need a raid key to open this chest");
            return;
        }

        player.getPacketSender().resetItemsOnInterface(27832, 3);
        player.getPacketSender().sendInterface(27830);
        if (player.getCustomRaidParty().getRaidDifficulty() == RaidDifficulty.EASY) {
            int amount = player.getInventory().getAmount(EASY_KEY_ID);
            for (int i = 0; i < amount; i++) {
                int first = random.nextDouble() > 0.00666666666D ? EASY_COMMON_REWARDS[random.nextInt(EASY_COMMON_REWARDS.length)] : EASY_COMMON_REWARDS[random
                    .nextInt(EASY_COMMON_REWARDS.length)];
                int second = random.nextDouble() < 0.00666666666D ? EASY_RARE_REWARDS[random.nextInt(EASY_RARE_REWARDS.length)] : EASY_COMMON_REWARDS[random
                    .nextInt(EASY_RARE_REWARDS.length)];

                if (contains(EASY_RARE_REWARDS, first)) {
                    World.sendMessage("[RAID REWARD] " + player.getUsername() + " got a " + ItemDefinition
                        .forId(first)
                        .getName() + " from raid");
                }

                if (contains(EASY_RARE_REWARDS, second)) {
                    World.sendMessage("[RAID REWARD] " + player.getUsername() + " got a " + ItemDefinition
                        .forId(second)
                        .getName() + " from raid");
                }
                
                player.getPacketSender().sendItemOnInterface(27832, first, 0, 1);
                player.getPacketSender().sendItemOnInterface(27832, second, 1, 1);
                player.getInventory().delete(EASY_KEY_ID);
                player.getInventory().addItems(1, first, second);
                if (contains(EASY_RARE_REWARDS, first) || contains(EASY_RARE_REWARDS, second)) {
                    World.sendMessage("[RAID REWARD] " + player.getUsername() + " got a rare reward from raid");
                }
            }
        } else {
            int amount = player.getInventory().getAmount(HARD_KEY_ID);
            for (int i = 0; i < amount; i++) {
                int first = random.nextDouble() < 0.00666666666D ? HARD_RARE_REWARDS[random.nextInt(HARD_RARE_REWARDS.length)] : HARD_COMMON_REWARDS[random
                    .nextInt(HARD_COMMON_REWARDS.length)];
                int second = random.nextDouble() < 0.00666666666D ? HARD_RARE_REWARDS[random.nextInt(HARD_RARE_REWARDS.length)] : HARD_COMMON_REWARDS[random
                    .nextInt(HARD_COMMON_REWARDS.length)];
                int third = random.nextDouble() < 0.00666666666D ? HARD_RARE_REWARDS[random.nextInt(HARD_RARE_REWARDS.length)] : HARD_COMMON_REWARDS[random
                    .nextInt(HARD_COMMON_REWARDS.length)];

                if (contains(HARD_RARE_REWARDS, first)) {
                    World.sendMessage("[RAID REWARD] " + player.getUsername() + " got a " + ItemDefinition
                        .forId(first)
                        .getName() + " from raid");
                }

                if (contains(HARD_RARE_REWARDS, second)) {
                    World.sendMessage("[RAID REWARD] " + player.getUsername() + " got a " + ItemDefinition
                        .forId(second)
                        .getName() + " from raid");
                }

                if (contains(HARD_RARE_REWARDS, third)) {
                    World.sendMessage("[RAID REWARD] " + player.getUsername() + " got a " + ItemDefinition
                        .forId(third)
                        .getName() + " from raid");
                }

                player.getPacketSender().sendItemOnInterface(27832, first, 0, 1);
                player.getPacketSender().sendItemOnInterface(27832, second, 1, 1);
                player.getPacketSender().sendItemOnInterface(27832, third, 2, 1);
                player.getInventory().delete(HARD_KEY_ID, 1);
                player.getInventory().addItems(1, first, second, third);
            }
        }


    }

    private boolean contains(int[] array, int value) {
        for (int i : array) {
            if (i == value) {
                return true;
            }
        }
        return false;
    }

}
