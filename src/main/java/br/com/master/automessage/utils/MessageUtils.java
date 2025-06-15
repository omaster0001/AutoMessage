package br.com.master.automessage.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.sound.Sound;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class MessageUtils {

    private static final MiniMessage miniMessage = MiniMessage.miniMessage();

    public static Component parseMessage(String message, Player player) {
        // Substituir placeholders básicos
        String parsed = parsePlaceholders(message, player);

        // Converter usando MiniMessage
        return miniMessage.deserialize(parsed);
    }

    public static String parsePlaceholders(String message, Player player) {
        return message
                .replace("{player}", player.getName())
                .replace("{world}", player.getWorld().getName())
                .replace("{online}", String.valueOf(Bukkit.getOnlinePlayers().size()))
                .replace("{max_players}", String.valueOf(Bukkit.getMaxPlayers()));
    }

    public static void playSound(Player player, String soundName) {
        try {
            org.bukkit.Sound bukkitSound = org.bukkit.Sound.valueOf(soundName.toUpperCase());
            player.playSound(player.getLocation(), bukkitSound, 1.0f, 1.0f);
        } catch (IllegalArgumentException e) {
            // Som não encontrado, ignorar
        }
    }
}