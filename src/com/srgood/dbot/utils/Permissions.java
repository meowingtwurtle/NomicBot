package com.srgood.dbot.utils;

public enum Permissions {
    DEVELOPER(99), ADMINISTRATOR(3), MEOW_MEOW_CAT(-87);

    int level;

    Permissions(int lvl) {
        level = lvl;
    }

    int getLevel() {
        return level;
    }
}
