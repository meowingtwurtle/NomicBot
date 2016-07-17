package com.derek.hooks.events;

import com.derek.MusicPlayer;

public class ResumeEvent extends PlayerEvent
{
    public ResumeEvent(MusicPlayer player)
    {
        super(player);
    }
}
