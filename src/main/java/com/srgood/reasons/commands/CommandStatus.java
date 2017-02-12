package com.srgood.reasons.commands;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.RateLimitedException;

import java.awt.*;
import java.lang.management.ManagementFactory;

public class CommandStatus implements Command {

    @Override
    public void action(String[] args, GuildMessageReceivedEvent event) throws RateLimitedException {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        Runtime runtime = Runtime.getRuntime();
        String totalMemory = formatMemoryMB(runtime.totalMemory());
        String freeMemory = formatMemoryMB(runtime.freeMemory());
        String usedMemory = formatMemoryMB(runtime.totalMemory() - runtime.freeMemory());
        int threadCount = Thread.getAllStackTraces().size();
        String cpuUsage = (ManagementFactory.getOperatingSystemMXBean().getSystemLoadAverage() * 100) + "%";

        embedBuilder.addField("Used memory", usedMemory, false);
        embedBuilder.addField("Free memory", freeMemory, false);
        embedBuilder.addField("Total memory", totalMemory, false);
        embedBuilder.addField("Total threads", String.valueOf(threadCount), false);
        embedBuilder.addField("CPU Usage", cpuUsage, false);
        embedBuilder.setColor(Color.GREEN);

        event.getChannel().sendMessage(embedBuilder.build()).queue();
    }

    private static String formatMemoryMB(double number) {
        return String.format("%.0fMB", number/ 1024 / 1024);
    }

    @Override
    public String help() {
        return "TODO";
    }
}
