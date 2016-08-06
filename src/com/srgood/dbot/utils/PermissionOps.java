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

    	
    	List<Node> roleNodeList = XMLUtils.getRoleNodeListFromGuild(guild);
    	
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
    
    public static void createRoleIfNotExists(Permissions roleLevel, Guild guild) {
        if (rolesToPermissions(guild.getRoles(), guild).contains(roleLevel)) {return;}
        
        if (!roleLevel.isVisible()) return;
        
        Element serverElement = (Element) BotMain.servers.get(guild.getId());
        
        Element rolesElement = (Element) serverElement.getElementsByTagName("roles");
        
        List<Node> roleElementList = ( XMLUtils.nodeListToList(rolesElement.getElementsByTagName("role")));
        
        
        for (Node n : roleElementList) {
            Element roleElem = (Element) n;
            if (roleElem.getAttribute("name").equals(roleLevel.getXMLName())) {
                return;
            }
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

        if (!addToXML) {
            return role;
        }

        Element elementRoles = (Element) ((Element) BotMain.servers.get(guild.getId())).getElementsByTagName("roles").item(0);
        
        Element elementRole = BotMain.PInputFile.createElement("role");
        Attr roleAttr = BotMain.PInputFile.createAttribute("name");
        roleAttr.setValue(roleLevel.getXMLName());
        elementRole.setAttributeNode(roleAttr);
        elementRole.setTextContent(role.getId());

        elementRoles.appendChild(elementRole);

        return role;
    }

}
