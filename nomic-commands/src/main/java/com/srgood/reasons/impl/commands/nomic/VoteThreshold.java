package com.srgood.reasons.impl.commands.nomic;

public enum VoteThreshold {
    SIMPLE_MAJORITY("simple majority",
            (yesVotes, noVotes, abstentions) -> yesVotes > (noVotes + abstentions),
            (yesVotes, noVotes, abstentions) -> noVotes >= (yesVotes + abstentions),
            (yesVotes, noVotes, abstentions) -> yesVotes > noVotes),
    ABSOLUTE_MAJORITY("absolute majority",
            (yesVotes, noVotes, abstentions) -> yesVotes > (noVotes + abstentions),
            (yesVotes, noVotes, abstentions) -> noVotes >= (yesVotes + abstentions),
            (yesVotes, noVotes, abstentions) -> yesVotes > (noVotes + abstentions)),
    TWO_THIRDS("two-thirds",
            (yesVotes, noVotes, abstentions) -> yesVotes >= 2 * (noVotes + abstentions),
            (yesVotes, noVotes, abstentions) -> 2 * noVotes > (yesVotes + abstentions),
            (yesVotes, noVotes, abstentions) -> yesVotes > 0 && yesVotes >= 2 * noVotes),
    THREE_QUARTERS("three-quarters",
            (yesVotes, noVotes, abstentions) -> yesVotes >= 3 * (noVotes + abstentions),
            (yesVotes, noVotes, abstentions) -> 3 * noVotes > (yesVotes + abstentions),
            (yesVotes, noVotes, abstentions) -> yesVotes > 0 && yesVotes >= 3 * noVotes),
    NO_OBJECTIONS("no objections",
            (yesVotes, noVotes, abstentions) -> yesVotes > 0 && noVotes == 0 && abstentions == 0,
            (yesVotes, noVotes, abstentions) -> noVotes > 0,
            (yesVotes, noVotes, abstentions) -> yesVotes > 0 && noVotes == 0),
    ABSOLUTE_UNANIMOUS("unanimous",
            (yesVotes, noVotes, abstentions) -> yesVotes > 0 && noVotes == 0 && abstentions == 0,
            (yesVotes, noVotes, abstentions) -> noVotes > 0,
            (yesVotes, noVotes, abstentions) -> yesVotes > 0 && noVotes == 0 && abstentions == 0);

    private final String readableName;
    private final VoteResultPredicate preemptivePassCheck;
    private final VoteResultPredicate preemptiveFailCheck;
    private final VoteResultPredicate closingCheck;

    VoteThreshold(String readableName, VoteResultPredicate preemptivePassCheck, VoteResultPredicate preemptiveFailCheck, VoteResultPredicate closingCheck) {
        this.readableName = readableName;
        this.preemptivePassCheck = preemptivePassCheck;
        this.preemptiveFailCheck = preemptiveFailCheck;
        this.closingCheck = closingCheck;
    }

    public String readableName() {
        return readableName;
    }

    public boolean preemptivePassCheck(int yesVotes, int noVotes, int abstentions) {
        return preemptivePassCheck.test(yesVotes, noVotes, abstentions);
    }

    public boolean preemptiveFailCheck(int yesVotes, int noVotes, int abstentions) {
        return preemptiveFailCheck.test(yesVotes, noVotes, abstentions);
    }

    public boolean closingCheck(int yesVotes, int noVotes, int abstentions) {
        return closingCheck.test(yesVotes, noVotes, abstentions);
    }

    @FunctionalInterface
    private interface VoteResultPredicate {
        boolean test(int yesVotes, int noVotes, int abstentions);
    }
}
