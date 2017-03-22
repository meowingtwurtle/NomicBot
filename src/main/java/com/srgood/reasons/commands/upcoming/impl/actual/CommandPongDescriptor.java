package com.srgood.reasons.commands.upcoming.impl.actual;

import com.srgood.reasons.commands.upcoming.impl.base.descriptor.SimpleTextCommandDescriptor;

public class CommandPongDescriptor extends SimpleTextCommandDescriptor {
    public CommandPongDescriptor() {
        super("pong", "Ping!");
    }
}
