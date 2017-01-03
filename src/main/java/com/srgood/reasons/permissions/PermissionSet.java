package com.srgood.reasons.permissions;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.srgood.reasons.permissions.PermissionStatus.*;

public class PermissionSet implements Serializable {

    private Map<PermissibleAction, PermissionStatus> permissionMap = new HashMap<>();



    public PermissionSet(Collection<PermissibleAction> allowedActions) {
        this(allowedActions, null, null);
    }

    public PermissionSet(Collection<PermissibleAction> allowedActions, Collection<PermissibleAction> deniedActions) {
        this(allowedActions, deniedActions, null);
    }

    public PermissionSet(Collection<PermissibleAction> allowedActions, Collection<PermissibleAction> deniedActions, Collection<PermissibleAction> deferredActions) {
        this();

        if (allowedActions != null) {
            for (PermissibleAction action : allowedActions) {
                permissionMap.put(action, ALLOWED);
            }
        }

        if (deniedActions != null) {
            for (PermissibleAction action : deniedActions) {
                permissionMap.put(action, DENIED);
            }
        }

        if (deferredActions != null) {
            for (PermissibleAction action : deferredActions) {
                permissionMap.put(action, DEFERRED);
            }
        }
    }

    public PermissionSet(PermissionSet setToDuplicate) {
        permissionMap = new HashMap<>(new HashMap<>(setToDuplicate.permissionMap));
    }

    public PermissionSet() {
        populatePermissionMap();
    }

    private void populatePermissionMap() {
        for (PermissibleAction action : PermissibleAction.values()) {
            if (!permissionMap.containsKey(action)) {
                permissionMap.put(action, DEFERRED);
            }
        }
    }



    public Set<PermissibleAction> getAllowedActions() {
        return getActionsByStatus(ALLOWED);
    }

    public Set<PermissibleAction> getDeniedActions() {
        return getActionsByStatus(DENIED);
    }

    public Set<PermissibleAction> getDefferedActions() {
        return getActionsByStatus(DEFERRED);
    }

    public Set<PermissibleAction> getActionsByStatus(PermissionStatus action) {
        return permissionMap.entrySet()
                            .stream()
                            .filter(entry -> entry.getValue() == action)
                            .map(Map.Entry::getKey)
                            .collect(Collectors.toSet());
    }

    public void setActionStatus(PermissibleAction action, PermissionStatus status) {
        permissionMap.put(action, status);
    }

}
