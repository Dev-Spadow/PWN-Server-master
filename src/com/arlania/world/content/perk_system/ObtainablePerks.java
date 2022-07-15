package com.arlania.world.content.perk_system;

public enum ObtainablePerks {
	BLOOD_HERO_I(PerkType.TEN_PERCENT_MORE_BLOODBAGS, 17750, 5_000, 51508);
	/*BLOOD_HERO_II(PerkType.TWENTY_FIVE_PERCENT_MORE_BLOODBAGS, 17750, 25_000),
	BLOOD_MASTER_I(PerkType.TEN_PERCENT_MORE_BLOOD_SLAYER_POINTS, 17750, 10_000),
	BLOOD_MASTER_II(PerkType.TWENTY_FIVE_PERCENT_MORE_BLOOD_SLAYER_POINTS, 17750, 30_000),
	BLOOD_MASTER_III(PerkType.FIFTY_PERCENT_MORE_BLOOD_SLAYER_POINTS, 17750, 50_000),
	BLOOD_TREASURER_I(PerkType.FIVE_PERCENT_MORE_DROP_RATE, 17750, 10_000),
	BLOOD_TREASURER_II(PerkType.TEN_PERCENT_MORE_DROP_RATE, 17750, 20_000),
	BLOOD_LUCK_I(PerkType.FIFTEEN_PERCENT_DOUBLE_DROPS, 17750, 15_000),
	BLOOD_LUCK_II(PerkType.TWENTY_FIVE_PERCENTY_DOUBLE_DROPS, 17750, 25_000),
	BLOOD_SLAYER_I(PerkType.ZERO_PT_TEN_DAMAGE_BOOST, 17750, 10_000),
	BLOOD_SLAYER_II(PerkType.ZERO_PT_TWENTY_FIVE_DAMAGE_BOOST, 17750, 25_000);*/
	
	private PerkType type;
	
	private int currency;
	
	private int currencyAmt;
	
	private int interfaceId;
	
	ObtainablePerks(PerkType type, int currency, int currencyAmt, int interfaceId) {
		this.type = type;
		this.currency = currency;
		this.currencyAmt = currencyAmt;
		this.interfaceId = interfaceId;
	}
	
	public PerkType getPerkType() {
		return type;
	}
	
	public int getCurrency() {
		return currency;
	}
	
	public int getCurrencyAmount() {
		return currencyAmt;
	}
	
	public int getInterfaceId() {
		return interfaceId;
	}
}
