package com.arlania.world.content.teleportation.teleportsystem;

import com.arlania.model.Position;

public enum BossInformationEnum {

	BEGINNER(-28705, 420, "Starter Zone", new String[] {"Are You Ready To Face", "A @gre@ couple zones?", "", ""}, new Position(2759, 3174, 0)),
	DEFILER(-28704, 2767, "Defiler",new String[] {"Creepy Crawlers", "", "", ""}, new Position(2664, 3432, 0)),
	FROSTS(-28703, 51, "Frost Dragon",new String[] {"Burrr Cold", "", "", ""}, new Position(2790, 4766, 0)),
	SIRENIC(-28702, 2783, "Sirenic Ogres",new String[] {"", "", "", ""}, new Position(3184, 5471, 0)),
	HERCULES(-28701, 17, "Hercules",new String[] {"", "", "", ""}, new Position(2783, 4636, 0)),//
	LUCARIO(-28700, 3263, "Lucario",new String[] {"", "", "", ""}, new Position(2913, 4759, 0)),
	HADES(-28699, 15, "Hades",new String[] {"", "", "", "", ""}, new Position(2095, 3677, 0)),
	CHARIZARD(-28698, 1982, "Charizard",new String[] {"", "", "", "", "", ""}, new Position(2270, 3240, 0)),
	HELLFIRE(-28697, 9231, "Hellfire Wizards",new String[] {"", "", "", ""}, new Position(2524, 4777, 0)),
	JINIS(-28696, 9994, "Jinis",new String[] {"", "", "", "", ""}, new Position(2724, 9821, 0)),
	GODZILLA(-28695, 9932, "Godzilla",new String[] {"", "", "", "", ""}, new Position(3374, 9807, 0)),
	GREAT_OLMS(-28694, 224, "Great Olm",new String[] {"", "", "", "", ""}, new Position(2399, 3548, 0)),
	INFERNALS(-28693, 1999, "Bloated infernals", new String[] {"", "", "", "",}, new Position(1240, 1247, 0)),
	ZEUS(-28692, 16, "Zeus", new String[] {"", "", "", "",}, new Position(2065, 3663, 0)),
	INFARTICO(-28691, 9993, "Infartico", new String[] {"", "", "", "",}, new Position(3479, 3087, 0));
	
	public int buttonId, npcId;
	public String bossName;
	public String[] information;
	public Position pos;

	BossInformationEnum(int buttonId, int npcId, String bossName, String[] information, Position pos) {
		this.buttonId = buttonId;
		this.npcId = npcId;
		this.bossName = bossName;
		this.information = information;
		this.pos = pos;
	}
	
	public int getButtonId() {
		return buttonId;
	}

	public String getBossName() {
		return bossName;
	}
	
	public String[] getInformation() {
		return information;
	}
	
	public Position getPos() {
		return pos;
	}
}