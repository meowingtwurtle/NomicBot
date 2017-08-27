package com.srgood.reasons.impl.commands.main;


import com.srgood.reasons.commands.CommandExecutionData;
import com.srgood.reasons.impl.base.commands.descriptor.BaseCommandDescriptor;
import com.srgood.reasons.impl.base.commands.executor.ChannelOutputCommandExecutor;
import com.srgood.reasons.impl.commands.nomic.*;

public class BaseVoteCommandDescriptor extends BaseCommandDescriptor {
    public BaseVoteCommandDescriptor(VoteType voteType, String help, String primaryName, String... names) {
        super(commandExecutionData -> new Executor(commandExecutionData, voteType), help, "<number>", primaryName, names);
    }

    private static class Executor extends ChannelOutputCommandExecutor {
        private final VoteType voteType;

        public Executor(CommandExecutionData executionData, VoteType voteType) {
            super(executionData);
            this.voteType = voteType;
        }

        @Override
        public void execute() {
            int number = Integer.parseInt(executionData.getParsedArguments().get(0));
            Proposal proposal = DBUtil.getProposalByID(number);
            if (proposal == null) {
                sendOutput("No proposal with that number exists.");
                return;
            }
            if (!(proposal instanceof ProposalVoting)) {
                sendOutput("Voting is not open for that proposal.");
                return;
            }

            ProposalManager.castVote((ProposalVoting) proposal, executionData.getMessage().getAuthor().getIdLong(), voteType);
            sendOutput("Vote cast.");
        }
    }
}
