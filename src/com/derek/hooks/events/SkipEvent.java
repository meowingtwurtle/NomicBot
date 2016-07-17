package com.derek.hooks.events;

import com.derek.MusicPlayer;
import com.derek.source.AudioSource;

public class SkipEvent extends PlayerEvent
{
    protected final AudioSource skippedSource;

    public SkipEvent(MusicPlayer player, AudioSource skippedSource)
    {
        super(player);
        this.skippedSource = skippedSource;
    }

    public AudioSource getSkippedSource()
    {
        return skippedSource;
    }
}
