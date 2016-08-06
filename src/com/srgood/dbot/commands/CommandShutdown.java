package com.srgood.dbot.commands;

import javax.xml.transform.TransformerException;

import com.srgood.dbot.BotMain;
import com.srgood.dbot.utils.Permissions;

import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;

public class CommandShutdown implements Command {
	private final String help = "Used to shutdown Reasons. Use: '" + BotMain.prefix + "shutdown' -OR- '" + BotMain.prefix + "shutdown override [override key]'";
	@Override
	public boolean called(String[] args, GuildMessageReceivedEvent event) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void action(String[] args, GuildMessageReceivedEvent event) {
		long uid = Long.parseLong(event.getAuthor().getId());
		
		
		try {
			if (164117897025683456L == uid || 138048665112543233L == uid) {
				event.getChannel().sendMessage("Shutting down! " + event.getAuthor().getAsMention());
				try {
					BotMain.WriteXML();
				} catch (TransformerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				BotMain.jda.shutdown();
			} else {
				if (args[0].toLowerCase().equals("override")) {
					if (BotMain.Okey.equals(args[1])) {
						event.getChannel().sendMessage("Valid key. Shutting down! " + event.getAuthor().getAsMention());
						try {
							BotMain.WriteXML();
						} catch (TransformerException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						BotMain.jda.shutdown();
					} else {
						event.getChannel().sendMessage("Bad key " + event.getAuthor().getAsMention());
					}
				} else {
					
				}
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			try {
				if (args[0].toLowerCase().equals("override")) {
					event.getChannel().sendMessage("Invalid Arguments, you should quit the debate team " + event.getAuthor().getAsMention());
				}
			} catch (ArrayIndexOutOfBoundsException ex) {
				event.getChannel().sendMessage("You arent me. you cant do that " + event.getAuthor().getAsMention());
			}
			
		}
		
	}

	@Override
	public String help() {
		// TODO Auto-generated method stub
		return help;
	}

	@Override
	public void executed(boolean success, GuildMessageReceivedEvent event) {
		// TODO Auto-generated method stub
		return;
	}

    @Override
    public Permissions defaultPermissionLevel() {
        // TODO Auto-generated method stub
        return Permissions.ADMINISTRATOR;
    }

	@Override
	public Permissions permissionLevel(Guild guild) {
		// TODO Auto-generated method stub
		return Command.getPermissionXML(guild, this);
	}
	
}
