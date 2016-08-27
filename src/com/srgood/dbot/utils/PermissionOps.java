package com.srgood.dbot.utils;

import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.Role;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.managers.RoleManager;

import java.util.Collection;

public class PermissionOps {

    public static Collection<Permissions> getPermissions(Guild guild, User user) {
        return rolesToPermissions(guild.getRolesForUser(user), guild);
    }


    public static Permissions getHighestPermission(Collection<Permissions> Roles, Guild guild) {

        Permissions maxFound = Permissions.STANDARD;

        for (Permissions permLevel : Permissions.values()) {
            if ((permLevel.getLevel() > maxFound.getLevel())) {
                if (containsAny(rolesToPermissions(XMLHandler.getGuildRolesFromInternalName(permLevel.getXMLName(), guild), guild), Roles)) {
                    maxFound = permLevel;
                    break;
                }
            }
        }
        return maxFound;
    }

    private static Collection<Permissions> rolesToPermissions(Collection<? extends Role> roles, Guild guild) {
        return roles.stream().map(role -> XMLHandler.roleToPermission(role, guild)).collect(java.util.stream.Collectors.toList());
    }

    private static boolean containsAny(Collection<?> container, Collection<?> checkFor) {
        for (Object o : checkFor) {
            if (container.contains(o)) {
                return true;
            }
        }
        return false;
    }

    public static Permissions intToEnum(int level) {
        for (Permissions p : Permissions.values()) {
            if (p.level == level) {
                return p;
            }
        }
        return null;
    }

    public static Role createRole(Permissions roleLevel, Guild guild, boolean addToXML) {
        if (!roleLevel.isVisible()) return null;

        RoleManager roleMgr = guild.createRole();
        roleMgr.setName(roleLevel.getReadableName());
        roleMgr.setColor(roleLevel.getColor());

        roleMgr.update();

        Role role = roleMgr.getRole();

        if (addToXML) {
            XMLHandler.registerRole(guild, role, roleLevel);
        }


        return role;
    }

}
