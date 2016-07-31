package com.srgood.dbot.utils;

public enum Permissions {
	DEVELOPER(99, "DEV", "Bot Developer"), ADMINISTRATOR(3, "ADMIN", "Reasons Admin"), STANDARD(0, "STANDARD", null);

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
    
    String getXMLName() {
        return xmlName;
    }
    
    String getReadableName() {
        return readableName;
    }
}
