package com.srgood.reasons.commands.upcoming.impl.actual;

import com.srgood.reasons.commands.upcoming.CommandExecutionData;
import com.srgood.reasons.commands.upcoming.impl.base.descriptor.BaseCommandDescriptor;
import com.srgood.reasons.commands.upcoming.impl.base.executor.ChannelOutputCommandExecutor;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.User;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static com.srgood.reasons.utils.RoleUtils.*;
import static com.srgood.reasons.utils.MemberUtils.*;

public class CommandNotifyRandDescriptor extends BaseCommandDescriptor {
    public CommandNotifyRandDescriptor() {
        super(Executor::new, "Notifies a random Member with the specified role and optional amount","<role name> {amount}", "notifyrand");
    }

    private static class Executor extends ChannelOutputCommandExecutor {
        public Executor(CommandExecutionData executionData) {
            super(executionData);
        }

        @Override
        public void execute() {
            Random random = new Random();

            int amount = 1;

            if (executionData.getParsedArguments().size() > 1) {
                amount = Integer.parseInt(executionData.getParsedArguments().get(1));
            }


            final List<Role> foundRoles = getRolesByName(executionData.getGuild(), executionData.getParsedArguments().get(0));

            if (foundRoles.size() < 1) {
                sendOutput("Found no roles called *%s*", executionData.getParsedArguments().get(0));
                return;
            }

            final Role targetRole = foundRoles.get(0);
            List<Member> foundMembers = getMembersWithRole(getOnlineMembers(executionData.getGuild()), targetRole);
            foundMembers.remove(executionData.getGuild().getSelfMember());

            if (foundMembers.size() < 1) {
                sendOutput("Found no online members with role *%s*", targetRole.getName());
                return;
            }

            List<User> dmTargetUsers = new LinkedList<>();

            int actualMax = Math.min(amount, foundMembers.size());

            for (int i = 0;i < actualMax; i++) {
                Member m = foundMembers.get(random.nextInt(foundMembers.size() + 1));
                assert m != null;
                foundMembers.remove(m);
                dmTargetUsers.add(m.getUser());
            }

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < dmTargetUsers.size(); i++) {
                User u = dmTargetUsers.get(i);
                u.getPrivateChannel().sendMessage(String.format("%s, your presence was requested by *%s* in *%s -> #%s*", u
                        .getName(), executionData.getSender().getAsMention(), executionData.getGuild()
                                                                                           .getName(), executionData.getChannel()
                                                                                                                    .getName())).queue();

                sb.append(u.getName());

                if (i < dmTargetUsers.size() - 3) {
                    sb.append(", ");
                } else if (i == dmTargetUsers.size() - 2) {
                    sb.append(", and ");
                }
            }

            sendOutput("Notified %s", sb.toString());
        }
    }
}
