package com.srgood.dbot.hooks.events;

import com.srgood.dbot.MusicPlayer;
import com.srgood.dbot.source.AudioSource;

public class SkipEvent extends PlayerEvent {
    private final AudioSource skippedSource;

    public SkipEvent(MusicPlayer player, AudioSource skippedSource) {
        super(player);
        this.skippedSource = skippedSource;
    }

    public AudioSource getSkippedSource() {
        return skippedSource;
    }
}
