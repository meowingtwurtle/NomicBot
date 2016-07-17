package com.derek.hooks.events;

import com.derek.MusicPlayer;

public abstract class PlayerEvent
{
    protected final MusicPlayer player;

    public PlayerEvent(MusicPlayer player)
    {
        this.player = player;
    }

    public MusicPlayer getPlayer()
    {
        return player;
    }
}