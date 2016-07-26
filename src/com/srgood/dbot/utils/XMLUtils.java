package com.srgood.dbot.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.srgood.dbot.BotMain;

import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.Role;

public class XMLUtils {
    public static Set<Role> getGuildRolesFromInternalName(String internalName, Guild guild) {
        Element serverElem = (Element) BotMain.servers.get(guild.getId());
        Element rolesElem = (Element) serverElem.getElementsByTagName("roles").item(0);
        List<Node> roleNodes = nodeListToList(rolesElem.getElementsByTagName("role"));
        
        Set<Role> ret = new HashSet<>();
        
        for (Node i : roleNodes) {
            if (internalName.equals(((Attr) i.getAttributes().getNamedItem("name")).getValue())) {
                ret.add(guild.getRoleById(i.getTextContent()));
            }
        }
        
        return ret;
    }
    
    public static List<Node> nodeListToList(NodeList nl) {
        List<Node> ret = new ArrayList<>();
        
        for (int i = 0; i < nl.getLength(); i++) {
            ret.add(nl.item(i));
        }
        
        return ret;
    }
}
