package com.srgood.reasons.audio.events;

import com.srgood.reasons.audio.MusicPlayer;

public class NextEvent extends PlayerEvent {

    public NextEvent(MusicPlayer player) {
        super(player);
    }
}