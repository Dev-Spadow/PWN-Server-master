package com.arlania.world.content.gim;

import com.arlania.GameSettings;
import com.arlania.model.Flag;
import com.arlania.model.Item;
import com.arlania.model.definitions.ItemDefinition;
import com.arlania.world.World;
import com.arlania.world.content.gim.container.Container;
import com.arlania.world.entity.impl.player.Player;
import com.arlania.world.entity.impl.player.PlayerSaving;

public class SharedStorage {

	/**
	 * Max storage size
	 */
	public transient static final int SIZE = 100;
	
	/**
	 * 
	 * @param player
	 */
	public static void open(Player player) {
		//Gets the group
		Group group = Group.forOwner(player.groupOwner);
		if(group == null) {
            player.sendMessage("Something went from with your group, please contact a admin.");
		    return;
        }
		//Handles the shifting of empty items (when opening the bank)
		//Loops though all items
		for(Item item : group.storage.toArray()) {
			//Skip if item is null
			if(item == null) continue;
			
			//If the id is -1
			if(item.getId() == -1)
				//Remove item
				group.storage.remove(item);
		}
		//Shift the tabs
		group.storage.shift();
		
		//Updates the inventory and bank container
		update(player);
		
		//Bank spots
		player.getPacketSender().sendString(15266, "" + group.storage.getSpaces());

		//Sends the interface and inventory
		player.getPacketSender().sendInterfaceSet(15264, 995);
		
		//Sets it that you are in the storage
		player.setStorage(true);
	}
	
	/**
	 * Updates the bank and inventory container
	 * @param player
	 */
	public static void update(Player player) {
		//Gets the group
		Group group = Group.forOwner(player.groupOwner);
		//Checks if the player is in a group based on the owner or not
        if(group == null) {
            player.sendMessage("Something went from with your group, please contact a admin.");
            return;
        }
		//Inventory and storage container
		Item[] inventory = player.getInventory().toSafeArray(), storage = group.storage.toArray();
		//Updates inventory
		player.getPacketSender().sendItemContainer(inventory, 6640);
		//Updates storage
		player.getPacketSender().sendItemContainer(storage, 15265);
		//Updates the storage for members when they are inside it aswell
		for(String name : group.members) {
			Player member = World.getPlayerByName(name);
			if(member == null || !member.inStorage()) continue;
			member.getPacketSender().sendItemContainer(storage, 15265);
		}
	}

	public static void deposit(Player player, int slot, int id, int amount, boolean inventory, boolean update) {
		if(id == -1) return;
		//Gets the group
		Group group = Group.forOwner(player.groupOwner);
		//Checks if the player is in a group based on the owner or not
        if(group == null) {
            player.sendMessage("Something went from with your group, please contact a admin.");
            return;
        }
		//Checks if your actually banking or deposit box
		if(!player.inStorage()) {
			player.getPacketSender().sendMessage("Please open up the storage properly.");
			player.getPacketSender().closeAllWindows();
			return;
		}
		//Gets the container
		Container container = group.storage;
		
		if(!container.hasRoomFor(new Item(id))) {
			player.getPacketSender().sendMessage("You don't have enough space in your storage.");
			return;
		}

		Item inventoryItem = inventory ? player.getInventory().get(slot) : player.getEquipment().get(slot);
		
		if (inventoryItem == null || inventoryItem.getId() != id) {
			return;
		}
		
		if(!isTradeable(inventoryItem.getId())) {
			player.getPacketSender().sendMessage("You cannot put untradeable items into the storage.");
			return;
		}
		
		int inventoryAmount = inventory ? player.getInventory().getAmount(id) : player.getEquipment().getAmount(id);
		boolean isNote = ItemDefinition.forId(inventoryItem.getId()).isNoted();
		
		if (amount > inventoryAmount)
			amount = inventoryAmount;
		
		if(inventory)
			player.getInventory().delete(new Item(id, amount));
		
		int transferId = isNote ? Item.getNoted(inventoryItem.getId()) : id;
		int bankCount = container.getCount(transferId);
		
		System.out.println("transferId: " + transferId);
		
		if (bankCount == 0)
			container.add(new Item(transferId, amount));
		else
			container.set(container.getSlotById(transferId), new Item(transferId, bankCount + amount));
		
		//player.client.sendString("" + container.getSpaces(), 642, 3);
		player.getPacketSender().sendString(15266, "" + container.getSpaces());
		
		if(update) {
			update(player);
			player.getInventory().refreshItems();
		}
		group.save();
		PlayerSaving.save(player);
	}

	public static void withdraw(Player player, int slot, int id, int amount, boolean placeholder) {
		//Gets the group
		Group group = Group.forOwner(player.groupOwner);
		//Checks if the player is in a group based on the owner or not
        if(group == null) {
            player.sendMessage("Something went from with your group, please contact a admin.");
            return;
        }
		//Gets the container
		Container container = group.storage;
		//Checks if your actually banking or deposit box
		if(!player.inStorage()) {
			player.getPacketSender().sendMessage("Please open up the storage properly.");
			player.getPacketSender().closeAllWindows();
			return;
		}
		//Checks if the container contains the item
		if(!container.contains(id))
			return;
		
		boolean noted = ItemDefinition.forId(id + 1).isNoted();
		boolean stackable = new Item(id).getDefinition().isStackable();
		int freeInventorySlot = player.getInventory().getFreeSlots();
		boolean hasItem = player.getInventory().contains(player.withdrawAsNote() && !stackable ? id + 1 : id);
		
		if (freeInventorySlot < 1 && !hasItem && (!stackable || !noted)) {
			player.getPacketSender().sendMessage("Not enough space in your inventory.");
			return;
		}
		
		int inBankAmount = container.getCount(id);
		Item bank = new Item(id, amount);
		
		if(inBankAmount == 0 || id == 11169 && amount == 1) { //Clear placeholder or one bank filler
			container.set(slot, new Item(-1));
			//player.client.sendString("" + container.getSpaces(), 642, 3);
			player.getPacketSender().sendString(15266, "" + container.getSpaces());
			update(player);
			return;
		}
		
		if (amount < 1 || id < 0)
			return;

		if(amount > player.getInventory().getFreeSlots() && !bank.getDefinition().isStackable() && !player.withdrawAsNote()) {
			amount = player.getInventory().getFreeSlots();
			bank = new Item(bank.getId(), amount);
		}
		
		if (amount > inBankAmount) {
			amount = inBankAmount;
			bank = new Item(bank.getId(), amount);
		}
		
		boolean withdrawAsnote = player.withdrawAsNote();

		if (withdrawAsnote && !noted) {
			player.getPacketSender().sendMessage("This item cannot be withdrawn as a note.");
			withdrawAsnote = false;
		}
		
		//if(inBankAmount == amount && !player.usePlaceholders && player.usePlaceholders)
		if(inBankAmount == amount && (!player.isPlaceholders() && !placeholder) || bank.getDefinition().getName().startsWith("Clue scroll") && player.isPlaceholders())
			container.set(slot, new Item(-1));
		else
			container.remove(bank, slot, bank.getDefinition().getName().startsWith("Clue scroll") ? false : placeholder ? true : player.isPlaceholders());
		
		
		if(withdrawAsnote)
			bank = new Item(Item.getNoted(bank.getId()), amount);
		
		player.getInventory().add(bank);
		
		//Bank spots
		player.getPacketSender().sendString(15266, "" + container.getSpaces());
		
		update(player);
		player.getInventory().refreshItems();
		group.save();
		PlayerSaving.save(player);
	}
	
	public static void depositInventory(Player player) {
		//Gets the group
		Group group = Group.forOwner(player.groupOwner);
		//Checks if the player is in a group based on the owner or not
        if(group == null) {
            player.sendMessage("Something went from with your group, please contact a admin.");
            return;
        }
		//Gets the container
		Container container = group.storage;
		
		for(int slot = 0; slot < 28; slot++) {
			if(container.freeSlots() == 0) {
				player.getPacketSender().sendMessage("You don't have enough space in your bank account.");
				break;
			}
			Item item = player.getInventory().get(slot);
			if(item == null || !isTradeable(item.getId()))
				continue;
			
			deposit(player, slot, item.getId(), item.getAmount(), true, false);
		}
		update(player);
		//Inventory.refresh(player);
		player.getInventory().refreshItems();
		player.getPacketSender().sendItemContainer(player.getInventory().toSafeArray(), 6640);
	}

	public static void depositEquipment(Player player) {
		//Gets the group
		Group group = Group.forOwner(player.groupOwner);
		//Checks if the player is in a group based on the owner or not
        if(group == null) {
            player.sendMessage("Something went from with your group, please contact a admin.");
            return;
        }
		//Gets the container
		Container container = group.storage;
		
		for(int equipSlot = 0; equipSlot < 14; equipSlot++) {
			
			if(container.freeSlots() == 0) {
				player.getPacketSender().sendMessage("You don't have enough space in your bank account.");
				break;
			}
			
			Item item = player.getEquipment().get(equipSlot);
			
			if(item == null || !isTradeable(item.getId()))
				continue;
				
			deposit(player, equipSlot, item.getId(), item.getAmount(), false, false);
			player.getEquipment().set(equipSlot, new Item(-1));
		}
		player.getUpdateFlag().flag(Flag.APPEARANCE);
		update(player);
		player.getEquipment().refreshItems();
	}
	
	public static boolean isTradeable(int itemId) {
		for (int i : GameSettings.UNTRADEABLE_ITEMS)
			if (i == itemId)
				return false;
		return true;
	}
	
	public static void close(Player player) {
		player.getPacketSender().closeChatInterface();
		player.getInventory().refreshItems();
		player.setStorage(false);
		player.getPacketSender().closeAllWindows();
	}

	public static void swaporinsert(Player player, int from, int to) {		
		//Checks if the to or from is lower then 0
		if(to < 0 || from < 0){
			//Updates the bank
			update(player);
			return;
		}
		Group group = Group.forOwner(player.groupOwner);

        if(group == null) {
            player.sendMessage("Something went from with your group, please contact a admin.");
            return;
        }

		//Current container the item is in
		Container container = group.storage;
		
		//Checks if your in insert mode
		if(player.swapMode()) {
			
			//Checks if the new container is null if so use old one else new
			Container grab = container;
			
			//Grabs the item your moving
			Item from_item = container.get(from);
			//Grabs the item your inserting
			Item to_item = grab.get(to);
			
			//Sets the old item in the container
			container.set(from, to_item);
			//Sets the item in the new container
			grab.set(to, from_item);
		}
		//Swap mode
		else {
			//Grabs the item your moving
			Item from_item = container.get(from);
			//Checks if the new container is null if so use old one else new
			Container grab = container;
			//Checks if the container has toom for it, or if the old container is where your moving too
			if(grab.freeSlots() == 0 && container == grab) {
				return;
			}
			
			//Removes the item from the old container
			container.remove(from_item);

			//Grabs all items in the new container
			Item[] items = grab.getItems();
			//Moves the items to the right
			if(container != grab || from > to) {
				for(int i = items.length - 2; i >= to + (from > to ? 0 : 1); i--) {
					items[i+1] = items[i];
				}
				
				items[to + (from > to ? 0 : 1)] = from_item;
				container.shift();
			}
			//Moves the items to the left
			else {
				for(int i = from + 1; i <= to; i++) {
					items[i-1] = items[i];
				}
				items[to] = from_item;
			}
			//Fire changes
			if (grab.isFiringEvents())
				grab.fireItemsChanged();
		}
		//Updates the whole bank interface
		update(player);
	}
}
