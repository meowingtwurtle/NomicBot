package com.srgood.dbot.hooks.events;

import com.srgood.dbot.MusicPlayer;

public class FinishEvent extends PlayerEvent {
    public FinishEvent(MusicPlayer player) {
        super(player);
    }
}