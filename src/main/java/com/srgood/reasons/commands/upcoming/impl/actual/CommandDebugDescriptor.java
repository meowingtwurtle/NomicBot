package com.srgood.reasons.commands.upcoming.impl.actual;

import com.srgood.reasons.ReasonsMain;
import com.srgood.reasons.commands.upcoming.CommandExecutionData;
import com.srgood.reasons.commands.upcoming.impl.base.descriptor.BaseCommandDescriptor;
import com.srgood.reasons.commands.upcoming.impl.base.descriptor.MultiTierCommandDescriptor;
import com.srgood.reasons.commands.upcoming.impl.base.executor.ChannelOutputCommandExecutor;
import com.srgood.reasons.utils.GuildUtils;
import net.dv8tion.jda.core.entities.Role;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

public class CommandDebugDescriptor extends MultiTierCommandDescriptor {
    private static final boolean ALLOW_DEBUG = true;

    public CommandDebugDescriptor() {
        super(new HashSet<>(
              Arrays.asList(
                        new DeleteGuildDescriptor(),
                        new RemoveRolesDescriptor(),
                        new UptimeDescriptor())),
              "FOR DEBUG ONLY",
              Collections.singletonList("debug"));
    }

    private static abstract class BaseDebugExecutor extends ChannelOutputCommandExecutor {
        public BaseDebugExecutor(CommandExecutionData executionData) {
            super(executionData);
        }

        @Override
        public boolean shouldExecute() {
            return ALLOW_DEBUG;
        }
    }

    private static class DeleteGuildDescriptor extends BaseCommandDescriptor {
        public DeleteGuildDescriptor() {
            super(Executor::new, "", "deleteguild");
        }

        private static class Executor extends BaseDebugExecutor {
            public Executor(CommandExecutionData executionData) {
                super(executionData);
            }

            @Override
            public void execute() {
                GuildUtils.deleteGuild(executionData.getGuild());
            }
        }
    }

    private static class RemoveRolesDescriptor extends BaseCommandDescriptor {
        public RemoveRolesDescriptor() {
            super(Executor::new, "", "removeroles");
        }

        private static class Executor extends BaseDebugExecutor {
            public Executor(CommandExecutionData executionData) {
                super(executionData);
            }

            @Override
            public void execute() {
                for (Role r : executionData.getGuild().getRoles()) {
                    if (r.getName().equals("Reasons Admin") || r.getName().equals("DJ")) {
                        r.delete()
                         .queue(role -> sendOutput("Removed role: **`%s`**", r.getName()));
                    }
                }
            }
        }
    }

    private static class UptimeDescriptor extends BaseCommandDescriptor {
        public UptimeDescriptor() {
            super(Executor::new, "", "uptime");
        }

        private static class Executor extends BaseDebugExecutor {

            public Executor(CommandExecutionData executionData) {
                super(executionData);
            }

            @Override
            public void execute() {
                long seconds = Duration.between(ReasonsMain.START_INSTANT, Instant.now()).getSeconds();
                long absSeconds = Math.abs(seconds);
                String positive = String.format("%d:%02d:%02d", absSeconds / 3600, (absSeconds % 3600) / 60, absSeconds % 60);
                String x = seconds < 0 ? "-" + positive : positive;
                sendOutput(x);
            }
        }
    }
}
