package com.srgood.reasons.config;

import com.srgood.reasons.commands.PermissionLevels;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Role;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.srgood.reasons.config.ConfigBasicUtils.getDocument;
import static com.srgood.reasons.config.ConfigBasicUtils.getDocumentLock;

class ConfigRoleUtils {
    private static Element getRolesElement(Guild guild) {
        return ConfigBasicUtils.getOrCreateFirstSubElement(ConfigGuildUtils.getGuildNode(guild), "roles");
    }

    private static List<Node> getRoleNodeListFromGuild(Guild guild) {
            Element rolesElem = getRolesElement(guild);
            return ConfigBasicUtils.nodeListToList(rolesElem.getElementsByTagName("role"));
    }

    static PermissionLevels roleToPermission(Guild guild, Role role) {
        PermissionLevels permission = null;
        if (role == null) {
            throw new IllegalArgumentException("role cannot be null");
        }

        List<Node> roleNodeList = getRoleNodeListFromGuild(guild);

        String roleID = role.getId();

        try {
            getDocumentLock().readLock().lock();

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
        } finally {
            getDocumentLock().readLock().unlock();
        }
    }

    static boolean guildHasRoleForPermission(Guild guild, PermissionLevels roleLevel) {
        try {
            getDocumentLock().readLock().lock();
            List<Node> roleElementList = getRoleNodeListFromGuild(guild);

            for (Node n : roleElementList) {
                Element roleElem = (Element) n;
                if (roleElem.getAttribute("name").equals(roleLevel.getXMLName())) {
                    return true;
                }
            }
            return false;
        } finally {
            getDocumentLock().readLock().unlock();
        }
    }

    static void registerRoleConfig(Guild guild, Role role, PermissionLevels roleLevel) {
        try {
            getDocumentLock().writeLock().lock();
            Document doc = getDocument();
            Element elementRoles = ConfigBasicUtils.getOrCreateFirstSubElement(ConfigGuildUtils.getGuildNode(guild), "roles");

            Element elementRole = doc.createElement("role");
            Attr roleAttr = doc.createAttribute("name");
            roleAttr.setValue(roleLevel.getXMLName());
            elementRole.setAttributeNode(roleAttr);
            elementRole.setTextContent(role.getId());

            elementRoles.appendChild(elementRole);
        } finally {
            getDocumentLock().writeLock().unlock();
        }
    }

    static void deregisterRoleConfig(Guild guild, String roleID) {
        try {
            getDocumentLock().writeLock().lock();
            Element elementRole = null;

            List<Node> roleNodeList = getRoleNodeListFromGuild(guild);
            for (Node n : roleNodeList) {
                Element elem = (Element) n;
                String textContent = elem.getTextContent();
                if (textContent.equals(roleID)) {
                    elementRole = elem;
                    break;
                }
            }
            if (elementRole == null) {
                return;
            }
            elementRole.getParentNode().removeChild(elementRole);
        } finally {
            getDocumentLock().writeLock().unlock();
        }
    }

    static Set<String> getGuildRegisteredRoleIDs(Guild guild) {
        return getRoleNodeListFromGuild(guild).stream().map(Node::getTextContent).collect(Collectors.toSet());
    }

    static Set<Role> getGuildRolesFromPermissionLevel(Guild guild, PermissionLevels permissionLevel) {
        try {
            getDocumentLock().readLock().lock();
            String permissionName = permissionLevel.getXMLName();
            return getRoleNodeListFromGuild(guild).stream().filter(n -> n instanceof Element).map(n -> (Element) n).filter(elem -> elem.getAttribute("name").equals(permissionName)).map(Node::getTextContent).map(guild::getRoleById).collect(Collectors.toSet());
        } finally {
            getDocumentLock().readLock().unlock();
        }
    }
}
