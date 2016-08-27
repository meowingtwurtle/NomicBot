package com.srgood.dbot.audio.events;

import com.srgood.dbot.audio.MusicPlayer;

public class FinishEvent extends PlayerEvent {
    public FinishEvent(MusicPlayer player) {
        super(player);
    }
}