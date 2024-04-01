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
    private static String url = "jdbc:mysql://localhost:3306/";
    private static String user = "root";
    private static String password = "";

    private static String Schema = "nekoreports";
    private static String Table = "report_list";
    private static String EndAdress = Schema + "." + Table;

    // JDBC поля для создания и управления соединением
    private static Connection con;
    private static Statement stmt;
    private static ResultSet rs;

    public static String getCount() // Вернуть количество записей в таблице
    {
        FillFields();

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
            return "Error. Check console.";
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
        FillFields();
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

            String returnMessage = LoadFromCfg("messages.report.player-notify");
            returnMessage = returnMessage.replace("%reportedPlayer%", reportedName);

            return HEX.ApplyColor(returnMessage);

        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
            return "Error. Check console.";
        } finally {
            //close connection ,stmt and resultset here
            try { con.close(); } catch(SQLException se) { /*can't do anything */ }
            try { stmt.close(); } catch(SQLException se) { /*can't do anything */ }
        }
    }

    public static List<String> reportsList(CommandSender sender)
    {
        FillFields();
        String query = "SELECT ID, username, reportedUsername, reason, reportDate, solved FROM " + EndAdress + ";";

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

                String strStatus = solved ? LoadFromCfg("messages.report.true-status") : LoadFromCfg("messages.report.false-status");;

                output.add("&#ff7700[&#ebd91eID: " + id + "&#ff7700] " +
                        "\n" + LoadFromCfg("messages.report.owner") + username +
                        "\n" + LoadFromCfg("messages.report.reported-player") + reportedUsername +
                        "\n" + LoadFromCfg("messages.report.reason") + reason +
                        "\n" + LoadFromCfg("messages.report.date") + date +" "+ time +
                        "\n" + LoadFromCfg("messages.report.status") + strStatus);
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
        FillFields();
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
        FillFields();
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
        FillFields();
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
        FillFields();
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
        FillFields();
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
            Bukkit.getPluginManager().disablePlugin(NekoReports.getPlugin());
            Bukkit.getLogger().warning("Error when connecting to the database, check the correctness of the entered data in config.yml and the connection to the database yourself");
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
        FillFields();
        String query1 = "CREATE DATABASE IF NOT EXISTS " + Schema + ";";
        String query2 =
                "CREATE TABLE IF NOT EXISTS " + EndAdress + "(" +
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
        FillFields();
        try {
            // opening database connection to MySQL server
            con = DriverManager.getConnection(url, user, password);
            checkTable();
            return true;

        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(NekoReports.getPlugin());
            Bukkit.getLogger().warning("Error when connecting to the database, check the correctness of the entered data in config.yml and the connection to the database yourself");
            return false;
        } finally {
            //close connection ,stmt and resultset here
            try { con.close(); } catch(SQLException se) { /*can't do anything */ }
        }
    }

    public static void FillFields()
    {
        url = "jdbc:mysql://" + LoadFromCfg("mySQL.host");
        url = url + ":" + LoadFromCfg("mySQL.port") + "/";
        user = LoadFromCfg("mySQL.login");
        password = LoadFromCfg("mySQL.password");
        Schema = LoadFromCfg("mySQL.main-schema");
        Table = LoadFromCfg("mySQL.main-table");
        EndAdress = Schema + "." + Table;
    }

    public static String LoadFromCfg(String path)
    {
        return NekoReports.getPlugin().getConfig().getString(path);
    }
}
