package com.arlania.world.content;

import com.arlania.world.entity.impl.player.Player;

public class PointsHandler {

	private Player p;

	public PointsHandler(Player p) {
		this.p = p;
	}

	public void reset() {
		dungTokens = pestcontrolpoints = (int) (loyaltyPoints = votingPoints = slayerPoints = pkPoints = 0);
		p.getPlayerKillingAttributes().setPlayerKillStreak(0);
		p.getPlayerKillingAttributes().setPlayerKills(0);
		p.getPlayerKillingAttributes().setPlayerDeaths(0);
		p.getDueling().arenaStats[0] = p.getDueling().arenaStats[1] = 0;
	}

	public PointsHandler refreshPanel() {
		p.getPacketSender().sendString(26703, "@or2@Raid Points: @gre@ " + raidPoints);


		p.getPacketSender().sendString(26702, "@or2@Prestige Points: @gre@" + prestigePoints);
		p.getPacketSender().sendString(26706, "@or2@Pest Control Points: @gre@ " + pestcontrolpoints);
		p.getPacketSender().sendString(26701, "@or2@Loyalty Points: @gre@" + (int) loyaltyPoints);
		p.getPacketSender().sendString(26707, "@or2@Bossing Points: @gre@ " + p.getBossPoints());

		p.getPacketSender().sendString(26710, "@or2@Dung. Tokens: @gre@ " + dungTokens);
		p.getPacketSender().sendString(26704, "@or2@Voting Points: @gre@ " + votingPoints);
		p.getPacketSender().sendString(26705, "@or2@Slayer Points: @gre@" + slayerPoints);
		p.getPacketSender().sendString(26709, "@or2@Pk Points: @gre@" + pkPoints);
		p.getPacketSender().sendString(26708, "@or2@Donation Points: @gre@" + donationPoints);

		p.getPacketSender().sendString(26711,
				"@or2@Wilderness Killstreak: @gre@" + p.getPlayerKillingAttributes().getPlayerKillStreak());
		p.getPacketSender().sendString(26712,
				"@or2@Wilderness Kills: @gre@" + p.getPlayerKillingAttributes().getPlayerKills());
		p.getPacketSender().sendString(26713,
				"@or2@Wilderness Deaths: @gre@" + p.getPlayerKillingAttributes().getPlayerDeaths());
		p.getPacketSender().sendString(26714, "@or2@Arena Victories: @gre@" + p.getDueling().arenaStats[0]);
		p.getPacketSender().sendString(26715, "@or2@Arena Losses: @gre@" + p.getDueling().arenaStats[1]);

		return this;
	}

	private int prestigePoints;
	private int triviaPoints;
	private int slayerPoints;
	private int pestcontrolpoints;
	private int dungTokens;
	private int pkPoints;
	private double loyaltyPoints;
	public int votingPoints;
	private int achievementPoints;
	public int donationPoints;
	public int boxPoints;
	private long raidPoints;
	private int npckillcount;
	public int miniGamePoints1;
	public int miniGamePoints2;
	public int miniGamePoints3;
	public int miniGamePoints4;
	public int miniGamePoints5;
	public int skillPoints;
	private int bloodslayerPoints;
	
	private int superPoints;
	
	// raid
	public long getRaidPoints() {
		return raidPoints;
	}

	public void setRaidPoints(long points, boolean add) {
		if (add)
			this.raidPoints += points;
		else
			this.raidPoints = points;
	}

	public void incrementRaidPoints(double amount) {
		this.raidPoints += amount;
	}
	public int getPrestigePoints() {
		return prestigePoints;
	}

	public void setPrestigePoints(int points, boolean add) {
		if (add)
			this.prestigePoints += points;
		else
			this.prestigePoints = points;
	}

	public int getSlayerPoints() {
		return slayerPoints;
	}
	public int getBloodSlayerPoints() {
		return bloodslayerPoints;
	}

	public int getminiGamePoints1() {
		return miniGamePoints1;
	}
	public int getminiGamePoints2() {
		return miniGamePoints2;
	}
	public int getminiGamePoints3() {
		return miniGamePoints3;
	}
	public int getminiGamePoints4() {
		return miniGamePoints4;
	}
	public int getminiGamePoints5() {
		return miniGamePoints5;
	}
	
	public void setMinigamePoints1(int miniGamePoints1, boolean add) {
		if (add)
			this.miniGamePoints1 += miniGamePoints1;
		else
			this.miniGamePoints1 = miniGamePoints1;
	}
	public void setMinigamePoints2(int miniGamePoints2, boolean add) {
		if (add)
			this.miniGamePoints2 += miniGamePoints2;
		else
			this.miniGamePoints2 = miniGamePoints2;
	}
	public void setminiGamePoints3(int miniGamePoints3, boolean add) {
		if (add)
			this.miniGamePoints3 += miniGamePoints3;
		else
			this.miniGamePoints3 = miniGamePoints3;
	}
	public void setminiGamePoints4(int miniGamePoints4, boolean add) {
		if (add)
			this.miniGamePoints4 += miniGamePoints4;
		else
			this.miniGamePoints4 = miniGamePoints4;
	}
	public void setminiGamePoints5(int miniGamePoints5, boolean add) {
		if (add)
			this.miniGamePoints5 += miniGamePoints5;
		else
			this.miniGamePoints5 = miniGamePoints5;
	}


	public int getDonationPoints() {
		return donationPoints;
	}
	
	public int getboxPoints() {
		return boxPoints;
	}

	public void setDonationPoints(int donationPoints, boolean add) {
		if (add)
			this.donationPoints += donationPoints;
		else
			this.donationPoints = donationPoints;
	}
	
	public void setboxPoints(int boxPoints, boolean add) {
		if (add)
			this.boxPoints += boxPoints;
		else
			this.boxPoints = boxPoints;
	}

	public void incrementDonationPoints(double amount) { // BUTTPLUGS
		this.donationPoints += amount; // BUTTPLUGS
	}
	
	public void incrementMiniGamePoints1(double amount) { // BUTTPLUGS
		this.miniGamePoints1 +=amount; // BUTTPLUGS
	}
	public void incrementMiniGamePoints2(double amount) { // BUTTPLUGS
		this.miniGamePoints2 +=amount; // BUTTPLUGS
	}
	public void decrementSlayerPoints(int amount) {
		this.slayerPoints -= amount;
	}
	
	public void incrementSlayerPoints(int amount) {
		this.slayerPoints += amount;
	}
	

	
	public void decrementMiniGamePoints1(double amount) { // BUTTPLUGS
		this.miniGamePoints1 -=amount;
	}
	public void decrementMiniGamePoints2(double amount) { // BUTTPLUGS
		this.miniGamePoints2 -=amount;
	}
	
	public void incrementMiniGamePoints3(double amount) {
		this.miniGamePoints3 += amount;
	}
	public void decrementMiniGamePoints3(double amount) {
		this.miniGamePoints3 -= amount;
	}
	public void incrementMiniGamePoints4(double amount) {
		this.miniGamePoints4 += amount;
	}
	public void decrementMiniGamePoints4(double amount) {
		this.miniGamePoints4 -= amount;
	}
	public void incrementMiniGamePoints5(double amount) {
		this.miniGamePoints5 += amount;
	}
	public void decrementMiniGamePoints5(double amount) {
		this.miniGamePoints5 -= amount;
	}
	public void incrementboxPoints(double amount) {
		this.boxPoints += amount;
	}
	
	public void decrementboxPoints(double amount) {
		this.boxPoints -=amount;
	}

	public int getSkillPoints() {
		return skillPoints;
	}

	public void setSkillPoints(int skillPoints, boolean add) {
		if (add)
			this.skillPoints += skillPoints;
		else
			this.skillPoints = skillPoints;
	}

	public void incrementSkillPoints(double amount) {
		this.skillPoints += amount;
	}

	public int getTriviaPoints() {
		return triviaPoints;
	}

	public void setTriviaPoints(int triviaPoints, boolean add) {
		if (add)
			this.triviaPoints += triviaPoints;
		else
			this.triviaPoints = triviaPoints;
	}

	public void incrementTriviaPoints(Player player, double amount) {
		this.triviaPoints += amount;
	}

	public void setSlayerPoints(int slayerPoints, boolean add) {
		if (add)
			this.slayerPoints += slayerPoints;
		else
			this.slayerPoints = slayerPoints;
	}
	
	public void setBloodSlayerPoints(int bloodslayerPoints, boolean add) {
		if (add)
			this.bloodslayerPoints += bloodslayerPoints;
		else
			this.bloodslayerPoints = bloodslayerPoints;
	}

	
	
	public void setSlayerPointsDouble(double slayerPoints, boolean add) {
		if (add)
			this.slayerPoints += slayerPoints;
		else
			this.slayerPoints = (int) slayerPoints;
	}
	
	public void setBloodSlayerPointsDouble(double bloodslayerPoints, boolean add) {
		if (add)
			this.bloodslayerPoints += bloodslayerPoints;
		else
			this.bloodslayerPoints = (int) bloodslayerPoints;
	}

	public int getPestcontrolpoints() {
		return this.pestcontrolpoints;
	}

	public void setPestcontrolpoints(int pestcontrolpoints, boolean add) {
		if (add)
			this.pestcontrolpoints += pestcontrolpoints;
		else
			this.pestcontrolpoints = pestcontrolpoints;
	}

	public int getLoyaltyPoints() {
		return (int) this.loyaltyPoints;
	}

	public void setLoyaltyPoints(int points, boolean add) {
		if (add)
			this.loyaltyPoints += points;
		else
			this.loyaltyPoints = points;
	}

	public void incrementLoyaltyPoints(double amount) {
		this.loyaltyPoints += amount;
	}

	public int getPkPoints() {
		return this.pkPoints;
	}

	public void setPkPoints(int points, boolean add) {
		if (add)
			this.pkPoints += points;
		else
			this.pkPoints = points;
	}

	public int getDungeoneeringTokens() {
		return dungTokens;
	}

	public void setDungeoneeringTokens(int dungTokens, boolean add) {
		if (add)
			this.dungTokens += dungTokens;
		else
			this.dungTokens = dungTokens;
	}

	public int getVotingPoints() {
		return votingPoints;
	}

	public void setVotingPoints(int votingPoints) {
		this.votingPoints = votingPoints;
	}

	public void incrementVotingPoints() {
		this.votingPoints++;
	}

	public void incrementVotingPoints(int amt) {
		this.votingPoints += amt;
	}

	public void setVotingPoints(int points, boolean add) {
		if (add)
			this.votingPoints += points;
		else
			this.votingPoints = points;
	}

	public int getAchievementPoints() {
		return achievementPoints;
	}

	public void setAchievementPoints(int points, boolean add) {
		if (add)
			this.achievementPoints += points;
		else
			this.achievementPoints = points;
	}

	
	public int getNPCKILLCount() {
		return npckillcount;
	}

	public void setNPCKILLCount(int npckillcount) {
		this.npckillcount = npckillcount;
	}

	public void incrementNPCKILLCount() {
		this.npckillcount++;
	}

	public void incrementNPCKILLCount(int amt) {
		this.npckillcount += amt;
	}

	public void setNPCKILLCount(int points, boolean add) {
		if (add)
			this.npckillcount += points;
		else
			this.npckillcount = points;
	}

	

	public int getSuperPoints() {
		// TODO Auto-generated method stub
		return 0;
	}


	public void setSuperPoints(int i, boolean b) {
		// TODO Auto-generated method stub
		
	}

	public void SuperPoints(int i) {
		// TODO Auto-generated method stub
		
	}
	
	
	public int getBossPoints() {
		// TODO Auto-generated method stub
		return 0;
	}


	public void setBossPoints(int i, boolean b) {
		// TODO Auto-generated method stub
		
	}

	public void BossPoints(int i) {
		// TODO Auto-generated method stub
		
	}
}