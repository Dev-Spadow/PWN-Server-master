package com.arlania.world.content.instances;

import com.arlania.model.Position;
import com.arlania.model.RegionInstance.RegionInstanceType;

public enum InstanceData {
    LUMI(22171, 185, "Luminitous Warriors", 5000, "Luminitous Warriors", new Position(3040, 2918), RegionInstanceType.INSTANCE, new int[][]{{1, 1}}),
    HELL(22172, 6311, "Custom Hellhounds", 5000, "Custom Hellhounds", new Position(3040, 2918), RegionInstanceType.INSTANCE, new int[][]{{1, 1}}),
    RAZ(22173, 2907, "Razorspawn", 5000, "Razorspawn", new Position(3040, 2918), RegionInstanceType.INSTANCE, new int[][]{{1, 1}}),
    DREAM(22174, 20, "Dreamflow Assassin", 5000, "Dreamflow Assassin", new Position(3040, 2918), RegionInstanceType.INSTANCE, new int[][]{{1, 1}}),
    KING(22175, 259, "King Khione", 5000, "king Khione", new Position(3040, 2918), RegionInstanceType.INSTANCE, new int[][]{{1, 1}}),
    SABLE(22176, 1123, "Sable Beast", 5000, "Sable Beast", new Position(3040, 2918), RegionInstanceType.INSTANCE, new int[][]{{1, 1}}),
    DEMOGO(22177, 12835, "Demogorgon", 5000, "Demogorgon", new Position(3040, 2918), RegionInstanceType.INSTANCE, new int[][]{{1, 1}}),
    YOSHI(22178, 8548, "Yoshi", 5000, "Yoshi", new Position(3040, 2918), RegionInstanceType.INSTANCE, new int[][]{{1, 1}}),
    RAJIN(22179, 12836, "Rajin", 5000, "Rajin", new Position(3040, 2918), RegionInstanceType.INSTANCE, new int[][]{{1, 1}}),
    SCAR(22180, 12805, "Scarlett Falcon", 5000, "Scarlett Falcon", new Position(3040, 2918), RegionInstanceType.INSTANCE, new int[][]{{1, 1}}),
    AVATAR(22181, 2264, "Avatar The Airbender", 5000, "Avatar The Airbender", new Position(3040, 2918), RegionInstanceType.INSTANCE, new int[][]{{1, 1}}),
    LILI(22182, 11360, "Lili", 5000, "Lili", new Position(3040, 2918), RegionInstanceType.INSTANCE, new int[][]{{1, 1}}),
    OBITO(22183, 11383, "Obito Uchiha", 5000, "Obito Uchiha", new Position(3040, 2918), RegionInstanceType.INSTANCE, new int[][]{{1, 1}}),
    URU(22184, 11305, "Uru Isiha", 5000, "Uru Isiha", new Position(3040, 2918), RegionInstanceType.INSTANCE, new int[][]{{1, 1}}),
    AHRI(22185, 5931, "Ahri", 5000, "Ahri", new Position(3040, 2918), RegionInstanceType.INSTANCE, new int[][]{{1, 1}}),
    MYS(22186, 254, "Mystery Man", 5000, "Mystery Man", new Position(3040, 2918), RegionInstanceType.INSTANCE, new int[][]{{1, 1}});

    InstanceData(int buttonid, int npcid, String text, int endamount , String name, Position position, RegionInstanceType region, int[][] rewards) {
        this.buttonid = buttonid;
        this.npcid = npcid;
        this.text = text;
        this.endamount = endamount;
        this.name = name;
        this.position = position;
        this.region = region;
        this.item = rewards;
    }

    private Position position;
    private RegionInstanceType region;

    public RegionInstanceType getRegion() {
        return region;
    }

    public void setRegion(RegionInstanceType region) {
        this.region = region;
    }

    public Position getPosition() {
        return position;
    }

    private int npcid, endamount, taskNumber, buttonid;

    public int getButtonid() {
        return buttonid;
    }

    private int amount;

    private int[][] item;

    public int[][] getItems() {
        return item;
    }

    private String text, name, weakness;

    public int getAmount() {
        return amount;
    }

    public String getWeakness() {
        return weakness;
    }

    public void setWeakness(String weakness) {
        this.weakness = weakness;
    }

    public int getTaskNumber() {
        return taskNumber;
    }

    public int getEndamount() {
        return endamount;
    }

    public void setEndamount(int endamount) {
        this.endamount = endamount;
    }

    public String getName() {
        return name;
    }

    public int getNpcid() {
        return npcid;
    }

    public String getText() {
        return text;
    }
}
