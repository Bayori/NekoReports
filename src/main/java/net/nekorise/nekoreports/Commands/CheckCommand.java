package net.nekorise.nekoreports.Commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextColor;
import net.nekorise.nekoreports.utlis.HEX;
import net.nekorise.nekoreports.utlis.MySQLConnection;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CheckCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args)
    {
        List<String> reportsList = new ArrayList<>();
        reportsList = MySQLConnection.reportsList(sender);

        if (reportsList.size() <= 0)
        {
            sender.sendMessage(HEX.ApplyColor("&#eb531eРепортов нет!"));
            return true;
        }

        int page = 1;
        int pageSize = 5;
        int listSize = reportsList.size();

        int previousPage = 0;
        int nextPage = 0;


        if (args.length > 0){
            try
            {
                page = Integer.parseInt(args[0]);
            }
            catch (Exception ex){
                sender.sendMessage(HEX.ApplyColor("&#ed471bСтраница должна быть числом"));
            }
        }


        // Вычисление индекса последнего элемента для текущей страницы
        int endIndex = listSize - (page - 1) * pageSize;
        // Вычисление индекса первого элемента для текущей страницы
        int startIndex = Math.max(endIndex - pageSize, 0);

        int nextPageEndIndex = listSize - page * pageSize;

        nextPage = page + 1;
        previousPage = page - 1;

        // Проверка на корректность страницы
        if (page <= 0 || endIndex <= 0)
        {
            sender.sendMessage(HEX.ApplyColor("&#ed471bСтраницы не существует"));

            return false;
        }

        // Защита от перехода на страницу которой не существует
        if (nextPageEndIndex < 0)
        {
            nextPage = page;
        }

        // Защита от перехода на страницу ниже первой
        if (previousPage < 1)
        {
            previousPage = 1;
        }




        // Создание ПЕРВОЙ кнопки перелистывания страницы
        TextComponent PrevPageMes = page == 1 ? Component.empty() : Component
                .text("<===")
                .color(TextColor.color(182, 235, 30))
                .clickEvent(ClickEvent.runCommand("/reportcheck " + previousPage))
                .hoverEvent(HoverEvent.showText(Component
                        .text("/reportcheck " + previousPage)
                        .color(TextColor.color(182, 235, 30))));

        // Создание ВТОРОЙ кнопки перелистывания страницы
        TextComponent NextPageMes = nextPageEndIndex <= 0 ? Component.empty() : Component
                .text("===>")
                .color(TextColor.color(182, 235, 30))
                .clickEvent(ClickEvent.runCommand("/reportcheck " + nextPage))
                .hoverEvent(HoverEvent.showText(Component
                        .text("/reportcheck " + nextPage)
                        .color(TextColor.color(182, 235, 30))));

        // Сообщение номера страницы
        TextComponent PageText = Component
                .text(" Страница " + page + " ")
                .color(TextColor.color(182, 235, 30))
                .clickEvent(ClickEvent.runCommand(""))
                .hoverEvent(HoverEvent.showText(Component.empty()));

        // Сборка кнопок и сообщения номера страницы в один компонент
        TextComponent FinalPageMessage = PrevPageMes.append(PageText).append(NextPageMes);

        // Сообщение "Список репортов" с кнопками по бокам
        TextComponent ReportListText = Component
                .text(" Список репортов (Всего " + MySQLConnection.getCount() + ") ")
                .color(TextColor.color(182, 235, 30))
                .clickEvent(ClickEvent.runCommand(""))
                .hoverEvent(HoverEvent.showText(Component.empty()));

        // Сборка кнопок и сообщения "Список репортов" воедино
        TextComponent FinalReportListMessage = PrevPageMes.append(ReportListText).append(NextPageMes);

        // Вывод элементов текущей страницы с конца
        sender.sendMessage(""); // Пустышка для визуального разделения страниц
        sender.sendMessage(FinalReportListMessage);
        sender.sendMessage(HEX.ApplyColor("&#eb531e---"));
        for (int i = endIndex - 1; i >= startIndex; i--) {
            sender.sendMessage(HEX.ApplyColor(reportsList.get(i) + "\n&#eb531e---\n"));
        }
        sender.sendMessage(FinalPageMessage);

        return true;
    }
}
