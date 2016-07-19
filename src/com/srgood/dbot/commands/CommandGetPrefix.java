package com.srgood.dbot.commands;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.srgood.dbot.BotMain;

import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class CommandGetPrefix implements Command {

	@Override
	public boolean called(String[] args, MessageReceivedEvent event) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void action(String[] args, MessageReceivedEvent event) {
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
		return null;
	}

	@Override
	public void executed(boolean success, MessageReceivedEvent event) {
		// TODO Auto-generated method stub
		return;
	}

}
