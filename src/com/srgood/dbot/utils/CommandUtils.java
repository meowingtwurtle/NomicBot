package com.srgood.dbot.utils;

import com.srgood.dbot.BotMain;
import com.srgood.dbot.commands.CommandParser;
import com.srgood.dbot.threading.ChannelThread;

public class CommandUtils {

    private static java.util.Map<String, ChannelThread> channelThreadMap = new java.util.HashMap<>();

    public static void handleCommand(CommandParser.CommandContainer cmd) {
        // checks if the referenced command is in the command list
        if (BotMain.commands.containsKey(cmd.invoke)) {
            XMLUtils.initCommandIfNotExists(cmd);
            //if the command is enabled for the message's guild...
            //if the message author has the required permission level...
            if (XMLUtils.commandIsEnabled(cmd.event.getGuild(), BotMain.commands.get(cmd.invoke)))
                if (PermissionUtils.userPermissionLevel(cmd.event.getGuild(), cmd.event.getAuthor()).getLevel() >= BotMain.commands.get(cmd.invoke).permissionLevel(cmd.event.getGuild()).getLevel()) {
                    boolean safe = BotMain.commands.get(cmd.invoke).called(cmd.args, cmd.event);
                    if (channelThreadMap.containsKey(cmd.event.getChannel().getId())) {
                        ChannelThread channelThread = channelThreadMap.get(cmd.event.getChannel().getId());
                        channelThread.addCommand(new ChannelThread.CommandItem(cmd, safe));
                        if (!channelThread.isAlive()) {
                            channelThread.start();
                        }
                    } else {
                        ChannelThread channelThread = new ChannelThread(cmd.event.getChannel());
                        channelThread.addCommand(new ChannelThread.CommandItem(cmd, safe));
                        channelThreadMap.put(cmd.event.getChannel().getId(), channelThread);
                        channelThread.start();
                    }
                } else {
                    cmd.event.getChannel().sendMessage("You lack the required permission to preform this action");
                }
            else {
                cmd.event.getChannel().sendMessage("This command is disabled");
            }
        }
    }

    public static void deregisterThread(ChannelThread thread) {
        if (channelThreadMap.containsKey(thread.getChannelID())) {
            channelThreadMap.remove(thread.getChannelID());
        }
    }
}
