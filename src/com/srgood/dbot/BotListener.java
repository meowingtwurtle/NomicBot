package com.srgood.dbot;
 
import java.util.HashMap;
import java.util.Map;
 
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.srgood.dbot.commands.Command;
import com.srgood.dbot.ref.RefStrings;
import com.srgood.dbot.utils.PermissionOps;
import com.srgood.dbot.utils.Permissions;
import com.srgood.dbot.utils.XMLUtils;

import net.dv8tion.jda.audio.player.Player;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.Role;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.ReadyEvent;
import net.dv8tion.jda.events.guild.GuildJoinEvent;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;
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
        public void onGuildMessageReceived(GuildMessageReceivedEvent event){
           
           
            if (BotMain.servers == null) {
                System.out.println("servers == null");
                return;
            }
            if (event.getGuild() == null) {
                System.out.println("guild == null");
                return;
            }
           
            if (BotMain.servers.containsKey(event.getGuild().getId())) {
                Node ServerNode = BotMain.servers.get(event.getGuild().getId());
                Element NodeElement = (Element)ServerNode;
                localPrefix = NodeElement.getElementsByTagName("prefix").item(0).getTextContent();
            } else {               
                Element server = BotMain.PInputFile.createElement("server");
               
                System.out.println("initting Guild from message");
                initGuild(event.getGuild());
               
                localPrefix = BotMain.prefix;
            }
           
           
           
            if (event.getMessage().getContent().equals(RefStrings.TABLE_FLIP)) {
                event.getChannel().sendMessage(RefStrings.TABLE_UNFLIP_JOKE);
            }
           
            BotMain.StoreMessage(event,BotMain.servers.get(event.getGuild().getId()));
           
           
            if(event.getMessage().getContent().startsWith(localPrefix) && event.getMessage().getAuthor().getId() != event.getJDA().getSelfInfo().getId()){
                BotMain.handleCommand(BotMain.parser.parse(event.getMessage().getContent().toLowerCase(),event));
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
       
        //does stuff once JDA is loaded
        @Override
        public void onReady(ReadyEvent event){
           
        }
       
        @Override
        public void onGuildJoin(GuildJoinEvent event) {
//          initGuild(event.getGuild());
        }
        public static void deleteGuild(Guild guild) {
        	for (Node n : XMLUtils.nodeListToList(((Element) ((Element) BotMain.servers.get(guild.getId())).getElementsByTagName("roles").item(0)).getElementsByTagName("role"))) {
        	    guild.getRoleById(n.getTextContent()).getManager().delete();
        	}
        	BotMain.servers.get(guild.getId()).getParentNode().removeChild(BotMain.servers.get(guild.getId()));
        } 
        public static void initGuild(Guild guild) {
      
            Element server = BotMain.PInputFile.createElement("server");
   
            Element elementServers = (Element) BotMain.PInputFile.getDocumentElement().getElementsByTagName("servers")
                    .item(0);
   
            Attr idAttr = BotMain.PInputFile.createAttribute("id");
            idAttr.setValue(guild.getId());
            server.setAttributeNode(idAttr);
            
        Element globalElement = (Element) BotMain.PInputFile.getDocumentElement().getElementsByTagName("global")
                .item(0);
        
        for (Node n : XMLUtils.nodeListToList(globalElement.getChildNodes())) {
            if (n instanceof Element) {
                Element elem = (Element) n;
                server.appendChild(elem.cloneNode(true));
            }
        }

            elementServers.appendChild(server);            
            BotMain.servers.put(guild.getId(), server);
           
            try {            	
            	
                Element elementRoleContainer = BotMain.PInputFile.createElement("roles");
                
                server.appendChild(elementRoleContainer);
                
            	for (Permissions permission : Permissions.values()) {
            		PermissionOps.createRole(permission, guild, true);
            	}
            } catch (PermissionException e3) {
                SimpleLog.getLog("Reasons").warn("Could not create custom role! Possible permissions problem?");
            }
            
            XMLUtils.initGuildCommands(guild);
        }
}