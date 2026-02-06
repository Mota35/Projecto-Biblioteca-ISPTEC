package isptec.biblioteca;

import isptec.biblioteca.views.LoginView;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Classe principal da aplicação Biblioteca ISPTEC
 * Sistema de Gestão de Biblioteca com JavaFX
 * 
 * @author ISPTEC
 * @version 1.0.0
 */

public class Program extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Biblioteca ISPTEC");
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(600);
        
        // Iniciar com a tela de login
        LoginView loginView = new LoginView(primaryStage);
        loginView.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
