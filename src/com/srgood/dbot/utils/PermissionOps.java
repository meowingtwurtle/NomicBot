package com.srgood.dbot.utils;

import java.util.Collection;
import java.util.List;

import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.Role;
import net.dv8tion.jda.entities.User;

public class PermissionOps {

    public List<Role> getPermissions(Guild guild, User user) {
        return guild.getRolesForUser(user);
    }

    public Permissions getHighestPermission(Guild guild, User user) {
        List<Role> roles = getPermissions(guild, user);
        
        Permissions maxFound = Permissions.STANDARD;

        for (Permissions permLevel : Permissions.values()) {
            if ((permLevel.getLevel() > maxFound.getLevel())) {
                if (containsAny(XMLUtils.getGuildRolesFromInternalName(permLevel.getXMLName(), guild), getPermissions(guild, user))) {
                    maxFound = permLevel;
                    break;
                }
            }
        }
        return maxFound;
    }
    
    private boolean containsAny(Collection<?> container, Collection<?> checkFor) {
        for (Object o : checkFor) {
            if (container.contains(o)) {
                return true;
            }
        }
        return false;
    }
}
