package com.srgood.dbot.commands;

import java.util.Collection;

import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;

public interface Command {

		public boolean called(String[] args, GuildMessageReceivedEvent event); 
		public void action(String[] args, GuildMessageReceivedEvent event);
		public String help();
		public void executed(boolean success, GuildMessageReceivedEvent event);
		public Collection<?> permissionLevels();
}
