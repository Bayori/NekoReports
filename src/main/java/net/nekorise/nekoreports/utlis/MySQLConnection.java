package net.nekorise.nekoreports.utlis;
import net.nekorise.nekoreports.NekoReports;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySQLConnection {

    // URL, ЛОГИН, ПАРОЛЬ
    private static final String url = "jdbc:mysql://localhost:3306/";
    private static final String user = "Bayori";
    private static final String password = "Papika27";

    private static final String Schema = "nekoreports";
    private static final String Table = "report_list";
    private static final String EndAdress = Schema + "." + Table;

    // JDBC поля для создания и управления соединением
    private static Connection con;
    private static Statement stmt;
    private static ResultSet rs;

    public static String getCount() // Вернуть количество записей в таблице
    {
        String query = "select count(*) from " + EndAdress + ";";

        try {
            // opening database connection to MySQL server
            con = DriverManager.getConnection(url, user, password);

            // getting Statement object to execute query
            stmt = con.createStatement();

            // executing SELECT query
            rs = stmt.executeQuery(query);

            while (rs.next()) {
                int count = rs.getInt(1);
                return count+"";
            }

        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
            return "&#ed471bОшибка соединения с БД, проверьте консоль.";
        } finally {
            //close connection ,stmt and resultset here
            try { con.close(); } catch(SQLException se) { /*can't do anything */ }
            try { stmt.close(); } catch(SQLException se) { /*can't do anything */ }
            try { rs.close(); } catch(SQLException se) { /*can't do anything */ }
        }
        return null;
    }

    public static String addReportToDB(@NotNull String senderName, @NotNull String reportedName, String reason)
    {
        String query = "INSERT INTO " + EndAdress + " (username, reportedUsername, reason) " +
                "VALUES " +
                "('" + senderName + "', " + // Ник подающего
                "'" + reportedName +"', " + // Ник обидевшего
                "'" + reason + "');"; // Причина

        try {
            // opening database connection to MySQL server
            con = DriverManager.getConnection(url, user, password);

            // getting Statement object to execute query
            stmt = con.createStatement();

            // executing INSERT query
            stmt.executeUpdate(query);

            return  HEX.ApplyColor("&#ff7700Жалоба на игрока &#ffcb00" + reportedName + " &#ff7700подана.\nАдминистрация сервера будет уведомлена.");

        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
            return "Ошибка соединения с БД, проверьте консоль.";
        } finally {
            //close connection ,stmt and resultset here
            try { con.close(); } catch(SQLException se) { /*can't do anything */ }
            try { stmt.close(); } catch(SQLException se) { /*can't do anything */ }
        }
    }

    public static List<String> reportsList(CommandSender sender)
    {
        String query = "SELECT ID, username, reportedUsername, reason, reportDate, solved FROM " + EndAdress + ";";
        System.out.println(query);

        List<String> output = new ArrayList<>();

        try {
            // opening database connection to MySQL server
            con = DriverManager.getConnection(url, user, password);

            // getting Statement object to execute query
            stmt = con.createStatement();

            // executing SELECT query
            rs = stmt.executeQuery(query);

            while (rs.next())
            {
                int id = rs.getInt(1);
                String username = rs.getString(2);
                String reportedUsername = rs.getString(3);
                String reason = rs.getString(4);
                String time = rs.getTime(5).toString();
                String date = rs.getDate(5).toString();
                Boolean solved = rs.getBoolean(6);

                String strStatus = solved ? "&#1fe9a4Решено" : "&#eb531eНе решено";;

                output.add("&#ff7700[&#ebd91eID: " + id + "&#ff7700] " +
                        "\n&#ff7700Подал: &#ffcb00" + username +
                        "\n&#ff7700Обвиняемый: &#ffcb00" + reportedUsername +
                        "\n&#ff7700Причина: &#ffcb00" + reason +
                        "\n&#ff7700Дата: &#ffcb00" + date +" "+ time +
                        "\n&#ff7700Статус: " + strStatus);
            }
            return output;

        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
            sender.sendMessage("Ошибка соединения с БД, проверьте консоль.");
        } finally {
            //close connection ,stmt and resultset here
            try { con.close(); } catch(SQLException se) { /*can't do anything */ }
            try { stmt.close(); } catch(SQLException se) { /*can't do anything */ }
            try { rs.close(); } catch(SQLException se) { /*can't do anything */ }
        }
        return null;
    }

    public static boolean deleteReport(int ID)
    {
        String query = "DELETE FROM " + EndAdress + " WHERE `ID` = " + ID +";";

        try {
            // opening database connection to MySQL server
            con = DriverManager.getConnection(url, user, password);

            // getting Statement object to execute query
            stmt = con.createStatement();

            // executing INSERT query
            stmt.executeUpdate(query);

            return true;

        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
            return false;
        } finally {
            //close connection ,stmt and resultset here
            try { con.close(); } catch(SQLException se) { /*can't do anything */ }
            try { stmt.close(); } catch(SQLException se) { /*can't do anything */ }
        }
    }

    public static boolean checkEntry(int ID)
    {
        String query = "SELECT * FROM " + EndAdress + " WHERE `ID` = " + ID +";";

        try {
            // opening database connection to MySQL server
            con = DriverManager.getConnection(url, user, password);

            // getting Statement object to execute query
            stmt = con.createStatement();

            // executing SELECT query
            rs = stmt.executeQuery(query);;

            int id = 0;
            while (rs.next()){
                id = rs.getInt(1);
            }

            if (id <= 0) { return false; }
            return true;

        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
            return false;
        } finally {
            //close connection ,stmt and resultset here
            try { con.close(); } catch(SQLException se) { /*can't do anything */ }
            try { stmt.close(); } catch(SQLException se) { /*can't do anything */ }
            try { rs.close(); } catch(SQLException se) { /*can't do anything */ }
        }
    }

    public static boolean deleteAll()
    {
        String query = "DELETE FROM " + EndAdress + ";";

        try {
            // opening database connection to MySQL server
            con = DriverManager.getConnection(url, user, password);

            // getting Statement object to execute query
            stmt = con.createStatement();

            // executing INSERT query
            stmt.executeUpdate(query);

            return true;

        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
            return false;
        } finally {
            //close connection ,stmt and resultset here
            try { con.close(); } catch(SQLException se) { /*can't do anything */ }
            try { stmt.close(); } catch(SQLException se) { /*can't do anything */ }
        }
    }

    public static boolean changeStatus(int ID, String status)
    {
        String query = "UPDATE " + EndAdress + " SET `solved` = " + status + " WHERE ID = " + ID + ";";

        try {
            // opening database connection to MySQL server
            con = DriverManager.getConnection(url, user, password);

            // getting Statement object to execute query
            stmt = con.createStatement();

            // executing INSERT query
            stmt.executeUpdate(query);

            return true;

        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
            return false;
        } finally {
            //close connection ,stmt and resultset here
            try { con.close(); } catch(SQLException se) { /*can't do anything */ }
            try { stmt.close(); } catch(SQLException se) { /*can't do anything */ }
        }
    }

    public static boolean checkTable()
    {
        String query = "SHOW DATABASES LIKE '" + Schema + "';";

        try {
            // opening database connection to MySQL server
            con = DriverManager.getConnection(url, user, password);

            // getting Statement object to execute query
            stmt = con.createStatement();

            // executing SELECT query
            rs = stmt.executeQuery(query);;

            if (!rs.next())
            {
                createSchemaTable();
            }

            return true;

        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
            return false;
        } finally {
            //close connection ,stmt and resultset here
            try { con.close(); } catch(SQLException se) { /*can't do anything */ }
            try { stmt.close(); } catch(SQLException se) { /*can't do anything */ }
            try { rs.close(); } catch(SQLException se) { /*can't do anything */ }
        }
    }

    public static boolean createSchemaTable()
    {
        String query1 = "CREATE DATABASE " + Schema + ";";
        String query2 =
                "CREATE TABLE " + EndAdress + "(" +
                "ID INT AUTO_INCREMENT PRIMARY KEY," +
                "username VARCHAR(40) NOT NULL," +
                "reportedUsername VARCHAR(40) NOT NULL," +
                "reason VARCHAR(150)," +
                "reportDate DATETIME DEFAULT CURRENT_TIMESTAMP," +
                "solved BOOLEAN DEFAULT FALSE " +
                ");";

        try {
            // opening database connection to MySQL server
            con = DriverManager.getConnection(url, user, password);

            // getting Statement object to execute query
            stmt = con.createStatement();

            stmt.executeUpdate(query1);
            stmt.executeUpdate(query2);

            return true;

        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
            return false;
        } finally {
            //close connection ,stmt and resultset here
            try { con.close(); } catch(SQLException se) { /*can't do anything */ }
            try { stmt.close(); } catch(SQLException se) { /*can't do anything */ }
        }
    }

    public static boolean startupLogic()
    {
        try {
            // opening database connection to MySQL server
            con = DriverManager.getConnection(url, user, password);
            checkTable();
            return true;

        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(NekoReports.getPlugin());
            Bukkit.getLogger().warning("Ошибка при подключении к БД, проверьте корректность введённых данных в config.yml и подключение к БД самостоятельно");
            return false;
        } finally {
            //close connection ,stmt and resultset here
            try { con.close(); } catch(SQLException se) { /*can't do anything */ }
        }
    }
}
