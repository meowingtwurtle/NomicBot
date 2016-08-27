package com.srgood.dbot.audio.events;

import com.srgood.dbot.audio.MusicPlayer;

public class ResumeEvent extends PlayerEvent {
    public ResumeEvent(MusicPlayer player) {
        super(player);
    }
}
