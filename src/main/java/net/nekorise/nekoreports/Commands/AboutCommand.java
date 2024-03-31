package net.nekorise.nekoreports.Commands;

import net.nekorise.nekoreports.NekoReports;
import net.nekorise.nekoreports.utlis.HEX;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;



public class AboutCommand implements CommandExecutor
{
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args)
    {

        sender.sendMessage(HEX.ApplyColor("&#ff7700NekoReports v" + NekoReports.getPlugin().getDescription().getVersion() + "\n&#ff7700by Nekorise"));
        

        return true;
    }
}
