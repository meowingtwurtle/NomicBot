package com.srgood.dbot.audio.events;

import com.srgood.dbot.audio.MusicPlayer;

public class StopEvent extends PlayerEvent {
    public StopEvent(MusicPlayer player) {
        super(player);
    }
}