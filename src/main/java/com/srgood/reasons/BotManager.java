package com.srgood.reasons;

import com.srgood.reasons.commands.CommandManager;
import com.srgood.reasons.config.BotConfigManager;
import net.dv8tion.jda.core.JDA;

import java.time.Instant;
import java.util.logging.Logger;

public interface BotManager {
    void init(String token);
    void shutdown();
    void shutdown(boolean force);

    BotConfigManager getConfigManager();
    CommandManager getCommandManager();
    Logger getLogger();

    JDA getJDA();

    String getDefaultPrefix();
    void setDefaultPrefix(String prefix);

    Instant getStartTime();
}
