package com.arlania.model.definitions;

//import com.arlania.NPC;

import com.arlania.model.Item;
import com.arlania.model.container.impl.Equipment;
import com.arlania.world.content.skill.impl.summoning.Familiar;
import com.arlania.world.content.well_of_goodwill.WellOfGoodwillHandler;
import com.arlania.world.entity.impl.player.Player;

public class DropUtils {

    private static final int[][] DRITEMS = {

            {22001, 75},
            {22002, 75},
            {22003, 75},

            {22005, 90},
            {22006, 90},
            {22007, 90},
            {22011, 50},
            {22012, 100},

            {13566, 90},

            /**
             * ItemID, DR bonus (as int)
             **/
            /*
             * Amulets
             */
            {6041, 2}, // 2% DR Amulet
            {9503, 3}, // Blessed Amulet 3%DR
            {19335, 3}, // Infartico amulet of fury
            {19886, 3}, // col neck
            {19106, 5}, // col neck i
            {15454, 7}, // col lvl 5
            {4780, 10}, // fate collector necklace
            {7028, 15}, // skotizos amulet
            {17291, 15}, // blood necklace
            {7027, 20}, // Scarletts necklace
            {774, 20}, // Perfect necklace
            {10500, 30}, // Millenial necklacce
            {9116, 30}, // PERFECT necklace(i)
            {5239, 40}, // bis owners necklace
            {8731, 100}, // Q's fuzed diamond
            {6758, 20}, // saturated diamond
            {6760, 35}, // Exquisite diamond
            {6762, 60}, // Pwnlite diamond
            /*
             * Auras
             */

            {4652, 20}, // Neo Aura
            {3309, 25}, // Red super saiyan Aura
            {3307, 30}, // Yellow super saiyan Aura
            {3277, 12}, // donators aura
            {19156, 25}, // sharingan aura
            {8562, 35}, // Divine aura
            {8566, 50}, // Pwnlite aura
            {19810, 10}, // discord aura

            /*
             * Helmets
             */

            {18950, 5}, // paper sack
            {17933, 10}, // marios head
            {17932, 10}, // luigis head
            {3949, 15}, // Slayer helmet cyan
            {3950, 15}, // Slayer helmet green
            {3952, 15}, // slayer helmet blue
            {934, 15}, // mask of the dead
            {18904, 20}, // Iron partyhat
            {15376, 30}, // warrior Slayer helmet
            {15377, 30}, // banished Slayer helmet
            {15497, 45}, // infused Slayer helmet
            {11425, 30}, // infused Slayer helmet

            /*
             * Boots
             */

            {5079, 5}, // 50% Drop rate boots
            {7762, 10}, // 50% Drop rate boots
            {19821, 20}, // 50% Drop rate boots
            {4803, 30}, // 60% Drop rate boots
            {5052, 35}, // Alpha Drop rate boots
            {8461, 40}, // Nike court visions
            /*
             * Gloves
             */

            {18751, 3}, // Demonic gloves
            {15026, 5}, // 50% Dr gloves
            {15032, 5}, // 50% Dr gloves
            {8493, 5}, // 50% Dr gloves
            {3918, 10}, // 10% Dr gloves
            {17842, 15}, // Bloodshed gloves
            {17843, 20}, // OP BLoody Dr gloves
            {17844, 25}, // OP BLoody Dr gloves

            /*
             * Rings
             */

            {2572, 3}, // Ring of Wealth
            {20054, 8}, // Ring of Devotion
            {19896, 10}, // kingship ring
            {5167, 10}, // Pwnlite Ring
            {4202, 10}, // ring of chaos
            {3317, 20}, // 20 dr ring
            {5159, 20}, // vpr god ring
            {773, 25}, // pring
            {10502, 30}, // Millenial necklacce
            {9117, 30}, // flawless ring
            {5238, 40}, // omni ring
            {14549, 50}, // 50% dr diamond

            /*
             * Capes
             */

            {3073, 7}, // special wings
            {922, 8}, // Sirenic wings
            {5092, 10}, // Ironman Cape
            {18748, 10}, // great olm cape
            {5209, 10}, // Infartico cape
            {3321, 10}, // Sirenic wings
            {14019, 15}, // max cape
            {14022, 15}, // comp cape
            {930, 15}, // Aquatic Cape
            {931, 15}, // Elemental cape
            {926, 15}, // Toxic cape
            {19958, 20}, // Dreamers cape
            {3267, 25}, // Opulant cape
            {3312, 25}, // slayer cape
            {15566, 30}, // Owners cape
            {6482, 40}, // OP Owners cape
            {14036, 35}, // Bloodslayer cape
            {16539, 60}, // Divine cape
            {8039, 60}, // Gary's cape
            {7026, 60}, // Vendetta cape
            {13028, 60}, // Ninja's wings
            /*
             * Armor sets
             */

            {4761, 1}, // Kings Armor
            {4762, 1}, // Kings Armor
            {4763, 1}, // Kings Armor
            {4764, 1}, // Kings Armor
            {4765, 1}, // Kings Armor
            {3905, 1}, // Kings Armor

            {3822, 2}, // lucid legs
            {3821, 2}, // lucid body
            {3820, 2}, // lucid helm
            {17912, 2}, // abyssal body
            {17913, 2}, // abyssal legs
            {17914, 2}, // abyssal helm
            {5082, 2}, // Darkblue Wizard
            {5083, 2}, // Darkblue Wizard
            {5084, 2}, // Darkblue Wizard
            {3985, 2}, // Darkblue Wizard
            {19619, 2}, // Pyro
            {19470, 2}, // Pyro
            {19471, 2}, // Pyro
            {19472, 2}, // Pyro
            {19473, 2}, // Pyro
            {19474, 2}, // Pyro

            {4641, 3}, // Purple wyrm
            {4642, 3}, // Purple wyrm
            {4643, 3}, // Purple wyrm
            {3983, 3}, // Purple wyrm
            {3064, 3}, // Purple wyrm

            {20240, 3}, // hulk justiticar helm
            {4781, 3}, // hulk justiticar plate
            {4782, 3}, // hulk justiticar legs

            {9500, 4}, // Pyro
            {9501, 4}, // Pyro
            {9502, 4}, // Pyro

            {19691, 4}, // Trinity
            {19692, 4}, // Trinity
            {19693, 4}, // Trinity
            {19694, 4}, // Trinity
            {19695, 4}, // Trinity
            {19696, 4}, // Trinity
            {19618, 4}, // Trinity

            {19159, 5}, // Cloud
            {19160, 5}, // Cloud
            {19161, 5}, // Cloud
            {19163, 5}, // Cloud
            {19164, 5}, // Cloud
            {19165, 5}, // Cloud
            {19166, 5}, // Cloud

            {9493, 6}, // Rouge
            {9494, 6}, // Rouge
            {9495, 6}, // Rouge
            {9104, 6}, // Rouge
            {9492, 6}, // Rouge

            {14494, 7}, // Exoden
            {14492, 7}, // Exoden
            {14490, 7}, // Exoden
            {2760, 7}, // Exoden

            {19728, 7}, // Supreme
            {19729, 7}, // Supreme
            {19730, 7}, // Supreme
            {19731, 7}, // Supreme
            {19732, 7}, // Supreme
            {6485, 7}, // Supreme
            {19727, 7}, // Supreme

            {13202, 8}, // Stormbreaker
            {13203, 8}, // Stormbreaker
            {13204, 8}, // Stormbreaker
            {13205, 8}, // Stormbreaker
            {13206, 8}, // Stormbreaker
            {13207, 8}, // Stormbreaker

            {11143, 8}, // Apollo
            {11144, 8}, // Apollo
            {11145, 8}, // Apollo
            {11146, 8}, // Apollo
            {11147, 8}, // Apollo

            {4794, 9}, // Noxious
            {4795, 9}, // Noxious
            {4796, 9}, // Noxious
            {4797, 9}, // Noxious
            {19127, 9}, // Noxious
            {19128, 9}, // Noxious
            {19129, 9}, // Noxious

            {8664, 9}, // Exotic
            {8665, 9}, // Exotic
            {8666, 9}, // Exotic
            {8667, 9}, // Exotic
            {8668, 9}, // Exotic
            {8669, 9}, // Exotic
            {8670, 9}, // Exotic

            {6927, 10}, // immortal
            {6928, 10}, // immortal
            {6929, 10}, // immortal
            {6930, 10}, // immortal
            {6931, 10}, // immortal

            {13991, 10}, // Azazel
            {13992, 10}, // Azazel
            {13995, 10}, // Azazel
            {13994, 10}, // Azazel
            {13993, 10}, // Azazel
            {14447, 10}, // Azazel
            {14448, 10}, // Azazel

            {9496, 10}, // Detrimental
            {9497, 10}, // Detrimental
            {9498, 10}, // Detrimental
            {9499, 10}, // Detrimental
            {19155, 10}, // Detrimental

            {19741, 11}, // Luminita
            {19742, 11}, // Luminita
            {19743, 11}, // Luminita
            {19744, 11}, // Luminita

            {5226, 12}, // Vanquisher boots
            {5227, 12}, // Vanquisher gloves
            {5228, 12}, // Vanquisher helmet
            {5229, 12}, // Vanquisher body
            {5230, 12}, // Vanquisher legs

            {16443, 13}, // Dreamflow mask
            {16444, 13}, // Dreamflow body
            {16445, 13}, // Dreamflow legs
            {16446, 13}, // Dreamflow boots
            {16448, 13}, // Dreamflow gloves
            {16449, 13}, // Dreamflow cape

            {14437, 14}, // Khione helmet
            {14438, 14}, // Khione body
            {14439, 14}, // Khione legs
            {14440, 14}, // Sable helmet
            {14441, 14}, // Sable body
            {14442, 14}, // Sable legs
            {14694, 14}, // Gorgon gloves
            {14695, 14}, // Gorgon boots
            {14872, 14}, // Gorgon helmet
            {14873, 14}, // Gorgon body
            {14874, 14}, // Gorgon legs
            {4282, 15}, // Yoshi gloves
            {4283, 15}, // Yoshi boots
            {4284, 15}, // Yoshi helmet
            {4280, 15}, // Yoshi body
            {4281, 15}, // Yoshi legs

            {14443, 15}, // Glacial helmet
            {14482, 15}, // Glacial body
            {14483, 15}, // Glacial legs

            {18419, 15}, // Apricity helmet
            {18420, 15}, // Apricity body
            {18421, 15}, // Apricity legs

            {18422, 20}, // Seraphic helmet
            {18423, 20}, // Seraphic body
            {18424, 20}, // Seraphic legs

            {20928, 15}, // avatar helmet
            {20931, 15}, // avatar body
            {20932, 15}, // avatar legs
            {20933, 15}, // avatar helmet
            {20936, 15}, // avatar body
            {20937, 15}, // avatar body
            {20935, 15}, // avatar legs

            {19949, 15}, // lili helmet
            {19950, 15}, // lili body
            {19951, 15}, // lili legs
            {19952, 15}, // lili helmet
            {19953, 15}, // lili body

            {8469, 15}, // Obito helmet
            {8470, 15}, // Obito body
            {8471, 15}, // Obito legs
            {8472, 15}, // Obito boots
            {8473, 15}, // Obito gloves
            {8474, 15}, // Obito Staff
            {8466, 15}, // Obito aura

            {8482, 15}, // Uru helmet
            {8483, 15}, // Uru body
            {8484, 15}, // Uru legs
            {8485, 15}, // Uru helmet
            {8486, 15}, // Uru body
            {8487, 15}, // Uru aura
            {8488, 15}, // Uru bow

            {8569, 15}, // Ahri helmet
            {8570, 20}, // Ahri body
            {17855, 20}, // Ahri legs
            {8572, 15}, // Ahri boots
            {17856, 15}, // Ahri gloves

            {6308, 20}, // Mysteryman helmet
            {6310, 20}, // Mysteryman body
            {6311, 20}, // Mysteryman legs
            {6312, 20}, // Mysteryman boots
            {6313, 20}, // Mysteryman gloves

            {16513, 10}, // Akasha gloves
            {16510, 10}, // Akasha boots
            {16514, 10}, // Akasha helmet
            {16515, 15}, // Akasha body
            {16516, 15}, // Akasha legs
            {16511, 15}, // Akasha cape
            {16518, 10}, // Akasha staff

            {3968, 20}, // Forgotten helmet
            {3966, 20}, // Forgotten body
            {3967, 20}, // Forgotten legs
            {3970, 20}, // Forgotten boots
            {3969, 20}, // Forgotten gloves

            {14030, 15}, // Green arsenic Helmet
            {14031, 15}, // Green arsenic Body
            {14032, 15}, // Green arsenic Legs
            {16563, 15}, // Green arsenic Boots
            {919, 15}, // Green arsenic Gloves

            {20938, 25}, // Inuyasha helmet
            {20939, 25}, // Inuyasha body
            {20940, 25}, // Inuyasha legs
            {20941, 25}, // Inuyasha boots
            {20942, 25}, // Inuyasha gloves

            {17746, 25}, // Ezreal Helmet
            {17748, 25}, // Ezreal Body
            {17749, 25}, // Ezreal Legs
            {17751, 25}, // Ezreal Boots
            {17756, 25}, // Ezreal Gloves

            {16480, 20}, // Blue arsenic Helmet
            {16484, 20}, // Blue arsenic Body
            {16485, 20}, // Blue arsenic Legs
            {16479, 20}, // Blue arsenic Boots
            {16486, 20}, // Blue arsenic Gloves

            {16489, 30}, // Blood arsenic Helmet
            {16490, 30}, // Blood arsenic Body
            {16491, 30}, // Blood arsenic Legs
            {16494, 30}, // Blood arsenic Boots
            {16493, 30}, // Blood arsenic Gloves

            {7772,30}, // xdiorrs
            {10306,30}, // exdiorr 2
            {17309,30}, // exdiorr 3


            {19881, 30}, // Pwnlite creator Helmet
           {19882, 30}, // Pwnlite creator Body
            {19883, 30}, // Pwnlite creator Legs
            {19884, 30}, // Pwnlite creator Boots
            {19885, 30}, // Pwnlite creator Gloves
            {19905, 30}, // Pwnlite creator Boots
            {19906, 20}, // Pwnlite creator Gloves

            {10906, 40}, // q Helmet
            {7607, 40}, // q Body
            {7608, 40}, // q Legs
            {7609, 50}, // q Boots
            {7610, 40}, // q Gloves

            {8034, 40}, // Gary's Helmet
            {8035, 40}, // Gary's Body
            {8036, 40}, // Gary's Legs
            {8037, 50}, // Gary's Boots
            {8038, 40}, // Gary's Gloves

            {17850, 40}, // vendetta Helmet
            {17847, 40}, // vendetta Body
            {17848, 40}, // vendetta Legs
            {17851, 50}, // vendetta Boots
            {17852, 40}, // vendetta Gloves
            {17854, 40}, // vendetta Gloves
            {7016, 40}, // Pwnlite's offhand
            {9530, 40}, // ninja's Helmet
            {9531, 40}, // ninja's Body
            {9532, 40}, // ninja's Legs
            {9533, 50}, // ninja's Boots
            {9534, 40}, // ninja's Gloves
            {13032, 40}, // ninja's Gloves


            {10296, 40}, // ninja's Helmet
            {19173, 40}, // ninja's Body
            {19175, 40}, // ninja's Legs
            {20453, 50}, // ninja's Boots
            {20464, 40}, // ninja's Gloves

            {12178, 40}, // byes Helmet
            {12179, 40}, // byes Body
            {12180, 40}, // byes Legs
            {12181, 50}, // byes Boots
            {12182, 40}, // byes Gloves
            {12183, 40}, // byes Gloves

            {19954, 35}, // Naraku Helmet
            {19955, 35}, // Naraku Body
            {19956, 35}, // Naraku Legs
            {19959, 35}, // Naraku Boots
            {19960, 35}, // Naraku Gloves
            {19998, 35}, // Naraku Wings

            /*
             * Weapons
             */

            {19468, 3}, // staff of envy
            {933, 5}, // staff of envy
            {932, 5}, // bluefire vip sword
            {12426, 5}, // Icy glaive
            {13094, 5}, // hween scythe
            {5116, 20}, // Staff of lust
            {3911, 5}, // custom trident
            {3311, 10}, // Skotizos mace
            {5231, 10}, // Vanquisher whip
            {6930, 10}, // seph sword
            {14029, 10}, // green arsen
            {14028, 10}, // green arsen
            {14027, 10}, // green arsen
            {20427, 10}, // Bfg mainhand
            {19154, 10}, // lumin sword
            {10905, 10}, // detrimental minigun
            {20927, 10}, // Summer sword
            {14922, 10}, // Arctic frost blade
            {14921, 10}, // Arctic frost staff
            {14923, 10}, // Arctic frost bow
            {5171, 10}, // stormbreaker bow
            {3077, 15}, // skotizos bow
            {3971, 15}, // archeon bow
            {16475, 15}, // blue arsen
            {16476, 15}, // blue arsen
            {16478, 15}, // blue arsen

            {8654, 15}, // imbued sword
            {8655, 15}, // imbued bow
            {8656, 15}, // imbued staff

            {14581, 20}, // saturated sword
            {18911, 20}, // saturated bow
            {18912, 20}, // saturated staff

            {16450, 15}, // dreamflow sword
            {13265, 15}, // seph sword
            {5115, 15}, // Yoshi bow
            {3282, 15}, // God staff
            {14535, 15}, // Thanos gauntlet
            {4741, 20}, // Emerald staff
            {16463, 20}, // blood arsen
            {16436, 20}, // blood arsen
            {16435, 20}, // blood arsen
            {6934, 20}, // Apollyon staff
            {13289, 20}, // op seph sword
            {5214, 20}, // Omni blade
            {20943, 22}, // Inuyasha sword
            {20934, 20}, // tham Sceptre
            {8705, 20}, // tham Sceptre
            {20917, 20}, // Saguine scythe of virtur
            {3920, 25}, // Owners staff
            {5263, 25}, // Owners sword
            {13024, 25}, // byes sword
            {6499, 25}, // Owners bow
            {14287, 20}, // Torrent's custom weapon
            {8031, 25}, // Q's Owners staff
            {5265, 25}, // Q's Owners bow
            {5267, 25}, // Q's Owners sword
            {17853, 40}, // Vendetta's custom weapon
            {8040, 40}, // Pwnlite40's custom weapon
            {8041, 40}, // Pwnlite40's custom weapon
            {13388, 40}, // Ninja's's custom weapon
            {13389, 40}, // Ninja's's custom weapon
            {8042, 40}, // Pwnlite40's custom weapon
            {12185, 40}, // Torrent's custom weapon
            {11952, 40}, // Torrent's custom weapon

            {15008, 20}, // Halloween Hammer
            {15009, 20}, // Pumpkin staff
            {15010, 20}, // spooky bow
            /*
             * IRONMAN ARMOR
             */
            {18636, 10}, // head
            {18637, 10}, // body
            {18638, 10}, // legs
            {18470, 10}, // gloves
            {18471, 10}, // boots

            /*
             * Shields
             */
            {5184, 5}, // Oceans Teddybear
            {2547, 5}, // Oceans Teddybear
            {2546, 5}, // Rasta Teddybear

            {5210, 10}, // Toxic shield
            {5211, 10}, // Elemental shield
            {15045, 10}, // Aquatic shield

            {2545, 5}, // Panda Teddybear
            {7617, 10}, // Donor only Teddybear
            {15660, 15}, // Chuckydoll
            {4279, 20}, // OP god shield
            {13026, 20}, // byes shield
            {20431, 10}, // bfg offhand
            {7732, 100}, // xdiorr shield
            {10306, 100}, // xdiorr helm
            {17309, 100}, // xdiror boots
            {19167, 100}, // xdiorr platebody

            /*
             * Darksoul set
             */
            {3088, 15}, {3085, 10}, {3086, 10}, {3087, 10},

            /*
             * Youtuber items
             */
            {5107, 20}, {5108, 20}, {5109, 20}, {5110, 20}, {5111, 20},

            /*
             * Imbued set
             */

            {4060, 20}, {4061, 20}, {4062, 20}, {4063, 20}, {4064, 20}, {4065, 20}, {4066, 20},
            {4067, 20}, {4085, 20}, {4555, 20},

    };

    // list of ids of items that have the collectors effect
    private static final int[] COLLITEMS = {19886, // col neck
            19106, // col neck i
            15566, // owners cape
            8732, 15454, // collecter lvl 5
            4780, // fates collector necklace
            9116, // perfect amulet(i)
            9117, // perfect ring(i)
            3277, // donators aura
            5239, // owners ring
            5238, // owners necklace
            773, // perfect ring
            774, // perfect necklace
            13095, // H'ween Aura
            4652, // neo aura
            8562, // Divine aura
            8566, // Pwnlite Aura
            19156, // shanringan aura
            19810, // discord aura
            7027, // scarlett necklace
            22000,
            22001,
            22002,
            22005,
            22006,
            22007
    };

    // list of petspawnid's and how much dr they give
    private static final int[][] DRPETS = {

            {640, 2}, // joker
            {1008, 3}, // bulbasour
            {639, 3}, // charmander
            {1739, 4}, // lucario
            {229, 5}, // donald duck
            {641, 5}, // Charizard
            {3032, 5}, // Jad
            {1785, 5}, // Nex
            {9995, 5}, // Infartico
            {744, 7}, // Godzilla
            {806, 7}, // Goku
            {808, 7}, // Vegeta
            {3034, 9}, // Corp
            {230, 10}, // baby yoda
            {8512, 10}, // Vorago
            {3137, 10}, // Eevee
            {2322, 10}, // Killer chucky pet
            {1060, 20}, // brol
            {8497, 10}, // Hulk
            {3054, 10}, // Nex
            {6596, 10}, // Bluefire dragon
            {2759, 10}, // Vasa Nistirio Pet
            {203, 13}, // kid buu Pet
            {3138, 15}, // Jolteon
            {642, 15}, // Flareon
            {644, 15}, // Vapereon
            {722, 15}, // Sylveon
            {6604, 15}, // Forest dragon
            {13463, 15}, // Rainbow Eevee
            {6333, 15}, // kbd
            {815, 15}, // zorbak
            {4971, 15}, // Virulent dragon
            {1946, 15}, // skotizo jr
            {2756, 15}, // stone toad pet
            {816, 17}, // stone toad pet
            {809, 20}, // Fuzed Goku
            {11811, 20}, // Akuna guardian
            {5960, 25}, // Rainbow Eevee
            {1252, 25}, // Hulk
            {12213, 30}, // Rainbow Eevee
            {12240, 50}, // Byes (toxics) Custom pet
            {160, 50}, // Pwnlite's (gary's) Custom pet
    };

    private static final int[][] DR_ITEMS_IN_INVENTORY = {{5197, 50}, // 50% dr charm
            {6770, 25}, // 25% dr charm
            {18392, 2}, // 2% dr charm
            {18401, 5}, // 5% dr charm
            {20582, 15}, // dbz balls
            {20590, 25}, // dbz balls
            {8045, 100}, // Q's dbz balls
            {8033, 100}, // Q's dbz balls
            {15223, 10}, // beta ticket
            {8732, 100}, // q's lucky charm
            {13030, 100}, // ninja's lucky charm
            {13390, 100}, // Pwnlite's lucky charm
            {7013, 100}, // custom dbz ball
            {7015, 100}, // custom dbz ball
            {7925, 100}, // Femboys lucky charm

    };

    public static int drBonus(Player player, boolean fake) { // this should have a cap aswell
        int totalBonus = 0;
        Equipment playerEquip = player.getEquipment();
        Familiar playerFamiliar = player.getSummoning().getFamiliar();

        for (int[] item : DRITEMS)
            if (playerEquip.contains(item[0]))
                totalBonus += item[1];

        for (int[] scroll : DR_ITEMS_IN_INVENTORY) {
            if (player.getInventory().contains(scroll[0])) {
                totalBonus += scroll[1];
            }
        }

        totalBonus += player.salvageDropRate;

        if (player.getEquipment().contains(13201)) {
            totalBonus += 5;
        }

        totalBonus += player.getKillPrestige() * 5;

        totalBonus += player.getAchievementDRBoost();

        if (player.hasPlayerBoostTime)
            totalBonus += 100;

        switch (player.getRights()) {
            case DONATOR:
                totalBonus += 1.0;
                break;

            case SUPER_DONATOR:
                totalBonus += 2.0;
                break;

            case ULTRA_DONATOR:
                totalBonus += 5.0;
                break;

            case MYSTIC_DONATOR:
                totalBonus += 8.0;
                break;

            case OBSIDIAN_DONATOR:
                totalBonus += 11;
                break;

            case LEGENDARY_DONATOR:
            case CoolRank:
                totalBonus += 14;
                break;

            case CELESTIAL_DONATOR:
                totalBonus += 18;
                break;

            case OWNER:
            case DEVELOPER:
                totalBonus += 40;
                break;

            case EXECUTIVE_DONATOR:
                totalBonus += 21;
                break;

            case SUPREME_DONATOR:
                totalBonus += 25;
                break;

            case DIVINE_DONATOR:
                totalBonus += 30;
                break;

            case SUPPORT:
            case MODERATOR:
            case ADMINISTRATOR:
                totalBonus += 30;
                break;

            case CO_OWNER:
                totalBonus += 25;
                break;
            default:
                break;
        }

        if (playerFamiliar != null) {
            for (int[] pet : DRPETS)
                if (playerFamiliar.getSummonNpc().getId() == pet[0])
                    totalBonus += pet[1];
        }

        if (player.isDoubleRateActive())
            totalBonus += 100;
        if (player.isChocCreamRateActive())
            totalBonus += 30;
        if (player.isSmokeTheBongRateActive())
            totalBonus += 30;
        if (player.isIceCreamRateActive())
            totalBonus += 20;
        if (player.isEatPumpkinRateActive())
            totalBonus += 20;

        if (WellOfGoodwillHandler.isDoubleDropRateActive())
            totalBonus += 50;

        if (!fake) {
            if (totalBonus >= 650) {
                totalBonus = 650;
            }
        }

        if (player.getInventory().contains(22010)) {
            totalBonus = (int) (totalBonus * 1.75);
        } else if (player.getInventory().contains(22009)) {
            totalBonus = (int) (totalBonus * 1.5);
        }

        return totalBonus;
    }

    public static boolean hasCollItemEquipped(Player player) {
        for (int itemId : COLLITEMS)
            if (player.getEquipment().contains(itemId))
                return true;

        return false;
    }
}
