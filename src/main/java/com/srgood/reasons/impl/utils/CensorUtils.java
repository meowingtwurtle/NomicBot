package com.srgood.reasons.impl.utils;

import com.srgood.reasons.config.GuildConfigManager;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.requests.RestAction;
import net.dv8tion.jda.core.utils.PermissionUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CensorUtils {

    public static List<String> getGuildCensorList(GuildConfigManager guildConfigManager) {
        return guildConfigManager.getSerializedProperty("moderation/censorlist", ArrayList.class, new ArrayList<>(), false);
    }

    public static void setGuildCensorList(GuildConfigManager guildConfigManager, Collection<String> newCensorList) {
        List<String> patchedList = newCensorList.stream()
                                                .sorted()
                                                .distinct()
                                                .collect(Collectors.toCollection(ArrayList::new));
        guildConfigManager.setSerializedProperty("moderation/censorlist", (Serializable) patchedList);
    }

    public static void checkCensor(List<String> censorList, Message message) {
        if (!PermissionUtil.checkPermission(
                ((Channel) message.getChannel()),
                message.getGuild().getSelfMember(),
                Permission.MESSAGE_MANAGE)) {
            return;
        }

        for (String aCensorList : censorList) {
            Pattern p = Pattern.compile("\\b" + aCensorList + "\\b");
            Matcher m = p.matcher(message.getContent().toLowerCase());
            if (m.find()) {
                message.getAuthor()
                       .openPrivateChannel()
                       .queue(channel -> channel.sendMessage(String.format("The word `%s` is not allowed on this server.", aCensorList))
                                                .queue());
                // noinspection unchecked [For IntelliJ]
                message.delete().queue(RestAction.DEFAULT_SUCCESS, RestAction.DEFAULT_SUCCESS);
                break;
            }
        }
    }
}