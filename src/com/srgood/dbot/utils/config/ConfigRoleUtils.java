package com.srgood.dbot.utils.config;

import com.srgood.dbot.PermissionLevels;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.Role;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

class ConfigRoleUtils {
    private static Element getRolesElement(Guild guild) {
        return ConfigGuildUtils.getGuildComplexPropertyElement(guild, "roles");
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

    static boolean guildHasRoleForPermission(Guild guild, PermissionLevels roleLevel) {

        List<Node> roleElementList = getRoleNodeListFromGuild(guild);

        for (Node n : roleElementList) {
            Element roleElem = (Element) n;
            if (roleElem.getAttribute("name").equals(roleLevel.getXMLName())) {
                return true;
            }
        }
        return false;
    }

    static void registerRoleConfig(Guild guild, Role role, PermissionLevels roleLevel) {
        Element elementRoles = ConfigBasicUtils.getFirstSubElement(ConfigGuildUtils.getGuildNode(guild), "roles");

        Element elementRole = ConfigBasicUtils.getDocument().createElement("role");
        Attr roleAttr = ConfigBasicUtils.getDocument().createAttribute("name");
        roleAttr.setValue(roleLevel.getXMLName());
        elementRole.setAttributeNode(roleAttr);
        elementRole.setTextContent(role.getId());

        elementRoles.appendChild(elementRole);
    }

    static void deregisterRoleConfig(Guild guild, String roleID) {
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
    }

    static Set<String> getGuildRegisteredRoleIDs(Guild guild) {
        return getRoleNodeListFromGuild(guild).stream().map(Node::getTextContent).collect(Collectors.toSet());
    }

    static Set<Role> getGuildRolesFromPermissionLevel(Guild guild, PermissionLevels permissionLevel) {
        String permissionName = permissionLevel.getXMLName();
        return getRoleNodeListFromGuild(guild)
                .stream()
                .filter(n -> n instanceof Element).map(n -> (Element) n)
                .filter(elem -> elem.getAttribute("name").equals(permissionName))
                .map(Node::getTextContent)
                .map(guild::getRoleById)
                .collect(Collectors.toSet());
    }
}
