package br.com.master.automessage.models;

import org.bukkit.configuration.ConfigurationSection;

import java.util.List;

public class AutoMessage {

    private final String id;
    private final List<String> content;
    private final String permission;
    private final List<String> commands;
    private final String sound;
    private final boolean enabled;

    public AutoMessage(String id, List<String> content, String permission,
                       List<String> commands, String sound, boolean enabled) {
        this.id = id;
        this.content = content;
        this.permission = permission;
        this.commands = commands;
        this.sound = sound;
        this.enabled = enabled;
    }

    public static AutoMessage fromConfig(String id, ConfigurationSection section) {
        if (!section.getBoolean("enabled", true)) {
            return null;
        }

        List<String> content = section.getStringList("content");
        if (content.isEmpty()) {
            return null;
        }

        String permission = section.getString("permission");
        List<String> commands = section.getStringList("commands");
        String sound = section.getString("sound");
        boolean enabled = section.getBoolean("enabled", true);

        return new AutoMessage(id, content, permission, commands, sound, enabled);
    }

    // Getters
    public String getId() {
        return id;
    }

    public List<String> getContent() {
        return content;
    }

    public String getPermission() {
        return permission;
    }

    public boolean hasPermission() {
        return permission != null && !permission.isEmpty();
    }

    public List<String> getCommands() {
        return commands;
    }

    public boolean hasCommands() {
        return commands != null && !commands.isEmpty();
    }

    public String getSound() {
        return sound;
    }

    public boolean hasSound() {
        return sound != null && !sound.isEmpty();
    }

    public boolean isEnabled() {
        return enabled;
    }
}
