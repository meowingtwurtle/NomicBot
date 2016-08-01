package com.srgood.dbot.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.Role;
import net.dv8tion.jda.entities.User;

public class PermissionOps {

    public static List<Permissions> getPermissions(Guild guild, User user) {
        List<Permissions> roles = new ArrayList<Permissions>();
        
    	for (Role i : guild.getRolesForUser(user)) {
        	roles.add(roleToPermission(i));
        }
    	
    	return roles;
    }
    
    public static Permissions roleToPermission(Role role) {
    	Permissions permission = Permissions.ADMINISTRATOR;
    	for (Permissions permLevel : Permissions.values()) {
    		if (permLevel.readableName.equals(role.getName())) {
    			permission = permLevel;
    		}
    	}
    	return permission;
    }

    public static Permissions getHighestPermission(List<Permissions> Roles, Guild guild) {
        
        Permissions maxFound = Permissions.STANDARD;

        for (Permissions permLevel : Permissions.values()) {
            if ((permLevel.getLevel() > maxFound.getLevel())) {
                if (containsAny(XMLUtils.getGuildRolesFromInternalName(permLevel.getXMLName(), guild), Roles)) {
                    maxFound = permLevel;
                    break;
                }
            }
        }
        return maxFound;
    }
    
    public static boolean containsAny(Collection<?> container, Collection<?> checkFor) {
        for (Object o : checkFor) {
            if (container.contains(o)) {
                return true;
            }
        }
        return false;
    }
    

}
