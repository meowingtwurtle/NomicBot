package com.derek.hooks.events;

import com.derek.MusicPlayer;

public class PauseEvent extends PlayerEvent
{
    public PauseEvent(MusicPlayer player)
    {
        super(player);
    }
}