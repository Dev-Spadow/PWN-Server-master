package com.arlania.world.content.dailytasks;

import com.arlania.engine.task.TaskManager;
import com.arlania.model.Item;
import com.arlania.world.entity.impl.player.Player;
import com.moandjiezana.toml.Toml;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public enum DailyTasks {
    INSTANCE;

    public static final String POINTS_ATTRIBUTE_KEY = "dailytask_points";
    public static final String LAST_DATE_PLAYED_ATTRIBUTE_KEY = "dailytask_last_day_played";
    public static final String STREAK_ATTRIBUTE_KEY = "dailytask_streak";
    public static final String STREAK_PROTECTION_ATTRIBUTE_KEY = "daily_task_streak_protection";
    public static final String STREAK_LONGEST_ATTRIBUTE_KEY = "dailytask_streak_longest";
    public static final String TOTAL_EASY_COMPLETED_ATTRIBUTE_KEY = "dailytask_completed_easy";
    public static final String TOTAL_MEDIUM_COMPLETED_ATTRIBUTE_KEY = "dailytask_completed_medium";
    public static final String TOTAL_HARD_COMPLETED_ATTRIBUTE_KEY = "dailytask_completed_hard";
    public static final String TASK_STATUS_ATTRIBUTE_KEY = "dailytask_status";
    public static final String TASK_PROGRESS_ATTRIBUTE_KEY = "dailytask_progress";
    public static final String LAST_LOGIN_TASKS_COMPLETED_ATTRIBUTE_KEY = "dailytask_last_login_completed_count";
    public static final String LAST_LOGIN_TASKS_TOTAL_ATTRIBUTE_KEY = "dailytask_last_login_task_count";
    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("dd, MMMM yyyy");
    private static final int SHOP_INTERFACE = 48027;
    private static final int SHOP_ITEM_INTERFACE = 48128;
    private static final int SHOP_HEADER_INTERFACE = 48115;
    private static final int SHOP_POINTS_INTERFACE = 48118;
    private static final int DAILY_TASKS_INTERFACE = 19712;
    private static final int DAILY_TASKS_HEADER_INTERFACE = 19716;
    private static final int DAILY_TASKS_POINTS_INTERFACE = 19719;
    private static final int TASKS_COMPLETED_INTERFACE = 19730;
    private static final int VOTING_BONUS_VARP = 3550;
    private static final int TASK_PROGRESS_VARP = 3551;
    private static final int TASK_LOCKED_VARP = 3557;
    private static final int TASK_CATEGORY_INTERFACE = 19732;
    private static final int TASK_PROGRESS_INTERFACE = 19737;
    private static final int TASK_INVENTORY_INTERFACE = 19738;
    private static final int TASK_DESCRIPTION_INTERFACE = 19739;
    private static final int TASK_REWARD_INTERFACE = 19740;
    private static final int STREAK_INTERFACE = 49179;
    private static final int STREAK_HEADER_INTERFACE = 49183;
    private static final int STREAK_POINTS_INTERFACE = 49186;
    private static final int STREAK_DAYS_INTERFACE = 49196;
    private static final int STREAK_PROTECTION2_INTERFACE = 49244;
    private static final int STREAK_PROTECTION_INTERFACE = 49197;
    private static final int STREAK_CLAIM_VARP = 3570;
    private static final int STREAK_REWARD_INTERFACE = 49199;
    private static final int STREAK_DAYS_REQUIRED_INTERFACE = 49207;
    private static final int STREAK_STATS_INTERFACE = 49251;
    private static final String STREAK_CLAIMED_ATTRIBUTE_KEY = "dailytask_streak_claimed";
    private static final int STREAK_LONGEST_INTERFACE = 49252;
    private static final int STREAK_TOTAL_EASY_INTERFACE = 49253;
    private static final int STREAK_TOTAL_MEDIUM_INTERFACE = 49254;
    private static final int STREAK_TOTAL_HARD_INTERFACE = 49256;
    private static final int STREAK_PROTECTION_COST_INTERFACE = 49243;
    private static final int DAILY_TASKS_TIMER_INTERFACE = 19729;
    private final List<TaskShopItem> shopItems = new LinkedList<>();
    private final List<DailyTask> activeTasks = new LinkedList<>();
    private final List<DailyTask> availableTasks = new LinkedList<>();
    private final List<StreakReward> streakRewards = new ArrayList<>();
    private LocalDateTime lastUpdate;
    private List<DailyTask> easyTasks = activeTasks.stream().filter(t -> t.getCategory() == TaskCategory.EASY).collect(Collectors.toList());
    private List<DailyTask> medTasks = activeTasks.stream().filter(t -> t.getCategory() == TaskCategory.MEDIUM).collect(Collectors.toList());
    private List<DailyTask> hardTasks = activeTasks.stream().filter(t -> t.getCategory() == TaskCategory.HARD).collect(Collectors.toList());
    private int streakProtectionCost = 0;

    public List<DailyTask> getActiveTasks() {
        return activeTasks;
    }

    public void initialize() {
        final Toml config = new Toml().read(new File("./data/def/dailytasks.toml"));
        streakProtectionCost = Math.toIntExact(config.getLong("streakprotection_cost"));
        availableTasks.addAll(config.getTables("task").stream().map(t -> t.to(DailyTask.class)).collect(Collectors.toList()));
        shopItems.addAll(config.getTables("shopitem").stream().map(s -> s.to(TaskShopItem.class)).collect(Collectors.toList()));
        streakRewards.addAll(config.getTables("streak").stream().map(s -> s.to(StreakReward.class)).collect(Collectors.toList()));
        TaskManager.submit(new DailyUpdateTask());
    }


    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(final LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    void updateDailyTasks() {
        activeTasks.clear();
        final List<DailyTask> easy = availableTasks.stream().filter(t -> t.getCategory() == TaskCategory.EASY).collect(Collectors.toList());
        Collections.shuffle(easy);
        activeTasks.addAll(easy.subList(0, 2));
        final List<DailyTask> medium = availableTasks.stream().filter(t -> t.getCategory() == TaskCategory.MEDIUM).collect(Collectors.toList());
        Collections.shuffle(medium);
        activeTasks.addAll(medium.subList(0, 2));
        final List<DailyTask> hard = availableTasks.stream().filter(t -> t.getCategory() == TaskCategory.HARD).collect(Collectors.toList());
        Collections.shuffle(hard);
        activeTasks.addAll(hard.subList(0, 2));
        easyTasks = activeTasks.stream().filter(t -> t.getCategory() == TaskCategory.EASY).collect(Collectors.toList());
        medTasks = activeTasks.stream().filter(t -> t.getCategory() == TaskCategory.MEDIUM).collect(Collectors.toList());
        hardTasks = activeTasks.stream().filter(t -> t.getCategory() == TaskCategory.HARD).collect(Collectors.toList());
    }

    public boolean handleButtonClick(final Player player, final int buttonIndex) {
        switch (buttonIndex) {
            case -16346:
            case 19723:
                openShopInterface(player);
                return true;
            case -16348:
            case -17416:
                openTasksInterface(player);
                return true;
            case 19725:
            case -17412:
                openStreaksInterface(player);
                return true;
            case -16289:
                purchaseStreakProtection(player);
                return true;
            case -16354:
            case -17422:
            case 19715:
                closeInterface(player);
                return true;
        }
        return purchaseItem(player, buttonIndex) || claimTask(player, buttonIndex) || claimStreak(player, buttonIndex);
    }

    public void openShopInterface(final Player player) {
        sendHeaderText(player, SHOP_HEADER_INTERFACE, SHOP_POINTS_INTERFACE);
        for (int i = 0; i < shopItems.size(); i++) {
            final TaskShopItem item = shopItems.get(i);
            player.getPacketSender().sendItemOnInterface(SHOP_ITEM_INTERFACE + 1 + i * 7, item.getIndex(), item.getAmount())
                .sendString(SHOP_ITEM_INTERFACE + 2 + i * 7, item.getName())
                .sendString(SHOP_ITEM_INTERFACE + 3 + i * 7, "- " + item.getDescription())
                .sendString(SHOP_ITEM_INTERFACE + 5 + i * 7, item.getCost() + " Points");
        }
        sendInterface(player, SHOP_INTERFACE);
    }

    public void openTasksInterface(final Player player) {
        final LocalDateTime time = LocalDateTime.now();
        final int completedEasy = (int) easyTasks.stream().map(t -> {
            final TaskStatus status = player.getAttribute(t.getKey() + "_status", TaskStatus.INCOMPLETE);
            return status == TaskStatus.COMPLETE || status == TaskStatus.CLAIMED;
        }).filter(t -> t).count();
        final int completedMedium = (int) medTasks.stream().map(t -> {
            final TaskStatus status = player.getAttribute(t.getKey() + "_status", TaskStatus.INCOMPLETE);
            return status == TaskStatus.COMPLETE || status == TaskStatus.CLAIMED;
        }).filter(t -> t).count();
        final int completedHard = (int) hardTasks.stream().map(t -> {
            final TaskStatus status = player.getAttribute(t.getKey() + "_status", TaskStatus.INCOMPLETE);
            return status == TaskStatus.COMPLETE || status == TaskStatus.CLAIMED;
        }).filter(t -> t).count();
        final int completedTasks = completedEasy + completedMedium + completedHard;
        player.getPacketSender().sendString(TASKS_COMPLETED_INTERFACE, "Tasks completed: " + completedTasks + "/" + activeTasks.size())
            .sendString(TASK_CATEGORY_INTERFACE, "Easy tasks (" + completedEasy + "/2)")
            .sendString(TASK_CATEGORY_INTERFACE + 28, "Medium tasks (" + completedMedium + "/2)")
            .sendString(TASK_CATEGORY_INTERFACE + 56, "Hard tasks (" + completedHard + "/2)");
        final int categorySize = activeTasks.size() / 3;
        for (int i = 0, index = 0, cat = 0, offset = 12; i < activeTasks.size(); i++) {
            final DailyTask task = cat == 0 ? easyTasks.get(index) : cat == 1 ? medTasks.get(index) : hardTasks.get(index);
            player.getPacketSender().sendToggle(TASK_PROGRESS_VARP + i, getProgressBarWidth(player, task))
                .sendItemOnInterface(TASK_INVENTORY_INTERFACE + i * offset + (2 * (i / 2)), task.getDisplayIndex(), task.getDisplayAmount())
                .sendString(TASK_PROGRESS_INTERFACE + i * offset + (2 * (i / 2)), formatProgressBarText(player, task))
                .sendString(TASK_DESCRIPTION_INTERFACE + i * offset + (2 * (i / 2)), task.getDescription())
                .sendString(TASK_REWARD_INTERFACE + i * offset + (2 * (i / 2)), task.getReward() + " POINTS")
                .sendString(DAILY_TASKS_TIMER_INTERFACE, "New Tasks In: @or1@" + (23 - time.getHour()) + "h " + (60 - time.getMinute()) + "m");
            final DailyTask dailyTask = activeTasks.get(i);
            final TaskStatus status = player.getAttribute(dailyTask.getKey() + "_status", TaskStatus.INCOMPLETE);
            player.getPacketSender().sendConfig(TASK_LOCKED_VARP + i, status.getVarpValue());
            index++;
            if (index == categorySize) {
                index = 0;
                cat++;
            }
        }
        sendHeaderText(player, DAILY_TASKS_HEADER_INTERFACE, DAILY_TASKS_POINTS_INTERFACE);
        sendInterface(player, DAILY_TASKS_INTERFACE);
    }

    public int getLongestStreak(final Player player) {
        final int longest = player.getAttribute(STREAK_LONGEST_ATTRIBUTE_KEY, 0);
        final int current = player.getAttribute(STREAK_ATTRIBUTE_KEY, 0);
        return Math.max(current, longest);
    }

    public void purchaseStreakProtection(final Player player) {
        final int donorPoints = player.getPointsHandler().getDonationPoints();
        if (donorPoints < streakProtectionCost) {
            player.sendMessage("You don't have enough donator points for that!");
            player.sendMessage("Streak protection costs " + streakProtectionCost + " donator points.");
        } else {
            player.getPointsHandler().setDonationPoints(donorPoints - streakProtectionCost, false);
        }
    }

    public void openStreaksInterface(final Player player) {
        final int streakDays = player.getAttribute(STREAK_ATTRIBUTE_KEY, 0);
        final int protectionDays = player.getAttribute(STREAK_PROTECTION_ATTRIBUTE_KEY, 0);
        player.getPacketSender().sendString(STREAK_DAYS_INTERFACE, "Your Current Streak: " + streakDays + (streakDays == 1 ? " Day" : " Days"))
            .sendString(STREAK_PROTECTION_INTERFACE, "Streak Protection: " + (protectionDays < 1 ? "@red@" : "@gre@") + protectionDays + (protectionDays == 1 ? " Day" : " Days"))
            .sendString(STREAK_PROTECTION2_INTERFACE, (protectionDays < 1 ? "@red@" : "@gre@") + protectionDays + (protectionDays == 1 ? " Day" : " Days"))
            .sendString(STREAK_STATS_INTERFACE, "Your Daily Task Statistics (" + player.getUsername() + ")")
            .sendString(STREAK_LONGEST_INTERFACE, "Longest Daily Streak: " + getLongestStreak(player))
            .sendString(STREAK_TOTAL_EASY_INTERFACE, "Total Easy Tasks Completed: " + player.getAttribute(TOTAL_EASY_COMPLETED_ATTRIBUTE_KEY, 0))
            .sendString(STREAK_TOTAL_MEDIUM_INTERFACE, "Total Medium Tasks Completed: " + player.getAttribute(TOTAL_MEDIUM_COMPLETED_ATTRIBUTE_KEY, 0))
            .sendString(STREAK_TOTAL_HARD_INTERFACE, "Total Hard Tasks Completed: " + player.getAttribute(TOTAL_HARD_COMPLETED_ATTRIBUTE_KEY, 0))
            .sendString(STREAK_PROTECTION_COST_INTERFACE, "Streak Protection protects you from losing \\nyour streak if you miss a day by accident \\nor if you go on vacation. Streak Protection \\nstarts at "
                + streakProtectionCost + " Donator Points per day.");
        final boolean[] claimed = player.getAttribute(STREAK_CLAIMED_ATTRIBUTE_KEY, new boolean[streakRewards.size()]);
        for (int i = 0; i < streakRewards.size(); i++) {
            final StreakReward reward = streakRewards.get(i);
            player.getPacketSender().sendConfig(STREAK_CLAIM_VARP + i, claimed[i] || streakDays < reward.getDays() ? 0 : 1)
                .sendString(STREAK_REWARD_INTERFACE + i, reward.getPoints() + " \\n@or2@POINTS")
                .sendString(STREAK_DAYS_REQUIRED_INTERFACE + i, reward.getDays() + " DAY");
        }
        sendHeaderText(player, STREAK_HEADER_INTERFACE, STREAK_POINTS_INTERFACE);
        sendInterface(player, STREAK_INTERFACE);
    }

    private boolean purchaseItem(final Player player, final int buttonIndex) {
        if (buttonIndex >= -17408 && buttonIndex <= -17338) {
            final int points = player.getAttribute("dailytask_points", 0);
            final TaskShopItem shopItem = shopItems.get((Math.abs(-17408) - Math.abs(buttonIndex)) / 7);
            if (points < shopItem.getCost()) {
                player.sendMessage("You don't have enough points for that item!");
            } else {
                final Item item = new Item(shopItem.getIndex(), shopItem.getAmount());
                final boolean stackable = item.getDefinition().isStackable();
                final int requiredSlots = stackable ? 1 : item.getAmount();
                if (player.getInventory().getFreeSlots() < requiredSlots) {
                    final int invSlot = player.getInventory().getSlot(item.getId());
                    if (stackable && invSlot != -1 && Integer.MAX_VALUE <= player.getInventory().get(invSlot).getAmount() + item.getAmount()) {
                        final int amount = player.getInventory().get(invSlot).getAmount() + item.getAmount();
                        player.getInventory().delete(item.getId(), invSlot);
                        player.getInventory().add(item.getId(), amount);
                        player.sendMessage("Your reward has been added to your inventory!");
                        player.setAttribute("dailytask_points", points - shopItem.getCost());
                        player.getPacketSender().sendString(SHOP_POINTS_INTERFACE, "Points: " + (points - shopItem.getCost()));
                    } else {
                        player.sendMessage("You don't have enough free inventory slots!");
                    }
                } else {
                    player.getInventory().addItem(item);
                    player.sendMessage("Your reward has been added to your inventory!");
                    player.setAttribute("dailytask_points", points - shopItem.getCost());
                    player.getPacketSender().sendString(SHOP_POINTS_INTERFACE, "Points: " + (points - shopItem.getCost()));
                }
            }
            return true;
        }
        return false;
    }

    private boolean claimTask(final Player player, final int buttonIndex) {
        if (buttonIndex >= 19743 && buttonIndex <= 19807) {
            final int slot = (buttonIndex - 19743) / 12;
            final DailyTask task = activeTasks.get(slot);
            final int progress = player.getAttribute(task.getKey(), 0);
            final TaskStatus status = player.getAttribute(task.getKey() + "_status", TaskStatus.INCOMPLETE);
            if (task.getAmount() <= progress && status != TaskStatus.CLAIMED) {
                player.setAttribute(task.getKey() + "_status", TaskStatus.CLAIMED);
                player.setAttribute(POINTS_ATTRIBUTE_KEY, player.getAttribute(POINTS_ATTRIBUTE_KEY, 0) + task.getReward() + (player.hasVotedToday ? task.getReward() * 0.2 : 0));
                player.getPacketSender().sendConfig(TASK_LOCKED_VARP + slot, TaskStatus.CLAIMED.getVarpValue())
                    .sendMessage("You have been rewarded with " + task.getReward() + " points!");
                if (player.hasVotedToday) {
                    player.sendMessage("You received an additional " + task.getReward() * 0.2 + " points for voting!");
                }
                sendHeaderText(player, DAILY_TASKS_HEADER_INTERFACE, DAILY_TASKS_POINTS_INTERFACE);
            }
            return true;
        }
        return false;
    }

    private void sendHeaderText(final Player player, final int dateInterface, final int pointsInterface) {
        final int points = player.getAttribute("dailytask_points", 0);
        player.getPacketSender().sendString(dateInterface, "Pwnlite Daily Tasks - " + DATE_FORMATTER.format(new Date()))
            .sendString(pointsInterface, "Points: " + points)
            .sendConfig(VOTING_BONUS_VARP, player.hasVotedToday ? 1 : 0);
    }

    private void closeInterface(final Player player) {
        player.getPacketSender().sendInterfaceRemoval();
        player.getSkillManager().stopSkilling();
    }

    private void sendInterface(final Player player, final int index) {
        closeInterface(player);
        player.getPacketSender().sendInterface(index);
    }

    private int getProgressBarWidth(final Player player, final DailyTask task) {
        final int progress = getTaskProgress(player, task);
        return (int) (4.09 * ((progress / (double) task.getAmount()) * 100.0));
    }

    private boolean claimStreak(final Player player, final int buttonIndex) {
        final int slot = (Math.abs(-16320) - Math.abs(buttonIndex)) / 3;
        if (slot >= 0 && slot <= streakRewards.size()) {
            final StreakReward reward = streakRewards.get(slot);
            if (player.getAttribute(STREAK_CLAIMED_ATTRIBUTE_KEY, new boolean[streakRewards.size()])[slot]) {
                return true;
            } else if (player.getAttribute(STREAK_ATTRIBUTE_KEY, 0) < reward.getDays()) {
                player.sendMessage("Your daily task streak isn't long enough to claim that.");
            } else {
                player.setAttribute(POINTS_ATTRIBUTE_KEY, player.getAttribute(POINTS_ATTRIBUTE_KEY, 0) + reward.getPoints());
                player.sendMessage("You have been rewarded with " + reward.getPoints() + " points for your streak!");
                final boolean[] claimed = player.getAttribute(STREAK_CLAIMED_ATTRIBUTE_KEY, new boolean[streakRewards.size()]);
                claimed[slot] = true;
                player.setAttribute(STREAK_CLAIMED_ATTRIBUTE_KEY, claimed);
                player.getPacketSender().sendConfig(STREAK_CLAIM_VARP + slot, 0);
            }
            return true;
        }
        return false;
    }

    private String formatProgressBarText(final Player player, final DailyTask task) {
        final int progress = getTaskProgress(player, task);
        return progress + " / " + task.getAmount() + " - " + ((progress / (double) task.getAmount()) * 100) + "%";
    }

    public void updateTaskProgress(final TaskType type, final Player player, final int index, final int amount) {
        resetProgress(player);
        activeTasks.stream().filter(t -> t.getType() == type && t.getIndex() == index && getTaskProgress(player, t) < t.getAmount()).findFirst().ifPresent(task -> {
            final int progress = getTaskProgress(player, task);
            final int updatedProgress = Math.min(progress + amount, task.getAmount());
            if (progress < task.getAmount()) {
                player.setAttribute(task.getKey(), updatedProgress);
                if (updatedProgress == task.getAmount()) {
                    switch (task.getCategory()) {
                        case EASY:
                            player.setAttribute(TOTAL_EASY_COMPLETED_ATTRIBUTE_KEY, player.getAttribute(TOTAL_EASY_COMPLETED_ATTRIBUTE_KEY, 0) + 1);
                            break;
                        case MEDIUM:
                            player.setAttribute(TOTAL_MEDIUM_COMPLETED_ATTRIBUTE_KEY, player.getAttribute(TOTAL_MEDIUM_COMPLETED_ATTRIBUTE_KEY, 0) + 1);
                            break;
                        case HARD:
                            player.setAttribute(TOTAL_HARD_COMPLETED_ATTRIBUTE_KEY, player.getAttribute(TOTAL_HARD_COMPLETED_ATTRIBUTE_KEY, 0) + 1);
                            break;
                    }
                    player.setAttribute(LAST_LOGIN_TASKS_COMPLETED_ATTRIBUTE_KEY, player.getAttribute(LAST_LOGIN_TASKS_COMPLETED_ATTRIBUTE_KEY, 0) + 1);
                    player.setAttribute(task.getKey() + "_status", TaskStatus.COMPLETE);
                    player.sendMessage("You've just completed " + task.getName() + "!");
                    player.sendMessage("Head over to the daily tasks interface to claim your reward.");
                    if (getCompletedTasks(player) == activeTasks.size()) {
                        final int streak = player.getAttribute(STREAK_ATTRIBUTE_KEY, 0) + 1;
                        player.setAttribute(STREAK_ATTRIBUTE_KEY, streak);
                        player.sendMessage("You have completed all the tasks that are available for today!");
                        player.sendMessage("Your current streak is now " + streak);
                    }
                } else if (updatedProgress - progress >= task.getBroadcastAmount()) {
                    player.sendMessage("Your progress for " + task.getName() + " is now " + updatedProgress + " / " + task.getAmount());
                }
            }
        });
    }

    public boolean resetProgress(final Player player) {
        final LocalDateTime lastProgress = player.getAttribute(LAST_DATE_PLAYED_ATTRIBUTE_KEY);
        if (lastProgress == null || lastProgress.getDayOfMonth() != lastUpdate.getDayOfMonth()
            || lastProgress.getMonth() != lastUpdate.getMonth() || lastProgress.getYear() != lastUpdate.getYear()) {
            activeTasks.forEach(t -> {
                if (player.hasAttribute(t.getKey())) {
                    player.removeAttribute(t.getKey());
                }

                if (player.hasAttribute(t.getKey() + "_status")) {
                    player.removeAttribute(t.getKey() + "_status");
                }
            });
            player.setAttribute(LAST_DATE_PLAYED_ATTRIBUTE_KEY, lastUpdate);
            final int lastLoginCompleted = player.getAttribute(LAST_LOGIN_TASKS_COMPLETED_ATTRIBUTE_KEY, 0);
            final int lastLoginTasks = player.getAttribute(LAST_LOGIN_TASKS_TOTAL_ATTRIBUTE_KEY, 0);
            if (lastLoginCompleted < lastLoginTasks) {
                final int protection = player.getAttribute(STREAK_PROTECTION_ATTRIBUTE_KEY, 0);
                if (protection > 0) {
                    player.setAttribute(STREAK_PROTECTION_ATTRIBUTE_KEY, protection - 1);
                } else {
                    player.setAttribute(STREAK_ATTRIBUTE_KEY, 0);
                }
            }
            return true;
        }
        return false;
    }

    public int getTaskProgress(final Player player, final DailyTask task) {
        if (resetProgress(player)) {
            return 0;
        }
        return player.getAttribute(task.getKey(), 0);
    }

    private int getCompletedTasks(final Player player) {
        return (int) activeTasks.stream().filter(t -> {
            final TaskStatus status = player.getAttribute(t.getKey() + "_status", TaskStatus.INCOMPLETE);
            return status == TaskStatus.COMPLETE || status == TaskStatus.CLAIMED;
        }).count();
    }
}
