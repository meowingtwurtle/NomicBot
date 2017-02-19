package com.srgood.reasons.commands;

import com.srgood.reasons.ReasonsMain;
import net.dv8tion.jda.core.entities.MessageHistory;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.util.Collection;

public class CommandDelete implements Command {

    private static final String HELP = "Deletes messages. Use: '" + ReasonsMain.prefix + "delete [all|bot] [channel name]' Default is all in current channel";

    @Override
    public boolean called(String[] args, GuildMessageReceivedEvent event) {
        return true;
    }

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) {

        if (!event.getGuild().getSelfMember().hasPermission(event.getChannel(), Permission.MANAGE_PERMISSIONS)) {
            event.getChannel().sendMessage("Error, unable to delete messages! Please check permissions.").queue();
            return;
        }

        MessageHistory historyHistory = event.getChannel().getHistoryAround(event.getMessage(), 100).complete();
        Collection<Message> cachedHistory = historyHistory.getRetrievedHistory();
        event.getChannel().deleteMessages(cachedHistory).queue( x -> event.getChannel().sendMessage(String.format("Successfully deleted **%s** messages.", cachedHistory.size())).queue());

    }

    @Override
    public String help() {
        return HELP;
    }

    @Override
    public PermissionLevels defaultPermissionLevel() {
        
        return PermissionLevels.ADMINISTRATOR;
    }

    @Override
    public String[] names() {
        return new String[] {"delete"};
    }

}
