package com.srgood.dbot.audio.events;

import com.srgood.dbot.audio.MusicPlayer;

public class PauseEvent extends PlayerEvent {
    public PauseEvent(MusicPlayer player) {
        super(player);
    }
}