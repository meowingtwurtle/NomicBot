package com.srgood.reasons.utils;

import net.dv8tion.jda.core.entities.Guild;

import java.util.*;

public class Censor {
    private static Map<String, List<String>> censorMap = new HashMap<>();

    public static List<String> getCensoredWordListForGuild(Guild guild) {
        List<String> result = censorMap.get(guild.getId());
        if (result == null) {
            result = new ArrayList<>();
            censorMap.put(guild.getId(), result);
        }
        return result;
    }
}
