package com.srgood.reasons.commands;

import com.srgood.reasons.ReasonsMain;
import com.srgood.reasons.utils.CommandUtils;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import static com.srgood.reasons.config.ConfigUtils.setCommandPermission;
import static com.srgood.reasons.utils.Permissions.PermissionUtils.stringToRole;

/**
 * Created by dmanl on 9/11/2016.
 */
public class CommandSetPermLevel implements Command {
    private static final String HELP = "Sets the permission level required for a command on this server. Use: '" + ReasonsMain.prefix + "setpermlevel <command> <role ID>'";

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) {
        Command mCommand = CommandUtils.getCommandByName(args[0]);
        PermissionLevels perm;
        perm = args[1].equalsIgnoreCase("DEFAULT") ? mCommand.defaultPermissionLevel() : stringToRole(args[1]);

        setCommandPermission(event.getGuild(),mCommand,perm);

        event.getChannel().sendMessage("Permission for " + CommandUtils.getNameFromCommand(mCommand) + " set to " + perm.getReadableName() + ".").queue();
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
        return new String[] {"setpermlevel"};
    }
}
