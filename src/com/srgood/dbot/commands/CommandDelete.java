package com.srgood.dbot.commands;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.dv8tion.jda.entities.Channel;
import net.dv8tion.jda.entities.MessageChannel;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class CommandDelete implements Command {

	@Override
	public boolean called(String[] args, MessageReceivedEvent event) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void action(String[] args, MessageReceivedEvent event) {
		
		String channel = null;
		
		if (args.length >= 1) {
			channel = args[0];
		} else {
			channel = event.getChannel().getId();
		}
		
		File dir = new File("messages\\guilds\\" + event.getGuild().getName()+ "\\" + channel + "\\all\\");
		File[] directoryListing = dir.listFiles();
		List<String> messages = new ArrayList<String>();
		if (directoryListing != null) {
			for (File child : directoryListing) {
				try {
					messages.add(child.getName().replace(".ser", ""));
					child.delete();
				} catch (Exception e) {
					  
				}

		    }
		} else {
			dir.mkdirs();
		}
		  
		event.getTextChannel().deleteMessagesByIds(messages);
		  
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
