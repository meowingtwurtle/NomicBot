package com.srgood.reasons.impl.config;

import com.srgood.reasons.commands.CommandDescriptor;
import com.srgood.reasons.config.CommandConfigManager;
import com.srgood.reasons.config.GuildConfigManager;
import com.srgood.reasons.config.MemberConfigManager;
import com.srgood.reasons.config.RoleConfigManager;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import org.w3c.dom.Element;

import java.util.HashMap;
import java.util.Map;

public class GuildConfigManagerImpl extends BasicConfigManagerImpl implements GuildConfigManager {
    private static final String ROLES_TAG_NAME = "roles";
    private static final String ROLE_TAG_NAME = "role";
    private static final String ROLE_ID_ATTRIBUTE_NAME = "id";
    private static final String MEMBERS_TAG_NAME = "members";
    private static final String MEMBER_TAG_NAME = "member";
    private static final String MEMBER_ID_ATTRIBUTE_NAME = "id";
    private static final String COMMANDS_TAG_NAME = "commands";
    private static final String COMMAND_TAG_NAME = "command";
    private static final String COMMAND_NAME_ATTRIBUTE_NAME = "name";

    private Element guildElement;

    public GuildConfigManagerImpl(Element guildElement) {
        super(guildElement);
        this.guildElement = guildElement;
    }

    @Override
    public MemberConfigManager getMemberConfigManager(Member member) {
        checkState();
        Map<String, String> attributeMap = new HashMap<>();
        attributeMap.put(MEMBER_ID_ATTRIBUTE_NAME, member.getUser().getId());
        Element roleElement = ConfigUtils.getOrCreateChildElementWithAttributes(
                ConfigUtils.getOrCreateChildElement(
                        guildElement,
                        MEMBERS_TAG_NAME),
                MEMBER_TAG_NAME,
                attributeMap);
        return new MemberConfigManagerImpl(roleElement);
    }

    @Override
    public RoleConfigManager getRoleConfigManager(Role role) {
        checkState();
        Map<String, String> attributeMap = new HashMap<>();
        attributeMap.put(ROLE_ID_ATTRIBUTE_NAME, role.getId());
        Element roleElement = ConfigUtils.getOrCreateChildElementWithAttributes(
                ConfigUtils.getOrCreateChildElement(
                        guildElement,
                        ROLES_TAG_NAME),
                ROLE_TAG_NAME,
                attributeMap);
        return new RoleConfigManagerImpl(roleElement);
    }

    @Override
    public CommandConfigManager getCommandConfigManager(CommandDescriptor command) {
        checkState();
        Map<String, String> attributeMap = new HashMap<>();
        attributeMap.put(COMMAND_NAME_ATTRIBUTE_NAME, command.getPrimaryName());
        Element commandElement = ConfigUtils.getOrCreateChildElementWithAttributes(
                ConfigUtils.getOrCreateChildElement(
                        guildElement,
                        COMMANDS_TAG_NAME),
                COMMAND_TAG_NAME,
                attributeMap);
        return new CommandConfigManagerImpl(commandElement);
    }

    @Override
    public void delete() {
        checkState();
        guildElement.getParentNode().removeChild(guildElement);
        guildElement = null;
    }

    private void checkState() {
        if (guildElement == null) {
            throw new IllegalStateException("This GuildConfigManager has been deleted. If more operations must be done, get another one from the BotConfigManager.");
        }
    }
}
