package com.srgood.dbot.commands;

import java.util.Set;

import com.srgood.dbot.BotMain;
import com.srgood.dbot.utils.Permissions;
import com.srgood.dbot.utils.XMLHandler;

import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;

public class CommandHelp implements Command {
	private final String help = "Lists all commands. Use: '" + BotMain.prefix + "help'";
	@Override
	public boolean called(String[] args, GuildMessageReceivedEvent event) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void action(String[] args, GuildMessageReceivedEvent event) {
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
	public void executed(boolean success, GuildMessageReceivedEvent event) {
		// TODO Auto-generated method stub
		return;
	}
	
    @Override
    public Permissions permissionLevel(Guild guild) {
        // TODO Auto-generated method stub
        return XMLHandler.getCommandPermissionXML(guild, this);
    }

    @Override
    public Permissions defaultPermissionLevel() {
        // TODO Auto-generated method stub
        return Permissions.STANDARD;
    }

}
