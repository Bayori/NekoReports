package net.nekorise.nekoreports.Commands;

import net.nekorise.nekoreports.NekoReports;
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
            sender.sendMessage(HEX.ApplyColor(LoadFromCfg("messages.report-change.usage")));
            return false;
        }

        if (!(args[1].equals("true") || args[1].equals("false")))
        {
            sender.sendMessage(HEX.ApplyColor(LoadFromCfg("messages.report-change.usage")));
            return false;
        }

        if (!MySQLConnection.checkEntry(Integer.parseInt(args[0])))
        {
            sender.sendMessage(HEX.ApplyColor(LoadFromCfg("messages.report-change.not-exists")));
            return false;
        }

        if (MySQLConnection.changeStatus(Integer.parseInt(args[0]), args[1]))
        {
            String message = LoadFromCfg("messages.report-change.suc-changed");
            message = message.replace("%report-ID%", args[0]);
            sender.sendMessage(HEX.ApplyColor(message
                    +  ((args[1].equals("true")) ? LoadFromCfg("messages.report.true-status") : LoadFromCfg("messages.report.false-status"))));
        }
        return true;
    }
    public static String LoadFromCfg(String path)
    {
        return NekoReports.getPlugin().getConfig().getString(path);
    }
}
