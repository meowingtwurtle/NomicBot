package com.srgood.dbot.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.srgood.dbot.BotListener;
import com.srgood.dbot.BotMain;
import com.srgood.dbot.commands.Command;

import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.Role;
import net.dv8tion.jda.utils.SimpleLog;

public class XMLHandler {
    public static Map<String,Node> servers = new HashMap<String,Node>();
    public static Node getServerNode(Guild guild) {
        return servers.get(guild.getId());
    }
    
    public static Set<Role> getGuildRolesFromInternalName(String internalName, Guild guild) {
        Element rolesElem = getRolesElement(guild);
        
        List<Node> roleNodes = nodeListToList(rolesElem.getElementsByTagName("role"));
        
        Set<Role> ret = new HashSet<>();
        
        for (Node i : roleNodes) {
            if (internalName.equals(((Attr) i.getAttributes().getNamedItem("name")).getValue())) {
                ret.add(guild.getRoleById(i.getTextContent()));
            }
        }
        
        return ret;
    }
    
    public static Element getRolesElement(Guild guild) {
        Element serverElem = (Element) getServerNode(guild);
        Element rolesElem = (Element) serverElem.getElementsByTagName("roles").item(0);
        return rolesElem;
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
        getServerNode(guild).appendChild(commandsElement);
        initCommandsElement(commandsElement);
    }
    
    public static Element getCommandsElement(Guild guild) {
        return (Element) ((Element) getServerNode(guild)).getElementsByTagName("commands").item(0);
    }
//    
//    public static Element getCommandsElement(Guild guild) {
//        
//    }
    
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
        
        commandsElement.appendChild(commandElement);
        
        addMissingSubElementsToCommand(commandsElement, command);
    }
    
    public static void initGuildXML(Guild guild) {
        Element server = BotMain.PInputFile.createElement("server");

        Element elementServers = (Element) BotMain.PInputFile.getDocumentElement().getElementsByTagName("servers")
                .item(0);

        Attr idAttr = BotMain.PInputFile.createAttribute("id");
        idAttr.setValue(guild.getId());
        server.setAttributeNode(idAttr);

        Element globalElement = (Element) BotMain.PInputFile.getDocumentElement().getElementsByTagName("global")
                .item(0);

        for (Node n : XMLHandler.nodeListToList(globalElement.getChildNodes())) {
            if (n instanceof Element) {
                Element elem = (Element) n;
                server.appendChild(elem.cloneNode(true));
            }
        }

        elementServers.appendChild(server);
        servers.put(guild.getId(), server);

        Element elementRoleContainer = BotMain.PInputFile.createElement("roles");

        server.appendChild(elementRoleContainer);
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
                    if (commandElement.getElementsByTagName("isEnabled").getLength() != 1) {
                        System.out.println("Not 1 servers/server/commands/command/isEnabled element");
                        return false;
                    }
                }
            }
        }

        return true;

    }
    
    public static List<Node> getRoleNodeListFromGuild(Guild guild) {
        Element serverElem = (Element) getServerNode(guild);
        
        Element rolesElem = (Element) serverElem.getElementsByTagName("roles").item(0);
                
        return nodeListToList(rolesElem.getElementsByTagName("role"));
    }
    
    public static boolean commandIsEnabled(Guild guild, Command command) {


        String commandName = null;

        for (String e : BotMain.commands.keySet()) {
            if (BotMain.commands.get(e) == command) {
                commandName = e;
            }
        }

        if (BotMain.commands.values().contains(command)) {
            Element commandsElement = getCommandsElement(guild) ;
            
            List<Node> commandList = XMLHandler.nodeListToList(commandsElement.getElementsByTagName("command"));
            
            for (Node n : commandList) {
                Element elem = (Element) n;
                if (elem.getAttribute("name").equals(commandName)) {
//                	System.out.println("" + Boolean.parseBoolean(elem.getLastChild().getTextContent().trim()));
                	try {
                	    Boolean ret = Boolean.parseBoolean(elem.getElementsByTagName("isEnabled").item(0).getTextContent());
                		return ret == null ? true : ret;
                	} catch (NullPointerException e) {
                	    e.printStackTrace();
                	}
                	
                }
            }
        }
        return false;
    }

    public static boolean commandElementExists(Element commandsElement, String cmdName) {
        for (Node n : nodeListToList(commandsElement.getElementsByTagName("command"))) {
            Element elem = (Element) n;
            if (elem.getAttribute("name").equals(cmdName)) return true;
        }
        return false;
    }
    
    static Map<String, Function<String, Object>> requiredCommandSubElements = new HashMap<String, Function<String, Object>>() {
        /**
         * 
         */
        private static final long serialVersionUID = -710068261487017415L;

        {
            put("permLevel", name -> BotMain.commands.get(name).defaultPermissionLevel().getLevel());
            put("isEnabled", name -> true);
        }
    };

    public static void addMissingSubElementsToCommand(Element commandsElement, String commandName) {
        Element targetCommand = null;
        for (Node n : nodeListToList(commandsElement.getElementsByTagName("command"))) {
            if (((Element) n).getAttribute("name").equals(commandName)) {
                targetCommand = (Element) n;
                break;
            }
        }
        
        for (String s : requiredCommandSubElements.keySet()) {
            if (targetCommand.getElementsByTagName(s).getLength() < 1) {
                Element subElement = BotMain.PInputFile.createElement(s);
                subElement.setTextContent("" + requiredCommandSubElements.get(s).apply(commandName));
                targetCommand.appendChild(subElement);
            }
        }
    }

    
    public static void deleteGuild(Guild guild) {
        for (Node n : nodeListToList(
                ((Element) ((Element) getServerNode(guild)).getElementsByTagName("roles").item(0))
                        .getElementsByTagName("role"))) {
            guild.getRoleById(n.getTextContent()).getManager().delete();
        }
        getServerNode(guild).getParentNode().removeChild(getServerNode(guild));
    }

    public static String getGuildPrefix(Guild guild) {
        if (!servers.containsKey(guild.getId())) {
            System.out.println("initting Guild from message");
            BotListener.initGuild(guild);
        }
        System.out.println(((Element)getGuildPrefixNode(guild)).getTagName());
        return getGuildPrefixNode(guild).getTextContent();
    }
    
    public static void setGuildPrefix(Guild guild, String newPrefix) {
        getGuildPrefixNode(guild).setTextContent(newPrefix);
    }

    private static Node getGuildPrefixNode(Guild guild) {
        Node ServerNode = getServerNode(guild);
        Element NodeElement = (Element) ServerNode;
        return NodeElement.getElementsByTagName("prefix").item(0);
    }
    
    public static void createCommandNodeIfNotExists(CommandParser.CommandContainer cmd) {
        Element serverElement = (Element) getServerNode(cmd.event.getGuild());
        Element commandsElement;
        {
            NodeList commandsNodeList = serverElement.getElementsByTagName("commands");
            if (commandsNodeList.getLength() == 0) {
                initGuildCommands(cmd.event.getGuild());
            }
            commandsNodeList = serverElement.getElementsByTagName("commands");
            commandsElement = getCommandsElement(cmd.event.getGuild());
            
            NodeList commandNodeList = commandsElement.getElementsByTagName("command");
            if (commandNodeList.getLength() == 0) {
                initCommandsElement(commandsElement);
            }
        }
        if (commandElementExists(commandsElement, cmd.invoke)) {
            System.out.println("Command element exists");
                addMissingSubElementsToCommand(commandsElement, cmd.invoke);
                return;
        }
        System.out.println("Command element not exists");
        initCommandElement(commandsElement, cmd.invoke);
    }

    // TODO fix the exceptions here, too
    public static void putNodes() throws Exception {
        try {
    	BotMain.DomFactory = DocumentBuilderFactory.newInstance();
    	BotMain.DomInput = BotMain.DomFactory.newDocumentBuilder();
    
    	File InputFile = new File("servers.xml");
    
    	BotMain.PInputFile = BotMain.DomInput.parse(InputFile);
    	BotMain.PInputFile.getDocumentElement().normalize();
    	SimpleLog.getLog("Reasons.").info(BotMain.PInputFile.getDocumentElement().getNodeName());
    
    	// <config> element
    	Element rootElem = BotMain.PInputFile.getDocumentElement();
    	// <server> element list
    	NodeList ServerNodes = rootElem.getElementsByTagName("server");
    	for (int i = 0; i < ServerNodes.getLength(); i++) {
    		Node ServerNode = ServerNodes.item(i);
    		Element ServerNodeElement = (Element) ServerNode;
    
    		servers.put(ServerNodeElement.getAttribute("id"), ServerNode);
    	}
    	
    	Element globalElem = (Element) rootElem.getElementsByTagName("global").item(0);
    	
    	BotMain.prefix = globalElem.getElementsByTagName("prefix").item(0).getTextContent();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static Permissions getCommandPermissionXML(Guild guild, Command command) {
    
    
        String commandName = null;
    
        for (String e : BotMain.commands.keySet()) {
            if (BotMain.commands.get(e) == command) {
                commandName = e;
            }
        }
    
        if (BotMain.commands.values().contains(command)) {
            Element commandsElement = (Element) ((Element) getServerNode(guild))
                    .getElementsByTagName("commands").item(0);
            
            List<Node> commandList = nodeListToList(commandsElement.getElementsByTagName("command"));
            
            for (Node n : commandList) {
                Element elem = (Element) n;
                if (elem.getAttribute("name").equals(commandName)) {
                	System.out.println(elem.getTagName());
                    return PermissionOps.intToEnum(Integer.parseInt(elem.getElementsByTagName("permLevel").item(0).getTextContent()));
                }
            }
        }
    
        return null;
    }

    public static Permissions roleToPermission(Role role, Guild guild) {
    	Permissions permission = null;
    	
    	if (role == null) {
    	    return permission;
    	}
    	
    	//<config>
    	//  <servers>
    	//    <server>
    	//      <roles>
    	//        <role>
    	
    	// <server>
    
    	
    	List<Node> roleNodeList = getRoleNodeListFromGuild(guild);
    	
    	String roleID = role.getId();
    	
    	for (Node n : roleNodeList) {
    	    Element roleElem = (Element) n;
    	    String roleXMLName = roleElem.getAttribute("name");
    	    
    	    if (!roleID.equals(roleElem.getTextContent())) {
    	        continue;
    	    }
    	    
    	    for (Permissions permLevel : Permissions.values()) {
    	        if (permLevel.getLevel() >= (permission == null ? Permissions.STANDARD : permission).getLevel() && permLevel.getXMLName().equals(roleXMLName)) {
    	            permission = permLevel;
    	        }
    	    }
    	}
    	
    	return permission;
    }
    
    public static boolean guildHasRoleForPermission(Guild guild, Permissions roleLevel) {
        Element serverElement = (Element) XMLHandler.getServerNode(guild);

        Element rolesElement = (Element) serverElement.getElementsByTagName("roles");

        List<Node> roleElementList = nodeListToList(rolesElement.getElementsByTagName("role"));

        for (Node n : roleElementList) {
            Element roleElem = (Element) n;
            if (roleElem.getAttribute("name").equals(roleLevel.getXMLName())) {
                return true;
            }
        }
        return false;
    }
    
    public static void registerRoleXML(Guild guild, Role role, Permissions roleLevel) {
       Element elementRoles = (Element) (((Element) getServerNode(guild)).getElementsByTagName("roles").item(0));
        
        Element elementRole = BotMain.PInputFile.createElement("role");
        Attr roleAttr = BotMain.PInputFile.createAttribute("name");
        roleAttr.setValue(roleLevel.getXMLName());
        elementRole.setAttributeNode(roleAttr);
        elementRole.setTextContent(role.getId());

        elementRoles.appendChild(elementRole);
    }
}
