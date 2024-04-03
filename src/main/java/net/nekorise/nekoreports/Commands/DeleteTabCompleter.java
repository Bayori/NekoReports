package net.nekorise.nekoreports.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DeleteTabCompleter implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        if (args.length == 1 && sender.hasPermission("nekoreports.delete"))
        {
            return List.of("<id>", "all");
        }else if (args.length > 1) {
            return List.of("");
        }
        return null;
    }
}
