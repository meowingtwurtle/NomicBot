package com.srgood.reasons.impl.commands.nomic;

public enum RuleMutability {
    IMMUTABLE(0), MUTABLE(1);

    private final int id;

    RuleMutability(int id) {
        this.id = id;
    }

    public int getID() {
        return id;
    }
}
