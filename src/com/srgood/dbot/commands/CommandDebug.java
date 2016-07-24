package com.srgood.dbot.commands;

import java.awt.Color;

import com.srgood.dbot.BotMain;

import net.dv8tion.jda.entities.Role;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.exceptions.PermissionException;
import net.dv8tion.jda.managers.RoleManager;

public class CommandDebug implements Command {
	private final String help = "Used internally for debugging. Use: '" + BotMain.prefix + "debug [debug arg]'";
	private boolean exe = true;
	@Override
	public boolean called(String[] args, MessageReceivedEvent event) {
		// TODO Auto-generated method stub
		return exe;
	}

	@Override
	public void action(String[] args, MessageReceivedEvent event) {
		// TODO Auto-generated method stub
		try {
			event.getChannel().sendMessage(args[0].toString());
			event.getChannel().sendMessage("Author Name: " + event.getAuthor().getUsername() + "\n" + 
			"Author Nick: " + event.getAuthorNick() + "\n" + 
			"id: " + event.getAuthor().getId() + "\n" + 
			event.getAuthor().getAsMention() + "\n" + 
			"Picture url: " + event.getAuthor().getAvatarUrl().toString() + "\n" + 
			"BotMain.jda.getSelfInfo().getAsMention().length()" + BotMain.jda.getSelfInfo().getAsMention().toString().length() + "\n" + 
			"BotMain.jda.getSelfInfo().getAsMention()" + BotMain.jda.getSelfInfo().getAsMention());
			
		} catch (Exception e) {
			event.getChannel().sendMessage("Author Name: " + event.getAuthor().getUsername() + "\n" + 
			"Author Nick: " + event.getAuthorNick() + "\n" + 
			"id: " + event.getAuthor().getId() + "\n" + 
			event.getAuthor().getAsMention() + "\n" + 
			"Picture url: " + event.getAuthor().getAvatarUrl().toString() + "\n" + 
			"BotMain.jda.getSelfInfo().getAsMention().length()" + BotMain.jda.getSelfInfo().getAsMention().toString().length() + "\n" + 
			"BotMain.jda.getSelfInfo().getAsMention()" + BotMain.jda.getSelfInfo().getAsMention());
			try {
				RoleManager role = event.getGuild().createRole();
				role.setName("Reasons Admin");
				role.setColor(Color.GREEN);
				
			} catch(PermissionException e3) {
				event.getChannel().sendMessage("\n\n\n you done messed up\n" + e.getMessage());
			}

			
			
			
		}
		
	}

	@Override
	public String help() {
		// TODO Auto-generated method stub
		return help;
	}

	@Override
	public void executed(boolean success, MessageReceivedEvent event) {
		// TODO Auto-generated method stub
		return;
	}
	
}
