package org.al9.exmep;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class ExmepPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("Exmep plugin enabled!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Проверяем, что команда — именно /exmep
        if (!command.getName().equalsIgnoreCase("exmep")) {
            return false;
        }

        // Проверка минимального количества аргументов
        if (args.length < 2) {
            sender.sendMessage("\u00A7cUsage: /exmep <player> [say|command] <message>");
            sender.sendMessage("\u00A7cOr: /exmep -sudo <player> [say|command] <message>");
            return true;
        }

        boolean isSudo = args[0].equals("-sudo");
        String targetName;
        int offset;

        // Обработка режима -sudo
        if (isSudo) {
            if (args.length < 3) {
                sender.sendMessage("\u00A7cNot enough arguments for -sudo mode!");
                return true;
            }
            targetName = args[1];
            offset = 2;
        } else {
            targetName = args[0];
            offset = 1;
        }

        // Проверка, что игрок онлайн
        Player target = Bukkit.getPlayer(targetName);
        if (target == null) {
            sender.sendMessage("\u00A7cPlayer " + targetName + " is not online!");
            return true;
        }

        // Получаем действие (say или command)
        String action = args[offset];

        // Собираем сообщение/команду из оставшихся аргументов
        StringBuilder messageBuilder = new StringBuilder();
        for (int i = offset + 1; i < args.length; i++) {
            if (i > offset + 1) {
                messageBuilder.append(" ");
            }
            messageBuilder.append(args[i]);
        }
        String message = messageBuilder.toString();

        // Обработка команды say
        if (action.equalsIgnoreCase("say")) {
            if (isSudo || sender.hasPermission("exmep.say")) {
                target.chat(message);
                sender.sendMessage("\u00A7aMessage sent as " + targetName + ": " + message);
            } else {
                sender.sendMessage("\u00A7cYou don't have permission to use say!");
            }
        } 
        // Обработка команды command
        else if (action.equalsIgnoreCase("command")) {
            if (isSudo || sender.hasPermission("exmep.command")) {
                Bukkit.dispatchCommand(target, message);
                sender.sendMessage("\u00A7aCommand executed as " + targetName + ": /" + message);
            } else {
                sender.sendMessage("\u00A7cYou don't have permission to use command!");
            }
        }
        // Неизвестное действие
        else {
            sender.sendMessage("\u00A7cUnknown action: " + action + ". Use 'say' or 'command'.");
        }

        return true;
    }
}
