package com.srgood.reasons.impl.commands.main;

import com.srgood.reasons.impl.commands.nomic.VoteType;

public class CommandAbstainDescriptor extends BaseVoteCommandDescriptor {
    public CommandAbstainDescriptor() {
        super(VoteType.ABSTAIN,"Clears your vote on the proposal with the getNumber given", "abstain", "(vote)?(abstain)(vote)?");
    }
}
