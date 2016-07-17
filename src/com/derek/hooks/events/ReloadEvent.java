package com.derek.hooks.events;

import com.derek.MusicPlayer;

public class ReloadEvent extends PlayerEvent
{
    public ReloadEvent(MusicPlayer player)
    {
        super(player);
    }
}
