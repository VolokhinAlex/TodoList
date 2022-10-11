package com.application.todo_list;

import org.sqlite.JDBC;

import java.sql.*;

public class SqlService {

    private static Connection connection;
    private static PreparedStatement preparedStatementQuery;
    private static Statement statement;
    public static final String DELIMITER = "±";

    public static void connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(JDBC.PREFIX + "todolist.db");
            statement = connection.createStatement();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createTask(String title, String text, long dateTime) {
        try {
            preparedStatementQuery = connection.prepareStatement("INSERT INTO todo_list(title, task_text, task_status, date_time) VALUES(?, ?, ?, ?);");
            preparedStatementQuery.setString(1, title);
            preparedStatementQuery.setString(2, text);
            preparedStatementQuery.setBoolean(3, true);
            preparedStatementQuery.setLong(4, dateTime);
            preparedStatementQuery.executeUpdate();
            preparedStatementQuery.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean getTaskStatus(String title) {
        try (PreparedStatement getStatus = connection.prepareStatement("SELECT task_status FROM todo_list WHERE title=?")) {
            getStatus.setString(1, title);
            try (ResultSet resultSet = getStatus.executeQuery()) {
                while (resultSet.next()) {
                    return resultSet.getBoolean("task_status");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getTaskText(String title) {
        try (PreparedStatement getText = connection.prepareStatement("SELECT task_text FROM todo_list WHERE title=?")) {
            getText.setString(1, title);
            try (ResultSet resultSet = getText.executeQuery()) {
                while (resultSet.next()) {
                    return resultSet.getString("task_text");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getTaskTitle(boolean status) {
        String query = String.format("SELECT * FROM todo_list WHERE task_status=%s", status);
        try (ResultSet set = statement.executeQuery(query)) {
            StringBuilder openTasks = new StringBuilder();
            while (set.next()) {
                openTasks.append(set.getString("title")).append(DELIMITER);
            }
            return openTasks.toString();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void editAndSaveTask(String taskTitle, String newText) {
        try {
            preparedStatementQuery = connection.prepareStatement("UPDATE todo_list SET task_text=? WHERE title=? AND task_status=?");
            preparedStatementQuery.setString(1, newText);
            preparedStatementQuery.setString(2, taskTitle);
            preparedStatementQuery.setBoolean(3, true);
            preparedStatementQuery.executeUpdate();
            preparedStatementQuery.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void closeTask(String taskTitle, boolean status) {
        try {
            preparedStatementQuery = connection.prepareStatement("UPDATE todo_list SET task_status=? WHERE title=?;");
            preparedStatementQuery.setBoolean(1, status);
            preparedStatementQuery.setString(2, taskTitle);
            preparedStatementQuery.executeUpdate();
            preparedStatementQuery.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getHistoryTasks() {
        try (PreparedStatement getTasks = connection.prepareStatement("SELECT title FROM todo_list")) {
            StringBuilder tasks = new StringBuilder();
            try (ResultSet resultSet = getTasks.executeQuery()) {
                while (resultSet.next()) {
                    tasks.append(resultSet.getString("title")).append(DELIMITER);
                }
            }
            return tasks.toString();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void close() {
        try {
            statement.close();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}