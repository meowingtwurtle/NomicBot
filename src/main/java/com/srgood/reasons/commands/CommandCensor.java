package com.srgood.reasons.commands;

import com.srgood.reasons.ReasonsMain;
import com.srgood.reasons.config.ConfigUtils;
import com.srgood.reasons.utils.CensorUtils;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;
import java.util.List;

public class CommandCensor implements Command {

    private static final String HELP = "Modifies the list of censored words.  Use: '" + ReasonsMain.prefix + "censor <add/remove/list> [word]'";

    @Override
    public boolean called(String[] args, GuildMessageReceivedEvent event) { return true; }

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) {
        event.getMessage().deleteMessage().queue();

        List<String> censoredWords = new ArrayList<>(CensorUtils.getGuildCensorList(event.getGuild()));

        if(args.length == 1) {
            if(args[0].equals("list")) {
                String out = "__**Censored Words**__ ```\n";
                for (String censoredWord : censoredWords) {
                    out += "- ";
                    out += censoredWord;
                    out += "\n";
                }
                out += "```";
                if(censoredWords.size() == 0) out = "There are no censored words on this server.";
                final String finalOut = out; // Because of lambda requirement
                event.getAuthor().openPrivateChannel().queue(c -> c.sendMessage(finalOut).queue());
            }
        } else if(args.length == 2) {
            String messageVerb = "";
            try {
                if (args[0].equals("add")) {
                    messageVerb = "added to";
                    censoredWords.add(args[1].toLowerCase());
                    CensorUtils.setGuildCensorList(event.getGuild(), censoredWords);
                }
                if (args[0].equals("remove")) {
                    messageVerb = "removed from";
                    if (censoredWords.remove(args[1].toLowerCase())) {
                        CensorUtils.setGuildCensorList(event.getGuild(), censoredWords);
                    } else {
                        event.getAuthor()
                             .openPrivateChannel().queue(channel -> channel
                             .sendMessage(String.format("The word `%s` was not found in the censor list.", args[1]))
                             .queue());
                        return;
                    }
                }
                final String finalMessageVerb = messageVerb; // Because of lambda requirement
                event.getAuthor()
                     .openPrivateChannel().queue(channel -> channel
                        .sendMessage(String.format("The word `%s` was %s the censor list.", args[1], finalMessageVerb)).queue());
            } catch (Exception e) {
                final String finalMessageVerb = messageVerb; // Because of lambda requirement
                event.getAuthor()
                     .openPrivateChannel().queue(channel -> channel
                        .sendMessage(String.format("The word `%s` could not be %s the censor list.", args[1], finalMessageVerb)).queue());
            }
        }
    }

    @Override
    public String help() {
        return HELP;
    }

    @Override
    public void executed(boolean success, GuildMessageReceivedEvent event) {}

    @Override
    public PermissionLevels permissionLevel(Guild guild) {
        return ConfigUtils.getCommandPermission(guild, this);
    }

    @Override
    public PermissionLevels defaultPermissionLevel() {
        return PermissionLevels.ADMINISTRATOR;
    }

    @Override
    public String[] names() {
        return new String[] {"censor"};
    }

}
