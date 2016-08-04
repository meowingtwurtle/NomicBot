package com.srgood.dbot.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.srgood.dbot.BotMain;

import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.Role;
import net.dv8tion.jda.entities.User;

public class PermissionOps {

    public static Collection<Permissions> getPermissions(Guild guild, User user) {
    	return rolesToPermissions(guild.getRolesForUser(user), guild);
    }
    
    public static Permissions roleToPermission(Role role, Guild guild) {
    	Permissions permission = Permissions.STANDARD;
    	
    	if (role == null) {
    	    return permission;
    	}
    	
    	//<config>
    	//  <servers>
    	//    <server>
    	//      <roles>
    	//        <role>
    	
    	// <server>
    	Element serverElem = (Element) BotMain.servers.get(guild.getId());
    	
    	Element rolesElem = (Element) serverElem.getElementsByTagName("role").item(0);
    	
    	List<Node> roleNodeList = XMLUtils.nodeListToList(rolesElem.getElementsByTagName("role"));
    	
    	String roleID = role.getId();
    	
    	for (Node n : roleNodeList) {
    	    Element roleElem = (Element) n;
    	    String roleXMLName = roleElem.getAttribute("name");
    	    
    	    if (!roleID.equals(roleElem.getTextContent())) {
    	        continue;
    	    }
    	    
    	    for (Permissions permLevel : Permissions.values()) {
    	        if (permLevel.getLevel() >= permission.getLevel() && permLevel.getXMLName().equals(roleXMLName)) {
    	            permission = permLevel;
    	        }
    	    }
    	}
    	
    	return permission;
    }

    public static Permissions getHighestPermission(Collection<Permissions> Roles, Guild guild) {
        
        Permissions maxFound = Permissions.STANDARD;

        for (Permissions permLevel : Permissions.values()) {
            if ((permLevel.getLevel() > maxFound.getLevel())) {
                if (containsAny(rolesToPermissions(XMLUtils.getGuildRolesFromInternalName(permLevel.getXMLName(), guild), guild), Roles)) {
                    maxFound = permLevel;
                    break;
                }
            }
        }
        return maxFound;
    }
    
    public static Collection<Permissions> rolesToPermissions(Collection<? extends Role> roles, Guild guild) {
        List<Permissions> ret = new ArrayList<Permissions>();
        for (Role role : roles) {
        	ret.add(roleToPermission(role, guild));
        }
        return ret;
    }
    
    public static boolean containsAny(Collection<?> container, Collection<?> checkFor) {
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

}
