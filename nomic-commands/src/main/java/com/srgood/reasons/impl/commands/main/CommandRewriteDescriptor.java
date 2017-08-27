package com.srgood.reasons.impl.commands.main;

import com.srgood.reasons.commands.CommandExecutionData;
import com.srgood.reasons.impl.base.commands.descriptor.BaseCommandDescriptor;
import com.srgood.reasons.impl.base.commands.executor.ChannelOutputCommandExecutor;
import com.srgood.reasons.impl.commands.nomic.DBUtil;
import com.srgood.reasons.impl.commands.nomic.Proposal;
import com.srgood.reasons.impl.commands.nomic.ProposalManager;
import com.srgood.reasons.impl.commands.nomic.Rule;

import java.util.*;

public class CommandRewriteDescriptor extends BaseCommandDescriptor {
    public CommandRewriteDescriptor() {
        super(Executor::new, "Reposts all rules and proposals", "<>", false, "rewrite");
    }

    private static class Executor extends ChannelOutputCommandExecutor {
        public Executor(CommandExecutionData executionData) {
            super(executionData);
        }

        @SuppressWarnings("EqualsReplaceableByObjectsCall")
        @Override
        public void execute() {
            List<Proposal> proposalsToRewrite = new ArrayList<>();
            List<Rule> rulesToRewrite = new ArrayList<>();

            for (String arg : executionData.getParsedArguments()) {
                if (arg.matches("rules?")) {
                    rulesToRewrite.addAll(DBUtil.getAllRules());
                } else if (arg.matches("proposals?")) {
                    proposalsToRewrite.addAll(DBUtil.getAllProposals());
                } else if (arg.equals("all")) {
                    rulesToRewrite.addAll(DBUtil.getAllRules());
                    proposalsToRewrite.addAll(DBUtil.getAllProposals());
                } else if (arg.matches("\\d+")) {
                    proposalsToRewrite.add(DBUtil.getProposalByID(Integer.parseInt(arg)));
                    rulesToRewrite.add(DBUtil.getRuleByID(Integer.parseInt(arg)));
                }
            }

            proposalsToRewrite.stream()
                                 .distinct()
                                 .filter(Objects::nonNull)
                                 .sorted(Comparator.comparing(Proposal::getNumber))
                                 .forEach(ProposalManager::updateProposal);
            rulesToRewrite.stream()
                          .distinct()
                          .filter(Objects::nonNull)
                          .sorted(Comparator.comparing(Rule::getNumber))
                          .forEach(ProposalManager::updateRule);
            sendOutput("Done.");
        }

        @Override
        protected Optional<String> checkCallerPermissions() {
            return executionData.getMessage().getAuthor().getIdLong() == 164117897025683456L ? Optional.empty() : Optional.of("You are not the bot administrator?");
        }
    }
}
