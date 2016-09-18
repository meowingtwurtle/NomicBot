package com.srgood.dbot.utils;

import com.srgood.dbot.BotMain;
import com.srgood.dbot.PermissionLevels;
import com.srgood.dbot.commands.Command;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.Role;
import net.dv8tion.jda.utils.SimpleLog;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;
import java.util.function.Function;

public class ConfigUtils {
    private static Map<String, Element> servers = new HashMap<>();

    private static Element getServerNode(Guild guild) {
        return servers.get(guild.getId());
    }

    public static Set<Role> getGuildRolesFromInternalName(String internalName, Guild guild) {
        Element rolesElem = getRolesElement(guild);

        List<Node> roleNodes = nodeListToList(rolesElem.getElementsByTagName("role"));

        Set<Role> ret = new HashSet<>();

        for (Node i : roleNodes) {
            Element elem = (Element) i;
            if (internalName.equals(elem.getAttribute(internalName))) {
                ret.add(guild.getRoleById(i.getTextContent()));
            }
        }

        return ret;
    }

    private static Element getRolesElement(Guild guild) {
        return getFirstSubElement(getServerNode(guild), "roles");
    }

    private static List<Node> nodeListToList(NodeList nl) {
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

    private static Element getCommandsElement(Guild guild) {
        return getFirstSubElement(getServerNode(guild), "commands");
    }

    private static void initCommandsElement(Element commandsElement) {
        try {
            for (String command : CommandUtils.commands.keySet()) {
                initCommandElement(commandsElement, command);
            }
        } catch (Exception e4) {
            e4.printStackTrace();
        }
    }

    private static void initCommandElement(Element commandsElement, String command) {
        command = CommandUtils.getPrimaryCommandAlias(command);

        if (commandElementExists(commandsElement, command)) { return; }

        Element commandElement = BotMain.PInputFile.createElement("command");

        commandElement.setAttribute("name", command);

        commandsElement.appendChild(commandElement);

        addMissingSubElementsToCommand(commandsElement, command);
    }

    public static void initGuildXML(Guild guild) {
        Element server = BotMain.PInputFile.createElement("server");

        Element elementServers = getFirstSubElement(BotMain.PInputFile.getDocumentElement(), "servers");

        Attr idAttr = BotMain.PInputFile.createAttribute("id");
        idAttr.setValue(guild.getId());
        server.setAttributeNode(idAttr);

        Element defaultElement = getFirstSubElement(BotMain.PInputFile.getDocumentElement(), "default");

        ConfigUtils.nodeListToList(defaultElement.getChildNodes()).stream().filter(n -> n instanceof org.w3c.dom.Element).forEach(n -> {
            org.w3c.dom.Element elem = (org.w3c.dom.Element) n;
            server.appendChild(elem.cloneNode(true));
        });

        elementServers.appendChild(server);
        servers.put(guild.getId(), server);

        Element elementRoleContainer = BotMain.PInputFile.createElement("roles");

        server.appendChild(elementRoleContainer);
    }

    public static boolean verifyXML() {

        Document doc = BotMain.PInputFile;
        SimpleLog.getLog("Reasons").warn("**XML IS BEING VERIFIED**");

        if (!doc.getDocumentElement().getTagName().equals("config")) {
            SimpleLog.getLog("Reasons").info("Invalid document element");
            return false;
        }

        NodeList defaultNodeList = doc.getDocumentElement().getElementsByTagName("default");
        if (defaultNodeList.getLength() != 1) {
            SimpleLog.getLog("Reasons").info("Not 1 default element");
            return false;
        }
        Element defaultElement = (Element) defaultNodeList.item(0);
        if (defaultElement.getElementsByTagName("prefix").getLength() != 1) {
            SimpleLog.getLog("Reasons").info("Not 1 default/prefix element");
            return false;
        }


        if (doc.getDocumentElement().getElementsByTagName("servers").getLength() != 1) {
            SimpleLog.getLog("Reasons").info("Not 1 servers element");
            return false;
        }

        for (Node n : nodeListToList(((Element) doc.getDocumentElement().getElementsByTagName("servers").item(0)).getElementsByTagName("server"))) {
            Element serverElement = (Element) n;

            if (serverElement.getElementsByTagName("prefix").getLength() != 1) {
                SimpleLog.getLog("Reasons").info("Not 1 servers/server/prefix element");
                return false;
            }

            NodeList rolesNodeList = serverElement.getElementsByTagName("roles");
            if (rolesNodeList.getLength() != 1) {
                SimpleLog.getLog("Reasons").info("Not 1 servers/server/roles element");
                return false;
            }
            if (((Element) rolesNodeList.item(0)).getElementsByTagName("role").getLength() < 1) {
                SimpleLog.getLog("Reasons").info("Less than 1 servers/server/roles/role element");
                return false;
            }

            NodeList commandsNodeList = serverElement.getElementsByTagName("commands");

            if (commandsNodeList.getLength() != 1) {
                SimpleLog.getLog("Reasons").info("Not 1 servers/server/commands element");
                return false;
            }
            NodeList commandNodeList = ((Element) commandsNodeList.item(0)).getElementsByTagName("command");
            if (commandNodeList.getLength() < 1) {
                SimpleLog.getLog("Reasons").info("Less than 1 servers/server/commands/command element");
                return false;
            }
            {
                for (Node node : nodeListToList(commandNodeList)) {
                    Element commandElement = (Element) node;
                    if (commandElement.getElementsByTagName("permLevel").getLength() != 1) {
                        SimpleLog.getLog("Reasons").info("Not 1 servers/server/commands/command/permLevel element");
                        return false;
                    }
                    if (commandElement.getElementsByTagName("isEnabled").getLength() != 1) {
                        SimpleLog.getLog("Reasons").info("Not 1 servers/server/commands/command/isEnabled element");
                        return false;
                    }
                }
            }
        }

        return true;

    }

    private static List<Node> getRoleNodeListFromGuild(Guild guild) {
        Element serverElem = getServerNode(guild);

        Element rolesElem = getFirstSubElement(serverElem, "roles");

        return nodeListToList(rolesElem.getElementsByTagName("role"));
    }

    private static Element getCommandEnabledElement(Guild guild, Command command) {
        String commandName = CommandUtils.getNameFromCommand(command);

        if (CommandUtils.commands.values().contains(command)) {
            Element commandsElement = getCommandsElement(guild);

            List<Node> commandList = ConfigUtils.nodeListToList(commandsElement.getElementsByTagName("command"));

            for (Node n : commandList) {
                Element elem = (Element) n;
                if (elem.getAttribute("name").equals(commandName)) {
//                  System.out.println("" + Boolean.parseBoolean(elem.getLastChild().getTextContent().trim()));
                    try {
                        return getFirstSubElement(elem, "isEnabled");
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
        return null;
    }

    public static boolean commandIsEnabled(Guild guild, Command command) {
        return Boolean.parseBoolean(getCommandEnabledElement(guild, command).getTextContent());
    }

    public static void setCommandIsEnabled(Guild guild, Command command, boolean enabled) {
        getCommandEnabledElement(guild, command).setTextContent("" + enabled);
    }

    private static boolean commandElementExists(Element commandsElement, String cmdName) {
        for (Node n : nodeListToList(commandsElement.getElementsByTagName("command"))) {
            Element elem = (Element) n;
            if (elem.getAttribute("name").equals(cmdName)) return true;
        }
        return false;
    }

    private static final Map<String, Function<String, Object>> requiredCommandSubElements = new HashMap<String, Function<String, Object>>() {
        /**
         *
         */
        private static final long serialVersionUID = -710068261487017415L;

        {
            put("permLevel", name -> CommandUtils.commands.get(name).defaultPermissionLevel().getLevel());
            put("isEnabled", name -> true);
        }
    };

    private static void addMissingSubElementsToCommand(Element commandsElement, String commandName) {
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

    private static Element getFirstSubElement(Element parent, String subTagName) {
        List<Node> nList = nodeListToList(parent.getElementsByTagName(subTagName));
        return (Element) nList.get(0);
    }

    public static void deleteGuild(Guild guild) {
        for (Node n : nodeListToList(getFirstSubElement(getServerNode(guild), "roles").getElementsByTagName("role"))) {
            guild.getRoleById(n.getTextContent()).getManager().delete();
        }
        getServerNode(guild).getParentNode().removeChild(getServerNode(guild));
    }

    public static String getGuildPrefix(Guild guild) {
        if (!servers.containsKey(guild.getId())) {
            SimpleLog.getLog("Reasons").info("initializing Guild from message");
            com.srgood.dbot.utils.GuildUtils.initGuild(guild);
        }
        return getGuildPrefixNode(guild).getTextContent();
    }

    public static void setGuildPrefix(Guild guild, String newPrefix) {
        getGuildPrefixNode(guild).setTextContent(newPrefix);
    }

    private static Node getGuildPrefixNode(Guild guild) {
        return getFirstSubElement(getServerNode(guild), "prefix");
    }

    public static void initCommandIfNotExists(com.srgood.dbot.commands.CommandParser.CommandContainer cmd) {
        Element serverElement = getServerNode(cmd.event.getGuild());
        Element commandsElement;
        {
            NodeList commandsNodeList = serverElement.getElementsByTagName("commands");
            if (commandsNodeList.getLength() == 0) {
                initGuildCommands(cmd.event.getGuild());
            }
            commandsElement = getCommandsElement(cmd.event.getGuild());

            NodeList commandNodeList = commandsElement.getElementsByTagName("command");
            if (commandNodeList.getLength() == 0) {
                initCommandsElement(commandsElement);
            }
        }
        if (commandElementExists(commandsElement, cmd.invoke)) {
            addMissingSubElementsToCommand(commandsElement, cmd.invoke);
            return;
        }
        initCommandElement(commandsElement, cmd.invoke);
    }

    // TODO fix the exceptions here, too
    public static void initStorage() throws Exception {
        File inputFile = new File("servers.xml");
        initFromStream(new FileInputStream(inputFile));
    }

    public static void initFromStream(InputStream inputStream) throws Exception {
        try {
            servers = new HashMap<>();

            BotMain.DomFactory = DocumentBuilderFactory.newInstance();
            BotMain.DomInput = BotMain.DomFactory.newDocumentBuilder();

            BotMain.PInputFile = BotMain.DomInput.parse(inputStream);
            BotMain.PInputFile.getDocumentElement().normalize();

            // <config> element
            Element rootElem = BotMain.PInputFile.getDocumentElement();
            // <server> element list
            NodeList ServerNodes = rootElem.getElementsByTagName("server");
            for (int i = 0; i < ServerNodes.getLength(); i++) {
                Element ServerNode = (Element) ServerNodes.item(i);

                servers.put(ServerNode.getAttribute("id"), ServerNode);
            }

            BotMain.prefix = getFirstSubElement(getFirstSubElement(rootElem, "default"), "prefix").getTextContent();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static com.srgood.dbot.PermissionLevels getCommandPermissionXML(Guild guild, Command command) {


        String commandName = CommandUtils.getNameFromCommand(command);

        if (CommandUtils.commands.values().contains(command)) {
            Element commandsElement = getFirstSubElement(getServerNode(guild), "commands");

            List<Node> commandList = nodeListToList(commandsElement.getElementsByTagName("command"));

            for (Node n : commandList) {
                Element elem = (Element) n;
                if (elem.getAttribute("name").equals(commandName)) {
                    return PermissionUtils.intToEnum(Integer.parseInt(getFirstSubElement(elem, "permLevel").getTextContent()));
                }
            }
        }

        return null;
    }

    public static void setCommandPermissionXML(Guild guild, Command command, PermissionLevels perm) {
        String commandName = CommandUtils.getNameFromCommand(command);

        if (CommandUtils.commands.values().contains(command)) {
            Element commandsElement = getFirstSubElement(getServerNode(guild), "commands");

            List<Node> commandList = nodeListToList(commandsElement.getElementsByTagName("command"));

            for (Node n : commandList) {
                Element elem = (Element) n;
                if (elem.getAttribute("name").equals(commandName)) {
                    getFirstSubElement(elem, "permLevel").setTextContent("" + perm.getLevel());
                }
            }
        }

    }

    public static com.srgood.dbot.PermissionLevels roleToPermission(Role role, Guild guild) {
        com.srgood.dbot.PermissionLevels permission = null;

        if (role == null) {
            return null;
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

            for (com.srgood.dbot.PermissionLevels permLevel : com.srgood.dbot.PermissionLevels.values()) {
                if (permLevel.getLevel() >= (permission == null ? com.srgood.dbot.PermissionLevels.STANDARD : permission).getLevel() && permLevel.getXMLName().equals(roleXMLName)) {
                    permission = permLevel;
                }
            }
        }

        return permission;
    }

    public static boolean guildHasRoleForPermission(Guild guild, com.srgood.dbot.PermissionLevels roleLevel) {
        Element serverElement = ConfigUtils.getServerNode(guild);

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

    public static void registerRole(Guild guild, Role role, com.srgood.dbot.PermissionLevels roleLevel) {
        Element elementRoles = getFirstSubElement(getServerNode(guild), "roles");

        Element elementRole = BotMain.PInputFile.createElement("role");
        Attr roleAttr = BotMain.PInputFile.createAttribute("name");
        roleAttr.setValue(roleLevel.getXMLName());
        elementRole.setAttributeNode(roleAttr);
        elementRole.setTextContent(role.getId());

        elementRoles.appendChild(elementRole);
    }
}
