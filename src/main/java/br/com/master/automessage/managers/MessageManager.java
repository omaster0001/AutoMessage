package br.com.master.automessage.managers;

import br.com.master.automessage.Main;
import br.com.master.automessage.models.AutoMessage;
import br.com.master.automessage.utils.MessageUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.*;

public class MessageManager {

    private final Main plugin;
    private final List<AutoMessage> messages;
    private int currentIndex = 0;
    private final Random random = new Random();

    public MessageManager(Main plugin) {
        this.plugin = plugin;
        this.messages = new ArrayList<>();
        loadMessages();
    }

    public void loadMessages() {
        messages.clear();
        currentIndex = 0;

        ConfigurationSection messagesSection = plugin.getConfigManager()
                .getMessagesConfig().getConfigurationSection("messages");

        if (messagesSection == null) {
            plugin.getLogger().warning("Seção 'messages' não encontrada no messages.yml!");
            return;
        }

        for (String key : messagesSection.getKeys(false)) {
            ConfigurationSection messageSection = messagesSection.getConfigurationSection(key);
            if (messageSection != null) {
                AutoMessage autoMessage = AutoMessage.fromConfig(key, messageSection);
                if (autoMessage != null) {
                    messages.add(autoMessage);
                }
            }
        }

        plugin.getLogger().info("Carregadas " + messages.size() + " mensagens automáticas.");
    }

    public void sendNextMessage() {
        if (messages.isEmpty()) {
            return;
        }

        AutoMessage message;

        if (plugin.getConfigManager().isRandomOrder()) {
            message = messages.get(random.nextInt(messages.size()));
        } else {
            message = messages.get(currentIndex);
            currentIndex = (currentIndex + 1) % messages.size();
        }

        sendMessage(message);
    }

    public void sendMessage(AutoMessage message) {
        List<String> enabledWorlds = plugin.getConfigManager().getEnabledWorlds();

        for (Player player : Bukkit.getOnlinePlayers()) {
            // Verificar se o mundo do jogador está habilitado
            if (!enabledWorlds.isEmpty() && !enabledWorlds.contains(player.getWorld().getName())) {
                continue;
            }

            // Verificar permissão
            if (message.hasPermission() && !player.hasPermission(message.getPermission())) {
                continue;
            }

            // Enviar mensagem
            sendMessageToPlayer(player, message);
        }
    }

    private void sendMessageToPlayer(Player player, AutoMessage message) {
        for (String line : message.getContent()) {
            Component component = MessageUtils.parseMessage(line, player);
            player.sendMessage(component);
        }

        // Executar comandos se houver
        if (message.hasCommands()) {
            for (String command : message.getCommands()) {
                String parsedCommand = MessageUtils.parsePlaceholders(command, player);
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), parsedCommand);
            }
        }

        // Tocar som se configurado
        if (message.hasSound()) {
            MessageUtils.playSound(player, message.getSound());
        }
    }

    public List<AutoMessage> getMessages() {
        return new ArrayList<>(messages);
    }

    public int getMessagesCount() {
        return messages.size();
    }
}