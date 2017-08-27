package com.srgood.reasons.impl.commands.nomic;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SchedulingUtil {
    private static ScheduledExecutorService voteExecutorService = Executors.newSingleThreadScheduledExecutor();

    public static void init() {
        List<Proposal> allProps = DBUtil.getAllProposals();
        allProps.stream()
                .filter(ProposalVoting.class::isInstance)
                .map(ProposalVoting.class::cast)
                .forEach(proposalVoting -> scheduleVoteClosing(proposalVoting.getNumber(), proposalVoting.voteExpiry()
                                                                                                                  .toInstant()));
    }

    public static void registerVoteClosing(ProposalVoting proposalVoting) {
        scheduleVoteClosing(proposalVoting.getNumber(), proposalVoting.voteExpiry().toInstant());
    }

    private static void scheduleVoteClosing(int number, Instant voteExpiry) {
        voteExecutorService.schedule(
                () -> ProposalManager.closeAndWriteproposal(DBUtil.getProposalByID(number)),
                Instant.now().until(voteExpiry, ChronoUnit.SECONDS),
                TimeUnit.SECONDS);
    }

}
