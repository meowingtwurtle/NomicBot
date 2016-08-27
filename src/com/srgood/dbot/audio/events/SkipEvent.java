package com.srgood.dbot.audio.events;

import com.srgood.dbot.audio.MusicPlayer;
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
