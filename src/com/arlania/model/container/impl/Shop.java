package com.arlania.model.container.impl;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import com.arlania.engine.task.TaskManager;
import com.arlania.engine.task.impl.ShopRestockTask;
import com.arlania.model.GameMode;
import com.arlania.model.Item;
import com.arlania.model.PlayerRights;
import com.arlania.model.Skill;
import com.arlania.model.container.ItemContainer;
import com.arlania.model.container.StackType;
import com.arlania.model.container.impl.Shop.ShopManager;
import com.arlania.model.definitions.ItemDefinition;
import com.arlania.model.input.impl.EnterAmountToBuyFromShop;
import com.arlania.model.input.impl.EnterAmountToSellToShop;
import com.arlania.util.JsonLoader;
import com.arlania.util.Misc;
import com.arlania.world.World;
import com.arlania.world.content.dailytasks.DailyTasks;
import com.arlania.world.content.dailytasks.TaskType;
import com.arlania.world.content.minigames.impl.RecipeForDisaster;
import com.arlania.world.content.skill.impl.dungeoneering.Dungeoneering;
import com.arlania.world.entity.impl.player.Player;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Messy but perfect Shop System
 *
 * @@author Milo
 */

public class Shop extends ItemContainer {

	/*
	 * The shop constructor
	 */
	
	private static final String PREFIX = "Pwnlite Taxbags";
	
	public Shop(Player player, int id, String name, Item currency, Item[] stockItems) {
		super(player);
		if (stockItems.length > 102)
			throw new ArrayIndexOutOfBoundsException(
					"Stock cannot have more than 100 items; check shop[" + id + "]: stockLength: " + stockItems.length);
		this.id = id;
		this.name = name.length() > 0 ? name : "General Store";
		this.currency = currency;
		this.originalStock = new Item[stockItems.length];
		for (int i = 0; i < stockItems.length; i++) {
			Item item = new Item(stockItems[i].getId(), stockItems[i].getAmount());
			add(item, false);
			this.originalStock[i] = item;
		}
	}

	private final int id;

	private String name;

	private Item currency;

	private Item[] originalStock;

	public Item[] getOriginalStock() {
		return this.originalStock;
	}

	public int getId() {
		return this.id;
	}

	public String getName() {
		return name;
	}

	public Shop setName(String name) {
		this.name = name;
		return this;
	}

	public static String formatNumber(int number) {
		return NumberFormat.getInstance().format(number);
	}

	public Item getCurrency() {
		return currency;
	}

	public Shop setCurrency(Item currency) {
		this.currency = currency;
		return this;
	}

	private boolean restockingItems;

	public boolean isRestockingItems() {
		return restockingItems;
	}

	public void setRestockingItems(boolean restockingItems) {
		this.restockingItems = restockingItems;
	}

	/**
	 * Opens a shop for a player
	 *
	 * @param player
	 *            The player to open the shop for
	 * @return The shop instance
	 */
	public Shop open(Player player) {
		setPlayer(player);
		getPlayer().getPacketSender().sendInterfaceRemoval().sendClientRightClickRemoval();
		getPlayer().setShop(ShopManager.getShops().get(id)).setInterfaceId(INTERFACE_ID).setShopping(true);
		refreshItems();
		if (Misc.getMinutesPlayed(getPlayer()) <= 190)
			getPlayer().getPacketSender()
					.sendMessage("Note: When selling an item to a store, it loses 15% of its original value.");
		return this;
	}

	/**
	 * Refreshes a shop for every player who's viewing it
	 */
	public void publicRefresh() {
		Shop publicShop = ShopManager.getShops().get(id);
		if (publicShop == null)
			return;
		publicShop.setItems(getItems());
		for (Player player : World.getPlayers()) {
			if (player == null)
				continue;
			if (player.getShop() != null && player.getShop().id == id && player.isShopping())
				player.getShop().setItems(publicShop.getItems());
		}
	}

	/**
	 * Checks a value of an item in a shop
	 *
	 * @param player
	 *            The player who's checking the item's value
	 * @param slot
	 *            The shop item's slot (in the shop!)
	 * @param sellingItem
	 *            Is the player selling the item?
	 */
	public void checkValue(Player player, int slot, boolean sellingItem) {
		this.setPlayer(player);
		Item shopItem = new Item(getItems()[slot].getId());
		if (!player.isShopping()) {
			player.getPacketSender().sendInterfaceRemoval();
			return;
		}
		Item item = sellingItem ? player.getInventory().getItems()[slot] : getItems()[slot];
		if (item.getId() == 10835)
			return;
		if (sellingItem) {
			if (!shopBuysItem(id, item)) {
				player.getPacketSender().sendMessage("You cannot sell this item to this store.");
				return;
			}
		}
		int finalValue = 0;
		String finalString = sellingItem ? "" + ItemDefinition.forId(item.getId()).getName() + ": shop will buy for "
				: "" + ItemDefinition.forId(shopItem.getId()).getName() + " currently costs ";
		if (getCurrency().getId() != -1) {
			finalValue = ItemDefinition.forId(item.getId()).getValue();
			String s = currency.getDefinition().getName().toLowerCase().endsWith("s")
					? currency.getDefinition().getName().toLowerCase()
					: currency.getDefinition().getName().toLowerCase() + "s";
			/** CUSTOM CURRENCY, CUSTOM SHOP VALUES **/
			if (id == TOKKUL_EXCHANGE_STORE || id == ENERGY_FRAGMENT_STORE || id == STARDUST_STORE|| id == RAIDSTORE
					|| id == AGILITY_TICKET_STORE || id == TOKEN_STORE || id == SUIC_NUMBER_ONE_TOKEN_STORE|| id == GENERAL_STORE
					|| id == GRAVEYARD_STORE || id == DONORTICKET_SHOP || id == BTC_SHOP || id == ETH_SHOP ||  id == DOGE_SHOP ||  id == AFKFISHING_SHOP ||id == BLOODBAG_STORE | id == HWEEN_STORE || id == SANTAS_STORE || id == STARTER_STORE  || id == TAX_BAG_SHOP
					|| id == LOYALTYPOINT_STORE || id == BLOOD_MONEY_STORE || id == BLOOD_MONEY_STORE2 || id == SELL_FOR_TAXBAGS_SHOP|| id == IRONMAN_SHOP
					|| id == DARKLORD_TOKEN_SHOP || id == BLOODSLAYER_STORE || id == RAIDS_FISHING_STORE|| id == IRONZONE_SHOP || id == LOL_SHOP) {
				Object[] obj = ShopManager.getCustomShopData(id, item.getId());
				if (obj == null)
					return;
				finalValue = (int) obj[0];
				s = (String) obj[1];
			}
			if (sellingItem) {
				if (finalValue != 1) {
					finalValue = (int) (finalValue * 0.85);
				}
			}
			finalString += "" + formatNumber((int) finalValue) + " " + s + ".";
		} else {
			Object[] obj = ShopManager.getCustomShopData(id, item.getId());
			if (obj == null)
				return;
			finalValue = (int) obj[0];
			if (sellingItem) {
				if (finalValue != 1) {
					finalValue = (int) (finalValue * 0.85);
				}
			}
			finalString += "" + finalValue + " " + (String) obj[1] + ".";
		}
		if (player != null && finalValue > 0) {
			player.getPacketSender().sendMessage(finalString);
			return;
		}
	}

	public void sellItem(Player player, int slot, int amountToSell) {
		this.setPlayer(player);
		if (!player.isShopping() || player.isBanking()) {
			player.getPacketSender().sendInterfaceRemoval();
			return;
		}

		if (!player.isShopping() || player.isBanking()) {
			player.getPacketSender().sendInterfaceRemoval();
			return;
		}
		Item itemToSell = player.getInventory().getItems()[slot];
		if (!itemToSell.sellable()) {
			player.getPacketSender().sendMessage("This item cannot be sold.");
			return;
		}

		if (id == BLOOD_MONEY_STORE) {
			player.getPacketSender().sendMessage("You cannot sell items to this store.");
			return;
		}
		if (id == BLOOD_MONEY_STORE2) {
			player.getPacketSender().sendMessage("You cannot sell items to this store.");
			return;
		}
		
		if (id == DARKLORD_TOKEN_SHOP || id == BLOODSLAYER_STORE || id == RAIDS_FISHING_STORE || id == RAIDSTORE|| id == GENERAL_STORE) {
			player.sendMessage("You cannot sell items to this store.");
			return;
		}

		if (!shopBuysItem(id, itemToSell)) {
			player.getPacketSender().sendMessage("You cannot sell this item to this store.");
			return;
		}
		if (!player.getInventory().contains(itemToSell.getId()) || itemToSell.getId() == 995)
			return;
		if (this.full(itemToSell.getId()))
			return;
		if (player.getInventory().getAmount(itemToSell.getId()) < amountToSell)
			amountToSell = player.getInventory().getAmount(itemToSell.getId());
		if (amountToSell == 0)
			return;

		/*
		 * if(amountToSell > 300) { String s =
		 * ItemDefinition.forId(itemToSell.getId()).getName().endsWith("s") ?
		 * ItemDefinition.forId(itemToSell.getId()).getName() :
		 * ItemDefinition.forId(itemToSell.getId()).getName() + "s";
		 * player.getPacketSender().sendMessage("You can only sell 300 "+s+
		 * " at a time."); return; }
		 */
		int itemId = itemToSell.getId();
		boolean customShop = this.getCurrency().getId() == -1;
		boolean inventorySpace = customShop ? true : false;
		if (!customShop) {
			if (!itemToSell.getDefinition().isStackable()) {
				if (!player.getInventory().contains(this.getCurrency().getId()))
					inventorySpace = true;
			}
			if (player.getInventory().getFreeSlots() <= 0
					&& player.getInventory().getAmount(this.getCurrency().getId()) > 0)
				inventorySpace = true;
			if (player.getInventory().getFreeSlots() > 0
					|| player.getInventory().getAmount(this.getCurrency().getId()) > 0)
				inventorySpace = true;
		}
		int itemValue = 0;
		if (id != 119 && getCurrency().getId() > 0 && id != 125) {
			itemValue = ItemDefinition.forId(itemToSell.getId()).getValue();
		} else {
			Object[] obj = ShopManager.getCustomShopData(id, itemToSell.getId());
			if (obj == null)
				return;
			itemValue = (int) obj[0];
		}
        player.sendMessage(String.valueOf(itemValue));
		if (itemValue <= 0) {
            return;
        }
		itemValue = (int) (itemValue * 0.85);
		if (itemValue <= 0) {
			itemValue = 1;
		}

		for (int i = amountToSell; i > 0; i--) {
			itemToSell = new Item(itemId);
			if (this.full(itemToSell.getId()) || !player.getInventory().contains(itemToSell.getId())
					|| !player.isShopping())
				break;
			if (!itemToSell.getDefinition().isStackable()) {
				if (inventorySpace) {
					super.switchItem(player.getInventory(), this, itemToSell.getId(), -1);
					if (!customShop) {
						player.getInventory().add(new Item(getCurrency().getId(), itemValue), false);
					} else {
						// Return points here
					}
				} else {
					player.getPacketSender().sendMessage("Please free some inventory space before doing that.");
					break;
				}
			} else {
				if (inventorySpace) {
					super.switchItem(player.getInventory(), this, itemToSell.getId(), amountToSell);
					if (!customShop) {
						player.getInventory().add(new Item(getCurrency().getId(), itemValue * amountToSell), false);
					} else {
						// Return points here
					}
					break;
				} else {
					player.getPacketSender().sendMessage("Please free some inventory space before doing that.");
					break;
				}
			}
			amountToSell--;
		}
		if (customShop) {
			player.getPointsHandler().refreshPanel();
		}
		player.getInventory().refreshItems();
		fireRestockTask();
		refreshItems();
		publicRefresh();
	}

	/**
	 * Buying an item from a shop
	 */
	@Override
	public Shop switchItem(ItemContainer to, Item item, int slot, boolean sort, boolean refresh) {
		final Player player = getPlayer();
		if (player == null)
			return this;
		if (!player.isShopping() || player.isBanking()) {
			player.getPacketSender().sendInterfaceRemoval();
			return this;
		}
		if (this.id == GENERAL_STORE) {
			if (Dungeoneering.doingDungeoneering(player)) {
				player.getPacketSender().sendMessage("You can't do this whilst Dungeoneering");
				return this;
			}
			if (player.getGameMode() == GameMode.IRONMAN) {
				player.getPacketSender()
						.sendMessage("Ironman-players are not allowed to buy items from the general-store.");
				return this;
			}
			if (player.getGameMode() == GameMode.HARDCORE_IRONMAN) {
				player.getPacketSender()
						.sendMessage("Hardcore-ironman-players are not allowed to buy items from the general-store.");
				return this;
			}
		}
		if (!shopSellsItem(item))
			return this;

		if (getItems()[slot].getAmount() <= 1 && id != GENERAL_STORE) {

			player.getPacketSender()
					.sendMessage("The shop can't be 1 items and needs to regenerate some items first..");

		}

		if (item.getAmount() > getItems()[slot].getAmount())
			item.setAmount(getItems()[slot].getAmount());
		int amountBuying = item.getAmount();
		if (id == 21) { // farming cheapfix
			if (getItems()[slot].getAmount() - amountBuying <= 1) {
				amountBuying = getItems()[slot].getAmount() - 1;
				while (getItems()[slot].getAmount() - amountBuying <= 1) {
					if (getItems()[slot].getAmount() - amountBuying == 1)
						break;
					amountBuying--;
				}
			}
		}
		if (getItems()[slot].getAmount() < amountBuying) {
			amountBuying = getItems()[slot].getAmount() - 101;
		}
		if (amountBuying == 0)
			return this;

		if (amountBuying > 25000) {
			player.getPacketSender().sendMessage(
					"You can only buy 25000 " + ItemDefinition.forId(item.getId()).getName() + "s at a time.");
			return this;
		}
		boolean customShop = getCurrency().getId() == -1;
		boolean usePouch = false;
		long playerCurrencyAmount = 0;
		int value = ItemDefinition.forId(item.getId()).getValue();
		String currencyName = "";
		if (getCurrency().getId() != -1) {
			playerCurrencyAmount = player.getInventory().getAmount(currency.getId());
			currencyName = ItemDefinition.forId(currency.getId()).getName().toLowerCase();
			if (currency.getId() == 995) {
				if (player.getMoneyInPouch() >= value) {
					playerCurrencyAmount = player.getMoneyInPouchAsInt();
					if (!(player.getInventory().getFreeSlots() == 0
							&& player.getInventory().getAmount(currency.getId()) == value)) {
						usePouch = true;
					}
				}
			} else {
				/** CUSTOM CURRENCY, CUSTOM SHOP VALUES **/
				if (id == TOKKUL_EXCHANGE_STORE || id == ENERGY_FRAGMENT_STORE || id == STARDUST_STORE|| id == RAIDSTORE
						|| id == AGILITY_TICKET_STORE || id == TOKEN_STORE || id == SUIC_NUMBER_ONE_TOKEN_STORE
						|| id == GRAVEYARD_STORE || id == DONORTICKET_SHOP || id == BTC_SHOP ||  id == ETH_SHOP || id == DOGE_SHOP || id == AFKFISHING_SHOP ||id == SANTAS_STORE || id == STARTER_STORE  || id == TAX_BAG_SHOP
						|| id == LOYALTYPOINT_STORE || id == BLOOD_MONEY_STORE || id == BLOOD_MONEY_STORE2 || id == SELL_FOR_TAXBAGS_SHOP|| id == IRONMAN_SHOP
						|| id == DARKLORD_TOKEN_SHOP || id == BLOODBAG_STORE || id == HWEEN_STORE|| id == BLOODSLAYER_STORE || id == RAIDS_FISHING_STORE|| id == IRONZONE_SHOP|| id == LOL_SHOP ) {
					value = (int) ShopManager.getCustomShopData(id, item.getId())[0];
				}
			}
		} else {
			Object[] obj = ShopManager.getCustomShopData(id, item.getId());
			if (obj == null)
				return this;
			value = (int) obj[0];
			currencyName = (String) obj[1];
			if (id == PKING_REWARDS_STORE) {
			} else if (id == LOYALTYPOINT_STORE) {
				playerCurrencyAmount = player.getPointsHandler().getLoyaltyPoints();
			} else if (id == VOTING_REWARDS_STORE) {
				playerCurrencyAmount = player.getPointsHandler().getVotingPoints();
			} else if (id == DUNGEONEERING_STORE) {
				playerCurrencyAmount = player.getPointsHandler().getDungeoneeringTokens();
			} else if (id == DONATOR_STORE_1) {
				playerCurrencyAmount = player.getPointsHandler().getDonationPoints();
			} else if (id == VOID_STORE) {
				playerCurrencyAmount = player.getPointsHandler().getPestcontrolpoints();
			} else if (id == SKILLING_STORE) {
				playerCurrencyAmount = player.getPointsHandler().getSkillPoints();
			} else if (id == TRIVIA_STORE) {
				playerCurrencyAmount = player.getPointsHandler().getTriviaPoints();
			} else if (id == BOSS_POINT_STORE) {
				playerCurrencyAmount = player.getBossPoints();
			} else if (id == DONATOR_STORE_2) {
				playerCurrencyAmount = player.getPointsHandler().getDonationPoints();
			} else if (id == DONATOR_STORE_3) {
				playerCurrencyAmount = player.getPointsHandler().getDonationPoints();
			} else if (id == PRESTIGE_STORE) {
				playerCurrencyAmount = player.getPointsHandler().getPrestigePoints();
			} else if (id == SLAYER_STORE) {
				playerCurrencyAmount = player.getPointsHandler().getSlayerPoints();
			} else if (id == CUSTOMSLAYER_POINT_STORE) {
				playerCurrencyAmount = player.getPointsHandler().getSlayerPoints();
			} else if (id == BLOODSLAYER_STORE) {
					playerCurrencyAmount = player.getPointsHandler().getBloodSlayerPoints();
			} else if (id == RAIDSTORE) {
			playerCurrencyAmount = player.getPointsHandler().getRaidPoints();
			
			}
			
		}
		if (value <= 0) {
			return this;
		}
		if (!hasInventorySpace(player, item, getCurrency().getId(), value)) {
			player.getPacketSender().sendMessage("You do not have any free inventory slots.");
			return this;
		}
		if (playerCurrencyAmount <= 0 || playerCurrencyAmount < value) {
			player.getPacketSender()
					.sendMessage("You do not have enough "
							+ ((currencyName.endsWith("s") ? (currencyName) : (currencyName + "s")))
							+ " to purchase this item.");
			return this;
		}
		if (id == SKILLCAPE_STORE_1 || id == SKILLCAPE_STORE_2 || id == SKILLCAPE_STORE_3) {
			for (int i = 0; i < item.getDefinition().getRequirement().length; i++) {
				int req = item.getDefinition().getRequirement()[i];
				if ((i == 3 || i == 5) && req == 99)
					req *= 10;
				if (req > player.getSkillManager().getMaxLevel(i)) {
					player.getPacketSender().sendMessage("You need to have at least level 99 in "
							+ Misc.formatText(Skill.forId(i).toString().toLowerCase()) + " to buy this item.");
					return this;
				}
			}
		} else if (id == GAMBLING_STORE) {
			if (item.getId() == 15084 || item.getId() == 299) {
				if (player.getRights() == PlayerRights.PLAYER) {
					player.getPacketSender().sendMessage("You need to be a member to use these items.");
					return this;
				}
			}
		}

		for (int i = amountBuying; i > 0; i--) {
			if (!shopSellsItem(item)) {
				break;
			}
			if (getItems()[slot].getAmount() < amountBuying) {
				amountBuying = getItems()[slot].getAmount() - 101;

			}

			if (getItems()[slot].getAmount() <= 1 && id != GENERAL_STORE) {

				player.getPacketSender()
						.sendMessage("The shop can't be below 1 items and needs to regenerate some items first...");
				break;
			}
			if (!item.getDefinition().isStackable()) {
				if (playerCurrencyAmount >= value && hasInventorySpace(player, item, getCurrency().getId(), value)) {

					if (!customShop) {
						if (usePouch) {
							player.setMoneyInPouch((player.getMoneyInPouch() - value));
						} else {
							player.getInventory().delete(currency.getId(), value, false);
						}
					} else {
						if (id == PKING_REWARDS_STORE) {
							player.getPointsHandler().setPkPoints(-value, true);
						} else if (id == LOYALTYPOINT_STORE) {
							player.getPointsHandler().setLoyaltyPoints(-value, true);
						} else if (id == VOTING_REWARDS_STORE) {
							player.getPointsHandler().setVotingPoints(-value, true);
						} else if (id == DUNGEONEERING_STORE) {
							player.getPointsHandler().setDungeoneeringTokens(-value, true);
						} else if (id == DONATOR_STORE_1) {
							player.getPointsHandler().setDonationPoints(-value, true);
						} else if (id == SKILLING_STORE) {
							player.getPointsHandler().setSkillPoints(-value, true);
						} else if (id == BOSS_POINT_STORE) {
							player.setBossPoints(player.getBossPoints() - value);
						} else if (id == TRIVIA_STORE) {
							player.getPointsHandler().setTriviaPoints(-value, true);
						} else if (id == VOID_STORE) {
							player.getPointsHandler().setPestcontrolpoints(-value, true);
						} else if (id == DONATOR_STORE_2) {
							player.getPointsHandler().setDonationPoints(-value, true);
						} else if (id == DONATOR_STORE_3) {
							player.getPointsHandler().setDonationPoints(-value, true);
						} else if (id == PRESTIGE_STORE) {
							player.getPointsHandler().setPrestigePoints(-value, true);
						} else if (id == SLAYER_STORE) {
							player.getPointsHandler().setSlayerPoints(-value, true);
						} else if (id == BLOODSLAYER_STORE) {
							player.getPointsHandler().setBloodSlayerPoints(-value, true);
						} else if (id == RAIDSTORE) {
							player.getPointsHandler().setRaidPoints(-value, true);
						} else if (id == CUSTOMSLAYER_POINT_STORE) {
							player.getPointsHandler().setSlayerPoints(-value, true);
						}
					}
					

					super.switchItem(to, new Item(item.getId(), 1), slot, false, false);

					playerCurrencyAmount -= value;
				} else {
					break;
				}
			} else {
				if (playerCurrencyAmount >= value && hasInventorySpace(player, item, getCurrency().getId(), value)) {

					int canBeBought = (int) playerCurrencyAmount / (value);
					if (canBeBought >= amountBuying) {
						canBeBought = amountBuying;
					}
					if (canBeBought == 0)
						break;

					if (!customShop) {
                        DailyTasks.INSTANCE.updateTaskProgress(TaskType.SHOPPING, player, currency.getId(), value * canBeBought);
						if (usePouch) {
							player.setMoneyInPouch((player.getMoneyInPouch() - (value * canBeBought)));
						} else {
							player.getInventory().delete(currency.getId(), value * canBeBought, false);
						}
					} else {
						if (id == PKING_REWARDS_STORE) {
							player.getPointsHandler().setPkPoints(-value * canBeBought, true);
						} else if (id == LOYALTYPOINT_STORE) {
							player.getPointsHandler().setLoyaltyPoints(-value * canBeBought, true);
						} else if (id == VOTING_REWARDS_STORE) {
							player.getPointsHandler().setVotingPoints(-value * canBeBought, true);
						} else if (id == DUNGEONEERING_STORE) {
							player.getPointsHandler().setDungeoneeringTokens(-value * canBeBought, true);
						} else if (id == DONATOR_STORE_1) {
							player.getPointsHandler().setDonationPoints(-value * canBeBought, true);
						} else if (id == SKILLING_STORE) {
							player.getPointsHandler().setSkillPoints(-value * canBeBought, true);
						} else if (id == TRIVIA_STORE) {
							player.getPointsHandler().setTriviaPoints(-value * canBeBought, true);
						} else if (id == BOSS_POINT_STORE) {
							player.setBossPoints(player.getBossPoints() - (value * canBeBought));
						} else if (id == DONATOR_STORE_2) {
							player.getPointsHandler().setDonationPoints(-value * canBeBought, true);
						} else if (id == VOID_STORE) {
							player.getPointsHandler().setPestcontrolpoints(-value * canBeBought, true);
						} else if (id == DONATOR_STORE_3) {
							player.getPointsHandler().setDonationPoints(-value * canBeBought, true);
						} else if (id == PRESTIGE_STORE) {
							player.getPointsHandler().setPrestigePoints(-value * canBeBought, true);
						} else if (id == SLAYER_STORE) {
							player.getPointsHandler().setSlayerPoints(-value * canBeBought, true);
						} else if (id == BLOODSLAYER_STORE) {
							player.getPointsHandler().setBloodSlayerPoints(-value * canBeBought, true);
						} else if (id == CUSTOMSLAYER_POINT_STORE) {
							player.getPointsHandler().setSlayerPoints(-value * canBeBought, true);
						} else if (id == RAIDSTORE) {
							player.getPointsHandler().setRaidPoints(-value * canBeBought, true);
						}
					}
					super.switchItem(to, new Item(item.getId(), canBeBought), slot, false, false);
					playerCurrencyAmount -= value;
					break;
				} else {
					break;
				}
			}
			amountBuying--;
		}
		if (!customShop) {
			if (usePouch) {
				player.getPacketSender().sendString(8135, "" + player.getMoneyInPouch()); // Update
				// the
				// money
				// pouch
			}
		} else {
			player.getPointsHandler().refreshPanel();
		}
		player.getInventory().refreshItems();
		fireRestockTask();
		refreshItems();
		publicRefresh();
		return this;
	}

	/**
	 * Checks if a player has enough inventory space to buy an item
	 *
	 * @param item
	 *            The item which the player is buying
	 * @return true or false if the player has enough space to buy the item
	 */
	public static boolean hasInventorySpace(Player player, Item item, int currency, int pricePerItem) {
		if (player.getInventory().getFreeSlots() >= 1) {
			return true;
		}
		if (item.getDefinition().isStackable()) {
			if (player.getInventory().contains(item.getId())) {
				return true;
			}
		}
		if (currency != -1) {
			if (player.getInventory().getFreeSlots() == 0
					&& player.getInventory().getAmount(currency) == pricePerItem) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Shop add(Item item, boolean refresh) {
		super.add(item, false);
		if (id != RECIPE_FOR_DISASTER_STORE)
			publicRefresh();
		return this;
	}

	@Override
	public int capacity() {
		return 100;
	}

	@Override
	public StackType stackType() {
		return StackType.STACKS;
	}

	@Override
	public Shop refreshItems() {
		if (id == RECIPE_FOR_DISASTER_STORE) {
			RecipeForDisaster.openRFDShop(getPlayer());
			return this;
		}
		for (Player player : World.getPlayers()) {
			if (player == null || !player.isShopping() || player.getShop() == null || player.getShop().id != id)
				continue;
			player.getPacketSender().sendItemContainer(player.getInventory(), INVENTORY_INTERFACE_ID);
			player.getPacketSender().sendItemContainer(ShopManager.getShops().get(id), ITEM_CHILD_ID);
			player.getPacketSender().sendString(NAME_INTERFACE_CHILD_ID, name);
			if (player.getInputHandling() == null || !(player.getInputHandling() instanceof EnterAmountToSellToShop
					|| player.getInputHandling() instanceof EnterAmountToBuyFromShop))
				player.getPacketSender().sendInterfaceSet(INTERFACE_ID, INVENTORY_INTERFACE_ID - 1);
		}
		return this;
	}

	@Override
	public Shop full() {
		getPlayer().getPacketSender().sendMessage("The shop is currently full. Please come back later.");
		return this;
	}

	private boolean shopSellsItem(Item item) {
		return contains(item.getId());
	}

	public void fireRestockTask() {
		if (isRestockingItems() || fullyRestocked())
			return;
		setRestockingItems(true);
		TaskManager.submit(new ShopRestockTask(this));
	}

	public void restockShop() {
		for (int shopItemIndex = 0; shopItemIndex < getOriginalStock().length; shopItemIndex++) {
			int currentStockAmount = getItems()[shopItemIndex].getAmount();
			add(getItems()[shopItemIndex].getId(), getOriginalStock()[shopItemIndex].getAmount() - currentStockAmount);
			// publicRefresh();
			refreshItems();
		}

	}

	public boolean fullyRestocked() {
		if (id == GENERAL_STORE) {
			return getValidItems().size() == 0;
		} else if (id == RECIPE_FOR_DISASTER_STORE) {
			return true;
		}
		if (getOriginalStock() != null) {
			for (int shopItemIndex = 0; shopItemIndex < getOriginalStock().length; shopItemIndex++) {
				if (getItems()[shopItemIndex].getAmount() != getOriginalStock()[shopItemIndex].getAmount())
					return false;
			}
		}
		return true;
	}

	public static boolean shopBuysItem(int shopId, Item item) {
		if (shopId == GENERAL_STORE || shopId ==SELL_FOR_TAXBAGS_SHOP|| shopId == IRONMAN_SHOP)
			return true;
		if (shopId == DUNGEONEERING_STORE || shopId == BOSS_POINT_STORE || shopId == SANTAS_STORE || shopId == RAIDSTORE
				|| shopId == SKILLING_STORE || shopId == STARTER_STORE || shopId == TRIVIA_STORE
				|| shopId == DONATOR_STORE_1 || shopId == DONATOR_STORE_2 || shopId == VOID_STORE
				|| shopId == DONATOR_STORE_3 || shopId == PKING_REWARDS_STORE || shopId == VOTING_REWARDS_STORE
				|| shopId == RECIPE_FOR_DISASTER_STORE || shopId == DONORTICKET_SHOP || shopId == BTC_SHOP || shopId == ETH_SHOP || shopId == DOGE_SHOP || shopId == AFKFISHING_SHOP || shopId == BLOODBAG_STORE || shopId == HWEEN_STORE || shopId == ENERGY_FRAGMENT_STORE
				|| shopId == AGILITY_TICKET_STORE || shopId == TOKEN_STORE || shopId == SUIC_NUMBER_ONE_TOKEN_STORE
				|| shopId == GRAVEYARD_STORE || shopId == IRONZONE_SHOP  || shopId == LOL_SHOP|| shopId == TOKKUL_EXCHANGE_STORE || shopId == PRESTIGE_STORE
				|| shopId == STARDUST_STORE || shopId == BLOOD_MONEY_STORE || shopId == SLAYER_STORE
				|| shopId == CUSTOMSLAYER_POINT_STORE || shopId == BLOOD_MONEY_STORE2 || shopId == DARKLORD_TOKEN_SHOP || shopId == RAIDSTORE|| shopId == RAIDS_FISHING_STORE || shopId == BLOODSLAYER_STORE  || shopId == TAX_BAG_SHOP)
			return false;
		Shop shop = ShopManager.getShops().get(shopId);
		if (shop != null && shop.getOriginalStock() != null) {
			for (Item it : shop.getOriginalStock()) {
				if (it != null && it.getId() == item.getId())
					return true;
			}
		}
		return false;
	}

	public static int getSantasStore() {
		return SANTAS_STORE;
	}

	public static int getSkillingStore() {
		return SKILLING_STORE;
	}

	public static int getStarterStore() {
		return STARTER_STORE;
	}

	public static class ShopManager {

		private static Map<Integer, Shop> shops = new HashMap<Integer, Shop>();

		public static Map<Integer, Shop> getShops() {
			return shops;
		}
		
		public static void saveTaxShop() {
			final Item[] items = getShops().get(119).getItems();
			final Path path = Paths.get("./customdata/taxshop/shopdata.txt");
			List<String> lines = new ArrayList<>();
			for (Item item : items) {
				lines.add(item.getId() + ":" + item.getAmount());
			}

			try {
				Files.write(path, lines);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		
		public static void saveTaxShop1() {
			final Item[] items1 = getShops().get(125).getItems();
			final Path path1 = Paths.get("./customdata/taxshop/shopdata.txt");
			List<String> lines = new ArrayList<>();
			for (Item item : items1) {
				lines.add(item.getId() + ":" + item.getAmount());
			}

			try {
				Files.write(path1, lines);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		
		public static void parseTaxShop1() {
			final Shop shop1 = getShops().get(125);
			final Path path1 = Paths.get("./customdata/taxshop/shopdata.txt");
			final Charset cs = Charset.forName("UTF-8");
			try (Stream<String> lines = Files.lines(path1, cs)) {

				lines.forEach(x -> {

					String[] split = x.split(":");
					int id = Integer.parseInt(split[0]);
					int amount = Integer.parseInt(split[1]);
					shop1.addItem(id, amount);
				});

			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

		public static void parseTaxShop() {
			final Shop shop = getShops().get(119);
			final Path path = Paths.get("./customdata/taxshop/shopdata.txt");
			final Charset cs = Charset.forName("UTF-8");
			try (Stream<String> lines = Files.lines(path, cs)) {

				lines.forEach(x -> {

					String[] split = x.split(":");
					int id = Integer.parseInt(split[0]);
					int amount = Integer.parseInt(split[1]);
					shop.addItem(id, amount);
				});

			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

		public static JsonLoader parseShops() {
			//System.out.println("starting to load shops..");
			return new JsonLoader() {
				@Override
				public void load(JsonObject reader, Gson builder) {
					int id = reader.get("id").getAsInt();
					String name = reader.get("name").getAsString();
					Item[] items = builder.fromJson(reader.get("items").getAsJsonArray(), Item[].class);
					Item currency = new Item(reader.get("currency").getAsInt());
					if (id != 0)
						//System.out.println("Id=" + id + " " + name + " " + currency + " " + "");
					shops.put(id, new Shop(null, id, name, currency, items));
				}

				@Override
				public String filePath() {
					return "./data/def/json/world_shops.json";
				}
			};
		}

		public static Object[] getCustomShopData(int shop, int item) {
			if (shop == VOTING_REWARDS_STORE) {
				switch (item) {
				case 6770:
					return new Object[] { 500, "Voting Points" };
				case 13999:
					return new Object[] { 50, "Voting Points" };
				case 298:
					return new Object[] { 10, "Voting Points" };
				case 5163:
					return new Object[] { 500, "Voting Points" };
				case 15373:
					return new Object[] { 3, "Voting Points" };
				case 989:
					return new Object[] { 3, "Voting Points" };
				case 1543:
					return new Object[] { 25, "Voting Points" };
				case 6199:
					return new Object[] { 25, "Voting Points" };
				case 3912:
					return new Object[] { 5, "Voting Points" };
				case 13997:
					return new Object[] { 50, "Voting Points" };
				case 10168:
					return new Object[] { 250, "Voting Points" };
				case 3988:
					return new Object[] { 35, "Voting Points" };
				case 19935:
					return new Object[] { 100, "Voting Points" };
				case 19936:
					return new Object[] { 200, "Voting Points" };
				case 16455:
					return new Object[] { 500, "Voting Points" };
				case 6191:
					return new Object[] { 500, "Voting Points" };
				case 9943:
					return new Object[] { 100, "Voting Points" };
				case 6640:
					return new Object[] { 25, "Voting Points" };
				case 6507:
					return new Object[] { 10, "Voting Points" };
				case 5170:
					return new Object[] { 200, "Voting Points" };
				case 4780:
					return new Object[] { 125, "Voting Points" };
				case 19821:
					return new Object[] { 100, "Voting Points" };
				case 933:
					return new Object[] { 25, "Voting Points" };
				case 18782:
					return new Object[] { 2, "Voting Points" };
						
				case 4789:
				case 4790:
				case 4791:	
				case 4792:
					return new Object[] { 10, "Voting Points" };
				case 6500:
					return new Object[] { 10, "Voting Points" };
				case 19958:
					return new Object[] { 150, "Voting Points" };		
				case 9813:
					return new Object[] { 250, "Voting Points" };
				}
				return new Object[] { 100, "Voting Points" };
				
			} else if (shop == SELL_FOR_TAXBAGS_SHOP || shop == IRONMAN_SHOP) {
			    System.out.println("Am i hitting here");
				switch (item) {

                    case 18956: //Hurricane gloves
                        return new Object[] { 500, "Pwnlite Taxbags" };
                    case 20938: //Inuyasha
                    case 20939:
                    case 20940:
                    case 20941:
                    case 20942:
                    case 20943:
                        return new Object[] { 115000, "Pwnlite Taxbags" };
                    case 20427: //BFG-MMH
                        return new Object[] { 10000, "Pwnlite Taxbags" };

				case 19821:
				case 19754:
				case 5132:
					return new Object[] { 30000, "Pwnlite Taxbags" };
					
				case 12426:
				case 3961:	
				case 18985:
				case 19154:
				case 19741:
				case 19742:
				case 19743:
				case 19744:
				case 5228:
				case 5229:
				case 5230:
				case 5226:
				case 5227:
				case 5231:
					return new Object[] { 25000, "Pwnlite Taxbags" };
					
					
				case 933:
				case 3911:
				case 16451:
				case 16443:
				case 16444:
				case 16445:
				case 16446:
				case 16447:
				case 16448:
				case 16449:
				case 16450:
					return new Object[] { 35000, "Pwnlite Taxbags" };
					
				case 14437:
				case 14438:
				case 14439:
				case 14440:
				case 14441:
				case 14442:
					
					return new Object[] { 38500, "Pwnlite Taxbags" };
					
					
					
				case 14443:
				case 14482:
				case 14483:
				case 18427:
				case 18419:
				case 18420:
				case 18421:
				case 18425:
					return new Object[] { 40000, "Pwnlite Taxbags" };
					
					
					
				case 18422:
				case 18423:
				case 18424:
				case 18426:
					return new Object[] { 50000, "Pwnlite Taxbags" };
					
					
					
					
				case 14694:
				case 14695:
				case 14872:
				case 14873:
				case 14874:
					return new Object[] { 47550, "Pwnlite Taxbags" };
						
					
					
				case 4280:
				case 4281:
				case 4282:
				case 4283:
				case 4284:
				case 5115:
					return new Object[] { 55550, "Pwnlite Taxbags" };
					
					
					
					
				case 20928:
				case 20931:
				case 20932:
				case 20933:
				case 20934:
				case 20935:
				case 20936:
				case 20937:
					return new Object[] { 57750, "Pwnlite Taxbags" };
					
					
					
					
				case 19949:
				case 19950:
				case 19951:
				case 19952:
				case 19953:
					return new Object[] { 60350, "Pwnlite Taxbags" };
					
					
					
				case 8469:
				case 8470:
				case 8471:
				case 8472:
				case 8473:
				case 8474:
				case 8466:
					return new Object[] { 65750, "Pwnlite Taxbags" };
					
					
				case 8482:
				case 8483:
				case 8484:
				case 8486:
				case 8485:
				case 8487:
				case 8488:
					return new Object[] { 69750, "Pwnlite Taxbags" };
					
				case 8569:
				case 8570:
				case 8572:
				case 17855:
				case 17856:
				case 5116:
					return new Object[] { 72750, "Pwnlite Taxbags" };
						
					
					
				case 13271:
				case 13272:
				case 13273:
				case 13274:
				case 13275:	
					return new Object[] { 15000, "Pwnlite Taxbags" };
					
				case 13594:
				case 13595:
				case 13596:
				case 13597:
				case 13664:	
					return new Object[] { 20000, "Pwnlite Taxbags" };
					
				case 9484:
				case 9485:
				case 9486:
				case 16579:
					return new Object[] { 20000, "Pwnlite Taxbags" };
					
					
				case 9487:
				case 9488:
				case 9489:
				case 9490:
				case 9491:
				case 9481:
				case 9482:
				case 9483:
					
					return new Object[] { 23500, "Pwnlite Taxbags" };
					
				case 12926:
				case 6640:
					return new Object[] { 10250, "Pwnlite Taxbags" };
				
				case 12927:
				case 16551:
				case 16549:
				case 16550:
				case 16558:
					return new Object[] { 10250, "Pwnlite Taxbags" };
				case 4669:
				case 18957:
					return new Object[] { 10250, "Pwnlite Taxbags" };
					
					
				case 4670:
				case 6485 :	
				case 20054:
					return new Object[] { 10050, "Pwnlite Taxbags" };
					
				case 4671:
					return new Object[] { 5050, "Pwnlite Taxbags" };
					
				case 19886:
					return new Object[] { 6550, "Pwnlite Taxbags" };
				case 19106:
				case 4780:
					return new Object[] { 7550, "Pwnlite Taxbags" };
				
				case 4672:
				case 6507:
				case 18944:
					return new Object[] { 7575, "Pwnlite Taxbags" };
				case 11218:
				case 16137:
				case 3071:
				case 3069:
				case 18975:
				case 18976:
				case 3951:
				case 11133:
				case 19957:
				case 18934:
				case 3904:
				case 18973:
					return new Object[] { 500, "Pwnlite Taxbags" };	
					
				case 5154:
				case 12001:
				case 3957:
				case 4769:
				case 18931:
				case 19004:
				case 6452:
					return new Object[] { 500, "Pwnlite Taxbags" };
			
				
				case 2577:
				case 989:
				case 85:
				case 3082:
				case 9104:
					return new Object[] { 35, "Pwnlite Taxbags" };
					
				case 18967:
				case 19024:
				case 19025:
				case 19026:
				case 19027:
					
					
				case 905:
				case 902:
				case 903:
				case 904:
				case 5161:
					return new Object[] { 55, "Pwnlite Taxbags" };
					
				case 20016:
				case 20017:
				case 20018:
				case 10720:
				case 14006:
				case 20021:
				case 20022:
				case 18910:
					return new Object[] { 65, "Pwnlite Taxbags" };
					
				case 17911:
				case 19892:
				case 3956:
				case 3928:
				case 17908:
				case 17909:
				case 3932:
				case 4775:
				case 11732:
				case 12601:
				case 18346:
					return new Object[] { 120, "Pwnlite Taxbags" };
					
				case 5079:
				case 18933:
				case 4799:
				case 4800:
				case 4801:
				case 3973:
				case 15012:
				case 1499:
					return new Object[] { 275, "Pwnlite Taxbags" };
					
					
				case 2749:
				case 2750:
				case 2751:
				case 2752:
				case 2753:
				case 2754:
				case 13261:
				case 18865:
					return new Object[] { 225, "Pwnlite Taxbags" };
					
				case 19137:
				case 19138:
				case 19139:
				case 6041:
				case 15044:
				case 5130:
				case 3073:
					return new Object[] { 155, "Pwnlite Taxbags" };
					
				case 3960:
				case 3958:
				case 3959:
				case 5187:
				case 5186:
				case 3316:
				case 3931:
				case 14559:
				case 6583:
					return new Object[] { 500, "Pwnlite Taxbags" };
					
				case 17776:
				case 19131:
				case 19132:
				case 19133:
				case 18942:
				case 18941:
				case 18940:
				case 922:
				case 20002:
					return new Object[] { 250, "Pwnlite Taxbags" };
					
				case 18380:
				case 18381:
				case 18382:
				case 9006:
				case 3941:
				case 3974:
				case 5162:
					return new Object[] { 500, "Pwnlite Taxbags" };
					
				case 19721:
				case 19722:
				case 19723:
				case 19736:
				case 19734:
				case 18892:
				case 15418:
				case 15398:
				case 18363:
					return new Object[] { 500, "Pwnlite Taxbags" };
					
				case 6193:
				case 6194:
				case 6195:
				case 6196:
				case 6197:
				case 6198:
				case 6199:
					return new Object[] { 1200, "Pwnlite Taxbags" };
					
				case 11148:
				case 11149:
				case 11150:
				case 11160:
				case 11161:
					return new Object[] { 1600, "Pwnlite Taxbags" };
					
				case 5131:
				case 4772:
				case 4771:
				case 4770:
				case 12708:
				case 13235:
				case 13239:
				case 18347:
					return new Object[] { 1000, "Pwnlite Taxbags" };
					
				case 15020:
				case 15018:
				case 15019:
				case 15220:
					return new Object[] { 150, "Pwnlite Taxbags" };
				case 14453:
				case 14457:
				case 14459:
					return new Object[] { 250, "Pwnlite Taxbags" };
					
				case 4059:
				case 4057:
				case 4058:
					return new Object[] { 7000, "Pwnlite Taxbags" };

				case 923:
					return new Object[] { 3200, "Pwnlite Taxbags" };
					
				case 12605:
				case 3908:
				case 3909:
				case 3910:
				case 3907:
				case 19720:
					return new Object[] { 4500, "Pwnlite Taxbags" };	
					
					
				case 3980:
				case 3999:
				case 4000:
				case 4001:
				case 18955:
					return new Object[] { 4750, "Pwnlite Taxbags" };	
					
					
					
					
				case 15649:
				case 15650:
				case 15651:
				case 15654:
				case 15655:
				case 5167:
				case 15652:
				case 15653:
					return new Object[] { 5000, "Pwnlite Taxbags" };	
					
				case 4761:
				case 4762:
				case 4763:
				case 4764:
				case 4765:
				case 3905:
				case 5089:
				case 18894:
					return new Object[] { 6000, "Pwnlite Taxbags" };
					
				case 15045:
				case 930:
				case 926:
				case 5210:
				case 931:
				case 5211:
					return new Object[] { 4300, "Pwnlite Taxbags" };
					

				case 3820:
				case 3821:
				case 3822:
				case 4781:
				case 4782:
				case 20240:
				case 15032:
				case 4785:
				case 5195:
				case 3914:
				case 19140:
				case 18950:
					return new Object[] { 7500, "Pwnlite Taxbags" };
					
				case 3985:
				case 5082:
				case 5083:
				case 5084:
				case 15656:
					return new Object[] { 8000, "Pwnlite Taxbags" };	
				case 19619:
				case 19470:
				case 19471:
				case 19472:
				case 19473:
				case 19474:
				case 5129:

					return new Object[] { 9000, "Pwnlite Taxbags" };	
					
					
				case 3064:
				case 3983:
				case 4641:
				case 4642:
				case 4643:
				case 19085:

					return new Object[] { 11000, "Pwnlite Taxbags" };	
					
				case 19618:
				case 19620:
				case 19691:
				case 19692:
				case 19693:
				case 19694:
				case 19695:
				case 19696:
					return new Object[] { 13500, "Pwnlite Taxbags" };	
					
					
				case 19159:
				case 19160:
				case 19161:
				case 19162:
				case 19163:
				case 19164:	
				case 19165:
				case 19166:

					return new Object[] { 14300, "Pwnlite Taxbags" };
					
					
				case 9492:
				case 9493:
				case 9494:
				case 9495:
				case 14490:
				case 14492:	
				case 14494:
				case 2760:

					return new Object[] { 17500, "Pwnlite Taxbags" };
					
					
					
				case 19727:
				case 19728:
				case 19729:
				case 19730:
				case 19731:
				case 19732:	

					return new Object[] { 19000, "Pwnlite Taxbags" };
					
					
				case 8664:	
					return new Object[] { 25000, "Pwnlite Taxbags" };
					
					
				case 13202:
				case 13203:
				case 13204:
				case 13205:
				case 13206:
				case 13207:	

					return new Object[] { 20000, "Pwnlite Taxbags" };
					
				case 11143:
				case 11144:
				case 11145:
				case 11146:
				case 11147:

					return new Object[] { 22500, "Pwnlite Taxbags" };
					
				case 4794:
				case 4795:
				case 4796:
				case 4797:
				case 19127:
				case 19128:
				case 19129:

					return new Object[] { 23500, "Pwnlite Taxbags" };
					
				case 13991:
				case 13992:
				case 13993:
				case 13994:
				case 13995:
				case 14447:
				case 14448:

					return new Object[] { 25500, "Pwnlite Taxbags" };
					
					
				case 9496:
				case 9497:
				case 9498:
				case 9499:
				case 10905:
				case 19155:

					return new Object[] { 23500, "Pwnlite Taxbags" };

				}
			} else if (shop == IRONMAN_SHOP || shop == SELL_FOR_TAXBAGS_SHOP) {
				switch (item) {
                case 20938: //Inuyasha
                case 20939:
                case 20940:
                case 20941:
                case 20942:
                case 20943:
                    return new Object[] { 115000, "Pwnlite Taxbags" };
            case 18956: //Hurricane gloves
                return new Object[] { 500, "Pwnlite Taxbags" };

            case 20427: //BFG-MMH
                return new Object[] { 10000, "Pwnlite Taxbags" };

		case 19821:
		case 19754:
		case 5132:
			return new Object[] { 30000, "Pwnlite Taxbags" };
			
		case 12426:
		case 3961:	
		case 18985:
		case 19154:
		case 19741:
		case 19742:
		case 19743:
		case 19744:
		case 5228:
		case 5229:
		case 5230:
		case 5226:
		case 5227:
		case 5231:
			return new Object[] { 25000, "Pwnlite Taxbags" };
			
			
		case 933:
		case 3911:
		case 16451:
		case 16443:
		case 16444:
		case 16445:
		case 16446:
		case 16447:
		case 16448:
		case 16449:
		case 16450:
			return new Object[] { 35000, "Pwnlite Taxbags" };
			
		case 14437:
		case 14438:
		case 14439:
		case 14440:
		case 14441:
		case 14442:
			
			return new Object[] { 38500, "Pwnlite Taxbags" };
			
			
			
		case 14443:
		case 14482:
		case 14483:
		case 18427:
		case 18419:
		case 18420:
		case 18421:
		case 18425:
			return new Object[] { 40000, "Pwnlite Taxbags" };
			
			
			
		case 18422:
		case 18423:
		case 18424:
		case 18426:
			return new Object[] { 50000, "Pwnlite Taxbags" };
			
			
			
			
		case 14694:
		case 14695:
		case 14872:
		case 14873:
		case 14874:
			return new Object[] { 47550, "Pwnlite Taxbags" };
				
			
			
		case 4280:
		case 4281:
		case 4282:
		case 4283:
		case 4284:
		case 5115:
			return new Object[] { 55550, "Pwnlite Taxbags" };
			
			
			
			
		case 20928:
		case 20931:
		case 20932:
		case 20933:
		case 20934:
		case 20935:
		case 20936:
		case 20937:
			return new Object[] { 57750, "Pwnlite Taxbags" };
			
			
			
			
		case 19949:
		case 19950:
		case 19951:
		case 19952:
		case 19953:
			return new Object[] { 60350, "Pwnlite Taxbags" };
			
			
			
		case 8469:
		case 8470:
		case 8471:
		case 8472:
		case 8473:
		case 8474:
		case 8466:
			return new Object[] { 65750, "Pwnlite Taxbags" };
			
			
			
			
		case 8482:
		case 8483:
		case 8484:
		case 8486:
		case 8485:
		case 8487:
		case 8488:
			return new Object[] { 69750, "Pwnlite Taxbags" };
			
		case 8569:
		case 8570:
		case 8572:
		case 17855:
		case 17856:
		case 5116:
			return new Object[] { 72750, "Pwnlite Taxbags" };
				
			
			
		case 13271:
		case 13272:
		case 13273:
		case 13274:
		case 13275:	
			return new Object[] { 15000, "Pwnlite Taxbags" };
			
		case 13594:
		case 13595:
		case 13596:
		case 13597:
		case 13664:	
			return new Object[] { 20000, "Pwnlite Taxbags" };
			
		case 9484:
		case 9485:
		case 9486:
		case 16579:
			return new Object[] { 20000, "Pwnlite Taxbags" };
			
			
		case 9487:
		case 9488:
		case 9489:
		case 9490:
		case 9491:
		case 9481:
		case 9482:
		case 9483:
			
			return new Object[] { 23500, "Pwnlite Taxbags" };
			
		case 12926:
		case 6640:
			return new Object[] { 10250, "Pwnlite Taxbags" };
		
		case 12927:
		case 16551:
		case 16549:
		case 16558:
		case 16550:
			return new Object[] { 10250, "Pwnlite Taxbags" };
		case 4669:
		case 18957:
			return new Object[] { 10250, "Pwnlite Taxbags" };
			
			
		case 4670:
		case 6485 :	
		case 20054:
			return new Object[] { 10050, "Pwnlite Taxbags" };
			
		case 4671:
			return new Object[] { 5050, "Pwnlite Taxbags" };
			
		case 19886:
			return new Object[] { 6550, "Pwnlite Taxbags" };
		case 19106:
		case 4780:
			return new Object[] { 7550, "Pwnlite Taxbags" };
		
		case 4672:
		case 6507:
		case 18944:
			return new Object[] { 7575, "Pwnlite Taxbags" };
		case 11218:
		case 16137:
		case 3071:
		case 3069:
		case 18975:
		case 18976:
		case 3951:
		case 11133:
		case 19957:
		case 18934:
		case 3904:
		case 18973:
			return new Object[] { 500, "Pwnlite Taxbags" };	
			
		case 5154:
		case 12001:
		case 3957:
		case 4769:
		case 18931:
		case 19004:
		case 6452:
			return new Object[] { 500, "Pwnlite Taxbags" };
	
		
		case 2577:
		case 989:
		case 85:
		case 3082:
		case 9104:
			return new Object[] { 35, "Pwnlite Taxbags" };
			
		case 18967:
		case 19024:
		case 19025:
		case 19026:
		case 19027:
			
			
		case 905:
		case 902:
		case 903:
		case 904:
		case 5161:
			return new Object[] { 55, "Pwnlite Taxbags" };
			
		case 20016:
		case 20017:
		case 20018:
		case 10720:
		case 14006:
		case 20021:
		case 20022:
		case 18910:
			return new Object[] { 65, "Pwnlite Taxbags" };
			
		case 17911:
		case 19892:
		case 3956:
		case 3928:
		case 17908:
		case 17909:
		case 3932:
		case 4775:
		case 11732:
		case 12601:
		case 18346:
			return new Object[] { 120, "Pwnlite Taxbags" };
			
		case 5079:
		case 18933:
		case 4799:
		case 4800:
		case 4801:
		case 3973:
		case 15012:
		case 1499:
			return new Object[] { 275, "Pwnlite Taxbags" };
			
			
		case 2749:
		case 2750:
		case 2751:
		case 2752:
		case 2753:
		case 2754:
		case 13261:
		case 18865:
			return new Object[] { 225, "Pwnlite Taxbags" };
			
		case 19137:
		case 19138:
		case 19139:
		case 6041:
		case 15044:
		case 5130:
		case 3073:
			return new Object[] { 155, "Pwnlite Taxbags" };
			
		case 3960:
		case 3958:
		case 3959:
		case 5187:
		case 5186:
		case 3316:
		case 3931:
		case 14559:
		case 6583:
			return new Object[] { 500, "Pwnlite Taxbags" };
			
		case 17776:
		case 19131:
		case 19132:
		case 19133:
		case 18942:
		case 18941:
		case 18940:
		case 922:
		case 20002:
			return new Object[] { 250, "Pwnlite Taxbags" };
			
		case 18380:
		case 18381:
		case 18382:
		case 9006:
		case 3941:
		case 3974:
		case 5162:
			return new Object[] { 500, "Pwnlite Taxbags" };
			
		case 19721:
		case 19722:
		case 19723:
		case 19736:
		case 19734:
		case 18892:
		case 15418:
		case 15398:
		case 18363:
			return new Object[] { 500, "Pwnlite Taxbags" };
			
		case 6193:
		case 6194:
		case 6195:
		case 6196:
		case 6197:
		case 6198:
		case 6199:
			return new Object[] { 1200, "Pwnlite Taxbags" };
			
		case 11148:
		case 11149:
		case 11150:
		case 11160:
		case 11161:
			return new Object[] { 1600, "Pwnlite Taxbags" };
			
		case 5131:
		case 4772:
		case 4771:
		case 4770:
		case 12708:
		case 13235:
		case 13239:
		case 18347:
			return new Object[] { 1000, "Pwnlite Taxbags" };
			
		case 15020:
		case 15018:
		case 15019:
		case 15220:
			return new Object[] { 150, "Pwnlite Taxbags" };
		case 14453:
		case 14457:
		case 14459:
			return new Object[] { 250, "Pwnlite Taxbags" };
			
		case 4059:
		case 4057:
		case 4058:
			return new Object[] { 7000, "Pwnlite Taxbags" };

		case 923:
			return new Object[] { 3200, "Pwnlite Taxbags" };
			
		case 12605:
		case 3908:
		case 3909:
		case 3910:
		case 3907:
		case 19720:
			return new Object[] { 4500, "Pwnlite Taxbags" };	
			
			
		case 3980:
		case 3999:
		case 4000:
		case 4001:
		case 18955:
			return new Object[] { 4750, "Pwnlite Taxbags" };	
			
			
			
			
		case 15649:
		case 15650:
		case 15651:
		case 15654:
		case 15655:
		case 5167:
		case 15652:
		case 15653:
			return new Object[] { 5000, "Pwnlite Taxbags" };	
			
		case 4761:
		case 4762:
		case 4763:
		case 4764:
		case 4765:
		case 3905:
		case 5089:
		case 18894:
			return new Object[] { 6000, "Pwnlite Taxbags" };
			
		case 15045:
		case 930:
		case 926:
		case 5210:
		case 931:
		case 5211:
			return new Object[] { 4300, "Pwnlite Taxbags" };
			

		case 3820:
		case 3821:
		case 3822:
		case 4781:
		case 4782:
		case 20240:
		case 15032:
		case 4785:
		case 5195:
		case 3914:
		case 19140:
		case 18950:
			return new Object[] { 7500, "Pwnlite Taxbags" };
			
		case 3985:
		case 5082:
		case 5083:
		case 5084:
		case 15656:
			return new Object[] { 8000, "Pwnlite Taxbags" };	
		case 19619:
		case 19470:
		case 19471:
		case 19472:
		case 19473:
		case 19474:
		case 5129:

			return new Object[] { 9000, "Pwnlite Taxbags" };	
			
			
		case 3064:
		case 3983:
		case 4641:
		case 4642:
		case 4643:
		case 19085:

			return new Object[] { 11000, "Pwnlite Taxbags" };	
			
		case 19618:
		case 19620:
		case 19691:
		case 19692:
		case 19693:
		case 19694:
		case 19695:
		case 19696:
			return new Object[] { 13500, "Pwnlite Taxbags" };	
			
			
		case 19159:
		case 19160:
		case 19161:
		case 19162:
		case 19163:
		case 19164:	
		case 19165:
		case 19166:

			return new Object[] { 14300, "Pwnlite Taxbags" };
			
			
		case 9492:
		case 9493:
		case 9494:
		case 9495:
		case 14429:
		case 14430:	
		case 14431:
		case 2760:

			return new Object[] { 17500, "Pwnlite Taxbags" };
			
			
			
		case 19727:
		case 19728:
		case 19729:
		case 19730:
		case 19731:
		case 19732:	

			return new Object[] { 19000, "Pwnlite Taxbags" };
			
			
		case 8664:	
			return new Object[] { 25000, "Pwnlite Taxbags" };
			
			
		case 13202:
		case 13203:
		case 13204:
		case 13205:
		case 13206:
		case 13207:	

			return new Object[] { 20000, "Pwnlite Taxbags" };
			
		case 11143:
		case 11144:
		case 11145:
		case 11146:
		case 11147:

			return new Object[] { 22500, "Pwnlite Taxbags" };
			
		case 4794:
		case 4795:
		case 4796:
		case 4797:
		case 19127:
		case 19128:
		case 19129:

			return new Object[] { 23500, "Pwnlite Taxbags" };
			
		case 13991:
		case 13992:
		case 13993:
		case 13994:
		case 13995:
		case 14447:
		case 14448:

			return new Object[] { 25500, "Pwnlite Taxbags" };
			
			
		case 9496:
		case 9497:
		case 9498:
		case 9499:
		case 10905:
		case 19155:

			return new Object[] { 23500, "Pwnlite Taxbags" };


				}
			} else if (shop == BLOOD_MONEY_STORE) {
				switch (item) {
				case 11614:
					return new Object[] { 10000, "Blood Money" };
				case 15492:
					return new Object[] { 20000, "Blood Money" };
				case 18967:
					return new Object[] { 8500, "Blood Money" };
				case 15373:
					return new Object[] { 5000, "Blood Money" };
				case 15243:
					return new Object[] { 15000, "Blood Money" };
				case 19024:
				case 19025:
				case 19026:
				case 19027:
					return new Object[] { 7500, "Blood Money" };
				case 13740:
					return new Object[] { 10000, "Blood Money" };
				case 19002:
					return new Object[] { 12500, "Blood Money" };
				case 3445:
					return new Object[] { 30000, "Blood Money" };
				case 20250:
					return new Object[] { 15000, "Blood Money" };
				case 18931:
					return new Object[] { 35000, "Blood Money" };
				case 18963:
				case 18964:
				case 18972:
					return new Object[] { 100000, "Blood Money" };
				}
				return new Object[] { 70000, "Blood Money" };
			} else if (shop == DARKLORD_TOKEN_SHOP) {
				switch(item) {
					
				case 3941:
				case 3974:
					return new Object[] { 10, "Claw Tokens" };
					
				case 18380:
				case 18381:
				case 18382:
				case 9006:

					return new Object[] { 3, "Claw Tokens" };
				case 5162:
					return new Object[] { 20, "Claw Tokens" };	
				case 18392:
					return new Object[] { 5, "Claw Tokens" };
				}
				
			} else if (shop == BLOODSLAYER_STORE) {
				switch(item) {
					
				case 10205:
					return new Object[] { 2500, "Bloodslayer points" };
				case 13022:
					return new Object[] { 2500, "Bloodslayer points" };
				case 14033:
					return new Object[] { 7500, "Bloodslayer points" };
				case 13999:
					return new Object[] { 5000, "Bloodslayer points" };
				case 6758:
					return new Object[] { 7500, "Bloodslayer points" };
				case 6759:
					return new Object[] { 10000, "Bloodslayer points" };
				//case 14033:
					//return new Object[] { 2500, "Bloodslayer points" };
				case 16488:
					return new Object[] { 12500, "Bloodslayer points" };
				case 5266:
					return new Object[] { 5000, "Bloodslayer points" };
				case 10168:
					return new Object[] { 1500, "Bloodslayer points" };
				case 13997:
					return new Object[] { 750, "Bloodslayer points" };
				case 5185:
					return new Object[] { 2500, "Bloodslayer points" };
				case 3949:
				case 3950:
				case 3952:
				case 3918:
				case 4803:
					return new Object[] { 1500, "Bloodslayer points" };

				case 19156:
					return new Object[] { 5000, "Bloodslayer points" };
				case 16455:
					return new Object[] { 1250, "Bloodslayer points" };
				}
				
			} else if (shop == RAIDS_FISHING_STORE) {
				switch(item) {
					
				case 4777:
					return new Object[] { 25, "Fish tokens" };
				case 19479:
					return new Object[] { 20, "Fish tokens" };
				case 18950:
					return new Object[] { 13, "Fish tokens" };
				case 19886:
					return new Object[] { 18, "Fish tokens" };
				case 6507:
					return new Object[] { 12, "Fish tokens" };

				
				}
			} else if (shop == BLOOD_MONEY_STORE2) {
				switch (item) {
				case 20555:
				case 12284:
				case 4706:
					return new Object[] { 100000, "Blood Money" };
				case 13848:// brawlers
				case 13849:
				case 13850:
				case 13851:
				case 13852:
				case 13853:
				case 13854:
				case 13855:
				case 13856:
				case 13857:
					return new Object[] { 3000, "Blood Money" };
				case 12936:
				case 12282:
				case 20998:
					return new Object[] { 600000, "Blood Money" };
				case 15273:
				case 4451:
					return new Object[] { 50, "Blood Money" };
				case 6585:
				case 11732:
				case 6920:
				case 2577:
					return new Object[] { 10000, "Blood Money" };
				case 4153:
					return new Object[] { 5000, "Blood Money" };
				case 11924:
				case 11926:
					return new Object[] { 40000, "Blood Money" };
				case 6914:
				case 6889:
				case 6918:
				case 6916:
				case 6924:
					return new Object[] { 30000, "Blood Money" };
				}
				return new Object[] { 60000, "Blood Money" };
			} else if (shop == STARDUST_STORE) {
				switch (item) {
				case 19089:
				case 19092:
				case 19091:
				case 19094:
				case 19090:
				case 19093:
					return new Object[] { 2000, "Stardust" };
				case 17933:
					return new Object[] { 3000, "Stardust" };
				case 2756:
					return new Object[] { 25000, "Stardust" };
				case 19055:
				case 5080:	
					return new Object[] { 12500, "Stardust" };
				case 1666:
					return new Object[] { 3500, "Stardust" };
				case 19935:
				case 13876:
				case 13870:
				case 13873:
				case 13893:
				case 13887:
				case 13899:
					return new Object[] { 7500, "Stardust" };
					
				case 19936:
					return new Object[] { 15000, "Stardust" };
					
				case 6507:
					return new Object[] { 4500, "Stardust" };
				case 3912:
					return new Object[] { 350, "Stardust" };
				case 19886:
					return new Object[] { 15000, "Stardust" };
				case 3317:
					return new Object[] { 18000, "Stardust" };
				case 934:
					return new Object[] { 50000, "Stardust" };
				case 744:
			    
					return new Object[] { 5, "Stardust" };
				case 85:
				    
					return new Object[] { 250, "Stardust" };
				
				case 11133:
					return new Object[] { 1250, "Stardust" };
					
				}
				return new Object[] { 100, "Stardust Points" };
			} else if (shop == SKILLING_STORE) {
				switch (item) {
				case 3912:// Coal ore ( Noted )
					return new Object[] { 3000, "Skilling Points" };
				case 18782:// Coal ore ( Noted )
					return new Object[] { 2500, "Skilling Points" };
				case 15369:// Common mbox
					return new Object[] { 400, "Skilling Points" };
				case 15370:// Uncommon box
					return new Object[] { 600, "Skilling Points" };
				case 15373:
					return new Object[] { 1250, "Skilling Points" };
				case 15374:
				case 6640:
					return new Object[] { 10000, "Skilling Points" };
				case 19780:// Korasi
					return new Object[] { 100, "Skilling Points" };
				case 17291:// Blood Necklace
				case 13738:// Arcane
					return new Object[] { 125, "Skilling Points" };
				case 10408:
				case 10410:
				case 10404:
				case 10406:
					return new Object[] { 5000, "Skilling Points" };
				case 18979:
				case 11587:
					return new Object[] { 75000, "Skilling Points" };
				case 19088:
					return new Object[] { 75000, "Skilling Points" };
				case 11588:
					return new Object[] { 50000, "Skilling Points" };
				
				case 9470:
					return new Object[] { 475, "Skilling Points" };
				case 19708:// Blowpipe
				case 19707:
				case 19706:
					return new Object[] { 1500, "Skilling Points" };
				case 1419:
					return new Object[] { 2000, "Skilling Points" };
				case 18978:
				case 18964:
				case 18963:
				case 18972:
				case 19023: // goldclaws
				case 3082:
				case 12434:
				case 12435:
				case 12436:
				case 18901:
					return new Object[] { 75, "Skilling Points" };
				case 18971:
				case 18903:
				case 14559:
				case 12433:
					return new Object[] { 120, "Skilling Points" };
				case 3083:
					return new Object[] { 95, "Skilling Points" };
				case 16049:
					return new Object[] { 90, "Skilling Points" };
				case 12428:
					return new Object[] { 185, "Skilling Points" };
				case 19054:
				case 19055:
				case 18957:
				case 12430:

					return new Object[] { 150, "Skilling Points" };
				}
				return new Object[] { 150, "Skilling Points" };
			} else if (shop == ENERGY_FRAGMENT_STORE) {
				switch (item) {
				case 5509:
					return new Object[] { 400, "energy fragments" };
				case 5510:
					return new Object[] { 750, "energy fragments" };
				case 5512:
					return new Object[] { 1100, "energy fragments" };
				}
			} else if (shop == DONORTICKET_SHOP) {
				switch (item) {
				case 7691:
					return new Object[] { 6000, "Donator tickets" };
				case 7692:
					return new Object[] { 1500, "Donator tickets" };
				case 7694:
					return new Object[] { 1, "Donator tickets" };
				}
				
				return new Object[] { 10000, "Donor tickets" };
				
			} else if (shop == BTC_SHOP) {
				switch (item) {
				case 2595:
					return new Object[] { 10, "BTC" };
				case 7696:
					return new Object[] { 10, "BTC" };
				case 14549:
					return new Object[] { 5, "BTC" };
				case 10674:
					return new Object[] { 5, "BTC" };
				}
				
				return new Object[] { 10000, "BTC" };
			} else if (shop == ETH_SHOP) {
				switch (item) {
				case 2595:
					return new Object[] { 100, "ETH" };
				case 7696:
					return new Object[] { 100, "ETH" };
				case 5197:
					return new Object[] { 50, "ETH" };
					
				}
				
				return new Object[] { 10000, "ETH" };
			} else if (shop == DOGE_SHOP) {
				switch (item) {
				case 2595:
					return new Object[] { 150000, "Dogecoins" };
				case 7696:
					return new Object[] { 150000, "Dogecoins" };
				case 6770:
					return new Object[] { 50000, "Dogecoins" };
				case 14101:
					return new Object[] { 500, "Dogecoins" };
				}
				
				return new Object[] { 10000, "Dogecoins" };
			} else if (shop == AFKFISHING_SHOP) {
				switch (item) {
				case 6532:
					return new Object[] { 1, "AFK Fish" };

					
				}
				
				return new Object[] { 10000, "AFK Fish" };
			} else if (shop == BLOODBAG_STORE) {
				switch (item) {
				case 5205:
					return new Object[] { 10000, "@red@Bloodbags" };
				case 19935:
					return new Object[] { 5000, "@red@Bloodbags" };
				case 19936:
					return new Object[] { 10000, "@red@Bloodbags" };
				case 13999:
					return new Object[] { 20000, "@red@Bloodbags" };
				case 14808:
					return new Object[] { 20000, "@red@Bloodbags" };
				case 14259:
					return new Object[] { 10000, "@red@Bloodbags" };
				case 4082:
					return new Object[] { 25000, "@red@Bloodbags" };
				case 8699:
					return new Object[] { 15000, "@red@Bloodbags" };
				case 14549:
					return new Object[] { 100000, "@red@Bloodbags" };
				case 6770:
					return new Object[] { 50000, "@red@Bloodbags" };
				case 4670:
					return new Object[] { 2500, "@red@Bloodbags" };
				case 4671:
					return new Object[] { 2500, "@red@Bloodbags" };
				case 4672:
					return new Object[] { 2500, "@red@Bloodbags" };
				case 4673:
					return new Object[] { 2500, "@red@Bloodbags" };
				case 5185:
					return new Object[] { 25000, "@red@Bloodbags" };
					
				}
				
				return new Object[] { 10000, "@red@Bloodbags" };
			} else if (shop == HWEEN_STORE) {
				switch (item) {
				case 6504:
					return new Object[] { 750, "@red@Halloween tickets" };
				case 6503:
				return new Object[] { 1500, "@red@Halloween tickets" };
				case 1959:
					return new Object[] { 500, "@red@Halloween tickets" };
				case 14084:
					return new Object[] { 750, "@red@Halloween tickets" };
				}
				return new Object[] { 10000, "@red@Halloween tickets" };
			} else if (shop == TAX_BAG_SHOP) {
				switch (item) {
				
				
				case 19886: // col neck
					return new Object[] {5000, PREFIX};
				case 4770: // crimson pieces
				case 4771:
				case 4772:
					return new Object[] {1500, PREFIX};
				case 4802: // defenders sword
					return new Object[] {2500, PREFIX};
					
				case 3316: // greywave ss
					return new Object[] {750, PREFIX};
				case 18865: // lit sword
					return new Object[] {8000, PREFIX};
				case 3286: // egyptian g
					return new Object[] {20, PREFIX};
				case 3943: // Text neck
					return new Object[] {2500, PREFIX};
				case 3958: // rex (reg)
				case 3959:
				case 3960:
					return new Object[] {1000, PREFIX};
				case 4764: //Suic nr1 gloves/boots
				case 4765:
					return new Object[] {1000, PREFIX};
				case 3064: // defenders ss
					return new Object[] {200, PREFIX};
					
				case 5131: // dmg
					return new Object[] {6000, PREFIX};
					
				case 5133: // suics mg
					return new Object[] {4500, PREFIX};
				case 5132: // vortexs mg
					return new Object[] {3500, PREFIX};
					
				case 16140: // 100% ammy(ruby)
					return new Object[] {5000, PREFIX};
					
				case 20054: // 100% ring(devotion)
					return new Object[] {3000, PREFIX};
					
					
					
				
				
				
				
				
				}
				
				
			} else if (shop == SANTAS_STORE) {
				switch (item) {

				case 5130:
					return new Object[] { 2500, "PVM Tickets" };
				case 20054:
					return new Object[] { 20000, "PVM Tickets" };

				case 18918:
				case 18917:
				case 18916:
					return new Object[] { 3000, "PVM Tickets" };

				case 17914:
				case 17913:
				case 17912:
					return new Object[] { 20000, "PVM Tickets" };

				case 14453:
				case 14455:
				case 14457:
					return new Object[] { 2500, "PVM Tickets" };

				case 3912:
					return new Object[] { 12000, "PVM Tickets" };

				case 3072:
					return new Object[] { 4500, "PVM Tickets" };

				case 4312:
					return new Object[] { 6500, "PVM Tickets" };

				case 18985:
					return new Object[] { 9500, "PVM Tickets" };

				case 19091:
				case 19094:
					return new Object[] { 7500, "PVM Tickets" };

				case 11288:
					return new Object[] { 1000, "PVM Tickets" };
				case 19936:
					return new Object[] { 1000, "PVM Tickets" };
				case 18768:
					return new Object[] { 13000, "PVM Tickets" };
				case 15374:
					return new Object[] { 15000, "PVM Tickets" };
				case 1038:
				case 1040:
				case 1042:
				case 1044:
				case 1046:
				case 1048:
				case 4565:
				case 13742:
				case 15373:
				case 15372:
				case 6199:
					return new Object[] { 300, "PVM Tickets" };
				case 5134:
					return new Object[] { 38000, "PVM Tickets" };
				case 19000:
				case 19010:
					return new Object[] { 15000, "PVM Tickets" };
				case 19004:
					return new Object[] { 150000, "PVM Tickets" };
				case 19033:
				case 19032:
				case 19031:
				case 19030:
				case 19029:
					return new Object[] { 2000, "PVM Tickets" };
				case 758:
					return new Object[] { 15000, "PVM Tickets" };
				case 3287:
					return new Object[] { 85000, "PVM Tickets" };
				case 14587:
					return new Object[] { 57500, "PVM Tickets" };
				case 13051:
					return new Object[] { 800, "PVM Tickets" };
				case 3445:
					return new Object[] { 1000, "PVM Tickets" };
				case 3082:
				case 12926:
					return new Object[] { 750, "PVM Tickets" };
				case 17896:
				case 17897:
				case 17898:
					return new Object[] { 7500, "PVM Tickets" };
				case 6183:
					return new Object[] { 2500, "PVM Tickets" };
				case 3074:
					return new Object[] { 4000, "PVM Tickets" };
				case 11591:
					return new Object[] { 15000, "PVM Tickets" };
				case 20250:
					return new Object[] { 4000, "PVM Tickets" };
				case 15422:
				case 15423:
				case 15425:
					return new Object[] { 250, "PVM Tickets" };
				case 14603:
				case 14595:
				case 14605:
				case 14602:
					return new Object[] { 500, "PVM Tickets" };
				case 1050:
					return new Object[] { 1000, "PVM Tickets" };
				case 14012:
				case 14013:
				case 962:
					return new Object[] { 1000, "PVM Tickets" };
				case 14051:
				case 14052:
					return new Object[] { 5000, "PVM Tickets" };
				case 19023:
					return new Object[] { 1750, "PVM Tickets" };
				case 20061:
					return new Object[] { 2000, "PVM Tickets" };
				case 17291:
					return new Object[] { 2500, "PVM Tickets" };
				case 1053:
				case 1055:
				case 1057:
					return new Object[] { 1000, "PVM Tickets" };
				case 19002:
					return new Object[] { 1200, "PVM Tickets" };
				case 19935:
					return new Object[] { 2000, "PVM Tickets" };

				case 18999:
				case 12282:
				case 11614:
					return new Object[] { 50000_00, "PVM Tickets" };
				}
				return new Object[] { 25000, "PVM Tickets" };
			} else if (shop == STARTER_STORE) {
				switch (item) {
				case 989:
					return new Object[] { 300, "Starter Tickets" };
				case 1543:
					return new Object[] { 1000, "Starter Tickets" };
				case 15373:
					return new Object[] { 300, "Starter Tickets" };
				case 10835:
					return new Object[] { 2, "Starter Tickets" };
				case 19080:
					return new Object[] { 15, "Starter Tickets" };
				case 15332:
					return new Object[] { 150, "Starter Tickets" };
				case 17849:
					return new Object[] { 250, "Starter Tickets" };
				case 13258:
				case 13259:
				case 13256:
					return new Object[] { 200, "Starter Tickets" };
					
				case 19137:
				case 19138:
				case 19139:
					return new Object[] { 500, "Starter Tickets" };
				case 5130:
					return new Object[] { 750, "Starter Tickets" };
					
				case 19131:
				case 19132:
				case 19133:
				case 15398:
					return new Object[] { 800, "Starter Tickets" };
					
				case 18392:
					return new Object[] { 750, "Starter Tickets" };
				case 18865:
					return new Object[] { 1500, "Starter Tickets" };
				}
			} else if (shop == RAIDSTORE) {
				switch (item) {
				case 6927:
				case 6928:
				case 6929:
				case 6930:
				case 6931:
					return new Object[] { 10000, "Raid Points" };
				case 1648:
				case 1647:
				case 1855:
				case 2756:
				case 2757:
				case 2758:
				case 2759:
				case 2762:
				case 2763:
				case 2764:
					return new Object[] { 5000, "Raid Points" };
				}
				return new Object[] { 10_000, "Raid Points" };
				
			} else if (shop == BOSS_POINT_STORE) {
				switch (item) {
				case 14287:
					return new Object[] { 500000, "MOB Points" };
				case 5170:
					return new Object[] { 30000, "MOB Points" };
				case 3317:
					return new Object[] { 15000, "MOB Points" };
				case 15454:
					return new Object[] { 10000, "MOB Points" };
				case 13999:
					return new Object[] { 10000, "MOB Points" };
				case 3988:
					return new Object[] { 3000, "MOB Points" };
				case 6199:
					return new Object[] { 1000, "MOB Points" };
				case 19821:
					return new Object[] { 10000, "MOB Points" };
				case 5185:
					return new Object[] { 25000, "MOB Points" };
				case 19935:
					return new Object[] { 5000, "MOB Points" };
				case 19936:
					return new Object[] { 10000, "MOB Points" };
				case 16455:
					return new Object[] { 22500, "MOB Points" };
				case 6758:
					return new Object[] { 25000, "MOB Points" };
				case 6507:
					return new Object[] { 500, "MOB Points" };
				case 13997:
					return new Object[] { 10000, "MOB Points" };
				case 10205:
					return new Object[] { 15000, "MOB Points" };
				case 15374:
					return new Object[] { 10000, "MOB Points" };
				case 12426:
					return new Object[] { 7_500, "MOB Points" };
				case 5131:
					return new Object[] { 3_500, "MOB Points" };
				case 5130:
					return new Object[] { 2_500, "MOB Points" };
				case 19055:
					return new Object[] { 1_000, "MOB Points" };
				case 14808:
					return new Object[] { 15_000, "MOB Points" };
				case 14249:
					return new Object[] { 15_000, "MOB Points" };
				case 10168:
					return new Object[] { 15_000, "MOB Points" };
				case 19727:
					return new Object[] { 25_000, "MOB Points" };
				case 3971:
					return new Object[] { 150_000, "MOB Points" };
				}
				return new Object[] { 100, "MOB Points" };
			} else if (shop == LOYALTYPOINT_STORE) {
				switch (item) {
				case 16623:
				case 16635:
				case 16636:
				case 16621:
				case 16639:
				case 16644:
				case 16643:
				case 16641:
				case 15373:
				case 14808:	
					return new Object[] { 50000, "Loyalty Points" };
				case 15332:
				case 3912:
					return new Object[] { 1000, "Loyalty Points" };
				case 13095://hween
					return new Object[] { 150000, "Loyalty Points" };
				case 19156://shargian aura
					return new Object[] { 200000, "Loyalty Points" };
				case 3277://donator aura
					return new Object[] { 120000, "Loyalty Points" };
				case 5607:
				case 1419:
				case 1057:
				case 1055:
				case 1053:
				case 7673:
				case 7671:
				case 4780:
				case 3961:
					return new Object[] { 100000, "Loyalty Points" };
				case 9813:
				case 19042:
					return new Object[] { 200000, "Loyalty Points" };
				case 3988:
				case 3317:	
					return new Object[] { 25000, "Loyalty Points" };
				case 11924:
				case 11926:
				case 13045:
				case 18786:
					return new Object[] { 75000, "Loyalty Points" };
				case 16455:
				case 5170:
					return new Object[] { 150000, "Loyalty Points" };
				case 9470:
				case 4151:
				case 11730:
					return new Object[] { 10000, "Loyalty Points" };
				case 2572:
				case 6507:
					return new Object[] { 25000, "Loyalty Points" };
				}
				return new Object[] { 100, "Loyalty Points" };
			} else if (shop == DONATOR_STORE_1) {
				switch (item) {
				
				case 19081:
				case 19082:
				case 19083:
				case 19084:
				case 19085:
				case 19086:
					return new Object[] { 3000, "Donation Points" };
				case 7617:// Black hween
					return new Object[] { 500, "Donation Points" };
				case 4565:// Basketofeggs
				case 14046:// Pink Partyhat
				case 14049:// Pink Santa Hat
				case 14051:// Lime Santa Hat
				case 14052:// Lava Santa Hat
					return new Object[] { 1550, "Donation Points" };
				case 11862:// 3rd Mage
					return new Object[] { 900, "Donation Points" };
				case 10478:// 3rd Mage
					return new Object[] { 500, "Donation Points" };
					
				case 14044:// Black Partyhat
				case 14050:// Black Santa Hat
				case 11288:// Black hween
					return new Object[] { 1500, "Donation Points" };
				case 11858:// 3rd Melee
					return new Object[] { 1500, "Donation Points" };
					

				case 13997:
					return new Object[] { 190, "Donation Points" };
				case 15374:
					return new Object[] { 220, "Donation Points" };
				case 19886:
					return new Object[] { 105, "Donation Points" };
				case 5266:
					return new Object[] { 1200, "Donation Points" };

				case 19106:
					return new Object[] { 400, "Donation Points" };
				case 3277:
					return new Object[] { 500, "Donation Points" };
				case 4652:
					return new Object[] { 209, "Donation Points" };
				case 5185:
					return new Object[] { 100, "Donation Points" };
				case 14546:
					return new Object[] { 205, "Donation Points" };
				case 5170:
					return new Object[] { 205, "Donation Points" };
				case 19958:
					return new Object[] { 100, "Donation Points" };
				case 19821:
					return new Object[] { 205, "Donation Points" };
				case 17933:
					return new Object[] { 300, "Donation Points" };
				case 17932:
					return new Object[] { 300, "Donation Points" };
				case 18950:
					return new Object[] { 150, "Donation Points" };
				case 4742:
					return new Object[] { 750, "Donation Points" };
				case 11978:
					return new Object[] { 900, "Donation Points" };
					
				case 8699:
					return new Object[] { 125, "Donation Points" };
				case 6758:
					return new Object[] { 350, "Donation Points" };	
				case 6759:
					return new Object[] { 500, "Donation Points" };
					
				case 298:
					return new Object[] { 25, "Donation Points" };
				case 19670:
					return new Object[] { 1, "Donation Points" };
					
					
				}
				return new Object[] { 1000, "Donation Points" };
			} else if (shop == VOID_STORE) {
				switch (item) {
				case 11664:// Tax bag lol
					return new Object[] { 1000, "Pest Control Points" };
				case 8839:// Black hween
					return new Object[] { 1000, "Pest Control Points" };
				case 8840:// 3rd Melee
					return new Object[] { 1000, "Pest Control Points" };
				case 11684:// 3rd Melee
					return new Object[] { 1000, "Pest Control Points" };
				case 8842:// 3rd Melee
					return new Object[] { 1000, "Pest Control Points" };
				case 11683:// 3rd Melee
					return new Object[] { 1000, "Pest Control Points" };
				case 8024:// 3rd Melee
					return new Object[] { 2000, "Pest Control Points" };
				}
				return new Object[] { 100, "Pest Control Points" };
			} else if (shop == DONATOR_STORE_2) {
				switch (item) {
				}
				return new Object[] { 10000, "Donation Points" };
			} else if (shop == DONATOR_STORE_3) {
				switch (item) {
				case 18933:// Web Cloak
				case 18934:// Skele Mask
				case 3085:
				case 3086:
				case 3087:
					return new Object[] { 300, "Donation Points" };
				case 20054:
					return new Object[] { 500, "Donation Points" };
				case 14207:
					return new Object[] { 85, "Donation Points" };
				case 18946:// Sled
				case 18918:// Grain
				case 18917:// Scythe
				case 18916:// Basketofeggs
					return new Object[] { 300, "Donation Points" };
				case 3077:
					return new Object[] { 425, "Donation Points" };
				case 11532:
					return new Object[] { 325, "Donation Points" };
				case 18965:
					return new Object[] { 1325, "Donation Points" };
				case 3088:
					return new Object[] { 500, "Donation Points" };
				case 20557:
					return new Object[] { 300, "Donation Points" };
				case 18984:// Red Partyhat
				case 18973:// Yellow Partyhat
				case 18974:// Purple Partyhat
				case 18975:// Blue Partyhat
				case 18976:// White Partyhat
					return new Object[] { 125, "Donation Points" };
				case 18896:// 3rd Range
					return new Object[] { 500, "Donation Points" };
				case 14808:// 3rd Range
					return new Object[] { 50, "Donation Points" };
				case 18925:// 3rd Mage
					return new Object[] { 250, "Donation Points" };
				case 18906:// Attacker
					return new Object[] { 300, "Donation Points" };
				case 18943:// Black Partyhat
				case 18944:// Black Santa Hat
				case 18945:// Black hween
					return new Object[] { 130, "Donation Points" };
				case 14453:// American pernix piece
				case 14455:// American pernix piece
				case 14457:// American pernix piece
					return new Object[] { 100, "Donation Points" };
				case 11858:// 3rd Melee
					return new Object[] { 75, "Donation Points" };
				}
				return new Object[] { 100, "Donation Points" };
			} else if (shop == AGILITY_TICKET_STORE) {
				switch (item) {
				case 14936:
				case 14938:
					return new Object[] { 60, "agility tickets" };
				case 10941:
				case 10939:
				case 10940:
				case 10933:
					return new Object[] { 20, "agility tickets" };
				case 13661:
					return new Object[] { 100, "agility tickets" };
				}
			} else if (shop == TOKEN_STORE) {
				switch (item) {
				case 3648:
				case 3649:
				case 3650:
				case 3651:
				case 3652:
				case 3659:
					return new Object[] { 15000, "Colorful Tokens" };
				case 19935:
					return new Object[] { 40000, "Colorful Tokens" };
				case 19936:
					return new Object[] { 75000, "Colorful Tokens" };
				case 3912:
					return new Object[] { 2500, "Colorful Tokens" };

				}
			} else if (shop == SUIC_NUMBER_ONE_TOKEN_STORE) {
				switch (item) {
				case 19935:
					return new Object[] { 10000, "VIP Gems" };
				case 19936:
					return new Object[] { 20000, "VIP Gems" };
				case 7759:
					return new Object[] { 5000, "VIP Gems" };
				case 7760:
					return new Object[] { 5000, "VIP Gems" };
				case 7761:
					return new Object[] { 5000, "VIP Gems" };
				case 7762:
					
				case 13999:
					return new Object[] { 3000, "VIP Gems" };
					
				case 7682:
					return new Object[] { 3000, "VIP Gems" };
				case 7683:
					return new Object[] { 3000, "VIP Gems" };
				case 7684:
					return new Object[] { 3000, "VIP Gems" };
				case 7686:
					return new Object[] { 3000, "VIP Gems" };
				case 7687:
					return new Object[] { 3000, "VIP Gems" };
				case 7688:
					return new Object[] { 3000, "VIP Gems" };
				case 7763:
					return new Object[] { 5000, "VIP Gems" };
				case 7764:
					return new Object[] { 5000, "VIP Gems" };
				case 7765:
					return new Object[] { 5000, "VIP Gems" };

				}
				
			} else if (shop == IRONZONE_SHOP) {
				switch (item) {
				
				case 13047:
					return new Object[] { 2500, "Ironman tickets" };
				}
				return new Object[] { 10000, "Ironman tickets" };
				
			} else if (shop == LOL_SHOP) {
				switch (item) {
					case 17746:
					case 17748:
					case 17749:
					case 17751:
					case 17756:
						return new Object[]{150, "Arcade tokens"};
				}
				
			} else if (shop == GRAVEYARD_STORE) {
				switch (item) {
				case 18337:
					return new Object[] { 350, "zombie fragments" };
				case 20010:
				case 20011:
				case 20012:
				case 20009:
				case 20020:
				case 10551:
				case 1464:
				case 926:
				case 5131:
				case 4769:
					return new Object[] { 500, "zombie fragments" };
				case 10548:
				case 10549:
				case 10550:
				case 11846:
				case 11848:
				case 11850:
				case 11852:
				case 11854:
				case 6507:
				case 3957:
				case 18782:
					return new Object[] { 200, "zombie fragments" };
				case 11842:
				case 11844:
				case 7592:
				case 7593:
				case 7594:
				case 7595:
				case 7596:
				case 1543:
				case 6199:
				case 930:
					return new Object[] { 150, "zombie fragments" };
				case 5170:
				case 5154:	
				case 19886:
				case 15026:
				case 5184:
				case 6120:
				case 6119:
				case 8788:
					return new Object[] { 5000, "zombie fragments" };
				case 16579:
				case 4281:
				case 4282:
				case 4283:
				case 4284:
				case 4280:
				case 5163:
					return new Object[] { 10000, "zombie fragments" };
				case 18889:
				case 18890:
				case 18891:

				case 16137:
				case 13045:
				case 13047:
				case 3961:
				case 931:
				case 5195:
				case 15032:
					return new Object[] { 2500, "zombie fragments" };
				case 1:
				case 744:
					return new Object[] { 2, "zombie fragments" };
				}
				return new Object[] { 10000, "zombie fragments" };
			} else if (shop == TOKKUL_EXCHANGE_STORE) {
				switch (item) {
				case 438:
				case 436:
					return new Object[] { 10, "tokkul" };
				case 440:
					return new Object[] { 25, "tokkul" };
				case 453:
					return new Object[] { 30, "tokkul" };
				case 442:
					return new Object[] { 30, "tokkul" };
				case 444:
					return new Object[] { 40, "tokkul" };
				case 447:
					return new Object[] { 70, "tokkul" };
				case 449:
					return new Object[] { 120, "tokkul" };
				case 451:
					return new Object[] { 250, "tokkul" };
				case 1623:
					return new Object[] { 20, "tokkul" };
				case 1621:
					return new Object[] { 40, "tokkul" };
				case 1619:
					return new Object[] { 70, "tokkul" };
				case 1617:
					return new Object[] { 150, "tokkul" };
				case 1631:
					return new Object[] { 1600, "tokkul" };
				case 6571:
					return new Object[] { 50000, "tokkul" };
				case 11128:
					return new Object[] { 22000, "tokkul" };
				case 6522:
					return new Object[] { 20, "tokkul" };
				case 6524:
				case 6523:
				case 6526:
					return new Object[] { 5000, "tokkul" };
				case 6528:
				case 6568:
					return new Object[] { 800, "tokkul" };
				}
			} else if (shop == DUNGEONEERING_STORE) {
				switch (item) {
				case 18351:
				case 18349:
				case 18353:
				case 18357:
				case 18355:
				case 18359:
				case 18361:
				case 18363:
					return new Object[] { 150000, "Dungeoneering tokens" };
				case 16955:
				case 16425:
				case 16403:
					return new Object[] { 300000, "Dungeoneering tokens" };
				case 18335:
				case 18509:
					return new Object[] { 75000, "Dungeoneering tokens" };
				case 19709:
					return new Object[] { 500000, "Dungeoneering tokens" };
				}
			} else if (shop == TRIVIA_STORE) {
				switch (item) {
				case 19935:
					return new Object[] { 100, "Trivia Points" };
				case 19936:
					return new Object[] { 200, "Trivia Points" };
					
					
					
					
				case 16455:
					return new Object[] { 400, "Trivia Points" };
				case 18400:
					return new Object[] { 250, "Trivia Points" };
				case 1855:
					return new Object[] { 250, "Trivia Points" };
				case 18983:
					return new Object[] { 500, "Trivia Points" };
				case 4566:
					return new Object[] { 500, "Trivia Points" };
				case 15255:
					return new Object[] { 750, "Trivia Points" };
				case 15257:
					return new Object[] { 750, "Trivia Points" };
				case 15256:
					return new Object[] { 750, "Trivia Points" };
				case 15258:
					return new Object[] { 750, "Trivia Points" };
				case 19048:
					return new Object[] { 750, "Trivia Points" };
				case 19047:
					return new Object[] { 750, "Trivia Points" };
				case 19049:
					return new Object[] { 750, "Trivia Points" };
				case 19054:
					return new Object[] { 750, "Trivia Points" };
				case 298:
					return new Object[] { 100, "Trivia Points" };
				case 2764:
					return new Object[] { 250, "Trivia Points" };
					
					
					
					
					
					
					
					
				case 15648:
					return new Object[] { 10, "Trivia Points" };
					
				case 6483:
					return new Object[] { 300, "Trivia Points" };
					
				case 6484:
					
				case 6486:
					return new Object[] { 250, "Trivia Points" };
					
					
				case 6445:
				case 6447:
				case 6446:
				case 6443:
				case 6444:
					return new Object[] { 250, "Trivia Points" };
					
				case 2771:
				case 2867:
				case 2868:
				case 2870:
				case 2772:
				case 2869:
					return new Object[] { 500, "Trivia Points" };
					
				case 15000:
				case 1419:
					
					return new Object[] { 1000, "Trivia Points" };
					
					
					
					
					
					
					
					
				}
				return new Object[] { 100, "Trivia Points" };
				
			} else if (shop == CUSTOMSLAYER_POINT_STORE) {
				switch (item) {

				case 6199:
					return new Object[] { 500, "Slayer points" };
				case 19935:
					return new Object[] { 1000, "Slayer points" };
				case 19936:
					return new Object[] { 1750, "Slayer points" };
				case 19890:
					return new Object[] { 1000, "Slayer points" };
				case 3322:
				case 3315:
				case 3318:
					return new Object[] { 2000, "Slayer points" };
				case 3313:
				case 3314:
				case 3312:
					return new Object[] { 2000, "Slayer points" };
					
				case 3810:
				case 3813:
				case 3814:
					return new Object[] { 5000, "Slayer points" };
				case 3949:
				case 3950:
				case 3952:
					return new Object[] { 2500, "Slayer points" };
					
				case 10205:
					return new Object[] { 3000, "Slayer points" };	
						
				case 14033:
					return new Object[] { 5000, "Slayer points" };	
					
				case 19087:
				case 19103:
				case 19106:
					return new Object[] { 2500, "Slayer points" };
				case 3912:
					return new Object[] { 500, "Slayer points" };
				case 15374:
					return new Object[] { 2500, "Slayer points" };
				}

			} else if (shop == PRESTIGE_STORE) {
				switch (item) {
				case 14047:
					return new Object[] { 10000, "Prestige Points" };
				case 19935:
					return new Object[] { 7500, "Prestige points" };
				case 3072:
					return new Object[] { 60, "Prestige points" };
				case 3666:
					return new Object[] { 200, "Prestige points" };
				case 3286:
					return new Object[] { 50, "Prestige points" };
				case 19007:
					return new Object[] { 30, "Prestige points" };
				case 12428:
					return new Object[] { 45, "Prestige points" };
				case 11588:
				case 11587:
				case 11589:
				case 11591:
					return new Object[] { 75, "Prestige points" };
				case 15373:
					return new Object[] { 20, "Prestige points" };
				case 6183:
					return new Object[] { 45, "Prestige points" };
				case 3444:
					return new Object[] { 70, "Prestige points" };
				case 10408:
				case 10410:
					return new Object[] { 15, "Prestige points" };
				case 10404:
				case 10406:
					return new Object[] { 15, "Prestige points" };
				case 20000:
				case 20001:
				case 20002:
					return new Object[] { 5, "Prestige points" };
				}
			} else if (shop == SLAYER_STORE) {
				switch (item) {

				case 6199:
					return new Object[] { 500, "Slayer points" };
				case 19935:
					return new Object[] { 1000, "Slayer points" };
				case 19936:
					return new Object[] { 1750, "Slayer points" };
				case 19890:
					return new Object[] { 1000, "Slayer points" };
				case 3322:
				case 3315:
				case 3318:
					return new Object[] { 2000, "Slayer points" };
				case 3313:
				case 3314:
				case 3312:
					return new Object[] { 3000, "Slayer points" };
					
				case 3810:
				case 3813:
				case 3814:
					return new Object[] { 5000, "Slayer points" };
				case 3949:
				case 3950:
				case 3952:
					return new Object[] { 5000, "Slayer points" };
					
				case 10205:
					return new Object[] { 3000, "Slayer points" };	
						
				case 14033:
					return new Object[] { 5000, "Slayer points" };	
					
				case 19087:
				case 19103:
				case 19106:
					return new Object[] { 2500, "Slayer points" };
				case 3912:
					return new Object[] { 500, "Slayer points" };
				case 15374:
					return new Object[] { 2500, "Slayer points" };
				}

			}
			return null;
		}
	}

	/**
	 * The shop interface id.
	 */
	public static final int INTERFACE_ID = 3824;

	/**
	 * The starting interface child id of items.
	 */
	public static final int ITEM_CHILD_ID = 3900;

	/**
	 * The interface child id of the shop's name.
	 */
	public static final int NAME_INTERFACE_CHILD_ID = 3901;

	/**
	 * The inventory interface id, used to set the items right click values to
	 * 'sell'.
	 */
	public static final int INVENTORY_INTERFACE_ID = 3823;

	/*
	 * Declared shops
	 */

	public static final int DONATOR_STORE_1 = 48;
	public static final int DONATOR_STORE_2 = 49;
	public static final int DONATOR_STORE_3 = 54;

	public static final int TRIVIA_STORE = 50;

	public static final int GENERAL_STORE = 12;
	public static final int RECIPE_FOR_DISASTER_STORE = 36;

	private static final int VOTING_REWARDS_STORE = 27;
	private static final int PKING_REWARDS_STORE = 26;
	private static final int ENERGY_FRAGMENT_STORE = 33;
	private static final int AGILITY_TICKET_STORE = 39;
	private static final int GRAVEYARD_STORE = 42;
	private static final int IRONZONE_SHOP = 130;
	private static final int LOL_SHOP = 131;
	private static final int TOKKUL_EXCHANGE_STORE = 43;
	private static final int DONORTICKET_SHOP = 51;
	private static final int BTC_SHOP = 134;
	private static final int ETH_SHOP = 135;
	private static final int DOGE_SHOP = 136;
	private static final int AFKFISHING_SHOP = 137;
	private static final int SANTAS_STORE = 57;
	private static final int SKILLING_STORE = 59;
	private static final int STARTER_STORE = 58;
	private static final int SKILLCAPE_STORE_1 = 8;
	private static final int SKILLCAPE_STORE_2 = 9;
	private static final int SKILLCAPE_STORE_3 = 10;
	private static final int GAMBLING_STORE = 41;
	private static final int DUNGEONEERING_STORE = 44;
	private static final int PRESTIGE_STORE = 46;
	public static final int BOSS_POINT_STORE = 92;
	public static final int RAIDSTORE = 124;
	private static final int SLAYER_STORE = 47;
	public static final int STARDUST_STORE = 55;
	private static final int LOYALTYPOINT_STORE = 205;
	private static final int BLOODBAG_STORE = 132;
	private static final int HWEEN_STORE = 133;
	private static final int BLOOD_MONEY_STORE = 100;
	private static final int BLOOD_MONEY_STORE2 = 101;
	private static final int VOID_STORE = 115;
	private static final int TOKEN_STORE = 116;
	private static final int CUSTOMSLAYER_POINT_STORE = 117;
	private static final int SUIC_NUMBER_ONE_TOKEN_STORE = 118;
	private static final int SELL_FOR_TAXBAGS_SHOP = 119;
	private static final int DARKLORD_TOKEN_SHOP = 120;
	private static final int TAX_BAG_SHOP = 121;
	private static final int IRONMAN_SHOP = 125;
	private static final int BLOODSLAYER_STORE = 122;
	private static final int RAIDS_FISHING_STORE = 123;
}
