package com.srgood.reasons.impl.commands.main;

import com.srgood.reasons.commands.CommandExecutionData;
import com.srgood.reasons.impl.base.commands.descriptor.BaseCommandDescriptor;
import com.srgood.reasons.impl.base.commands.executor.ChannelOutputCommandExecutor;
import com.srgood.reasons.impl.commands.nomic.*;

import java.util.OptionalInt;

import static com.srgood.reasons.impl.commands.nomic.ProposalManager.updateProposal;

public class CommandTransmuteDescriptor extends BaseCommandDescriptor {
    public CommandTransmuteDescriptor() {
        super(Executor::new, "Proposes to transmute a rule from mutable to immutable, or vice-versa", "<number>", "transmute");
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

            OptionalInt duplicate = ProposalManager.getMatchingProposal(
                    ruleFromNumber.getMutability() == RuleMutability.MUTABLE ?
                            ProposalType.TRANSMUTATION_MUTABLE_TO_IMMUTABLE :
                            ProposalType.TRANSMUTATION_IMMUTABLE_TO_MUTABLE,
                    number);
            if (duplicate.isPresent()) {
                sendOutput("A proposal to transmute rule %d, proposal %d, is already open. Please vote on that instead.", number, duplicate.getAsInt());
                return;
            }

            Proposal proposal = ProposalVoting.ofTransmutation(executionData.getMessage().getAuthor().getIdLong(), number);
            updateProposal(proposal);
            sendOutput("Proposed as number " + proposal.getNumber());
        }
    }
}
