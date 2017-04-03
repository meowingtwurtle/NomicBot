package com.srgood.reasons.commands.impl.actual;

import com.srgood.reasons.commands.CommandExecutionData;
import com.srgood.reasons.commands.impl.base.descriptor.BaseCommandDescriptor;
import com.srgood.reasons.commands.impl.base.descriptor.MultiTierCommandDescriptor;
import com.srgood.reasons.commands.impl.base.executor.DMOutputCommandExecutor;
import com.srgood.reasons.permissions.*;
import com.srgood.reasons.utils.RoleUtils;
import net.dv8tion.jda.core.entities.Role;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

public class CommandPermissionsDescriptor extends MultiTierCommandDescriptor {
    public CommandPermissionsDescriptor() {
        super(new HashSet<>(Arrays.asList(new ListDescriptor(), new SetDescriptor())), "Gets and modifies information about permissions for roles", "<list | set> <...>", Collections
                .singletonList("permissions"));
    }

    private static class ListDescriptor extends BaseCommandDescriptor {
        public ListDescriptor() {
            super(Executor::new, "Lists permissions for all Roles in the current Guild", "<>", "list");
        }

        private static class Executor extends DMOutputCommandExecutor {
            public Executor(CommandExecutionData executionData) {
                super(executionData);
            }

            @Override
            public void execute() {
                if (executionData.getParsedArguments().size() == 0) {
                    displayAllRoles();
                } else {
                    try {
                        sendOutput(formatPermissions(RoleUtils.getUniqueRole(executionData.getGuild(), executionData.getParsedArguments()
                                                                                                                    .get(0))));
                    } catch (IllegalArgumentException e) {
                        sendOutput(e.getMessage());
                    }
                }
            }

            private void displayAllRoles() {
                sendOutput("**`Roles in %s`**", executionData.getGuild().getName());
                for (Role role : executionData.getGuild().getRoles()) {
                    sendOutput(formatPermissions(role));
                }
            }

            private String formatPermissions(Role role) {
                GuildPermissionSet permissionSet = GuildDataManager.getGuildPermissionSet(role.getGuild());
                StringBuilder builder = new StringBuilder();
                builder.append(String.format("```Markdown\n[%s]\n", role.getName()));
                for (Permission permission : Permission.values()) {
                    builder.append(String.format("[%s](%s)\n", permission, permissionSet.getPermissionStatus(role, permission)));
                }
                builder.append("```");
                return builder.toString();
            }
        }
    }

    private static class SetDescriptor extends BaseCommandDescriptor {
        public SetDescriptor() {
            super(Executor::new, "Sets a permission's status for the given role", "<role> <permission> <status>", "set");
        }

        private static class Executor extends DMOutputCommandExecutor {
            public Executor(CommandExecutionData executionData) {
                super(executionData);
            }

            @Override
            public void execute() {
                Role role;
                try {
                    role = RoleUtils.getUniqueRole(executionData.getGuild(), executionData.getParsedArguments().get(0));
                } catch (IllegalArgumentException e) {
                    sendOutput(e.getMessage());
                    return;
                }

                PermissionStatus status;
                try {
                    status = getPermissionStatus(executionData.getParsedArguments().get(2));
                } catch (IllegalArgumentException e) {
                    sendOutput("Invalid permission state. Options are `ALLOW`, `DEFER`, `DENY`");
                    return;
                }

                if (status == PermissionStatus.DEFERRED && role.getGuild().getPublicRole().equals(role)) {
                    sendOutput("Cannot defer on the everyone role.");
                    return;
                }

                GuildPermissionSet permissionSet = GuildDataManager.getGuildPermissionSet(executionData.getGuild());

                if (executionData.getParsedArguments().get(1).equalsIgnoreCase("all")) {
                    for (Permission permission : Permission.values()) {
                        permissionSet.setPermissionStatus(role, permission, status);
                        sendOutput("Permission **`%s`** set to state **`%s`** for role **`%s`**", permission, status, role.getName());
                    }
                } else {
                    try {
                        Permission permission = Permission.valueOf(executionData.getParsedArguments()
                                                                                .get(1)
                                                                                .toUpperCase()
                                                                                .replaceAll("\\s+", "_"));
                        permissionSet.setPermissionStatus(role, permission, status);
                        sendOutput("Permission **`%s`** set to state **`%s`** for role **`%s`**", permission, status, role.getName());
                    } catch (IllegalArgumentException e) {
                        sendOutput("Found no permission by that name.");
                        return;
                    }
                }

                GuildDataManager.setGuildPermissionSet(executionData.getGuild(), permissionSet);
            }

            private PermissionStatus getPermissionStatus(String name) {
                name = name.toLowerCase().trim();
                if (name.matches("allow(ed)?")) {
                    return PermissionStatus.ALLOWED;
                }
                if (name.matches("den(y|ied)")) {
                    return PermissionStatus.DENIED;
                }
                if (name.matches("defer(red)?")) {
                    return PermissionStatus.DEFERRED;
                }
                throw new IllegalArgumentException();
            }

            @Override
            protected void checkCallerPermissions() {
                PermissionChecker.checkMemberPermission(executionData.getSender(), Permission.MANAGE_PERMISSIONS);
            }
        }
    }
}
