package com.srgood.dbot.utils;

public enum Permissions {
    DEVELOPER(99), ADMINISTRATOR(3), STANDARD(0);

    int level;
    String xmlName;
    String readableName;
    
    Permissions(int lvl) {
        this(lvl, "", "");
    }

    Permissions(int lvl, String xmlName, String readableName) {
        level = lvl;
        this.xmlName = xmlName;
        this.readableName = readableName;
    }
    

    int getLevel() {
        return level;
    }
}
