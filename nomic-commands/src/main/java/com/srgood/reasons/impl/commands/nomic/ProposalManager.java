package com.srgood.reasons.impl.commands.nomic;

import net.dv8tion.jda.core.exceptions.ErrorResponseException;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

import static com.srgood.reasons.impl.commands.nomic.JDAUtil.getRulesChannel;
import static com.srgood.reasons.impl.commands.nomic.ProposalState.ENACTED;
import static com.srgood.reasons.impl.commands.nomic.ProposalState.VOTING;
import static com.srgood.reasons.impl.commands.nomic.ProposalType.*;

public class ProposalManager {
    public static void updateRule(Rule rule) {
        DBUtil.writeRule(rule);
        MessageUtil.displayRule(rule);
    }

    public static void closeAndWriteproposal(Proposal proposal) {
        if (proposal instanceof ProposalVoting) {
            Pair<Proposal, Optional<Rule>> closeResult = ((ProposalVoting) proposal).closeVoting();
            handleClosing(closeResult);
        }
    }

    private static void handleClosing(Pair<Proposal, Optional<Rule>> closeResult) {
        updateProposal(closeResult.getLeft());
        closeResult.getRight().ifPresent(ProposalManager::updateRule);
        if (closeResult.getLeft().getState() == ENACTED && closeResult.getLeft().getType() != ENACTMENT) {
            removeRuleAfterClosing(closeResult);
        }
    }

    public static void updateProposal(Proposal proposal) {
        DBUtil.writeProposal(proposal);
        MessageUtil.displayProposal(proposal);
        if (proposal instanceof ProposalVoting) {
            SchedulingUtil.registerVoteClosing((ProposalVoting) proposal);
        }
    }

    private static void removeRuleAfterClosing(Pair<Proposal, Optional<Rule>> closeResult) {
        int removalID = closeResult.getLeft().getAffectedRule();
        Rule toRemove = DBUtil.getRuleByID(removalID);
        DBUtil.removeRule(removalID);
        try {
            getRulesChannel().deleteMessageById(toRemove.getMessageID()).complete();
        } catch (ErrorResponseException e) {
            // Message probably deleted, ignore.
        }
    }

    public static void castVote(ProposalVoting proposal, long voterID, VoteType voteType) {
        if (voteType == VoteType.ABSTAIN) {
            DBUtil.removeVote(proposal.getNumber(), voterID);
        } else {
            DBUtil.writeVote(proposal.getNumber(), voterID, voteType);
        }
        proposal.updateVotes();

        Pair<Proposal, Optional<Rule>> preemptiveResult = proposal.checkPreemptives();
        if (preemptiveResult.getLeft() != proposal) {
            handleClosing(preemptiveResult);
            return;
        }

        updateProposal(proposal);
    }

    public static OptionalInt getMatchingProposal(ProposalType proposalType, int affectedRule) {
        if (List.of(ENACTMENT, AMENDMENT, INITIAL_RULE).contains(proposalType)) {
            return OptionalInt.empty();
        }

        return DBUtil.getAllProposals()
                     .stream()
                     .filter(proposal -> proposal.getState() == VOTING)
                     .filter(proposal -> proposal.getAffectedRule() == affectedRule)
                     .filter(proposal -> proposal.getType() == proposalType)
                     .mapToInt(Proposal::getNumber)
                     .findFirst();
    }
}
