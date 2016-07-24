package com.srgood.dbot;

import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.srgood.dbot.ref.RefStrings;

import net.dv8tion.jda.audio.player.Player;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.ReadyEvent;
import net.dv8tion.jda.events.guild.GuildJoinEvent;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.exceptions.PermissionException;
import net.dv8tion.jda.hooks.ListenerAdapter;
import net.dv8tion.jda.managers.RoleManager;
import net.dv8tion.jda.utils.SimpleLog;
import net.dv8tion.jda.utils.SimpleLog.Level;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * <h1>Bot Listener</h1>
 * 
 * Bot Listener deals with MessageRecieved events, excluding its own.
 * 
 * @author srgood
 * @version 0.8
 * @since 7/12/16
 */

public class BotListener extends ListenerAdapter {
	public static String localPrefix;
	public static Map<String,Player> players = new HashMap<>();
	
		@Override
		public void onMessageReceived(MessageReceivedEvent event){
			

			if (BotMain.servers.containsKey(event.getGuild().getId())) {
				Node ServerNode = BotMain.servers.get(event.getGuild().getId());
				Element NodeElement = (Element)ServerNode;
				localPrefix = NodeElement.getElementsByTagName("prefix").item(0).getTextContent();
			} else {				
				Element server = BotMain.PInputFile.createElement("server");
				
				initGuild(event.getGuild());
				
				localPrefix = server.getElementsByTagName("prefix").item(0).getTextContent();
			}
			
			
			
			if (event.getMessage().getContent().equals(RefStrings.TABLE_FLIP)) {
				event.getChannel().sendMessage(RefStrings.TABLE_UNFLIP_JOKE);
			}
			
			BotMain.StoreMessage(event,BotMain.servers.get(event.getGuild().getId()));
			
			
			if(event.getMessage().getContent().startsWith(localPrefix) && event.getMessage().getAuthor().getId() != event.getJDA().getSelfInfo().getId()){
				BotMain.handleCommand(BotMain.parser.parse(event.getMessage().getContent().toLowerCase(),event));
				//if (event.getMessage().getContent().toString().substring(1,7).equals("compile"))
				SimpleLog.getLog("Reasons").info("Got Valid Input");
			} else {
				try {
					
					if(event.getJDA().getSelfInfo().getAsMention().equals(event.getMessage().getMentionedUsers().get(0).getAsMention())) {
						
						SimpleLog.getLog("Reasons").info("Got Valid Input (mention)");
						BotMain.handleCommand(BotMain.parser.parse(event.getMessage().getContent().toLowerCase(),event));
					}
				} catch (Exception e) {
					
				}
			}
			

		
				

				
			
			
		}
		
		@Override
		public void onReady(ReadyEvent event){
			
		}
		
		@Override
		public void onGuildJoin(GuildJoinEvent event) {
//		    initGuild(event.getGuild());
		}
		
		private void initGuild(Guild guild) {
            
            Node root = BotMain.PInputFile.getDocumentElement();
            
            Element server = BotMain.PInputFile.createElement("server");
            
            Element elementServers = (Element) BotMain.PInputFile.getDocumentElement().getElementsByTagName("servers").item(0);
            
            Attr idAttr = BotMain.PInputFile.createAttribute("id");
            idAttr.setValue(guild.getId());
            server.setAttributeNode(idAttr);
            
            Element prefixElement = BotMain.PInputFile.createElement("prefix");
            
            
            prefixElement.appendChild(BotMain.PInputFile.createTextNode(BotMain.prefix));
            
            server.appendChild(prefixElement);
            
            elementServers.appendChild(server);            
            BotMain.servers.put(guild.getId(), server);
            
            try {
                RoleManager role = guild.createRole();
                role.setName(RefStrings.ROLE_NAME_ADMIN_READABLE);
                role.setColor(Color.GREEN);
                role.update();
                
                Element elementRoleContainer = BotMain.PInputFile.createElement("roles");
                Element elementAdminRole = BotMain.PInputFile.createElement("role");
                
                Attr roleAttr = BotMain.PInputFile.createAttribute("name");
                roleAttr.setValue(RefStrings.ROLE_NAME_ADMIN_XML);

                elementAdminRole.setAttributeNode(roleAttr);
                
                elementAdminRole.setTextContent(role.getRole().getId());
                
                elementRoleContainer.appendChild(elementAdminRole);
                
                server.appendChild(elementRoleContainer);
                
            } catch (PermissionException e3) {
                SimpleLog.getLog("Reasons").warn("Could not create custom role! Possible permissions problem?");
            }
		}
}
