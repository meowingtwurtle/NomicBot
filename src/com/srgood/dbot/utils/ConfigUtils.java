package com.srgood.dbot.utils;

import com.srgood.dbot.BotMain;
import com.srgood.dbot.PermissionLevels;
import com.srgood.dbot.commands.Command;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.Role;
import net.dv8tion.jda.utils.SimpleLog;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;
import java.util.function.Function;

public class ConfigUtils {
    private static Document document;
    private static Map<String, Element> servers = new HashMap<>();

    static Document getDocument() {
        return document;
    }

    private static Element getCommandElement(Guild guild, String commandName) {
        return getCommandElement(getCommandsElement(guild), commandName);
    }

    private static Element getCommandElement(Guild guild, Command command) {
        return getCommandElement(guild, CommandUtils.getNameFromCommand(command));
    }

    private static Element getCommandElement(Element commandsElement, String commandName) {
            List<Node> commandList = ConfigUtils.nodeListToList(commandsElement.getElementsByTagName("command"));

            for (Node n : commandList) {
                Element elem = (Element) n;
                if (elem.getAttribute("name").equals(commandName)) {
                    return elem;
                }
            }
        return null;
    }

    private static Element getGuildNode(Guild guild) {
        return servers.get(guild.getId());
    }


    private static String getCommandProperty(Element commandElement, String property) {
        return getFirstSubElement(commandElement, property).getTextContent();
    }

    private static String getCommandProperty(Guild guild, Command command, String property) {
        return getCommandProperty(getCommandElement(guild, command), property);
    }

    private static String getCommandProperty(Guild guild, String commandName, String property) {
        return getCommandProperty(guild, CommandUtils.getCommandByName(commandName), property);
    }

    private static void setCommandProperty(Element commandElement, String property, String value) {
        Element firstMatchElement = getFirstSubElement(commandElement, property);
        if (firstMatchElement == null) {
            Element newPropElem = getDocument().createElement(property);
            newPropElem.setTextContent(value);
            commandElement.appendChild(newPropElem);
            return;
        }
        firstMatchElement.setTextContent(value);
    }

    private static void setCommandProperty(Guild guild, Command command, String property, String value) {
        setCommandProperty(getCommandElement(guild, command), property, value);
    }

    private static void setCommandProperty(Guild guild, String commandName, String property, String value) {
        setCommandProperty(guild, CommandUtils.getCommandByName(commandName), property, value);
    }


    public static Set<Role> getGuildRolesFromPermissionName(Guild guild, String permissionName) {
        Element rolesElem = getRolesElement(guild);

        List<Node> roleNodes = nodeListToList(rolesElem.getElementsByTagName("role"));

        Set<Role> ret = new HashSet<>();

        for (Node i : roleNodes) {
            Element elem = (Element) i;
            if (permissionName.equals(elem.getAttribute(permissionName))) {
                ret.add(guild.getRoleById(i.getTextContent()));
            }
        }

        return ret;
    }

    private static Element getRolesElement(Guild guild) {
        return getFirstSubElement(getGuildNode(guild), "roles");
    }

    private static List<Node> nodeListToList(NodeList nl) {
        List<Node> ret = new ArrayList<>();

        for (int i = 0; i < nl.getLength(); i++) {
            ret.add(nl.item(i));
        }

        return ret;
    }

    public static void initGuildCommands(Guild guild) {
        Element commandsElement = getDocument().createElement("commands");
        getGuildNode(guild).appendChild(commandsElement);
        initCommandsElement(commandsElement);
    }

    private static Element getCommandsElement(Guild guild) {
        return getFirstSubElement(getGuildNode(guild), "commands");
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

        Element commandElement = getDocument().createElement("command");

        commandElement.setAttribute("name", command);

        commandsElement.appendChild(commandElement);

        addMissingSubElementsToCommand(commandsElement, command);
    }

    public static void initGuildConfig(Guild guild) {
        Element elementServer = getDocument().createElement("server");

        Element elementServers = getFirstSubElement(getDocument().getDocumentElement(), "servers");

        Attr attrID = getDocument().createAttribute("id");
        attrID.setValue(guild.getId());
        elementServer.setAttributeNode(attrID);

        Element elementDefault = getFirstSubElement(getDocument().getDocumentElement(), "default");

        ConfigUtils.nodeListToList(elementDefault.getChildNodes()).stream().filter(n -> n instanceof Element).forEach(n -> {
            Element elem = (Element) n;
            elementServer.appendChild(elem.cloneNode(true));
        });

        elementServers.appendChild(elementServer);
        servers.put(guild.getId(), elementServer);

        Element elementRoleContainer = getDocument().createElement("roles");

        elementServer.appendChild(elementRoleContainer);
    }

    public static boolean verifyConfig() {

        Document doc = getDocument();
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
        Element serverElem = getGuildNode(guild);

        Element rolesElem = getFirstSubElement(serverElem, "roles");

        return nodeListToList(rolesElem.getElementsByTagName("role"));
    }

    public static boolean commandIsEnabled(Guild guild, Command command) {
        return Boolean.parseBoolean(getCommandProperty(guild, command, "enabled"));
    }

    public static void setCommandIsEnabled(Guild guild, Command command, boolean enabled) {
        setCommandProperty(guild, command, "enabled", "" + enabled);
    }

    private static boolean commandElementExists(Element commandsElement, String cmdName) {
        return getCommandElement(commandsElement, cmdName) != null;
    }

    private static final Map<String, Function<String, Object>> requiredCommandSubElements = new HashMap<String, Function<String, Object>>() {
        private static final long serialVersionUID = -710068261487017415L;

        {
            put("permLevel", name -> CommandUtils.getCommandByName(name).defaultPermissionLevel().getLevel());
            put("isEnabled", name -> true);
        }
    };

    private static void addMissingSubElementsToCommand(Element commandsElement, String commandName) {
        Element targetCommand = getCommandElement(commandsElement, commandName);

        for (Map.Entry<String, Function<String, Object>> entry : requiredCommandSubElements.entrySet()) {
            setCommandProperty(targetCommand, entry.getKey(), entry.getValue().apply(commandName).toString());
        }
    }

    private static Element getFirstSubElement(Element parent, String subTagName) {
        List<Node> nList = nodeListToList(parent.getElementsByTagName(subTagName));
        return (Element) (nList.size() > 0 ? nList.get(0) : null);
    }

    public static void deleteGuild(Guild guild) {
        for (Node n : nodeListToList(getFirstSubElement(getGuildNode(guild), "roles").getElementsByTagName("role"))) {
            guild.getRoleById(n.getTextContent()).getManager().delete();
        }
        getGuildNode(guild).getParentNode().removeChild(getGuildNode(guild));
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
        return getFirstSubElement(getGuildNode(guild), "prefix");
    }

    static void initCommandConfigIfNotExists(com.srgood.dbot.commands.CommandParser.CommandContainer cmd) {
        Element serverElement = getGuildNode(cmd.event.getGuild());
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

            DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder domInput = domFactory.newDocumentBuilder();

            document = domInput.parse(inputStream);
            getDocument().getDocumentElement().normalize();

            // <config> element
            Element rootElem = getDocument().getDocumentElement();
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

    public static PermissionLevels getCommandPermission(Guild guild, Command command) {
        return PermissionUtils.intToEnum(Integer.parseInt(getCommandProperty(guild, command, "permLevel")));
    }

    public static void setCommandPermission(Guild guild, Command command, PermissionLevels perm) {
        setCommandProperty(guild, command, "permLevel" , "" + perm.getLevel());
    }

    public static PermissionLevels roleToPermission(Role role, Guild guild) {
        PermissionLevels permission = null;

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

            for (PermissionLevels permLevel : PermissionLevels.values()) {
                if (permLevel.getLevel() >= (permission == null ? PermissionLevels.STANDARD : permission).getLevel() && permLevel.getXMLName().equals(roleXMLName)) {
                    permission = permLevel;
                }
            }
        }

        return permission;
    }

    public static boolean guildHasRoleForPermission(Guild guild, PermissionLevels roleLevel) {
        Element serverElement = ConfigUtils.getGuildNode(guild);

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

    public static void registerRoleConfig(Guild guild, Role role, PermissionLevels roleLevel) {
        Element elementRoles = getFirstSubElement(getGuildNode(guild), "roles");

        Element elementRole = getDocument().createElement("role");
        Attr roleAttr = getDocument().createAttribute("name");
        roleAttr.setValue(roleLevel.getXMLName());
        elementRole.setAttributeNode(roleAttr);
        elementRole.setTextContent(role.getId());

        elementRoles.appendChild(elementRole);
    }
}
