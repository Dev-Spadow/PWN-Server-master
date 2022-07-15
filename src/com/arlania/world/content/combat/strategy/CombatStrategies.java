package com.arlania.world.content.combat.strategy;

import java.util.HashMap;
import java.util.Map;

import com.arlania.world.content.bosses.Customwrencher;
import com.arlania.world.content.combat.strategy.impl.*;
import com.arlania.world.content.combat.strategy.impl.godwars.*;
import com.arlania.world.content.combat.strategy.wilderness.*;
import com.arlania.world.content.raids.RaidNpc;
import com.arlania.world.entity.impl.npc.NPC;
import com.arlania.world.content.combat.strategy.impl.dks.DagannothSupreme;
import com.arlania.world.content.combat.strategy.impl.Geerin;
import com.arlania.world.content.combat.strategy.impl.godwars.Tsutsuroth;
import com.arlania.world.content.combat.strategy.impl.raid.BalanceElemental;



public class CombatStrategies {

	private static final DefaultMeleeCombatStrategy defaultMeleeCombatStrategy = new DefaultMeleeCombatStrategy();
	private static final DefaultMagicCombatStrategy defaultMagicCombatStrategy = new DefaultMagicCombatStrategy();
	private static final DefaultRangedCombatStrategy defaultRangedCombatStrategy = new DefaultRangedCombatStrategy();
	private static final Map<Integer, CombatStrategy> STRATEGIES = new HashMap<Integer, CombatStrategy>();
	
	public static void init() {
		DefaultMagicCombatStrategy defaultMagicStrategy = new DefaultMagicCombatStrategy();
		STRATEGIES.put(13, defaultMagicStrategy);
		STRATEGIES.put(9203, defaultMagicStrategy);
		STRATEGIES.put(9864, defaultMagicStrategy);
		STRATEGIES.put(172, defaultMagicStrategy);
		STRATEGIES.put(9647, defaultMagicStrategy);
		STRATEGIES.put(2025, defaultMagicStrategy);
		STRATEGIES.put(3495, defaultMagicStrategy);
		STRATEGIES.put(3496, defaultMagicStrategy);
		STRATEGIES.put(3491, defaultMagicStrategy);
		STRATEGIES.put(2882, defaultMagicStrategy);
		STRATEGIES.put(13451, defaultMagicStrategy);
		STRATEGIES.put(13452, defaultMagicStrategy);
		STRATEGIES.put(13453, defaultMagicStrategy);
		STRATEGIES.put(13454, defaultMagicStrategy);
		STRATEGIES.put(1643, defaultMagicStrategy);
		STRATEGIES.put(6254, defaultMagicStrategy);
		STRATEGIES.put(6257, defaultMagicStrategy);
		STRATEGIES.put(6278, defaultMagicStrategy);
		STRATEGIES.put(6221, defaultMagicStrategy);
		STRATEGIES.put(12801, defaultMagicStrategy);
		STRATEGIES.putIfAbsent(1, defaultMagicStrategy);
	;
		
		DefaultRangedCombatStrategy defaultRangedStrategy = new DefaultRangedCombatStrategy();
		STRATEGIES.put(688, defaultRangedStrategy);
		STRATEGIES.put(12810, defaultRangedStrategy);
		STRATEGIES.put(2028, defaultRangedStrategy);
		STRATEGIES.put(6220, defaultRangedStrategy);
		STRATEGIES.put(6256, defaultRangedStrategy);
		STRATEGIES.put(6276, defaultRangedStrategy);
		STRATEGIES.put(6252, defaultRangedStrategy);
		STRATEGIES.put(27, defaultRangedStrategy);
		STRATEGIES.put(1684, defaultRangedStrategy);
		STRATEGIES.put(11384, new Jad());
		STRATEGIES.put(2745, new Jad());
		STRATEGIES.put(8528, new Nomad());
		STRATEGIES.put(8349, new TormentedDemon());
		STRATEGIES.put(9994, new TormentedDemon());
		STRATEGIES.put(3200, new ChaosElemental());
		STRATEGIES.put(4540, new BandosAvatar());
		STRATEGIES.put(8133, new CorporealBeast());
		STRATEGIES.put(2896, new Spinolyp());
		STRATEGIES.put(2881, new DagannothSupreme());
		STRATEGIES.put(6260, new Graardor());
		STRATEGIES.put(13447, new Nex());
		//STRATEGIES.put(3154, new Nex());
		STRATEGIES.put(6263, new Steelwill());
		STRATEGIES.put(6265, new Grimspike());
		STRATEGIES.put(6222, new KreeArra());
		STRATEGIES.put(6223, new WingmanSkree());
		STRATEGIES.put(6225, new Geerin());
		STRATEGIES.put(11202, new BigSmokeScript());
		STRATEGIES.put(6203, new Tsutsuroth());
		STRATEGIES.put(6208, new Kreeyath());
		STRATEGIES.put(6206, new Gritch());
		STRATEGIES.put(6247, new Zilyana());
		STRATEGIES.put(6250, new Growler());
		STRATEGIES.put(1382, new Glacor());
		STRATEGIES.put(9939, new PlaneFreezer());
		 STRATEGIES.put(2042, new RemadeZulrah());
			STRATEGIES.put(2043, new RemadeZulrah());
			STRATEGIES.put(2044, new RemadeZulrah());
			STRATEGIES.put(12836, new RajinStrategy());
			STRATEGIES.put(9287, new GarenScript());
			STRATEGIES.put(11362, new VeigarScript());
			STRATEGIES.put(11361, new SuperiorQueen());
			STRATEGIES.put(9286, new SuperiorQueen());
		Dragon dragonStrategy = new Dragon();
		STRATEGIES.put(50, dragonStrategy);
		STRATEGIES.put(941, dragonStrategy);
		STRATEGIES.put(55, dragonStrategy);
		STRATEGIES.put(53, dragonStrategy);
		STRATEGIES.put(54, dragonStrategy);
		STRATEGIES.put(1590, dragonStrategy);
		STRATEGIES.put(1591, dragonStrategy);
		STRATEGIES.put(1592, dragonStrategy);
		STRATEGIES.put(5362, dragonStrategy);
		STRATEGIES.put(5363, dragonStrategy);
		
		Aviansie aviansieStrategy = new Aviansie();
		STRATEGIES.put(6246, aviansieStrategy);
		STRATEGIES.put(6230, aviansieStrategy);
		STRATEGIES.put(6231, aviansieStrategy);
		
		KalphiteQueen kalphiteQueenStrategy = new KalphiteQueen();
		STRATEGIES.put(1158, kalphiteQueenStrategy);
		STRATEGIES.put(1160, kalphiteQueenStrategy);
		
		Revenant revenantStrategy = new Revenant();
		STRATEGIES.put(6715, revenantStrategy);
		STRATEGIES.put(6716, revenantStrategy);
		STRATEGIES.put(6701, revenantStrategy);
		STRATEGIES.put(6725, revenantStrategy);
		STRATEGIES.put(6691, revenantStrategy);
		
		STRATEGIES.put(2000, new Venenatis());
		STRATEGIES.put(2006, new Vetion());
		STRATEGIES.put(2010, new Callisto());
		STRATEGIES.put(6766, new LizardMan());
		//STRATEGIES.put(8281, new BalanceElemental());
		STRATEGIES.put(499, new Thermonuclear());
		STRATEGIES.put(7286, new Skotizo());
		STRATEGIES.put(5886, new Sire());
		STRATEGIES.put(9231, new Sire());
		/*
		 * new npc's. added by ME
		 */
		STRATEGIES.put(8597, new AvatarOfCreation());
		STRATEGIES.put(10126, new UnholyCursebearer());
		STRATEGIES.put(12823, new UnholyCursebearer());
		STRATEGIES.put(10051, new Icedemon()); //now it does , b
		STRATEGIES.put(9176, new SkeletalHorror()); // this one nee
		STRATEGIES.put(6208, new BalfrugKreeyath());
		STRATEGIES.put(11751, new Necrolord());
		STRATEGIES.put(9766, new Sagittare());
		STRATEGIES.put(9855, new Lexicus());
		STRATEGIES.put(3, new Tezkid());

		/**
		 * Converted Bosses.
		 */
		
		STRATEGIES.put(4413, new DireWolf());
        STRATEGIES.put(6305, new Dragonix());
        STRATEGIES.put(10141, new BallakPummeler());
        STRATEGIES.put(10039, new ToKashBloodchiller());
        STRATEGIES.put(6307, new ZamorakIktomi());
        STRATEGIES.put(839, new MiniDire());
        STRATEGIES.put(509, new Nazastarool());
        STRATEGIES.put(433, new Cyrisus());
        STRATEGIES.put(6307, new ZamorakIktomi());
        
		/**
		 * End of converted bosses.
		 */
        
        
		STRATEGIES.put(7553, new TheGeneral());
		STRATEGIES.put(12841, new Warmonger());
		STRATEGIES.put(6313, new ArmadylAbyzou());
		STRATEGIES.put(6314, new ArmadylAbyzou());
		STRATEGIES.put(6309, new ZamorakLefosh());
		STRATEGIES.put(7134, new BorkStrategy());
		STRATEGIES.put(6303, new Abbadon());
		STRATEGIES.put(9903, new Harambe());
		STRATEGIES.put(9913, new DailyNpcCombat());
		STRATEGIES.put(8507, new CustomVoragoCombat());
		STRATEGIES.put(9273, new WizardOfTridentCombat());
		STRATEGIES.put(9277, new RainbowTextureNPC());
		STRATEGIES.put(421, new RainbowTextureNPC());
		STRATEGIES.put(2509, new RainbowTextureNPC());
		STRATEGIES.put(2005, new Glacor());
		STRATEGIES.put(9280, new KCMinions());
		STRATEGIES.put(9647, new InfernalWizardCombat());
		STRATEGIES.put(8675, new OlmMinionsCombat());
		STRATEGIES.put(6593, new SuicsBoss());
        STRATEGIES.put(68, new KalvothBoss());
        STRATEGIES.put(10001, new FirstStrategy());
        STRATEGIES.put(10002, new SecondStrategy()); //TODO protect from 2 attack types
        STRATEGIES.put(10003, new ThirdStrategy());
        STRATEGIES.put(10000, new FourthStrategy());
		
		
		
	}
	
	public static CombatStrategy getStrategy(NPC npc) {
		if(npc instanceof RaidNpc) {
			return defaultMagicCombatStrategy;
		}
		if(STRATEGIES.get(npc.getId()) != null) {
			return STRATEGIES.get(npc.getId());
		}
		return defaultMeleeCombatStrategy;
	}
	
	public static CombatStrategy getDefaultMeleeStrategy() {
		return defaultMeleeCombatStrategy;
	}

	public static CombatStrategy getDefaultMagicStrategy() {
		return defaultMagicCombatStrategy;
	}


	public static CombatStrategy getDefaultRangedStrategy() {
		return defaultRangedCombatStrategy;
	}
}
