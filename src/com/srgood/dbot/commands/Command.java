package com.srgood.dbot.commands;



import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.srgood.dbot.BotMain;
import com.srgood.dbot.utils.PermissionOps;
import com.srgood.dbot.utils.Permissions;
import com.srgood.dbot.utils.XMLUtils;

import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;

public interface Command {

		public boolean called(String[] args, GuildMessageReceivedEvent event); 
		public void action(String[] args, GuildMessageReceivedEvent event);
		public String help();
		public void executed(boolean success, GuildMessageReceivedEvent event);
		public Permissions permissionLevel(Guild guild);
		public Permissions defaultPermissionLevel();
		
		public static Permissions getPermissionXML(Guild guild, Command command) {


        String commandName = null;

        for (String e : BotMain.commands.keySet()) {
            if (BotMain.commands.get(e) == command) {
                commandName = e;
            }
        }

        if (BotMain.commands.values().contains(command)) {
            Element commandsElement = (Element) ((Element) BotMain.servers.get(guild.getId()))
                    .getElementsByTagName("commands").item(0);
            
            List<Node> commandList = XMLUtils.nodeListToList(commandsElement.getElementsByTagName("command"));
            
            for (Node n : commandList) {
                Element elem = (Element) n;
                if (elem.getAttribute("name").equals(commandName)) {
                    return PermissionOps.intToEnum(Integer.parseInt(elem.getTextContent().trim()));
                }
            }
        }

        return null;
    }
}
