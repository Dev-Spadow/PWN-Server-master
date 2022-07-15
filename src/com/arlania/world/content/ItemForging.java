package com.arlania.world.content;

import com.arlania.model.Graphic;
import com.arlania.model.Item;
import com.arlania.model.Skill;
import com.arlania.model.definitions.ItemDefinition;
import com.arlania.util.Misc;
import com.arlania.world.content.Achievements.AchievementData;
import com.arlania.world.entity.impl.player.Player;

/**
 * Handles item forging, such as Spirit shields making etc.
 * @author Gabriel Hannason and Samy
 */
public class ItemForging {

	public static void forgeItem(final Player p, final int item1, final int item2) {
		if(item1 == item2)
			return;
		ItemForgeData data = ItemForgeData.getDataForItems(item1, item2);
		if(data == null || !p.getInventory().contains(item1) || !p.getInventory().contains(item2))
			return;
		if(!p.getClickDelay().elapsed(3000)) 
			return;
		if(p.getInterfaceId() > 0) {
			p.getPacketSender().sendMessage("Please close the interface you have open before doing this.");
			return;
		}
		Skill skill = Skill.forId(data.skillRequirement[0]);
		int skillReq = data.skillRequirement[1];
		if(p.getSkillManager().getCurrentLevel(skill) >= skillReq) {
			for(Item reqItem : data.requiredItems) {
				if(!p.getInventory().contains(reqItem.getId()) || p.getInventory().getAmount(reqItem.getId()) < reqItem.getAmount()) {
					p.getPacketSender().sendMessage("You need "+Misc.anOrA(reqItem.getDefinition().getName())+" "+reqItem.getDefinition().getName()+" to forge a new item.");
					return;
				}
			}
			p.performGraphic(new Graphic(2010));
			for(Item reqItem : data.requiredItems) {
				if(reqItem.getId() == 1755 || reqItem.getId() == 1595)
					continue;
				p.getInventory().delete(reqItem);
			}
			p.getInventory().add(data.product, true);
			final String itemName = Misc.formatText(ItemDefinition.forId(data.product.getId()).getName().toLowerCase());
			p.getPacketSender().sendMessage("You make "+Misc.anOrA(itemName)+" "+itemName+".");
			p.getClickDelay().reset();
			p.getSkillManager().addExperience(skill, data.skillRequirement[2]);
			if(data == ItemForgeData.ARMADYL_GODSWORD || data == ItemForgeData.BANDOS_GODSWORD || data == ItemForgeData.ZAMORAK_GODSWORD || data == ItemForgeData.SARADOMIN_GODSWORD) {

			}
			return;
		} else {
			p.getPacketSender().sendMessage("You need "+Misc.anOrA(skill.getFormatName())+" "+skill.getFormatName()+" level of at least "+skillReq+" to forge this item.");
			return;
		}
	}

	/**
	 ** The enum holding all our data
	 */
	private static enum ItemForgeData {
		BLESSED_SPIRIT_SHIELD(new Item[] {new Item(13754), new Item(13734)}, new Item(13736), new int[] {1, -1, 0}),
		FORGER(new Item[] {new Item(9906, 1), new Item(11789, 5)}, new Item(16139), new int[] {1, -1, 0}),
		SPECTRAL_SPIRIT_SHIELD(new Item[] {new Item(13752), new Item(13736)}, new Item(13744), new int[] {13, 85, 40000}),
		ARCANE_SPIRIT_SHIELD(new Item[] {new Item(13746), new Item(13736)}, new Item(13738), new int[] {13, 85, 40000}),
		ELYSIAN_SPIRIT_SHIELD(new Item[] {new Item(13750), new Item(13736)}, new Item(13742), new int[] {13, 85, 40000}),
		DIVINE_SPIRIT_SHIELD(new Item[] {new Item(13748), new Item(13736)}, new Item(13740), new int[] {13, 85, 40000}),

		DRAGON_SQ_SHIELD(new Item[] {new Item(2368), new Item(2366)}, new Item(1187), new int[] {13, 60, 10000}),
		DRAGON_PLATEBY(new Item[] {new Item(14472), new Item(14474), new Item(14476)}, new Item(14479), new int[] {13, 92, 120000}),
		DRAGONFIRE_SHIELD(new Item[] {new Item(11286), new Item(1540)}, new Item(11283), new int[] {13, 82, 36000}),

		CRYSTAL_KEY(new Item[] {new Item(985), new Item(987)}, new Item(989), new int[] {1, -1, 0}),

		GODSWORD_BLADE(new Item[] {new Item(11710), new Item(11712), new Item(11714)}, new Item(11690), new int[] {1, -1, 0}),
		ARMADYL_GODSWORD(new Item[] {new Item(11702), new Item(11690)}, new Item(11694), new int[] {1, -1, 0}),
		BANDOS_GODSWORD(new Item[] {new Item(11704), new Item(11690)}, new Item(11696), new int[] {1, -1, 0}),
		SARADOMIN_GODSWORD(new Item[] {new Item(11706), new Item(11690)}, new Item(11698), new int[] {1, -1, 0}),
		ZAMORAK_GODSWORD(new Item[] {new Item(11708), new Item(11690)}, new Item(11700), new int[] {1, -1, 0}),

		AMULET_OF_FURY(new Item[] {new Item(1755), new Item(6573), new Item(1595)}, new Item(6585), new int[] {12, 90, 100000}),

		AMULET_OF_FURY_ORNAMENT(new Item[] {new Item(19333), new Item(6585)}, new Item(19335), new int[] {1, -1, 0}),
		DRAGON_FULL_HELM_SPIKE(new Item[] {new Item(19354), new Item(11335)}, new Item(19341), new int[] {1, -1, 0}),
		DRAGON_PLATELEGS_SPIKE(new Item[] {new Item(19356), new Item(4087)}, new Item(19343), new int[] {1, -1, 0}),
		DRAGON_PLATEBODY_SPIKE(new Item[] {new Item(19358), new Item(14479)}, new Item(19342), new int[] {1, -1, 0}),
		DRAGON_SQUARE_SHIELD_SPIKE(new Item[] {new Item(19360), new Item(1187)}, new Item(19345), new int[] {1, -1, 0}),
		DRAGON_FULL_HELM_GOLD(new Item[] {new Item(19346), new Item(11335)}, new Item(19336), new int[] {1, -1, 0}),
		DRAGON_PLATELEGS_GOLD(new Item[] {new Item(19348), new Item(4087)}, new Item(19338), new int[] {1, -1, 0}),
		DRAGON_PLATEBODY_GOLD(new Item[] {new Item(19350), new Item(14479)}, new Item(19337), new int[] {1, -1, 0}),
		DRAGON_SQUARE_SHIELD_GOLD(new Item[] {new Item(19352), new Item(1187)}, new Item(19340), new int[] {1, -1, 0}),

		FULL_SLAYER_HELMET(new Item[] {new Item(13263), new Item(15490), new Item(15488)}, new Item(15492), new int[] {18, 75, 0}),
		
		//TIER 1 UPGRADES
				ROYAL_PERNIX_LEGS(new Item[] {new Item(19139),new Item(13211, 1)}, new Item(13210), new int[] {1, -1, 0}),
				ROYAL_PERNIX_HOOD(new Item[] {new Item(19137),new Item(13211, 1)}, new Item(13208), new int[] {1, -1, 0}),
				ROYAL_PERNIX_BODY(new Item[] {new Item(19138),new Item(13211, 1)}, new Item(13209), new int[] {1, -1, 0}),
				FROST_DEMON_HELMET(new Item[] {new Item(19131),new Item(13211, 1)}, new Item(13221), new int[] {1, -1, 0}),
				FROST_DEMON_BODY(new Item[] {new Item(19132),new Item(13211, 1)}, new Item(13222), new int[] {1, -1, 0}),
				FROST_DEMON_LEGS(new Item[] {new Item(19133),new Item(13211, 1)}, new Item(13223), new int[] {1, -1, 0}),
				SIRENIC_WINGS(new Item[] {new Item(922),new Item(13211, 1)}, new Item(13226), new int[] {1, -1, 0}),
				SIRENIC_BODY(new Item[] {new Item(18941),new Item(13211, 1)}, new Item(13225), new int[] {1, -1, 0}),
				SIRENIC_LEGS(new Item[] {new Item(18940),new Item(13211, 1)}, new Item(13224), new int[] {1, -1, 0}),
		//END TIER 1 UPGRADES
				
		//TIER 2 UPGRADES
				HERCULES_HELMET(new Item[] {new Item(2749),new Item(13212, 1)}, new Item(13251), new int[] {1, -1, 0}),
				HERCULES_FIGHTERBODY(new Item[] {new Item(2750),new Item(13212, 1)}, new Item(13252), new int[] {1, -1, 0}),
				HERCULES_FIGHTERLEGS(new Item[] {new Item(2751),new Item(13212, 1)}, new Item(13253), new int[] {1, -1, 0}),
				HERCULES_CAPE(new Item[] {new Item(2752),new Item(13212, 1)}, new Item(13255), new int[] {1, -1, 0}),
				HERCULES_SHIELD(new Item[] {new Item(2753),new Item(13212, 1)}, new Item(13254), new int[] {1, -1, 0}),
				HERCULES_GLOVES(new Item[] {new Item(2754),new Item(13212, 1)}, new Item(13260), new int[] {1, -1, 0}),
				HERCULES_BOOTS(new Item[] {new Item(13261),new Item(13212, 1)}, new Item(13261), new int[] {1, -1, 0}),
				
				BLESSED_HOOD(new Item[] {new Item(19721),new Item(13212, 1)}, new Item(13692), new int[] {1, -1, 0}),
				BLESSED_BODY(new Item[] {new Item(19722),new Item(13212, 1)}, new Item(13693), new int[] {1, -1, 0}),
				BLESSED_LEGS(new Item[] {new Item(19723),new Item(13212, 1)}, new Item(13695), new int[] {1, -1, 0}),
				BLESSED_KITE(new Item[] {new Item(18363),new Item(13212, 1)}, new Item(13694), new int[] {1, -1, 0}),
				
				JINNI_AMULET(new Item[] {new Item(1499),new Item(13212, 1)}, new Item(13696), new int[] {1, -1, 0}),
				JINNI_RING(new Item[] {new Item(15012),new Item(13212, 1)}, new Item(13697), new int[] {1, -1, 0}),
				JINNI_CAPE(new Item[] {new Item(3973),new Item(13212, 1)}, new Item(13698), new int[] {1, -1, 0}),
				JINNI_HELMET(new Item[] {new Item(4799),new Item(13212, 1)}, new Item(13699), new int[] {1, -1, 0}),
				JINNI_PLATEBODY(new Item[] {new Item(4800),new Item(13212, 1)}, new Item(13700), new int[] {1, -1, 0}),
				JINNI_PLATELEGS(new Item[] {new Item(4801),new Item(13212, 1)}, new Item(13701), new int[] {1, -1, 0}),

				REX_CAPE(new Item[] {new Item(3931),new Item(13212, 1)}, new Item(13702), new int[] {1, -1, 0}),
				REX_HELMET(new Item[] {new Item(3960),new Item(13212, 1)}, new Item(13705), new int[] {1, -1, 0}),
				REX_PLATEBODY(new Item[] {new Item(3958),new Item(13212, 1)}, new Item(13706), new int[] {1, -1, 0}),
				REX_PLATELEGS(new Item[] {new Item(3959),new Item(13212, 1)}, new Item(13704), new int[] {1, -1, 0}),
				REX_BOOTS(new Item[] {new Item(5186),new Item(13212, 1)}, new Item(13707), new int[] {1, -1, 0}),
				REX_GLOVES(new Item[] {new Item(5187),new Item(13212, 1)}, new Item(13708), new int[] {1, -1, 0}),

				DRAGONSLAYER_HELMET(new Item[] {new Item(936),new Item(13212, 1)}, new Item(13840), new int[] {1, -1, 0}),
				DRAGONSLAYER_PLATEBODY(new Item[] {new Item(937),new Item(13212, 1)}, new Item(13841), new int[] {1, -1, 0}),
				DRAGONSLAYER_PLATELEGS(new Item[] {new Item(938),new Item(13212, 1)}, new Item(13842), new int[] {1, -1, 0}),
				DRAGONSLAYER_CAPE(new Item[] {new Item(939),new Item(13212, 1)}, new Item(13843), new int[] {1, -1, 0}),

				
				
				
				
				
				
				
		DONATORAURA(new Item[] {new Item(4780),new Item(10835, 100000), new Item(15454)}, new Item(3277), new int[] {1, -1, 0}),
		;

		ItemForgeData(Item[] requiredItems, Item product, int[] skillRequirement) {
			this.requiredItems = requiredItems;
			this.product = product;
			this.skillRequirement = skillRequirement;
		}

		private Item[] requiredItems;
		private Item product;
		private int[] skillRequirement;

		public static ItemForgeData getDataForItems(int item1, int item2) {
			for(ItemForgeData shieldData : ItemForgeData.values()) {
				int found = 0;
				for(Item it : shieldData.requiredItems) {
					if(it.getId() == item1 || it.getId() == item2)
						found++;
				}
				if(found >= 2)
					return shieldData;
			}
			return null;
		}
	}
}
