package br.com.master.automessage.tasks;

import br.com.master.automessage.Main;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

public class MessageTask {

    private final Main plugin;
    private BukkitTask task;

    public MessageTask(Main plugin) {
        this.plugin = plugin;
    }

    public void start() {
        if (!plugin.getConfigManager().isEnabled()) {
            return;
        }

        long interval = plugin.getConfigManager().getInterval() * 20L; // Converter para ticks

        task = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if (Bukkit.getOnlinePlayers().isEmpty()) {
                return; // Não enviar mensagens se não há jogadores online
            }

            plugin.getMessageManager().sendNextMessage();
        }, interval, interval);

        plugin.getLogger().info("Task de mensagens automáticas iniciada com intervalo de "
                + plugin.getConfigManager().getInterval() + " segundos.");
    }

    public void stop() {
        if (task != null && !task.isCancelled()) {
            task.cancel();
            plugin.getLogger().info("Task de mensagens automáticas parada.");
        }
    }
}