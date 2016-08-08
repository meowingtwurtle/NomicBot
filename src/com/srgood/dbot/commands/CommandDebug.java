package com.srgood.dbot.commands;
 
import java.awt.Color;
import java.time.Duration;
import java.time.Instant;

import com.srgood.dbot.BotListener;
import com.srgood.dbot.BotMain;
import com.srgood.dbot.utils.Permissions;
import com.srgood.dbot.utils.XMLHandler;

import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.Role;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.exceptions.PermissionException;
import net.dv8tion.jda.managers.RoleManager;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;
 
public class CommandDebug implements Command {
    private final String help = "Used internally for debugging. Use: '" + BotMain.prefix + "debug [debug arg]'";
    private boolean exe = true;
    @Override
    public boolean called(String[] args, GuildMessageReceivedEvent event) {
        // TODO Auto-generated method stub
        return exe;
    }
 
    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) {
        // TODO Auto-generated method stub

    	if (args.length > 0) {
    		switch(args[0].toLowerCase()) {
    			case "flushandinitguild":
    				XMLHandler.deleteGuild(event.getGuild());
    				BotListener.initGuild(event.getGuild());
    				event.getChannel().sendMessage("Done");
    				break;
    			
    			case "verifyXML":
    				event.getChannel().sendMessage("" + XMLHandler.verifyXML());
    				break;
    				
    			case "getuptime":
    				long seconds = Duration.between(BotMain.startInstant, Instant.now()).getSeconds();
    				long absSeconds = Math.abs(seconds);
    				String positive = String.format(
    				        "%d:%02d:%02d",
    				        absSeconds / 3600,
    				        (absSeconds % 3600) / 60,
    				        absSeconds % 60);
    				String x = seconds < 0 ? "-" + positive : positive;
    				event.getChannel().sendMessage(x);
    				break;
    			default:
    				event.getChannel().sendMessage("Invalid argument");
    				break;
    		} 
    	} else {
            event.getChannel().sendMessage("Author Name: " + event.getAuthor().getUsername() + "\n" +
                    "Author Nick: " + event.getAuthorNick() + "\n" +
                    "id: " + event.getAuthor().getId() + "\n" +
                    event.getAuthor().getAsMention() + "\n" +
                    "Picture url: " + event.getAuthor().getAvatarUrl().toString() + "\n" +
                    "BotMain.jda.getSelfInfo().getAsMention().length()" + BotMain.jda.getSelfInfo().getAsMention().toString().length() + "\n" +
                    "BotMain.jda.getSelfInfo().getAsMention()" + BotMain.jda.getSelfInfo().getAsMention() + "\n" + "XMLHandler.verifyXML() = " + XMLHandler.verifyXML());
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
        return Permissions.STANDARD;
    }
   
}