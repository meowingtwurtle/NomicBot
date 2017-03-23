package com.srgood.reasons.commands.upcoming.impl.actual;

import com.srgood.reasons.commands.upcoming.CommandExecutionData;
import com.srgood.reasons.commands.upcoming.impl.base.descriptor.BaseCommandDescriptor;
import com.srgood.reasons.commands.upcoming.impl.base.descriptor.MultiTierCommandDescriptor;
import com.srgood.reasons.commands.upcoming.impl.base.executor.DMOutputCommandExecutor;
import com.srgood.reasons.utils.CensorUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class CommandCensorDescriptor extends MultiTierCommandDescriptor {
    public CommandCensorDescriptor() {
        super(new HashSet<>(
                Arrays.asList(
                        new ListDescriptor(),
                        new AddDescriptor(),
                        new RemoveDescriptor())),
                "Performs operations with the censorlist of the current Guild",
                "<list | add | remove> <...>",
                Collections.singletonList("censor"));
    }

    private static class ListDescriptor extends BaseCommandDescriptor {
        public ListDescriptor() {
            super(Executor::new, "Gets the current censorlist", "<>","list");
        }

        private static class Executor extends DMOutputCommandExecutor {
            public Executor(CommandExecutionData executionData) {
                super(executionData);
            }

            @Override
            public void execute() {
                List<String> censoredWords = CensorUtils.getGuildCensorList(executionData.getGuild());

                StringBuilder outBuilder = new StringBuilder("__**Censored Words**__ ```\n");
                for (String censoredWord : censoredWords) {
                    outBuilder.append("- ");
                    outBuilder.append(censoredWord);
                    outBuilder.append("\n");
                }
                outBuilder.append("```");
                if (censoredWords.size() == 0) {
                    outBuilder = new StringBuilder("There are no censored words on this server.");
                }
                sendOutput(outBuilder.toString());
            }
        }
    }

    private static class AddDescriptor extends BaseCommandDescriptor {
        public AddDescriptor() {
            super(Executor::new, "Adds a word to the current censorlist", "<word>", "add");
        }

        private static class Executor extends DMOutputCommandExecutor {
            public Executor(CommandExecutionData executionData) {
                super(executionData);
            }

            @Override
            public void execute() {
                List<String> censoredWords = CensorUtils.getGuildCensorList(executionData.getGuild());


                String wordToCensor = executionData.getParsedArguments().get(0).toLowerCase();
                censoredWords.add(wordToCensor);
                CensorUtils.setGuildCensorList(executionData.getGuild(), censoredWords);

                sendOutput("The word `%s` has been added to the censorlist.", wordToCensor);
            }
        }
    }

    private static class RemoveDescriptor extends BaseCommandDescriptor  {
        public RemoveDescriptor() {
            super(Executor::new, "Removes a word from the current censorlist", "<word>", "remove");
        }

        private static class Executor extends DMOutputCommandExecutor {
            public Executor(CommandExecutionData executionData) {
                super(executionData);
            }

            @Override
            public void execute() {
                List<String> censoredWords = CensorUtils.getGuildCensorList(executionData.getGuild());

                String wordToRemove = executionData.getParsedArguments().get(0).toLowerCase();
                boolean inList = censoredWords.contains(wordToRemove);

                if (inList) {
                    censoredWords.remove(wordToRemove);
                    CensorUtils.setGuildCensorList(executionData.getGuild(), censoredWords);
                    sendOutput("The word `%s` has been removed from the censor list.", wordToRemove);
                } else {
                    sendOutput("The word `%s` was not found in the censor list.", wordToRemove);
                }
            }
        }
    }
}
