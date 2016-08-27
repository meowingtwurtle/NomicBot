package com.srgood.dbot.audio.events;

import com.srgood.dbot.audio.MusicPlayer;

public class PlayEvent extends PlayerEvent {

    public PlayEvent(MusicPlayer player) {
        super(player);
    }
}

