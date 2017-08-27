package com.srgood.reasons.impl.commands.main;

import com.srgood.reasons.commands.CommandExecutionData;
import com.srgood.reasons.impl.base.commands.descriptor.BaseCommandDescriptor;
import com.srgood.reasons.impl.base.commands.executor.ChannelOutputCommandExecutor;
import com.srgood.reasons.impl.commands.nomic.*;

import java.util.stream.Collectors;

import static com.srgood.reasons.impl.commands.nomic.ProposalManager.updateProposal;

public class CommandAmendDescriptor extends BaseCommandDescriptor {
    public CommandAmendDescriptor() {
        super(Executor::new, "Proposes an amendment of the rule specified by the number", "<getNumber> <new text (no quotes needed)>", "amend");
    }

    private static class Executor extends ChannelOutputCommandExecutor {
        public Executor(CommandExecutionData executionData) {
            super(executionData);
        }

        @Override
        public void execute() {
            int number = Integer.parseInt(executionData.getParsedArguments().get(0));
            Rule ruleFromNumber = DBUtil.getRuleByID(number);
            if (ruleFromNumber == null) {
                sendOutput("That rule is not currently in effect or does not exist.");
                return;
            }
            Proposal proposalToAmend = ruleFromNumber.getProposal();
            if (proposalToAmend.getType() == ProposalType.AMENDMENT && DBUtil.getProposalByID(proposalToAmend.getAffectedRule()).getType() == ProposalType.AMENDMENT) {
                sendOutput("Cannot amend an amendment of an amendment. Repeal and reenactment are required.");
                return;
            }
            String newText = executionData.getParsedArguments().subList(1, executionData.getParsedArguments().size()).stream().collect(Collectors
                    .joining(" "));
            try {
                Proposal proposal = ProposalVoting.ofAmendment(executionData.getMessage().getAuthor().getIdLong(), number, newText);
                updateProposal(proposal);
                sendOutput("Proposed as number " + proposal.getNumber());
            } catch (IllegalStateException e) {
                sendOutput(e.getMessage());
            }
        }
    }
}
