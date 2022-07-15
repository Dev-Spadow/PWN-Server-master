package com.arlania.world.content.aoesystem;

import java.util.Iterator;
import com.arlania.model.CombatIcon;
import com.arlania.model.Hit;
import com.arlania.model.Hitmask;
import com.arlania.model.Locations;
import com.arlania.util.RandomUtility;
import com.arlania.world.content.combat.Maxhits;
import com.arlania.world.entity.impl.Character;
import com.arlania.world.entity.impl.npc.NPC;
import com.arlania.world.entity.impl.player.Player;

public class AOEHandler {

	public static void handleAttack(Character attacker, Character victim, int minimumDamage, int maximumDamage,
									int radius, CombatIcon combatIcon) {

		// if no radius, loc isn't multi, stops.
		if (radius == 0 || !Locations.Location.inMulti(victim)) {
			// System.out.println("Radius 0");
			return;
		}

		// We passed the checks, so now we do multiple target stuff.
		Iterator<? extends Character> it = null;
		if (attacker.isPlayer() && victim.isPlayer()) {
			it = ((Player) attacker).getLocalPlayers().iterator();
		} else if (attacker.isPlayer() && victim.isNpc()) {
			it = ((Player) attacker).getLocalNpcs().iterator();

			for (Iterator<? extends Character> $it = it; $it.hasNext();) {
				Character next = $it.next();

				if (next == null) {
					continue;
				}

				if (next.isNpc()) {
					NPC n = (NPC) next;
					if (!n.getDefinition().isAttackable() || n.isSummoningNpc()) {
						continue;
					}
				} else {
					Player p = (Player) next;
					if (p.getLocation() != Locations.Location.WILDERNESS || !Locations.Location.inMulti(p)) {
						continue;
					}
				}

				if (next.getPosition().isWithinDistance(victim.getPosition(), radius) && !next.equals(attacker)
						&& !next.equals(victim) && next.getConstitution() > 0) {
					if (next.isNpc() && next.getConstitution() <= 0 && ((NPC)next).isDying()){
						continue;
					}

					int maxhit = maximumDamage;
					switch (((Player) attacker).getLastCombatType()) {
						case MELEE:
							maxhit = Maxhits.melee(attacker, victim) / 10;
							break;
						case RANGED:
							maxhit = Maxhits.ranged(attacker, victim) / 10;
							break;
						case MAGIC:
							maxhit = Maxhits.magic(attacker, victim) / 10;
							break;
					}

					int calc = RandomUtility.inclusiveRandom(minimumDamage, maxhit);
					Player player = (Player) attacker;
					next.dealDamage(new Hit(calc, Hitmask.RED, combatIcon));
					next.getCombatBuilder().addDamage(attacker, calc);
					next.getCombatBuilder().attack(attacker);
				}
			}
		}

	}
}

