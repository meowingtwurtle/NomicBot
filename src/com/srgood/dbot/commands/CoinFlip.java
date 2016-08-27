package com.srgood.dbot.commands;

import com.srgood.app.BotMain;
import com.srgood.dbot.utils.Permissions;
import com.srgood.dbot.utils.XMLHandler;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;

import java.util.Random;

public class CoinFlip implements Command {

    private final String help = "Flips a coin, and prints the result use:'" + BotMain.prefix + "flip'";

    @Override
    public boolean called(String[] args, GuildMessageReceivedEvent event) {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) {
        // TODO Auto-generated method stub
        Random r = new Random();
        int n = r.nextInt(6002);
        if (n < 3000) {
            event.getChannel().sendMessage("Heads");
        } else if (n > 3001) {
            event.getChannel().sendMessage("Tails");
        } else {
            event.getChannel().sendMessage("Side");
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
