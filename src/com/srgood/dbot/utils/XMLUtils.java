package com.srgood.dbot.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.srgood.dbot.BotMain;

import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.Role;

public class XMLUtils {
    public static Set<Role> getGuildRolesFromInternalName(String internalName, Guild guild) {
        Element serverElem = (Element) BotMain.servers.get(guild.getId());
        Element rolesElem = (Element) serverElem.getElementsByTagName("roles").item(0);
        List<Node> roleNodes = nodeListToList(rolesElem.getElementsByTagName("role"));
        
        Set<Role> ret = new HashSet<>();
        
        for (Node i : roleNodes) {
            if (internalName.equals(((Attr) i.getAttributes().getNamedItem("name")).getValue())) {
                ret.add(guild.getRoleById(i.getTextContent()));
            }
        }
        
        return ret;
    }
    
    public static List<Node> nodeListToList(NodeList nl) {
        List<Node> ret = new ArrayList<>();
        
        for (int i = 0; i < nl.getLength(); i++) {
            ret.add(nl.item(i));
        }
        
        return ret;
    }
    
    public static void initGuildCommands(Guild guild) {
        Element commandsElement = BotMain.PInputFile.createElement("commands");
        BotMain.servers.get(guild.getId()).appendChild(commandsElement);
        initCommandsElement(commandsElement);
    }
    
    public static void initCommandsElement(Element commandsElement) {
        try {            
            for (String command : BotMain.commands.keySet()) {
                initCommandElement(commandsElement, command);
            }
        } catch (Exception e4) {
            e4.printStackTrace();
        }
    }
    
    public static void initCommandElement(Element commandsElement, String command) {
        Element commandElement = BotMain.PInputFile.createElement("command");
        
        commandElement.setAttribute("name", command);
        
        Element permLevelElement = BotMain.PInputFile.createElement("permLevel");
        permLevelElement.setTextContent("" + BotMain.commands.get(command).defaultPermissionLevel().getLevel());

        commandElement.appendChild(permLevelElement);
        commandsElement.appendChild(commandElement);
    }
}
