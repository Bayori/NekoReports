package net.nekorise.nekoreports.Commands;

import net.nekorise.nekoreports.utlis.HEX;
import net.nekorise.nekoreports.utlis.MySQLConnection;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class DeleteCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        if (args.length <= 0)
        {
         sender.sendMessage(HEX.ApplyColor("&#ed471bИспользование: /reportdelete <ID репорта>"));
         return false;
        }

        if (args[0].equals("all"))
        {
            MySQLConnection.deleteAll();
            for (Player player : Bukkit.getOnlinePlayers())
            {
                if (player.hasPermission("nekoreports.delete"))
                {
                    player.sendMessage(HEX.ApplyColor("&4База данных NekoReports была полностью очищена пользователем " + sender.getName()));
                }
            }
            return true;
        }

        if (!args[0].chars().allMatch((Character::isDigit)))
        {
            sender.sendMessage(HEX.ApplyColor("&#ed471bИспользование: /reportdelete <ID репорта>"));
            return false;
        }

        sender.sendMessage(HEX.ApplyColor("&#ff7700Выполняется..."));
        if(!MySQLConnection.checkEntry(Integer.parseInt(args[0])))
        {
            sender.sendMessage(HEX.ApplyColor("&#ed471bЗапись с таким ID отсутствует в базе данных."));
            return false;
        }

        if (MySQLConnection.deleteReport(Integer.parseInt(args[0])))
        {
            sender.sendMessage(HEX.ApplyColor("&#1fe9a4Запись с ID " + args[0] + " была удалена из базы данных!"));
            return true;
        }

        sender.sendMessage(HEX.ApplyColor("&#ed471bПроизошла ошибка, проверьте консоль."));
        return false;
    }
}
