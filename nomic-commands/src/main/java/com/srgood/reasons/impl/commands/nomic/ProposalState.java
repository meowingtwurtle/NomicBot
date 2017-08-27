package com.srgood.reasons.impl.commands.nomic;

public enum ProposalState {
    VOTING(0), ENACTED(1), DEFEATED(-1);

    private final int id;

    ProposalState(int id) {
        this.id = id;
    }

    public int getID() {
        return id;
    }
}
