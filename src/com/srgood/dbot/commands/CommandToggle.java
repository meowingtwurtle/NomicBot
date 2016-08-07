package com.srgood.dbot.commands;

import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.srgood.dbot.BotMain;
import com.srgood.dbot.utils.Permissions;
import com.srgood.dbot.utils.XMLUtils;

import net.dv8tion.jda.Permission;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;

public class CommandToggle implements Command {
	String help = "Toggles a commands state to on or off use:'" + BotMain.prefix + "toggle'";
	
	@Override
	public boolean called(String[] args, GuildMessageReceivedEvent event) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void action(String[] args, GuildMessageReceivedEvent event) {
		// TODO Auto-generated method stub
		if (args.length > 0) {
			if (BotMain.commands.containsKey(args[0].toLowerCase())) {
				 Element commandsElement = (Element) ((Element) BotMain.servers.get(event.getGuild().getId()))
		                    .getElementsByTagName("commands").item(0);
		            
		            List<Node> commandList = XMLUtils.nodeListToList(commandsElement.getElementsByTagName("command"));
		            
		            for (Node n : commandList) {
		                Element elem = (Element) n;
		                if (elem.getAttribute("name").equals(args[0].toLowerCase())) {
		                	System.out.println("" + Boolean.parseBoolean(elem.getLastChild().getTextContent().trim()));
		                	if (Boolean.parseBoolean(elem.getElementsByTagName("isEnabled").item(0).getTextContent())) {
		                		elem.getElementsByTagName("isEnabled").item(0).setTextContent("false");
		                	} else {
		                		elem.getElementsByTagName("isEnabled").item(0).setTextContent("true");
		                	}
		                	event.getChannel().sendMessage("Toggled " + args[0]);
		                }
		            }
			}
		} else {
			event.getChannel().sendMessage("Please specify a command to toggle");
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

	}

	@Override
	public Permissions permissionLevel(Guild guild) {
		// TODO Auto-generated method stub
		return Command.getPermissionXML(guild, this);
	}

	@Override
	public Permissions defaultPermissionLevel() {
		// TODO Auto-generated method stub
		return Permissions.ADMINISTRATOR;
	}

}
