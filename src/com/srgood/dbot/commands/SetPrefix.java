package com.srgood.dbot.commands;

import com.srgood.dbot.Command;
import com.srgood.dbot.Main;

import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class SetPrefix implements Command{
	private final String help = "Sets the global prefix Use: " + Main.prefix + "setprefix [prefix]";
	
	@Override
	public boolean called(String[] args, MessageReceivedEvent event) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void action(String[] args, MessageReceivedEvent event) {
		// TODO Auto-generated method stub
		try {
			Main.servers.get(event.getGuild().getId()).put("prefix",args[0].toString());
			event.getChannel().sendMessage("Prefix set to: " + args[0].toString());
		} catch (Exception e){
			event.getChannel().sendMessage(help().toString());
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
