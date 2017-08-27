package com.srgood.reasons.impl.commands.nomic;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;

public class JDAUtil {
    private static final long PROPOSALS_CHANNEL_ID = 348187157770403841L;
    private static final long RULES_CHANNEL_ID = 348186982070878209L;
    private static final long GUILD_ID = 347911619977805824L;
    private static final long VOTER_ROLE_ID = 348187431444676620L;

    private static TextChannel proposalsChannel;
    private static TextChannel rulesChannel;
    private static Guild guild;
    private static JDA jda;
    private static Role voterRole;

    public static void init(JDA _jda) {
        proposalsChannel = _jda.getTextChannelById(PROPOSALS_CHANNEL_ID);
        rulesChannel = _jda.getTextChannelById(RULES_CHANNEL_ID);
        guild = _jda.getGuildById(GUILD_ID);
        jda = _jda;
        voterRole = guild.getRoleById(VOTER_ROLE_ID);
    }

    public static TextChannel getProposalsChannel() {
        return proposalsChannel;
    }

    public static TextChannel getRulesChannel() {
        return rulesChannel;
    }

    public static Guild getNomicGuild() {
        return guild;
    }

    public static JDA getJDA() {
        return jda;
    }

    public static int getVoterCount() {
        return (int) guild.getMembers().stream().filter(guild -> guild.getRoles().contains(voterRole)).count();
    }
}
