package com.application.todo_list;

public interface OnActionTodoList {

    void onCreateTask(String taskTitle, String taskText);
    void onCloseTask(String taskTitle);
    void onEditAndSaveTask(String taskTitle, String taskText);
    String[] onShowTitleTasks();
    String onShowTextTask(String taskTitle);

}
