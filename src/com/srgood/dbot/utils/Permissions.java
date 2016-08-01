package com.srgood.dbot.utils;

public enum Permissions {
	DEVELOPER(99, "DEV", "Bot Developer"), ADMINISTRATOR(3, "ADMIN", "Reasons Admin"), STANDARD(0, "STANDARD", "Standard");

    int level;
    String xmlName;
    String readableName;
    
    Permissions(int lvl, String xmlName, String readableName) {
        level = lvl;
        this.xmlName = xmlName;
        this.readableName = readableName;
    }
    

    public int getLevel() {
        return level;
    }
    
    public String getXMLName() {
        return xmlName;
    }
    
    public String getReadableName() {
        return readableName;
    }
}
