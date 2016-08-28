package com.srgood.dbot.utils;

public class CommandUtils {
    public static void handleCommand(com.srgood.dbot.commands.CommandParser.CommandContainer cmd) {
        // checks if the referenced command is in the command list
        if (com.srgood.dbot.BotMain.commands.containsKey(cmd.invoke)) {
            XMLUtils.initCommandIfNotExists(cmd);
            //if the command is enabled for the message's guild...
            if (XMLUtils.commandIsEnabled(cmd.event.getGuild(), com.srgood.dbot.BotMain.commands.get(cmd.invoke))) {
                //if the message author has the required permission level...
                if (PermissionUtils.userPermissionLevel(cmd.event.getGuild(), cmd.event.getAuthor())
                        .getLevel() >= com.srgood.dbot.BotMain.commands.get(cmd.invoke).permissionLevel(cmd.event.getGuild()).getLevel()) {
                    boolean safe = com.srgood.dbot.BotMain.commands.get(cmd.invoke).called(cmd.args, cmd.event);
                    //if the commands get method returns true (see command.class)...
                    if (safe) {
                        //then run the command and its post execution code (see command)
                        com.srgood.dbot.BotMain.commands.get(cmd.invoke).action(cmd.args, cmd.event);
                        com.srgood.dbot.BotMain.commands.get(cmd.invoke).executed(true, cmd.event);
                    } else {
                        //else only run the execution code
                        com.srgood.dbot.BotMain.commands.get(cmd.invoke).executed(false, cmd.event);
                    }
                } else {
                    cmd.event.getChannel().sendMessage("You lack the required permission to preform this action");
                }
            } else {
                cmd.event.getChannel().sendMessage("This command is disabled");
            }
        }
    }
}
