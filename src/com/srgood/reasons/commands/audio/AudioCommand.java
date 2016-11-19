package com.srgood.reasons.commands.audio;

import com.srgood.reasons.audio.MusicPlayer;
import com.srgood.reasons.commands.Command;

interface AudioCommand extends Command {
    float DEFAULT_VOLUME = 0.35f;

    static MusicPlayer initAndGetPlayer(AudioManager manager) {
        MusicPlayer player;
        if (manager.getSendingHandler() == null) {
            player = new MusicPlayer();
            player.setVolume(DEFAULT_VOLUME);
            manager.setSendingHandler(player);
        } else {
            player = (MusicPlayer) manager.getSendingHandler();
        }
        return player;
    }
}
