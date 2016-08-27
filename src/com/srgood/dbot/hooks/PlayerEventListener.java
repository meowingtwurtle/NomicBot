package com.srgood.dbot.hooks;

import com.srgood.dbot.audio.events.PlayerEvent;

public interface PlayerEventListener {
    void onEvent(PlayerEvent event);
}

