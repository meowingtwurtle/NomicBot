package com.derek.utils;

import java.util.ArrayList;

import com.derek.BotListener;
import com.derek.Main;

import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class CommandParser {
	//creates a new method of type CommandContainer which is used for parsing
	public CommandContainer parse(String rw, MessageReceivedEvent e) {
		ArrayList<String> split = new ArrayList<String>();
		String raw = rw,beheaded;
		try {
			if (e.getJDA().getSelfInfo().getAsMention().equals(e.getMessage().getMentionedUsers().get(0).getAsMention())) {
				beheaded = e.getMessage().getRawContent().replaceFirst(e.getJDA().getSelfInfo().getAsMention() + " ","");
				
				System.out.println(e.getMessage().getRawContent());
				System.out.println(e.getJDA().getSelfInfo().getAsMention());
				System.out.println(beheaded);
			} else {
				beheaded = raw.replaceFirst(BotListener.localPrefix,"");
			}
		} catch (IndexOutOfBoundsException ex) {
			beheaded = raw.replaceFirst(BotListener.localPrefix,"");
		}
		
		
		String[] splitBeheaded = beheaded.split(" ");
		for (String s : splitBeheaded) {
			split.add(s);
		}
		String invoke = split.get(0);
		String[] args = new String[split.size() - 1];
		split.subList(1, split.size()).toArray(args);
		
		return new CommandContainer(raw,beheaded,splitBeheaded,invoke,args,e);
	}
	   
	public class CommandContainer {
		public final String raw;
		public final String beheaded;
		public final String[] splitBeheaded;
		public final String invoke;
		public final String[] args;
		public final MessageReceivedEvent event;
		
		public CommandContainer(String rw, String beheaded, String[] splitBeheaded,String invoke,String[] args,MessageReceivedEvent e){
			this.raw = rw;
			this.beheaded = beheaded;
			this.splitBeheaded = splitBeheaded;
			this.invoke = invoke;
			this.args = args;
			this.event = e;
					
		}
	}
}
