package com.arlania.world.content;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import com.arlania.model.Item;
import com.arlania.util.Misc;
import com.arlania.world.World;
import com.arlania.world.entity.impl.player.Player;

public class GpayDonationManager {

	public static void gpay(Player c, String username) {
		 try {
		  username = username.replaceAll(" ", "_");
		  String secret = "e235fb76147efaf1baa3bcc9e6c5b575"; //YOUR SECRET KEY!
		  URL url = new URL("http://app.gpay.io/api/runescape/" + username + "/" + secret);
		  BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
		  String results = reader.readLine();
		  if (results.toLowerCase().contains("!error:")) {
		   //Logger.log(this, "[GPAY]"+results);
		  } else {
		   String[] ary = results.split(",");
		   for (int i = 0; i < ary.length; i++) {
		    switch (ary[i]) {
		     case "0":
		    	 c.getPacketSender().sendMessage("You have no donations to claim");
		      break;
		     case "48421": //product ids can be found on the webstore page
		    	 c.getInventory().add(13691, 1);
		    	 World.sendMessage( "@red@" + c.getUsername() + " claimed a Battle-Pass donation!");
		      break;
		     case "48422": //product ids can be found on the webstore page
		    	 c.getInventory().add(14512, 1);
		    	 World.sendMessage( "@red@" + c.getUsername() + " claimed a Custom Island donation!");
		      break;
		     case "48423": //product ids can be found on the webstore page
		    	 c.getInventory().add(6638, 1);
		    	 World.sendMessage( "@red@" + c.getUsername() + " claimed @blu@x1@red@ Squeal of fortune donation!");
		      break;
		     case "48424": //product ids can be found on the webstore page
		    	 c.getInventory().add(12162, 1);
		    	 World.sendMessage( "@red@" + c.getUsername() + " claimed a Lazy Charm (100KC) donation!");
		      break;
		     case "48425": //product ids can be found on the webstore page
		    	 c.getInventory().add(995, 1);
		    	 World.sendMessage( "@red@" + c.getUsername() + " claimed a Mini-me pet donation!");
		      break;
		     case "48426": //product ids can be found on the webstore page
		    	 c.getInventory().add(8566, 1);
		    	 World.sendMessage( "@red@" + c.getUsername() + " claimed a Pwnlite Aura donation!");
		      break;
		     case "48427": //product ids can be found on the webstore page
		    	 c.getInventory().add(5197, 1);
		    	 World.sendMessage( "@red@" + c.getUsername() + " claimed a 50% Droprate charm donation!");
		      break;
		     case "48428": //product ids can be found on the webstore page
		    	 c.getInventory().add(16456, 10);
		    	 World.sendMessage( "@red@" + c.getUsername() + " claimed @blu@x10 @red@Grand Mystery Chest donation!");
		      break;
		     case "48429": //product ids can be found on the webstore page
		    	 c.getInventory().add(16456, 5);
		    	 World.sendMessage( "@red@" + c.getUsername() + " claimed @blu@x5 @red@Grand Mystery Chest donation!");
		      break;
		     case "48430": //product ids can be found on the webstore page
		    	 c.getInventory().add(16456, 1);
		    	 World.sendMessage( "@red@" + c.getUsername() + " claimed @blu@x1 @red@Grand Mystery Chest donation!");
		      break;
		     case "48431": //product ids can be found on the webstore page
		    	 c.getInventory().add(6191, 1);
		    	 World.sendMessage( "@red@" + c.getUsername() + " claimed a @red@Stoner Bong donation!");
		      break;
		     case "48432": //product ids can be found on the webstore page
		    	 c.getInventory().add(9943, 1);
		    	 World.sendMessage( "@red@" + c.getUsername() + " claimed a @red@Oz of Jane donation!");
		      break;
		     case "48433": //product ids can be found on the webstore page
		    	 c.getInventory().add(8461, 1);
		    	 World.sendMessage( "@red@" + c.getUsername() + " claimed a @red@Nike Court Sneakers donation!");
		      break;
		     case "48434": //product ids can be found on the webstore page
		    	 c.getInventory().add(8699, 1);
		    	 World.sendMessage( "@red@" + c.getUsername() + " claimed a @red@Boomer Dreampass donation!");
		      break;
		     case "48435": //product ids can be found on the webstore page
		    	 c.getInventory().add(6942, 1);
		    	 World.sendMessage( "@red@" + c.getUsername() + " claimed a @red@Corrupt Bunny Mask donation!");
		      break;
		     case "48436": //product ids can be found on the webstore page
		    	 c.getInventory().add(8654, 1);
		    	 World.sendMessage( "@red@" + c.getUsername() + " claimed a @red@Imbued Weapon donation!");
		      break;
		     case "48437": //product ids can be found on the webstore page
		    	 c.getInventory().add(5239, 1);
		    	 World.sendMessage( "@red@" + c.getUsername() + " claimed a @red@Owners Necklace donation!");
		      break;
		     case "48438": //product ids can be found on the webstore page
		    	 c.getInventory().add(5238, 1);
		    	 World.sendMessage( "@red@" + c.getUsername() + " claimed a @red@Owners Ring donation!");
		      break;
		     case "48439": //product ids can be found on the webstore page
		    	 c.getInventory().add(6482, 1);
		    	 World.sendMessage( "@red@" + c.getUsername() + " claimed a @red@Alpha Donator Cape donation!");
		      break;
		     case "48440": //product ids can be found on the webstore page
		    	 c.getInventory().add(3920, 1);
		    	 World.sendMessage( "@red@" + c.getUsername() + " claimed a @red@Owners staff donation!");
		      break;
		     case "48441": //product ids can be found on the webstore page
		    	 c.getInventory().add(16501, 1);
		    	 World.sendMessage( "@red@" + c.getUsername() + " claimed a @red@CUSTOM Armor set donation!");
		      break;
		     case "48442": //product ids can be found on the webstore page
		    	 c.getInventory().add(925, 1);
		    	 World.sendMessage( "@red@" + c.getUsername() + " claimed a @red@Lucid blade donation!");
		      break;
		     case "48443": //product ids can be found on the webstore page
		    	 c.getInventory().add(13201, 1);
		    	 World.sendMessage( "@red@" + c.getUsername() + " claimed a @red@Supreme herbal bow donation!");
		      break;
		     case "48444": //product ids can be found on the webstore page
		    	 c.getInventory().add(5129, 1);
		    	 World.sendMessage( "@red@" + c.getUsername() + " claimed a @red@Heated battlestaff donation!");
		      break;
		     case "48445": //product ids can be found on the webstore page
		    	 c.getInventory().add(4742, 1);
		    	 World.sendMessage( "@red@" + c.getUsername() + " claimed a @red@Eevee pet donation!");
		      break;
		     case "48446": //product ids can be found on the webstore page
		    	 c.getInventory().add(774, 1);
		    	 World.sendMessage( "@red@" + c.getUsername() + " claimed a @red@perfect' necklace donation!");
		      break;
		     case "48447": //product ids can be found on the webstore page
		    	 c.getInventory().add(773, 1);
		    	 World.sendMessage( "@red@" + c.getUsername() + " claimed a @red@perfect' ring donation!");
		      break;
		     case "48448": //product ids can be found on the webstore page
		    	 c.getInventory().add(10205, 1);
		    	 World.sendMessage( "@red@" + c.getUsername() + " claimed a @red@Bond Casket donation");
		      break;
		     case "48449": //product ids can be found on the webstore page
		    	 c.getInventory().add(14033, 1);
		    	 World.sendMessage( "@red@" + c.getUsername() + " claimed @blu@x1 @red@Green arsenic chest donation");
		      break;
		     case "48450": //product ids can be found on the webstore page
		    	 c.getInventory().add(14033, 5);
		    	 World.sendMessage( "@red@" + c.getUsername() + " claimed @blu@x5 @red@Green arsenic chest donation");
		      break;
		     case "48451": //product ids can be found on the webstore page
		    	 c.getInventory().add(14033, 10);
		    	 World.sendMessage( "@red@" + c.getUsername() + " claimed @blu@x10 @red@Green arsenic chest donation");
		      break;
		     case "48452": //product ids can be found on the webstore page
		    	 c.getInventory().add(16474, 1);
		    	 World.sendMessage( "@red@" + c.getUsername() + " claimed a Oink mask donation");
		      break;
		     case "48453": //product ids can be found on the webstore page
		    	 c.getInventory().add(16498, 1);
		    	 World.sendMessage( "@red@" + c.getUsername() + " claimed a Custom Pet Token donation");
		      break;
		     case "48454": //product ids can be found on the webstore page
		    	 c.getInventory().add(16500, 1);
		    	 World.sendMessage( "@red@" + c.getUsername() + " claimed a Custom Owner Weapon Token donation");
		      break;
		     case "48455": //product ids can be found on the webstore page
		    	 c.getInventory().add(16499, 1);
		    	 World.sendMessage( "@red@" + c.getUsername() + " claimed a Custom owner cape Token donation");
		      break;
		     case "48456": //product ids can be found on the webstore page
		    	 c.getInventory().add(16488, 1);
		    	 World.sendMessage( "@red@" + c.getUsername() + " claimed @blu@x1 @red@Blue arsenic chest donation");
		      break;
		     case "48457": //product ids can be found on the webstore page
		    	 c.getInventory().add(16488, 5);
		    	 World.sendMessage( "@red@" + c.getUsername() + " claimed @blu@x5 @red@Blue arsenic chest donation");
		      break;
		     case "48458": //product ids can be found on the webstore page
		    	 c.getInventory().add(16488, 10);
		    	 World.sendMessage( "@red@" + c.getUsername() + " claimed @blu@x10 @red@Blue arsenic chest donation");
		      break;
		     case "48459": //product ids can be found on the webstore page
		    	 c.getInventory().add(19821, 1);
		    	 World.sendMessage( "@red@" + c.getUsername() + " claimed a Thunderlight boots donation");
		      break;
		     case "48460": //product ids can be found on the webstore page
		    	 c.getInventory().add(4803, 1);
		    	 World.sendMessage( "@red@" + c.getUsername() + " claimed a Bloodshed boots donation");
		      break;
		     case "48461": //product ids can be found on the webstore page
		    	 c.getInventory().add(5052, 1);
		    	 World.sendMessage( "@red@" + c.getUsername() + " claimed a Alpha boots donation");
		      break;
		     case "48462": //product ids can be found on the webstore page
		    	 c.getInventory().add(14808, 1);
		    	 World.sendMessage( "@red@" + c.getUsername() + " claimed a Happy hour scroll donation");
		      break;
		     case "48463": //product ids can be found on the webstore page
		    	 c.getInventory().add(17750, 10_000);
		    	 World.sendMessage( "@red@" + c.getUsername() + " claimed @blu@10k@red@ Bloodbags donation");
		      break;
		     case "48464": //product ids can be found on the webstore page
		    	 c.getInventory().add(17750, 5_000);
		    	 World.sendMessage( "@red@" + c.getUsername() + " claimed @blu@5k@red@ Bloodbags donation");
		      break;
		     case "48466": //product ids can be found on the webstore page
		    	 c.getInventory().add(12852, 1_000);
		    	 World.sendMessage( "@red@" + c.getUsername() + " claimed @blu@x1k@red@ Salvage tokens donation");
		      break;
		     case "48467": //product ids can be found on the webstore page
		    	 c.getInventory().add(12852, 3_000);
		    	 World.sendMessage( "@red@" + c.getUsername() + " claimed @blu@x5k@red@ Salvage tokens donation");
		      break;
		     case "48468": //product ids can be found on the webstore page
		    	 c.getInventory().add(5266, 25);
		    	 World.sendMessage( "@red@" + c.getUsername() + " claimed @blu@x25@red@ Grand donation keys donation");
		      break;
		     case "48469": //product ids can be found on the webstore page
		    	 c.getInventory().add(5266, 5);
		    	 World.sendMessage( "@red@" + c.getUsername() + " claimed @blu@x5@red@ Grand donation keys donation");
		      break;
		     case "48470": //product ids can be found on the webstore page
		    	 c.getInventory().add(5266, 1);
		    	 World.sendMessage( "@red@" + c.getUsername() + " claimed @blu@x1@red@ Grand donation key donation");
		      break;
		     case "48471": //product ids can be found on the webstore page
		    	 c.getInventory().add(14549, 1);
		    	 World.sendMessage( "@red@" + c.getUsername() + " claimed a Fuzed Diamond donation");		
		    	 break;
		     case "48472": //product ids can be found on the webstore page
		    	 c.getInventory().add(10168, 25);
		    	 World.sendMessage( "@red@" + c.getUsername() + " claimed @blu@x25@red@ Owners boxes donation");
		      break;
		     case "48473": //product ids can be found on the webstore page
		    	 c.getInventory().add(10168, 10);
		    	 World.sendMessage( "@red@" + c.getUsername() + " claimed @blu@x10@red@ Owners boxes donation");
		      break;
		     case "48474": //product ids can be found on the webstore page
		    	 c.getInventory().add(10168, 1);
		    	 World.sendMessage( "@red@" + c.getUsername() + " claimed @blu@x1@red@ Owners box donation");
		      break;
		     case "48475": //product ids can be found on the webstore page
		    	 c.getInventory().add(19727, 1);
		    	 World.sendMessage( "@red@" + c.getUsername() + " claimed a Supreme Staff donation");		
		    	 break;
		     case "48476": //product ids can be found on the webstore page
		    	 c.getInventory().add(933, 1);
		    	 World.sendMessage( "@red@" + c.getUsername() + " claimed a Celestial Glaive donation");		
		    	 break;
		     case "48477": //product ids can be found on the webstore page
		    	 c.getInventory().add(16496, 1);
		    	 World.sendMessage( "@red@" + c.getUsername() + " claimed a Blood Arsenic Set Package donation");		
		    	 break;
		     case "48478": //product ids can be found on the webstore page
		    	 c.getInventory().add(11978, 1);
		    	 World.sendMessage( "@red@" + c.getUsername() + " claimed a X2 KC pet (Yk'lagor) donation");		
		    	 break;
		     case "48479": //product ids can be found on the webstore page
		    	 c.getInventory().add(3961, 1);
		    	 World.sendMessage( "@red@" + c.getUsername() + " claimed a Eternal Potion(Unlimited) donation");		
		    	 break;
		     case "48480": //product ids can be found on the webstore page
		    	 c.getInventory().add(19886, 1);
		    	 World.sendMessage( "@red@" + c.getUsername() + " claimed a Collector's Necklace donation");		
		    	 break;
		     case "48481": //product ids can be found on the webstore page
		    	 c.getInventory().add(5185, 1);
		    	 World.sendMessage( "@red@" + c.getUsername() + " claimed a Infinite Overload potion donation");		
		    	 break;
		     case "48482": //product ids can be found on the webstore page
		    	 c.getInventory().add(16641, 1);
		    	 World.sendMessage( "@red@" + c.getUsername() + " claimed @blu@x1@red@ Minigame Chest Key donation");
		      break;
		     case "48483": //product ids can be found on the webstore page
		    	 c.getInventory().add(16641, 10);
		    	 World.sendMessage( "@red@" + c.getUsername() + " claimed @blu@x10@red@ Minigame Chest Key donation");
		      break;
		     case "48484": //product ids can be found on the webstore page
		    	 c.getInventory().add(298, 1);
		    	 World.sendMessage( "@red@" + c.getUsername() + " claimed @blu@x1@red@ Cosmetic Override key donation");
		      break;
		     case "48485": //product ids can be found on the webstore page
		    	 c.getInventory().add(1543, 1);
		    	 World.sendMessage( "@red@" + c.getUsername() + " claimed @blu@x1@red@ Medium key donation");
		      break;
		     case "48486": //product ids can be found on the webstore page
		    	 c.getInventory().add(16641, 25);
		    	 World.sendMessage( "@red@" + c.getUsername() + " claimed @blu@x25@red@ Minigame Chest Key donation");
		      break;
		     case "48487": //product ids can be found on the webstore page
		    	 c.getInventory().add(15374, 1);
		    	 World.sendMessage( "@red@" + c.getUsername() + " claimed @blu@x1@red@ Elemental Mystery box donation");
		      break;
		     case "48488": //product ids can be found on the webstore page
		    	 c.getInventory().add(6199, 1);
		    	 World.sendMessage( "@red@" + c.getUsername() + " claimed @blu@x1@red@ Gracious Mystery box donation");
		      break;
		     case "48489": //product ids can be found on the webstore page
		    	 c.getInventory().add(7047, 1);
		    	 World.sendMessage( "@red@" + c.getUsername() + " claimed a Easter bunny mask donation");			      break;
		    }
		   }
		  }
		 } catch (IOException e) {}
		}
}
