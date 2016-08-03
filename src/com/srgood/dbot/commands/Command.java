package com.srgood.dbot.commands;



import com.srgood.dbot.utils.Permissions;

import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;

public interface Command {

		public boolean called(String[] args, GuildMessageReceivedEvent event); 
		public void action(String[] args, GuildMessageReceivedEvent event);
		public String help();
		public void executed(boolean success, GuildMessageReceivedEvent event);
		public Permissions permissionLevel(Guild guild);
		public Permissions defaultPermissionLevel();
		
		static Permissions getSelfPermissionXML(Guild guild,Command command) {
			command.getClass()
			return null;
		}
}
