package com.application.todo_list;

import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SettingApp implements OnActionTodoList {

    private static final boolean OPEN_TASK = true;
    private static final boolean CLOSE_TASK = false;
    private Logger logger = Logger.getLogger(SettingApp.class.getName());

    public void onStartApp() {
        SqlService.connect();
        Handler handler = new ConsoleHandler();
        logger.setUseParentHandlers(false);
        logger.addHandler(handler);
    }

    public void onCloseApp() {
        SqlService.close();
    }

    @Override
    public void onCreateTask(String taskTitle, String taskText) {
        if (SqlService.checkStatusTask(taskTitle, OPEN_TASK)) return;
        SqlService.createTask(taskTitle, taskText, System.currentTimeMillis() / 1000L);
        logger.log(Level.INFO, "Task Created");
    }

    @Override
    public void onCloseTask(String taskTitle) {
        SqlService.closeTask(taskTitle, CLOSE_TASK);
        logger.log(Level.INFO, "Task Closed");
    }

    @Override
    public void onEditAndSaveTask(String taskTitle, String taskText) {
        SqlService.editAndSaveTask(taskTitle, taskText);
        logger.log(Level.INFO, "Task edited");
    }

    @Override
    public String[] onShowTitleTasks() {
        return SqlService.getTitleTask(OPEN_TASK).split(SqlService.DELIMITER);
    }

    @Override
    public String onShowTextTask(String taskTitle) {
        if (SqlService.checkStatusTask(taskTitle, OPEN_TASK)) return SqlService.getTextTask(taskTitle);
        return null;
    }

    @Override
    public String[] onShowTitleTasksAll() {
        return SqlService.getAllTitleTask().split(SqlService.DELIMITER);
    }

    @Override
    public String onShowTextTasksAll(String taskTitle) {
        return SqlService.getTextTask(taskTitle);
    }

    public void setIconApp(Stage stage) {
        String urlImage = String.valueOf(getClass().getResource("/com/application/todo_list/icons/app_icon.png"));
        stage.getIcons().add(new Image(urlImage));
    }

}
