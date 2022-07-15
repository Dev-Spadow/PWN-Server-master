package com.arlania.world.content.teleport;

import com.arlania.model.Item;
import com.arlania.model.Position;
import com.arlania.world.content.teleport.TeleportPage;

import java.util.ArrayList;

public enum TeleportData {

    /*
     * Monster Teleports
     */
	NOOBS(0, "Starter Zone", 11204, new Position(3795, 3551, 0), new Item[] {new Item(13258, 1), new Item (13259, 1), new Item (13256, 1), new Item (989, 1),new Item(15373, 1),new Item(19080, 1)}
    , new String[] {"Drops Beginner Items","@red@HP:@gre@ 150-600"}),
	MR_INCREDIBLE(0, "Mr Incredible", 12833, new Position(2659, 3429, 0), new Item[] {new Item(19137, 1), new Item (19138, 1), new Item (19139, 1), new Item (7159, 1), new Item (15373, 1), new Item (5130, 1), new Item (3073, 1),  new Item (6733, 1),new Item (6041, 1)}
    , new String[] {"Drops the Golden Minigun","@red@HP:@gre@ 800"}),
	LIGHTREAPERS(0, "Lightreapers", 51, new Position(2528, 5859, 4), new Item[] {new Item(19131, 1), new Item (19132, 1), new Item (19133, 1), new Item (18871, 1), new Item (18865, 1), new Item (18872, 1)}
    , new String[] {"Drops the Lightreaper set","@red@HP:@gre@ 1000"}),
	SIRENIC(0, "Sirenic Ogres", 2783, new Position(1640, 5604, 0), new Item[] {new Item (15373, 1), new Item (18942, 1), new Item (18941, 1), new Item (18940, 1), new Item (922, 1), new Item (10835, 1), new Item (20002, 1)}
    , new String[] {"Drops Sirenic set","@red@HP:@gre@ 1500"}),
	HERCULES(0, "Hercules", 17, new Position(2791, 4634, 0), new Item[] {new Item(6199, 1), new Item (1543, 1), new Item (2749, 1), new Item (2750, 1), new Item (2751, 1), new Item (2752, 1), new Item (2753, 1), new Item (2754, 1), new Item (13261, 1), new Item (18865, 1)}
    , new String[] {"Drops Hercules set","@red@HP:@gre@ 1750"}),
	CRASH(0, "Crash Bandicoot", 12839, new Position( 2911, 4773, 0 ), new Item[] {new Item(19721, 1), new Item (19722, 1), new Item (19723, 1), new Item (19734, 1), new Item (19736, 1), new Item (15418, 1), new Item (19468, 1), new Item (18363, 1), new Item (18373, 1)}
    , new String[] {"Drops blessed set","@red@KC REQ:  15 Hercules","@red@HP:@gre@ 2000"}),
	ZEUS(0, "Zeus@or2@(Multi Boss)", 15, new Position(2079, 3667, 0), new Item[] {new Item(1464, 1), new Item (19721, 1), new Item (19722, 1), new Item (19723, 1), new Item (19724, 1), new Item (15373, 1), new Item (16137, 1), new Item (11076, 1), new Item (18363, 1)}
    , new String[] {"Drops gear/@or2@$1 Tickets","@red@KC REQ: 15 Crash","@red@HP:@gre@ 7500"}),
	CHARIZARD(0, "Charizard", 1982, new Position(3295, 2963, 0), new Item[] {new Item(18380, 1), new Item (18381, 1), new Item (18382, 1), new Item (18383, 1), new Item (18384, 1), new Item (3941, 1), new Item (18392, 1), new Item (9006, 1), new Item (3941, 1)}
    , new String[] {"Drops blazed set","and Charizard pet!","@red@KC REQ:  25 Crash","@red@HP:@gre@ 2500"}),
	JINIS(0, "Jinis", 9994, new Position(2720, 9828, 0), new Item[] {new Item(4799, 1), new Item (4800, 1), new Item (4801, 1), new Item (5079, 1), new Item (3973, 1), new Item (3951, 1)}
    , new String[] {"Magical Jinis",//test
    "@red@KC REQ: 30 Charizards","@red@HP:@gre@ 3000"}),
	GODZILLA(0, "Godzilla", 9932, new Position(2655, 4829, 0), new Item[] {new Item(3960, 1), new Item (3958, 1), new Item (3959, 1), new Item (5186, 1), new Item (3316, 1), new Item (14559, 1)}
	, new String[] {"This NPC drops","The Rex set",
			"@red@KC REQ: 30 Jinis","@red@HP:@gre@ 3250"}),
	NARUTO(0, "Naruto@or2@(Multi Boss)", 11813, new Position(2397, 3555, 0), new Item[] {new Item(17835, 1), new Item (11162, 1), new Item (11132, 1), new Item (5184, 1), new Item (18950, 1), new Item (4673, 1)}
	, new String[] {"This NPC drops","misc items",
	"@red@KC REQ: 50 Godzilla","@red@HP:@gre@ 30k"}),
	INFERNALS(0, "Bloated infernals", 1999, new Position(1240, 1260, 0), new Item[] {new Item(5131, 1), new Item (4772, 1), new Item (4771, 1), new Item (4770, 1), new Item (18347, 1), new Item (13239, 1)}
	, new String[] {"This NPC drops","Misc Gear",
	"@red@KC REQ: 50 Godzilla","@red@HP:@gre@ 4000"}),
	/*
     * Dungeon Teleports
     */
	HADES(1, "Hades", 16, new Position(2080, 3748, 0), new Item[] {new Item(6193, 1), new Item (6194, 1), new Item (6195, 1), new Item (6196, 1), new Item (6197, 1), new Item (6198, 1)}
    , new String[] {"This NPC drops","the Hades set",
    "@red@KC REQ: 50 Bloated","@red@Infernals","@red@HP:@gre@ 6000"}),
	VADER(1, "Darth Vader", 11, new Position(2088, 3995, 0), new Item[] {new Item(3276, 1), new Item (3271, 1), new Item (3272, 1), new Item (3273, 1), new Item (3283, 1), new Item (3284, 1)}
    , new String[] {"@red@HP:@gre@ 10k",
    "@red@KC REQ: 75 Hades"}),
	DANTES_SATAN(1, "Dantes Satan", 6303, new Position(2784, 3858, 0), new Item[] {new Item(4001, 1), new Item (3999, 1), new Item (4000, 1), new Item (3980, 1), new Item (18957, 1), new Item (18955, 1)}
    , new String[] {"@red@HP:@gre@ 15k",
    "@red@KC REQ: 100 Vaders"}),
	RICK(1, "Rick", 9273, new Position(2369, 4944, 0), new Item[] {new Item(15649, 1), new Item (15650, 1), new Item (15651, 1), new Item (15654, 1), new Item (15655, 1), new Item (5167, 1)}
    , new String[] {"@red@HP:@gre@ 20k",
    "@red@KC REQ: 125 Dantes Satans"}),
	KING_KONG(1, "King Kong", 9903, new Position(2722, 9886, 0), new Item[] {new Item(5089, 1),new Item(4761, 1), new Item (4762, 1), new Item (4763, 1), new Item (4765, 1), new Item (4764, 1), new Item (3905, 1)}
    , new String[] {"@red@HP:@gre@ 25k",
    "@red@KC REQ: 150 Rick"}),
	ASUNA(1, "Asuna@or2@(Multi Boss)", 12823, new Position(3483, 3094, 0), new Item[] {new Item(2545, 1),new Item(2546, 1), new Item (2547, 1), new Item (2548, 1), new Item (5184, 1), new Item (4670, 1)}
    , new String[] {"@red@HP:@gre@ 200k",
    "@red@KC REQ: 150 King Kong"}),
	CORPOREAL_BEAST(2, "Corp Beast@or2@(Multi Boss)", 8133, new Position(2886, 4376, 0), new Item[] {new Item(926, 1), new Item (931, 1), new Item (930, 1), new Item (5210, 1), new Item (5211,1), new Item (15045, 1)}
    , new String[] {"@red@HP:@gre@ 250k",
    "@red@KC REQ: 200 King Kong"}),
	LUCID(1, "Lucid Dragons", 9247, new Position(2557, 4953, 0), new Item[] {new Item(5173, 1), new Item (3820, 1), new Item (3821, 1), new Item (3822, 1), new Item (20054, 1)}
    , new String[] {"@red@HP:@gre@ 30k",
    "@red@KC REQ: 200 King Kong"}),
	HULK(1, "Hulk", 8493, new Position(3856, 5841, 0), new Item[] {new Item(5195, 1), new Item (4785, 1), new Item (20240, 1), new Item (4781, 1), new Item (4782, 1), new Item (15032, 1)}
    , new String[] {"@red@HP:@gre@ 40k",
    "@red@KC REQ: 300 Lucid Dragons"}),
	WIZARDS(1, "Dark Wizards", 9203, new Position(2920, 9687, 0), new Item[] {new Item(15656, 1), new Item (5084, 1), new Item (5083, 1), new Item (5082, 1), new Item (3985, 1), new Item (17151, 1)}
    , new String[] {"@red@HP:@gre@ 50k",
    "@red@KC REQ: 300 Hulk"}),
	PYROS(1, "Heated Pyros", 172, new Position(2334, 4576, 0), new Item[] {new Item(19619, 1), new Item (19470, 1), new Item (19471, 1), new Item (19473, 1), new Item (19474, 1), new Item (5129, 1)}
    , new String[] {"@red@HP:@gre@ 60k",

    /*
     * Boss Teleports
     */

    "@red@KC REQ:500 Dark wizards"}),
	PURPLEFIRE_WYRM(2, "Dark Purple Wyrm", 9935, new Position(2340, 3822, 0), new Item[] {new Item(4643, 1), new Item (4641, 1), new Item (4642, 1), new Item (3983, 1), new Item (3064, 1), new Item (20570, 1)}
    , new String[] {"@red@HP:@gre@ 65k",
    "@red@KC REQ: 500 Heated pyros"}),
	TRINITY(2, "Trinity", 170, new Position(2325, 4586, 0), new Item[] {new Item(19618, 1), new Item (19691, 1), new Item (19692, 1), new Item (19693,1), new Item (19694, 1), new Item (19696, 1)}
    , new String[] {"@red@HP:@gre@ 75k",
    "@red@KC REQ: 750 Purple Wyrms"}),
	CLOUD(2, "Cloud@or2@(Multi Boss)", 169, new Position(2539, 5774, 0), new Item[] {new Item(19159, 1), new Item (19160, 1), new Item (19161, 1), new Item (19163, 1), new Item (19164,1), new Item (19166, 1)}
    , new String[] {"@red@HP:@gre@ 1m",
    "@red@KC REQ: 750 Trinity"}),
	HERBAL(2, "Herbal Rogue", 219, new Position(2018, 4440, 0), new Item[] {new Item(9492, 1), new Item (9493, 1), new Item (9494, 1), new Item (9495, 1), new Item (10835, 500), new Item (19935, 1)}
    , new String[] {"@red@HP:@gre@ 100k",
    "@red@KC REQ: 750 Trinity"}),
	EXODEN(2, "Exoden", 12239, new Position(1956, 5022, 0), new Item[] {new Item(14494, 1), new Item (14492, 1), new Item (14490, 1), new Item (2760, 1), new Item (19935, 1), new Item (10835, 550)}
    , new String[] {"@red@HP:@gre@ 125k",
    "@red@KC REQ: 1k Herbal Rogue"}),
	NEX(2, "Supreme Nex@or2@(Multi Boss)", 3154, new Position(2325, 4763, 0), new Item[] {new Item(19727, 1), new Item (19730,1), new Item (19731, 1), new Item (19732, 1), new Item (19728, 1), new Item (19729, 1)}
    , new String[] {"@red@HP:@gre@ 150k",
    "@red@KC REQ: 1k Exoden"}),
	STORMBREAKER(2, "Storm Breaker", 527, new Position(3226, 2844, 0), new Item[] {new Item(13206, 1), new Item (13202, 1), new Item (13203, 1), new Item (13204, 1), new Item (13205, 1), new Item (13207, 1)}
    , new String[] {"@red@HP:@gre@ 200k",
    "@red@KC REQ: 1k Exoden"}),
	APOLLO(2, "Apollo Ranger", 1684, new Position(3166, 9816, 0), new Item[] {new Item(11143, 1), new Item (11144, 1), new Item (11145,1), new Item (11146, 1), new Item (11147, 1), new Item (19935, 1)}
    , new String[] {"@red@HP:@gre@ 250k",
    "@red@KC REQ: 1k Stormbreakers"}),
	TROLL(2, "Noxious Troll", 5957, new Position(2402, 9251, 0), new Item[] {new Item(4796, 1), new Item (4797, 1), new Item (4794, 1), new Item (4795, 1), new Item (19127, 1), new Item (19128, 1)}
    , new String[] {"@red@HP:@gre@ 275k",
    "@red@KC REQ: 1k Apollo Rangers"}),
	AZAZEL(2, "Azazel Beast", 5958, new Position(2468, 3372, 0), new Item[] {new Item(13995, 1), new Item (13992, 1), new Item (13994, 1), new Item (13993, 1), new Item (13991,1), new Item (14448, 1)}
    , new String[] {"@red@HP:@gre@ 300k",
    "@red@KC REQ: 1k Noxious Trolls"}),
	RAVANA(2, "Ravana", 5959, new Position(3595, 3492, 0), new Item[] {new Item(10905, 1), new Item (9496, 1), new Item (9497, 1), new Item (9498, 1), new Item (19155, 1), new Item (15374, 1)}
    , new String[] {"@red@HP:@gre@ 350k",
    "@red@KC REQ: 1.25k Azazel Beasts"}),
    /*
     * Minigames Teleports
     */

	LUMINITIOS(3, "Luminitous Warriors", 185, new Position(2525, 4776, 0), new Item[] {new Item(19741, 1), new Item (19742, 1), new Item (19743, 1), new Item (19744, 1), new Item (19154, 1), new Item (13997, 1)}
    , new String[] {"@red@HP:@gre@ 400k",
    "@red@KC REQ: 1.25k Ravanas"}),
	HELLHOUND(3, "Custom Hellhounds", 6311, new Position(3176, 3029, 0), new Item[] {new Item(20427, 1), new Item (20431, 1), new Item (19936, 1), new Item (6640, 1), new Item (10835, 25)}
    , new String[] {"@red@HP:@gre@ 450k",
    "@red@KC REQ: 1.25k Luminitous warriors"}),
	RAZORSPAWN(3, "Razorspawn", 2907, new Position(3164, 4827, 0), new Item[] {new Item(5228, 1), new Item (5229, 1), new Item (5230, 1), new Item (5226, 1), new Item (5227, 1), new Item (5231, 1)}
    , new String[] {"@red@HP:@gre@ 500k",
    "@red@KC REQ: 1.5k Hellhounds"}),
	DREAMFLOW(3, "Dreamflow Assassin", 20, new Position(2719, 4837, 0), new Item[] {new Item(16443, 1), new Item (16444, 1), new Item (16445, 1), new Item (16446, 1), new Item (16448,1), new Item (16449, 1)}
    , new String[] {"@red@HP:@gre@ 750k",
    "@red@KC REQ: 1.5k Razorspawn"}),
    MANIAC1(3, "King Khione", 259, new Position(3046, 9569, 0), new Item[] {new Item(14437, 1), new Item (14438, 1),new Item(14439, 1), new Item (13997, 1), new Item (1464, 1), new Item (19935, 1),}
    , new String[] {"@red@HP:@gre@ 800k", "@red@KC REQ: 2.5k Dreamflow", ""}),
   MANIAC2(3, "Sable Beast", 1123, new Position(3176, 4242, 0), new Item[] {new Item(14440, 1), new Item (14441, 1),new Item(14442, 1), new Item (13997, 1), new Item (1464, 1), new Item (19935, 1),}
   , new String[] {"@red@HP:@gre@ 1m", "@red@KC REQ: 3000 King Khione", ""}),
   GLACOR(3, "Glacial Queen @or2@(Multi Boss)", 1382, new Position(2786, 3748, 4), new Item[] {new Item(14443, 1), new Item (14482, 1),new Item(14483, 1), new Item (19936, 1),}
   , new String[] {"@red@HP:@gre@ 5m", "@red@KC REQ: 3k Sable Beasts", ""}),
   ATRICITY(3, "Atricity Queen @or2@(Multi Boss)", 12101, new Position(2786, 3740, 4), new Item[] {new Item(18419, 1), new Item (18420, 1),new Item(18421, 1), new Item (19936, 1),}
   , new String[] {"@red@HP:@gre@ 5m", "@red@KC REQ: 3k Sable Beasts", ""}),
   DEMOGORGON(3, "Demogorgon", 12835, new Position(3299, 2788, 0), new Item[] {new Item(14872, 1), new Item (14695, 1),new Item(14694, 1),new Item(14873, 1),new Item(14874, 1), new Item (19936, 1),}
   , new String[] {"@red@HP:@gre@ 1.25m", "@red@KC REQ: 3k Sable Beasts", ""}),
   YOSHI(3, "Yoshi", 8548, new Position(2403, 3241, 0), new Item[] {new Item(4282, 1), new Item (4281, 1),new Item(4280, 1),new Item(4284, 1),new Item(4283, 1), new Item (19936, 1),}
   , new String[] {"@red@HP:@gre@ 1.5m", "@red@KC REQ: 4k Demogorgon", ""}),
    /*
     * City Teleports
     */


    RAJIN(4, "Rajin@or2@(Multi Boss)", 12836, new Position(2915, 4449, 0), new Item[] {new Item(14932, 1), new Item (14933, 1),new Item(14934, 1),new Item(14931, 1),new Item(16524, 1), new Item (10168, 1),}
    , new String[] {"@red@HP:@gre@ 7.5m", "@red@KC REQ: 4k Yoshi", ""}),
    SCARLETT_FALCON(4, "Scarlett Falcon@or2@(Multi Boss)", 12805, new Position(2784, 10077, 0), new Item[] {new Item(20917, 1), new Item (8705, 1),new Item(7027, 1),new Item(16519, 1),new Item(17750, 1), new Item (10168, 1),}
    , new String[] {"@red@HP:@gre@ 7.5m", "@red@KC REQ: 4k Yoshi", ""}),
    AVATAR(4, "Avatar the Airbender", 2264, new Position(3808, 9240, 0), new Item[] {new Item(20928, 1), new Item (20936, 1),new Item(20937, 1),new Item(20931, 1),new Item(20932, 1), new Item (20935, 1),}
    , new String[] {"@red@HP:@gre@ 3.5m", "@red@KC REQ: 5k Yoshi", ""}),
    LILI(4, "Lili", 11360, new Position(2528, 5861, 0), new Item[] {new Item(19949, 1), new Item (19950, 1),new Item(19951, 1),new Item(19952, 1),new Item(19953, 1),}
    , new String[] {"@red@HP:@gre@ 4.5m", "@red@KC REQ: 7.5k Avatar", ""}),
    OBITU(4, "Obito Uchiha ", 11383, new Position(3551, 5414, 0), new Item[] {new Item(8469, 1), new Item (8470, 1),new Item(8471, 1),new Item(8472, 1),new Item(8466, 1),new Item(8474, 1)}
    , new String[] {"@red@HP:@gre@ 5m", "@red@KC REQ: 10k Lili", ""}),
    URU(4, "Uru Isiha", 11305, new Position(3743, 9436, 0), new Item[] {new Item(8483, 1), new Item (8484, 1),new Item(8485, 1),new Item(8486, 1),new Item(8487, 1),new Item(8488, 1)}
    , new String[] {"@red@HP:@gre@ 7.5m", "@red@KC REQ: 12.5k Obitu Uchiha", ""}),
    KUMIHO (4, "Ahri", 5931, new Position(4256, 5927, 0), new Item[] {new Item(8569, 1), new Item (8570, 1),new Item(17855, 1),new Item(8572, 1),new Item(17856, 1),new Item(15521, 1)}
    , new String[] {"@red@HP:@gre@ 12.5m", "@red@KC REQ: 15k Uru Isiha", ""}),   
    MYSTERYMAN (4, "Mystery Man", 254, new Position(2143, 4129, 0), new Item[] {new Item(6308, 1), new Item (6310, 1),new Item(6311, 1),new Item(6312, 1),new Item(6313, 1),new Item(15521, 1)}
    , new String[] {"@red@HP:@gre@ 12.5m", "@red@KC REQ: 15k Ahri", ""}), 
    /*
     * Other Teleports
     */
    DBZ_ZONE(5, "DBZ Minigame", 100, new Position(2976, 2781, 0), new Item[] {new Item(9481, 1), new Item (9482, 1), new Item (9483, 1), new Item (16580, 1), new Item (20582, 1), new Item (20590, 1)}
    , new String[] {"This place drops ", "DBZ Items", "Goodluck"}),
    PORTAL_ZONE(5, "Portal Zone", 7286, new Position(3311, 4057, 0 ), new Item[] {new Item(1165, 1), new Item (995, 2200)}
    , new String[] {"4 Mass bosses", "@red@REQ: 20 crystals", ""}),
    MILOS_DREAM(5, "Milos Dream", 12113, new Position(3319, 4057, 0), new Item[] {new Item(20938, 1),new Item(20939, 1),new Item(20940, 1),new Item(20941, 1),new Item(20942, 1),new Item (20943, 1)}
    , new String[] {"30 Minutes session", "Earn nice rewards", "@red@REQ: 1 Dreampass"}),
    LEAUGE_OF_LEGENDS(5, "League of Legends", 11362, new Position(3437, 9570, 0), new Item[] {new Item(17745, 1),new Item(17745, 2),new Item(17745, 3),new Item(17745, 4),new Item(17745, 5)}
    , new String[] {"30 Minutes session", "Earn tokens to buy", "LoL Arcade sets"}),
    PEST_CONTROL(5, "Pest Control", 3789, new Position(2657, 2647, 0), new Item[] {new Item(11664, 1),new Item(8839, 1),new Item(8840, 1),new Item(11684, 1),new Item(11683, 1),new Item(8842, 1)}
    , new String[] {"Help Defend Portals", "Earn PC Points to buy", "Items from the", "PC Point shop"}),
    YUGIOH_RAIDS(5, "Yu-Gi-Oh Raids", 11362, new Position(3316, 4056, 0), new Item[] {new Item(11425, 1),new Item(3891, 1),new Item(5266, 1),new Item(3961, 1),new Item(16455, 1)}
    , new String[] {"30 Minutes session", "Earn tokens to buy", "LoL Arcade sets"}),
    MINIGAME(5, "Forgotten Minigame", 6357, new Position(2405, 3482, 0 ), new Item[] {new Item(13999, 1), new Item (3967, 1), new Item (3970, 1), new Item (3969, 1), new Item (3968, 1), new Item (3966, 1)}
    , new String[] {"Random RNG chance", "@red@REQ: 2.5K Dreamflow KC", ""}),
    KALVTOH(5, "Starter Boss", 68, new Position(2400, 2837,0), new Item[] {new Item(19936, 1), new Item (3911, 1), new Item (15418, 1), new Item (15653, 1), new Item (5154, 1)}
    , new String[] {"@red@HP:@gre@ 30000(30k)", "Have Fun!", ""}),
    DRAGON(5, "Medium Boss", 4972, new Position(3043, 3411, 0 ), new Item[] {new Item(6199, 1), new Item (18950, 1), new Item (3988, 1), new Item (14808, 1)}
    , new String[] {"Multiple People Needed", "Have Fun!", ""}),
    SEPHIROTH(5, "Hardened boss", 25, new Position(2596, 5727, 0), new Item[] {new Item(13998, 1),new Item (13999, 1)}
    , new String[] {"Sephiroth drops keys", "to use on Sephiroth", "Chest"}),
    AQUATIC_FROST_BEAST(5, "Expert boss", 12802, new Position(3173, 4956, 0), new Item[] {new Item(14921, 1), new Item (14922, 1), new Item (14923, 1),new Item (14915, 1)}
    , new String[] {"This Zagal drops", "Expert Tier Gear", "And Other Goodies"}),
    MASS_BOSS(5, "Mass Boss", 10010, new Position(2787, 4442, 0 ), new Item[] {new Item(15566, 1), new Item (13999, 1), new Item (5266, 1), new Item (16455, 1), new Item (19938, 1)}
    , new String[] {"Can you get the", "Diablo set?", "Maybe an @red@OC?"}),
    THANOS(5, "Vote Boss", 12808, new Position(3117, 3216, 0 ), new Item[] {new Item(14053, 1), new Item (14054, 1), new Item (14055, 1), new Item (14063, 1), new Item (14523, 1), new Item (14388, 1)}
    , new String[] {"Offer 50 Votes", "at ::offervotes", "to fight Thanos!"}),
    YKLAGOR(5, "World boss", 9911, new Position(3038, 5356, 0 ), new Item[] {new Item(19890, 1), new Item (6507, 1), new Item (19936, 1), new Item (9505, 1), new Item (9506, 1), new Item (9507, 1)}
    , new String[] {"Spawns every hour", "Goodluck!", ""}),
    MAY(5, "Pokemon boss", 2005, new Position(2848, 3038, 0 ), new Item[] {new Item(4742, 1), new Item (4789, 1), new Item (4790, 1),new Item (4791, 1), new Item (4792, 1)}
    , new String[] {"Drops Eevee pet", "And upgrade crystals", ""}),
    APOLLYON(5, "Apollyon boss", 12801, new Position(2822, 2823, 0 ), new Item[] {new Item(6491, 1), new Item (6492, 1), new Item (6493, 1),new Item (6494, 1), new Item (6495, 1)}
    , new String[] {"Drops Apollyon set", "And Apollyon staff", ""}),
    ;

    private final int categoryIndex;
    private final String name;
    private final int npcId;
    private final Position destination;
    private final Item[] drops;
    private final String[] description;

    TeleportData(int categoryIndex, String name, int npcId, Position destination, Item[] drops, String[] description) {
        this.categoryIndex = categoryIndex;
        this.name = name;
        this.npcId = npcId;
        this.destination = destination;
        this.drops = drops;
        this.description = description;
    }

    public static ArrayList<TeleportData> getPageList(TeleportPage page) {
        ArrayList<TeleportData> list = new ArrayList<>();
        for (TeleportData data : values()) {
            if (data.getCategoryIndex() == page.ordinal()) {
                list.add(data);
            }
        }
        return list;
    }

    public int getCategoryIndex() {
        return categoryIndex;
    }

    public String getName() {
        return name;
    }

    public int getNpcId() {
        return npcId;
    }

    public Position getDestination() {
        return destination;
    }

    public Item[] getDrops() {
        return drops;
    }

    public String[] getDescription() {
        return description;
    }
}