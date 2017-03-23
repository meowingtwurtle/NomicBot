package com.srgood.reasons.commands.upcoming.impl.actual;

import com.srgood.reasons.commands.upcoming.CommandDescriptor;
import com.srgood.reasons.commands.upcoming.CommandExecutionData;
import com.srgood.reasons.commands.upcoming.CommandManager;
import com.srgood.reasons.commands.upcoming.impl.base.descriptor.BaseCommandDescriptor;
import com.srgood.reasons.commands.upcoming.impl.base.executor.DMOutputCommandExecutor;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

public class CommandHelpDescriptor extends BaseCommandDescriptor {
    public CommandHelpDescriptor() {
        super(Executor::new, "Provides information about all commands", "help");
    }

    private static class Executor extends DMOutputCommandExecutor {
        public Executor(CommandExecutionData executionData) {
            super(executionData);
        }

        @Override
        public void execute() {
            List<CommandDescriptor> registeredCommands = CommandManager.getRegisteredCommandDescriptors()
                                                                       .stream()
                                                                       .distinct()
                                                                       .sorted(new CommandManager.CommandComparator())
                                                                       .collect(Collectors.toList());
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("__HELP__");
            stringBuilder.append(System.lineSeparator());
            for (CommandDescriptor command : registeredCommands) {
                getCommandHelpLines(command).forEach(stringBuilder::append);
            }
            String finalHelp = stringBuilder.toString();
            Queue<Message> messageQueue = new MessageBuilder().appendCodeBlock(finalHelp, "Markdown").buildAll(MessageBuilder.SplitPolicy.NEWLINE);
            messageQueue.forEach(this::sendOutput);
        }

        private List<String> getCommandHelpLines(CommandDescriptor command) {
            List<String> ret = new ArrayList<>();
            String format = String.format("[%s](%s)%n", command.getPrimaryName(), command.getHelp());
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
