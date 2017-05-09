package com.srgood.reasons.impl.commands;

import net.dv8tion.jda.core.entities.Message;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandUtils {

    public static boolean isCommandMessage(Message message, String guildPrefix) {
        String mention = message.getGuild().getSelfMember().getAsMention();
        return message.getRawContent().startsWith(guildPrefix) ||
                message.getRawContent().startsWith(mention);
    }

    public static String getCommandMessageArgsSection(Message message, String guildPrefix) {
        if (!isCommandMessage(message, guildPrefix)) {
            return null;
        }
        String noPrefix = removeCommandMessagePrefix(message, guildPrefix);
        String command = getCalledCommand(message, guildPrefix);
        return noPrefix.substring(command.length());
    }

    public static String getCalledCommand(Message message, String guildPrefix) {
        if (!isCommandMessage(message, guildPrefix)) {
            return null;
        }
        String noPrefix = removeCommandMessagePrefix(message, guildPrefix).trim();
        return noPrefix.split("\\s")[0];
    }

    public static String removeCommandMessagePrefix(Message message, String guildPrefix) {
        if (!isCommandMessage(message, guildPrefix)) {
            return null;
        }
        String mention = message.getGuild().getSelfMember().getAsMention();
        String rawContent = message.getRawContent();
        String actualPrefix = rawContent.startsWith(guildPrefix) ? guildPrefix : mention;
        return rawContent.substring(actualPrefix.length()).trim();
    }

    public static List<String> parseCommandMessageArguments(Message message, String guildPrefix) {
        if (!isCommandMessage(message, guildPrefix)) {
            return Collections.emptyList();
        }
        String argsOnly = getCommandMessageArgsSection(message, guildPrefix);

        boolean isEscaped = false;
        boolean inQuote = false;
        List<String> args = new ArrayList<>();
        StringBuilder currentArgBuilder = new StringBuilder();

        for (char c : argsOnly.toCharArray()) {
            if (!isEscaped) {
                if (Character.isWhitespace(c)) {
                    if (!inQuote) { // Not in a quote, so whitespace separates the args
                        String arg = currentArgBuilder.toString();
                        if (!arg.isEmpty()) {
                            args.add(arg);
                            currentArgBuilder = new StringBuilder();
                        }
                    } else { // In a quote, just append to the arg
                        currentArgBuilder.append(c);
                    }
                } else if (c == '\\') { // Next character is escaped, set the flag
                    isEscaped = true;
                } else if (c == '\"') { // Either start or end a quote. Add anything in the arg
                    String arg = currentArgBuilder.toString();
                    if (!arg.isEmpty()) {
                        args.add(arg);
                        currentArgBuilder = new StringBuilder();
                    }
                    inQuote = !inQuote;
                } else { // No special character, just append to arg
                    currentArgBuilder.append(c);
                }
            } else { // Escaped, bypass any special character behavior
                currentArgBuilder.append(c);
                isEscaped = false;
            }
        }

        // Add final arg, if any
        String arg = currentArgBuilder.toString();
        if (!arg.isEmpty()) {
            args.add(arg);
        }

        return args;
    }

}
