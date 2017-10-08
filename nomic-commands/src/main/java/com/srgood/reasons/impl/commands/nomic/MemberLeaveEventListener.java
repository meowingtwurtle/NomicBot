package com.srgood.reasons.impl.commands.nomic;

import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.stream.Collectors;

public class MemberLeaveEventListener extends ListenerAdapter {
    @Override
    public void onGuildMemberLeave(GuildMemberLeaveEvent event) {
        for (Proposal proposal : DBUtil.getAllProposals()
                                       .stream()
                                       .filter(proposal -> proposal.getState() == ProposalState.VOTING)
                                       .collect(Collectors.toList())) {
            if (!DBUtil.getVotersByVoteType(proposal.getNumber(), VoteType.ABSTAIN).contains(event.getUser().getIdLong())) {
                ProposalManager.castVote((ProposalVoting) proposal, event.getUser().getIdLong(), VoteType.ABSTAIN);
            }
        }
    }
}
