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

public class AdministradorDashboard {

    private static final String STYLE_SECONDARY_BTN = "-fx-font-size: 12; -fx-padding: 10; -fx-background-color: #f5f5f5; -fx-text-fill: #333; -fx-border-color: #ddd; -fx-border-radius: 4; -fx-cursor: hand;";

    private AdministradorDashboard() {
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

        Label logoLabel = new Label("📚 Biblioteca ISPTEC - Admin");
        logoLabel.setStyle("-fx-font-size: 18; -fx-font-weight: bold; -fx-text-fill: white;");

        HBox spacer = new HBox();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label userLabel = new Label("👤 " + pessoa.getNome());
        userLabel.setStyle("-fx-font-size: 12; -fx-text-fill: white;");

        Button logoutBtn = new Button("Sair");
        logoutBtn.setStyle("-fx-font-size: 11; -fx-padding: 8 15 8 15; -fx-background-color: #D4A574; -fx-text-fill: white; -fx-border-radius: 4;");
        logoutBtn.setOnAction(e -> voltar(stage));

        header.getChildren().addAll(logoLabel, spacer, userLabel, logoutBtn);

        // Menu lateral
        VBox sidebar = new VBox();
        sidebar.setStyle("-fx-background-color: #f0f0f0; -fx-padding: 15; -fx-border-color: #ddd; -fx-border-width: 0 1 0 0;");
        sidebar.setPrefWidth(200);
        sidebar.setSpacing(10);

        Label menuLabel = new Label("Menu");
        menuLabel.setStyle("-fx-font-size: 14; -fx-font-weight: bold; -fx-text-fill: #0a1428;");

        Button dashboardBtn = new Button("📊 Dashboard");
        dashboardBtn.setStyle("-fx-font-size: 12; -fx-padding: 10; -fx-background-color: #0a1428; -fx-text-fill: white; -fx-border-radius: 4; -fx-cursor: hand;");
        dashboardBtn.setPrefWidth(Double.MAX_VALUE);

        Button livrosBtn = new Button("📖 Consultar Livros");
        livrosBtn.setStyle(STYLE_SECONDARY_BTN);
        livrosBtn.setPrefWidth(Double.MAX_VALUE);

        Button chatBtn = new Button("💬 Chatbot");
        chatBtn.setStyle(STYLE_SECONDARY_BTN);
        chatBtn.setPrefWidth(Double.MAX_VALUE);

        Button perfilBtn = new Button("👤 Perfil");
        perfilBtn.setStyle(STYLE_SECONDARY_BTN);
        perfilBtn.setPrefWidth(Double.MAX_VALUE);

        sidebar.getChildren().addAll(menuLabel, dashboardBtn, livrosBtn, chatBtn, perfilBtn);

        // Conteúdo principal
        VBox mainContent = new VBox();
        mainContent.setStyle("-fx-background-color: #f5f5f5;");
        mainContent.setPadding(new Insets(30));
        mainContent.setSpacing(20);
        mainContent.setAlignment(Pos.TOP_CENTER);

        Label welcomeLabel = new Label("Meu Painel");
        welcomeLabel.setStyle("-fx-font-size: 24; -fx-font-weight: bold; -fx-text-fill: #0a1428;");

        Label subtitleLabel = new Label("Bem-vindo, " + pessoa.getNome());
        subtitleLabel.setStyle("-fx-font-size: 14; -fx-text-fill: #666;");

        // Cards com estatísticas
        HBox statsBox = new HBox();
        statsBox.setSpacing(20);
        statsBox.setPrefHeight(100);

        VBox card1 = createCard("📚 Livros Emprestados", "0");
        VBox card2 = createCard("🔖 Reservas ativas", "0");
        VBox card3 = createCard("💰 Multas", "KZ 0.00");

        statsBox.getChildren().addAll(card1, card2, card3);

        VBox infoBox = new VBox();
        infoBox.setStyle("-fx-background-color: white; -fx-border-radius: 8; -fx-padding: 20; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 3);");
        infoBox.setSpacing(15);

        Label infoTitleLabel = new Label("Meus Empréstimos");
        infoTitleLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-text-fill: #0a1428;");

        Label noDataLabel = new Label("Você não possui empréstimos ativos");
        noDataLabel.setStyle("-fx-font-size: 12; -fx-text-fill: #999; -fx-text-alignment: center;");
        noDataLabel.setAlignment(Pos.CENTER);

        infoBox.getChildren().addAll(infoTitleLabel, noDataLabel);

        mainContent.getChildren().addAll(welcomeLabel, subtitleLabel, statsBox, infoBox);

        // Container central com sidebar
        HBox centerContainer = new HBox();
        centerContainer.getChildren().addAll(sidebar, mainContent);
        HBox.setHgrow(mainContent, Priority.ALWAYS);

        root.setTop(header);
        root.setCenter(centerContainer);

        return new Scene(root, 1200, 700);
    }

    private static VBox createCard(String title, String value) {
        VBox card = new VBox();
        card.setStyle("-fx-background-color: white; -fx-border-radius: 8; -fx-padding: 15; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 3);");
        card.setSpacing(10);
        card.setPrefWidth(200);

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 12; -fx-text-fill: #999; -fx-font-weight: bold;");

        Label valueLabel = new Label(value);
        valueLabel.setStyle("-fx-font-size: 24; -fx-font-weight: bold; -fx-text-fill: #0a1428;");

        card.getChildren().addAll(titleLabel, valueLabel);
        return card;
    }

    private static void voltar(Stage stage) {
        stage.setScene(LoginUI.getLoginScene());
    }
}
