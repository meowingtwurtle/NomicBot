package com.srgood.dbot.hooks;

import com.srgood.dbot.hooks.events.PlayerEvent;

public interface PlayerEventListener
{
    void onEvent(PlayerEvent event);
}

