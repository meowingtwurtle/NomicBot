package com.srgood.dbot.utils;

public enum Permissions {
    DEVELOPER(99), ADMINISTRATOR(3), STANDARD(0);

    int level;

    Permissions(int lvl) {
        level = lvl;
    }

    int getLevel() {
        return level;
    }
}
