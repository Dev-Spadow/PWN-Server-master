package com.arlania.world.content.referral;

import com.arlania.model.Item;

public enum ReferralOptions {
	YOUTUBERS(new String[] {"I Pk Max Jr", "Wr3cked You", "", "", "", "", "", "", "", ""}, new Item[] {new Item(10168, 2),new Item(989, 1),new Item(19080, 20)}, ReferralType.YOUTUBERS),
	WEBSITES(new String[] {"Google search", "Forums", "Topg", "Runelocus", "Facebook", "Discord", "", "",""}, new Item[] {new Item(15003, 1),new Item(989, 1),new Item(19080, 20)}, ReferralType.WEBSITES),
	OTHER(new String[] {"Word of mouth", "Evil","","","","","","","",""}, new Item[] {new Item(15003, 1),new Item(989, 1),new Item(19080, 20)}, ReferralType.OTHER);
	
	private String[] options;
	
	private ReferralType type;
	
	private Item[] rewards;
	
	ReferralOptions(String[] options, Item[] rewards, ReferralType type) {
		this.options = options;
		this.rewards = rewards;
		this.type = type;
	}
	
	public String[] getOptions() {
		return options;
	}
	
	public ReferralType getType() {
		return type;
	}
	
	public Item[] getRewards() {
		return rewards;
	}

}
