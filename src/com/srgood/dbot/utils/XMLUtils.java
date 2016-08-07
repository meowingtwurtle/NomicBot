package com.srgood.dbot.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.srgood.dbot.BotMain;
import com.srgood.dbot.commands.Command;

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
        Element isEnabledElement = BotMain.PInputFile.createElement("isEnabled");
        permLevelElement.setTextContent("" + BotMain.commands.get(command).defaultPermissionLevel().getLevel());
        isEnabledElement.setTextContent("true");
        
        commandElement.appendChild(permLevelElement);
        commandElement.appendChild(isEnabledElement);
        commandsElement.appendChild(commandElement);
    }
    
    
    
    public static boolean verifyXML() {

        Document doc = BotMain.PInputFile;

        if (!doc.getDocumentElement().getTagName().equals("config")) {
            System.out.println("Invalid document element");
            return false;
        }

        NodeList globalNodeList = doc.getDocumentElement().getElementsByTagName("global");
        if (globalNodeList.getLength() != 1) {
            System.out.println("Not 1 global element");
            return false;
        }
        Element globalElement = (Element) globalNodeList.item(0);
        if (globalElement.getElementsByTagName("prefix").getLength() != 1) {
            System.out.println("Not 1 global/prefix element");
            return false;
        }
        

        if (doc.getDocumentElement().getElementsByTagName("servers").getLength() != 1) {
            System.out.println("Not 1 servers element");
            return false;
        }

        for (Node n : nodeListToList(((Element) doc.getDocumentElement().getElementsByTagName("servers").item(0))
                .getElementsByTagName("server"))) {
            Element serverElement = (Element) n;
            
            if (serverElement.getElementsByTagName("prefix").getLength() != 1) {
                System.out.println("Not 1 servers/server/prefix element");
                return false;
            }
            
            NodeList rolesNodeList = serverElement.getElementsByTagName("roles");
            if (rolesNodeList.getLength() != 1) {
                System.out.println("Not 1 servers/server/roles element");
                return false;
            }
            if (((Element) rolesNodeList.item(0)).getElementsByTagName("role").getLength() < 1) {
                System.out.println("Less than 1 servers/server/roles/role element");
                return false;
            }
            
            NodeList commandsNodeList = serverElement.getElementsByTagName("commands");
            
            if (commandsNodeList.getLength() != 1) {
                System.out.println("Not 1 servers/server/commands element");
                return false;
            }
            NodeList commandNodeList = ( (Element) commandsNodeList.item(0)).getElementsByTagName("command");
            if (commandNodeList.getLength() < 1) {
                System.out.println("Less than 1 servers/server/commands/command element");
                return false;
            }
            {
                for (Node temp : nodeListToList(commandNodeList)) {
                    Element commandElement = (Element ) temp;
                    if (commandElement.getElementsByTagName("permLevel").getLength() != 1) {
                        System.out.println("Not 1 servers/server/commands/command/permLevel element");
                        return false;
                    }
                }
            }
        }

        return true;

    }
    
    public static List<Node> getRoleNodeListFromGuild(Guild guild) {
        Element serverElem = (Element) BotMain.servers.get(guild.getId());
        
        Element rolesElem = (Element) serverElem.getElementsByTagName("roles").item(0);
                
        return nodeListToList(rolesElem.getElementsByTagName("role"));
    }
    
    public static Boolean commandIsEnabled(Guild guild, Command command) {


        String commandName = null;

        for (String e : BotMain.commands.keySet()) {
            if (BotMain.commands.get(e) == command) {
                commandName = e;
            }
        }

        if (BotMain.commands.values().contains(command)) {
            Element commandsElement = (Element) ((Element) BotMain.servers.get(guild.getId()))
                    .getElementsByTagName("commands").item(0);
            
            List<Node> commandList = XMLUtils.nodeListToList(commandsElement.getElementsByTagName("command"));
            
            for (Node n : commandList) {
                Element elem = (Element) n;
                if (elem.getAttribute("name").equals(commandName)) {
                	System.out.println("" + Boolean.parseBoolean(elem.getLastChild().getTextContent().trim()));
                	return Boolean.parseBoolean(elem.getElementsByTagName("isEnabled").item(0).getTextContent());
                }
            }
        }

    	return null;
    }
}
