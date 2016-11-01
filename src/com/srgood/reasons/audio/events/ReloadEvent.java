package com.srgood.reasons.audio.events;

import com.srgood.reasons.audio.MusicPlayer;

public class ReloadEvent extends PlayerEvent {
    public ReloadEvent(MusicPlayer player) {
        super(player);
    }
}
