package com.arlania.net.packet;

import com.arlania.model.container.impl.Equipment;
import com.arlania.net.packet.impl.ButtonClickPacketListener;
import com.arlania.net.packet.impl.ChangeAppearancePacketListener;
import com.arlania.net.packet.impl.ChangeRelationStatusPacketListener;
import com.arlania.net.packet.impl.ChatPacketListener;
import com.arlania.net.packet.impl.ClickTextMenuPacketListener;
import com.arlania.net.packet.impl.CloseInterfacePacketListener;
import com.arlania.net.packet.impl.CommandPacketListener;
import com.arlania.net.packet.impl.DialoguePacketListener;
import com.arlania.net.packet.impl.DropItemPacketListener;
import com.arlania.net.packet.impl.DuelAcceptancePacketListener;
import com.arlania.net.packet.impl.DungeoneeringPartyInvitatationPacketListener;
import com.arlania.net.packet.impl.EnterInputPacketListener;
import com.arlania.net.packet.impl.EquipPacketListener;
import com.arlania.net.packet.impl.ExamineItemPacketListener;
import com.arlania.net.packet.impl.ExamineNpcPacketListener;
import com.arlania.net.packet.impl.FinalizedMapRegionChangePacketListener;
import com.arlania.net.packet.impl.FollowPlayerPacketListener;
import com.arlania.net.packet.impl.GESelectItemPacketListener;
import com.arlania.net.packet.impl.HeightCheckPacketListener;
import com.arlania.net.packet.impl.IdleLogoutPacketListener;
import com.arlania.net.packet.impl.ItemActionPacketListener;
import com.arlania.net.packet.impl.ItemColorCustomization;
import com.arlania.net.packet.impl.ItemContainerActionPacketListener;
import com.arlania.net.packet.impl.KeycodeListener;
import com.arlania.net.packet.impl.MagicOnItemsPacketListener;
import com.arlania.net.packet.impl.MagicOnPlayerPacketListener;
import com.arlania.net.packet.impl.MovementPacketListener;
import com.arlania.net.packet.impl.NPCOptionPacketListener;
import com.arlania.net.packet.impl.ObjectActionPacketListener;
import com.arlania.net.packet.impl.PickupItemPacketListener;
import com.arlania.net.packet.impl.PlayerOptionPacketListener;
import com.arlania.net.packet.impl.PlayerRelationPacketListener;
import com.arlania.net.packet.impl.PrestigeSkillPacketListener;
import com.arlania.net.packet.impl.RegionChangePacketListener;
import com.arlania.net.packet.impl.SendClanChatMessagePacketListener;
import com.arlania.net.packet.impl.SilencedPacketListener;
import com.arlania.net.packet.impl.SwitchItemSlotPacketListener;
import com.arlania.net.packet.impl.TradeInvitationPacketListener;
import com.arlania.net.packet.impl.UseItemPacketListener;
import com.arlania.net.packet.impl.ViewShopPacketListener;
import com.arlania.net.packet.impl.WithdrawMoneyFromPouchPacketListener;
import com.arlania.world.content.keepsake.KeepSake;
import com.arlania.world.entity.impl.player.Player;

public class PacketConstants {

	public static final PacketListener[] PACKETS = new PacketListener[257];

	static {
		for(int i = 0; i < PACKETS.length; i++)
			PACKETS[i] = new SilencedPacketListener();
		PACKETS[4] = PACKETS[230] = new ChatPacketListener();
		PACKETS[EquipPacketListener.OPCODE] = new EquipPacketListener();
		PACKETS[87] = new DropItemPacketListener();
		PACKETS[103] = new CommandPacketListener();	
		PACKETS[121] = new FinalizedMapRegionChangePacketListener();	
		PACKETS[130] = new CloseInterfacePacketListener();
		PACKETS[ButtonClickPacketListener.OPCODE] = new ButtonClickPacketListener();
		PACKETS[2] = new ExamineItemPacketListener();
		PACKETS[6] = new ExamineNpcPacketListener();
		PACKETS[5] = new SendClanChatMessagePacketListener();
		PACKETS[7] = new WithdrawMoneyFromPouchPacketListener();
		PACKETS[8] = new ChangeRelationStatusPacketListener();
		PACKETS[11] = new ChangeAppearancePacketListener();
		PACKETS[202] = new IdleLogoutPacketListener();
		PACKETS[131] = new NPCOptionPacketListener();
		PACKETS[17] = new NPCOptionPacketListener();
		PACKETS[18] = new NPCOptionPacketListener();
		PACKETS[21] = new NPCOptionPacketListener();
		PACKETS[210] = new RegionChangePacketListener();
		PACKETS[214] = new SwitchItemSlotPacketListener();
		PACKETS[236] = new PickupItemPacketListener();
		PACKETS[73] = new FollowPlayerPacketListener();
		PACKETS[NPCOptionPacketListener.ATTACK_NPC] = PACKETS[NPCOptionPacketListener.FIRST_CLICK_OPCODE] =
				new NPCOptionPacketListener();
		PACKETS[EnterInputPacketListener.ENTER_SYNTAX_OPCODE] =
				PACKETS[EnterInputPacketListener.ENTER_AMOUNT_OPCODE] = new EnterInputPacketListener();
		PACKETS[UseItemPacketListener.ITEM_ON_GROUND_ITEM] = PACKETS[UseItemPacketListener.ITEM_ON_ITEM] = 
				PACKETS[UseItemPacketListener.ITEM_ON_NPC] = PACKETS[UseItemPacketListener.ITEM_ON_OBJECT] = 
				PACKETS[UseItemPacketListener.ITEM_ON_PLAYER] = new UseItemPacketListener();
		PACKETS[UseItemPacketListener.USE_ITEM] = new UseItemPacketListener();
		PACKETS[TradeInvitationPacketListener.TRADE_OPCODE] = new TradeInvitationPacketListener();
		PACKETS[TradeInvitationPacketListener.CHATBOX_TRADE_OPCODE] = new TradeInvitationPacketListener();
		PACKETS[DialoguePacketListener.DIALOGUE_OPCODE] = new DialoguePacketListener();
		PACKETS[PlayerRelationPacketListener.ADD_FRIEND_OPCODE] = new PlayerRelationPacketListener();
		PACKETS[PlayerRelationPacketListener.REMOVE_FRIEND_OPCODE] = new PlayerRelationPacketListener();
		PACKETS[PlayerRelationPacketListener.ADD_IGNORE_OPCODE] = new PlayerRelationPacketListener();
		PACKETS[PlayerRelationPacketListener.REMOVE_IGNORE_OPCODE] = new PlayerRelationPacketListener();
		PACKETS[PlayerRelationPacketListener.SEND_PM_OPCODE] = new PlayerRelationPacketListener();
		PACKETS[MovementPacketListener.COMMAND_MOVEMENT_OPCODE] = new MovementPacketListener();
		PACKETS[MovementPacketListener.GAME_MOVEMENT_OPCODE] = new MovementPacketListener();
		PACKETS[MovementPacketListener.MINIMAP_MOVEMENT_OPCODE] = new MovementPacketListener();
		PACKETS[ObjectActionPacketListener.FIRST_CLICK] = PACKETS[ObjectActionPacketListener.SECOND_CLICK] =
				PACKETS[ObjectActionPacketListener.THIRD_CLICK] = PACKETS[ObjectActionPacketListener.FOURTH_CLICK] =
				PACKETS[ObjectActionPacketListener.FIFTH_CLICK] = new ObjectActionPacketListener();
		PACKETS[ItemContainerActionPacketListener.FIRST_ITEM_ACTION_OPCODE] = PACKETS[ItemContainerActionPacketListener.SECOND_ITEM_ACTION_OPCODE] = 
				PACKETS[ItemContainerActionPacketListener.THIRD_ITEM_ACTION_OPCODE] = PACKETS[ItemContainerActionPacketListener.FOURTH_ITEM_ACTION_OPCODE] =
				PACKETS[ItemContainerActionPacketListener.FIFTH_ITEM_ACTION_OPCODE] = PACKETS[ItemContainerActionPacketListener.SIXTH_ITEM_ACTION_OPCODE] = new ItemContainerActionPacketListener();
		PACKETS[ItemActionPacketListener.SECOND_ITEM_ACTION_OPCODE] = PACKETS[ItemActionPacketListener.THIRD_ITEM_ACTION_OPCODE] = PACKETS[ItemActionPacketListener.FIRST_ITEM_ACTION_OPCODE] = new ItemActionPacketListener();
		PACKETS[MagicOnItemsPacketListener.MAGIC_ON_ITEMS] = new MagicOnItemsPacketListener();
		PACKETS[MagicOnItemsPacketListener.MAGIC_ON_GROUNDITEMS] = new MagicOnItemsPacketListener();
		PACKETS[249] = new MagicOnPlayerPacketListener();
		PACKETS[153] = new PlayerOptionPacketListener();
		PACKETS[DuelAcceptancePacketListener.OPCODE] = new DuelAcceptancePacketListener();
		PACKETS[12] = new DungeoneeringPartyInvitatationPacketListener();
		PACKETS[204] = new GESelectItemPacketListener();
		PACKETS[222] = new ClickTextMenuPacketListener();
		PACKETS[223] = new PrestigeSkillPacketListener();
		PACKETS[229] = new HeightCheckPacketListener();
		PACKETS[186] = new ItemColorCustomization();
		PACKETS[184] = new KeycodeListener();
		PACKETS[182] = new ShiftBankListener();
		//PACKETS[190] = new SantaHatCustomization();
		PACKETS[ViewShopPacketListener.VIEW_SHOP_OPCODE] = new ViewShopPacketListener();
		PACKETS[GambleInvititationPacketListener.GAMBLE_OPCODE] = new GambleInvititationPacketListener();
		PACKETS[GambleInvititationPacketListener.CHATBOX_GAMBLE_OPCODE] = new GambleInvititationPacketListener();
		//PACKETS[193] = new GambleInvititationPacketListener();
		PACKETS[201] = new ReadItemDataFromClient();
		PACKETS[203] = new NpcConfigActionPacketListener();
		PACKETS[205] = new TransformToNpcPacketListener();
		PACKETS[194] = new AddGambleItemPacketListener();
		PACKETS[195] = new RouletteSpinEndPacketListener();
		PACKETS[199] = new ScratchCardEndPacketListener();
		PACKETS[211] = new TitleCustomizerPacketListener();
        PACKETS[225] = new WheelOfFortuneEndListener();
        PACKETS[239] = new BoxSpinnerEndPacketListener();
        PACKETS[243] = new ClientSettingsChangePacketListener();
		PACKETS[140] = new PacketListener() {

			@Override
			public void handleMessage(Player player, Packet packet) {
				if(packet.getOpcode() == 140) {
					int interfaceId = packet.readLEShort();
					int id = packet.readShortA();
					int slot = packet.readShortA();
					if(-31915 == interfaceId) {
						player.getPlayerOwnedShopManager().handleWithdraw(slot, id, 5);
					} else if(32621 == interfaceId) {
						player.getPlayerOwnedShopManager().handleBuy(slot, id, 5);
					}
				}
			}
			
		};
		//System.out.println("Read packet");
		PACKETS[141] = new PacketListener() {

			@Override
			public void handleMessage(Player player, Packet packet) {
				if(packet.getOpcode() == 141) {
					int interfaceId = packet.readLEShort();
					int id = packet.readShortA();
					int slot = packet.readShortA();
					
					//System.out.println("[DEBUG] Interface id: "+interfaceId+" Id: "+id+", Slot: " + slot);

					switch(interfaceId) {

						case 2392: //Preset 1
						case 2393: //Preset 2
						case 2394: //Preset 3
						case 2395: //Preset 4
						case 2396: //Preset 5
							int index = interfaceId - 2392;
							//System.out.println("index: " + index);
							switch(slot) {
								case 0:
									KeepSake.loadPreset(player, index);
									break;
								case 1:
									KeepSake.savePreset(player, index, false, "");
									break;
								case 2:
									KeepSake.renamePreset(player, index, true, "");
									break;
							}
							break;
					
						case 2367:
							if(slot == 0)
								KeepSake.displayItems(player, Equipment.HEAD_SLOT);
							else if(slot == 1)
								KeepSake.resetSlot(player, Equipment.HEAD_SLOT);
							break;
						case 2369:
							if(slot == 0)
								KeepSake.displayItems(player, Equipment.CAPE_SLOT);
							else if(slot == 1)
								KeepSake.resetSlot(player, Equipment.CAPE_SLOT);
							break;
						case 2371:
							if(slot == 0)
								KeepSake.displayItems(player, Equipment.AMULET_SLOT);
							else if(slot == 1)
								KeepSake.resetSlot(player, Equipment.AMULET_SLOT);
							break;
						case 2373:
							player.getPacketSender().sendMessage("@red@You are not able to override the arrow slot!");
							break;
						case 2375:
							if(slot == 0)
								KeepSake.displayItems(player, Equipment.WEAPON_SLOT);
							else if(slot == 1)
								KeepSake.resetSlot(player, Equipment.WEAPON_SLOT);
							break;
						case 2377:
							if(slot == 0)
								KeepSake.displayItems(player, Equipment.BODY_SLOT);
							else if(slot == 1)
								KeepSake.resetSlot(player, Equipment.BODY_SLOT);
							break;
						case 2379:
							if(slot == 0)
								KeepSake.displayItems(player, Equipment.SHIELD_SLOT);
							else if(slot == 1)
								KeepSake.resetSlot(player, Equipment.SHIELD_SLOT);
							break;
						case 2381:
							if(slot == 0)
								KeepSake.displayItems(player, Equipment.LEG_SLOT);
							else if(slot == 1)
								KeepSake.resetSlot(player, Equipment.LEG_SLOT);
							break;
						case 2383:
							if(slot == 0)
								KeepSake.displayItems(player, Equipment.HANDS_SLOT);
							else if(slot == 1)
								KeepSake.resetSlot(player, Equipment.HANDS_SLOT);
							break;
						case 2385:
							if(slot == 0)
								KeepSake.displayItems(player, Equipment.FEET_SLOT);
							else if(slot == 1)
								KeepSake.resetSlot(player, Equipment.FEET_SLOT);
							break;
						case 2387:
							player.getPacketSender().sendMessage("@red@You are not able to override the ring slot!");
							break;
						case 2397:
							if(slot == 0)
								KeepSake.displayItems(player, Equipment.AURA_SLOT);
							else if(slot == 1)
								KeepSake.resetSlot(player, Equipment.AURA_SLOT);
							break;
					}
					
					if(-31915 == interfaceId) {
						player.getPlayerOwnedShopManager().handleWithdraw(slot, id, 10);
					} else if(32621 == interfaceId) {
						player.getPlayerOwnedShopManager().handleBuy(slot, id, 10);
					}
				}
			}
			
		};
		PACKETS[127] = new PacketListener() {

			@Override
			public void handleMessage(Player player, Packet packet) {
				if(packet.getOpcode() == 127) {
					String text = packet.readString();
					int index = text.indexOf(",");
					int id = Integer.parseInt(text.substring(0, index));
					String string = text.substring(index + 1);
					
					if(id == 32611) {
						player.getPlayerOwnedShopManager().updateFilter(string);
					}
				}
			}
			
		};
	}
	
	/**
	 * The size of packets sent from client to the server 
	 * used to decode them.
	 */
	public final static int[] MESSAGE_SIZES = {
			0, 0, 2, 1, -1, -1, 2, 8, 4, 4, //0
	        4, -1, -1, -1, 8, 0, 6, 2, 2, 0,  //10
	        0, 2, 0, 6, 0, 12, 0, 0, 0, 0, //20
	        9, 0, 0, 0, 0, 8, 4, 0, 0, 2,  //30
	        2, 6, 0, 6, 0, -1, 0, 0, 0, 1, //40
	        0, 0, 0, 12, 0, 0, 0, 8, 8, 0, //50
	        -1, 8, 0, 0, 0, 0, 0, 0, 0, 0,  //60
	        6, 0, 2, 2, 8, 6, 0, -1, 0, 6, //70
	        -1, 0, 0, 0, 0, 1, 4, 6, 0, 0,  //80
	        0, 0, 0, 0, 0, 3, 0, 0, -1, 0, //90
	        0, 13, 0, -1, 0, 0, 0, 0, 0, 0,//100
	        0, 0, 0, 0, 0, 0, 0, 6, 0, 0,  //110
	        1, 0, 6, 0, 0, 0, -1, -1, 2, 6, //120
	        0, 4, 6, 8, 0, 6, 0, 0, 6, 2,  //130
	        6, 6, 0, 0, 0, 6, 0, 0, 0, 0,  //140
	        0, 0, 1, 2, 0, 2, 6, 0, 0, 0,  //150
	        0, 0, 0, 0, -1, -1, 0, 0, 0, 0,//160
	        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,  //170
	        0, 8, 3, 3, 2, 2, 19, 0, 8, 1,  //180
	        2, 2, 12, 2, 2, 2, 0, 0, 0, 1, //190
	        2, 2, 0, 10, 2, 2, 0, 0, 4, 0,  //200
	        4, -1, 0, -1, 7, 8, 0, 0, 10, 0, //210
	        0, 0, 3, 2, 0, 1, -1, 0, 6, 1, //220
	        1, 0, 0, 0, 6, 0, 6, 8, 1, 2,  //230
	        0, 4, 0, 26, 0, -1, -1, 0, -1, 4,//240
	        0, 0, 6, 6, 0, 0           //250
		};
}
