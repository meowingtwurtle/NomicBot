package com.srgood.dbot.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.srgood.dbot.BotMain;

import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.Role;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.managers.RoleManager;

public class PermissionOps {

    public static Collection<Permissions> getPermissions(Guild guild, User user) {
    	return rolesToPermissions(guild.getRolesForUser(user), guild);
    }
    
<<<<<<< HEAD
=======
    public static Permissions roleToPermission(Role role, Guild guild) {
    	Permissions permission = null;
    	
    	if (role == null) {
    	    return permission;
    	}
    	
    	//<config>
    	//  <servers>
    	//    <server>
    	//      <roles>
    	//        <role>
    	
    	// <server>

    	
    	List<Node> roleNodeList = XMLHandler.getRoleNodeListFromGuild(guild);
    	
    	String roleID = role.getId();
    	
    	for (Node n : roleNodeList) {
    	    Element roleElem = (Element) n;
    	    String roleXMLName = roleElem.getAttribute("name");
    	    
    	    if (!roleID.equals(roleElem.getTextContent())) {
    	        continue;
    	    }
    	    
    	    for (Permissions permLevel : Permissions.values()) {
    	        if (permLevel.getLevel() >= (permission == null ? Permissions.STANDARD : permission).getLevel() && permLevel.getXMLName().equals(roleXMLName)) {
    	            permission = permLevel;
    	        }
    	    }
    	}
    	
    	return permission;
    }

>>>>>>> 6f1545b6bd89c070e93f166e2a52b5b1242ef23b
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
    
    public static Collection<Permissions> rolesToPermissions(Collection<? extends Role> roles, Guild guild) {
        List<Permissions> ret = new ArrayList<Permissions>();
        for (Role role : roles) {
        	ret.add(XMLHandler.roleToPermission(role, guild));
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
    
    public static void createRoleIfNotExists(Permissions roleLevel, Guild guild) {
        if (rolesToPermissions(guild.getRoles(), guild).contains(roleLevel)) {return;}
        
        if (!roleLevel.isVisible()) return;
        
        if (XMLHandler.guildHasRoleForPermission(guild, roleLevel)) {
            return;
        }
        
        createRole(roleLevel, guild, true);
        
    }
    
    public static Role createRole(Permissions roleLevel, Guild guild, boolean addToXML) {
        if (!roleLevel.isVisible())
            return null;

        RoleManager roleMgr = guild.createRole();
        roleMgr.setName(roleLevel.getReadableName());
        roleMgr.setColor(roleLevel.getColor());

        roleMgr.update();

        Role role = roleMgr.getRole();

        if (addToXML) {
            XMLHandler.registerRoleXML(guild, role, roleLevel);
        }

        

        return role;
    }

}
