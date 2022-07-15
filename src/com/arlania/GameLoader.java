package com.arlania;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.arlania.model.input.impl.EnterReferral;
import com.arlania.world.content.battlepass.BattlePass;
import com.arlania.world.content.bis.BestInSlotInterface;
import com.arlania.world.content.bosses.Armadylabzyou;
import com.arlania.world.content.bosses.Bork;
import com.arlania.world.content.bosses.Apollyon;
import com.arlania.world.content.bosses.Customwrencher;
import com.arlania.world.content.bosses.DailyNpc;
import com.arlania.world.content.bosses.ElementalJad;
import com.arlania.world.content.bosses.EmeraldWorldBoss;
import com.arlania.world.content.bosses.FrostBeast;
import com.arlania.world.content.bosses.General;
import com.arlania.world.content.bosses.Newabbadon;
import com.arlania.world.content.bosses.TheVortex;
import com.arlania.world.content.bosses.VeigarBoss;
import com.arlania.world.content.bosses.TheZamorakLefosh;
import com.arlania.world.content.boxspinner.MysteryBoxHandler;
import com.arlania.world.content.dailytasks.DailyTasks;
import com.arlania.world.content.referral.ReferralHandler;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.util.HashedWheelTimer;

import com.arlania.engine.GameEngine;
import com.arlania.engine.task.TaskManager;
import com.arlania.engine.task.impl.ServerTimeUpdateTask;
import com.arlania.model.container.impl.Shop.ShopManager;
import com.arlania.model.definitions.ItemDefinition;
import com.arlania.model.definitions.NPCDrops;
import com.arlania.model.definitions.NpcDefinition;
import com.arlania.model.definitions.WeaponInterfaces;
import com.arlania.net.PipelineFactory;
import com.arlania.net.security.ConnectionHandler;
import com.arlania.util.DiscordRewards;
import com.arlania.world.clip.region.RegionClipping;
import com.arlania.world.content.aoesystem.AOESystem;
import com.arlania.world.content.CustomObjects;
import com.arlania.world.content.DropSimulator;
//import com.arlania.world.content.ItemComparing;
import com.arlania.world.content.Lottery;
import com.arlania.world.content.MonsterDrops;
import com.arlania.world.content.PlayerPunishment;
import com.arlania.world.content.ProfileViewing;
import com.arlania.world.content.Scoreboards;
import com.arlania.world.content.WellOfGoodwill;
import com.arlania.world.content.achievements.AchievementData;
import com.arlania.world.content.clan.ClanChatManager;
import com.arlania.world.content.combat.effect.CombatPoisonEffect.CombatPoisonData;
import com.arlania.world.content.combat.strategy.CombatStrategies;
import com.arlania.world.content.customcollectionlog.CollectionLog;
import com.arlania.world.content.dialogue.DialogueManager;
import com.arlania.world.content.gim.Group;
import com.arlania.world.content.grandexchange.GrandExchangeOffers;
import com.arlania.world.content.pos.PlayerOwnedShopManager;
import com.arlania.world.entity.impl.npc.NPC;
import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import mysql.MySQLController;

/**
 * Credit: lare96, Gabbe
 */
public final class GameLoader {

	private final ExecutorService serviceLoader = Executors
			.newSingleThreadExecutor(new ThreadFactoryBuilder().setNameFormat("GameLoadingThread").build());
	private final ScheduledExecutorService executor = Executors
			.newSingleThreadScheduledExecutor(new ThreadFactoryBuilder().setNameFormat("GameThread").build());
	private final GameEngine engine;
	private final int port;

	protected GameLoader(int port) {
		this.port = port;
		this.engine = new GameEngine();
	}

	public void init() {
		Preconditions.checkState(!serviceLoader.isShutdown(), "The bootstrap has been bound already!");
		executeServiceLoad();
		serviceLoader.shutdown();
	}

	public void finish() throws IOException, InterruptedException {
		if (!serviceLoader.awaitTermination(15, TimeUnit.MINUTES))
			throw new IllegalStateException("The background service load took too long!");
		ExecutorService networkExecutor = Executors.newCachedThreadPool();
		ServerBootstrap serverBootstrap = new ServerBootstrap(
				new NioServerSocketChannelFactory(networkExecutor, networkExecutor));
		serverBootstrap.setPipelineFactory(new PipelineFactory(new HashedWheelTimer()));
		serverBootstrap.bind(new InetSocketAddress(port));
		executor.scheduleAtFixedRate(engine, 0, GameSettings.ENGINE_PROCESSING_CYCLE_RATE, TimeUnit.MILLISECONDS);
		TaskManager.submit(new ServerTimeUpdateTask());
	}

	private void executeServiceLoad() {
		if (GameSettings.MYSQL_ENABLED) {
			serviceLoader.execute(() -> MySQLController.init());
		}

		serviceLoader.execute(() -> ConnectionHandler.init());
		serviceLoader.execute(() -> AchievementData.checkDuplicateIds());

		serviceLoader.execute(MysteryBoxHandler.getInstance()::load);

		serviceLoader.execute(() -> PlayerPunishment.init());
		serviceLoader.execute(() -> EnterReferral.init());
		serviceLoader.execute(() -> ReferralHandler.init());
		serviceLoader.execute(() -> RegionClipping.init());
		serviceLoader.execute(() -> CustomObjects.init());
		serviceLoader.execute(() -> ItemDefinition.init());
		serviceLoader.execute(() -> Lottery.init());
		serviceLoader.execute(() -> GrandExchangeOffers.init());
		serviceLoader.execute(() -> Scoreboards.init());
		serviceLoader.execute(() -> WellOfGoodwill.init());
		serviceLoader.execute(() -> ClanChatManager.init());
		serviceLoader.execute(() -> CombatPoisonData.init());
		serviceLoader.execute(() -> CombatStrategies.init());
		serviceLoader.execute(() -> NpcDefinition.parseNpcs().load());
		serviceLoader.execute(() -> NPCDrops.parseDrops().load());
		serviceLoader.execute(() -> WeaponInterfaces.parseInterfaces().load());
		serviceLoader.execute(() -> ShopManager.parseShops().load());
		serviceLoader.execute(() -> DialogueManager.parseDialogues().load());
		serviceLoader.execute(() -> NPC.init());
		serviceLoader.execute(() -> ProfileViewing.init());
		serviceLoader.execute(() -> PlayerOwnedShopManager.loadShops());
		serviceLoader.execute(() -> MonsterDrops.initialize());
		serviceLoader.execute(() -> MonsterDrops.initialize());
		serviceLoader.execute(() -> General.initialize());
		serviceLoader.execute(() -> Bork.initialize());
		serviceLoader.execute(() -> Customwrencher.initialize());
		serviceLoader.execute(() -> TheZamorakLefosh.initialize());
		serviceLoader.execute(() -> Newabbadon.initialize());
		serviceLoader.execute(() -> Armadylabzyou.initialize());
		serviceLoader.execute(() -> DailyNpc.initialize());
		serviceLoader.execute(() -> Apollyon.initialize());
		serviceLoader.execute(() -> FrostBeast.initialize());
		serviceLoader.execute(() -> EmeraldWorldBoss.initialize());
		serviceLoader.execute(() -> ElementalJad.initialize());
		serviceLoader.execute(() -> TheVortex.initialize());
		serviceLoader.execute(() -> VeigarBoss.initialize());
		serviceLoader.execute(() -> DiscordRewards.init());
		serviceLoader.execute(() -> ShopManager.parseTaxShop());
		serviceLoader.execute(() -> ShopManager.parseTaxShop1());
		serviceLoader.execute(() -> AOESystem.getSingleton().parseData());
		serviceLoader.execute(() -> DropSimulator.initializeNpcs());
		serviceLoader.execute(() -> Group.load());
		serviceLoader.execute(BestInSlotInterface::loadAllItems);
		serviceLoader.execute(BattlePass.INSTANCE::initialize);
		serviceLoader.execute(DailyTasks.INSTANCE::initialize);
		serviceLoader.execute(() -> CollectionLog.getInstance().checkCustomNpcs());
		serviceLoader.execute(CollectionLog.getInstance()::loadNpcs); // serviceLoader.execute(() -> new
																		// DiscordBot().initialize());
		// serviceLoader.execute(() -> HourlyBoss.setHourlyBoss());
		// serviceLoader.execute(() -> NPC.loadDifferentFormat());
		// CrashGame.init();
		// new DailyRewards().processTime();
	}

	public GameEngine getEngine() {
		return engine;
	}
}
