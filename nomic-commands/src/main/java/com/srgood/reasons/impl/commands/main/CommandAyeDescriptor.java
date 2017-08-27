package com.srgood.reasons.impl.commands.main;


import com.srgood.reasons.impl.commands.nomic.VoteType;

public class CommandAyeDescriptor extends BaseVoteCommandDescriptor {
    public CommandAyeDescriptor() {
        super(VoteType.YEA, "Casts a vote in favor of the proposal with the number given", "aye", "(vote)?(aye|yes|up|infavor)(vote)?");
    }
}
