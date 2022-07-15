package com.arlania.world.content.scratchcard;

public enum ScratchCardData {

	PERFECT_NECKLACE(774),BLOODSLAYER_KEY(5205),BOND_CASKET(10205),EEVEE(4742),AKASHA_MAGE_SET(11862),OWNER_BOX(10168),ONE_HUNDRED_BOND(5020),  PERFECT_RING(773);

	private int displayId;


	ScratchCardData(int displayId) {
		this.displayId = displayId;
	}

	public int getDisplayId() {
		return displayId;
	}

	public void setDisplayId(int displayId) {
		this.displayId = displayId;
	}

}