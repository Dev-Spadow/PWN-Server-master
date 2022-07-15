package com.arlania.world.content.bis;

import com.arlania.model.definitions.ItemDefinition;
import com.arlania.model.definitions.WeaponInterfaces;
import com.arlania.world.entity.impl.player.Player;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

public class BestInSlotInterface {

    private final Player player;

    public BestInSlotInterface(Player player) {
        this.player = player;
    }

    private final int STARTING_POINT = 27245;

    private int classIndex = 0;
    private int categoryIndex = 0;
    private int viewedIndex = 0;

    private final int[] categoryItemIds = {1163, 6570, 1127, 1201, 1079, 1333, 4131, 1725, 7462, 2570};

    private static Map<ItemDefinition.EquipmentType, List<ItemDefinition>> items;

    //private static final Map<ItemDefinition.EquipmentType, List<ItemDefinition>> items = new HashMap<>();

    public static void loadAllItems() { // alright that should solve it //ty mate <3
        items = Arrays.stream(ItemDefinition
            .getDefinitions())
            .filter(Objects::nonNull)
            .collect(groupingBy(ItemDefinition::getEquipmentType));
    }

    private List<ItemDefinition> loadedDefinitions;
    private ItemDefinition selectedDefinition;

    private final int[] ignoredIds = {21898, 21907, 21903, 21904, 21908, 21883, 21913, 16554};

    public void open() {
        sendInterfaceData();
        player.getPacketSender().sendInterface(STARTING_POINT);
        loadItems();
    }

    public String getSpeedText() {
        WeaponInterfaces.WeaponInterface weaponInterface = WeaponInterfaces.getById(selectedDefinition
            .getId());
        int speed = weaponInterface != null ? weaponInterface.getSpeed() : -1;
        String text = "NONE";
        switch (speed) {
            case 0:
                text = "Fastest";
                break;
            case 1:
                text = "2nd Fastest";
                break;
            case 2:
                text = "3rd Fastest";
                break;
            case 3:
                text = "4th Fastest";
                break;
            case 4:
                text = "5th Fastest";
                break;
            case 5:
                text = "6th Fastest";
                break;
            case 6:
                text = "7th Fastest";
                break;
        }
        return text;
    }


    private void sendInterfaceData() {
        player.getPacketSender().sendInterfaceIntArray(STARTING_POINT + 12, categoryItemIds);
    }

    private void loadItems() {
        ItemDefinition.EquipmentType type = getEquipmentType();
        List<ItemDefinition> data = items.get(type)
            .stream()
            .filter(this::isValid)
            .filter(Predicate.not(ItemDefinition::isNoted))
            .sorted(Comparator.<ItemDefinition>comparingInt(ItemDefinition::getTotalBonus)
                .reversed()).limit(40).collect(toList());
        int[] ids = data.stream()
            .mapToInt(ItemDefinition::getId)
            .toArray();
        loadedDefinitions = data;
        List<String> strings = new ArrayList<>();
        IntStream.range(0, data.size()).forEach(i -> {
            strings.add(data.get(i).getName());
        });
        int textIndex = STARTING_POINT + 164;

        for (int i = 0; i < textIndex + 150; i++) {
            player.getPacketSender().sendString(textIndex, "");
        }

        player.getPacketSender().sendInterfaceIntArray(STARTING_POINT + 314, ids);

        for (int i = 0; i < strings.size(); i++) {
            player.getPacketSender().sendString(textIndex + i, strings.get(i));
        }

        viewedIndex = 0;
        player.getPacketSender().sendToggle(3200, 0);
        handleItemSelection();

    }

    private boolean isValid(ItemDefinition definition) {
        if (Arrays.stream(ignoredIds).anyMatch(i -> i == definition.getId())) {
            return false;
        }
        if (definition.allZero()) {
            return false;
        }

        if (classIndex == 0 && definition.getBonus()[14] > 0) {
            return true;
        } else if (classIndex == 1 && (definition.getBonus()[4] > 0 || definition.getBonus()[15] > 0)) {
            return true;
        } else if (classIndex == 2 && definition.getBonus()[3] > 0) {
            return true;
        }

        return false;
    }

    private void handleItemSelection() {
        if (loadedDefinitions.size() > viewedIndex) {
            selectedDefinition = loadedDefinitions.get(viewedIndex);
            sendStats();
        }
    }

    private final String[] BONUS_STRINGS = {"Attack bonus", "Stab: +0", "Slash: +0", "Crush: +0", "Magic: +0", "Range: +0", "Defence bonus", "Stab: +0", "Slash: +0", "Crush: +0", "Magic: +0", "Range: +0", "Other bonus", "Melee strength: +0", "Ranged strength: +0", "Magic damage: +0.0%", "Prayer: +0"};

    private void sendStats() {
        int start = STARTING_POINT + 315;
        int attackOffset = 27561;
        int defenceOffset = 27567;
        int otherOffset = 27573;
        int index;
        String[] mainNames = {"Stab: ", "Slash: ", "Crush: ", "Magic: ", "Range: "};
        String[] otherNames = {"Melee strength: ", "Ranged strength: ", "Prayer: ", "Magic damage: "};

        Map<Integer, String> stringMap = new HashMap<>();
        for (int i = 0; i < BONUS_STRINGS.length; i++) {
            player.getPacketSender().sendString(start + i, BONUS_STRINGS[i]);
        }

        for (index = 0; index < 5; index++) {
            stringMap.put(attackOffset + index, mainNames[index] + selectedDefinition.getBonus()[index]);
            stringMap.put(defenceOffset + index, mainNames[index] + selectedDefinition.getBonus()[5 + index]);
            if (index < otherNames.length) {
                stringMap.put(otherOffset + index, otherNames[index] + selectedDefinition.getBonus()[14 + index]);
            }
        }

        stringMap.put(otherOffset + 4, "@yel@Attack Speed: " + getSpeedText());

        stringMap.forEach((id, string) -> {
            player.getPacketSender().sendString(id, string);
        });
    }

    private ItemDefinition.EquipmentType getEquipmentType() {
        ItemDefinition.EquipmentType type = ItemDefinition.EquipmentType.FULL_HELMET;
        switch (categoryIndex) {
            case 1:
                type = ItemDefinition.EquipmentType.PLATEBODY;
                break;

            case 2:
                type = ItemDefinition.EquipmentType.LEGS;
                break;

            case 3:
                type = ItemDefinition.EquipmentType.BOOTS;
                break;

            case 4:
                type = ItemDefinition.EquipmentType.GLOVES;
                break;

            case 5:
                type = ItemDefinition.EquipmentType.CAPE;
                break;

            case 6:
                type = ItemDefinition.EquipmentType.SHIELD;
                break;

            case 7:
                type = ItemDefinition.EquipmentType.WEAPON;
                break;

            case 8:
                type = ItemDefinition.EquipmentType.AMULET;
                break;

            case 9:
                type = ItemDefinition.EquipmentType.RING;
                break;

        }
        return type;
    }

    public boolean handleButtonClick(int buttonId) {
        if (buttonId < 27247 || buttonId > 27584) {
            return false;
        }

        if (buttonId >= 27582) {
            int oldClassIndex = classIndex;
            classIndex = buttonId - 27582;
            if (classIndex != oldClassIndex) {
                loadItems();
            }
            return true;
        }

        if (buttonId <= 27256) {
            int oldCategoryIndex = categoryIndex;
            categoryIndex = buttonId - 27247;
            if (categoryIndex != oldCategoryIndex) {
                loadItems();
            }
            return true;
        }

        if (buttonId >= 27259 && buttonId <= 27408) {
            int oldViewedIndex = viewedIndex;
            viewedIndex = buttonId - 27259;
            if (viewedIndex != oldViewedIndex) {
                handleItemSelection();
            }
        }

        return true;
    }


}
