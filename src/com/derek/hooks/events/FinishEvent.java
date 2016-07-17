package com.derek.hooks.events;

import com.derek.MusicPlayer;

public class FinishEvent extends PlayerEvent
{
    public FinishEvent(MusicPlayer player)
    {
        super(player);
    }
}