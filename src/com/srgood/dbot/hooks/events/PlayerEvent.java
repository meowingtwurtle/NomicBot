package com.srgood.dbot.hooks.events;

import com.srgood.dbot.MusicPlayer;

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