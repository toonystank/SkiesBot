package com.edward.skiesbot.events;

import java.util.Set;

public class ModuleActions {

    private boolean isEnabled;
    private Set<String> actions;

    public ModuleActions(boolean isEnabled, Set<String> actions) {
        this.isEnabled = isEnabled;
        this.actions = actions;
    }

}
