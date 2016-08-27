package com.srgood.dbot.audio.events;

import com.srgood.dbot.audio.MusicPlayer;

public class RepeatEvent extends PlayerEvent {
    public RepeatEvent(MusicPlayer player) {
        super(player);
    }
}