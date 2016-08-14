package com.srgood.dbot.commands;

import com.srgood.dbot.BotMain;
import com.srgood.dbot.utils.Permissions;
import com.srgood.dbot.utils.XMLHandler;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;

public class CommandGetPrefix implements Command {

	private final String help = "Prints the current Prefix Use: '" + BotMain.prefix + "getprefix'";
	
	@Override
	public boolean called(String[] args, GuildMessageReceivedEvent event) {
		// TODO Auto-generated method stub
		return true;
	}

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) {
        event.getChannel().sendMessage("Prefix: " + XMLHandler.getGuildPrefix(event.getGuild()));
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
