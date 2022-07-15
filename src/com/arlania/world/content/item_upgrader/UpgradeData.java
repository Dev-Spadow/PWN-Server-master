package com.arlania.world.content.item_upgrader;

import com.arlania.model.Item;

public enum UpgradeData {
    
    /*
     * Weapons
     */
    BLAZED_SCIMITAR(1, -18255, UpgradeType.WEAPON, 18385, 40, false, 0, -1, new Item(3928, 1), new Item(744, 250)),
    FROST_MINIGUN(2, -18254, UpgradeType.WEAPON, 5134, 50, false, 0, -1, new Item(5130, 1), new Item(4671, 1)),
    JINNI_STAFF(3, -18253, UpgradeType.WEAPON, 3951, 35, false, 0, -1, new Item(19468, 1), new Item(10835, 7500)),
    GODZILLA_WHIP(4, -18252, UpgradeType.WEAPON, 14559, 30, false, 0, -1, new Item(18865, 1), new Item(10835, 12500)),
    DRAGON_MINIGUN(5, -18251, UpgradeType.WEAPON, 5131, 50, false, 0, -1, new Item(5134, 1), new Item(4672, 1), new Item(10835, 12500)),
    STAFF_OF_VALOR(6, -18250, UpgradeType.WEAPON, 19720, 50, false, 0, -1, new Item(3951, 1), new Item(10835, 15000)),
    HULK_MINIGUN(7, -18249, UpgradeType.WEAPON, 5195, 30, false, 0, -1, new Item(5131, 1), new Item(10835, 17500)),
    DUAL_SIDED_SABER(8, -18248, UpgradeType.WEAPON, 3274, 35, false, 0, -1, new Item(3276, 1), new Item(10835, 20000)),
    KINGS_SWORD(9, -18247, UpgradeType.WEAPON, 5089, 45, false, 0, -1, new Item(18957, 1), new Item(10835, 20000)),
    EXOTIC_STAFF(10, -18246, UpgradeType.WEAPON, 8664, 30, false, 0, -1, new Item(19727, 1), new Item(10835, 25000)),
    HERBAL_BOW(11, -18245, UpgradeType.WEAPON, 8664, 25, false, 0, -1, new Item(5195, 1), new Item(10835, 20000)),
    EXODENS_BLADE(12, -18244, UpgradeType.WEAPON, 2760, 45, false, 0, -1, new Item(19618, 1), new Item(10835, 25000)),
    NOXIOUS_BLADE(13, -18243, UpgradeType.WEAPON, 4796, 50, false, 0, -1, new Item(13207, 1), new Item(10835, 25000)),
    DETRIMENTAL_MINIGUN(14, -18242, UpgradeType.WEAPON, 10905, 30, false, 0, -1, new Item(8664, 1), new Item(10835, 30000)),
    LUMINITA_SWORD(15, -18241, UpgradeType.WEAPON, 19154, 30, false, 0, -1, new Item(4796, 1), new Item(10835, 30000)),
    DREAMFLOW_SWORD(16, -18240, UpgradeType.WEAPON, 16450, 40, false, 0, -1, new Item(19154, 1), new Item(10835, 30000)),
    YOSHI_BOW(17, -18239, UpgradeType.WEAPON, 5115, 30, false, 0, -1, new Item(20427, 1), new Item(10835, 35000)),
    OBITO_STAFF(18, -18238, UpgradeType.WEAPON, 8474, 50, false, 0, -1, new Item(20934, 1), new Item(10835, 40000)),
    WAND_OF_LUST(19, -18237, UpgradeType.WEAPON, 5116, 25, false, 0, -1, new Item(8474, 1), new Item(10835, 50000)),
    /*
     * Armor
     */
    EXILE_HELMET(1, -18255, UpgradeType.ARMOR, 4001, 50, false, 1, -1, new Item(6194, 1), new Item(10835, 7_500)),
    EXILE_BODY(1, -18254, UpgradeType.ARMOR, 3999, 50, false, 1, -1, new Item(6195, 1), new Item(10835, 7_500)),
    EXILE_LEGS(1, -18253, UpgradeType.ARMOR, 4000, 50, false, 1, -1, new Item(6196, 1), new Item(10835, 7_500)),
    JINNI_CAPE(1, -18252, UpgradeType.ARMOR, 18748, 35, false, 1, -1, new Item(3973, 1), new Item(10835, 7_500)),
    LUCID_HELMET(1, -18251, UpgradeType.ARMOR, 3820, 40, false, 1, -1, new Item(4761, 1), new Item(10835, 10_00)),
    LUCID_BODY(1, -18250, UpgradeType.ARMOR, 3821, 40, false, 1, -1, new Item(4762, 1), new Item(10835, 10_000)),
    LUCID_LEGS(1, -18249, UpgradeType.ARMOR, 3822, 40, false, 1, -1, new Item(4763, 1), new Item(10835, 10_000)),
    HULKS_JUSTICIAR_HELMET(1, -18248, UpgradeType.ARMOR, 20240, 40, false, 1, -1, new Item(3820, 1), new Item(10835, 12_500)),
    HULKS_JUSTICIAR_BODY(1, -18247, UpgradeType.ARMOR, 4781, 40, false, 1, -1, new Item(3821, 1), new Item(10835, 12_500)),
    HULKS_JUSTICIAR_LEGS(1, -18246, UpgradeType.ARMOR, 4782, 40, false, 1, -1, new Item(3822, 1), new Item(10835, 12_500)),
    HEATED_HELMET(1, -18245, UpgradeType.ARMOR, 19619, 50, false, 1, -1, new Item(5084, 1), new Item(10835, 15_000)),
    HEATED_PLATEBODY(1, -18244, UpgradeType.ARMOR, 19470, 50, false, 1, -1, new Item(5083, 1), new Item(10835, 15_000)),
    HEATED_PLATELEGS(1, -18243, UpgradeType.ARMOR, 19471, 50, false, 1, -1, new Item(5082, 1), new Item(10835, 15_000)),
    HEATED_BOOTS(1, -18242, UpgradeType.ARMOR, 19473, 50, false, 1, -1, new Item(3985, 1), new Item(10835, 15_000)),
    HEATED_GLOVES(1, -18241, UpgradeType.ARMOR, 19472, 50, false, 1, -1, new Item(17151, 1), new Item(10835, 15_000)),

    STORMBREAKER_HELMET(1, -18240, UpgradeType.ARMOR, 13206, 45, false, 1, -1, new Item(14494, 1), new Item(10835, 20_000)),
    STORMBREAKER_BODY(1, -18239, UpgradeType.ARMOR, 13202, 45, false, 1, -1, new Item(14492, 1), new Item(10835, 20_000)),
    STORMBREAKER_LEGS(1, -18238, UpgradeType.ARMOR, 13203, 45, false, 1, -1, new Item(14490, 1), new Item(10835, 20_000)),

    DETRIMENTAL_HELMET(1, -18237, UpgradeType.ARMOR, 9496, 40, false, 1, -1, new Item(11143, 1), new Item(10835, 25_000)),
    DETRIMENTAL_BODY(1, -18236, UpgradeType.ARMOR, 9497, 40, false, 1, -1, new Item(11144, 1), new Item(10835, 25_000)),
    DETRIMENTAL_LEGS(1, -18235, UpgradeType.ARMOR, 9498, 40, false, 1, -1, new Item(11145, 1), new Item(10835, 25_000)),
    DETRIMENTAL_BOOTS(1, -18234, UpgradeType.ARMOR, 9499, 40, false, 1, -1, new Item(11146, 1), new Item(10835, 25_000)),

    SABLE_HELMET(1, -18233, UpgradeType.ARMOR, 14440, 50, false, 1, -1, new Item(14437, 1), new Item(10835, 30_000)),
    SABLE_PLATEBODY(1, -18232, UpgradeType.ARMOR, 14441, 50, false, 1, -1, new Item(14438, 1), new Item(10835, 30_000)),
    SABLE_PLATELEGS(1, -18231, UpgradeType.ARMOR, 14442, 50, false, 1, -1, new Item(14439, 1), new Item(10835, 30_000)),

    /*
     * Tools
     */


    MEDIUM_KEY(1, -18255, UpgradeType.TOOL, 1543, 20, false, 0, -1, new Item(989, 1), new Item(10835, 500)),
    GRACIOUS_MYSTERYBOX(1, -18254, UpgradeType.TOOL, 3988, 35, false, 0, -1, new Item(6199, 1), new Item(10835, 2000)),
    FIVE_PERCENT_DR_CHARM(1, -18253, UpgradeType.TOOL, 18401, 25, false, 0, -1, new Item(18392, 1), new Item(10835, 5000)),
    COLLECTORS_NECKLACE(1, -18252, UpgradeType.TOOL, 19886, 20, false, 0, -1, new Item(15418, 1), new Item(10835, 2500)),
    RANGE_DIAMOND_I_I(1, -18251, UpgradeType.TOOL, 6510, 35, false, 0, -1, new Item(6509, 1), new Item(10835, 20000)),
    MAGIC_DIAMOND_I_I(1, -18250, UpgradeType.TOOL, 6506, 35, false, 0, -1, new Item(6505, 1), new Item(10835, 20000)),
    MELEE_DIAMOND_I_I(1, -18249, UpgradeType.TOOL, 14547, 35, false, 0, -1, new Item(14546, 1), new Item(10835, 20000)),
    COLLECTORS_NECKLACE_I(1, -18248, UpgradeType.TOOL, 19106, 35, false, 0, -1, new Item(19886, 1), new Item(10835, 10000)),
    ELEMENTAL_MYSTERYBOX(1, -18247, UpgradeType.TOOL, 15374, 30, false, 0, -1, new Item(3988, 1), new Item(10835, 5000)),
    REGAL_MYSTERYBOX(1, -18246, UpgradeType.TOOL, 13997, 25, false, 0, -1, new Item(15374, 1), new Item(10835, 7500)),
    MARVELOUS_MYSTERYBOX(1, -18245, UpgradeType.TOOL, 15521, 20, false, 0, -1, new Item(13997, 1), new Item(10835, 12500)),
    RANGE_DIAMOND_I_I_I(1, -18244, UpgradeType.TOOL, 6511, 50, false, 0, -1, new Item(6510, 1), new Item(10835, 50000)),
    MAGIC_DIAMOND_I_I_I(1, -18243, UpgradeType.TOOL, 6508, 50, false, 0, -1, new Item(6506, 1), new Item(10835, 50000)),
    MELEE_DIAMOND_I_I_I(1, -18242, UpgradeType.TOOL, 14548, 50, false, 0, -1, new Item(14547, 1), new Item(10835, 50000)),
    ETERNAL_POTION(1, -18241, UpgradeType.TOOL, 3961, 30, false, 0, -1, new Item(5185, 1), new Item(10835, 50000)),
    EXQUISITE_DIAMOND(1, -18240, UpgradeType.TOOL, 6760, 55, false, 0, -1, new Item(6758, 1), new Item(6759, 1), new Item(10835, 500000), new Item(17750, 2000)),

    ;

    private final UpgradeType type;

    public final int buttonId;
    
    public final int clickId;
    
    private int resultItem;
    
    private float successRate;
    
    private boolean otherCurrency;
    
    private int currencyAmount;
    
    private Item[] ingredients;
    
    private int safeItem;
    
    
    
    UpgradeData(int buttonId, int clickId, UpgradeType type, int resultItem, float successRate, boolean otherCurrency, int currencyAmount, int safeItem, Item... ingredients) {
        this.buttonId = buttonId;
        this.clickId = clickId;
        this.type = type;
        this.resultItem = resultItem;
        this.successRate = successRate;
        this.otherCurrency = otherCurrency;
        this.currencyAmount = currencyAmount;
        this.ingredients = ingredients;
        this.safeItem = safeItem;
    }

    
    public int getButtonId() {
        return buttonId;
    }
    
    public int getClickId() {
        return clickId;
    }
    
    public UpgradeType getType() {
        return type;
    }
    
    public int getResultItem() {
        return resultItem;
    }
    
    public float getSuccessRate() {
        return successRate;
    }
    
    public boolean getOtherCurrency() {
        return otherCurrency;
    }
    
    public int getCurrencyAmount() {
        return currencyAmount;
    }
    
    public Item[] getIngredients() {
        return ingredients;
    }
    
    public int getSafeItem() {
        return safeItem;
    }
}