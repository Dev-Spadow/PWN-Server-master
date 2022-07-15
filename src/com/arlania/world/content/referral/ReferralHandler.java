package com.arlania.world.content.referral;

import com.arlania.model.Item;
import com.arlania.world.content.PlayerPunishment;
import com.arlania.world.entity.impl.player.Player;
import net.dv8tion.jda.api.entities.User;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ReferralHandler {

	private static final String REFERRAL_DIR = "./data/referrals/";

	private static final String FILE = "referral_data.txt";

	public static ArrayList<String> REFERRALS = new ArrayList<>();

	public static void initializeList(String directory, String file, ArrayList<String> list) {
		try {
			BufferedReader in = new BufferedReader(new FileReader(directory+file));
			String data = null;
			while ((data = in.readLine()) != null) {
				list.add(data);
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Fill the list with users who have claimed a referral previously
	 * This is called when the server launches
	 */
	public static void init() {
		initializeList(REFERRAL_DIR, FILE, REFERRALS);
		System.out.println("Referrals successfully initialised");
	}

	public static void checkReferralList() {
		System.out.println(REFERRALS);
	}

	public static void addReferralClaim(String IP, String Username, String MAC) {
		if (!REFERRALS.contains(IP)) {
			PlayerPunishment.addToFile(REFERRAL_DIR + FILE, Username + "\n" + IP);
			REFERRALS.add(IP);
		}
		if (!REFERRALS.contains(MAC)) {
			PlayerPunishment.addToFile(REFERRAL_DIR + FILE, Username + "\n" + MAC);
			REFERRALS.add(MAC);
		}
		if (!REFERRALS.contains(Username)) {
			PlayerPunishment.addToFile(REFERRAL_DIR + FILE, Username + "\n");
			REFERRALS.add(Username);
		}
	}

	public static boolean claimedReferral(String IP, String Username, String MAC) {
		return REFERRALS.contains(IP) || REFERRALS.contains(Username) || REFERRALS.contains(MAC);
	}
	
	public void openRefferal(Player player) {
		clearInterface(player);
		player.getRefferalHandler().setRefferalType(ReferralType.YOUTUBERS);
		player.getRefferalHandler().sendOptions(player);
		player.getRefferalHandler().sendRewards(player);
		player.getPA().sendInterface(57750);
	}
	
	public void sendOptions(Player player) {
		int start = 57768;
		for(ReferralOptions options : ReferralOptions.values()) {
			if(options.getType() == player.getRefferalHandler().getRefferalType()) {
				for(String option : options.getOptions()) {
					player.getPacketSender().sendString(start, option);
					start += 2;
				}
			}
		}
	}

	/** Sends the items onto the interface
	 *
	 * @param player
	 */
	public void sendRewards(Player player) {
		for(ReferralOptions options : ReferralOptions.values()) {
			if(options.getType() == player.getRefferalHandler().getRefferalType()) {
				player.getPA().sendItemContainer(options.getRewards(), 57766);
			}
		}
	}

	public void confirmOption(Player player) {
		if (!claimedReferral(player.getHostAddress(), player.getUsername().toUpperCase(), player.getMacAddress())) {

			for (ReferralOptions options : ReferralOptions.values()) {
				if (options.getType() == player.getRefferalHandler().getRefferalType()) {
					for (Item item : options.getRewards()) {
						player.getInventory().addItem(item);
					}
				}
			}
			addReferralClaim(player.getHostAddress(), player.getUsername().toUpperCase(), player.getMacAddress());
			System.out.println(player.getUsername() + " has just claimed a referral. Adding data to referral claims log.");
			System.out.println("DATA: " + player.getHostAddress() + player.getUsername().toUpperCase() + player.getMacAddress());
			player.referaledby = player.getRefferalHandler().getOptionSelected();
			player.getPA().sendMessage("thank you");
		}

		player.getPacketSender().sendInterface(3559);
		player.getAppearance().setCanChangeAppearance(true);
	}
	
	private void clearInterface(Player player) {
		int start = 0;
		for(int i = 0; i < 10; i++) {
			player.getPacketSender().sendString(57768 + start, "");
			start += 2;
		}
	}
	
	private ReferralType refType;

	public ReferralType getRefferalType() {
		return refType;
	}
	
	public void setRefferalType(ReferralType refType) {
		this.refType = refType;
	}
	
	private String optionSelected;
	
	public String getOptionSelected() {
		return optionSelected;
	}

	public void setOptionSelected(String optionSelected) {
		this.optionSelected = optionSelected;
	}
}
