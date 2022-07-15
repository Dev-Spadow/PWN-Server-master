package com.arlania.world.content.skill.impl.prayer;

public enum BonesData {
	 BONES(526, 555),
	 BAT_BONES(530, 655),
	 WOLF_BONES(2859, 655),
	 BIG_BONES(532, 780),
	 FEMUR_BONES(15182, 801),
	 BABYDRAGON_BONES(534, 1265),
	 JOGRE_BONE(3125, 1530),
	 ZOGRE_BONES(4812, 1644),
	 LONG_BONES(10976, 1751),
	 CURVED_BONE(10977, 1851),
	 SHAIKAHAN_BONES(3123, 1957),
	 DRAGON_BONES(536, 2050),
	 FAYRG_BONES(4830, 2281),
	 RAURG_BONES(4832, 2381),
	 DAGANNOTH_BONES(6729, 2581),
	 OURG_BONES(14793, 2881),
	 ZOMBIE_BONES(10852, 5000),
	 MANIAC_BONES(10853, 7500),
	 NISHIMIYA_BONES(10854, 9000),
	 BIGSMOKE_BONES(10855, 10000),
	 MACHINE_PARTS(23, 11500),
	 FROSTDRAGON_BONES(18830, 14400),
	 INCREDIBLE_BONES(16533, 25400),
	 SIRENIC_BONES(16534, 20400),
	 INFERNAL_BONES(19080, 15000);
	
	BonesData(int boneId, int buryXP) {
		this.boneId = boneId;
		this.buryXP = buryXP;
	}

	private int boneId;
	private int buryXP;
	
	public int getBoneID() {
		return this.boneId;
	}
	
	public int getBuryingXP() {
		return this.buryXP;
	}
	
	public static BonesData forId(int bone) {
		for(BonesData prayerData : BonesData.values()) {
			if(prayerData.getBoneID() == bone) {
				return prayerData;
			}
		}
		return null;
	}
	
}
