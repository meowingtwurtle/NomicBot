package com.srgood.reasons.impl.utils;

import com.srgood.reasons.BotManager;
import net.dv8tion.jda.core.entities.Channel;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;

public class Vote {

    public Vote(Map<String,Integer> choiceMap, int voteDuration, Channel channel, Runnable action, BotManager botManager) {
        final Instant begin = Instant.now();
        VoteListener voteListener = new VoteListener(channel,choiceMap);
        botManager.getJDA().addEventListener(voteListener);

        Thread thread = new Thread(() -> {
            while (true) {
                if (Duration.between(begin, Instant.now()).getSeconds() >=  voteDuration) {
                    action.run();
                    botManager.getJDA().removeEventListener(voteListener);
                    break;
                }
            }
        });
        thread.start();
    }
}
