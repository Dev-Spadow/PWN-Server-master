package com.arlania.world.content.starterzone;

import com.arlania.model.Item;

public enum StarterZoneData
{
	BLOOD_REAPER(0, 11204, 5, new Item(15332,1), new Item(17849,1), new Item(10852,10)),
    MANIAC_OGRE_CHILD(1, 11382, 10, new Item(13258), new Item(13259, 1), new Item(13256, 1)),
    NISHIMIYA(2, 11369, 15, new Item(3928, 1), new Item(17908, 1), new Item(17909,1)),
    KILLING_MACHINE(3, 11307, 20, new Item(5130,1), new Item(6733,1), new Item(15373, 1)),
    BIG_SMOKE(4, 11202, 30, new Item(989), new Item(15373, 1), new Item(10855,10)),
    SHADOWLORD(5, 11380, 50, new Item(10835,500), new Item(19468,1), new Item(15398,1));

	final int key;
	final int npcId;
	final int killsRequired;
	final Item[] itemRewards;

	StarterZoneData(int key, int npcId, int killsRequired, Item... itemRewards) {
		this.key = key;
		this.npcId = npcId;
		this.killsRequired = killsRequired;
		this.itemRewards = itemRewards;
	}
//dont worry hold up kkkkk
	public static StarterZoneData[] values = StarterZoneData.values();

	public static StarterZoneData getForNpc(int npcId)
	{
//		if (npcId == 8146) { // knuckles
//			return SONIC;
//		}
		for (StarterZoneData data : values) {
			if (data.npcId == npcId) {
				return data;
			}
		}
		return null;
	}

	public StarterZoneData next()
	{
		return values[Math.min(ordinal()+1, values.length - 1)]; // Lmao
	}
}
