module com.todolist.todolist {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires org.xerial.sqlitejdbc;
    requires java.sql;
    requires java.desktop;

    opens com.application.todo_list to javafx.fxml;
    exports com.application.todo_list;
}