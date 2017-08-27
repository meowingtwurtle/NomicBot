package com.srgood.reasons.impl.commands.nomic;

public enum VoteType {
    NAY(-1), ABSTAIN(0), YEA(1);

    private final int value;

    VoteType(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }
}
