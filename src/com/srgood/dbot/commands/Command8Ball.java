package com.srgood.dbot.commands;

import com.srgood.dbot.PermissionLevels;
import com.srgood.dbot.Reference;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;

import java.util.Random;

/**
 * Created by dmanl on 9/13/2016.
 */
public class Command8Ball implements Command {
    @Override
    public boolean called(String[] args, GuildMessageReceivedEvent event) {
        return true;
    }

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) {
        event.getChannel().sendMessage(Reference.Strings.EIGHT_BALL[new Random().nextInt(Reference.Strings.EIGHT_BALL.length)]);
    }

    @Override
    public String help() {
        return "Uses a magic 8Ball. ";
    }

    @Override
    public void executed(boolean success, GuildMessageReceivedEvent event) {

    }

    @Override
    public PermissionLevels permissionLevel(Guild guild) {
        return com.srgood.dbot.utils.XMLUtils.getCommandPermissionXML(guild, this);
    }

    @Override
    public PermissionLevels defaultPermissionLevel() {
        return PermissionLevels.STANDARD;
    }

    @Override
    public String[] names() {
        return new String[] {"8ball"};
    }
}
