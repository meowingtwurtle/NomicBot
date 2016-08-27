package com.srgood.dbot.audio.events;

import com.srgood.dbot.audio.MusicPlayer;

public class ReloadEvent extends PlayerEvent {
    public ReloadEvent(MusicPlayer player) {
        super(player);
    }
}
