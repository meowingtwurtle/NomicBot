package com.srgood.reasons.impl.commands.nomic;

import static com.srgood.reasons.impl.commands.nomic.RuleMutability.IMMUTABLE;
import static com.srgood.reasons.impl.commands.nomic.RuleMutability.MUTABLE;
import static com.srgood.reasons.impl.commands.nomic.VoteThreshold.ABSOLUTE_UNANIMOUS;
import static com.srgood.reasons.impl.commands.nomic.VoteThreshold.SIMPLE_MAJORITY;

public enum ProposalType {
    ENACTMENT(0, MUTABLE, SIMPLE_MAJORITY),
    AMENDMENT(1, MUTABLE, SIMPLE_MAJORITY),
    REPEAL(2, MUTABLE, SIMPLE_MAJORITY),
    TRANSMUTATION_IMMUTABLE_TO_MUTABLE(3, IMMUTABLE, ABSOLUTE_UNANIMOUS),
    TRANSMUTATION_MUTABLE_TO_IMMUTABLE(4, MUTABLE, SIMPLE_MAJORITY),
    INITIAL_RULE(5, null, null);

    private final int id;
    private final RuleMutability mutabilityRequired;
    private final VoteThreshold voteThreshold;

    ProposalType(int id, RuleMutability mutabilityRequired, VoteThreshold voteThreshold) {
        this.id = id;
        this.mutabilityRequired = mutabilityRequired;
        this.voteThreshold = voteThreshold;
    }

    public int getID() {
        return id;
    }

    public RuleMutability getMutabilityRequired() {
        return mutabilityRequired;
    }

    public VoteThreshold getVotesRequired() {
        return voteThreshold;
    }
}
