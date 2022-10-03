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

    public static void createTask(String taskTitle, String taskText, long dateTime) {
        try {
            preparedStatementQuery = connection.prepareStatement("INSERT INTO todo_list(title, task_text, task_status, date_time) VALUES(?, ?, ?, ?);");
            preparedStatementQuery.setString(1, taskTitle);
            preparedStatementQuery.setString(2, taskText);
            preparedStatementQuery.setBoolean(3, true);
            preparedStatementQuery.setLong(4, dateTime);
            preparedStatementQuery.executeUpdate();
            preparedStatementQuery.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isAlreadyThereTitle(String taskTitle, boolean taskStatus) {
        String query = String.format("SELECT title FROM todo_list WHERE title=\"%s\" AND task_status=%s", taskTitle, taskStatus);
        try (ResultSet set = statement.executeQuery(query)) {
            return set.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getTextTask(String taskTitle, boolean taskStatus) {
        String query = String.format("SELECT task_text FROM todo_list WHERE title=\"%s\" AND task_status=%s", taskTitle, taskStatus);
        try (ResultSet set = statement.executeQuery(query)) {
            return set.getString("task_text");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getTitleTask(boolean taskStatus) {
        String query = String.format("SELECT * FROM todo_list WHERE task_status=%s", taskStatus);
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

    public static void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}