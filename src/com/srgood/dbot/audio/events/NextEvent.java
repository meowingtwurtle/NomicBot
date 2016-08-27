package com.srgood.dbot.audio.events;

import com.srgood.dbot.audio.MusicPlayer;

public class NextEvent extends PlayerEvent {

    public NextEvent(MusicPlayer player) {
        super(player);
    }
}