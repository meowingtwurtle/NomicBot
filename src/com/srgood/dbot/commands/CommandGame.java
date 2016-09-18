package com.srgood.dbot.commands;

import com.srgood.dbot.Games.ChessGame;
import com.srgood.dbot.PermissionLevels;
import com.srgood.dbot.utils.ConfigUtils;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;

/**
 * Created by dmanl on 9/11/2016.
 */
public class CommandGame implements Command {
    private final String help = "A WIP chess game. Use: no usage available";

    @Override
    public boolean called(String[] args, GuildMessageReceivedEvent event) {
        return true;
    }

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) {
        if (args.length >= 1) {
            ChessGame chessGame = new ChessGame(event.getChannel().sendMessage(""));
        }
    }

    @Override
    public String help() {
        return help;
    }

    @Override
    public void executed(boolean success, GuildMessageReceivedEvent event) {

    }

    @Override
    public PermissionLevels permissionLevel(Guild guild) {
        return ConfigUtils.getCommandPermissionXML(guild, this);
    }

    @Override
    public PermissionLevels defaultPermissionLevel() {
        return PermissionLevels.STANDARD;
    }

    @Override
    public String[] names() {
        return new String[] {"game"};
    }
}
