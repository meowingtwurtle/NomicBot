package com.srgood.dbot.commands;

import com.srgood.dbot.BotMain;

import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class CommandPong implements Command {
	
	private final String help = "Usage: " + BotMain.prefix + "pong";
	
	@Override
	public boolean called(String[] args, MessageReceivedEvent event) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void action(String[] args, MessageReceivedEvent event) {
		// TODO Auto-generated method stub
		event.getChannel().sendMessage("Ping " + event.getAuthor().getAsMention());
		
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
