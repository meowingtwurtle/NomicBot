package com.srgood.reasons.impl.commands.impl.actual;

import com.srgood.reasons.impl.commands.impl.base.descriptor.SimpleTextCommandDescriptor;

public class CommandPingDescriptor extends SimpleTextCommandDescriptor {
    public CommandPingDescriptor() {
        super("ping", "Pong!");
    }
}
