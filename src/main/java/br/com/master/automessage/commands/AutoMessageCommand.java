package br.com.master.automessage.commands;

import br.com.master.automessage.Main;
import br.com.master.automessage.models.AutoMessage;
import br.com.master.automessage.utils.MessageUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AutoMessageCommand implements CommandExecutor, TabCompleter {

    private final Main plugin;

    public AutoMessageCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("automessage.admin")) {
            sender.sendMessage(Component.text("Você não tem permissão para usar este comando!")
                    .color(NamedTextColor.RED));
            return true;
        }

        if (args.length == 0) {
            sendHelp(sender);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "reload":
                plugin.reload();
                sender.sendMessage(Component.text("Plugin recarregado com sucesso!")
                        .color(NamedTextColor.GREEN));
                break;

            case "list":
                listMessages(sender);
                break;

            case "send":
                if (args.length < 2) {
                    sender.sendMessage(Component.text("Use: /automessage send <id>")
                            .color(NamedTextColor.RED));
                    return true;
                }
                sendSpecificMessage(sender, args[1]);
                break;



            default:
                sendHelp(sender);
                break;
        }

        return true;
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage(Component.text(""));
        sender.sendMessage(Component.text("/automessage reload - Recarregar plugin").color(NamedTextColor.YELLOW));
        sender.sendMessage(Component.text("/automessage list - Listar mensagens").color(NamedTextColor.YELLOW));
        sender.sendMessage(Component.text("/automessage send <id> - Enviar mensagem específica").color(NamedTextColor.YELLOW));
        sender.sendMessage(Component.text(""));
    }

    private void listMessages(CommandSender sender) {
        List<AutoMessage> messages = plugin.getMessageManager().getMessages();

        sender.sendMessage(Component.text("=== Mensagens Carregadas (" + messages.size() + ") ===")
                .color(NamedTextColor.GOLD));

        for (AutoMessage message : messages) {
            Component status = Component.text(message.isEnabled() ? "✓" : "✗")
                    .color(message.isEnabled() ? NamedTextColor.GREEN : NamedTextColor.RED);

            sender.sendMessage(Component.text()
                    .append(status)
                    .append(Component.text(" " + message.getId()).color(NamedTextColor.YELLOW))
                    .append(Component.text(" (" + message.getContent().size() + " linhas)")
                            .color(NamedTextColor.GRAY))
                    .build());
        }
    }

    private void sendSpecificMessage(CommandSender sender, String messageId) {
        AutoMessage message = plugin.getMessageManager().getMessages().stream()
                .filter(msg -> msg.getId().equalsIgnoreCase(messageId))
                .findFirst()
                .orElse(null);

        if (message == null) {
            sender.sendMessage(Component.text("Mensagem com ID '" + messageId + "' não encontrada!")
                    .color(NamedTextColor.RED));
            return;
        }

        plugin.getMessageManager().sendMessage(message);
        sender.sendMessage(Component.text("Mensagem '" + messageId + "' enviada!")
                .color(NamedTextColor.GREEN));
    }


    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!sender.hasPermission("automessage.admin")) {
            return null;
        }

        if (args.length == 1) {
            return Arrays.asList("reload", "list", "send").stream()
                    .filter(s -> s.toLowerCase().startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("send")) {
            return plugin.getMessageManager().getMessages().stream()
                    .map(AutoMessage::getId)
                    .filter(id -> id.toLowerCase().startsWith(args[1].toLowerCase()))
                    .collect(Collectors.toList());
        }

        return null;
    }
}