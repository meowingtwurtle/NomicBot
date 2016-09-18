package com.srgood.dbot.commands;

import com.srgood.dbot.BotMain;
import net.dv8tion.jda.Permission;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;
import java.util.List;

import static com.srgood.dbot.BotMain.jda;

public class CommandDelete implements Command {

    private final String help = "Deletes Messages Use: '" + BotMain.prefix + "delete [all|bot] [channel name]' Default is all in current channel";
    int total;

    @Override
    public boolean called(String[] args, GuildMessageReceivedEvent event) {
        // TODO Auto-generated method stub
        total = event.getChannel().getHistory().retrieveAll().size();
        return true;
    }

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) {

        if (!event.getChannel()
                .checkPermission(jda.getSelfInfo(), Permission.MESSAGE_MANAGE)) {
            event.getChannel().sendMessage("Error, unable to delete messages! Please check permissions.");
            return;
        }

        String channel, delType;
        List<Message> messages = event.getChannel().getHistory().retrieveAll();
        List<Message> buffer = new ArrayList<>();
        Boolean needsRecursion = false;

        if (args.length >= 1) {
            delType = args[0].toLowerCase();
            if (args.length >= 2) {
                channel = BotMain.cleanFileName(args[1]);
            } else {
                channel = BotMain.cleanFileName(event.getChannel().getName());
            }
        } else {
            delType = "all";
        }

        if (messages.size() > 100) {
            for (; ;) {
                messages.remove(messages.size()-1);
                if (messages.size() == 100) break;
            }
            needsRecursion = true;
        }
/*
        if (delType.equals("bot")) {
            for (Message message : messages) {
                if (!message.getContent().startsWith(XMLUtils.getGuildPrefix(event.getGuild())) & !event.getJDA().getSelfInfo().getAsMention().equals(event.getMessage().getMentionedUsers().get(0).getAsMention()) & !event.getAuthor().getId().equals(event.getJDA().getSelfInfo().getId())) {
                    messages.remove(message);
                }
            }
        }
*/

        event.getChannel().deleteMessages(messages);

        if (needsRecursion) {
            this.action(args,event);
        } else {
            event.getChannel().sendMessage("Successfully Deleted **" + total + "** messages");
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
    public com.srgood.dbot.PermissionLevels permissionLevel(Guild guild) {
        // TODO Auto-generated method stub
        return com.srgood.dbot.utils.XMLUtils.getCommandPermissionXML(guild, this);
    }

    @Override
    public com.srgood.dbot.PermissionLevels defaultPermissionLevel() {
        // TODO Auto-generated method stub
        return com.srgood.dbot.PermissionLevels.STANDARD;
    }

    @Override
    public String[] names() {
        return new String[] {"delete"};
    }

}
