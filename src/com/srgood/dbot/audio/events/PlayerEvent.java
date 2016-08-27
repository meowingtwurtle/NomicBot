package com.srgood.dbot.audio.events;

import com.srgood.dbot.audio.MusicPlayer;

public abstract class PlayerEvent {
    private final MusicPlayer player;

    PlayerEvent(MusicPlayer player) {
        this.player = player;
    }

    public MusicPlayer getPlayer() {
        return player;
    }
}