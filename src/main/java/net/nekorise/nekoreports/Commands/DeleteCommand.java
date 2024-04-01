package net.nekorise.nekoreports.Commands;

import net.nekorise.nekoreports.NekoReports;
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
         sender.sendMessage(HEX.ApplyColor(LoadFromCfg("messages.delete.usage")));
         return false;
        }

        if (args[0].equals("all"))
        {
            MySQLConnection.deleteAll();
            for (Player player : Bukkit.getOnlinePlayers())
            {
                if (player.hasPermission("nekoreports.delete"))
                {
                    player.sendMessage(HEX.ApplyColor(LoadFromCfg("messages.delete.full-delete") + sender.getName()));
                }
            }
            return true;
        }

        if (!args[0].chars().allMatch((Character::isDigit)))
        {
            sender.sendMessage(HEX.ApplyColor(LoadFromCfg("messages.delete.usage")));
            return false;
        }

        if(!MySQLConnection.checkEntry(Integer.parseInt(args[0])))
        {
            sender.sendMessage(HEX.ApplyColor(LoadFromCfg("messages.delete.not-exists")));
            return false;
        }

        if (MySQLConnection.deleteReport(Integer.parseInt(args[0])))
        {

            String message = LoadFromCfg("messages.delete.suc-delete");
            message = message.replace("%report-ID%", args[0]);
            sender.sendMessage(HEX.ApplyColor(message));
            return true;
        }

        sender.sendMessage("Error. Check console.");
        return false;
    }
    public static String LoadFromCfg(String path)
    {
        return NekoReports.getPlugin().getConfig().getString(path);
    }
}
