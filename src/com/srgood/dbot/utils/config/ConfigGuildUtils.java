package com.srgood.dbot.utils.config;

import net.dv8tion.jda.entities.Guild;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;

import java.util.HashMap;
import java.util.Map;

import static com.srgood.dbot.utils.config.ConfigBasicUtils.getDocument;
import static com.srgood.dbot.utils.config.ConfigBasicUtils.getFirstSubElement;

class ConfigGuildUtils {
    private static Map<String, Element> servers = new HashMap<>();

    static Element getGuildNode(Guild guild) {
        return servers.get(guild.getId());
    }

    static void ensureGuildInitted(Guild guild) {
        if (getGuildNode(guild) == null) {
            initGuildConfig(guild);
        } else {
            addMissingDefaultElementsToGuild(getGuildNode(guild));
        }
    }

    private static void addMissingDefaultElementsToGuild(Element elementServer) {
        Element elementDefault = getFirstSubElement(getDocument().getDocumentElement(), "default");

        ConfigBasicUtils.nodeListToList(elementDefault.getChildNodes()).stream().filter(n -> n instanceof Element).forEach(n -> {
            Element elem = (Element) n;
            if (getFirstSubElement(elementServer, elem.getTagName()) == null) {
                elementServer.appendChild(elem.cloneNode(true));
            }
        });
    }

    private static void initGuildConfig(Guild guild) {
        Element elementServer = getDocument().createElement("server");

        Element elementServers = getFirstSubElement(getDocument().getDocumentElement(), "servers");

        Attr attrID = getDocument().createAttribute("id");
        attrID.setValue(guild.getId());
        elementServer.setAttributeNode(attrID);

        addMissingDefaultElementsToGuild(elementServer);

        elementServers.appendChild(elementServer);
        servers.put(guild.getId(), elementServer);
    }

    static void deleteGuildConfig(Guild guild) {
        getGuildNode(guild).getParentNode().removeChild(getGuildNode(guild));
    }

    static String getGuildSimpleProperty(Guild guild, String property) {
        Element propertyElement = getFirstSubElement(getGuildNode(guild), property);
        return propertyElement != null ? propertyElement.getTextContent() : null;
    }

    static void setGuildSimpleProperty(Guild guild, String property, String value) {
        Element guildElement = getGuildNode(guild);
        Element targetElement = getFirstSubElement(guildElement, property);
        if (targetElement == null) {
            targetElement = getDocument().createElement(property);
            guildElement.appendChild(targetElement);
        }
        targetElement.setTextContent(value);
    }

    static Element getGuildComplexPropertyElement(Guild guild, String property) {
        return getFirstSubElement(getGuildNode(guild), property);
    }

    static String getGuildPrefix(Guild guild) {
        return getGuildSimpleProperty(guild, "prefix");
    }

    static void setGuildPrefix(Guild guild, String newPrefix) {
        setGuildSimpleProperty(guild, "prefix", newPrefix);
    }

    static void resetServers() {
        servers = new HashMap<>();
    }

    static void addServer(String guildID, Element guildElement) {
        servers.put(guildID, guildElement);
    }
}
