package com.srgood.reasons.config;


import com.srgood.reasons.commands.upcoming.CommandDescriptor;
import com.srgood.reasons.utils.CommandUtils;
import net.dv8tion.jda.core.entities.Guild;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static com.srgood.reasons.config.ConfigBasicUtils.getDocument;
import static com.srgood.reasons.config.ConfigBasicUtils.getDocumentLock;

class ConfigCommandUtils {
    private static final Map<String, Function<String, Object>> requiredCommandSubElements = new HashMap<String, Function<String, Object>>() {
        private static final long serialVersionUID = -710068261487017415L;

        {
            put("isEnabled", name -> true);
        }
    };

    private static Element getCommandElement(Guild guild, String commandName) {
        return getCommandElement(getCommandsElement(guild), commandName);
    }

    private static Element getCommandElement(Guild guild, CommandDescriptor command) {
        return getCommandElement(guild, CommandUtils.getNameFromCommandDescriptor(command));
    }

    private static Element getCommandElement(Element commandsElement, String commandName) {
        try {
            getDocumentLock().readLock().lock();
            List<Node> commandList = ConfigBasicUtils.nodeListToList(commandsElement.getElementsByTagName("command"));

            for (Node n : commandList) {
                Element elem = (Element) n;
                if (elem.getAttribute("name").equals(commandName)) {
                    return elem;
                }
            }
        } finally {
            getDocumentLock().readLock().unlock();
        }
        return null;
    }

    private static String getCommandProperty(Element commandElement, String property) {
        try {
            getDocumentLock().readLock().lock();
            Element propertyElement = ConfigBasicUtils.getFirstSubElement(commandElement, property);
            return propertyElement != null ? propertyElement.getTextContent() : null;
        } finally {
            getDocumentLock().readLock().unlock();
        }
    }

    private static String getCommandProperty(Guild guild, CommandDescriptor command, String property) {
        return getCommandProperty(getCommandElement(guild, command), property);
    }

    private static String getCommandProperty(Guild guild, String commandName, String property) {
        return getCommandProperty(guild, CommandUtils.getCommandDescriptorByName(commandName), property);
    }

    private static void setCommandProperty(Element commandElement, String property, String value) {
        try {
            getDocumentLock().writeLock().lock();
            Element firstMatchElement = ConfigBasicUtils.getOrCreateFirstSubElement(commandElement, property);
            firstMatchElement.setTextContent(value);
        } finally {
            getDocumentLock().writeLock().unlock();
        }
    }

    private static void setCommandProperty(Guild guild, CommandDescriptor command, String property, String value) {
        setCommandProperty(getCommandElement(guild, command), property, value);
    }

    static void setCommandProperty(Guild guild, String commandName, String property, String value) {
        setCommandProperty(guild, CommandUtils.getCommandDescriptorByName(commandName), property, value);
    }

    private static Element getCommandsElement(Guild guild) {
        return ConfigBasicUtils.getOrCreateFirstSubElement(ConfigGuildUtils.getGuildNode(guild), "commands");
    }

    private static void initGuildCommands(Guild guild) {
        try {
            getDocumentLock().writeLock().lock();
            Element commandsElement = getDocument().createElement("commands");
            ConfigGuildUtils.getGuildNode(guild).appendChild(commandsElement);
            initCommandsElement(commandsElement);
        } finally {
            getDocumentLock().writeLock().unlock();
        }
    }

    private static void initCommandsElement(Element commandsElement) {
        try {
            for (String command : CommandUtils.getCommandsMap().keySet()) {
                initCommandElement(commandsElement, command);
            }
        } catch (Exception e4) {
            e4.printStackTrace();
        }
    }

    private static void initCommandElement(Element commandsElement, String command) {
        try {
            getDocumentLock().writeLock().lock();

            command = CommandUtils.getPrimaryCommandAlias(command);

            if (commandElementExists(commandsElement, command)) {
                return;
            }

            Element commandElement = getDocument().createElement("command");

            commandElement.setAttribute("name", command);

            commandsElement.appendChild(commandElement);

            addMissingSubElementsToCommand(commandsElement, command);
        } finally {
            getDocumentLock().writeLock().unlock();
        }

    }

    public static boolean isCommandEnabled(Guild guild, CommandDescriptor command) {
        return Boolean.parseBoolean(getCommandProperty(guild, command, "isEnabled"));
    }

    public static void setCommandIsEnabled(Guild guild, CommandDescriptor command, boolean enabled) {
        setCommandProperty(guild, command, "isEnabled", "" + enabled);
    }

    private static boolean commandElementExists(Element commandsElement, String cmdName) {
        return getCommandElement(commandsElement, cmdName) != null;
    }

    private static void addMissingSubElementsToCommand(Element commandsElement, String commandName) {
        Element targetCommandElement = getCommandElement(commandsElement, commandName);

        for (Map.Entry<String, Function<String, Object>> entry : requiredCommandSubElements.entrySet()) {
            if (getCommandProperty(targetCommandElement, entry.getKey()) == null) {
                setCommandProperty(targetCommandElement, entry.getKey(), entry.getValue().apply(commandName).toString());
            }
        }
    }

    static void initCommandConfigIfNotExists(Guild guild, String cmd) {
        try {
            getDocumentLock().writeLock().lock();
            Element serverElement = ConfigGuildUtils.getGuildNode(guild);
            Element commandsElement;
            String realCommandName = CommandUtils.getNameFromCommandDescriptor(CommandUtils.getCommandDescriptorByName(cmd));
            {
                NodeList commandsNodeList = serverElement.getElementsByTagName("commands");
                if (commandsNodeList.getLength() == 0) {
                    initGuildCommands(guild);
                }
                commandsElement = getCommandsElement(guild);

                NodeList commandNodeList = commandsElement.getElementsByTagName("command");
                if (commandNodeList.getLength() == 0) {
                    initCommandsElement(commandsElement);
                }
            }
            if (commandElementExists(commandsElement, realCommandName)) {
                addMissingSubElementsToCommand(commandsElement, realCommandName);
                return;
            }
            initCommandElement(commandsElement, realCommandName);
        } finally {
            getDocumentLock().writeLock().unlock();
        }
    }
}
