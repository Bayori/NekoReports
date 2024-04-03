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

        // /nekor
        getCommand("nekoreports").setExecutor(new AboutCommand());
        getCommand("nekoreports").setTabCompleter(new AboutTabCompleter());

        // /report
        getCommand("report").setExecutor(new ReportCommand());
        getCommand("report").setTabCompleter(new ReportTabCompleter());

        // /reportcheck
        getCommand("reportcheck").setExecutor(new CheckCommand());
        getCommand("reportcheck").setTabCompleter(new CheckTabCompleter());


        // /reportdelete
        getCommand("reportdelete").setExecutor(new DeleteCommand());
        getCommand("reportdelete").setTabCompleter(new DeleteTabCompleter());

        // /reportchange
        getCommand("reportchange").setExecutor(new ChangeCommand());
        getCommand("reportchange").setTabCompleter(new ChangeTabCompleter());

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
