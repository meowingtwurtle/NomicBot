package com.derek.commands;

import com.derek.Command;
import com.derek.Main;

import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class GetPrefix implements Command {

	@Override
	public boolean called(String[] args, MessageReceivedEvent event) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void action(String[] args, MessageReceivedEvent event) {
		// TODO Auto-generated method stub
		event.getChannel().sendMessage(Main.servers.get(event.getGuild().getId()).get("prefix"));
	}

	@Override
	public String help() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void executed(boolean success, MessageReceivedEvent event) {
		// TODO Auto-generated method stub
		return;
	}

}
