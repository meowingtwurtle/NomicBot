package com.srgood.dbot.hooks.events;

import com.srgood.dbot.MusicPlayer;

public class ResumeEvent extends PlayerEvent {
    public ResumeEvent(MusicPlayer player) {
        super(player);
    }
}
