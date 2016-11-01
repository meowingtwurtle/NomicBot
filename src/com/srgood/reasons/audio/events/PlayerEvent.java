package com.srgood.reasons.audio.events;

import com.srgood.reasons.audio.MusicPlayer;

public abstract class PlayerEvent {
    private final MusicPlayer player;

    PlayerEvent(MusicPlayer player) {
        this.player = player;
    }

    public MusicPlayer getPlayer() {
        return player;
    }
}