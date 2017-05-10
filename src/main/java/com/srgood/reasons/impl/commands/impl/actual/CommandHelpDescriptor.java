package com.srgood.reasons.impl.commands.impl.actual;

import com.srgood.reasons.commands.CommandDescriptor;
import com.srgood.reasons.commands.CommandExecutionData;
import com.srgood.reasons.impl.commands.impl.base.descriptor.BaseCommandDescriptor;
import com.srgood.reasons.impl.commands.impl.base.executor.DMOutputCommandExecutor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CommandHelpDescriptor extends BaseCommandDescriptor {
    public CommandHelpDescriptor() {
        super(Executor::new, "Provides information about all commands", "<>", "help");
    }

    private static class Executor extends DMOutputCommandExecutor {
        public Executor(CommandExecutionData executionData) {
            super(executionData);
        }

        @Override
        public void execute() {
            if (executionData.getParsedArguments().size() < 1) {
                displayAllCommands();
            } else {
                displayCommands(executionData.getParsedArguments());
            }
        }

        private void displayAllCommands() {
            displayCommands(executionData.getBotManager()
                                         .getCommandManager()
                                         .getRegisteredCommands()
                                         .stream()
                                         .distinct()
                                         .map(CommandDescriptor::getPrimaryName)
                                         .sorted()
                                         .collect(Collectors.toList()));
        }


        private void displayCommands(List<String> commands) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("```Markdown");
            stringBuilder.append("\n");
            stringBuilder.append("__HELP__");
            stringBuilder.append("\n");
            List<String> messages = new ArrayList<>();
            for (String command : commands) {
                List<String> helpLines = getSingleCommandOutput(command);
                for (String line : helpLines) {
                    if (stringBuilder.length() + line.length() + "```".length() >= 2000) { // 2000 is max message length
                        stringBuilder.append("```");
                        messages.add(stringBuilder.toString());
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("```Markdown");
                        stringBuilder.append("\n");
                    }
                    stringBuilder.append(line);
                }
            }
            stringBuilder.append("```");
            messages.add(stringBuilder.toString());
            messages.forEach(this::sendOutput);
        }

        private List<String> getSingleCommandOutput(String command) {
            CommandDescriptor commandDescriptor = executionData.getBotManager()
                                                               .getCommandManager()
                                                               .getCommandByName(command);
            if (commandDescriptor == null) {
                return Collections.singletonList(String.format("There was no command found by the name [%s]\n", command));
            }
            return getCommandHelpLines(commandDescriptor);
        }

        private List<String> getCommandHelpLines(CommandDescriptor command) {
            List<String> ret = new ArrayList<>();
            String primaryName = command.getPrimaryName();
            String aliases = "";
            if (command.getNames().size() > 1) {
                aliases = ". Aliases: " + command.getNames()
                                                 .stream()
                                                 .filter(name -> !Objects.equals(name, primaryName))
                                                 .sorted()
                                                 .collect(Collectors.joining(", "));
            }
            String format = String.format("[%s \"%s\"](%s%s)\n", primaryName, command.help().args(), command.help()
                                                                                                            .description(), aliases);
            ret.add(format);
            if (command.hasSubCommands()) {
                for (CommandDescriptor subCommand : command.getSubCommands()) {
                    List<String> subLines = getCommandHelpLines(subCommand);
                    List<String> subLinesWithPrefix = subLines.stream().map(x -> "-" + x).collect(Collectors.toList());
                    ret.addAll(subLinesWithPrefix);
                }
            }
            return ret;
        }
    }
}
