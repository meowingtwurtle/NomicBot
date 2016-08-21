package com.srgood.dbot.hooks.events;

import com.srgood.dbot.MusicPlayer;

public abstract class PlayerEvent {
    private final MusicPlayer player;

    PlayerEvent(MusicPlayer player) {
        this.player = player;
    }

    public MusicPlayer getPlayer() {
        return player;
    }
}