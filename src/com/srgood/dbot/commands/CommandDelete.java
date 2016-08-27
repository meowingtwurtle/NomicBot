package com.srgood.dbot.commands;

import com.srgood.dbot.BotMain;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CommandDelete implements Command {

    private final String help = "Deletes Messages Use: '" + BotMain.prefix + "delete [all|bot] [channel name]' Default is all in current channel";

    @Override
    public boolean called(String[] args, GuildMessageReceivedEvent event) {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) {


        String channel, delType;
        List<String> messages = new ArrayList<>();


        if (args.length >= 1) {
            delType = args[0].toLowerCase();
            if (args.length >= 2) {
                channel = BotMain.cleanFileName(args[1]);
            } else {
                channel = BotMain.cleanFileName(event.getChannel().getName());
            }
        } else {
            delType = "all";
            channel = BotMain.cleanFileName(event.getChannel().getName());
        }


        if (delType.equals("all")) {
            File dir = new File("messages/guilds/" + BotMain.cleanFileName(event.getGuild().getName()) + "/" + channel + "/all/");
            File[] directoryListing = dir.listFiles();
            if (directoryListing != null) {
                for (File child : directoryListing) {
                    try {
                        messages.add(child.getName().replace(".ser", ""));
                        child.delete();
                    } catch (Exception ignored) {

                    }

                }
            } else {
                dir.mkdirs();
            }
        } else if (delType.equals("bot")) {
            File dir = new File("messages/guilds/" + BotMain.cleanFileName(event.getGuild().getName()) + "/" + channel + "/bot/");
            File[] directoryListing = dir.listFiles();
            if (directoryListing != null) {
                for (File child : directoryListing) {
                    try {
                        messages.add(child.getName().replace(".ser", ""));
                        child.delete();
                    } catch (Exception ignored) {

                    }

                }
            } else {
                dir.mkdirs();
            }
        }

        event.getChannel().deleteMessagesByIds(messages);
        event.getChannel().sendMessage("Successfully Deleted **" + messages.size() + "** messages");

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
    public com.srgood.dbot.PermissionLevels permissionLevel(Guild guild) {
        // TODO Auto-generated method stub
        return com.srgood.dbot.utils.XMLUtils.getCommandPermissionXML(guild, this);
    }

    @Override
    public com.srgood.dbot.PermissionLevels defaultPermissionLevel() {
        // TODO Auto-generated method stub
        return com.srgood.dbot.PermissionLevels.STANDARD;
    }

}
