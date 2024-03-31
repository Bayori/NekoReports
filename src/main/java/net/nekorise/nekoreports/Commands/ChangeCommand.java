package net.nekorise.nekoreports.Commands;

import net.nekorise.nekoreports.utlis.HEX;
import net.nekorise.nekoreports.utlis.MySQLConnection;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ChangeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args)
    {

        if (args.length <= 1)
        {
            sender.sendMessage(HEX.ApplyColor("&#ed471bИспользование: /reportchange <ID репорта> <true/false>"));
            return false;
        }

        if (!(args[1].equals("true") || args[1].equals("false")))
        {
            sender.sendMessage(HEX.ApplyColor("&#ed471bИспользование: /reportchange <ID репорта> <true/false>"));
            return false;
        }

        if (!MySQLConnection.checkEntry(Integer.parseInt(args[0])))
        {
            sender.sendMessage(HEX.ApplyColor("&#ed471bЗапись с таким ID отсутствует в базе данных."));
            return false;
        }

        if (MySQLConnection.changeStatus(Integer.parseInt(args[0]), args[1]))
        {
            sender.sendMessage(HEX.ApplyColor("&#ff7700Статус репорта " + args[0] + " был изменён на "
                    +  ((args[1].equals("true")) ? "&#1fe9a4Решено" : "&#eb531eНе решено")));
        }
        return true;
    }
}
