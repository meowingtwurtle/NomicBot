package com.srgood.reasons.utils;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Role;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by srgood on 1/18/2017.
 */
public class RoleUtils {

    public static List<Role> getRolesByName(Guild guild,String name) {
        return getRolesByName(guild.getRoles(),name);
    }

    public static List<Role> getRolesByName(List<Role> roleList,String name) {
        return roleList.stream().filter(role -> role.getName().equalsIgnoreCase(name)).collect(Collectors.toCollection(LinkedList::new));
    }
}
