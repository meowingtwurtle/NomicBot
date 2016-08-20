package com.srgood.dbot.commands;



import com.srgood.dbot.utils.Permissions;

import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;

public interface Command {

		boolean called(String[] args, GuildMessageReceivedEvent event);
		 void action(String[] args, GuildMessageReceivedEvent event);
		 String help();
		 void executed(boolean success, GuildMessageReceivedEvent event);
		 Permissions permissionLevel(Guild guild);
		 Permissions defaultPermissionLevel();
}
