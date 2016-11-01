package com.srgood.reasons.audio.events;

import com.srgood.reasons.audio.MusicPlayer;

public class StopEvent extends PlayerEvent {
    public StopEvent(MusicPlayer player) {
        super(player);
    }
}