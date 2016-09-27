package com.srgood.dbot.commands;

import com.srgood.dbot.BotMain;
import com.srgood.dbot.utils.config.ConfigUtils;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;

import java.util.Random;

public class CommandCoinFlip implements Command {

    private static final String HELP = "Flips a coin and prints the result. Use: '" + BotMain.prefix + "flip'";

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
        return HELP;
    }

    @Override
    public void executed(boolean success, GuildMessageReceivedEvent event) {
        // TODO Auto-generated method stub

    }

    @Override
    public com.srgood.dbot.PermissionLevels permissionLevel(Guild guild) {
        // TODO Auto-generated method stub
        return ConfigUtils.getCommandPermission(guild, this);
    }

    @Override
    public com.srgood.dbot.PermissionLevels defaultPermissionLevel() {
        // TODO Auto-generated method stub
        return com.srgood.dbot.PermissionLevels.STANDARD;
    }

    @Override
    public String[] names() {
        return new String[] {"coinflip"};
    }

}
