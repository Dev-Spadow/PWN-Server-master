package com.arlania.world.content;

import com.arlania.util.Misc;
import com.arlania.util.Stopwatch;
import com.arlania.world.World;

/*
 * @author Boomer
 * 
 */

public class Reminders {
	
	
    private static final int TIME = 600000; //10 minutes
	private static Stopwatch timer = new Stopwatch().reset();
	public static String currentMessage;
	
	/*
	 * Random Message Data
	 */
	private static final String[][] MESSAGE_DATA = { 
			{"<img=453>Join 'Help' CC For Help/Tips!"},
			{"<img=453>Do ::benefits To Check out donator Rank Benefits!"},
			{"<img=453>Never use the same password on different servers"},
			{"<img=453>Do ::guides To Check out some in depth server guides"},
			{"<img=453>Donate to help the server grow! ::store"},
			{"<img=453>Use the command ::dt to view drop tables"},
			{"<img=453>Make sure to join the discord for exclusive giveaways!"},
			{"<img=453>Alot of important guides can be found at ::guides"},
			{"<img=453>Donate to help the server grow! ::store"},
			{"<img=453>::vote for the server for free rewards!"},
			{"<img=453>Use the ::help command to ask staff for help"},
			{"<img=453>Join 'Help' CC For Help/Tips!"},
			{"<img=453>Make sure to read the forums for information visit Pwnlite317.net"},
			{"<img=453>Make sure to join the discord for exclusive giveaways!"},
			{"<img=453>Alot of important guides can be found at ::guides"},
			{"<img=453>Never use the same password on different servers"},
			{"<img=453>Remember to spread the word and invite your friends to play!"},
			{"<img=453>Make sure to join the discord for exclusive giveaways!"},
			{"<img=453>Use ::commands to find a list of commands"},
			{"<img=453>::vote for the server for free rewards!"},
			{"<img=453>If you'd like to get some good discounts on donating PM @red@Boomer"},
			{"<img=453>Available donation methods are Paypal,Cashapp,Bitcoin"},
			{"<img=453>Donate to help the server grow! ::store"},
			{"<img=453>Join 'Help' CC For Help/Tips!"},
			{"<img=453>Make sure to join the discord for exclusive giveaways!"},
			{"<img=453>Never use the same password on different servers"},
			{"<img=453>Alot of important guides can be found at ::guides"},
			{"<img=453>Toggle your client settings to your preference in the wrench tab!"},
			{"<img=453>Make sure to join the discord for exclusive giveaways!"},
			{"<img=453>Register and post on the forums to keep them active! ::Forum"},
			{"<img=453>If you'd like to get some good discounts on donating PM @red@Boomer"},
			{"<img=453>Use the command ::dt to view drop tables"},
			{"<img=453>Did you know you can get paid to make videos? @red@PM Boomer"},
			{"<img=453>Donate to help the server grow! ::store"},
			{"<img=453>Never use the same password on different servers"},
			{"<img=453>Use the command ::dt to view drop tables"},
			{"<img=453>Make sure to join the discord for exclusive giveaways!"},
			{"<img=453>Make sure you ::vote everyday for amazing Rewards!"},
			{"<img=453>Alot of important guides can be found at ::guides"},
			{"<img=453>If you'd like to get some good discounts on donating PM @red@Boomer"},
			{"<img=453>Please do NOT use same password on other servers!"},
			{"<img=453>Dont forget to set a bank pin"},
	         
	
	
	
	};

	/*
	 * Sequence called in world.java
	 * Handles the main method
	 * Grabs random message and announces it
	 */
	public static void sequence() {
		if(timer.elapsed(TIME)) {
			timer.reset();
			{
				
			currentMessage = MESSAGE_DATA[Misc.getRandom(MESSAGE_DATA.length - 1)][0];
			World.sendMessage(currentMessage);
			World.savePlayers();
					
				}
				
			World.savePlayers();
			}
		

          }

}