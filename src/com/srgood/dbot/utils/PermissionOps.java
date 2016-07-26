package com.srgood.dbot.utils;

import java.util.List;

import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.Role;
import net.dv8tion.jda.entities.User;

public class PermissionOps {
	
	public List<Role> getPermissions(Guild guild, User user) {
		return guild.getRolesForUser(user);
	}
	
	public int getHighestPermission(Guild guild, User user) {
		List<Role> roles = getPermissions(guild,user);
		for (Role role : roles) {
			role.getName()
		}
	}
}
