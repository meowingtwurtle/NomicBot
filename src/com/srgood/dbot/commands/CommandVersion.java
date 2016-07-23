package com.srgood.dbot.commands;

import com.srgood.dbot.ref.RefStrings;

import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class CommandVersion implements Command {

	@Override
	public boolean called(String[] args, MessageReceivedEvent event) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void action(String[] args, MessageReceivedEvent event) {
		// TODO Auto-generated method stub
		
		//see http://stackoverflow.com/questions/396429/how-do-you-know-what-version-number-to-use
		if (args.length >= 1) {
			if (args[0].equalsIgnoreCase("notes")) {
				//TODO add an XML field for past and current Release notes. I.E. <Notss ver= 0.1.2>Added Version command</Notes>
			} else {
				
			}
		} else {
			event.getChannel().sendMessage("The current version is:" + RefStrings.VERSION);
		}
	}

	@Override
	public String help() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void executed(boolean success, MessageReceivedEvent event) {
		// TODO Auto-generated method stub

	}

}
