package com.srgood.reasons.impl.commands.nomic;

import org.apache.commons.lang3.tuple.Pair;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

import static com.srgood.reasons.impl.commands.nomic.JDAUtil.getVoterCount;
import static com.srgood.reasons.impl.commands.nomic.ProposalState.*;
import static com.srgood.reasons.impl.commands.nomic.ProposalType.*;

public class ProposalVoting extends Proposal {
    public static final Duration VOTE_DURATION = Duration.ofDays(1);

    private final Timestamp voteExpiry;

    public ProposalVoting(int number, Timestamp voteExpiry) {
        super(DBUtil.getProposalByID(number));
        this.voteExpiry = voteExpiry;

        if (getState() != VOTING) {
            throw new IllegalArgumentException("Proposal number supplied must be in the VOTING state.");
        }
    }

    public ProposalVoting(int number, long messageID, long proposerID, String text, int affectedRule, ProposalType type, Timestamp voteExpiry) {
        super(number, messageID, proposerID, text, affectedRule, VOTING, type);
        this.voteExpiry = voteExpiry;
    }

    public static ProposalVoting ofEnactment(long proposerID, String text) {
        int number = DBUtil.getNextProposalID();
        return new ProposalVoting(number, 0, proposerID, text, number, ENACTMENT, createVoteEndingTimestamp());
    }

    public static ProposalVoting ofAmendment(long proposerID, int oldNumber, String newText) {
        int number = DBUtil.getNextProposalID();
        Rule affectedRule = DBUtil.getRuleByID(oldNumber);
        if (affectedRule == null || affectedRule.getMutability() == RuleMutability.IMMUTABLE) {
            throw new IllegalStateException("Cannot amend an immutable rule.");
        }
        return new ProposalVoting(number, 0, proposerID, newText, oldNumber, AMENDMENT, createVoteEndingTimestamp());
    }

    public static ProposalVoting ofRepeal(long proposerID, int repealedNumber) {
        int number = DBUtil.getNextProposalID();
        Rule affectedRule = DBUtil.getRuleByID(repealedNumber);
        if (affectedRule == null || affectedRule.getMutability() == RuleMutability.IMMUTABLE) {
            throw new IllegalStateException("Cannot repeal an immutable rule.");
        }
        return new ProposalVoting(number, 0, proposerID, "", repealedNumber, REPEAL, createVoteEndingTimestamp());
    }

    public static ProposalVoting ofTransmutation(long proposerID, int transmutedNumber) {
        int number = DBUtil.getNextProposalID();
        Rule rule = DBUtil.getRuleByID(transmutedNumber);
        return new ProposalVoting(number, 0, proposerID, "", transmutedNumber, rule.getMutability() == RuleMutability.MUTABLE ? ProposalType.TRANSMUTATION_MUTABLE_TO_IMMUTABLE : ProposalType.TRANSMUTATION_IMMUTABLE_TO_MUTABLE, createVoteEndingTimestamp());
    }

    private static Timestamp createVoteEndingTimestamp() {
        return Timestamp.from(Instant.now().plusSeconds(VOTE_DURATION.toSeconds()));
    }

    public Timestamp voteExpiry() {
        return voteExpiry;
    }

    private Proposal toEnacted() {
        return toState(ENACTED);
    }

    private Proposal toDefeated() {
        return toState(DEFEATED);
    }

    private Proposal toState(ProposalState newState) {
        return new Proposal(getNumber(), getMessageID(), getProposerID(), getText(), getAffectedRule(), newState, getType());
    }

    public void updateVotes() {
        yeaVotesCache = DBUtil.getYeaVoteCount(getNumber());
        nayVotesCache = DBUtil.getNayVoteCount(getNumber());
        abstentionsCache = getVoterCount() - getYeaVoteCount() - getNayVoteCount();
    }

    public Pair<Proposal, Optional<Rule>> checkPreemptives() {
        updateVotes();
        if (getType().getVotesRequired().preemptivePassCheck(getYeaVoteCount(), getNayVoteCount(), getAbstentionCount())) {
            Proposal asEnacted = toEnacted();
            Rule asRule = asEnactedRule();
            return Pair.of(asEnacted, Optional.ofNullable(asRule));
        }
        if (getType().getVotesRequired().preemptiveFailCheck(getYeaVoteCount(), getNayVoteCount(), getAbstentionCount())) {
            return Pair.of(toDefeated(), Optional.empty());
        }
        return Pair.of(this, Optional.empty());
    }

    private Rule asEnactedRule() {
        return getType() != ProposalType.REPEAL ? Rule.ofproposal(toEnacted()) : null;
    }

    public Pair<Proposal, Optional<Rule>> closeVoting() {
        updateVotes();
        Pair<Proposal, Optional<Rule>> afterPreemptive = checkPreemptives();
        if (afterPreemptive.getLeft() != this) {
            return afterPreemptive;
        }
        if (getType().getVotesRequired().closingCheck(getYeaVoteCount(), getNayVoteCount(), getAbstentionCount())) {
            Proposal asEnacted = toEnacted();
            return Pair.of(asEnacted, Optional.ofNullable(asEnactedRule()));
        }
        return Pair.of(toDefeated(), Optional.empty());
    }

    @Override
    public ProposalState getState() {
        return VOTING;
    }

    @Override
    public ProposalVoting withMessageID(long newMessageID) {
        return new ProposalVoting(getNumber(), newMessageID, getProposerID(), getText(), getAffectedRule(), getType(), voteExpiry());
    }
}
