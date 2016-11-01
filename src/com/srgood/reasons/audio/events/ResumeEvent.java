package com.srgood.reasons.audio.events;

import com.srgood.reasons.audio.MusicPlayer;

public class ResumeEvent extends PlayerEvent {
    public ResumeEvent(MusicPlayer player) {
        super(player);
    }
}
