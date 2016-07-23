package com.srgood.dbot.commands;

import java.util.Set;

import com.srgood.dbot.BotMain;

import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class CommandHelp implements Command {
	private final String help = "Lists all commands. Use: '" + BotMain.prefix + "help'";
	@Override
	public boolean called(String[] args, MessageReceivedEvent event) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void action(String[] args, MessageReceivedEvent event) {
		// TODO Auto-generated method stub
		
		Set<String> v = BotMain.commands.keySet();
		StringBuilder z = new StringBuilder();
		z.append("All commands: \n");
		
		
		
		for (String i : v) {
			String output = i.substring(0, 1).toUpperCase() + i.substring(1);
			z.append("**" + output + ":** " + "  `" +BotMain.commands.get(i).help() + "`" + "\n\n");
		}
		
		
		
		event.getAuthor().getPrivateChannel().sendMessage(z.toString());
		event.getChannel().sendMessage("Commands were set to you in a private message");
		
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
