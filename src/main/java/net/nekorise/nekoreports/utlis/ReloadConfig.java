package net.nekorise.nekoreports.utlis;

import net.nekorise.nekoreports.NekoReports;
import org.bukkit.plugin.Plugin;

public class ReloadConfig {
    public static boolean reloadCfg()
    {
        Plugin plugin = NekoReports.getPlugin();
        plugin.reloadConfig();
        return true;
    }
}
