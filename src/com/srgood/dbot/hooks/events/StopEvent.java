package com.srgood.dbot.hooks.events;

import com.srgood.dbot.MusicPlayer;

public class StopEvent extends PlayerEvent {
    public StopEvent(MusicPlayer player) {
        super(player);
    }
}