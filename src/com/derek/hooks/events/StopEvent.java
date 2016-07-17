package com.derek.hooks.events;

import com.derek.MusicPlayer;

public class StopEvent extends PlayerEvent
{
    public StopEvent(MusicPlayer player)
    {
        super(player);
    }
}