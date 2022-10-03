package com.application.todo_list;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.Objects;

public class TodoList extends Application implements Thread.UncaughtExceptionHandler {

    private static final int WIDTH = 650;
    private static final int HEIGHT = 400;
    private final SettingApp settingApp = new SettingApp();

    @FXML
    Button addTaskButton, closeTaskButton, historyButton, saveEditedTaskButton, startAppButton, closeHistoryButton;

    @FXML
    ListView<String> taskList;

    @FXML
    TextField fieldTitle, fieldText;

    @FXML
    HBox topOfPanel, taskOfPanel, centerOfPanel;

    @FXML
    FlowPane taskTextOfPanel;
    @FXML
    GridPane bottomButtonsPanel;

    @FXML
    TextArea taskText;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Thread.setDefaultUncaughtExceptionHandler(this);
        stage.setTitle("TODO LIST");
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/application/todo_list/main.fxml")));
        stage.setResizable(false);
        stage.setScene(new Scene(root, WIDTH, HEIGHT));
        settingApp.setIconApp(stage);
        stage.show();
        stage.setOnCloseRequest(e -> {
            settingApp.onCloseApp();
            System.exit(1);
        });
    }

    @FXML
    public void onStartApp() {
        Platform.runLater(() -> {
            topOfPanel.setVisible(true);
            taskOfPanel.setVisible(true);
            centerOfPanel.setVisible(true);
            historyButton.setVisible(true);
            startAppButton.setVisible(false);
            settingApp.onStartApp();
            onShowTasks();
        });
    }

    private void showException(Thread thread, Throwable exception) {
        String message;
        StackTraceElement[] ste = exception.getStackTrace();
        message = String.format("Exception in thread \"%s\" %s: %s\n\tat %s",
                thread.getName(), exception.getClass().getCanonicalName(),
                exception.getMessage(), ste[0]);
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
            alert.showAndWait();
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.setAlwaysOnTop(true);
        });
    }

    @Override
    public void uncaughtException(Thread thread, Throwable exception) {
        exception.printStackTrace();
        showException(thread, exception);
        System.exit(1);
    }

    @FXML
    public void onCreateTask() {
        settingApp.onCreateTask(fieldTitle.getText(), fieldText.getText());
        onShowTasks();
    }

    @FXML
    public void onEditAndSaveTask() {
        MultipleSelectionModel<String> getText = taskList.getSelectionModel();
        settingApp.onEditAndSaveTask(getText.getSelectedItem(), taskText.getText());
        onShowTextTask();
    }

    @FXML
    public void onCloseTask() {
        MultipleSelectionModel<String> getText = taskList.getSelectionModel();
        settingApp.onCloseTask(getText.getSelectedItem());
        onShowTasks();
    }
    @FXML
    private void onShowTasks() {
        Platform.runLater(() -> {
            String[] listTasksArray = settingApp.onShowTitleTasks();
            ObservableList<String> tasks = FXCollections.observableArrayList(listTasksArray);
            taskList.setItems(tasks);
            historyButton.setVisible(true);
            closeHistoryButton.setVisible(false);
            taskList.setOnMouseClicked(event -> {
                taskTextOfPanel.setVisible(true);
                onShowTextTask();
            });
            closeTaskButton.setVisible(true);
            saveEditedTaskButton.setVisible(true);
            taskText.setEditable(true);
        });
    }

    private void onShowTextTask() {
        MultipleSelectionModel<String> getText = taskList.getSelectionModel();
        taskText.setText(settingApp.onShowTextTask(getText.getSelectedItem()));
    }

    @FXML
    private void onShowHistoryTasks() {
        Platform.runLater(() -> {
            String[] listTasksArray = settingApp.onShowTitleTasksAll();
            ObservableList<String> tasks = FXCollections.observableArrayList(listTasksArray);
            taskList.setItems(tasks);
            historyButton.setVisible(false);
            closeHistoryButton.setVisible(true);
            closeTaskButton.setVisible(false);
            saveEditedTaskButton.setVisible(false);
            taskText.setEditable(false);
            taskList.setOnMouseClicked(event -> {
                taskTextOfPanel.setVisible(true);
                MultipleSelectionModel<String> getText = taskList.getSelectionModel();
                taskText.setText(settingApp.onShowTextTasksAll(getText.getSelectedItem()));
            });
        });
    }

}
