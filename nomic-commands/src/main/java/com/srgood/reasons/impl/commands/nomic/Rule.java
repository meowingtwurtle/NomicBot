package com.srgood.reasons.impl.commands.nomic;

import static com.srgood.reasons.impl.commands.nomic.RuleMutability.*;

public class Rule {
    private final int number;
    private final long messageID;
    private final RuleMutability mutability;

    public Rule(int number, long messageID, RuleMutability mutability) {
        this.number = number;
        this.messageID = messageID;
        this.mutability = mutability;
    }

    public static Rule ofproposal(Proposal proposal) {
        RuleMutability ruleMutability;
        switch (proposal.getType()) {
            case ENACTMENT:
                ruleMutability = MUTABLE;
                break;
            case AMENDMENT:
                ruleMutability = MUTABLE;
                break;
            case REPEAL:
                throw new IllegalArgumentException("Cannot create a rule from a repeal");
            case TRANSMUTATION_MUTABLE_TO_IMMUTABLE:
                ruleMutability = IMMUTABLE;
                break;
            case TRANSMUTATION_IMMUTABLE_TO_MUTABLE:
                ruleMutability = MUTABLE;
                break;
            default:
                throw new IllegalStateException("This exception is impossible. If you get it, I owe you 5 dollars.");
        }
        return new Rule(proposal.getNumber(), 0, ruleMutability);
    }

    public int getNumber() {
        return number;
    }

    public long getMessageID() {
        return messageID;
    }

    public Rule withMessageID(long newMessageID) {
        return new Rule(getNumber(), newMessageID, getMutability());
    }

    public Proposal getProposal() {
        return DBUtil.getProposalByID(number);
    }

    public RuleMutability getMutability() {
        return mutability;
    }
}
