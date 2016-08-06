package com.srgood.dbot.commands;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.srgood.dbot.BotMain;
import com.srgood.dbot.utils.Permissions;

import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
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
		// TODO Auto-generated method stub
		if (BotMain.servers.containsKey(event.getGuild().getId())) {
			Node ServerNode = BotMain.servers.get(event.getGuild().getId());
			Element NodeElement = (Element)ServerNode;
			event.getChannel().sendMessage("Prefix: " + NodeElement.getElementsByTagName("prefix").item(0).getTextContent());
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
		return Command.getPermissionXML(guild, this);
	}

	@Override
	public Permissions defaultPermissionLevel() {
		// TODO Auto-generated method stub
		return Permissions.STANDARD;
	}

}
