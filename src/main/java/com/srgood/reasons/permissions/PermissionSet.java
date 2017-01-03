package com.srgood.reasons.permissions;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.srgood.reasons.permissions.PermissionSet.PermissionAction.*;

public class PermissionSet implements Serializable {

    private Map<PermissibleAction, PermissionAction> permissionMap = new HashMap<>();

    public PermissionSet(Collection<PermissibleAction> allowedPerms) {
        this(allowedPerms, null, null);
    }

    public PermissionSet(Collection<PermissibleAction> allowedPerms, Collection<PermissibleAction> deniedPerms, Collection<PermissibleAction> deferredPerms) {
        this();

        if (allowedPerms != null) {
            for (PermissibleAction action : allowedPerms) {
                permissionMap.put(action, ALLOW);
            }
        }

        if (deniedPerms != null) {
            for (PermissibleAction action : deniedPerms) {
                permissionMap.put(action, DENY);
            }
        }

        if (deferredPerms != null) {
            for (PermissibleAction action : deferredPerms) {
                permissionMap.put(action, DEFER);
            }
        }
    }

    public PermissionSet() {
        populatePermissionMap();
    }

    private void populatePermissionMap() {
        for (PermissibleAction action : PermissibleAction.values()) {
            if (!permissionMap.containsKey(action)) {
                permissionMap.put(action, DEFER);
            }
        }
    }

    public PermissionSet(Collection<PermissibleAction> allowedPerms, Collection<PermissibleAction> deniedPerms) {
        this(allowedPerms, deniedPerms, null);
    }


    public PermissionSet(PermissionSet setToDuplicate) {
        permissionMap = new HashMap<>(new HashMap<>(setToDuplicate.permissionMap));
    }

    public Set<PermissibleAction> getAllowedActions() {
        return getPermissibleActionsByPermissionAction(ALLOW);
    }

    public Set<PermissibleAction> getDeniedActions() {
        return getPermissibleActionsByPermissionAction(DENY);
    }

    public Set<PermissibleAction> getDefferedActions() {
        return getPermissibleActionsByPermissionAction(DEFER);
    }

    private Set<PermissibleAction> getPermissibleActionsByPermissionAction(PermissionAction action) {
        return permissionMap.entrySet()
                            .stream()
                            .filter(entry -> entry.getValue() == action)
                            .map(Map.Entry::getKey)
                            .collect(Collectors.toSet());
    }

    public enum PermissionAction {
        ALLOW, DEFER, DENY
    }
}
