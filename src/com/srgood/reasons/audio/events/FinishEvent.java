package com.srgood.reasons.audio.events;

import com.srgood.reasons.audio.MusicPlayer;

public class FinishEvent extends PlayerEvent {
    public FinishEvent(MusicPlayer player) {
        super(player);
    }
}