package com.arlania.world.content.damage_cap;

public enum DamageCapHandler {
	MASS_BOSS(10010, 25000),
	WORLD_BOSS(9911, 25000),
	IRONMAN(2509, 25000),
	SEPHIROTH(25, 25000),
	VEIGAR_BOSS(11362, 25000),
	STARTER_BOSS(68, 25000),
	PENNYWISE_SPIDER(4595, 25000),
	PENNYWISE(4543, 25000),
	VIRULENT_DRAGON(4972, 25000),
	ARCTIC_BEAST(12802, 25000),
	THANOS(12808, 25000),
	APOLLYON(12801, 25000),
	MAY_BOSS(2005, 25000),
	PORTAL_2(6142, 100),
	PORTAL_3(6143, 100),
	PORTAL_4(6144, 100),
	PORTAL_1(6145, 100);
	private int npcId;
	
	private int maxDamage;
	
	DamageCapHandler(int npcId, int maxDamage) {
		this.npcId = npcId;
		this.maxDamage = maxDamage;
	}
	
	public int getNpcId() {
		return npcId;
	}
	
	public int getMaxDamage() {
		return maxDamage;
	}
}
