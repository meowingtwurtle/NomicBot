package com.srgood.reasons.commands.upcoming.impl.actual;

import com.srgood.reasons.commands.upcoming.impl.base.descriptor.SimpleTextCommandDescriptor;

public class CommandPingDescriptor extends SimpleTextCommandDescriptor {
    public CommandPingDescriptor() {
        super("ping", "Pong!");
    }
}
