package com.srgood.reasons.commands;

import com.srgood.reasons.ReasonsMain;
import com.srgood.reasons.config.ConfigUtils;
import com.srgood.reasons.utils.Censor;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;

public class CommandCensor implements Command {

    private static final String HELP = "Modifies the list of censored words.  Use: '" + ReasonsMain.prefix + "censor <add/remove/list> [word]'";

    @Override
    public boolean called(String[] args, GuildMessageReceivedEvent event) {
        return true;
    }

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) {
        List<String> censoredWords = Censor.getCensoredWordListForGuild(event.getGuild());
        if(args.length == 1) {
            if(args[0].equals("list")) {
                String out = "__**Censored Words**__ ```\n";
                for(int i = 0; i < censoredWords.size(); i++) {
                    out += "-";
                    out += censoredWords.get(i);
                    out += "\n";
                }
                out += "```";
                if(censoredWords.size() == 0) out = "There are no censored words on this server.";
                event.getAuthor().getPrivateChannel().sendMessage(out).queue();

            }
        } else if(args.length == 2) {
            if(args[0].equals("add")) {
                censoredWords.add(args[1].toLowerCase());
                event.getAuthor().getPrivateChannel().sendMessage("\"" + args[1] + "\" added to list of censored words.").queue();
            }
            if(args[0].equals("remove")) {
                if(censoredWords.remove(args[1].toLowerCase())) {
                    event.getAuthor().getPrivateChannel().sendMessage("\"" + args[1] + "\" removed from list of censored words.").queue();
                } else {
                    event.getAuthor().getPrivateChannel().sendMessage("\"" + args[1] + "\" not found in list of censored words.").queue();
                }
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
