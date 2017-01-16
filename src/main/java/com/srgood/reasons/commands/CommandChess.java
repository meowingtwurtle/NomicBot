package com.srgood.reasons.commands;

import com.srgood.reasons.games.ChessGame;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

/**
 * Created by dmanl on 9/11/2016.
 */
public class CommandChess implements Command {
    private static final String HELP = "A WIP chess game. Use: no usage available";

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) {
        if (args.length >= 1) {
            ChessGame chessGame = new ChessGame(event.getChannel());
        }
    }

    @Override
    public String help() {
        return HELP;
    }
    
    @Override
    public String[] names() {
        return new String[] {"chess"};
    }
}
