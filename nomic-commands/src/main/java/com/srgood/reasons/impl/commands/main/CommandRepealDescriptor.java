package com.srgood.reasons.impl.commands.main;

import com.srgood.reasons.commands.CommandExecutionData;
import com.srgood.reasons.impl.base.commands.descriptor.BaseCommandDescriptor;
import com.srgood.reasons.impl.base.commands.executor.ChannelOutputCommandExecutor;
import com.srgood.reasons.impl.commands.nomic.*;

import java.util.OptionalInt;

import static com.srgood.reasons.impl.commands.nomic.ProposalManager.updateProposal;

public class CommandRepealDescriptor extends BaseCommandDescriptor {
    public CommandRepealDescriptor() {
        super(Executor::new, "Proposes a repeal of the rule specified by the number", "<number>", "repeal");
    }

    private static class Executor extends ChannelOutputCommandExecutor {
        public Executor(CommandExecutionData executionData) {
            super(executionData);
        }

        @Override
        public void execute() {
            int number = Integer.parseInt(executionData.getParsedArguments().get(0));
            if (DBUtil.getRuleByID(number) == null) {
                sendOutput("That rule is not currently in effect or does not exist.");
                return;
            }

            OptionalInt duplicate = ProposalManager.getMatchingProposal(ProposalType.REPEAL, number);
            if (duplicate.isPresent()) {
                sendOutput("A proposal to repeal rule %d, proposal %d, is already open. Please vote on that instead.", number, duplicate.getAsInt());
                return;
            }

            try {
                Proposal proposal = ProposalVoting.ofRepeal(executionData.getMessage().getAuthor().getIdLong(), number);
                updateProposal(proposal);
                sendOutput("Proposed as number " + proposal.getNumber());
            } catch (IllegalStateException e) {
                sendOutput(e.getMessage());
            }
        }
    }
}
