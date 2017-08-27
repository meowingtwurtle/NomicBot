package com.srgood.reasons.impl.commands.main;


import com.srgood.reasons.impl.commands.nomic.VoteType;

public class CommandNayDescriptor extends BaseVoteCommandDescriptor {
    public CommandNayDescriptor() {
        super(VoteType.NAY,"Casts a vote opposing the proposal with the number given", "nay", "(vote)?(nay|no|down|oppose)(vote)?");
    }
}
