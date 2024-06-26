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

public class ReportCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        if (args.length <= 0) // Если аргументов 0, отменяем команду
        {
            sender.sendMessage(HEX.ApplyColor(LoadFromCfg("messages.report.usage")));
            return false;
        }

        if (args.length > 0){
            String reason = getAllArgsAfterN(1, args);
            if (reason.length() > 150)
            {
                sender.sendMessage(HEX.ApplyColor(HEX.ApplyColor(LoadFromCfg("messages.report.many-symbols"))));
                return false;
            } else if (reason.length() <= 0)
            {
                reason = null;
            }
            sender.sendMessage(MySQLConnection.addReportToDB(sender.getName(), args[0], reason)); // Запись репорта в БД
            SendToAllAdmins(sender, args, reason);
        }


        return true;
    }

    private void SendToAllAdmins(CommandSender sender, String[] args, String reason)
    {
        for (Player admin : Bukkit.getOnlinePlayers())
        {
            if (admin.hasPermission("nekoreports.check"))
            {
                admin.sendMessage(HEX.gradientText("\n" + MySQLConnection.LoadFromCfg("messages.report.admin-notify"), HEX.parseHexColor("#f2ae1f"), HEX.parseHexColor("#f2581f")) +
                                     HEX.ApplyColor(
                                             "\n" + MySQLConnection.LoadFromCfg("messages.report.owner") + sender.getName() +
                                                "\n" + MySQLConnection.LoadFromCfg("messages.report.reported-player") + args[0] +
                                                "\n" + MySQLConnection.LoadFromCfg("messages.report.reason")+ reason
                ));
            }
        }
    }

    private String getAllArgsAfterN(int N, String[] args){
        // Создаем строитель строчки, пустой
        StringBuilder sb = new StringBuilder();
        // Перебираем все аргументы, начиная от 1 (второй), до последнего,
        // добавляем в строитель аргумент, а затем пробел.
        for (int i = N; i < args.length; i++) sb.append(args[i]).append(' ');
        if (sb.length() > 0) sb.deleteCharAt(sb.length() - 1); // удаляем посл пробел

        // Строим строчку
        return sb.toString();
    }
    public static String LoadFromCfg(String path)
    {
        return NekoReports.getPlugin().getConfig().getString(path);
    }
}
