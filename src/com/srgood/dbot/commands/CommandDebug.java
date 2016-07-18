package com.srgood.dbot.commands;

import com.srgood.dbot.Main;

import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class CommandDebug implements Command {
	private final String help = "Used internally for debugging. Use: " + Main.prefix + "debug [debug arg]";
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
			"Main.jda.getSelfInfo().getAsMention().length()" + Main.jda.getSelfInfo().getAsMention().toString().length() + "\n" + 
			"Main.jda.getSelfInfo().getAsMention()" + Main.jda.getSelfInfo().getAsMention());
			
		} catch (Exception e) {
			event.getChannel().sendMessage("Author Name: " + event.getAuthor().getUsername() + "\n" + 
			"Author Nick: " + event.getAuthorNick() + "\n" + 
			"id: " + event.getAuthor().getId() + "\n" + 
			event.getAuthor().getAsMention() + "\n" + 
			"Picture url: " + event.getAuthor().getAvatarUrl().toString() + "\n" + 
			"Main.jda.getSelfInfo().getAsMention().length()" + Main.jda.getSelfInfo().getAsMention().toString().length() + "\n" + 
			"Main.jda.getSelfInfo().getAsMention()" + Main.jda.getSelfInfo().getAsMention());
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
