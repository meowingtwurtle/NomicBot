package com.srgood.reasons.impl.commands.main;

import com.srgood.reasons.commands.CommandExecutionData;
import com.srgood.reasons.impl.base.commands.descriptor.BaseCommandDescriptor;
import com.srgood.reasons.impl.base.commands.executor.ChannelOutputCommandExecutor;
import com.srgood.reasons.impl.commands.nomic.Proposal;
import com.srgood.reasons.impl.commands.nomic.ProposalVoting;

import static com.srgood.reasons.impl.commands.nomic.ProposalManager.updateProposal;

public class CommandProposeDescriptor extends BaseCommandDescriptor {
    public CommandProposeDescriptor() {
        super(Executor::new, "Proposes a new rule", "<text (no quote needed)>", "propose");
    }

    private static class Executor extends ChannelOutputCommandExecutor {
        public Executor(CommandExecutionData executionData) {
            super(executionData);
        }

        @Override
        public void execute() {
            String text = executionData.getRawArguments().trim();
            if (text.matches("\\s*")) {
                sendOutput("Proposal must have text.");
                return;
            }
            Proposal proposal = ProposalVoting.ofEnactment(executionData.getMessage().getAuthor().getIdLong(), text);
            updateProposal(proposal);
            sendOutput("Proposed as number %d.", proposal.getNumber());
        }
    }
}
