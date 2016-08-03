package com.srgood.dbot.commands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.srgood.dbot.BotMain;
import com.srgood.dbot.utils.Permissions;

import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.Role;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;

public class CommandPing implements Command {
	
	private final String help = "Ping! Use: '" +BotMain.prefix + "ping'";
	
	@Override
	public boolean called(String[] args, GuildMessageReceivedEvent event) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void action(String[] args, GuildMessageReceivedEvent event) {
		// TODO Auto-generated method stub
		event.getChannel().sendMessage("Pong " + event.getAuthor().getAsMention());
		
		
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
		
		
		if (BotMain.servers.containsKey(guild.getId())) {
			Node ServerNode = BotMain.servers.get(guild.getId());
			NodeList NodeElement = ServerNode.getChildNodes();
			for (int i = 0; i == NodeElement.getLength(); i++) {
				
				
			}
		}
		
		return;
		// TODO Auto-generated method stub
		
	}

    @Override
    public Permissions defaultPermissionLevel() {
        return Permissions.STANDARD;
    }
	
}
