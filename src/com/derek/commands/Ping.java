package com.derek.commands;

import com.derek.Command;
import com.derek.Main;

import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class Ping implements Command {
	
	private final String help = "Usage: " +Main.prefix+ "ping";
	
	@Override
	public boolean called(String[] args, MessageReceivedEvent event) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void action(String[] args, MessageReceivedEvent event) {
		// TODO Auto-generated method stub
		event.getChannel().sendMessage("Pong " + event.getAuthor().getAsMention());
		
		
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
