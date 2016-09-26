package com.srgood.dbot.commands;

import com.srgood.dbot.BotMain;
import com.srgood.dbot.PermissionLevels;
import com.srgood.dbot.utils.CommandUtils;
import com.srgood.dbot.utils.config.ConfigUtils;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;

import static com.srgood.dbot.utils.PermissionUtils.stringToRole;
import static com.srgood.dbot.utils.config.ConfigUtils.setCommandPermission;

/**
 * Created by dmanl on 9/11/2016.
 */
public class CommandSetPermLevel implements Command {
    private static final String HELP = "Sets the permission level required for a command on this server. Use: '" + BotMain.prefix + "setpermlevel <command> <role ID>'";

    @Override
    public boolean called(String[] args, GuildMessageReceivedEvent event) {
        return true;
    }

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) {
        Command mCommand = CommandUtils.getCommandByName(args[0]);
        PermissionLevels perm = stringToRole(args[1]);

        setCommandPermission(event.getGuild(),mCommand,perm);

        event.getChannel().sendMessage("Permission for " + CommandUtils.getNameFromCommand(mCommand) + " set to " + perm.getReadableName() + ".");
    }

    @Override
    public String help() {
        return HELP;
    }

    @Override
    public void executed(boolean success, GuildMessageReceivedEvent event) {

    }

    @Override
    public PermissionLevels permissionLevel(Guild guild) {
        return ConfigUtils.getCommandPermission(guild, this);
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
