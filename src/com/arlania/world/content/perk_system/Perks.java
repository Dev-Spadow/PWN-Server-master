package com.arlania.world.content.perk_system;

public class Perks {
	
	private PerkType type;
	
	private boolean completed;
	
	public Perks(PerkType type, boolean completed) {
		this.type = type;
		this.completed = completed;
	}

	public PerkType getType() {
		return type;
	}
	
	public void setType(PerkType type) {
		this.type = type;
	}
	
	public boolean getCompleted() {
		return completed;
	}
	
	public void setCompleted(boolean completed) {
		this.completed = completed;
	}
}
