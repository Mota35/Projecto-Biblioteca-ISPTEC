module isptec.biblioteca {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.net.http;
    requires java.sql;
    requires com.zaxxer.hikari;

    opens isptec.biblioteca to javafx.graphics;
    opens isptec.biblioteca.views to javafx.fxml;
    opens isptec.biblioteca.model to javafx.base;
    opens isptec.biblioteca.model.entities to javafx.base;

    exports isptec.biblioteca;
    exports isptec.biblioteca.views;
    exports isptec.biblioteca.service;
    exports isptec.biblioteca.service.impl;
    exports isptec.biblioteca.model;
    exports isptec.biblioteca.model.entities;
    exports isptec.biblioteca.enumeracao;
}

