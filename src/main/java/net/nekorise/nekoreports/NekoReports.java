package net.nekorise.nekoreports;

import net.nekorise.nekoreports.Commands.*;
import net.nekorise.nekoreports.utlis.MySQLConnection;
import org.bukkit.plugin.java.JavaPlugin;

public final class NekoReports extends JavaPlugin
{

    private static NekoReports plugin;
    @Override
    public void onEnable()
    {
        plugin = this;

        saveDefaultConfig();
        MySQLConnection.startupLogic();

        getCommand("nekoreports").setExecutor(new AboutCommand());
        getCommand("report").setExecutor(new ReportCommand());
        getCommand("reportcheck").setExecutor(new CheckCommand());
        getCommand("reportdelete").setExecutor(new DeleteCommand());
        getCommand("reportchange").setExecutor(new ChangeCommand());

        getLogger().info("Успешно запущено");

    }

    @Override
    public void onDisable()
    {
        getLogger().info("Отключение плагина");
    }

    public static NekoReports getPlugin(){
        return plugin;
    }
}
