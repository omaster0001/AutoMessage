package br.com.master.automessage.listeners;

import br.com.master.automessage.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private final Main plugin;

    public PlayerJoinListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        // Exemplo de funcionalidade futura
        // Pode ser usado para mensagens de boas-vindas, etc.
    }
}