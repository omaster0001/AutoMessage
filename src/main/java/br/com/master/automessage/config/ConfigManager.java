package br.com.master.automessage.config;

import br.com.master.automessage.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ConfigManager {

    private final Main plugin;
    private FileConfiguration config;
    private FileConfiguration messagesConfig;
    private File messagesFile;

    public ConfigManager(Main plugin) {
        this.plugin = plugin;
    }

    public void loadConfigs() {
        // Carregar config.yml
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        this.config = plugin.getConfig();

        // Carregar messages.yml
        loadMessagesConfig();
    }

    private void loadMessagesConfig() {
        messagesFile = new File(plugin.getDataFolder(), "messages.yml");

        if (!messagesFile.exists()) {
            plugin.saveResource("messages.yml", false);
        }

        messagesConfig = YamlConfiguration.loadConfiguration(messagesFile);
    }

    public void saveMessagesConfig() {
        try {
            messagesConfig.save(messagesFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Erro ao salvar messages.yml: " + e.getMessage());
        }
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public FileConfiguration getMessagesConfig() {
        return messagesConfig;
    }

    // Métodos utilitários para acessar configurações
    public boolean isEnabled() {
        return config.getBoolean("enabled", true);
    }

    public int getInterval() {
        return config.getInt("interval", 300); // 5 minutos por padrão
    }

    public boolean isRandomOrder() {
        return config.getBoolean("random-order", false);
    }

    public List<String> getEnabledWorlds() {
        return config.getStringList("enabled-worlds");
    }

    public String getPrefix() {
        return config.getString("prefix", "&7[&bAutoMessage&7]&r ");
    }
}