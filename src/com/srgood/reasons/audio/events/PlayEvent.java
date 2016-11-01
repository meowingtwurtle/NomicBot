package com.srgood.reasons.audio.events;

import com.srgood.reasons.audio.MusicPlayer;

public class PlayEvent extends PlayerEvent {

    public PlayEvent(MusicPlayer player) {
        super(player);
    }
}

