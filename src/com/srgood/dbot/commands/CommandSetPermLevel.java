package com.srgood.dbot.commands;

import com.srgood.dbot.PermissionLevels;
import com.srgood.dbot.utils.CommandUtils;
import com.srgood.dbot.utils.ConfigUtils;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;

import static com.srgood.dbot.utils.PermissionUtils.stringToRole;
import static com.srgood.dbot.utils.ConfigUtils.setCommandPermissionXML;

/**
 * Created by dmanl on 9/11/2016.
 */
public class CommandSetPermLevel implements Command {
    @Override
    public boolean called(String[] args, GuildMessageReceivedEvent event) {
        return true;
    }

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) {
        Command mCommand = CommandUtils.getCommandByName(args[0]);
        PermissionLevels perm = stringToRole(args[1]);

        setCommandPermissionXML(event.getGuild(),mCommand,perm);

        event.getChannel().sendMessage("Permission for " + CommandUtils.getNameFromCommand(mCommand) + " set to " + perm.getReadableName() + ".");
    }

    @Override
    public String help() {
        return null;
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
        return new String[] {"setpermlevel"};
    }
}
