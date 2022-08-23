package com.edward.skiesbot.utils;

public class PlaceholderParser {
    public enum Placeholders{
        MESSAGE("{message}"),
        USER("{user}"),
        USER_AVATAR("{user_avatar}"),
        MENTION_USER("{mention_user}");
        private final String placeholder;
        Placeholders(String s) {
            this.placeholder = s;
        }
        public String getPlaceholder() {
            return placeholder;
        }
    }
}
