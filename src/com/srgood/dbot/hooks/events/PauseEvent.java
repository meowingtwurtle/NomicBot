package com.srgood.dbot.hooks.events;

import com.srgood.dbot.MusicPlayer;

public class PauseEvent extends PlayerEvent
{
    public PauseEvent(MusicPlayer player)
    {
        super(player);
    }
}