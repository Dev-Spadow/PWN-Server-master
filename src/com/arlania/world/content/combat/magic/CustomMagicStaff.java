package com.arlania.world.content.combat.magic;


import com.arlania.model.Graphic;
import com.arlania.model.container.impl.Equipment;
import com.arlania.world.content.combat.CombatContainer;
import com.arlania.world.content.combat.CombatType;
import com.arlania.world.entity.impl.Character;
import com.arlania.world.entity.impl.player.Player;

public class CustomMagicStaff {

    public static enum CustomStaff {    
    	CRUMBLE_UNDEAD(new int[] {14006}, CombatSpells.CRUMBLE_UNDEAD.getSpell()),
    	ADAM_STAFF(new int[] {19468}, CombatSpells.ICE_RUSH.getSpell()),
    	PUMPKIN_STAFF(new int[] {15009}, CombatSpells.PUMPKIN_STRIKE.getSpell()),
    	SHADOW_BLITZ(new int[] {19720}, CombatSpells.SHADOW_BLITZ.getSpell()),
    	PURPLESTAFF(new int[] {15653}, CombatSpells.PURPLESTAFF.getSpell()),
    	DEATHBLUE(new int[] {15656}, CombatSpells.DEATHBLUE.getSpell()),
    	SHADOW_BARRAGE(new int[] {13867}, CombatSpells.SHADOW_BARRAGE.getSpell()),
    	SEISMIC(new int[] {3282,8656 }, CombatSpells.SEISMIC.getSpell()),
    	SEISMIC1(new int[] {14581}, CombatSpells.SEISMIC1.getSpell()),
    	AZAZEL_STAFF(new int[] {13995 }, CombatSpells.AZAZEL.getSpell()),
    	AKASHA_STAFF(new int[] {16518 }, CombatSpells.AKASHA.getSpell()),
    	ARCTIC_STAFF(new int[] {14922 }, CombatSpells.ARCTIC.getSpell()),
    	SUPREME_STAFF(new int[] {3282,19727,5108}, CombatSpells.BLOOD_BARRAGE.getSpell()),
    	EMERALDSSTAFF(new int[] {4741,14029}, CombatSpells.EMERALDSSTAFF.getSpell()),
    	PLAYER_ONE(new int[] {16495}, CombatSpells.PLAYER_ONE.getSpell()),
    	SCYTHESTAFF(new int[] {14550}, CombatSpells.SCYTHESTAFF.getSpell()),
    	CUNTSTAFF(new int[] {14910}, CombatSpells.CUNTSTAFF.getSpell()),
    	LLUS(new int[] {17379}, CombatSpells.LLUS.getSpell()),
    	LORIENSTAFF(new int[] {8817}, CombatSpells.LORIENSTAFF.getSpell()),
    	SUZUMIANSTAFF(new int[] {7035,}, CombatSpells.SUZUMIANSTAFF.getSpell()),
    	SAUSAGESTAFF(new int[] {5216}, CombatSpells.SAUSAGESTAFF.getSpell()),
    	DAFACKSTAFF(new int[] {5217}, CombatSpells.DAFACKSTAFF.getSpell()),
    	MARTYSTAFF(new int[] {17845}, CombatSpells.MARTY1.getSpell()),
    	TAIGA(new int[] {6499,16459,16468,8041,13389}, CombatSpells.TAIGA.getSpell()),
    	TAIGA1(new int[] {5265}, CombatSpells.TAIGA1.getSpell()),
    	EBOW(new int[] {8818}, CombatSpells.TAIGA.getSpell()),
    	AVEDA(new int[] {6498}, CombatSpells.AVEDA.getSpell()),
    	APOLLYONSTAFF(new int[] {6934}, CombatSpells.APOLLYONSTAFF.getSpell()),
    	RED_VIRUS(new int[] {3279,3920,16463}, CombatSpells.RED_VIRUS.getSpell()),
    	BLUE_VIRUS(new int[] {8042,13388}, CombatSpells.BLUE_VIRUS.getSpell()),
    	RED_VIRUS1(new int[] {8031}, CombatSpells.RED_VIRUS1.getSpell()),
    	BLUE_ARSENIC(new int[] {16478}, CombatSpells.BLUE_ARSENIC.getSpell()),
    	SCEPTRE(new int[] {8705}, CombatSpells.RED_SCEPTRE.getSpell()),
    	EXOTICSTAFF(new int[] {8664}, CombatSpells.ICE_BARRAGE.getSpell()),
     	DUCKSTAFF(new int[] {6483}, CombatSpells.SARADOMIN_STRIKE.getSpell()),
     	STAFF_OF_GRACE(new int[] {3291}, CombatSpells.STAFFOFGRACE.getSpell()),
     	FROZEN_STAFF(new int[] {5069}, CombatSpells.FROZEN_STAFF.getSpell()),
     	FROZEN_STAFFII(new int[] {5273}, CombatSpells.FROZEN_STAFFII.getSpell()),
     	FROZEN_STAFFIII(new int[] {5274}, CombatSpells.FROZEN_STAFFIII.getSpell()),
     	STAFF_OF_LUST(new int[] {5116}, CombatSpells.STAFFOFLUST.getSpell()),
     	OBITO(new int[] {8474}, CombatSpells.OBITO.getSpell()),
    	OP(new int[] { 4741 }, CombatSpells.EARTH_BLAST.getSpell());
        private int[] itemIds;
        private CombatSpell spell;

        CustomStaff(int[] itemIds, CombatSpell spell) {
            this.itemIds = itemIds;
            this.spell = spell;
        }

        public int[] getItems() {
            return this.itemIds;
        }

        public CombatSpell getSpell() {
            return this.spell;
        }

        public static CombatSpell getSpellForWeapon(int weaponId) {
            for (CustomStaff staff : CustomStaff.values()) {
                for (int itemId : staff.getItems())
                    if (weaponId == itemId)
                        return staff.getSpell();
            }
            return null;
        }
    }

    public static boolean checkCustomStaff(Character c) {
        int weapon;
        if (!c.isPlayer())
            return false;
        Player player = (Player)c;
        weapon = player.getEquipment().getItems()[Equipment.WEAPON_SLOT].getId();
        return CustomStaff.getSpellForWeapon(weapon) != null;
    }

    public static void handleCustomStaff(Character c) {
        Player player = (Player) c;
        int weapon = player.getEquipment().getItems()[Equipment.WEAPON_SLOT].getId();
        CombatSpell spell = CustomStaff.getSpellForWeapon(weapon);
        player.setCastSpell(spell);
        player.setAutocast(true);
        player.setAutocastSpell(spell);
        player.setCurrentlyCasting(spell);
        player.setLastCombatType(CombatType.MAGIC);

    }
    public static CombatContainer getCombatContainer(Character player, Character target) {
        ((Player)player).setLastCombatType(CombatType.MAGIC);
        return new CombatContainer(player, target, 1, 1, CombatType.MAGIC, true) {
            @Override
            public void onHit(int damage, boolean accurate) {
                target.performGraphic(new Graphic(1730));
            }
        };
    }

}
