package com.arlania.world.content;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;

import com.arlania.GameServer;
import com.arlania.model.Item;
import com.arlania.model.container.impl.Bank;
import com.arlania.model.definitions.ItemDefinition;
import com.arlania.util.Misc;
import com.arlania.world.entity.impl.player.Player;
import com.arlania.world.entity.impl.player.PlayerSaving;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class LootingBag {

		public static void displayLootingBagInterface(Player player) {
			for (int index = 0; index < player.lootingBagStorageItemId.length; index++) {
				int item = player.lootingBagStorageItemId[index] == 0 ? -1 : player.lootingBagStorageItemId[index];
				//system.out.println("item "+item+" index "+index+" am "+player.lootingBagStorageItemAmount[index]);
				player.getPA().sendItemOnInterface(29706, item, index, player.lootingBagStorageItemAmount[index]);
			}
			updateLootingBagValueText(player);
			player.getPA().sendTabInterface(3, 26900);
		}
		
		public static void displayLootingBagInterfaceBank(Player player) {
			for (int index = 0; index < player.lootingBagStorageItemId.length; index++) {
				int item = player.lootingBagStorageItemId[index] == 0 ? -1 : player.lootingBagStorageItemId[index];
				//system.out.println("item "+item+" index "+index+" am "+player.lootingBagStorageItemAmount[index]);
				player.getPA().sendItemOnInterface(29806, item, index, player.lootingBagStorageItemAmount[index]);
			}
			updateLootingBagValueTextBank(player);
			player.getPA().sendInterfaceSet(5292, 29800);
		}

		/**
		 * Update the value text of the looting bag contents.
		 */
		private static void updateLootingBagValueText(Player player) {
			int value = 0;

			for (int index = 0; index < player.lootingBagStorageItemId.length; index++) {
				int item = player.lootingBagStorageItemId[index];
				int amount = player.lootingBagStorageItemAmount[index];
				if (item <= 0) {
					continue;
				}
				value += ItemDefinition.getDefinitions()[item].getValue()* amount/*ServerConstants.getItemValue(item) * amount*/;
			}

			player.getPA().sendFrame126("Value: " + Misc.formatNumber(value) + " Taxbags", 29707);

		}
		
		private static void updateLootingBagValueTextBank(Player player) {
			int value = 0;

			for (int index = 0; index < player.lootingBagStorageItemId.length; index++) {
				int item = player.lootingBagStorageItemId[index];
				int amount = player.lootingBagStorageItemAmount[index];
				if (item <= 0) {
					continue;
				}
				value += ItemDefinition.getDefinitions()[item].getValue()* amount/*ServerConstants.getItemValue(item) * amount*/;
			}

			player.getPA().sendFrame126("Value: " + Misc.formatNumber(value) + " Taxbags", 29807);

		}

		public static void closeLootingBagInterface(Player player) {
			//player.getPA().setSidebarInterface(3, 3213);
			player.getPacketSender().sendTabInterface(3, 3213);
		}

		public static void lootingBagDeath(Player victim, Player attacker, boolean killerExists) {
			for (int index = 0; index < victim.lootingBagStorageItemId.length; index++) {
				int item = victim.lootingBagStorageItemId[index];
				int amount = victim.lootingBagStorageItemAmount[index];
				if (item <= 0) {
					continue;
				}

				victim.lootingBagStorageItemId[index] = 0;
				victim.lootingBagStorageItemAmount[index] = 0;
				//Death.victimLoot(victim, attacker, item, amount, false, killerExists); todo
			}
		}

		public static void withdrawLootingBag(Player player) {
			if (player.getTrading().inTrade() || player.getDueling().inDuelScreen) {
				return;
			}
			for (int index = 0; index < player.lootingBagStorageItemId.length; index++) {
				int item = player.lootingBagStorageItemId[index];
				int amount = player.lootingBagStorageItemAmount[index];
				boolean give = false;
				if (ItemDefinition.getDefinitions()[item].stackable && player.getInventory().hasItem(new Item(item, 1))) {
					give = true;
				} else if (player.getInventory().getFreeSlots() > 0) {
					give = true;
				}
				if (give) {
					if (player.getInventory().addItem(item, amount)) {
						player.lootingBagStorageItemId[index] = 0;
						player.lootingBagStorageItemAmount[index] = 0;
					}
				}
			}
		}
		
		public static void withdrawLootingBagBank(Player player) {
			if (player.getTrading().inTrade() || player.getDueling().inDuelScreen) {
				return;
			}
			for (int index = 0; index < player.lootingBagStorageItemId.length; index++) {
				int item = player.lootingBagStorageItemId[index];
				int amount = player.lootingBagStorageItemAmount[index];
				if(item > 0) {
					boolean give = false;
					if (ItemDefinition.getDefinitions()[item].stackable && player.getInventory().hasItem(new Item(item, 1))) {
						give = true;
					} else if (player.getInventory().getFreeSlots() > 0) {
						give = true;
					}
					if (give) {
						
						if (player.getBank(player.getCurrentBankTab()).getFreeSlots() <= 0
								&& !(player.getBank(player.getCurrentBankTab()).contains(item) && new Item(item, amount).getDefinition().isStackable())) {
							player.getPacketSender().sendMessage("Bank full.");
							return;
						}
	
						Item toBank = new Item(ItemDefinition.forId(item).isNoted() ? (item - 1) : item,
								new Item(item, amount).getAmount());
						int tab = Bank.getTabForItem(player, toBank.getId());
						player.setCurrentBankTab(tab);
						int bankAmt = player.getBank(tab).getAmount(toBank.getId());
						if (bankAmt + toBank.getAmount() >= Integer.MAX_VALUE || bankAmt + toBank.getAmount() <= 0) {
							player.getPacketSender().sendMessage("Your bank cannot hold that amount of that item.");
							continue;
						}
						player.getBank(tab).add(toBank.copy(), false);
							player.lootingBagStorageItemId[index] = 0;
							player.lootingBagStorageItemAmount[index] = 0;
					}
				}
			}
			player.getBank(player.getCurrentBankTab()).sortItems().refreshItems();
			displayLootingBagInterfaceBank(player);
		}

		public static boolean useWithLootingBag(Player player, int itemUsed, int usedWith, int itemUsedSlot, int usedWithSlot) {
			boolean hasBag = false;
			if (itemUsed == 6500 || usedWith == 6500) {
				hasBag = true;
			}
			if (!hasBag) {
				return false;
			}
			/*if (!Area.inDangerousPvpArea(player)) {
				player.getPA().sendMessage("You can't put items in the bag unless you're in the Wilderness");
				return true;
			}*/
			
			int storeItem = 0;
			int storeItemAmount = 0;
			int storeItemSlot = 0;
			if (itemUsed != 6500) {
				storeItem = itemUsed;
				storeItemSlot = itemUsedSlot;
				storeItemAmount = getItemAmount(player, storeItem, itemUsedSlot);
			}
			if (usedWith != 6500) {
				storeItem = usedWith;
				storeItemSlot = usedWithSlot;
				storeItemAmount = getItemAmount(player, storeItem, usedWithSlot);
			}
			if (storeItem == 6500) {
				player.getPA().sendMessage("Bag'ception is not permitted.");
				return true;
			}
			System.out.println("1");
			boolean itemStoredCompleted = false;
			if (player.getInventory().getItems()[itemUsedSlot].getDefinition().isStackable()) {
				System.out.println("9999");
				for (int index = 0; index < player.lootingBagStorageItemId.length; index++) {
					if (player.lootingBagStorageItemId[index] == storeItem) {
						int maximumAmount = Integer.MAX_VALUE - player.lootingBagStorageItemAmount[index];
						if (storeItemAmount > maximumAmount) {
							player.sendMessage("Maximum item stack reached, cannot add: " + ItemDefinition.forId(player.lootingBagStorageItemId[index]).getName() + ".");
							return false;
						}
						System.out.println("2");
						player.lootingBagStorageItemAmount[index] += storeItemAmount;
						player.getInventory().delete(player, storeItem, storeItemSlot, storeItemAmount);
						itemStoredCompleted = true;
						break;
					}
				}
			}
			if (!itemStoredCompleted) {
				System.out.println("amm "+storeItemAmount);
				for (int index = 0; index < player.lootingBagStorageItemId.length; index++) {
					if (player.lootingBagStorageItemId[index] == 0) {
						player.lootingBagStorageItemId[index] = storeItem;
						player.lootingBagStorageItemAmount[index] = storeItemAmount;
						player.getInventory().delete(player, storeItem, storeItemSlot, storeItemAmount);
						System.out.println("4");
						break;
					}
				}
			}
			System.out.println("5");
			return true;
		}
		
		public static int getItemAmount(Player player, int itemId, int slot) {
			int itemCount = 0;
			if ((player.getInventory().getItems()[slot].getId()) == itemId) {
				itemCount += player.getInventory().getItems()[slot].getAmount();
			}
			return itemCount;
		}
	

	/*private Path filePath;
	private File file;

	public static int BAG_ID = 19704;
	
	private Player player;
	private int[] items = new int[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 };

	public LootingBag(Player player) {
		this.player = player;
		filePath = Paths.get("./data/ironman/lootingbags/" + player.getUsername() + ".json");
		file = filePath.toFile();

		// Attempt to make the player save directory if it doesn't
		// exist.
		if (!file.getAbsoluteFile().exists()) {
			try {
				file.getParentFile().mkdirs();
				initialize();
			} catch (SecurityException e) {
				System.out.println("Unable to create directory for player data!");
			}
		} else {
			load();
		}
	}

	private void initialize() {
		file.getParentFile().setWritable(true);
		int[] temp = new int[] { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 };

		try (FileWriter writer = new FileWriter(file)) {
			Gson builder = new GsonBuilder().setPrettyPrinting().create();
			JsonObject object = new JsonObject();
			object.add("items", builder.toJsonTree(temp));
			writer.write(builder.toJson(object));
			writer.close();
			System.out.println("Created ironman looting bag file: " + file.getPath());
		} catch (Exception e) {
			// An error happened while saving.
			GameServer.getLogger().log(Level.WARNING, "An error has occured while creating ironman looting bag file!",
					e);
		}
	}

	public void load() {
		try (FileReader fileReader = new FileReader(file)) {
			System.out.println("Now attempting to read file from Item Enchantments");
			System.out.println(file.getAbsolutePath());
			JsonParser fileParser = new JsonParser();
			Gson builder = new GsonBuilder().create();
			JsonObject reader = (JsonObject) fileParser.parse(fileReader);

			if (reader.has("items")) {
				items = builder.fromJson(reader.get("items").getAsJsonArray(), int[].class);
			}
			fileReader.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public int checkForSpace() {
		player.sendMessage("@red@Temporarily disabled.");
		return -1;
		/*
		int freeSlot = -1;
		for (int i = 0; i < 10; i++) {
			if(items[i] == -1) {
				player.getPacketSender().sendMessage("f: " + freeSlot);
				freeSlot = i;
				return freeSlot;
			}
		}
		player.getPacketSender().sendMessage("a: " + freeSlot);
		return freeSlot;
	}*/

	/*public void addItemToBag(int itemId, int slot) {
		player.sendMessage("@red@Temporarily disabled.");
		return;
		/*
		player.getInventory().delete(itemId, 1);
		items[slot] = itemId;
		player.sendMessage("Added x1" + ItemDefinition.forId(itemId).getName() + " to looting bag.");
		PlayerLogs.log(player.getUsername(), "Added x1 " + ItemDefinition.forId(itemId).getName() + " to looting bag.");
		save();
		PlayerSaving.save(player);
	}*/

	/*public void save() {
		file.getParentFile().setWritable(true);

		// Attempt to make the player save directory if it doesn't
		// exist.
		if (!file.getAbsoluteFile().exists()) {
			try {
				try {
					file.getParentFile().createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (SecurityException e) {
				System.out.println("Unable to create directory for looting bag!");
			}
		}

		JsonObject reader = null;

		try (FileReader fileReader = new FileReader(file)) {

			System.out.println("Now attempting to read file from " + file.toPath());
			JsonElement fileParser = new Gson().fromJson(fileReader, JsonElement.class);
			Gson builder = new GsonBuilder().create();
			reader = fileParser.getAsJsonObject();

			if (reader.has("items")) {
				reader.remove("items");
			}
			reader.add("items", builder.toJsonTree(items));
			fileReader.close();
		} catch (Exception e) {
			System.out.print(e.getMessage());
		}

		try (FileWriter writer = new FileWriter(file)) {
			Gson builder = new GsonBuilder().setPrettyPrinting().create();
			writer.write(builder.toJson(reader));
			writer.close();
			System.out.println("Updated file: " + file.getPath());
		} catch (Exception e) {
			// An error happened while saving.
			GameServer.getLogger().log(Level.WARNING, "An error has occured while saving a file!", e);
		}
	}

	public void open() {
		player.sendMessage("@red@Temporarily disabled.");
		return;
		/*
		boolean hasItems = false;
		for(int i = 0; i < items.length - 1; i++) {
			if(items[i] != -1) {
				hasItems = true;
				break;
			}
		}
		
		if(!hasItems) {
			player.sendMessage("@red@You do not have any items stored.");
			return;
		}
		
		player.getPacketSender().sendInterface(2700);
		int count = 0;
		for(int i : items) {
			if(count == 0) {
				if(i != -1)
					player.getPacketSender().sendItemOnInterface(2702, i, 1);
				else
					player.getPacketSender().sendItemOnInterface(2702, i, 0);
			} else {
				if(i != -1)
					player.getPacketSender().sendItemOnInterface(2705 + count - 1, i, 1);
				else
					player.getPacketSender().sendItemOnInterface(2705 + count - 1, i, 0);
			}
			count++;
		}
	}*/
}