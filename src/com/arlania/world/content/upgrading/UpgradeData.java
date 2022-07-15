package com.arlania.world.content.upgrading;



import com.arlania.model.Item;
import com.arlania.world.entity.impl.player.Player;

public enum UpgradeData {


	CRYSTAL_KEY(new Item(989, 1), new Item(1543, 1), 15, 200),
	STARTER_BOX(new Item(15373, 1), new Item(6199, 1), 30, 300),
	GRACIOUS_MBOX(new Item(6199, 1), new Item(3988, 1), 25, 1000),
	PLEASANT_MBOX(new Item(3988, 1), new Item(15374, 1), 20, 5000),
	ELEMENTAL_MBOX(new Item(15374, 1), new Item(13997, 1), 15, 7500),
	OWNER_MBOX(new Item(13997, 1), new Item(10168, 1), 10, 15000),
	BLESSED_AMULET(new Item(15418, 1), new Item(19886, 1), 10, 2000),
	ROW(new Item(2572, 1), new Item(20054, 1), 33, 750),
	COLLECTORS_NECKLACE(new Item(19886, 1), new Item(19106, 1), 50, 5000),
	FIGHTING_BOOTS(new Item(9006, 1), new Item(5079, 1), 33, 1500),
	DEFENDER_CAPE(new Item(3973, 1), new Item(18748, 1), 33, 2500),
	//C25DR(new Item(2572, 1), new Item(3317, 1), 18, 10000),
	INFERNAL_SCYTHE(new Item(3928, 1), new Item(3941, 1), 33, 3500),
	REX_WHIP(new Item(18865, 1), new Item(14559, 1), 20, 7500),
	HURRICANE_WHIP(new Item(18957, 1), new Item(19618, 1), 15, 15000),
	FROST_MG(new Item(5130, 1), new Item(5134, 1), 33, 5000),
	DRAGON_MG(new Item(5134, 1), new Item(5131, 1), 33, 7500),
	HULK_MG(new Item(5131, 1), new Item(5195, 1), 25, 15000),
	EMPIRE_MG(new Item(5195, 1), new Item(5132, 1), 10, 50000),
	JINIS_BATTLESTAFF(new Item(3951, 1), new Item(19720, 1), 25, 10000),
	INFERNAL_BATTLESTAFF(new Item(19468, 1), new Item(3951, 1), 33, 2500),
	SABER(new Item(3276, 1), new Item(3274, 1), 20, 5000),
	HEATED_BATTLESTAFF(new Item(19720, 1), new Item(5129, 1), 20, 15000),
	DR_SCROLL(new Item(18392, 1), new Item(18401, 1), 25, 2000),
	$5(new Item(15375, 2500), new Item(19935, 1), 25, 5000),
	RANGE_DIAMOND_1(new Item(6509, 1), new Item(6510, 1), 20, 20000),
	RANGE_DIAMOND_2(new Item(6510, 1), new Item(6511, 1), 30, 50000),
	MAGIC_DIAMOND_1(new Item(6505, 1), new Item(6506, 1), 20, 20000),
	MAGIC_DIAMOND_2(new Item(6506, 1), new Item(6508, 1), 30, 50000),
	MELEE_DIAMOND_1(new Item(14546, 1), new Item(14547, 1), 20, 20000),
	MELEE_DIAMOND_2(new Item(14547, 1), new Item(14548, 1), 30, 50000),
	INFINITE_OVERLOAD(new Item(5185, 1), new Item(3961, 1), 33, 50000),
	HEATED(new Item(5129, 1), new Item(19727, 1), 20, 25000),
	REX_HELMET(new Item(3908, 1), new Item(5200, 1), 33, 4000),
	REX_PLATEBODY(new Item(3910, 1), new Item(5198, 1), 33, 4000),
	REX_PLATELEGS(new Item(3909, 1), new Item(5199, 1), 33, 4000),
	ZEUS_HELMET(new Item(6194, 1), new Item(4001, 1), 20, 4000),
	ZEUS_PLATEBODY(new Item(6195, 1), new Item(3999, 1), 20, 4000),
	ZEUS_PLATELEGS(new Item(6196, 1), new Item(4000, 1), 20, 4000),
	
	
	EXOTIC_STAFF(new Item(19727, 1), new Item(8664, 1), 20, 25000),
	EXOTIC_BOOTS(new Item(19728, 1), new Item(8666, 1), 25, 20000),
	EXOTIC_GLOVES(new Item(19729, 1), new Item(8667, 1), 25, 20000),
	EXOTIC_HELMET(new Item(19730, 1), new Item(8668, 1), 25, 20000),
	EXOTIC_PLATELEGS(new Item(19731, 1), new Item(8669, 1), 25, 20000),
	EXOTIC_PLATEBODY(new Item(19732, 1), new Item(8670, 1), 25, 20000),
	EXOTIC_WINGS(new Item(6485, 1), new Item(8665, 1), 25, 20000),
	
	EXODEN_HELMET(new Item(14494, 1), new Item(13206, 1), 25, 20000),
	EXODEN_PLATELEGS(new Item(14490, 1), new Item(13203, 1), 25, 20000),
	EXODEN_PLATEBODY(new Item(14492, 1), new Item(13202, 1), 25, 20000),
	EXODEN_WINGS(new Item(2760, 1), new Item(13207, 1), 25, 20000)

	
	
	
	
	
	;
	
	
	
	
	
	
	private Item required, reward;
	private int chance, bagsRequired;
	
	/*UpgradeData(Item required, Item reward, int chance) {
		this.required = required;
		this.reward = reward;
		this.chance = chance;
	}*/
	
	UpgradeData(Item required, Item reward, int chance, int bagsRequired) {
		this.required = required;
		this.reward = reward;
		this.chance = chance;
		this.bagsRequired = bagsRequired;
	}
	
	public Item getRequired() {
		return required;
	}

	public Item getReward() {
		return reward;
	}

	public int getChance() {
		return chance;
	}
	
	
	public int getBagsRequired() {
		return bagsRequired;
	}
	

}
