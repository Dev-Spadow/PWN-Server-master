package com.arlania.world.content.keepsake;

import com.arlania.model.Flag;
import com.arlania.model.Item;
import com.arlania.model.container.impl.Equipment;
import com.arlania.model.definitions.ItemDefinition;
import com.arlania.model.definitions.WeaponAnimations;
import com.arlania.model.definitions.WeaponInterfaces;
import com.arlania.model.input.impl.EnterClanChatToJoin;
import com.arlania.model.input.impl.NamePreset;
import com.arlania.model.input.impl.RenamePreset;
import com.arlania.world.entity.impl.player.Player;
import com.arlania.world.entity.impl.player.PlayerSaving;

//import jdk.nashorn.internal.runtime.Debug;

public class KeepSake {

	/**
	 * Handles opening the keep sake interface
	 * 
	 * @param player
	 * @param update
	 */
	public static void open(Player player, boolean update) {
		int[] item_order = { Equipment.HEAD_SLOT, Equipment.CAPE_SLOT, Equipment.AMULET_SLOT, Equipment.AMMUNITION_SLOT,
				Equipment.WEAPON_SLOT, Equipment.BODY_SLOT, Equipment.SHIELD_SLOT, Equipment.LEG_SLOT,
				Equipment.HANDS_SLOT, Equipment.FEET_SLOT, Equipment.RING_SLOT, Equipment.AURA_SLOT };
		for (int index = 0, child = 2368; index < item_order.length; index++, child += 2) {
			int itemId = player.overrideItems[getSlot(index)];
			if (itemId == 0)
				itemId = -1;
			player.getPacketSender().sendItemOnInterface(index == 11 ? 2398 : child, itemId, 0, 1);
			player.getPacketSender().sendConfig(1111 + index, itemId == -1 ? 0 : 1);
		}

		for (int index = 0; index < player.keepSakePresets.length; index++) {
			KeepSakePreset preset = player.keepSakePresets[index];
			player.getPacketSender().sendString(2392 + index,
					preset == null ? "Preset " + (index + 1) : preset.presetName);
		}

		if (!update)
			player.getPacketSender().sendInterface(2360);
	}

	/**
	 * Handles showing the items the player has keep saked
	 * 
	 * @param player
	 * @param slot
	 */
	public static void displayItems(Player player, int slot) {
		System.out.println("player.selectedSlot: " + player.selectedSlot);
		if (player.selectedSlot != -1)
			clear(player, false);

		player.selectedSlot = slot;

		for (ItemOverwrite item : player.overwritableItems) {
			if (item.slot == player.selectedSlot)
				player.displayedItems.add(item.itemId);
		}

		if (player.displayedItems.size() == 0) {
			player.getPacketSender().sendMessage("@red@You do not have any items to override for this slot.");
			player.getPacketSender().sendMessage("@red@Use a keepsake key on an item to add to your wardrobe.");
			return;
		}

		for (int index = 0; index < player.displayedItems.size(); index++)
			player.getPacketSender().sendItemOnInterface(33455, player.displayedItems.get(index).intValue(), index, 1);

		for (int index = 0; index < player.displayedItems.size(); index++)
			player.getPacketSender().sendString(33456 + index,
					ItemDefinition.forId(player.displayedItems.get(index).intValue()).getName());

		player.getPacketSender().sendChatboxInterface(33400);
	}

	/**
	 * Handles selecting a item to display on the player
	 * 
	 * @param player
	 * @param componentId
	 */
	public static void selectItem(Player player, int componentId) {
		if (componentId >= player.displayedItems.size()) {
			System.out.println("Omegalul");
			clear(player, true);
			return;
		}
		if (player.selectedSlot == Equipment.WEAPON_SLOT) {
			int myWeapon = player.getEquipment().isSlotUsed(Equipment.WEAPON_SLOT)
					? player.getEquipment().get(Equipment.WEAPON_SLOT).getId()
					: -1;
			int overrideId = player.displayedItems.get(componentId).intValue();

			boolean allowedOverride = allowed(player, myWeapon, overrideId);

			if (!allowedOverride) {
				clear(player, true);
				return;
			}
		}

		player.overrideItems[player.selectedSlot] = player.displayedItems.get(componentId).intValue();

		if (player.selectedSlot == Equipment.WEAPON_SLOT) {
			ItemDefinition def = ItemDefinition.forId(player.overrideItems[player.selectedSlot]);
			if (def.isTwoHanded())
				player.overrideItems[Equipment.SHIELD_SLOT] = -1;
			WeaponAnimations.assign(player, new Item(player.overrideItems[player.selectedSlot]));
		} else if (player.selectedSlot == Equipment.SHIELD_SLOT) {
			ItemDefinition def = ItemDefinition.forId(player.overrideItems[Equipment.WEAPON_SLOT]);
			if (def.isTwoHanded())
				player.overrideItems[Equipment.WEAPON_SLOT] = -1;
			WeaponAnimations.assign(player, new Item(player.overrideItems[player.selectedSlot]));
		}
		player.getUpdateFlag().flag(Flag.APPEARANCE);

		open(player, true);
		clear(player, true);
	}

	private static void clear(Player player, boolean close) {
		if (close)
			player.getPacketSender().closeChatInterface();

		// player.getPacketSender().sendItemsOnInterface(33505, new Item[] { });
		System.out.println("player.displayedItems.size(): " + player.displayedItems.size());
		for (int index = 1; index < 20; index++)
			player.getPacketSender().sendItemOnInterface(33455, -1, index, 1);

		for (int index = 0; index < player.displayedItems.size(); index++)
			player.getPacketSender().sendString(33456 + index, "");

		player.selectedSlot = -1;
		player.displayedItems.clear();
	}

	public static boolean allowed(Player player, int myWeapon, int override) {
		ItemDefinition myDef = ItemDefinition.forId(myWeapon);
		ItemDefinition overrideDef = ItemDefinition.forId(override);

		if (myDef == null || overrideDef == null)
			return false;

		// TODO: Find something for this on this server
		// WeaponInterfaces.WeaponInterface my = myDef.getWeaponInterface();
		// WeaponInterfaces.WeaponInterface or = overrideDef.getWeaponInterface();

		boolean myWeaponIsRanged = false;// my.equals(SHORTBOW) || my.equals(LONGBOW) || my.equals(CROSSBOW) ||
											// my.equals(KNIFE) || my.equals(DART) || my.equals(OBBY_RINGS);
		boolean overrideWeaponIsRanged = false; // or.equals(SHORTBOW) || or.equals(LONGBOW) || or.equals(CROSSBOW) ||
												// or.equals(KNIFE) || or.equals(DART) || or.equals(OBBY_RINGS);

		if (myWeaponIsRanged && !overrideWeaponIsRanged) {
			player.getPacketSender()
					.sendMessage("You are only allowed to override a ranged weapon with another ranged weapon.");
			return false;
		}

		if (!myWeaponIsRanged && overrideWeaponIsRanged) {
			player.getPacketSender().sendMessage("You are not allowed to override this weapon with a ranged weapon.");
			return false;
		}

		return true;
	}

	/**
	 * Handles resetting a slot
	 * 
	 * @param player
	 * @param slot
	 */
	public static void resetSlot(Player player, int slot) {
		player.overrideItems[slot] = -1;
		open(player, true);
		if (slot == Equipment.WEAPON_SLOT)
			WeaponAnimations.assign(player, player.getEquipment().get(Equipment.WEAPON_SLOT));

		player.getUpdateFlag().flag(Flag.APPEARANCE);
	}

	/**
	 * Handle using an keep sake key on an item
	 * 
	 * @param player
	 * @param useWith
	 * @param itemUsed
	 */
	public static void useKey(Player player, int useWith, int itemUsed) {
		if (useWith != 298 && itemUsed != 298)
			return;

		int otherItem = useWith == 298 ? itemUsed : useWith;

		ItemDefinition def = ItemDefinition.forId(otherItem);

		if (def == null)
			return;

		if (def.getEquipmentSlot() == -1) {
			player.getPacketSender().sendMessage("Nothing interesting happens.");
			return;
		}

		showItemOnInterface2Lines(player, "@red@Keepsake the " + def.getName() + "?",
				"The item you wish to convert will be consumed!", "A keepsake item cannot be retrieved!", otherItem,
				250, offset(otherItem));
		player.setActionInterface("keepsakeItem");
		player.setTempItem(otherItem);
	}

	private static int[] offset(int itemId) {
		int x = 0, y = 0;
		switch (itemId) {
		case 1057:
		case 1055:
		case 1053:
			y = 17;
			break;
		case 1050:
		case 1048:
		case 1046:
		case 1044:
		case 1042:
		case 1040:
		case 1038:
			y = 30;
			break;
		}
		return new int[] { x, y };
	}

	/**
	 * Handles the keepsaking of an item
	 * 
	 * @param player
	 */
	public static void keepsakeItem(Player player) {
		ItemDefinition def = ItemDefinition.forId(player.getTempItem());

		if (def == null || def.getName().equalsIgnoreCase("none")) {
			player.getPA().sendMessage("Something went wrong, let staff know! Err code: 98114");
			System.out.println("Error with cosmetic override.. ");
			return;
		}

		player.overwritableItems.add(new ItemOverwrite(def.getEquipmentSlot(), def.getId()));

		player.getInventory().delete(def.getId());
		player.getInventory().delete(298);

		player.getPacketSender().sendMessage("@gre@You have unlocked the " + def.getName() + " as an override!");

		player.setTempItem(-1);
		player.getPacketSender().closeAllWindows();
		PlayerSaving.save(player);
	}

	/**
	 * Handles loading a preset
	 * 
	 * @param player
	 * @param index
	 */
	public static void loadPreset(Player player, int index) {
		if (player.keepSakePresets[index] == null) {
			player.getPacketSender().sendMessage("@red@No preset was found!");
			return;
		}

		KeepSakePreset preset = player.keepSakePresets[index];

		player.overrideItems = preset.overrideItems;
		player.getPacketSender().sendMessage("@blu@Successfully loaded the '" + preset.presetName + "' preset.");
		open(player, true);
		player.getUpdateFlag().flag(Flag.APPEARANCE);
	}

	/**
	 * Handles saving a preset
	 * 
	 * @param player
	 * @param index
	 * @param skipSetup
	 * @param name
	 */
	public static void savePreset(Player player, int index, boolean skipSetup, String name) {
		if (!hasOverrideItems(player)) {
			player.getPacketSender().sendMessage("@red@You have no overrides active to save.");
			return;
		}

		if (player.keepSakePresets[index] == null && !skipSetup) {
			player.setTempItem(index);
			// TODO: Find out how this search handles input
			// player.client.sendInputDialog(3, "Please fill in the name of the preset:",
			// "setupCosmeticPreset");
			player.setInputHandling(new NamePreset());
			player.getPacketSender().sendEnterInputPrompt("Please fill in the name of the preset:");
			return;
		}

		if (player.keepSakePresets[index] != null && name.equalsIgnoreCase(""))
			name = player.keepSakePresets[index].presetName;

		int[] items = new int[player.overrideItems.length];
		for (int i = 0; i < items.length; i++)
			items[i] = new Integer(player.overrideItems[i]);

		player.setTempItem(-1);
		KeepSakePreset preset = new KeepSakePreset(name, items);
		player.getPacketSender().sendString(2392 + index, name);

		player.keepSakePresets[index] = preset;
		player.getPacketSender().sendMessage("@blu@Successfully saved the '" + name + "' preset.");
	}

	/**
	 * Handles renaming preset
	 * 
	 * @param player
	 * @param index
	 * @param setup
	 * @param name
	 */
	public static void renamePreset(Player player, int index, boolean setup, String name) {
		if (player.keepSakePresets[index] == null) {
			player.getPacketSender().sendMessage("@red@No preset was found!");
			return;
		}

		if (setup) {
			player.setTempItem(index);
			player.setInputHandling(new RenamePreset());
			player.getPacketSender().sendEnterInputPrompt("Please fill in the new name of the preset:");
		} else {
			KeepSakePreset preset = player.keepSakePresets[index];
			preset.presetName = name;

			player.setTempItem(-1);
			player.getPacketSender().sendMessage("@blu@Successfully renamed the '" + name + "' preset.");
			player.keepSakePresets[index] = preset;
			player.getPacketSender().sendString(2392 + index, name);
			PlayerSaving.save(player);
		}
	}

	/**
	 * Checks if the player has any items overrides active
	 * 
	 * @param player
	 * @return
	 */
	public static boolean hasOverrideItems(Player player) {
		for (int id : player.overrideItems)
			if (id > 0)
				return true;
		return false;
	}

	/**
	 * The Slot
	 * 
	 * @param index
	 * @return
	 */
	public static int getSlot(int index) {
		switch (index) {
		case 0:
			return Equipment.HEAD_SLOT;
		case 1:
			return Equipment.CAPE_SLOT;
		case 2:
			return Equipment.AMULET_SLOT;
		case 3:
			return Equipment.AMMUNITION_SLOT;
		case 4:
			return Equipment.WEAPON_SLOT;
		case 5:
			return Equipment.BODY_SLOT;
		case 6:
			return Equipment.SHIELD_SLOT;
		case 7:
			return Equipment.LEG_SLOT;
		case 8:
			return Equipment.HANDS_SLOT;
		case 9:
			return Equipment.FEET_SLOT;
		case 10:
			return Equipment.RING_SLOT;
		case 11:
			return Equipment.AURA_SLOT;
		}
		return index;
	}

	public static void handleButtons(Player player, int buttonId) {
		//System.out.println("KeepSake: Button: " + buttonId + ", Action: " + player.getActionInterface());

		if (buttonId >= -32131 && buttonId <= -31832) {
			int index = buttonId - -32131;
			System.out.println("index: " + index);
			selectItem(player, index);
			return;
		}

		switch (buttonId) {
		case 2362:
			player.getPacketSender().closeAllWindows();
			break;
		case 2461:
			if (player.getActionInterface().equalsIgnoreCase("keepsakeItem_1"))
				keepsakeItem(player);
			break;
		case 2462:
			if (player.getActionInterface().equalsIgnoreCase("keepsakeItem_1"))
				player.getPacketSender().closeAllWindows();
			break;
		}
	}

	public static void showItemOnInterface2Lines(Player player, String top, String s, String s2, int itemId, int zoom,
			int[] adjust) {
		player.getPacketSender().sendInterfaceModel(4888, itemId, zoom);
		player.getPacketSender().sendString(4889, top);
		player.getPacketSender().sendString(4890, s);
		player.getPacketSender().sendString(4891, s2);
		player.getPacketSender().sendChatboxInterface(4887);
	}

	public static void sendOptionsTitle(Player player, String title, String s, String s2) {
		player.getPacketSender().sendString(2460, title);
		player.getPacketSender().sendString(2461, s);
		player.getPacketSender().sendString(2462, s2);
		player.getPacketSender().sendChatboxInterface(2459);
	}
}
