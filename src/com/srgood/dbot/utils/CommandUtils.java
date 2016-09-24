package com.srgood.dbot.utils;

import com.srgood.dbot.commands.Command;
import com.srgood.dbot.commands.CommandParser;
import com.srgood.dbot.threading.ChannelThread;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class CommandUtils {

    public static Map<String, Command> commands = new TreeMap<>();
    private static java.util.Map<String, ChannelThread> channelThreadMap = new java.util.HashMap<>();

    public static void handleCommand(CommandParser.CommandContainer cmd) {
        // checks if the referenced command is in the command list
        if (commands.containsKey(cmd.invoke)) {
            ConfigUtils.initCommandIfNotExists(cmd);
            //if the command is enabled for the message's guild...
            //if the message author has the required permission level...
            if (ConfigUtils.commandIsEnabled(cmd.event.getGuild(), commands.get(cmd.invoke)))
                if (PermissionUtils.userPermissionLevel(cmd.event.getGuild(), cmd.event.getAuthor()).getLevel() >= commands.get(cmd.invoke).permissionLevel(cmd.event.getGuild()).getLevel()) {
                    boolean safe = commands.get(cmd.invoke).called(cmd.args, cmd.event);
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
        } else {
            cmd.event.getChannel().sendMessage("Unknown command `" + cmd.invoke+"`");
        }
    }

    public static void deregisterThread(ChannelThread thread) {
        if (channelThreadMap.containsKey(thread.getChannelID())) {
            channelThreadMap.remove(thread.getChannelID());
        }
    }

    public static String getNameFromCommand(Command cmd) {
        return cmd.names()[0];
    }

    public static String getPrimaryCommandAlias(String name) {
        return getNameFromCommand(getCommandByName(name));
    }

    public static Command getCommandByName(String name) {
        return commands.get(name);
    }

    public static class CommandComparator implements Comparator<Command> {

        @Override
        public int compare(Command command0, Command command1) {
            return getNameFromCommand(command0).compareTo(getNameFromCommand(command1));
        }
    }

}
