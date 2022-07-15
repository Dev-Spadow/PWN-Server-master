package com.arlania.world.content.dailytasks;

public enum TaskStatus {
    INCOMPLETE(1),
    COMPLETE(0),
    CLAIMED(1);

    private final int varpValue;

    TaskStatus(final int varpValue) {
        this.varpValue = varpValue;
    }

    public int getVarpValue() {
        return varpValue;
    }
}
