package com.srgood.dbot.commands;

import org.w3c.dom.Element;

import com.srgood.dbot.BotMain;
import com.srgood.dbot.utils.Permissions;
import com.srgood.dbot.utils.XMLHandler;

import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;

public class CommandSetPrefix implements Command{
	private final String help = "Sets the global prefix Use: '" + BotMain.prefix + "setprefix <prefix>'";
	
	@Override
	public boolean called(String[] args, GuildMessageReceivedEvent event) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void action(String[] args, GuildMessageReceivedEvent event) {
		try {
		    XMLHandler.setGuildPrefix(event.getGuild(), args[0]);
			event.getChannel().sendMessage("Prefix set to: " + args[0]);
		} catch (Exception e) {
			event.getChannel().sendMessage(help().toString());
			e.printStackTrace();
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
	public Permissions permissionLevel(Guild guild) {
		// TODO Auto-generated method stub
		return XMLHandler.getCommandPermissionXML(guild, this);
	}

	@Override
	public Permissions defaultPermissionLevel() {
		// TODO Auto-generated method stub
		return Permissions.ADMINISTRATOR;
	}

}
