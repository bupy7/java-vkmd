package ru.mihaly4.vkmd.model;

public class Target {
    public static final int TYPE_AUDIO = 10;
    public static final int TYPE_WALL = 20;

    private String value;
    private int type = TYPE_AUDIO;

    public Target(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isAudioType() {
        return getType() == TYPE_AUDIO;
    }

    public boolean isWallType() {
        return getType() == TYPE_WALL;
    }
}
