package com.srgood.reasons.permissions;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;

import java.io.Serializable;
import java.util.*;

public class GuildPermissionSet implements Serializable {
    private final String guildID;
    private final Map<String, BasicPermissionSet> rolePermissions = new HashMap<>();
    private transient JDA jda = null;
    private transient boolean initComplete = false;

    private static final Collection<PermissibleAction> PERMISSIBLE_ACTION_COLLECTION = Arrays.asList(PermissibleAction.values());

    public GuildPermissionSet(Guild guild) {
        guildID = guild.getId();
        jda = guild.getJDA();
        initComplete = true;
    }

    public void init(JDA jda) {
        if (this.jda != null) {
            this.jda = jda;
        }
        if (jda.getGuildById(guildID) == null) throw new IllegalStateException("Associated Guild has been deleted");
        Guild guild = jda.getGuildById(guildID);
        for (String roleID : rolePermissions.keySet()) {
            if (guild.getRoleById(roleID) == null) {
                rolePermissions.remove(roleID);
            }
        }
        for (Role role : guild.getRoles()) {
            if (!rolePermissions.containsKey(role.getId())) {
                if (!guild.getPublicRole().equals(role)) {
                    rolePermissions.put(role.getId(), new BasicPermissionSet());
                } else {
                    rolePermissions.put(role.getId(), new BasicPermissionSet(PERMISSIBLE_ACTION_COLLECTION));
                }
            }
        }
        initComplete = true;
    }

    public PermissionStatus getPermissionStatus(Role role, PermissibleAction action) {
        checkInit();
        checkRole(role);
        return rolePermissions.get(role.getId()).getActionStatus(action);
    }

    public void setPermissionStatus(Role role, PermissibleAction action, PermissionStatus permissionStatus) {
        checkInit();
        checkRole(role);
        if (jda.getGuildById(guildID).getPublicRole().equals(role) && permissionStatus == PermissionStatus.DEFERRED) {
            throw new IllegalArgumentException("Cannot defer on the everyone role");
        }
        rolePermissions.get(role.getId()).setActionStatus(action, permissionStatus);
    }

    public boolean checkMemberPermission(Member member, PermissibleAction action) {
        checkInit();
        if (!member.getGuild().getId().equals(guildID)) {
            throw new IllegalArgumentException("Member must be in same guild as registered");
        }
        for (Role role : member.getRoles()) {
            PermissionStatus roleActionStatus = rolePermissions.get(role.getId()).getActionStatus(action);
            if (roleActionStatus != PermissionStatus.DEFERRED) {
                return roleActionStatus == PermissionStatus.ALLOWED;
            }
        }
        return false;
    }

    private void checkInit() {
        if (!initComplete) {
            throw new IllegalStateException("Cannot use GuildPermissionSet before init complete");
        }
    }

    private void checkRole(Role role) {
        if (role == null) {
            throw new IllegalArgumentException("Role cannot be null");
        }
        if (!role.getGuild().equals(jda.getGuildById(guildID))) {
            throw new IllegalArgumentException("Role must be in same guild as registered");
        }
    }
}
