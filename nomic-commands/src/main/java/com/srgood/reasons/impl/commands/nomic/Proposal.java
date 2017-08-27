package com.srgood.reasons.impl.commands.nomic;

import java.util.Objects;

import static com.srgood.reasons.impl.commands.nomic.JDAUtil.getVoterCount;

public class Proposal {

    protected final int number;
    protected final long messageID;
    protected final long proposerID;
    protected final String text;
    protected final int affectedRule;
    protected final ProposalState proposalState;
    protected final ProposalType proposalType;

    protected int yeaVotesCache;
    protected int nayVotesCache;
    protected int abstentionsCache;


    public Proposal(int number, long messageID, long proposerID, String text, int affectedRule, ProposalState proposalState, ProposalType proposalType) {
        this.number = number;
        this.messageID = messageID;
        this.proposerID = proposerID;
        this.text = text;
        this.affectedRule = affectedRule;
        this.proposalState = proposalState;
        this.proposalType = proposalType;

        yeaVotesCache = DBUtil.getYeaVoteCount(number);
        nayVotesCache = DBUtil.getNayVoteCount(number);
        abstentionsCache = getVoterCount() - getYeaVoteCount() - getNayVoteCount();
    }

    public Proposal(Proposal toCopy) {
        this.number = toCopy.getNumber();
        this.messageID = toCopy.getMessageID();
        this.proposerID = toCopy.getProposerID();
        this.text = toCopy.getText();
        this.affectedRule = toCopy.getAffectedRule();
        this.proposalState = toCopy.getState();
        this.proposalType = toCopy.getType();
    }

    public int getNumber() {
        return number;
    }

    public long getMessageID() {
        return messageID;
    }

    public long getProposerID() {
        return proposerID;
    }

    public String getText() {
        return text;
    }

    public int getAffectedRule() {
        return affectedRule;
    }

    public ProposalState getState() {
        return proposalState;
    }

    public ProposalType getType() {
        return proposalType;
    }

    public int getYeaVoteCount() {
        return yeaVotesCache;
    }

    public int getNayVoteCount() {
        return nayVotesCache;
    }

    public int getAbstentionCount() {
        return abstentionsCache;
    }

    public Proposal withMessageID(long newMessageID) {
        return new Proposal(getNumber(), newMessageID, getProposerID(), getText(), getAffectedRule(), getState(), getType());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Proposal)) {
            return false;
        }
        Proposal that = (Proposal) o;
        return getNumber() == that.getNumber() && getMessageID() == that.getMessageID() && getProposerID() == that.getProposerID() && getAffectedRule() == that.getAffectedRule() && Objects
                .equals(getText(), that.getText()) && getState() == that.getState() && getType() == that.getType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNumber(), getMessageID(), getProposerID(), getText(), getAffectedRule(), getState(), getType());
    }
}
