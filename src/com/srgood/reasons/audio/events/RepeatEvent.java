package com.srgood.reasons.audio.events;

import com.srgood.reasons.audio.MusicPlayer;

public class RepeatEvent extends PlayerEvent {
    public RepeatEvent(MusicPlayer player) {
        super(player);
    }
}