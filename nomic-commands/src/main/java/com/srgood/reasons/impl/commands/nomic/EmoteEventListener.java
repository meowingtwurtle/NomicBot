package com.srgood.reasons.impl.commands.nomic;

import net.dv8tion.jda.core.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class EmoteEventListener extends ListenerAdapter {
    @Override
    public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event) {
        if (event.getUser().isBot()) {
            return;
        }
        Proposal proposal = DBUtil.getProposalByMessageID(event.getMessageIdLong());
        if (proposal == null || !(proposal instanceof ProposalVoting)) return;
        if (event.getReactionEmote().getName().equals("\u2611")) {
            ProposalManager.castVote(((ProposalVoting) proposal), event.getUser().getIdLong(), VoteType.YEA);
            event.getReaction().removeReaction(event.getUser()).complete();
        } else if (event.getReactionEmote().getName().equals("\uD83C\uDDFD")) {
            ProposalManager.castVote(((ProposalVoting) proposal), event.getUser().getIdLong(), VoteType.NAY);
            event.getReaction().removeReaction(event.getUser()).complete();
        }
    }
}
