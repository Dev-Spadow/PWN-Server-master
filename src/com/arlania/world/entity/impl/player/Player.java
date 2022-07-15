package com.arlania.world.entity.impl.player;

import com.arlania.GameSettings;
import com.arlania.engine.task.Task;
import com.arlania.engine.task.TaskManager;
import com.arlania.engine.task.impl.PlayerDeathTask;
import com.arlania.engine.task.impl.WalkToTask;
import com.arlania.model.*;
import com.arlania.model.container.impl.*;
import com.arlania.model.container.impl.Bank.BankSearchAttributes;
import com.arlania.model.definitions.ItemDefinition;
import com.arlania.model.definitions.WeaponAnimations;
import com.arlania.model.definitions.WeaponInterfaces;
import com.arlania.model.definitions.WeaponInterfaces.WeaponInterface;
import com.arlania.model.input.Input;
import com.arlania.net.PlayerSession;
import com.arlania.net.SessionState;
import com.arlania.net.packet.Packet;
import com.arlania.net.packet.PacketSender;
import com.arlania.util.FrameUpdater;
import com.arlania.util.Misc;
import com.arlania.util.Stopwatch;
import com.arlania.world.World;
import com.arlania.world.content.Achievements.AchievementAttributes;
import com.arlania.world.content.Achievements.Difficulty;
import com.arlania.world.content.BankPin.BankPinAttributes;
import com.arlania.world.content.*;
import com.arlania.world.content.DropLog.DropLogEntry;
import com.arlania.world.content.GamemodeSelecter.Gamemode;
import com.arlania.world.content.KillsTracker.KillsEntry;
import com.arlania.world.content.LoyaltyProgramme.LoyaltyTitles;
import com.arlania.world.content.NpcTasks.NpcTaskAttributes;
import com.arlania.world.content.StartScreen.GameModes;
import com.arlania.world.content.StarterTasks.StarterTaskAttributes;
import com.arlania.world.content.achievements.AchievementInterface;
import com.arlania.world.content.achievements.AchievementTracker;
import com.arlania.world.content.aoesystem.AoEInstance;
import com.arlania.world.content.battlepass.BattlePass;
import com.arlania.world.content.bis.BestInSlotInterface;
import com.arlania.world.content.boxspinner.CustomBoxSpinner;
import com.arlania.world.content.chance.goodiebag.OwnerGoodiebag;
import com.arlania.world.content.clan.ClanChat;
import com.arlania.world.content.clan.ClanChatManager;
import com.arlania.world.content.combat.CombatFactory;
import com.arlania.world.content.combat.CombatType;
import com.arlania.world.content.combat.effect.CombatPoisonEffect.CombatPoisonData;
import com.arlania.world.content.combat.magic.CombatSpell;
import com.arlania.world.content.combat.magic.CombatSpells;
import com.arlania.world.content.combat.magic.CustomMagicStaff;
import com.arlania.world.content.combat.prayer.CurseHandler;
import com.arlania.world.content.combat.prayer.PrayerHandler;
import com.arlania.world.content.combat.pvp.PlayerKillingAttributes;
import com.arlania.world.content.combat.range.CombatRangedAmmo.RangedWeaponData;
import com.arlania.world.content.combat.strategy.CombatStrategies;
import com.arlania.world.content.combat.strategy.CombatStrategy;
import com.arlania.world.content.combat.weapon.CombatSpecial;
import com.arlania.world.content.combat.weapon.FightType;
import com.arlania.world.content.customraid.CustomRaid;
import com.arlania.world.content.customraid.CustomRaidParty;
import com.arlania.world.content.daily_reward.DailyReward;
import com.arlania.world.content.dialogue.Dialogue;
import com.arlania.world.content.droptable.DropTableManager;
import com.arlania.world.content.forging.ForgingManager;
import com.arlania.world.content.gamblinginterface.GamblingInterface;
import com.arlania.world.content.grandexchange.GrandExchangeSlot;
import com.arlania.world.content.instances.InstanceData;
import com.arlania.world.content.interfaces.QuestTab;
import com.arlania.world.content.invansionminigame.InvasionGame;
import com.arlania.world.content.item_upgrader.UpgradeData;
import com.arlania.world.content.item_upgrader.UpgradeHandler;
import com.arlania.world.content.item_upgrader.UpgradeType;
import com.arlania.world.content.itemcombiner.Combiner;
import com.arlania.world.content.keepsake.ItemOverwrite;
import com.arlania.world.content.keepsake.KeepSakePreset;
import com.arlania.world.content.minigames.Minigame;
import com.arlania.world.content.minigames.MinigameAttributes;
import com.arlania.world.content.minigames.impl.Dueling;
import com.arlania.world.content.mysteryboxes.MysteryBox;
import com.arlania.world.content.mysteryboxes.MysteryBoxOpener;
import com.arlania.world.content.mysteryboxes.MysteryBoxViewerOwner;
import com.arlania.world.content.newminigames.Controller;
import com.arlania.world.content.perk_system.PerkHandler;
import com.arlania.world.content.playersettings.PlayerSetting;
import com.arlania.world.content.pos.PlayerOwnedShopManager;
import com.arlania.world.content.raids.RaidParty;
import com.arlania.world.content.referral.ReferralHandler;
import com.arlania.world.content.roulette.Roulette;
import com.arlania.world.content.scratchcards.ScratchCard;
import com.arlania.world.content.skill.SkillManager;
import com.arlania.world.content.skill.impl.construction.ConstructionData.HouseLocation;
import com.arlania.world.content.skill.impl.construction.ConstructionData.HouseTheme;
import com.arlania.world.content.skill.impl.construction.HouseFurniture;
import com.arlania.world.content.skill.impl.construction.Portal;
import com.arlania.world.content.skill.impl.construction.Room;
import com.arlania.world.content.skill.impl.farming.Farming;
import com.arlania.world.content.skill.impl.slayer.BloodSlayer;
import com.arlania.world.content.skill.impl.slayer.Slayer;
import com.arlania.world.content.skill.impl.summoning.BossPets;
import com.arlania.world.content.skill.impl.summoning.Familiar;
import com.arlania.world.content.skill.impl.summoning.Pouch;
import com.arlania.world.content.skill.impl.summoning.Summoning;
import com.arlania.world.content.slayer.slayerparty;
import com.arlania.world.content.starterzone.StarterZone;
import com.arlania.world.content.teleport.TeleportData;
import com.arlania.world.content.teleport.TeleportPage;
import com.arlania.world.content.timedlocations.TimedLocation;
import com.arlania.world.content.well_of_goodwill.WellOfGoodwillHandler;
import com.arlania.world.content.wheel.WheelOfFortune;
import com.arlania.world.entity.impl.Character;
import com.arlania.world.entity.impl.GroundItemManager;
import com.arlania.world.entity.impl.npc.Bosses.CustomBossInstance;
import com.arlania.world.entity.impl.npc.Bosses.Sagittare;
import com.arlania.world.entity.impl.npc.Bosses.zulrah.Zulrah;
import com.arlania.world.entity.impl.npc.NPC;
import com.arlania.world.entity.impl.npc.minigame.KeyRoom;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

//import mysql.impl.Donation;

public class Player extends Character {

    public static boolean Spinning = false;
    public static boolean inAOEInstance;
    public final StarterZone starterZone = new StarterZone(this);
    public final DailyRewards dailyRewards = new DailyRewards(this);
    private final BestInSlotInterface bis = new BestInSlotInterface(this);
    private final NpcTaskAttributes npcTaskAttributes = new NpcTaskAttributes();
    private final Stopwatch doubleRateTimer = new Stopwatch();
    private final Stopwatch iceCreamRateTimer = new Stopwatch();
    private final PlayerOwnedShopManager playerOwnedShopManager = new PlayerOwnedShopManager(this);
    // Timers (Stopwatches)
    private final Stopwatch sqlTimer = new Stopwatch();
    private final Stopwatch foodTimer = new Stopwatch();
    private final Stopwatch potionTimer = new Stopwatch();
    private final Stopwatch lastRunRecovery = new Stopwatch();
    private final Stopwatch clickDelay = new Stopwatch();
    private final Stopwatch lastItemPickup = new Stopwatch();
    private final Stopwatch lastYell = new Stopwatch();
    private final Stopwatch lastZulrah = new Stopwatch();
    private final Stopwatch lastSql = new Stopwatch();
    private final Stopwatch lastVengeance = new Stopwatch();
    private final Stopwatch emoteDelay = new Stopwatch();
    private final Stopwatch specialRestoreTimer = new Stopwatch();
    private final Stopwatch lastSummon = new Stopwatch();
    private final Stopwatch recordedLogin = new Stopwatch();
    @SuppressWarnings("unused")
    private final Stopwatch creationDate = new Stopwatch();
    private final Stopwatch tolerance = new Stopwatch();
    private final Stopwatch lougoutTimer = new Stopwatch();
    private final Stopwatch doubleDrops = new Stopwatch();
    /**
     * * INSTANCES **
     */
    private final CopyOnWriteArrayList<KillsEntry> killsTracker = new CopyOnWriteArrayList<KillsEntry>();
    private final CopyOnWriteArrayList<DropLogEntry> dropLog = new CopyOnWriteArrayList<DropLogEntry>();
    private final List<Player> localPlayers = new LinkedList<Player>();
    private final List<NPC> localNpcs = new LinkedList<NPC>();
    private final PlayerProcess process = new PlayerProcess(this);
    private final PlayerKillingAttributes playerKillingAttributes = new PlayerKillingAttributes(this);
    private final MinigameAttributes minigameAttributes = new MinigameAttributes();
    private final BankPinAttributes bankPinAttributes = new BankPinAttributes();
    private final BankSearchAttributes bankSearchAttributes = new BankSearchAttributes();
    private final AchievementAttributes achievementAttributes = new AchievementAttributes();
    private final StarterTaskAttributes starterTaskAttributes = new StarterTaskAttributes();
    private final BonusManager bonusManager = new BonusManager();
    private final PointsHandler pointsHandler = new PointsHandler(this);
    private final PacketSender packetSender = new PacketSender(this);
    private final Appearance appearance = new Appearance(this);
    private final FrameUpdater frameUpdater = new FrameUpdater();
    public Map<PlayerSetting, Integer> playerSettings = new LinkedHashMap<>();
    public int[] lootingBagStorageItemId = new int[28];
    public int[] lootingBagStorageItemAmount = new int[28];
    /**
     * Custom drop rate from salvage
     */
    public SalvageExchange.TokenExchange selectedTokenExchange;
    public int salvageDropRate;
    /**
     * Gamemode selector
     */
    public Gamemode selectedGamemode = Gamemode.NORMAL;
    /**
     * Group iron man
     */
    public String groupOwner = "";
    public Player requestToJoin;
    /**
     * Checks if the killer should receive a drop
     */
    public boolean receiveDrop = true;
    /**
     * Keepsake
     */
    public KeepSakePreset[] keepSakePresets = new KeepSakePreset[5];
    public int[] overrideItems = new int[15];
    public ArrayList<ItemOverwrite> overwritableItems = new ArrayList<ItemOverwrite>();
    public transient ArrayList<Integer> displayedItems = new ArrayList<Integer>();
    public transient int selectedSlot = -1;
    public Map<Integer, Map<Integer, Integer>> collectedItems = new HashMap<>();
    /*
     * New teleport controller variables
     */
    public TeleportPage teleportPageOpen = null;
    public TeleportData teleportView = null;
    public TeleportData previousTeleport = null;
    public boolean claimedFirst;
    public boolean claimedSecond;
    public boolean claimedThird;
    public long lastDonation;
    public long lastTimeReset;
    public boolean inLMS;
    public boolean inDreamZoneTimedLocation;
    public boolean inLMSLobby;
    public int lmsLifes;
    public int lmsKillStreak;
    public int lmsPoints;
    public boolean starterClaimed;
    public boolean day1Claimed = false;
    public boolean day2Claimed = false;
    public boolean day3Claimed = false;
    public boolean day4Claimed = false;
    public boolean day5Claimed = false;
    public boolean day6Claimed = false;
    public boolean day7Claimed = false;
    public long lastLogin;
    public long lastDailyClaim;
    public long lastVoteTime;
    public boolean hasVotedToday;
    public boolean npcTaskClaimed;
    public boolean inDragon = false;
    public boolean attackable = true;
    public boolean hasEntered = false;
    public boolean starterTaskCompleted;
    public Item upgradeSelection;
    public Difficulty difficulty;
    public boolean hasReferral;
    /*
     * Variables for DropTable & Player Profiling
     *
     * @author Levi Patton
     *
     * @www.rune-server.org/members/auguryps
     */
    public Player dropLogPlayer;
    public boolean dropLogOrder;
    public int clue1Amount;
    public int clue2Amount;
    public int clue3Amount;
    public int clueLevel;
    public Item[] puzzleStoredItems;
    public int sextantGlobalPiece;
    public double sextantBarDegree;
    public int rotationFactor;
    public int sextantLandScapeCoords;
    public int sextantSunCoords;
    /**
     * * INTS **
     */
    public int destination = 0;
    public int lastClickedTab = 0;
    public int timeOnline;
    public ArrayList<Integer> walkableInterfaceList = new ArrayList<>();
    public long lastHelpRequest;
    public long lastAuthClaimed;
    public GameModes selectedGameMode;
    public boolean inFFA;
    public boolean inFFALobby;
    public boolean inCustomFFA;
    public boolean inCustomFFALobby;
    public int[] oldSkillLevels = new int[25];
    public int[] oldSkillXP = new int[25];
    public int[] oldSkillMaxLevels = new int[25];
    public int[] skillPoints = new int[60];
    public String macAddress;
    public Zulrah zulrah = new Zulrah(this);
    public Sagittare sagittare = new Sagittare(this);
    public int ZULRAH_CLICKS = 0;
    public CustomBossInstance customBossInstance;
    public Position selectedPos = null;
    public int keyCount1, keyCount2, keyCount3;
    public int savedKeyCount1 = 0, savedKeyCount2 = 0, savedKeyCount3 = 0;
    public KeyRoom keyRoom;
    public long lastOpPotion;
    public long lastDonationClaim;
    public long lastHpRestore;
    public long lastVeigarRaid;
    public long lastPrayerRestore;
    public long lastCashClaim;
    public long lastSpecialClaim;
    public long lastSpecialRestoreClaim;
    public transient int dissolveId, dissolveOrbAmount, dissolveXP;
    public boolean hasFirstTimeTimerSet;
    public int box = 0;
    public int currentPlayerPanelIndex = 1;
    public String referaledby = "";
    public UpgradeData currentUpgrade;
    public UpgradeType upgradeType;
    public WellOfGoodwillHandler wellOfGoodwillHandler = new WellOfGoodwillHandler();
    public boolean recievedDD = false;
    public LocalDateTime agroPotionTime = LocalDateTime.MIN;
    public boolean activeAgro = this.agroPotionTime != LocalDateTime.MIN;
    public boolean sentFadeAgroPot = true;
    public LocalDateTime newPlayerBoostTime = LocalDateTime.MIN;
    public boolean hasPlayerBoostTime = false;
    public int selectedGoodieBag = -1;
    Stopwatch messageBlock = new Stopwatch();
    /**
     * Drop Simulator
     */
    DropSimulator ds = new DropSimulator(this);
    /**
     * Defenders minigame
     */

    DefendersMinigame defendersMg = new DefendersMinigame(this);
    /**
     * Invasion minigame
     */

    InvasionGame invasionGame = new InvasionGame(this);
    KCSystem kcSystem = new KCSystem(this);
    int totalBossKills;
    int dailyClaimed = Calendar.DAY_OF_YEAR;
    /**
     * Mini me
     */

    private Player owner;
    private boolean miniPlayer;
    private Item[] minimeEquipment = new Item[14];
    private MiniMeSystem miniMeSystem = new MiniMeSystem(this);
    private CustomBoxSpinner customBoxSpinner = new CustomBoxSpinner(this);
    /**
     * Timed locations*
     */
    private TimedLocation currentTimedLocation = null;
    /**
     * Raids
     */

    private CustomRaidParty customRaidParty = null;
    private Set<String> bannedRaidMembers = new HashSet<>();
    private boolean inRaid = false;
    private CustomRaid customRaid = new CustomRaid(this);
    private Controller controller;
    private boolean inStorage;
    private transient int tempItem;
    private transient int tempSkillInt;
    private transient boolean tempSkillingBoolean;
    private transient String actionInterface = "";
    /*
     * Drop table variables
     */
    private DropTableManager dropTableManager = new DropTableManager(this);
    private boolean inInstance;
    /*
     * Forging variables
     */
    private ForgingManager forgingManager = new ForgingManager(this);
    private int invasionKc;
    /**
     * Roulette
     */

    private Roulette roulette = new Roulette(this);
    private long rouletteBalance;
    private long rouletteBet;
    /**
     * Combiner
     */

    private Combiner combiner = new Combiner(this);
    /**
     * Instance manager interface
     */

    private InstanceInterfaceManager instanceInterface = new InstanceInterfaceManager(this);
    private int amountDonatedToday;
    private boolean rewardTimerActive1;
    private boolean rewardTimerActive2;
    private boolean rewardTimerActive3;
    private boolean rewardTimerActive4;
    private int rewardTimer1;
    private int rewardTimer2;
    private int rewardTimer3;
    private int rewardTimer4;
    private ScratchCard scratchCard = new ScratchCard(this);
    private Map<Integer, Integer> npcKillCountMap = new HashMap<>();
    private DonationDeals donationDeals = new DonationDeals(this);
    private long crashGameBalance = 0;
    private long crashGameBet = 0;
    private double crashAutoCashout = 0.0;
    private double cashedOutMult = 0.0;
    /**
     * Crash game variables
     */

    private int betAmount;
    private double cashoutMultiplier;
    private int depositedAmount;
    private List<Integer> blockedCollectorsList = new ArrayList<>();
    private int praiseTime;
    private int iceCreamTime;
    private boolean[] battlePassClaimedRewards = new boolean[BattlePass.INSTANCE.getRewards().size()];
    private int battlePassExp;
    private boolean battlePass;
    private int battlePassSeason;
    private int chocIceCreamTime;
    private int candyTime;
    private boolean candyRateActive = false;
    private int eatPumpkinTime;
    private boolean eatPumpkinRateActive = false;
    private int smokeTheBongTime;
    private boolean smokeTheBongRateActive = false;
    private boolean chocCreamRateActive = false;
    private int chocCreamTime;
    private boolean giveaway;
    private int cleansingTime;
    private int aoePayment = 0;
    private int aoeNpcId = -1;
    private AoEInstance aoeInstance = null;
    // summer event
    private int summerKC;
    private int summer1KC;
    private int summer2KC;
    private int summer3KC;
    private int minionsKC;
    private int herculesKC;
    private int hadesKC;
    private int lucarioKC;
    private int charizardKC;
    private int defendersKC;
    private int godzillaKC;
    private int demonolmKC;
    private int cerbKC;
    private int zeusKC;
    private int infarticoKC;
    private int valorKC;
    private int hwKC;
    private int dzanthKC;
    private int kongKC;
    private int corpKC;
    private int lucidKC;
    private int hulkKC;
    private int darkblueKC;
    private int pyroKC;
    private int wyrmKC;
    private int exodenKC;
    private int trinityKC;
    private int cloudKC;
    private ItemEnchantments itemEnchantments;
    private int herbalKC;
    private int supremeKC;
    private int breakerKC;
    private int apolloKC;
    private int noxKC;
    private int azazelKC;
    private int ravanaKC;
    private int luminKC;
    private int customhKC;
    private int razorKC;
    private int dreamflowKC;
    private int khioneKC;
    private int sableKC;
    private int demoKC;
    private int yoshiKC;
    private int avatarKC;
    private int liliKC;
    private int obitoKC;
    private int uruKC;
    private int kumihoKC;
    private int mysteryKC;
    private int customOlmKC;
    private String bravekDifficulty;
    private int gokuKC;
    private int vegetaKC;
    private int hweenKC;
    private int WarmongerHealth = 0;
    private int sellToShopPoints;
    private boolean DoubleDropsActive = false;
    private boolean doubleRateActive = false;
    private boolean iceCreamRateActive = false;
    private String savedPin;
    private String savedIp;
    private boolean hasPin = false;
    private boolean Warmonger = false;
    private int[] maxCapeColors = {65214, 65200, 65186, 62995};
    private int[] compCapeColors = {65214, 65200, 65186, 62995};
    private int currentCape;
    private int currentHat;
    private String title = "";
    private boolean active;
    private boolean shopUpdated;
    private Map<String, Object> attributes = new HashMap<>();
    private Minigame minigame = null;
    private int hardwareNumber;
    private int runeunityPoints;
    private int bossPoints;
    private int customPoints;

    // Instances
    private int loyaltyPoints;
    private PlayerDropLog playerDropLog = new PlayerDropLog();
    private ProfileViewing profile = new ProfileViewing();
    private int bloodslayerTaskCompleted;
    private Bank bank = new Bank(this);
    private boolean jailed;
    /**
     * * STRINGS **
     */
    private String username;
    private String password;
    private String serial_number;
    private String emailAddress;
    private String hostAddress;
    private String clanChatName;
    private HouseLocation houseLocation;
    private HouseTheme houseTheme;
    /**
     * * LONGS *
     */
    private Long longUsername;
    private long moneyInPouch;
    private long totalPlayTime;
    private ArrayList<HouseFurniture> houseFurniture = new ArrayList<HouseFurniture>();
    private ArrayList<Portal> housePortals = new ArrayList<>();
    private PlayerSession session;
    private CharacterAnimations characterAnimations = new CharacterAnimations();
    private PlayerRights rights = PlayerRights.PLAYER;
    private SkillManager skillManager = new SkillManager(this);
    private PlayerRelations relations = new PlayerRelations(this);
    private ChatMessage chatMessages = new ChatMessage();
    private Inventory inventory = new Inventory(this);

    // end summer event
    private Equipment equipment = new Equipment(this);
    private PriceChecker priceChecker = new PriceChecker(this);
    private Trading trading = new Trading(this);
    private GamblingInterface gambling = new GamblingInterface(this);
    private Dueling dueling = new Dueling(this);
    private Slayer slayer = new Slayer(this);
    private BloodSlayer bloodslayer = new BloodSlayer(this);
    private Farming farming = new Farming(this);
    private Summoning summoning = new Summoning(this);
    private Bank[] bankTabs = new Bank[9];
    private Room[][][] houseRooms = new Room[5][13][13];
    private PlayerInteractingOption playerInteractingOption = PlayerInteractingOption.NONE;
    private GameMode gameMode = GameMode.NORMAL;
    private CombatType lastCombatType = CombatType.MELEE;
    private FightType fightType = FightType.UNARMED_PUNCH;
    private Prayerbook prayerbook = Prayerbook.NORMAL;
    private MagicSpellbook spellbook = MagicSpellbook.NORMAL;
    private LoyaltyTitles loyaltyTitle = LoyaltyTitles.NONE;
    private ClanChat currentClanChat;
    private Input inputHandling;
    private WalkToTask walkToTask;
    private Shop shop;
    private GameObject interactingObject;
    private Item interactingItem;
    private Dialogue dialogue;
    private DwarfCannon cannon;
    private CombatSpell autocastSpell, castSpell, previousCastSpell;
    private RangedWeaponData rangedWeaponData;
    private CombatSpecial combatSpecial;
    private WeaponInterface weapon;
    private Item untradeableDropItem;
    private Object[] usableObject;
    private GrandExchangeSlot[] grandExchangeSlots = new GrandExchangeSlot[6];
    private Task currentTask;
    private Position resetPosition;
    private Pouch selectedPouch;
    private BlowpipeLoading blowpipeLoading = new BlowpipeLoading(this);
    private int[] brawlerCharges = new int[9];
    private int[] forceMovement = new int[7];
    private int[] leechedBonuses = new int[7];
    private int[] ores = new int[2];
    private int[] constructionCoords;
    private int recoilCharges;
    private int runEnergy = 100;
    private int currentBankTab;
    private int interfaceId, walkableInterfaceId, multiIcon;
    private int dialogueActionId;
    private int overloadPotionTimer, prayerRenewalPotionTimer;
    private int fireImmunity, fireDamageModifier;
    private int amountDonated;
    private int wildernessLevel;
    private int bravekTasksCompleted;
    private int fireAmmo;
    private int customWellTimesDonated;
    private int specialPercentage = 100;
    private int skullIcon = -1, skullTimer;
    private int teleblockTimer;
    private int dragonFireImmunity;
    private int poisonImmunity;
    private int shadowState;
    private int effigy;
    private int dfsCharges;
    private int playerViewingIndex;
    private int staffOfLightEffect;
    private int minutesBonusExp = -1;
    private int selectedGeSlot = -1;
    private int selectedGeItem = -1;
    private int geQuantity;
    private int gePricePerItem;
    private int selectedSkillingItem;
    private int currentBookPage;
    private int storedRuneEssence, storedPureEssence;
    private int trapsLaid;
    private int skillAnimation;
    private int houseServant;
    private int houseServantCharges;
    private int servantItemFetch;
    private int portalSelected;
    private int constructionInterface;
    private int buildFurnitureId;
    private int buildFurnitureX;
    private int buildFurnitureY;
    private int combatRingType;
    /**
     * * BOOLEANS **
     */
    private boolean unlockedLoyaltyTitles[] = new boolean[12];
    private boolean[] crossedObstacles = new boolean[7];
    private boolean processFarming;
    private boolean crossingObstacle;
    private boolean targeted;
    private boolean isBanking, noteWithdrawal, swapMode;
    private boolean regionChange, allowRegionChangePacket;
    private boolean isDying;
    private boolean isLegendary;
    private boolean isMbox1;
    private boolean isMbox2;
    private boolean isMbox3;
    private boolean isMbox4;
    private boolean isMbox5;
    private boolean isRunning = true, isResting;
    private boolean experienceLocked;
    private boolean bloodFountain;
    private boolean dreamZone;
    private boolean clientExitTaskActive;
    private boolean drainingPrayer;
    private boolean shopping;
    private boolean settingUpCannon;
    private boolean hasVengeance;
    private boolean killsTrackerOpen;
    private boolean acceptingAid;
    private boolean autoRetaliate;
    private boolean autocast;
    private boolean specialActivated;
    private boolean isCoughing;
    private boolean playerLocked;
    private boolean recoveringSpecialAttack;
    private boolean soundsActive, musicActive;
    private boolean newPlayer;
    private boolean openBank;
    private boolean inActive;
    private boolean inConstructionDungeon;
    private boolean isBuildingMode;
    private boolean voteMessageSent;
    private boolean receivedStarter;
    private Position previousPosition;
    private MysteryBox mysteryBox = new MysteryBox(this);
    private WheelOfFortune wheelOfFortune = new WheelOfFortune(this);
    private boolean areCloudsSpawned;
    private long bestZulrahTime;
    private boolean placeholders = true;
    private ArrayList<RaidParty> pendingRaidPartyInvites = new ArrayList<RaidParty>();
    private RaidParty raidParty;
    private int npcKills;
    private int itemToUpgrade;
    /*
     * Daily reward variables
     */
    private DailyReward dailyReward = new DailyReward(this);
    private boolean claimedTodays = false;
    private int transform;
    private int timer;
    private MysteryBoxOpener mysteryBoxOpener = new MysteryBoxOpener(this);
    private MysteryBoxViewerOwner mysteryBoxViewerOwner = new MysteryBoxViewerOwner(this);
    /**
     * DPS
     */

    private DPSOverlay dpsOverlay = new DPSOverlay(this);
    private StartScreen startScreen = null;
    private AchievementInterface achievementInterface;
    private AchievementTracker achievementTracker = new AchievementTracker(this);

    @Getter
    @Setter
    public double AchievementDRBoost;

    private ReferralHandler refHandler = new ReferralHandler();
    private UpgradeHandler upgradeHandler = new UpgradeHandler(this);
    private PerkHandler perkHandler = new PerkHandler();
    private GoodieBag goodieBag = new GoodieBag(this);

    private OwnerGoodiebag oGB = new OwnerGoodiebag(this);
    private boolean sendDpsOverlay = true;

    private static int killLevel = 1;
    private static int killExperience = 0;
    private static int killPrestige = 0;

    public Player(PlayerSession playerIO) {
        super(GameSettings.DEFAULT_POSITION.copy());
        this.session = playerIO;
    }

    public static KeyTypes get(int npcId) {
        for (KeyTypes i : KeyTypes.values()) {
            if (i.npcId == npcId)
                return i;
        }
        return null;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public boolean isMiniPlayer() {
        return miniPlayer;
    }

    public void setMiniPlayer(boolean miniPlayer) {
        this.miniPlayer = miniPlayer;
    }

    public Item[] getMinimeEquipment() {
        return minimeEquipment;
    }

    public void setMinimeEquipment(Item[] minimeEquipment) {
        this.minimeEquipment = minimeEquipment;
    }

    public void setMinimeEquipment(Item item, int slot) {
        minimeEquipment[slot] = item;
    }

    public MiniMeSystem getMinimeSystem() {
        return miniMeSystem;
    }

    public Map<PlayerSetting, Integer> getPlayerSettings() {
        return playerSettings;
    }

    public void setPlayerSettings(Map<PlayerSetting, Integer> playerSettings) {
        this.playerSettings = playerSettings;
    }

    public CustomBoxSpinner getCustomBoxSpinner() {
        return customBoxSpinner;
    }

    public Stopwatch getMessageBlock() {
        return messageBlock;
    }

    public TimedLocation getCurrentTimedLocation() {
        return currentTimedLocation;
    }

    public void setCurrentTimedLocation(TimedLocation timedLocation) {
        currentTimedLocation = timedLocation;
    }

    public void setSpinning(boolean Spinning) {
        this.Spinning = Spinning;
    }

    public CustomRaidParty getCustomRaidParty() {
        return this.customRaidParty;
    }

    public void setCustomRaidParty(CustomRaidParty raidParty) {
        this.customRaidParty = raidParty;
    }

    public Set<String> getBannedRaidMembers() {
        return this.bannedRaidMembers;
    }

    public void setBannedRaidMembers(Set<String> bannedRaidMembers) {
        this.bannedRaidMembers = bannedRaidMembers;
    }

    public BestInSlotInterface getBis() {
        return bis;
    }

    public CustomRaid getCustomRaid() {

        return customRaid;
    }

    public void setCustomRaid(CustomRaid customRaid) {
        this.customRaid = customRaid;
    }

    public boolean isInRaid() {
        return inRaid;
    }

    public void setInRaid(boolean inRaid) {
        this.inRaid = inRaid;
    }

    public Controller getController() {
        return this.controller;
    }

    public boolean inStorage() {
        return this.inStorage;
    }

    public void setStorage(Boolean b) {
        this.inStorage = b;
    }

    public boolean isAllowedToOverride() {
        return true;// !inWilderness() && !Boundary.isIn(this, Boundary.DUEL_LOBBY) &&
        // !Boundary.isIn(this, Boundary.DUEL_ARENAS);
    }

    public int getTempItem() {
        return this.tempItem;
    }

    public void setTempItem(int i) {
        this.tempItem = i;
    }

    public String getActionInterface() {
        return this.actionInterface;
    }

    public void setActionInterface(String s) {
        this.actionInterface = s;
    }

    public int getTempSkillInt() {
        return this.tempSkillInt;
    }

    public void setTempSkillInt(int i) {
        this.tempSkillInt = i;
    }

    public boolean getTempSkillBoolean() {
        return this.tempSkillingBoolean;
    }

    public void setTempSkillBoolean(boolean b) {
        this.tempSkillingBoolean = b;
    }

    public boolean isIronMan() {
        return getGameMode() == GameMode.IRONMAN || getGameMode() == GameMode.HARDCORE_IRONMAN
                || getGameMode() == GameMode.GROUP_IRONMAN;
    }

    public DropTableManager getDropTableManager() {
        return dropTableManager;
    }

    /**
     * Collection Log
     */
    public void handleCollectedItem(int npcId, Item item) {
        int id = item.getId();
        int amount = item.getAmount();
        if (collectedItems.get(npcId) == null) {
            Map<Integer, Integer> itemData = new HashMap<>();
            itemData.put(id, amount);
            collectedItems.put(npcId, itemData);
        } else {
            collectedItems.get(npcId).merge(id, amount, Integer::sum);
        }
        for (Map.Entry<Integer, Map<Integer, Integer>> collectedItemsForMonster : collectedItems.entrySet()) {
            // System.out.println("Collection log for monster " +
            // collectedItemsForMonster.getKey());
            for (Map.Entry<Integer, Integer> collectedItem : collectedItemsForMonster.getValue().entrySet()) {
                // System.out.println("\t Item " + collectedItem.getKey() + " x " +
                // collectedItem.getValue());
            }
        }
    }

    public Map<Integer, Map<Integer, Integer>> getCollectedItems() {
        return collectedItems;
    }

    public void setCollectedItems(Map<Integer, Map<Integer, Integer>> collectedItems) {
        this.collectedItems = collectedItems;
    }

    public boolean isInInstance() {
        return inInstance;
    }

    public void setInInstance(boolean inInstance) {
        this.inInstance = inInstance;
    }

    public ForgingManager getForgingManager() {
        return forgingManager;
    }

    public DropSimulator getDropSimulator() {
        return ds;
    }

    public DefendersMinigame getDefendersMg() {
        return defendersMg;
    }

    public InvasionGame getInvasionGame() {
        return invasionGame;
    }

    public int getInvasionKc() {
        return invasionKc;
    }

    public void setInvasionKc(int amount) {
        this.invasionKc = amount;
    }

    public boolean getBloodFountain() {
        return bloodFountain;
    }

    public void setBloodFountain(boolean bloodFountain) {
        this.bloodFountain = bloodFountain;
    }

    public boolean getDreamZone(boolean DreamZone) {
        return this.dreamZone = DreamZone;
    }

    public void incrementInvasionKc(int amount) {
        this.invasionKc += amount;
    }

    public void decrementInvasionKc(int amount) {
        this.invasionKc -= amount;
    }

    public Roulette getRoulette() {
        return roulette;
    }

    public long getRouletteBalance() {
        return rouletteBalance;
    }

    public void setRouletteBalance(long amount) {
        this.rouletteBalance = amount;
    }

    public void incrementRouletteBalance(long amount) {
        this.rouletteBalance += amount;
    }

    public void decrementRouletteBalance(long amount) {
        this.rouletteBalance -= amount;
    }

    public long getRouletteBet() {
        return rouletteBet;
    }

    public void setRouletteBet(long amount) {
        this.rouletteBet = amount;
    }

    public Combiner getCombiner() {
        return combiner;
    }

    public InstanceInterfaceManager getInstanceInterface() {
        return instanceInterface;
    }

    public int getAmountDonatedToday() {
        return amountDonatedToday;
    }

    public void setAmountDonatedToday(int amount) {
        this.amountDonatedToday = amount;
    }

    public void incrementAmountDonatedToday(int amount) {
        this.amountDonatedToday += amount;
    }

    public boolean hasRewardTimerActive1() {
        return rewardTimerActive1;
    }

    public void setRewardTimerActive1(boolean active) {
        this.rewardTimerActive1 = active;
    }

    public boolean hasRewardTimerActive2() {
        return rewardTimerActive2;
    }

    public void setRewardTimerActive2(boolean active) {
        this.rewardTimerActive2 = active;
    }

    public boolean hasRewardTimerActive3() {
        return rewardTimerActive3;
    }

    public void setRewardTimerActive3(boolean active) {
        this.rewardTimerActive3 = active;
    }

    public boolean hasRewardTimerActive4() {
        return rewardTimerActive4;
    }

    public void setRewardTimerActive4(boolean active) {
        this.rewardTimerActive4 = active;
    }

    public ScratchCard getScratchCard() {
        return scratchCard;
    }

    public Map<Integer, Integer> getNpcKillCount() {
        return npcKillCountMap;
    }

    public void setNpcKillCount(Map<Integer, Integer> dataMap) {
        this.npcKillCountMap = dataMap;
    }

    public void addNpcKillCount(int npcId) {
        npcKillCountMap.merge(npcId, 1, Integer::sum);
    }

    public void addNpcKillCount1(int npcId, int amount) {
        npcKillCountMap.merge(npcId, 1, Integer::sum);
    }

    public int getNpcKillCount(int npcId) {
        return npcKillCountMap.get(npcId) == null ? 0 : npcKillCountMap.get(npcId);
    }

    public int getKcSum() {
        return npcKillCountMap.values().stream().mapToInt(Integer::intValue).sum();
    }

    public KCSystem getKcSystem() {
        return kcSystem;
    }

    public int getRewardTimer1() {
        return rewardTimer1;
    }

    public void setRewardTimer1(int amount) {
        this.rewardTimer1 = amount;
    }

    public int getRewardTimer2() {
        return rewardTimer2;
    }

    public void setRewardTimer2(int amount) {
        this.rewardTimer2 = amount;
    }

    // ah i need to send u latest cache, sec. ok 2min remaining
    public int getRewardTimer3() {
        return rewardTimer3;
    }

    public void setRewardTimer3(int amount) {
        this.rewardTimer3 = amount;
    }

    public int getRewardTimer4() {
        return rewardTimer4;
    }

    public void setRewardTimer4(int amount) {
        this.rewardTimer4 = amount;
    }

    public void decrementRewardTimer1(int amount) {
        this.rewardTimer1 -= amount;
    }

    public void decrementRewardTimer2(int amount) {
        this.rewardTimer2 -= amount;
    }

    public void decrementRewardTimer3(int amount) {
        this.rewardTimer3 -= amount;
    }

    public void decrementRewardTimer4(int amount) {
        this.rewardTimer4 -= amount;
    }

    public DonationDeals getDonationDeals() {
        return donationDeals;
    }

    // private int[] santaColors = { 10351, 933 };

    public double getCashedOutMult() {
        return cashedOutMult;
    }

    public void setCashedOutMult(double cashedOutMult) {
        this.cashedOutMult = cashedOutMult;
    }

    public double getCrashAutoCashout() {
        return crashAutoCashout;
    }

    public void setCrashAutoCashout(double crashAutoCashout) {
        this.crashAutoCashout = crashAutoCashout;
    }

    public long getCrashGameBet() {
        return crashGameBet;
    }

    public void setCrashGameBet(long crashGameBet) {
        this.crashGameBet = crashGameBet;
    }

    public void addToCrashBalance(long l) {
        crashGameBalance += l;
    }

    /*
     * public int[] getSantaColors() { return santaColors; }
     *
     * public void setSantaColors(int[] santaColors) { this.santaColors =
     * santaColors; }
     */

    public void removeFromCrashBalance(long amount) {
        crashGameBalance -= amount;
    }

    public long getCrashGameBalance() {
        return crashGameBalance;
    }

    public int getBetAmount() {
        return betAmount;
    }

    public void setBetAmount(int betAmount) {
        this.betAmount = betAmount;
    }

    public double getCashoutMultiplier() {
        return cashoutMultiplier;
    }

    public void setCashoutMultiplier(double cashoutMultiplier) {
        this.cashoutMultiplier = cashoutMultiplier;
    }

    public int getDepositedAmount() {
        return depositedAmount;
    }

    public void setDepositedAmount(int depositedAmount) {
        this.depositedAmount = depositedAmount;
    }

    public void incrementDepositedAmount(int amount) {
        this.depositedAmount += amount;
    }

    public void decrementDepositedAmount(int amount) {
        this.depositedAmount -= amount;
    }

    public NpcTaskAttributes getNpcTaskAttributes() {
        return npcTaskAttributes;
    }

    public DailyRewards getDailyRewards() {
        return dailyRewards;
    }

    public int getBattlePassSeason() {
        return battlePassSeason;
    }

    public void setBattlePassSeason(final int battlePassSeason) {
        this.battlePassSeason = battlePassSeason;
    }

    public boolean hasBattlePass() {
        return battlePass;
    }

    public int getBattlePassExp() {
        return battlePassExp;
    }

    public void setBattlePassExp(final int battlePassExp) {
        this.battlePassExp = battlePassExp;
    }

    public void incrementBattlePassExp(final long exp) {
        if (hasBattlePass()) {
            QuestTab.updateCharacterSummaryTab(this, QuestTab.UpdateData.BATTLEPASS);
            this.battlePassExp += exp;
        }
    }

    public void setBattlePass(final boolean battlePass) {
        this.battlePass = battlePass;
    }

    public boolean[] getBattlePassClaimedRewards() {
        return battlePassClaimedRewards;
    }

    public void setBattlePassClaimedRewards(final boolean[] battlePassClaimedRewards) {
        this.battlePassClaimedRewards = battlePassClaimedRewards;
    }

    public boolean hasClaimedBattlePassReward(final int index) {
        return index < battlePassClaimedRewards.length && battlePassClaimedRewards[index];
    }

    public void markClaimedBattlePassReward(final int index) {
        if (index >= battlePassClaimedRewards.length) {
            return;
        }
        battlePassClaimedRewards[index] = true;
    }

    public boolean isCandyRateActive() {
        return candyRateActive;
    }

    public void setCandyRateActive(boolean candyRateActive) {
        this.candyRateActive = candyRateActive;
    }

    public int getCandyTime() {
        return candyTime;
    }

    public void setCandyTime(int candyTime) {
        this.candyTime = candyTime;
    }

    public void incrementCandyTime(int candyTime) {
        this.candyTime += candyTime;
    }

    public void decrementCandyTime(int candyTime) {
        this.candyTime -= candyTime;
    }

    public boolean isEatPumpkinRateActive() {
        return eatPumpkinRateActive;
    }

    public void setEatPumpkinRateActive(boolean eatPumpkinRateActive) {
        this.eatPumpkinRateActive = eatPumpkinRateActive;
    }

    public int getEatPumpkinTime() {
        return eatPumpkinTime;
    }

    public void setEatPumpkinTime(int eatPumpkinTime) {
        this.eatPumpkinTime = eatPumpkinTime;
    }

    public void incrementEatPumpkinTime(int eatPumpkinTime) {
        this.eatPumpkinTime += eatPumpkinTime;
    }

    public void decrementEatPumpkinTime(int eatPumpkinTime) {
        this.eatPumpkinTime -= eatPumpkinTime;
    }

    public boolean isSmokeTheBongRateActive() {
        return smokeTheBongRateActive;
    }

    public void setSmokeTheBongRateActive(boolean smokeTheBongRateActive) {
        this.smokeTheBongRateActive = smokeTheBongRateActive;
    }

    public int getSmokeTheBongTime() {
        return smokeTheBongTime;
    }

    public void setSmokeTheBongTime(int smokeTheBongTime) {
        this.smokeTheBongTime = smokeTheBongTime;
    }

    public void incrementSmokeTheBongTime(int smokeTheBongTime) {
        this.smokeTheBongTime += smokeTheBongTime;
    }

    public void decrementSmokeTheBongTime(int smokeTheBongTime) {
        this.smokeTheBongTime -= smokeTheBongTime;
    }

    public boolean isChocCreamRateActive() {
        return chocCreamRateActive;
    }

    public void setChocCreamRateActive(boolean chocCreamRateActive) {
        this.chocCreamRateActive = chocCreamRateActive;
    }

    public int getChocCreamTime() {
        return chocCreamTime;
    }

    public void setChocCreamTime(int chocCreamTime) {
        this.chocCreamTime = chocCreamTime;
    }

    public void incrementChocCreamTime(int chocCreamTime) {
        this.chocCreamTime += chocCreamTime;
    }

    public void decrementChocCreamTime(int chocCreamTime) {
        this.chocCreamTime -= chocCreamTime;
    }

    public int getIceCreamTime() {
        return iceCreamTime;
    }

    public void setIceCreamTime(int iceCreamTime) {
        this.iceCreamTime = iceCreamTime;
    }

    public void incrementIceCreamTime(int iceCreamTime) {
        this.iceCreamTime += iceCreamTime;
    }

    public void decrementIceCreamTime(int iceCreamTime) {
        this.iceCreamTime -= iceCreamTime;
    }

    public int getPraiseTime() {
        return praiseTime;
    }

    public void setPraiseTime(int praiseTime) {
        this.praiseTime = praiseTime;
    }

    public void incrementPraiseTime(int praiseTime) {
        this.praiseTime += praiseTime;
    }

    public void decrementPraiseTime(int praiseTime) {
        this.praiseTime -= praiseTime;
    }

    public boolean isGiveaway() {
        return giveaway;
    }

    public void setGiveaway(boolean giveaway) {
        this.giveaway = giveaway;
    }

    public int getCleansingTime() {
        return cleansingTime;
    }

    public void setCleansingTime(int cleansingTime) {
        this.cleansingTime = cleansingTime;
    }

    public void incrementCleansingTime(int cleansingTime) {
        this.cleansingTime += cleansingTime;
    }

    public void decrementCleansingTime(int cleansingTime) {
        this.cleansingTime -= cleansingTime;
    }

    public List<Integer> getBlockedCollectorsList() {
        return blockedCollectorsList;
    }

    public boolean getStarterClaimed() {
        return starterClaimed;
    }

    public void setStarterClaimed(boolean starterClaimed) {
        this.starterClaimed = starterClaimed;
    }

    public boolean isStarterTaskCompleted() {
        return starterTaskCompleted;
    }

    public void setStarterTaskCompleted() {
        starterTaskCompleted = true;
    }

    public int getLmsPoints() {
        return lmsPoints;
    }

    public void setLmsPoints(int lmsPoints) {
        this.lmsPoints = lmsPoints;
    }

    public void incrementLmsPoints(int amount) {
        this.lmsPoints += amount;
    }

    public void decrementLmsPoints(int amount) {
        this.lmsPoints -= amount;
    }

    // private Channel channel;

    // public Player write(Packet packet) {
    // if (channel.isConnected()) {
    // channel.write(packet);
    // }
    // return this;
    // }

    /// public Channel getChannel() {
    // return channel;
    // }

    public int getLMSLifes() {
        return lmsLifes;
    }

    public void setLMSLifes(int lmsLifes) {
        this.lmsLifes = lmsLifes;
    }

    public void incrementLMSLifes(int amount) {
        this.lmsLifes += amount;
    }

    public void decrementLMSLifes(int amount) {
        this.lmsLifes -= amount;
    }

    public int getLMSKillStreak() {
        return lmsKillStreak;
    }

    public void incrementLMSKillStreak(int amount) {
        this.lmsKillStreak += amount;
    }

    public void resetLMSKillStreak() {
        lmsKillStreak = 0;
    }

    public Item getUpgradeSelection() {
        return upgradeSelection;
    }

    public void setUpgradeSelection(Item upgradeSelection) {
        this.upgradeSelection = upgradeSelection;
    }

    public void setAoEPayment(int amount) {
        aoePayment = amount;
    }

    public void setAoeNpc(int id) {
        aoeNpcId = id;
    }

    public int getAoENpc() {
        return aoeNpcId;
    }

    public boolean payAoePayment() {
        if (getInventory().getAmount(5606) >= 1) {
            aoePayment = 0;
            getInventory().delete(5606, 1);
            sendMessage("@blu@You use your voucher to gain 30 minutes of access!");
            getAoEInstance().time = LocalDateTime.now().plusMinutes(30);
            getPacketSender().sendString(59803, getAoEInstance().getTime());
            sendParallellInterfaceVisibility(59800, true);
            return true;
        }
        if (getInventory().getAmount(10835) >= aoePayment) {
            getInventory().delete(10835, aoePayment, true);
            getAoEInstance().time = LocalDateTime.now().plusMinutes(30);
            getPacketSender().sendString(59803, getAoEInstance().getTime());
            sendParallellInterfaceVisibility(59800, true);
            PlayerLogs.log(getUsername(), "Npc: " + aoeNpcId + " - " + aoePayment);
            aoePayment = 0;
            return true;
        } else {
            sendMessage("You do no have the " + Misc.format(aoePayment) + " Taxbags to do this.");
            return false;
        }
    }


    public AoEInstance getAoEInstance() {
        return aoeInstance;
    }

    public void setAoEInstance(AoEInstance instance) {
        aoeInstance = instance;
    }

    public int getAoEPayAmount() {
        return aoePayment;
    }

    /*
     * Fields
     */

    public int getSummerKC() {
        return summerKC;
    }

    public void setSummerKC(int summerKC) {
        this.summerKC = summerKC;
    }

    public void incrementSummerKC(int amount) {
        this.summerKC += amount;
    }

    public int getSummer1KC() {
        return summer1KC;
    }

    public void setSummer1KC(int summer1KC) {
        this.summer1KC = summerKC;
    }

    public void incrementSummer1KC(int amount) {
        this.summer1KC += amount;
    }

    public int getSummer2KC() {
        return summer2KC;
    }

    public void setSummer2KC(int summer2KC) {
        this.summer2KC = summer2KC;
    }

    public void incrementSummer2KC(int amount) {
        this.summer2KC += amount;
    }

    public int getSummer3KC() {
        return summer3KC;
    }

    public void setSummer3KC(int summer3KC) {
        this.summer3KC = summer3KC;
    }

    public void incrementSummer3KC(int amount) {
        this.summer3KC += amount;
    }

    public int getMinionsKC() {
        return minionsKC;
    }

    public void setMinionsKC(int minionsKC) {
        this.minionsKC = minionsKC;
    }

    public void incrementMinionsKC(int amount) {
        this.minionsKC += amount;
    }

    public int getHerculesKC() {
        return herculesKC;
    }

    public void setHerculesKC(int herculesKC) {
        this.herculesKC = herculesKC;
    }

    public void incrementHerculesKC(int amount) {
        this.herculesKC += amount;
    }

    public int getHadesKC() {
        return hadesKC;
    }

    public void setHadesKC(int hadesKC) {
        this.hadesKC = hadesKC;
    }

    public void incrementHadesKC(int amount) {
        this.hadesKC += amount;
    }

    public int getLucarioKC() {
        return lucarioKC;
    }

    public void setLucarioKC(int lucarioKC) {
        this.lucarioKC = lucarioKC;
    }

    public void incrementLucarioKC(int amount) {
        this.lucarioKC += amount;
    }

    public int getCharizardKC() {
        return charizardKC;
    }

    public void setCharizardKC(int charizardKC) {
        this.charizardKC = charizardKC;
    }

    public void incrementCharizardKC(int amount) {
        this.charizardKC += amount;
    }

    public int getDefendersKC() {
        return defendersKC;
    }

    public void setDefendersKC(int defendersKC) {
        this.defendersKC = defendersKC;
    }

    public void incrementDefendersKC(int amount) {
        this.defendersKC += amount;
    }

    public int getGodzillaKC() {
        return godzillaKC;
    }

    public void setGodzillaKC(int GodzillaKC) {
        this.godzillaKC = GodzillaKC;
    }

    public void incrementGodzillaKC(int amount) {
        this.godzillaKC += amount;
    }

    public int getDemonolmKC() {
        return demonolmKC;
    }

    public void setDemonolmKC(int DemonolmKC) {
        this.demonolmKC = DemonolmKC;
    }

    public void incrementDemonolmKC(int amount) {
        this.demonolmKC += amount;
    }

    public int getCerbKC() {
        return cerbKC;
    }

    public void setCerbKC(int CerbKC) {
        this.cerbKC = CerbKC;
    }

    public void incrementCerbKC(int amount) {
        this.cerbKC += amount;
    }

    public int getZeusKC() {
        return zeusKC;
    }

    public void setZeusKC(int ZeusKC) {
        this.zeusKC = ZeusKC;
    }

    public void incrementZeusKC(int amount) {
        this.zeusKC += amount;
    }

    public int getInfarticoKC() {
        return infarticoKC;
    }

    public void setInfarticoKC(int InfarticoKC) {
        this.infarticoKC = InfarticoKC;
    }

    public void incrementInfarticoKC(int amount) {
        this.infarticoKC += amount;
    }

    public int getValorKC() {
        return valorKC;
    }

    public void setValorKC(int ValorKC) {
        this.valorKC = ValorKC;
    }

    public void incrementValorKC(int amount) {
        this.valorKC += amount;
    }

    public int getHwKC() {
        return hwKC;
    }

    public void setHwKC(int HwKC) {
        this.hwKC = HwKC;
    }

    public void incrementHwKC(int amount) {
        this.hwKC += amount;
    }

    public int getDzanthKC() {
        return dzanthKC;
    }

    public void setDzanthKC(int DzanthKC) {
        this.dzanthKC = DzanthKC;
    }

    public void incrementDzanthKC(int amount) {
        this.dzanthKC += amount;
    }

    public int getKongKC() {
        return kongKC;
    }

    public void setKongKC(int KongKC) {
        this.kongKC = KongKC;
    }

    public void incrementKongKC(int amount) {
        this.kongKC += amount;
    }

    public int getCorpKC() {
        return corpKC;
    }

    public void setCorpKC(int CorpKC) {
        this.corpKC = CorpKC;
    }

    public void incrementCorpKC(int amount) {
        this.corpKC += amount;
    }

    public int getLucidKC() {
        return lucidKC;
    }

    public void setLucidKC(int LucidKC) {
        this.lucidKC = LucidKC;
    }

    public void incrementLucidKC(int amount) {
        this.lucidKC += amount;
    }

    public int getHulkKC() {
        return hulkKC;
    }

    public void setHulkKC(int HulkKC) {
        this.hulkKC = HulkKC;
    }

    public void incrementHulkKC(int amount) {
        this.hulkKC += amount;
    }

    public int getDarkblueKC() {
        return darkblueKC;
    }

    public void setDarkblueKC(int DarkblueKC) {
        this.darkblueKC = DarkblueKC;
    }

    public void incrementDarkblueKC(int amount) {
        this.darkblueKC += amount;
    }

    public int getPyroKC() {
        return pyroKC;
    }

    public void setPyroKC(int PyroKC) {
        this.pyroKC = PyroKC;
    }

    public void incrementPyroKC(int amount) {
        this.pyroKC += amount;
    }

    public int getWyrmKC() {
        return wyrmKC;
    }

    public void setWyrmKC(int WyrmKC) {
        this.wyrmKC = WyrmKC;
    }

    public void incrementWyrmKC(int amount) {
        this.wyrmKC += amount;
    }

    public int getExodenKC() {
        return exodenKC;
    }

    public void setExodenKC(int ExodenKC) {
        this.exodenKC = ExodenKC;
    }

    public void incrementExodenKC(int amount) {
        this.exodenKC += amount;
    }

    public int getTrinityKC() {
        return trinityKC;
    }

    public void setTrinityKC(int TrinityKC) {
        this.trinityKC = TrinityKC;
    }

    public void incrementTrinityKC(int amount) {
        this.trinityKC += amount;
    }

    public int getCloudKC() {
        return cloudKC;
    }

    public void setCloudKC(int CloudKC) {
        this.cloudKC = CloudKC;
    }

    public ItemEnchantments getItemEnchantments() {
        return itemEnchantments;
    }

    public void setItemEnchantments(ItemEnchantments e) {
        itemEnchantments = e;
    }

    public void incrementCloudKC(int amount) {
        this.cloudKC += amount;
    }

    public int getHerbalKC() {
        return herbalKC;
    }

    public void setHerbalKC(int HerbalKC) {
        this.herbalKC = HerbalKC;
    }

    public void incrementHerbalKC(int amount) {
        this.herbalKC += amount;
    }

    public int getSupremeKC() {
        return supremeKC;
    }

    public void setSupremeKC(int SupremeKC) {
        this.supremeKC = SupremeKC;
    }

    public void incrementSupremeKC(int amount) {
        this.supremeKC += amount;
    }

    public int getBreakerKC() {
        return breakerKC;
    }

    public void setBreakerKC(int BreakerKC) {
        this.breakerKC = BreakerKC;
    }

    public void incrementBreakerKC(int amount) {
        this.breakerKC += amount;
    }

    public int getApolloKC() {
        return apolloKC;
    }

    public void setApolloKC(int ApolloKC) {
        this.apolloKC = ApolloKC;
    }

    public void incrementApolloKC(int amount) {
        this.apolloKC += amount;
    }

    public int getNoxKC() {
        return noxKC;
    }

    public void setNoxKC(int NoxKC) {
        this.noxKC = NoxKC;
    }

    public void incrementNoxKC(int amount) {
        this.noxKC += amount;
    }

    public int getAzazelKC() {
        return azazelKC;
    }

    public void setAzazelKC(int AzazelKC) {
        this.azazelKC = AzazelKC;
    }

    public void incrementAzazelKC(int amount) {
        this.azazelKC += amount;
    }

    public int getRavanaKC() {
        return ravanaKC;
    }

    public void setRavanaKC(int RavanaKC) {
        this.ravanaKC = RavanaKC;
    }

    public void incrementRavanaKC(int amount) {
        this.ravanaKC += amount;
    }

    public int getLuminKC() {
        return luminKC;
    }

    public void setLuminKC(int LuminKC) {
        this.luminKC = LuminKC;
    }

    public void incrementLuminKC(int amount) {
        this.luminKC += amount;
    }

    public int getCustomhKC() {
        return customhKC;
    }

    public void setCustomhKC(int CustomhKC) {
        this.customhKC = CustomhKC;
    }

    public void incrementCustomhKC(int amount) {
        this.customhKC += amount;
    }

    public int getRazorKC() {
        return razorKC;
    }

    public void setRazorKC(int RazorKC) {
        this.razorKC = RazorKC;
    }

    public void incrementRazorKC(int amount) {
        this.razorKC += amount;
    }

    public int getDreamflowKC() {
        return dreamflowKC;
    }

    public void setDreamflowKC(int DreamflowKC) {
        this.dreamflowKC = DreamflowKC;
    }

    public void incrementDreamflowKC(int amount) {
        this.dreamflowKC += amount;
    }

    public int getKhioneKC() {
        return khioneKC;
    }

    public void setKhioneKC(int KhioneKC) {
        this.khioneKC = KhioneKC;
    }

    public void incrementKhioneKC(int amount) {
        this.khioneKC += amount;
    }

    public int getSableKC() {
        return sableKC;
    }

    public void setSableKC(int SableKC) {
        this.sableKC = SableKC;
    }

    public void incrementSableKC(int amount) {
        this.sableKC += amount;
    }

    public int getDemoKC() {
        return demoKC;
    }

    public void setDemoKC(int DemoKC) {
        this.demoKC = DemoKC;
    }

    public void incrementDemoKC(int amount) {
        this.demoKC += amount;
    }

    public int getYoshiKC() {
        return yoshiKC;
    }

    public void setYoshiKC(int YoshiKC) {
        this.yoshiKC = YoshiKC;
    }

    public void incrementYoshiKC(int amount) {
        this.yoshiKC += amount;
    }

    public int getAvatarKC() {
        return avatarKC;
    }

    public void setAvatarKC(int AvatarKC) {
        this.avatarKC = AvatarKC;
    }

    public void incrementAvatarKC(int amount) {
        this.avatarKC += amount;
    }

    public int getLiliKC() {
        return liliKC;
    }

    public void setLiliKC(int LiliKC) {
        this.liliKC = LiliKC;
    }

    public void incrementLiliKC(int amount) {
        this.liliKC += amount;
    }

    public int getObitoKC() {
        return obitoKC;
    }

    public void setObitoKC(int ObitoKC) {
        this.obitoKC = ObitoKC;
    }

    public void incrementObitoKC(int amount) {
        this.obitoKC += amount;
    }

    public int getUruKC() {
        return uruKC;
    }

    public void setUruKC(int UruKC) {
        this.uruKC = UruKC;
    }

    public void incrementUruKC(int amount) {
        this.uruKC += amount;
    }

    public int getKumihoKC() {
        return kumihoKC;
    }

    public void setKumihoKC(int KumihoKC) {
        this.kumihoKC = KumihoKC;
    }

    public void incrementKumihoKC(int amount) {
        this.kumihoKC += amount;
    }

    public int getMysteryKC() {
        return mysteryKC;
    }

    public void setMysteryKC(int MysteryKC) {
        this.mysteryKC = MysteryKC;
    }

    public void incrementMysteryKC(int amount) {
        this.mysteryKC += amount;
    }

    public String getBravekDifficulty() {
        return bravekDifficulty;
    }

    public void setBravekDifficulty(String bravekDifficulty) {
        this.bravekDifficulty = bravekDifficulty;
    }

    public int getcustomOlmKC() {
        return customOlmKC;
    }

    public void incrementCustomOlmKC(int amount) {
        this.customOlmKC += amount;
    }

    public void setCustomOlmKC(int amount) {
        this.customOlmKC = amount;
    }

    public int getgokuKC() {
        return gokuKC;
    }

    public void incrementGokuKC(int amount) {
        this.gokuKC += amount;
    }

    public void setGokuKC(int amount) {
        this.gokuKC = amount;
    }

    public int getvegetaKC() {
        return vegetaKC;
    }

    public void incrementVegetaKC(int amount) {
        this.vegetaKC += amount;
    }

    public void setVegetaKC(int amount) {
        this.vegetaKC = amount;
    }

    public int gethweenKC() {
        return hweenKC;
    }

    public void incrementHweenKC(int amount) {
        this.hweenKC += amount;
    }

    public void decreaseHweenKC(int amount) {
        this.hweenKC -= amount;
    }

    public void setHweenKC(int amount) {
        this.hweenKC = amount;
    }

    public void incrementTotalBossKills(int amount) {
        this.totalBossKills += amount;
    }

    public int getTotalBossKills() {
        return totalBossKills;
    }

    public void setTotalBossKills(int totalBossKills) {
        this.totalBossKills = totalBossKills;
    }

    public int getSellToPoints() {
        return sellToShopPoints;
    }

    public void setShopPoints(int points) {
        this.sellToShopPoints += points;
    }

    public int getWarmongerHealth() {
        return WarmongerHealth;
    }

    public void setWarmongerHealth(int WarmongerHealth) {
        this.WarmongerHealth = WarmongerHealth;
    }

    public boolean isDoubleDropsActive() {
        return DoubleDropsActive;
    }

    public void setDoubleDropsActive(boolean doubleDropsActive) {
        DoubleDropsActive = doubleDropsActive;
    }

    public boolean isDoubleRateActive() {
        return doubleRateActive;
    }

    public void setDoubleRateActive(boolean doubleRateActive) {
        this.doubleRateActive = doubleRateActive;
    }

    public boolean isIceCreamRateActive() {
        return iceCreamRateActive;
    }

    public void setIceCreamRateActive(boolean iceCreamRateActive) {
        this.iceCreamRateActive = iceCreamRateActive;
    }

    public String getSavedPin() {
        return savedPin;
    }

    public void setSavedPin(String savedPin) {
        this.savedPin = savedPin;
    }

    public String getSavedIp() {
        return savedIp;
    }

    public void setSavedIp(String savedIp) {
        this.savedIp = savedIp;
    }

    public boolean getHasPin() {
        return hasPin;
    }

    public void setHasPin(boolean hasPin) {
        this.hasPin = hasPin;
    }

    public Stopwatch getDoubleRateTimer() {
        return doubleRateTimer;
    }

    public Stopwatch getIceCreamRateTimer() {
        return iceCreamRateTimer;
    }

    public boolean getWarmonger() {
        return Warmonger;
    }

    public void setWarmonger(boolean Warmonger) {
        this.Warmonger = Warmonger;
    }

    public int[] getCompCapeColors() {
        return compCapeColors;
    }

    public void setCompCapeColors(int[] compCapeColors) {
        this.compCapeColors = compCapeColors;
    }

    public int getCurrentCape() {
        return currentCape;
    }

    public void setCurrentCape(int currentCape) {
        this.currentCape = currentCape;
    }

    public int getCurrentHat() {
        return currentHat;
    }

    public void setCurrentHat(int currentHat) {
        this.currentHat = currentHat;
    }

    public int[] getMaxCapeColors() {
        return maxCapeColors;
    }

    public void setMaxCapeColors(int[] maxCapeColors) {
        this.maxCapeColors = maxCapeColors;
    }

    public PlayerOwnedShopManager getPlayerOwnedShopManager() {
        return playerOwnedShopManager;
    }

    @SuppressWarnings("unchecked")
    public <T> T getAttribute(String key) {
        return (T) attributes.get(key);
    }

    @SuppressWarnings("unchecked")
    public <T> T getAttribute(String key, T fail) {
        Object object = attributes.get(key);
        return object == null ? fail : (T) object;
    }

    public boolean hasAttribute(String key) {
        return attributes.containsKey(key);
    }

    public void removeAttribute(String key) {
        attributes.remove(key);
    }

    public int getHardwareNumber() {
        return hardwareNumber;
    }

    public Player setHardwareNumber(int hardwareNumber) {
        this.hardwareNumber = hardwareNumber;
        return this;
    }

    public void setAttribute(String key, Object value) {
        attributes.put(key, value);
    }

    @Override
    public void appendDeath() {
        if (!isDying) {
            isDying = true;
            TaskManager.submit(new PlayerDeathTask(this));
        }
    }

    public int getRuneUnityPoints() {
        return runeunityPoints;
    }

    public void setRuneUnityPoints(int runeunityPoints) {
        this.runeunityPoints = runeunityPoints;
    }

    public void incrementRuneUnityPoints(double amount) {
        this.runeunityPoints -= amount;
    }

    public int getBossPoints() {
        return bossPoints;
    }

    public void setBossPoints(int bossPoints) {
        this.bossPoints = bossPoints;
    }

    public int getCustomPoints() {
        return customPoints;
    }

    public void setCustomPoints(int customPoints) {
        this.customPoints = customPoints;
    }

    public void decrementCustomPoints(double amount) {
        this.customPoints -= amount;
    }

    public void incrementCustomPoints(double amount) {
        this.customPoints += amount;
    }

    public int getLoyaltyPoints() {
        return loyaltyPoints;
    }

    public void setLoyaltyPoints(int loyaltyPoints) {
        this.loyaltyPoints = loyaltyPoints;
    }

    /*
     * Variables for the DropLog
     *
     * @author Levi Patton
     */
    public PacketSender getPA() {
        return getPacketSender();
    }

    public PlayerDropLog getPlayerDropLog() {
        return playerDropLog;
    }

    public void setPlayerDropLog(PlayerDropLog playerDropLog) {
        this.playerDropLog = playerDropLog;
    }

    public ProfileViewing getProfile() {
        return profile;
    }

    public void setProfile(ProfileViewing profile) {
        this.profile = profile;
    }

    public void setBloodslayerTaskCompleted(int bloodslayerTaskCompleted, boolean add) {
        if (add)
            this.bloodslayerTaskCompleted += bloodslayerTaskCompleted;
        else
            this.bloodslayerTaskCompleted = bloodslayerTaskCompleted;
    }

    public void setBravekTasksCompleted(int bravekTasksCompleted, boolean add) {
        if (add)
            this.bravekTasksCompleted += bravekTasksCompleted;
        else
            this.bravekTasksCompleted = bravekTasksCompleted;
    }

    public void setCustomWellDonated(int customWellTimesDonated, boolean add) {
        if (add)
            this.customWellTimesDonated += customWellTimesDonated;
        else
            this.customWellTimesDonated = customWellTimesDonated;
    }

    public int getBloodslayerTaskCompleted() {
        return bloodslayerTaskCompleted;
    }

    public int getBravekTasksCompleted() {
        return bravekTasksCompleted;
    }

    public int getCustomDonations() {
        return customWellTimesDonated;
    }

    public void incrementCustomWellDonated(int amount) {
        this.customWellTimesDonated += amount;
    }

    public void incrementBravekTasksCompleted(int amount) {
        this.bravekTasksCompleted += amount;
    }

    public void decrementBravekTasksCompleted(int amount) {
        this.bravekTasksCompleted -= amount;
    }

    public void incrementBloodslayerTaskCompleted(int amount) {
        this.bloodslayerTaskCompleted += amount;
    }

    public void decrementBloodslayerTaskCompleted(int amount) {
        this.bloodslayerTaskCompleted -= amount;
    }

    @Override
    public int getConstitution() {
        return getSkillManager().getCurrentLevel(Skill.CONSTITUTION);
    }

    @Override
    public Character setConstitution(int constitution) {
        if (isDying) {
            return this;
        }
        skillManager.setCurrentLevel(Skill.CONSTITUTION, constitution);
        packetSender.sendSkill(Skill.CONSTITUTION);
        if (this.miniPlayer && getConstitution() <= 0) {
            this.setConstitution(1);
        }
        if (getConstitution() <= 0 && !this.miniPlayer && !isDying) {
            appendDeath();
        }
        return this;
    }

    @Override
    public void heal(int amount) {
        int level = skillManager.getMaxLevel(Skill.CONSTITUTION);
        if ((skillManager.getCurrentLevel(Skill.CONSTITUTION) + amount) >= level) {
            setConstitution(level);
        } else {
            setConstitution(skillManager.getCurrentLevel(Skill.CONSTITUTION) + amount);
        }
    }

    @Override
    public int getBaseAttack(CombatType type) {
        if (type == CombatType.RANGED) {
            return skillManager.getCurrentLevel(Skill.RANGED);
        } else if (type == CombatType.MAGIC) {
            return skillManager.getCurrentLevel(Skill.MAGIC);
        }
        return skillManager.getCurrentLevel(Skill.ATTACK);
    }

    @Override
    public int getBaseDefence(CombatType type) {
        if (type == CombatType.MAGIC) {
            return skillManager.getCurrentLevel(Skill.MAGIC);
        }
        return skillManager.getCurrentLevel(Skill.DEFENCE);
    }

    @Override
    public int getAttackSpeed() {
        int speed = weapon.getSpeed();
        String weapon = equipment.get(Equipment.WEAPON_SLOT).getDefinition().getName();
        if (getCurrentlyCasting() != null) {
            if (equipment.get(Equipment.WEAPON_SLOT).getId() == 11605) {
                return 4;
            } else if (equipment.get(Equipment.WEAPON_SLOT).getId() == 11609) {
                return 2;
            } else if (equipment.get(Equipment.WEAPON_SLOT).getId() == 3951) {
                return 2;
            } else if (equipment.get(Equipment.WEAPON_SLOT).getId() == 5129) {
                return 2;
            } else if (equipment.get(Equipment.WEAPON_SLOT).getId() == 6483) {
                return 2;
            } else if (equipment.get(Equipment.WEAPON_SLOT).getId() == 3275) {
                return 2;
            } else if (equipment.get(Equipment.WEAPON_SLOT).getId() == 11605) {
                return 2;
            } else if (equipment.get(Equipment.WEAPON_SLOT).getId() == 15653) {
                return 2;
            } else if (equipment.get(Equipment.WEAPON_SLOT).getId() == 19727) {
                return 2;
            } else if (equipment.get(Equipment.WEAPON_SLOT).getId() == 8474) {
                return 1;
            } else if (equipment.get(Equipment.WEAPON_SLOT).getId() == 3279) {
                return 1;

            } else if (equipment.get(Equipment.WEAPON_SLOT).getId() == 19720) {
                return 2;
            } else if (equipment.get(Equipment.WEAPON_SLOT).getId() == 16518) {
                return 2;
            } else if (equipment.get(Equipment.WEAPON_SLOT).getId() == 3282) {
                return 1;
            } else if (equipment.get(Equipment.WEAPON_SLOT).getId() == 3291) {
                return 2;
            } else if (equipment.get(Equipment.WEAPON_SLOT).getId() == 5069) {
                return 2;
            } else if (equipment.get(Equipment.WEAPON_SLOT).getId() == 5273) {
                return 2;
            } else if (equipment.get(Equipment.WEAPON_SLOT).getId() == 5179) {
                return 2;
            } else if (equipment.get(Equipment.WEAPON_SLOT).getId() == 3911) {
                return 3;
            } else if (equipment.get(Equipment.WEAPON_SLOT).getId() == 4741) {
                return 1;
            } else if (equipment.get(Equipment.WEAPON_SLOT).getId() == 14029) {
                return 1;
            } else if (equipment.get(Equipment.WEAPON_SLOT).getId() == 3920) {
                return 1;
            } else if (equipment.get(Equipment.WEAPON_SLOT).getId() == 15009) {
                return 1;
            } else if (equipment.get(Equipment.WEAPON_SLOT).getId() == 8042) {
                return 1;
            } else if (equipment.get(Equipment.WEAPON_SLOT).getId() == 13388) {
                return 1;
            } else if (equipment.get(Equipment.WEAPON_SLOT).getId() == 8031) {
                return 1;
            } else if (equipment.get(Equipment.WEAPON_SLOT).getId() == 8705) {
                return 1;
            } else if (equipment.get(Equipment.WEAPON_SLOT).getId() == 16478) {
                return 1;
            } else if (equipment.get(Equipment.WEAPON_SLOT).getId() == 16463) {
                return 1;

            } else if (equipment.get(Equipment.WEAPON_SLOT).getId() == 16435) {
                return 1;
            } else if (equipment.get(Equipment.WEAPON_SLOT).getId() == 14027) {
                return 1;
            } else if (equipment.get(Equipment.WEAPON_SLOT).getId() == 16475) {
                return 1;
            } else if (equipment.get(Equipment.WEAPON_SLOT).getId() == 16476) {
                return 1;
            } else if (equipment.get(Equipment.WEAPON_SLOT).getId() == 16436) {
                return 1;
            } else if (equipment.get(Equipment.WEAPON_SLOT).getId() == 14028) {
                return 1;
            } else if (equipment.get(Equipment.WEAPON_SLOT).getId() == 16468) {
                return 1;
            } else if (equipment.get(Equipment.WEAPON_SLOT).getId() == 16495) {
                return 1;
            } else if (equipment.get(Equipment.WEAPON_SLOT).getId() == 16459) {
                return 1;
            } else if (equipment.get(Equipment.WEAPON_SLOT).getId() == 14910) {
                return 1;
            } else if (equipment.get(Equipment.WEAPON_SLOT).getId() == 14550) {
                return 1;
            } else if (equipment.get(Equipment.WEAPON_SLOT).getId() == 17379) {
                return 1;
            } else if (equipment.get(Equipment.WEAPON_SLOT).getId() == 5116) {
                return 1;
            } else if (equipment.get(Equipment.WEAPON_SLOT).getId() == 8817) {
                return 1;
            } else if (equipment.get(Equipment.WEAPON_SLOT).getId() == 7035) {
                return 1;
            } else if (equipment.get(Equipment.WEAPON_SLOT).getId() == 5216) {
                return 1;
            } else if (equipment.get(Equipment.WEAPON_SLOT).getId() == 5217) {
                return 1;
            } else if (equipment.get(Equipment.WEAPON_SLOT).getId() == 17845) {
                return 1;
            } else if (equipment.get(Equipment.WEAPON_SLOT).getId() == 6499) {
                return 1;
            } else if (equipment.get(Equipment.WEAPON_SLOT).getId() == 8041) {
                return 1;
            } else if (equipment.get(Equipment.WEAPON_SLOT).getId() == 13389) {
                return 1;
            } else if (equipment.get(Equipment.WEAPON_SLOT).getId() == 5265) {
                return 1;
            } else if (equipment.get(Equipment.WEAPON_SLOT).getId() == 8818) {
                return 1;
            } else if (equipment.get(Equipment.WEAPON_SLOT).getId() == 18866) {
                return 1;
            } else if (equipment.get(Equipment.WEAPON_SLOT).getId() == 6498) {
                return 1;
            } else if (equipment.get(Equipment.WEAPON_SLOT).getId() == 6934) {
                return 1;
            } else if (equipment.get(Equipment.WEAPON_SLOT).getId() == 15656) {
                return 3;
            } else if (equipment.get(Equipment.WEAPON_SLOT).getId() == 5274) {
                return 3;
            } else if (equipment.get(Equipment.WEAPON_SLOT).getId() == 8664) {
                return 3;
            } else if (equipment.get(Equipment.WEAPON_SLOT).getId() == 8656) {
                return 1;
            } else if (equipment.get(Equipment.WEAPON_SLOT).getId() == 14581) {
                return 1;
            } else if (equipment.get(Equipment.WEAPON_SLOT).getId() == 19618) {
                return 3;
            } else if (equipment.get(Equipment.WEAPON_SLOT).getId() == 13995) {
                return 2;
            } else if (equipment.get(Equipment.WEAPON_SLOT).getId() == 14922) {
                return 2;

            }

            if (getCurrentlyCasting() == CombatSpells.BLOOD_BLITZ.getSpell()
                    || getCurrentlyCasting() == CombatSpells.SHADOW_BLITZ.getSpell()
                    || getCurrentlyCasting() == CombatSpells.SMOKE_BLITZ.getSpell()
                    || getCurrentlyCasting() == CombatSpells.ICE_BLITZ.getSpell()) {
                return 5;
            } else {
                return 6;
            }
        }
        int weaponId = equipment.get(Equipment.WEAPON_SLOT).getId();
        if (weaponId == 1419) {
            speed -= 2;
        }
        if (weaponId == 19720 || weaponId == 15653) {
            speed *= 0.66;
        }

        if (weaponId == 3276) {
            speed *= 1;
        }
        if (weaponId == 3274) {
            speed *= 0.66;
        }
        if (weaponId == 14535) {
            speed *= 0.66;
        }
        if (weaponId == 19123 || weaponId == 4741 || weaponId == 16478 || weaponId == 14029 || weaponId == 3920 || weaponId == 15009 || weaponId == 8042 || weaponId == 13388 || weaponId == 8031
                || weaponId == 8705 || weaponId == 5175 || weaponId == 16468 || weaponId == 16495 || weaponId == 16459
                || weaponId == 14910 || weaponId == 14550 || weaponId == 17379 || weaponId == 5116 || weaponId == 8817
                || weaponId == 7035 || weaponId == 5265 || weaponId == 8041 || weaponId == 13389 || weaponId == 5216 || weaponId == 5217 || weaponId == 6498 || weaponId == 8818
                || weaponId == 6499) {
            speed *= 2;
        }
        if (weaponId == 20917) {
            speed *= 0.25;
        }
        if (weaponId == 17853) {
            speed *= 0.15;
        }
        if (weaponId == 8040) {
            speed *= 0.15;
        }
        if (weaponId == 12185) {
            speed *= 0.15;
        }
        if (weaponId == 11952) {
            speed *= 0.15;
        }
        if (weaponId == 14287) {
            speed *= 0.20;
        }
        if (weaponId == 17929) {
            speed *= 0.30;
        }
        if (weaponId == 16528) {
            speed *= 0.15;
        }
        if (weaponId == 5263) {
            speed *= 0.15;
        }
        if (weaponId == 13024) {
            speed *= 0.20;
        }
        if (weaponId == 5267) {
            speed *= 0.20;
        }
        if (weaponId == 16523) {
            speed *= 0.60;
        }
        if (weaponId == 16525) {
            speed *= 0.80;
        }
        if (weaponId == 16526) {
            speed *= 0.60;
        }

        if (weaponId == 13201) {
            speed *= 0.50;
        }

        if (weaponId == 19720 || weaponId == 19618 || weaponId == 19620 || weaponId == 14018 || weaponId == 6320) {
            speed *= 0.50;
        }

        if (weaponId == 5130 || weaponId == 5131) {
            speed *= 0.80;
        }
        if (weaponId == 16555 || weaponId == 5264 || weaponId == 16470 || weaponId == 5271 || weaponId == 5272
                || weaponId == 14436 || weaponId == 3651) {
            speed *= 0.60;
        }

        if (weaponId == 933 || weaponId == 12426 || weaponId == 20934 || weaponId == 1666 || weaponId == 5171
                || weaponId == 18911 || weaponId == 14581 || weaponId == 19120 || weaponId == 6197 || weaponId == 19055
                || weaponId == 18912 || weaponId == 8655 || weaponId == 8656 || weaponId == 19906 || weaponId == 14027
                || weaponId == 5219 || weaponId == 5231 || weaponId == 3279 || weaponId == 5150 || weaponId == 18493
                || weaponId == 13289 || weaponId == 3311 || weaponId == 6930 || weaponId == 13265 || weaponId == 16450
                || weaponId == 1413 || weaponId == 13094 || weaponId == 13201 || weaponId == 16524 || weaponId == 8654
                || weaponId == 7682 || weaponId == 19154 || weaponId == 4796 || weaponId == 20927 || weaponId == 13207
                || weaponId == 18887 || weaponId == 20943 || weaponId == 12238 || weaponId == 2760 || weaponId == 17926
                || weaponId == 3641) {
            speed *= 0.66;
        }

        if (weaponId == 8488) {
            speed *= 0.50;
        }

        if (weaponId == 5214) {
            speed *= 0.65;
        }

        if (weaponId == 9492) {
            speed *= 0.05;
        }
        if (weaponId == 4059) {
            speed *= 0.30;
        }
        if (weaponId == 5115) {
            speed *= 0.20;
        }
        if (weaponId == 15010) {
            speed *= 0.20;
        }
        if (weaponId == 932) {
            speed *= 0.30;
        }
        if (weaponId == 5194 || weaponId == 3990 || weaponId == 5125 || weaponId == 17849 || weaponId == 16540) {
            speed *= 0.70;
        }

        if (weaponId == 19163) {
            speed *= 0.70;
        }

        if (weaponId == 14923) {
            speed *= 0.80;
        }

        if (weaponId == 5089) {
            speed *= 1.25;
        }
        if (weaponId == 3641) {
            speed *= 1.20;
        }

        if (weaponId == 18957 || weaponId == 5195 || weaponId == 3971 || weaponId == 3077 || weaponId == 8001
                || weaponId == 3290 || weaponId == 5249 || weaponId == 10905 || weaponId == 80 || weaponId == 7764
                || weaponId == 20427 || weaponId == 14028 || weaponId == 16436 || weaponId == 16476) {
            speed *= 0.85;
        }

        if (weaponId == 14921) {
            speed *= 0.30;
        }

        if (weaponId == 18965) {
            speed *= 0.85;
        }

        if (weaponId == 4802) {
            speed -= 1;
        }
        if (weaponId == 18865 || weaponId == 13260 || weaponId == 5173 || weaponId == 18683 || weaponId == 3941
                || weaponId == 14559 || weaponId == 18683 || weaponId == 18985 || weaponId == 5131 || weaponId == 5174
                || weaponId == 3928) {
            speed -= 1;
        }
        if (fightType == FightType.CROSSBOW_RAPID || fightType == FightType.LONGBOW_RAPID
                || weaponId == 6522 && fightType == FightType.KNIFE_RAPID || weapon.contains("rapier")) {
            if (weaponId != 11235) {
                speed--;
            }

        } else if (weaponId != 6522 && weaponId != 15241
                && (fightType == FightType.SHORTBOW_RAPID || fightType == FightType.DART_RAPID
                || fightType == FightType.KNIFE_RAPID || fightType == FightType.THROWNAXE_RAPID
                || fightType == FightType.JAVELIN_RAPID)
                || weaponId == 11730 || weaponId == 3083 || weaponId == 3082) {
            speed -= 2;
        }
        return speed;
        // return DesolaceFormulas.getAttackDelay(this);
    }

    public Bank getBank() {
        return bank;
    }

    @Override
    public boolean isPlayer() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Player)) {
            return false;
        }

        Player p = (Player) o;
        return p.getIndex() == getIndex() || p.getUsername().equals(username);
    }

    @Override
    public int getSize() {
        return 1;
    }

    @Override
    public void poisonVictim(Character victim, CombatType type) {
        if (type == CombatType.MELEE || weapon == WeaponInterface.DART || weapon == WeaponInterface.KNIFE
                || weapon == WeaponInterface.THROWNAXE || weapon == WeaponInterface.JAVELIN) {
            CombatFactory.poisonEntity(victim, CombatPoisonData.getPoisonType(equipment.get(Equipment.WEAPON_SLOT)));
        } else if (type == CombatType.RANGED) {
            CombatFactory.poisonEntity(victim,
                    CombatPoisonData.getPoisonType(equipment.get(Equipment.AMMUNITION_SLOT)));
        }
    }

    @Override
    public CombatStrategy determineStrategy() {
        if (specialActivated && castSpell == null) {

            if (combatSpecial.getCombatType() == CombatType.MELEE) {
                return CombatStrategies.getDefaultMeleeStrategy();
            } else if (combatSpecial.getCombatType() == CombatType.RANGED) {
                setRangedWeaponData(RangedWeaponData.getData(this));
                return CombatStrategies.getDefaultRangedStrategy();
            } else if (combatSpecial.getCombatType() == CombatType.MAGIC) {
                return CombatStrategies.getDefaultMagicStrategy();
            }
        }
        if (CustomMagicStaff.checkCustomStaff(this)) {
            CustomMagicStaff.handleCustomStaff(this);
            this.setCastSpell(CustomMagicStaff.CustomStaff
                    .getSpellForWeapon(this.getEquipment().getItems()[Equipment.WEAPON_SLOT].getId()));
            return CombatStrategies.getDefaultMagicStrategy();
        }

        if (castSpell != null || autocastSpell != null
                || this.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 11605
                || this.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 11609
                || this.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 3951
                || this.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 5129
                || this.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 13995
                || this.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 14922
                || this.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 15653
                || this.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 19727
                || this.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 8474
                || this.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 3279
                || this.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 6483
                || this.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 8664
                || this.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 19720
                || this.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 16518
                || this.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 5179
                || this.getEquipment().get(Equipment.WEAPON_SLOT).getId() == 3911) {
            return CombatStrategies.getDefaultMagicStrategy();
        }

        if (castSpell != null || autocastSpell != null) {
            return CombatStrategies.getDefaultMagicStrategy();
        }

        RangedWeaponData data = RangedWeaponData.getData(this);
        if (data != null) {
            setRangedWeaponData(data);
            return CombatStrategies.getDefaultRangedStrategy();
        }

        return CombatStrategies.getDefaultMeleeStrategy();
    }

    public void process() {
        process.sequence();
    }

    public void dispose() {
        save();
        packetSender.sendLogout();
    }

    public void save() {
        if (session.getState() != SessionState.LOGGED_IN && session.getState() != SessionState.LOGGING_OUT) {
            return;
        }
        PlayerSaving.save(this);
    }

    public boolean logout(boolean forced) {

        if (getInstanceInterface().getNpcToSpawn() != null) {
            getInstanceInterface().onFinish();
        }

        ClanChatManager.leave(this, false);

        boolean debugMessage = false;

        if (this.getSummoning().getFamiliar() != null) {
            BossPets.pickup(this, this.getSummoning().getFamiliar().getSummonNpc());
        }
        int[] playerXP = new int[Skill.values().length];
        for (int i = 0; i < Skill.values().length; i++) {
            playerXP[i] = this.getSkillManager().getExperience(Skill.forId(i));
        }
        com.everythingrs.hiscores.Hiscores.update(
                "A0fRA3GG2v0we8yCm0tnWcULR3LojfB7TkFyki38BrG6dAGzTAj1hthJ9DlksWJWGJs6Fjar", "Normal Mode",
                this.getUsername(), this.getRights().getIcon(), playerXP, debugMessage);
        if (gameMode != GameMode.HARDCORE_IRONMAN && gameMode != GameMode.IRONMAN && getRights() != PlayerRights.OWNER
                && getRights() != PlayerRights.DEVELOPER)
            com.everythingrs.hiscores.Hiscores.update(
                    "A0fRA3GG2v0we8yCm0tnWcULR3LojfB7TkFyki38BrG6dAGzTAj1hthJ9DlksWJWGJs6Fjar", "Normal Mode",
                    this.getUsername(), this.getRights().getIcon(), playerXP, debugMessage);
        if (gameMode == GameMode.IRONMAN && gameMode != GameMode.HARDCORE_IRONMAN && gameMode != GameMode.NORMAL
                && getRights() != PlayerRights.OWNER && getRights() != PlayerRights.DEVELOPER)
            com.everythingrs.hiscores.Hiscores.update(
                    "A0fRA3GG2v0we8yCm0tnWcULR3LojfB7TkFyki38BrG6dAGzTAj1hthJ9DlksWJWGJs6Fjar", "Ironman",
                    this.getUsername(), this.getRights().getIcon(), playerXP, debugMessage);
        if (gameMode == GameMode.HARDCORE_IRONMAN && gameMode != GameMode.IRONMAN && gameMode != GameMode.NORMAL
                && getRights() != PlayerRights.OWNER && getRights() != PlayerRights.DEVELOPER)
            com.everythingrs.hiscores.Hiscores.update(
                    "A0fRA3GG2v0we8yCm0tnWcULR3LojfB7TkFyki38BrG6dAGzTAj1hthJ9DlksWJWGJs6Fjar", "Hardcore Ironman",
                    this.getUsername(), this.getRights().getIcon(), playerXP, debugMessage);
        if (forced) {
            if (getAoEInstance() != null) {
                moveTo(GameSettings.DEFAULT_POSITION);
            }
            return true;
        }
        if (getCombatBuilder().isBeingAttacked()) {
            getPacketSender().sendMessage("You must wait a few seconds after being out of combat before doing this.");
            return false;
        }
        if (getGambling().inGamble()) {
            sendMessage("You are currently busy.");
            return false;
        }
        if (getConstitution() <= 0 || isDying || settingUpCannon || crossingObstacle) {
            getPacketSender().sendMessage("You cannot log out at the moment.");
            return false;
        }

        if (getAoEInstance() != null) {
            moveTo(GameSettings.DEFAULT_POSITION);
        }

        if (!isMiniPlayer()) {
            getMinimeSystem().onLogout();
        }

        return true;
    }

    public boolean isJailed() {
        return jailed;
    }

    public void setJailed(boolean jailed) {
        this.jailed = jailed;
    }

    public void restart() {
        setFreezeDelay(0);
        setOverloadPotionTimer(0);
        setPrayerRenewalPotionTimer(0);
        setSpecialPercentage(100);
        setSpecialActivated(false);
        CombatSpecial.updateBar(this);
        setHasVengeance(false);
        setSkullTimer(0);
        setSkullIcon(0);
        setTeleblockTimer(0);
        setPoisonDamage(0);
        setStaffOfLightEffect(0);
        performAnimation(new Animation(65535));
        WeaponInterfaces.assign(this, getEquipment().get(Equipment.WEAPON_SLOT));
        WeaponAnimations.assign(this, getEquipment().get(Equipment.WEAPON_SLOT));
        PrayerHandler.deactivateAll(this);
        CurseHandler.deactivateAll(this);
        getEquipment().refreshItems();
        getInventory().refreshItems();
        for (Skill skill : Skill.values()) {
            getSkillManager().setCurrentLevel(skill, getSkillManager().getMaxLevel(skill));
        }
        setRunEnergy(100);
        setDying(false);
        getMovementQueue().setLockMovement(false).reset();
        getUpdateFlag().flag(Flag.APPEARANCE);
    }

    public boolean busy() {
        return interfaceId > 0 || isBanking || shopping || trading.inTrade() || dueling.inDuelScreen || isResting;
    }

    public GamblingInterface getGambling() {
        return gambling;
    }

    public Position getPreviousPosition() {
        return previousPosition;
    }

    public void setPreviousPosition(Position previousPosition) {
        this.previousPosition = previousPosition;
    }

    public MysteryBox getMysteryBox() {
        return mysteryBox;
    }

    /*
     * Getters & Setters
     */
    public PlayerSession getSession() {
        return session;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public Inventory getInv(Item item, Position pos, String string, boolean b, int i, boolean c, int j) {
        return inventory;
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public PriceChecker getPriceChecker() {
        return priceChecker;
    }

    /*
     * Getters and setters
     */
    public String getUsername() {
        return username;
    }

    public Player setUsername(String username) {
        this.username = username;
        return this;
    }

    public Long getLongUsername() {
        return longUsername;
    }

    public Player setLongUsername(Long longUsername) {
        this.longUsername = longUsername;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public Player setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getEmailAddress() {
        return this.emailAddress;
    }

    public void setEmailAddress(String address) {
        this.emailAddress = address;
    }

    public String getHostAddress() {
        return hostAddress;
    }

    public Player setHostAddress(String hostAddress) {
        this.hostAddress = hostAddress;
        return this;
    }

    public String getSerialNumber() {
        return serial_number;
    }

    public Player setSerialNumber(String serial_number) {
        this.serial_number = serial_number;
        return this;
    }

    public FrameUpdater getFrameUpdater() {
        return this.frameUpdater;
    }

    public PlayerRights getRights() {
        return rights;
    }

    public Player setRights(PlayerRights rights) {
        this.rights = rights;
        return this;
    }

    public ChatMessage getChatMessages() {
        return chatMessages;
    }

    public PacketSender getPacketSender() {
        return packetSender;
    }

    public WheelOfFortune getWheelOfFortune() {
        return wheelOfFortune;
    }

    public SkillManager getSkillManager() {
        return skillManager;
    }

    public Appearance getAppearance() {
        return appearance;
    }

    public PlayerRelations getRelations() {
        return relations;
    }

    public PlayerKillingAttributes getPlayerKillingAttributes() {
        return playerKillingAttributes;
    }

    public PointsHandler getPointsHandler() {
        return pointsHandler;
    }

    public boolean isImmuneToDragonFire() {
        return dragonFireImmunity > 0;
    }

    public int getDragonFireImmunity() {
        return dragonFireImmunity;
    }

    public void setDragonFireImmunity(int dragonFireImmunity) {
        this.dragonFireImmunity = dragonFireImmunity;
    }

    public void incrementDragonFireImmunity(int amount) {
        dragonFireImmunity += amount;
    }

    public void decrementDragonFireImmunity(int amount) {
        dragonFireImmunity -= amount;
    }

    public int getPoisonImmunity() {
        return poisonImmunity;
    }

    public void setPoisonImmunity(int poisonImmunity) {
        this.poisonImmunity = poisonImmunity;
    }

    public void incrementPoisonImmunity(int amount) {
        poisonImmunity += amount;
    }

    public void decrementPoisonImmunity(int amount) {
        poisonImmunity -= amount;
    }

    public boolean isAutoRetaliate() {
        return autoRetaliate;
    }

    public void setAutoRetaliate(boolean autoRetaliate) {
        this.autoRetaliate = autoRetaliate;
    }

    /**
     * @return the castSpell
     */
    public CombatSpell getCastSpell() {
        return castSpell;
    }

    /**
     * @param castSpell the castSpell to set
     */
    public void setCastSpell(CombatSpell castSpell) {
        this.castSpell = castSpell;
    }

    public CombatSpell getPreviousCastSpell() {
        return previousCastSpell;
    }

    public void setPreviousCastSpell(CombatSpell previousCastSpell) {
        this.previousCastSpell = previousCastSpell;
    }

    /**
     * @return the autocast
     */
    public boolean isAutocast() {
        return autocast;
    }

    /**
     * @param autocast the autocast to set
     */
    public void setAutocast(boolean autocast) {
        this.autocast = autocast;
    }

    /**
     * @return the skullTimer
     */
    public int getSkullTimer() {
        return skullTimer;
    }

    /**
     * @param skullTimer the skullTimer to set
     */
    public void setSkullTimer(int skullTimer) {
        this.skullTimer = skullTimer;
    }

    public void decrementSkullTimer() {
        skullTimer -= 50;
    }

    /**
     * @return the skullIcon
     */
    public int getSkullIcon() {
        return skullIcon;
    }

    /**
     * @param skullIcon the skullIcon to set
     */
    public void setSkullIcon(int skullIcon) {
        this.skullIcon = skullIcon;
    }

    /**
     * @return the teleblockTimer
     */
    public int getTeleblockTimer() {
        return teleblockTimer;
    }

    /**
     * @param teleblockTimer the teleblockTimer to set
     */
    public void setTeleblockTimer(int teleblockTimer) {
        this.teleblockTimer = teleblockTimer;
    }

    public void decrementTeleblockTimer() {
        teleblockTimer--;
    }

    /**
     * @return the autocastSpell
     */
    public CombatSpell getAutocastSpell() {
        return autocastSpell;
    }

    /**
     * @param autocastSpell the autocastSpell to set
     */
    public void setAutocastSpell(CombatSpell autocastSpell) {
        this.autocastSpell = autocastSpell;
    }

    /**
     * @return the specialPercentage
     */
    public int getSpecialPercentage() {
        return specialPercentage;
    }

    /**
     * @param specialPercentage the specialPercentage to set
     */
    public void setSpecialPercentage(int specialPercentage) {
        this.specialPercentage = specialPercentage;
    }

    /**
     * @return the fireAmmo
     */
    public int getFireAmmo() {
        return fireAmmo;
    }

    /**
     * @param fireAmmo the fireAmmo to set
     */
    public void setFireAmmo(int fireAmmo) {
        this.fireAmmo = fireAmmo;
    }

    public int getWildernessLevel() {
        return wildernessLevel;
    }

    public void setWildernessLevel(int wildernessLevel) {
        this.wildernessLevel = wildernessLevel;
    }

    /**
     * @return the combatSpecial
     */
    public CombatSpecial getCombatSpecial() {
        return combatSpecial;
    }

    /**
     * @param combatSpecial the combatSpecial to set
     */
    public void setCombatSpecial(CombatSpecial combatSpecial) {
        this.combatSpecial = combatSpecial;
    }

    /**
     * @return the specialActivated
     */
    public boolean isSpecialActivated() {
        return specialActivated;
    }

    /**
     * @param specialActivated the specialActivated to set
     */
    public void setSpecialActivated(boolean specialActivated) {
        this.specialActivated = specialActivated;
    }

    public void decrementSpecialPercentage(int drainAmount) {
        this.specialPercentage -= drainAmount;

        if (specialPercentage < 0) {
            specialPercentage = 0;
        }
    }

    public void incrementSpecialPercentage(int gainAmount) {
        this.specialPercentage += gainAmount;

        if (specialPercentage > 100) {
            specialPercentage = 100;
        }
    }

    /**
     * @return the rangedAmmo
     */
    public RangedWeaponData getRangedWeaponData() {
        return rangedWeaponData;
    }

    public void setRangedWeaponData(RangedWeaponData rangedWeaponData) {
        this.rangedWeaponData = rangedWeaponData;
    }

    /**
     * @return the weapon.
     */
    public WeaponInterface getWeapon() {
        return weapon;
    }

    /**
     * @param weapon the weapon to set.
     */
    public void setWeapon(WeaponInterface weapon) {
        this.weapon = weapon;
    }

    public void resetInterfaces() {
        walkableInterfaceList.stream().filter((i) -> !(i == 41005 || i == 41000)).forEach((i) -> {
            getPacketSender().sendWalkableInterface(i, false);
        });

        walkableInterfaceList.clear();
    }

    public void sendParallellInterfaceVisibility(int interfaceId, boolean visible) {
        if (this != null && this.getPacketSender() != null) {
            if (visible) {
                if (walkableInterfaceList.contains(interfaceId)) {
                    return;
                } else {
                    walkableInterfaceList.add(interfaceId);
                }
            } else {
                if (!walkableInterfaceList.contains(interfaceId)) {
                    return;
                } else {
                    walkableInterfaceList.remove((Object) interfaceId);
                }
            }

            getPacketSender().sendWalkableInterface(interfaceId, visible);
        }
    }

    /**
     * @return the fightType
     */
    public FightType getFightType() {
        return fightType;
    }

    /**
     * @param fightType the fightType to set
     */
    public void setFightType(FightType fightType) {
        this.fightType = fightType;
    }

    public Bank[] getBanks() {
        return bankTabs;
    }

    public Bank getBank(int index) {
        return bankTabs[index];
    }

    public Player setBank(int index, Bank bank) {
        this.bankTabs[index] = bank;
        return this;
    }

    public boolean isAcceptAid() {
        return acceptingAid;
    }

    public void setAcceptAid(boolean acceptingAid) {
        this.acceptingAid = acceptingAid;
    }

    public Trading getTrading() {
        return trading;
    }

    public Dueling getDueling() {
        return dueling;
    }

    public CopyOnWriteArrayList<KillsEntry> getKillsTracker() {
        return killsTracker;
    }

    public CopyOnWriteArrayList<DropLogEntry> getDropLog() {
        return dropLog;
    }

    public WalkToTask getWalkToTask() {
        return walkToTask;
    }

    public void setWalkToTask(WalkToTask walkToTask) {
        this.walkToTask = walkToTask;
    }

    public MagicSpellbook getSpellbook() {
        return spellbook;
    }

    public Player setSpellbook(MagicSpellbook spellbook) {
        this.spellbook = spellbook;
        return this;
    }

    public Prayerbook getPrayerbook() {
        return prayerbook;
    }

    public Player setPrayerbook(Prayerbook prayerbook) {
        this.prayerbook = prayerbook;
        return this;
    }

    /**
     * The player's local players list.
     */
    public List<Player> getLocalPlayers() {
        return localPlayers;
    }

    /**
     * The player's local npcs list getter
     */
    public List<NPC> getLocalNpcs() {
        return localNpcs;
    }

    public int getInterfaceId() {
        return this.interfaceId;
    }

    public Player setInterfaceId(int interfaceId) {
        this.interfaceId = interfaceId;
        return this;
    }

    public boolean isDying() {
        return isDying;
    }

    public void setDying(boolean isDying) {
        this.isDying = isDying;
    }

    public boolean isLegendary() {
        return isLegendary;
    }

    public void setLegendary(boolean isLegendary) {
        this.isLegendary = isLegendary;
    }

    public boolean isMbox1() {
        return isMbox1;
    }

    public void setMbox1(boolean isMbox1) {
        this.isMbox1 = isMbox1;
    }

    public boolean isMbox2() {
        return isMbox2;
    }

    public void setMbox2(boolean isMbox2) {
        this.isMbox2 = isMbox2;
    }

    // public ArrayList<Integer> completedStarterTaskAttributes = new
    // ArrayList<Integer>();

    // public ArrayList<Integer> getCompletedStarterList() {
    // return completedStarterTaskAttributes;
    // }

    // public void setCompletedStarterList(ArrayList<Integer>
    // completedStarterTaskAttributes) {
    // this.completedStarterTaskAttributes = completedStarterTaskAttributes;
    // }

    public boolean isMbox3() {
        return isMbox3;
    }

    public void setMbox3(boolean isMbox3) {
        this.isMbox3 = isMbox3;
    }

    public boolean isMbox4() {
        return isMbox4;
    }

    public void setMbox4(boolean isMbox4) {
        this.isMbox4 = isMbox4;
    }

    public boolean isMbox5() {
        return isMbox5;
    }

    public void setMbox5(boolean isMbox5) {
        this.isMbox5 = isMbox5;
    }

    public int[] getForceMovement() {
        return forceMovement;
    }

    public Player setForceMovement(int[] forceMovement) {
        this.forceMovement = forceMovement;
        return this;
    }

    /**
     * @return the equipmentAnimation
     */
    public CharacterAnimations getCharacterAnimations() {
        return characterAnimations;
    }

    /**
     * @return the equipmentAnimation
     */
    public void setCharacterAnimations(CharacterAnimations equipmentAnimation) {
        this.characterAnimations = equipmentAnimation.clone();
    }

    public LoyaltyTitles getLoyaltyTitle() {
        return loyaltyTitle;
    }

    public void setLoyaltyTitle(LoyaltyTitles loyaltyTitle) {
        this.loyaltyTitle = loyaltyTitle;
    }

    public PlayerInteractingOption getPlayerInteractingOption() {
        return playerInteractingOption;
    }

    public Player setPlayerInteractingOption(PlayerInteractingOption playerInteractingOption) {
        this.playerInteractingOption = playerInteractingOption;
        return this;
    }

    public int getMultiIcon() {
        return multiIcon;
    }

    public Player setMultiIcon(int multiIcon) {
        this.multiIcon = multiIcon;
        return this;
    }

    public int getWalkableInterfaceId() {
        return walkableInterfaceId;
    }

    public void setWalkableInterfaceId(int interfaceId2) {
        this.walkableInterfaceId = interfaceId2;
    }

    public boolean soundsActive() {
        return soundsActive;
    }

    public void setSoundsActive(boolean soundsActive) {
        this.soundsActive = soundsActive;
    }

    public boolean musicActive() {
        return musicActive;
    }

    public void setMusicActive(boolean musicActive) {
        this.musicActive = musicActive;
    }

    public BonusManager getBonusManager() {
        return bonusManager;
    }

    public int getRunEnergy() {
        return runEnergy;
    }

    public Player setRunEnergy(int runEnergy) {
        this.runEnergy = runEnergy;
        return this;
    }

    public Stopwatch getLastRunRecovery() {
        return lastRunRecovery;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public Player setRunning(boolean isRunning) {
        this.isRunning = isRunning;
        return this;
    }

    public boolean isResting() {
        return isResting;
    }

    public Player setResting(boolean isResting) {
        this.isResting = isResting;
        return this;
    }

    public long getMoneyInPouch() {
        return moneyInPouch;
    }

    public void setMoneyInPouch(long moneyInPouch) {
        this.moneyInPouch = moneyInPouch;
    }

    public int getMoneyInPouchAsInt() {
        return moneyInPouch > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) moneyInPouch;
    }

    public boolean experienceLocked() {
        return experienceLocked;
    }

    public boolean bloodFountain() {
        return bloodFountain;
    }

    public boolean dreamZone() {
        return dreamZone;
    }

    public void setExperienceLocked(boolean experienceLocked) {
        this.experienceLocked = experienceLocked;
    }

    public void setDreamZone(boolean dreamZone) {
        this.dreamZone = dreamZone;
    }

    public boolean isClientExitTaskActive() {
        return clientExitTaskActive;
    }

    public void setClientExitTaskActive(boolean clientExitTaskActive) {
        this.clientExitTaskActive = clientExitTaskActive;
    }

    public ClanChat getCurrentClanChat() {
        return currentClanChat;
    }

    public Player setCurrentClanChat(ClanChat clanChat) {
        this.currentClanChat = clanChat;
        return this;
    }

    public String getClanChatName() {
        return clanChatName;
    }

    public Player setClanChatName(String clanChatName) {
        this.clanChatName = clanChatName;
        return this;
    }

    public Input getInputHandling() {
        return inputHandling;
    }

    public void setInputHandling(Input inputHandling) {
        this.inputHandling = inputHandling;
    }

    public boolean isDrainingPrayer() {
        return drainingPrayer;
    }

    public void setDrainingPrayer(boolean drainingPrayer) {
        this.drainingPrayer = drainingPrayer;
    }

    public Stopwatch getClickDelay() {
        return clickDelay;
    }

    public Stopwatch getDoubleDrops() {
        return doubleDrops;
    }

    public int[] getLeechedBonuses() {
        return leechedBonuses;
    }

    public Stopwatch getLastItemPickup() {
        return lastItemPickup;
    }

    public Stopwatch getLastSummon() {
        return lastSummon;
    }

    public BankSearchAttributes getBankSearchingAttribtues() {
        return bankSearchAttributes;
    }

    public AchievementAttributes getAchievementAttributes() {
        return achievementAttributes;
    }

    public StarterTaskAttributes getStarterTaskAttributes() {
        return starterTaskAttributes;
    }

    public BankPinAttributes getBankPinAttributes() {
        return bankPinAttributes;
    }

    public int getCurrentBankTab() {
        return currentBankTab;
    }

    public Player setCurrentBankTab(int tab) {
        this.currentBankTab = tab;
        return this;
    }

    public boolean isBanking() {
        return isBanking;
    }

    public Player setBanking(boolean isBanking) {
        this.isBanking = isBanking;
        return this;
    }

    public void setNoteWithdrawal(boolean noteWithdrawal) {
        this.noteWithdrawal = noteWithdrawal;
    }

    public boolean withdrawAsNote() {
        return noteWithdrawal;
    }

    public void setSwapMode(boolean swapMode) {
        this.swapMode = swapMode;
    }

    public boolean swapMode() {
        return swapMode;
    }

    public boolean isShopping() {
        return shopping;
    }

    public void setShopping(boolean shopping) {
        this.shopping = shopping;
    }

    public Shop getShop() {
        return shop;
    }

    public Player setShop(Shop shop) {
        this.shop = shop;
        return this;
    }

    public GameObject getInteractingObject() {
        return interactingObject;
    }

    public Player setInteractingObject(GameObject interactingObject) {
        this.interactingObject = interactingObject;
        return this;
    }

    public Item getInteractingItem() {
        return interactingItem;
    }

    public void setInteractingItem(Item interactingItem) {
        this.interactingItem = interactingItem;
    }

    public Dialogue getDialogue() {
        return this.dialogue;
    }

    public void setDialogue(Dialogue dialogue) {
        this.dialogue = dialogue;
    }

    public int getDialogueActionId() {
        return dialogueActionId;
    }

    public void setDialogueActionId(int dialogueActionId) {
        this.dialogueActionId = dialogueActionId;
    }

    public boolean isSettingUpCannon() {
        return settingUpCannon;
    }

    public void setSettingUpCannon(boolean settingUpCannon) {
        this.settingUpCannon = settingUpCannon;
    }

    public DwarfCannon getCannon() {
        return cannon;
    }

    public Player setCannon(DwarfCannon cannon) {
        this.cannon = cannon;
        return this;
    }

    public int getOverloadPotionTimer() {
        return overloadPotionTimer;
    }

    public void setOverloadPotionTimer(int overloadPotionTimer) {
        this.overloadPotionTimer = overloadPotionTimer;
    }

    public int getPrayerRenewalPotionTimer() {
        return prayerRenewalPotionTimer;
    }

    public void setPrayerRenewalPotionTimer(int prayerRenewalPotionTimer) {
        this.prayerRenewalPotionTimer = prayerRenewalPotionTimer;
    }

    public Stopwatch getSpecialRestoreTimer() {
        return specialRestoreTimer;
    }

    public boolean[] getUnlockedLoyaltyTitles() {
        return unlockedLoyaltyTitles;
    }

    public void setUnlockedLoyaltyTitles(boolean[] unlockedLoyaltyTitles) {
        this.unlockedLoyaltyTitles = unlockedLoyaltyTitles;
    }

    public void setUnlockedLoyaltyTitle(int index) {
        unlockedLoyaltyTitles[index] = true;
    }

    public Stopwatch getEmoteDelay() {
        return emoteDelay;
    }

    public MinigameAttributes getMinigameAttributes() {
        return minigameAttributes;
    }

    public Minigame getMinigame() {
        return minigame;
    }

    public void setMinigame(Minigame minigame) {
        this.minigame = minigame;
    }

    public int getFireImmunity() {
        return fireImmunity;
    }

    public Player setFireImmunity(int fireImmunity) {
        this.fireImmunity = fireImmunity;
        return this;
    }

    public int getFireDamageModifier() {
        return fireDamageModifier;
    }

    public Player setFireDamageModifier(int fireDamageModifier) {
        this.fireDamageModifier = fireDamageModifier;
        return this;
    }

    public boolean hasVengeance() {
        return hasVengeance;
    }

    public void setHasVengeance(boolean hasVengeance) {
        this.hasVengeance = hasVengeance;
    }

    public Stopwatch getLastVengeance() {
        return lastVengeance;
    }

    /*
     * Construction instancing Arlania
     */
    public boolean isVisible() {
        if (getLocation() == Locations.Location.CONSTRUCTION) {
            return false;
        }
        return true;
    }

    public void setHouseFurtinture(ArrayList<HouseFurniture> houseFurniture) {
        this.houseFurniture = houseFurniture;
    }

    public Stopwatch getTolerance() {
        return tolerance;
    }

    public boolean isTargeted() {
        return targeted;
    }

    public void setTargeted(boolean targeted) {
        this.targeted = targeted;
    }

    public Stopwatch getLastYell() {
        return lastYell;
    }

    public Stopwatch getLastZulrah() {
        return lastZulrah;
    }

    public Stopwatch getLastSql() {
        return lastSql;
    }

    public int getAmountDonated() {
        return amountDonated;
    }

    public void incrementAmountDonated(int amountDonated) {
        this.amountDonated += amountDonated;
    }

    public long getTotalPlayTime() {
        return totalPlayTime;
    }

    public void setTotalPlayTime(long amount) {
        this.totalPlayTime = amount;
    }

    public void incrementTotalPlayTime(long amount) {
        this.totalPlayTime += amount;
        BattlePass.INSTANCE.awardPlayTimeExperience(this, amount);
    }

    public Stopwatch getRecordedLogin() {
        return recordedLogin;
    }

    public Player setRegionChange(boolean regionChange) {
        this.regionChange = regionChange;
        return this;
    }

    public boolean isChangingRegion() {
        return this.regionChange;
    }

    public boolean isAllowRegionChangePacket() {
        return allowRegionChangePacket;
    }

    public void setAllowRegionChangePacket(boolean allowRegionChangePacket) {
        this.allowRegionChangePacket = allowRegionChangePacket;
    }

    public boolean isKillsTrackerOpen() {
        return killsTrackerOpen;
    }

    public void setKillsTrackerOpen(boolean killsTrackerOpen) {
        this.killsTrackerOpen = killsTrackerOpen;
    }

    public boolean isCoughing() {
        return isCoughing;
    }

    public void setCoughing(boolean isCoughing) {
        this.isCoughing = isCoughing;
    }

    public int getShadowState() {
        return shadowState;
    }

    public void setShadowState(int shadow) {
        this.shadowState = shadow;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    public boolean isPlayerLocked() {
        return playerLocked;
    }

    public Player setPlayerLocked(boolean playerLocked) {
        this.playerLocked = playerLocked;
        return this;
    }

    public Stopwatch getSqlTimer() {
        return sqlTimer;
    }

    public Stopwatch getFoodTimer() {
        return foodTimer;
    }

    public Stopwatch getPotionTimer() {
        return potionTimer;
    }

    public Item getUntradeableDropItem() {
        return untradeableDropItem;
    }

    public void setUntradeableDropItem(Item untradeableDropItem) {
        this.untradeableDropItem = untradeableDropItem;
    }

    public boolean isRecoveringSpecialAttack() {
        return recoveringSpecialAttack;
    }

    public void setRecoveringSpecialAttack(boolean recoveringSpecialAttack) {
        this.recoveringSpecialAttack = recoveringSpecialAttack;
    }

    public CombatType getLastCombatType() {
        return lastCombatType;
    }

    public void setLastCombatType(CombatType lastCombatType) {
        this.lastCombatType = lastCombatType;
    }

    public int getEffigy() {
        return this.effigy;
    }

    public void setEffigy(int effigy) {
        this.effigy = effigy;
    }

    public int getDfsCharges() {
        return dfsCharges;
    }

    public void incrementDfsCharges(int amount) {
        this.dfsCharges += amount;
    }

    public void setNewPlayer(boolean newPlayer) {
        this.newPlayer = newPlayer;
    }

    public boolean newPlayer() {
        return newPlayer;
    }

    public Stopwatch getLogoutTimer() {
        return lougoutTimer;
    }

    public Player setUsableObject(int index, Object usableObject) {
        this.usableObject[index] = usableObject;
        return this;
    }

    public Object[] getUsableObject() {
        return usableObject;
    }

    public Player setUsableObject(Object[] usableObject) {
        this.usableObject = usableObject;
        return this;
    }

    public int getPlayerViewingIndex() {
        return playerViewingIndex;
    }

    public void setPlayerViewingIndex(int playerViewingIndex) {
        this.playerViewingIndex = playerViewingIndex;
    }

    public boolean hasStaffOfLightEffect() {
        return staffOfLightEffect > 0;
    }

    public int getStaffOfLightEffect() {
        return staffOfLightEffect;
    }

    public void setStaffOfLightEffect(int staffOfLightEffect) {
        this.staffOfLightEffect = staffOfLightEffect;
    }

    public void decrementStaffOfLightEffect() {
        this.staffOfLightEffect--;
    }

    public boolean openBank() {
        return openBank;
    }

    public void setOpenBank(boolean openBank) {
        this.openBank = openBank;
    }

    public int getMinutesBonusExp() {
        return minutesBonusExp;
    }

    public void setMinutesBonusExp(int minutesBonusExp, boolean add) {
        this.minutesBonusExp = (add ? this.minutesBonusExp + minutesBonusExp : minutesBonusExp);
    }

    public void setInactive(boolean inActive) {
        this.inActive = inActive;
    }

    public boolean isInActive() {
        return inActive;
    }

    public int getSelectedGeItem() {
        return selectedGeItem;
    }

    public void setSelectedGeItem(int selectedGeItem) {
        this.selectedGeItem = selectedGeItem;
    }

    public int getGeQuantity() {
        return geQuantity;
    }

    public void setGeQuantity(int geQuantity) {
        this.geQuantity = geQuantity;
    }

    public int getGePricePerItem() {
        return gePricePerItem;
    }

    public void setGePricePerItem(int gePricePerItem) {
        this.gePricePerItem = gePricePerItem;
    }

    public GrandExchangeSlot[] getGrandExchangeSlots() {
        return grandExchangeSlots;
    }

    public void setGrandExchangeSlots(GrandExchangeSlot[] GrandExchangeSlots) {
        this.grandExchangeSlots = GrandExchangeSlots;
    }

    public void setGrandExchangeSlot(int index, GrandExchangeSlot state) {
        this.grandExchangeSlots[index] = state;
    }

    public int getSelectedGeSlot() {
        return selectedGeSlot;
    }

    public void setSelectedGeSlot(int slot) {
        this.selectedGeSlot = slot;
    }

    public Task getCurrentTask() {
        return currentTask;
    }

    public void setCurrentTask(Task currentTask) {
        this.currentTask = currentTask;
    }

    public int getSelectedSkillingItem() {
        return selectedSkillingItem;
    }

    public void setSelectedSkillingItem(int selectedItem) {
        this.selectedSkillingItem = selectedItem;
    }

    public boolean shouldProcessFarming() {
        return processFarming;
    }

    public void setProcessFarming(boolean processFarming) {
        this.processFarming = processFarming;
    }

    public Pouch getSelectedPouch() {
        return selectedPouch;
    }

    public void setSelectedPouch(Pouch selectedPouch) {
        this.selectedPouch = selectedPouch;
    }

    public int getCurrentBookPage() {
        return currentBookPage;
    }

    public void setCurrentBookPage(int currentBookPage) {
        this.currentBookPage = currentBookPage;
    }

    public int getStoredRuneEssence() {
        return storedRuneEssence;
    }

    public void setStoredRuneEssence(int storedRuneEssence) {
        this.storedRuneEssence = storedRuneEssence;
    }

    public int getStoredPureEssence() {
        return storedPureEssence;
    }

    public void setStoredPureEssence(int storedPureEssence) {
        this.storedPureEssence = storedPureEssence;
    }

    public int getTrapsLaid() {
        return trapsLaid;
    }

    public void setTrapsLaid(int trapsLaid) {
        this.trapsLaid = trapsLaid;
    }

    public boolean isCrossingObstacle() {
        return crossingObstacle;
    }

    public Player setCrossingObstacle(boolean crossingObstacle) {
        this.crossingObstacle = crossingObstacle;
        return this;
    }

    public boolean[] getCrossedObstacles() {
        return crossedObstacles;
    }

    public void setCrossedObstacles(boolean[] crossedObstacles) {
        this.crossedObstacles = crossedObstacles;
    }

    public boolean getCrossedObstacle(int i) {
        return crossedObstacles[i];
    }

    public Player setCrossedObstacle(int i, boolean completed) {
        crossedObstacles[i] = completed;
        return this;
    }

    public int getSkillAnimation() {
        return skillAnimation;
    }

    public Player setSkillAnimation(int animation) {
        this.skillAnimation = animation;
        return this;
    }

    public int[] getOres() {
        return ores;
    }

    public void setOres(int[] ores) {
        this.ores = ores;
    }

    public Position getResetPosition() {
        return resetPosition;
    }

    public void setResetPosition(Position resetPosition) {
        this.resetPosition = resetPosition;
    }

    public Slayer getSlayer() {
        return slayer;
    }

    public BloodSlayer getBloodSlayer() {
        return bloodslayer;
    }

    public Summoning getSummoning() {
        return summoning;
    }

    public Farming getFarming() {
        return farming;
    }

    public boolean inConstructionDungeon() {
        return inConstructionDungeon;
    }

    public void setInConstructionDungeon(boolean inConstructionDungeon) {
        this.inConstructionDungeon = inConstructionDungeon;
    }

    public int getHouseServant() {
        return houseServant;
    }

    public void setHouseServant(int houseServant) {
        this.houseServant = houseServant;
    }

    public HouseLocation getHouseLocation() {
        return houseLocation;
    }

    public void setHouseLocation(HouseLocation houseLocation) {
        this.houseLocation = houseLocation;
    }

    public HouseTheme getHouseTheme() {
        return houseTheme;
    }

    public void setHouseTheme(HouseTheme houseTheme) {
        this.houseTheme = houseTheme;
    }

    public int getHouseServantCharges() {
        return this.houseServantCharges;
    }

    public void setHouseServantCharges(int houseServantCharges) {
        this.houseServantCharges = houseServantCharges;
    }

    public void incrementHouseServantCharges() {
        this.houseServantCharges++;
    }

    public int getServantItemFetch() {
        return servantItemFetch;
    }

    public void setServantItemFetch(int servantItemFetch) {
        this.servantItemFetch = servantItemFetch;
    }

    public int getPortalSelected() {
        return portalSelected;
    }

    public void setPortalSelected(int portalSelected) {
        this.portalSelected = portalSelected;
    }

    public boolean isBuildingMode() {
        return this.isBuildingMode;
    }

    public void setIsBuildingMode(boolean isBuildingMode) {
        this.isBuildingMode = isBuildingMode;
    }

    public int[] getConstructionCoords() {
        return constructionCoords;
    }

    public void setConstructionCoords(int[] constructionCoords) {
        this.constructionCoords = constructionCoords;
    }

    public int getBuildFurnitureId() {
        return this.buildFurnitureId;
    }

    public void setBuildFuritureId(int buildFuritureId) {
        this.buildFurnitureId = buildFuritureId;
    }

    public int getBuildFurnitureX() {
        return this.buildFurnitureX;
    }

    public void setBuildFurnitureX(int buildFurnitureX) {
        this.buildFurnitureX = buildFurnitureX;
    }

    public int getBuildFurnitureY() {
        return this.buildFurnitureY;
    }

    public void setBuildFurnitureY(int buildFurnitureY) {
        this.buildFurnitureY = buildFurnitureY;
    }

    public int getCombatRingType() {
        return this.combatRingType;
    }

    public void setCombatRingType(int combatRingType) {
        this.combatRingType = combatRingType;
    }

    public Room[][][] getHouseRooms() {
        return houseRooms;
    }

    public void setHouseRooms(Room[][][] houseRooms) {
        this.houseRooms = houseRooms;
    }

    public ArrayList<Portal> getHousePortals() {
        return housePortals;
    }

    public void setHousePortals(ArrayList<Portal> housePortals) {
        this.housePortals = housePortals;
    }

    public ArrayList<HouseFurniture> getHouseFurniture() {
        return houseFurniture;
    }

    public int getConstructionInterface() {
        return this.constructionInterface;
    }

    public void setConstructionInterface(int constructionInterface) {
        this.constructionInterface = constructionInterface;
    }

    public int[] getBrawlerChargers() {
        return this.brawlerCharges;
    }

    public void setBrawlerCharges(int[] brawlerCharges) {
        this.brawlerCharges = brawlerCharges;
    }

    public int getRecoilCharges() {
        return this.recoilCharges;
    }

    public int setRecoilCharges(int recoilCharges) {
        return this.recoilCharges = recoilCharges;
    }

    public boolean voteMessageSent() {
        return this.voteMessageSent;
    }

    public void setVoteMessageSent(boolean voteMessageSent) {
        this.voteMessageSent = voteMessageSent;
    }

    public boolean didReceiveStarter() {
        return receivedStarter;
    }

    public void sendMessage(String string) {
        packetSender.sendMessage(string);
    }

    public void setReceivedStarter(boolean receivedStarter) {
        this.receivedStarter = receivedStarter;
    }

    public BlowpipeLoading getBlowpipeLoading() {
        return blowpipeLoading;
    }

    public boolean cloudsSpawned() {
        return areCloudsSpawned;
    }

    public void setCloudsSpawned(boolean cloudsSpawned) {
        this.areCloudsSpawned = cloudsSpawned;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isShopUpdated() {
        return shopUpdated;
    }

    public void setShopUpdated(boolean shopUpdated) {
        this.shopUpdated = shopUpdated;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void write(Packet packet) {
        // TODO Auto-generated method stub

    }

    public void datarsps(Player player, String username2) {
        // TODO Auto-generated method stub

    }

    public String getMacAddress() {
        // TODO Auto-generated method stub
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public CustomBossInstance getCustomBoss() {
        return customBossInstance;
    }

    public void setCustomBoss(CustomBossInstance customBossInstance) {
        this.customBossInstance = customBossInstance;
    }

    public Zulrah getZulrahEvent() {
        return zulrah;
    }

    public Sagittare getSagittareEvent() {
        return sagittare;
    }

    public long setBestZulrahTime(long bestZulrahTime) {
        return this.bestZulrahTime = bestZulrahTime;
    }

    public long getBestZulrahTime() {
        return bestZulrahTime;
    }

    public boolean isPlaceholders() {
        return placeholders;
    }

    public void setPlaceholders(boolean placeholders) {
        this.placeholders = placeholders;
    }

    public Position getSelectedPosition() {
        return selectedPos;
    }

    public void setSelectedPosition(Position selectedPos) {
        this.selectedPos = selectedPos;
    }

    public void handleKeyRates(Player killer, NPC npc) {

        if (npc == null || killer == null)
            return;

        KeyTypes keyData = get(npc.getId());

        if (keyData == null)
            return;

        if (npc.getId() == 1279)
            this.keyCount1++;
        else if (npc.getId() == 1290)
            this.keyCount2++;
        else if (npc.getId() == 742)
            this.keyCount3++;

        if (this.getKeyRoom() != null)
            this.getKeyRoom().count--;

        double percent = Math.random() * 100;

        if (percent <= keyData.dropChance) {
            killer.sendMessage("A " + keyData.itemName + " has been added to your inventory.");

            int necklaceId = killer.getEquipment().get(Equipment.AMULET_SLOT).getId();
            int freeSpaces = killer.getInventory().getFreeSlots();

            if (necklaceId == 19886 && freeSpaces > 0) {

                killer.getInventory().add(keyData.keyId, 1);
            } else {

                killer.sendMessage("A " + keyData.itemName + " has been dropped below the monster.");
                GroundItemManager.spawnGroundItem(killer, new GroundItem(new Item(keyData.keyId, 1), npc.getPosition(),
                        killer.getUsername(), false, 150, true, 200));
            }
        }
    }

    public KeyRoom getKeyRoom() {
        return keyRoom;
    }

    /*
	public void claimDonation(Player player, boolean fromLogin) {
		if (player.lastDonationClaim < System.currentTimeMillis())
	//	new Thread(new Donation(player)).start();
		else if (!fromLogin)
			player.sendMessage("You can only claim donations once every 30seconds!");
	}
*/
    public void setKeyRoom(KeyRoom keyRoom) {
        this.keyRoom = keyRoom;
    }

    /**
     *
     */
    public void endKeyRoom(boolean fromUpdate) {
        if (this.getKeyRoom() != null) {
            this.getKeyRoom().finishRoom(fromUpdate);
        }
    }

    public void endCustomBossRoom() {
        if (this.getCustomBoss() != null) {
            this.getCustomBoss().finishRoom();
        }
    }

    public String getLongDurationTimer(long ms) {
        ms = ms - System.currentTimeMillis();
        long minutes = (int) Math.ceil((ms / 1000) / 60);
        if (minutes >= 60) {
            long hours = minutes / 60;
            long remainder = minutes % 60;
            if (remainder == 0)
                return hours + " Hours";
            return hours + " Hours " + remainder + " Minutes";
        }
        return minutes + " Minutes";
    }

    public String getTimeRemaining(long totalTime) {
        /***
         * Only used for under 1hrs
         */
        return "(<col=258324>" + Misc.convertMsToTime(totalTime - System.currentTimeMillis()) + "</col>" + ")";
    }

    public void giveItem(int itemId, int itemAmount) {

        final ItemDefinition definition = ItemDefinition.forId(itemId);

        if (definition == null) {
            sendMessage("@red@[Error]: Could not find definition (" + itemId + "-" + itemAmount + ")");
            sendMessage("@red@Please take a screenshot and post it on the forums.");
            return;
        }

        final int occupiedSlots = definition.isNoted() || definition.isStackable() ? 1 : itemAmount;

        if (inventory.getFreeSlots() >= occupiedSlots) {
            inventory.add(itemId, itemAmount).refreshItems();
        } else if (bank.getFreeSlots() >= occupiedSlots) {
            boolean added = false;
            for (Bank bank : getBanks()) {
                if (!added && !Bank.isEmpty(bank)) {
                    bank.add(itemId, itemAmount).refreshItems();
                    added = true;
                }
            }
        } else {
            sendMessage("@red@[Error]: Could not give (" + itemId + "-" + itemAmount + ")");
            sendMessage("@red@Please take a screenshot and post it on the forums.");
            World.sendStaffMessage("@red@[Error]: Could not give (" + itemId + "-" + itemAmount + ") to " + username);
        }
    }

    public boolean busy1() {
        return interfaceId > 0 || isBanking || shopping || trading.inTrade() || dueling.inDuelScreen || isResting;
    }

    public ArrayList<RaidParty> getRaidPartyInvites() {
        return pendingRaidPartyInvites;
    }

    public void addPendingRaidParty(RaidParty party) {
        if (pendingRaidPartyInvites.contains(party))
            return;
        pendingRaidPartyInvites.add(party);
    }

    public void removeRaidPartyInvite(RaidParty party) {
        if (pendingRaidPartyInvites.contains(party)) {
            pendingRaidPartyInvites.remove(party);
        }
    }

    public RaidParty getRaidParty() {
        return raidParty;
    }

    public void setRaidParty(RaidParty raidParty) {
        this.raidParty = raidParty;
    }

    public Object getPlayers() {
        // TODO Auto-generated method stub
        return null;
    }

    public void updateMoneyPouch() {
        getPacketSender().sendString(8135, "" + getMoneyInPouch() + "");
    }

    public void addMoneyToPouch(long l) {
        moneyInPouch += l;
        this.updateMoneyPouch();
    }

    public boolean getGodPotionStatus() {
        // TODO Auto-generated method stub
        return false;
    }

    public void setGodPotionStatus(int i) {
        // TODO Auto-generated method stub

    }

    public void setGodPotionStatus(boolean b) {
        // TODO Auto-generated method stub

    }

    public void setBankHolder(Object object) {
        // TODO Auto-generated method stub

    }

    public boolean isBot() {
        // TODO Auto-generated method stub
        return false;
    }

    public int getNpcKills() {
        return npcKills;
    }

    public void setNpcKills(int npcKills) {
        this.npcKills = npcKills;
    }

    public int getItemToUpgrade() {
        return itemToUpgrade;
    }

    public void setItemToUpgrade(int itemId) {
        this.itemToUpgrade = itemId;
    }

    public void incrementNPCKills(int amount) {
        this.npcKills += amount;
    }

    public void setNPCKills(int NPCKills) {
        this.npcKills = NPCKills;
    }

    public void setForceLogout(boolean b) {
        World.deregister(null);
        // TODO Auto-generated method stub

    }

    public DailyReward getDailyReward() {
        return dailyReward;
    }

    public boolean getClaimedTodays() {
        return claimedTodays;
    }

    public void setClaimedTodays(boolean claimedTodays) {
        this.claimedTodays = claimedTodays;
    }

    public int getDailyClaimed() {
        return dailyClaimed;
    }

    public void setDailyClaimed(int daily) {
        this.dailyClaimed = daily;
    }

    public int getSlayerPoints() {
        // TODO Auto-generated method stub
        return 0;
    }

    public int getBloodSlayerPoints() {
        // TODO Auto-generated method stub
        return 0;
    }

    public void setslayerparty(Object object) {
        // TODO Auto-generated method stub

    }

    public Object getslayerparty() {
        // TODO Auto-generated method stub
        return null;
    }

    public ArrayList<Player> getslayerpartyInvites() {
        // TODO Auto-generated method stub
        return null;
    }

    public void removeslayerpartyInvite(slayerparty slayerparty) {
        // TODO Auto-generated method stub

    }

    public void addPendingslayerparty(Object getslayerparty) {
        // TODO Auto-generated method stub

    }

    public String getJokerKills() {
        // TODO Auto-generated method stub
        return null;
    }

    public Familiar getFamiliar() {
        // TODO Auto-generated method stub
        return null;
    }

    public int getDemonKills() {
        // TODO Auto-generated method stub
        return 0;
    }

    public int getTransform() {
        return transform;
    }

    public void setTransform(int npcId) {
        this.transform = npcId;
    }

    public int getTimer() {
        return timer;
    }

    public void setTimer(int time) {
        this.timer = time;
    }

    public int getBox() {
        return box;
    }

    public void setBox(int box1) {
        this.box = box1;
    }

    public MysteryBoxOpener getMysteryBoxOpener() {
        return mysteryBoxOpener;
    }

    public void setSuperPoints(int asInt) {
        // TODO Auto-generated method stub

    }

    public MysteryBoxViewerOwner getMysteryBoxViewerOwner() {
        return mysteryBoxViewerOwner;
    }

    public DPSOverlay getDpsOverlay() {
        return dpsOverlay;
    }

    public StartScreen getStartScreen() {
        return startScreen;
    }

    public void setStartScreen(StartScreen startScreen) {
        this.startScreen = startScreen;
    }

    public AchievementInterface getAchievementInterface() {
        return this.achievementInterface;
    }

    public void setAchievementInterface(AchievementInterface achievementInterface) {
        this.achievementInterface = achievementInterface;
    }

    public AchievementTracker getAchievementTracker() {
        return this.achievementTracker;
    }

    public ReferralHandler getRefferalHandler() {
        return refHandler;
    }

    public UpgradeData getCurrentUpgrade() {
        return currentUpgrade;
    }

    public void setCurrentUpgrade(UpgradeData currentUpgrade) {
        this.currentUpgrade = currentUpgrade;
    }

    public UpgradeType getUpgradeType() {
        return upgradeType;
    }

    public void setUpgradeType(UpgradeType upgradeType) {
        this.upgradeType = upgradeType;
    }

    public UpgradeHandler getUpgradeHandler() {
        return upgradeHandler;
    }

    public WellOfGoodwillHandler getWellOfGoodwillHandler() {
        return wellOfGoodwillHandler;
    }

    public GoodieBag getGoodieBag() {
        return goodieBag;
    }

    public OwnerGoodiebag getOwnerGB() {
        return oGB;
    }

    public PerkHandler getPerkHandler() {
        return perkHandler;
    }

    public boolean getSendDpsOverlay() {
        return sendDpsOverlay;
    }

    public void setSendDpsOverlay(boolean sendDpsOverlay) {
        this.sendDpsOverlay = sendDpsOverlay;
    }

    /**
     * Instance Manager variables.
     */

    public InstanceData data;
    public String currentInstanceNpcName;
    public int currentInstanceNpcId;
    public int currentInstanceAmount;
    public int lastInstanceNpc;

    public InstanceData getData() {
        return data;
    }

    public void setData(InstanceData data) {
        this.data = data;
    }

    public String getCurrentInstanceNpcName() {
        return currentInstanceNpcName;
    }

    public void setCurrentInstanceNpcName(String currentInstanceNpcName) {
        this.currentInstanceNpcName = currentInstanceNpcName;
    }

    public int getCurrentInstanceAmount() {
        return currentInstanceAmount;
    }

    public void setCurrentInstanceAmount(int currentInstanceAmount) {
        this.currentInstanceAmount = currentInstanceAmount;
    }

    public int getCurrentInstanceNpcId() {
        return currentInstanceNpcId;
    }

    public void setCurrentInstanceNpcId(int currentInstanceNpcId) {
        this.currentInstanceNpcId = currentInstanceNpcId;
    }

    private enum KeyTypes {

        ROOM_1("Beginner key", 1279, 2832, 100), ROOM_2("Decent key", 1290, 2834, 100),
        ROOM_3("Best Key", 742, 2836, 100);

        public String itemName;

        public String itemMessage;

        public int npcId, keyId;

        public double dropChance;

        KeyTypes(String itemName, int npcId, int keyId, double dropChance) {
            this.itemName = itemName;
            this.npcId = npcId;
            this.keyId = keyId;
            this.dropChance = dropChance;
        }

    }

    public static int getKillLevel() {
        return killLevel;
    }

    public void setKillLevel(int killLevel) {
        this.killLevel = killLevel;
    }

    public void addKillLevel() {
        this.killLevel ++;
    }

    public static int getKillExperience() {
        return killExperience;
    }

    public void setKillExperience(int killExperience) {
        this.killExperience = killExperience;
    }

    public void addKillExperience() {
        this.killExperience ++;
    }

    public static int getKillPrestige() {
        return killPrestige;
    }

    public void setKillPrestige(int killPrestige) {
        this.killPrestige = killPrestige;
    }

    public void addKillPrestige() {
        this.killPrestige ++;
    }
}
