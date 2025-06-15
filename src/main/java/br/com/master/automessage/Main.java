package br.com.master.automessage;

import br.com.master.automessage.commands.AutoMessageCommand;
import br.com.master.automessage.config.ConfigManager;
import br.com.master.automessage.listeners.PlayerJoinListener;
import br.com.master.automessage.managers.MessageManager;
import br.com.master.automessage.tasks.MessageTask;
import br.com.master.automessage.utils.MessageUtils;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private static Main instance;
    private ConfigManager configManager;
    private MessageManager messageManager;
    private MessageTask messageTask;

    @Override
    public void onEnable() {
        instance = this;

        // Inicializar configurações
        this.configManager = new ConfigManager(this);
        configManager.loadConfigs();

        // Inicializar managers
        this.messageManager = new MessageManager(this);

        // Registrar comandos
        registerCommands();

        // Registrar listeners
        registerListeners();

        // Iniciar task de mensagens automáticas
        startMessageTask();

        getLogger().info("AutoMessage plugin habilitado com sucesso!");
    }

    @Override
    public void onDisable() {
        if (messageTask != null) {
            messageTask.stop();
        }
        getLogger().info("AutoMessage plugin desabilitado!");
    }

    private void registerCommands() {
        getCommand("automessage").setExecutor(new AutoMessageCommand(this));
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
    }

    private void startMessageTask() {
        this.messageTask = new MessageTask(this);
        messageTask.start();
    }

    public void reload() {
        // Parar task atual
        if (messageTask != null) {
            messageTask.stop();
        }

        // Recarregar configurações
        configManager.loadConfigs();

        // Reiniciar task
        startMessageTask();
    }

    // Getters
    public static Main getInstance() {
        return instance;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public MessageManager getMessageManager() {
        return messageManager;
    }
}