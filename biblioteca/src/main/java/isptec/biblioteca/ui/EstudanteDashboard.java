package isptec.biblioteca.ui;

import isptec.biblioteca.domain.entities.Pessoa;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

public class EstudanteDashboard {

    private EstudanteDashboard() {
        // Classe utilitária
    }

    public static Scene createScene(Stage stage, Pessoa pessoa) {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f5f5f5;");

        // Header
        HBox header = new HBox();
        header.setStyle("-fx-background-color: #0a1428; -fx-padding: 15;");
        header.setAlignment(Pos.CENTER_LEFT);
        header.setSpacing(20);

        Label logoLabel = new Label("📚 Biblioteca ISPTEC");
        logoLabel.setStyle("-fx-font-size: 18; -fx-font-weight: bold; -fx-text-fill: white;");

        HBox spacer = new HBox();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label userLabel = new Label("👤 " + pessoa.getNome());
        userLabel.setStyle("-fx-font-size: 12; -fx-text-fill: white;");

        Button logoutBtn = new Button("Sair");
        logoutBtn.setStyle("-fx-font-size: 11; -fx-padding: 8 15 8 15; -fx-background-color: #D4A574; -fx-text-fill: white; -fx-border-radius: 4;");
        logoutBtn.setOnAction(e -> voltar(stage));

        header.getChildren().addAll(logoLabel, spacer, userLabel, logoutBtn);

        // Conteúdo principal
        VBox mainContent = new VBox();
        mainContent.setStyle("-fx-background-color: #f5f5f5;");
        mainContent.setPadding(new Insets(30));
        mainContent.setSpacing(20);
        mainContent.setAlignment(Pos.TOP_CENTER);

        Label welcomeLabel = new Label("Bem-vindo, " + pessoa.getNome() + "!");
        welcomeLabel.setStyle("-fx-font-size: 24; -fx-font-weight: bold; -fx-text-fill: #0a1428;");

        Label subtitleLabel = new Label("Matrícula: " + pessoa.getMatricula());
        subtitleLabel.setStyle("-fx-font-size: 14; -fx-text-fill: #666;");

        VBox infoBox = new VBox();
        infoBox.setStyle("-fx-background-color: white; -fx-border-radius: 8; -fx-padding: 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 3);");
        infoBox.setSpacing(15);

        Label infoTitleLabel = new Label("Informações");
        infoTitleLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-text-fill: #0a1428;");

        Label emailLabel = new Label("Email: " + pessoa.getEmail());
        emailLabel.setStyle("-fx-font-size: 12; -fx-text-fill: #333;");

        Label statusLabel = new Label("Status: Ativo");
        statusLabel.setStyle("-fx-font-size: 12; -fx-text-fill: #4caf50; -fx-font-weight: bold;");

        infoBox.getChildren().addAll(infoTitleLabel, emailLabel, statusLabel);

        mainContent.getChildren().addAll(welcomeLabel, subtitleLabel, infoBox);

        root.setTop(header);
        root.setCenter(mainContent);

        return new Scene(root, 900, 600);
    }

    private static void voltar(Stage stage) {
        stage.setScene(LoginUI.getLoginScene());
    }
}
