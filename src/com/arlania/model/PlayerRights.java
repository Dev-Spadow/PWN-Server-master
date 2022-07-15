package com.arlania.model;

import com.arlania.model.container.impl.Bank;
import com.arlania.util.Misc;
import com.arlania.world.entity.impl.player.Player;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;


/**
 * Represents a player's privilege rights.
 * @author Gabriel Hannason
 */

public enum PlayerRights {

	/*
	 * A regular member of the server.
	 */
	PLAYER(-1, null, 1, 1, ""),
	/*
	 * A moderator who has more privilege than other regular members and donators.
	 */
	MODERATOR(-1, "><col=6600CC>", 1, 1.5, "<img=1>"),

	/*
	 * The second-highest-privileged member of the server.
	 */
	ADMINISTRATOR(-1, "<col=FFFF64>", 1, 1.5 , "<img=2>"),

	/*
	 * The highest-privileged member of the server
	 */
	OWNER(-1, "<col=B40404>", 1, 1.5, "<img=3>"),
	/*
	 * The Developer of the server, has same rights as the owner.
	 */
	DEVELOPER(-1, "<col=fa0505>", 1, 1.5, "<img=4>"),

	/*
	 * A member who has donated to the server. 
	 */
	DONATOR(60, "<shad=fe0000>", 1.5, 1.1, "<img=5>"),
	SUPER_DONATOR(40, "<shad=31e421>", 1.5, 1.2, "<img=6>"),
 	ULTRA_DONATOR(20, "<shad=c003b9>", 2, 1.3, "<img=7>"),
	MYSTIC_DONATOR(10, "<shad=ebf217>", 2.5, 1.4, "<img=8>"),
	OBSIDIAN_DONATOR(0, "<shad=0789de>", 3, 1.5, "<img=9>"),
	

	/*
	 * A member who has the ability to help people better.
	 */
	SUPPORT(-1, "<shad=debb0e>", 1, 1.5, "<img=10>"),

	/*
	 * A member who has been with the server for a long time.
	 */
	YOUTUBER(30, "<col=CD661D>", 1, 1.1, "<img=11>"),
	CoolRank(0, "<col=000043715>", 1, 1.1, "<img=348>"),//12
	LEGENDARY_DONATOR(0, "<shad=07dec8>", 3, 2.0, "<img=13>"),//13
	CELESTIAL_DONATOR(0, "<shad=eae100>", 5, 2.5, "<img=14>"), // 14
	EXECUTIVE_DONATOR(0, "<shad=0615f1>", 7, 2.75, "<img=412>"), // defines the rank server-sided, to send the index to client-side
	SUPREME_DONATOR(0, "<shad=b8f8fb>", 9, 3.0, "<img=16>"), // defines the rank server-sided, to send the index to client-side
	CO_OWNER(-1, "<col=0e7099>", 1, 1.5, "<img=17>"),
	DIVINE_DONATOR(0, "<shad=2cc100>", 9, 2.0, "<img=18>"),

    //Might want to change the yell hax color later
    COMMUNITY_MANAGER(-1, "<col=fa0505>", 1, 1.5, "<img=998>", 22),
    EVENT_MANAGER(-1, "<col=fa0505>", 1, 1.5, "<img=997>", 23),
    ; // defines the rank server-sided, to send the index to client-side



    PlayerRights(int yellDelaySeconds, String yellHexColorPrefix, double loyaltyPointsGainModifier, double experienceGainModifier, String rank) {
		this.yellDelay = yellDelaySeconds;
		this.yellHexColorPrefix = yellHexColorPrefix;
		this.loyaltyPointsGainModifier = loyaltyPointsGainModifier;
		this.experienceGainModifier = experienceGainModifier;
		this.rank = rank;
		this.iconId = ordinal();
	}

    PlayerRights(int yellDelaySeconds, String yellHexColorPrefix, double loyaltyPointsGainModifier, double experienceGainModifier, String rank, int iconId) {
        this.yellDelay = yellDelaySeconds;
        this.yellHexColorPrefix = yellHexColorPrefix;
        this.loyaltyPointsGainModifier = loyaltyPointsGainModifier;
        this.experienceGainModifier = experienceGainModifier;
        this.rank = rank;
        this.iconId = iconId;
    }
	
	private static final ImmutableSet<PlayerRights> STAFF = Sets.immutableEnumSet(SUPPORT, MODERATOR,COMMUNITY_MANAGER,EVENT_MANAGER, ADMINISTRATOR, OWNER,CO_OWNER, DEVELOPER, CoolRank);
	private static final ImmutableSet<PlayerRights> HIGH_RANK_STAFF = Sets.immutableEnumSet(ADMINISTRATOR, OWNER, DEVELOPER,CO_OWNER);
	private static final ImmutableSet<PlayerRights> DEVELOPERS = Sets.immutableEnumSet(ADMINISTRATOR, OWNER, DEVELOPER,CO_OWNER);
	private static final ImmutableSet<PlayerRights> HAS_BAN_RIGHTS = Sets.immutableEnumSet(OWNER,CO_OWNER, DEVELOPER,ADMINISTRATOR,EVENT_MANAGER,COMMUNITY_MANAGER);
	private static final ImmutableSet<PlayerRights> MEMBERS = Sets.immutableEnumSet(DONATOR, SUPER_DONATOR, ULTRA_DONATOR, MYSTIC_DONATOR, OBSIDIAN_DONATOR, LEGENDARY_DONATOR, CELESTIAL_DONATOR,EXECUTIVE_DONATOR,SUPREME_DONATOR,DIVINE_DONATOR);
	/*
	 * The yell delay for the rank
	 * The amount of seconds the player with the specified rank must wait before sending another yell message.
	 */
	private int yellDelay;
	private String yellHexColorPrefix;
	private double loyaltyPointsGainModifier;
	private double experienceGainModifier;
	private String rank;
	private int iconId;
	
	public int getYellDelay() {
		return yellDelay;
	}

    public String getRank() {
        return rank;
    }

    public int getIcon() {
	    return iconId;
    }
	
	public String customYellTitle;
	
	public String getCustomYellPrefix(boolean owner) {
        if (customYellTitle == null || customYellTitle.isEmpty()) {
            if (this == OWNER)
                return "Owner";
            else if (this == MODERATOR)
                return "Moderator";
            return "Deluxe";
        }
        
        return customYellTitle;
    }
	public String getCustomYellPrefix1(boolean owner) {
        if (customYellTitle == null || customYellTitle.isEmpty()) {
            if (this == CO_OWNER)
                return "Co Owner";
        }
        return customYellTitle;
    }
	
	/*
	 * The player's yell message prefix.
	 * Color and shadowing.
	 */
	
	public String getYellPrefix() {
        if (customYellTitle == null || customYellTitle.isEmpty())
            return "";
        return yellHexColorPrefix;
    }
	
	/**
	 * The amount of loyalty points the rank gain per 4 seconds
	 */
	public double getLoyaltyPointsGainModifier() {
		return loyaltyPointsGainModifier;
	}
	
	public double getExperienceGainModifier() {
		return experienceGainModifier;
	}
	
	public boolean isStaff() {
		return STAFF.contains(this);
	}
	
	public boolean isMember() {
		return MEMBERS.contains(this);
	}
	
	public boolean isAdmin() {
		return HIGH_RANK_STAFF.contains(this);
	}

	public boolean isDev() {
		return DEVELOPERS.contains(this);
	}
	
	public boolean isHighDonator() {
		return this == LEGENDARY_DONATOR || this == OBSIDIAN_DONATOR | this == ULTRA_DONATOR|| this == MYSTIC_DONATOR|| this == CELESTIAL_DONATOR|| this == EXECUTIVE_DONATOR|| this == SUPREME_DONATOR|| this == DIVINE_DONATOR;
	}
	
	public boolean isLegendary() {
		return this == LEGENDARY_DONATOR;
	}
	
	public boolean isCelestial() {
		return this == CELESTIAL_DONATOR;
	}
	
	public boolean isExectuive() {
		return this == EXECUTIVE_DONATOR;
	}
	public boolean isSupreme() {
		return this == SUPREME_DONATOR;
	}
	public boolean isDivine() {
		return this == DIVINE_DONATOR;
	}
	public boolean isCoOwner() {
		return this == CO_OWNER;
	}

	public boolean isDonator(Player player) {
		return player.getAmountDonated() >= 10;
	}
	
	public boolean isSuperDonator(Player player) {
		return player.getAmountDonated() >= 25;
	}
	
	
	public boolean isExtremeDonator(Player player) {
		return player.getAmountDonated() >= 50;
	}
	
	public boolean isLegendaryDonator(Player player) {
		return player.getAmountDonated() >= 125;
	}
	
	public boolean isUberDonator(Player player) {
		return player.getAmountDonated() >= 200;
	}
	
	public boolean isDeluxeDonator(Player player) {
		return player.getAmountDonated() >= 500;
	}
	
	public boolean isVipDonator(Player player) {
		return player.getAmountDonated() >= 1000;
	}
	
	public boolean isInvestorDonator(Player player) {
		return player.getAmountDonated() >= 2500;
	}
	public boolean isDiamond(Player player) {
		return player.getAmountDonated() >= 5000;
	}

	
	/**
	 * Gets the rank for a certain id.
	 * 
	 * @param id	The id (ordinal()) of the rank.
	 * @return		rights.
	 */
	public static PlayerRights forId(int id) {
		for (PlayerRights rights : PlayerRights.values()) {
			if (rights.ordinal() == id) 
				return rights;
		}
		return null;
	}
	@Override
	public String toString() {
		return Misc.ucFirst(name().replaceAll("_", " "));
	}

	/**
	 * @return
	 */
	public boolean hasBanRights() {
		return HAS_BAN_RIGHTS.contains(this);
	}

	public boolean hasEnoughDonated(Player player2) {
		// TODO Auto-generated method stub
		return false;
	}
}
