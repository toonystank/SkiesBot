package com.edward.skiesbot.modules;

import java.util.Set;

public class ModuleActions {

    private final boolean isEnabled;
    private final Set<String> actions;

    public ModuleActions(boolean isEnabled, Set<String> actions) {
        this.isEnabled = isEnabled;
        this.actions = actions;
    }
    public boolean isEnabled() {
        return isEnabled;
    }
    public Set<String> getActions() {
        return actions;
    }

}
