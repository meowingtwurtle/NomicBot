package com.srgood.dbot.commands;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.srgood.dbot.BotMain;

import net.dv8tion.jda.entities.Channel;
import net.dv8tion.jda.entities.MessageChannel;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class CommandDelete implements Command {
	
private final String help = "Deletes Messages Use: '" + BotMain.prefix + "delete [all|bot] [channel name]' Default is all in current channel";

	@Override
	public boolean called(String[] args, MessageReceivedEvent event) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void action(String[] args, MessageReceivedEvent event) {
		
		
		String channel = null,delType = null;
		List<String> messages = new ArrayList<String>();
		
		
		if (args.length >= 1) {
			delType = args[0].toLowerCase();
			if (args.length >= 2) {
				channel = BotMain.cleanFileName(args[1]);
			} else {
				channel = BotMain.cleanFileName(event.getTextChannel().getName());
			}
		} else {
			delType = "all";
			channel = BotMain.cleanFileName(event.getTextChannel().getName());
		}
		
		
		
		if (delType.equals("all")) {
			File dir = new File("messages\\guilds\\" + BotMain.cleanFileName(event.getGuild().getName()) + "\\" + channel + "\\all\\");
			File[] directoryListing = dir.listFiles();
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
		} else if(delType.equals("bot")){
			File dir = new File("messages\\guilds\\" + BotMain.cleanFileName(event.getGuild().getName()) + "\\" + channel + "\\bot\\");
			File[] directoryListing = dir.listFiles();
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
		}
		  
		event.getTextChannel().deleteMessagesByIds(messages);
		event.getChannel().sendMessage("Successfully Deleted **" + messages.size() + "** messages");
		  
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
