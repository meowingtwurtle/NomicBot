package com.srgood.reasons.audio.events;

import com.srgood.reasons.audio.MusicPlayer;

public class PauseEvent extends PlayerEvent {
    public PauseEvent(MusicPlayer player) {
        super(player);
    }
}