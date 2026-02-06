package isptec.biblioteca.views;

import isptec.biblioteca.service.AuthService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class LoginView {
    private Stage stage;
    private AuthService authService;

    public LoginView(Stage stage) {
        this.stage = stage;
        this.authService = AuthService.getInstance();
    }

    public Scene createScene() {
        // Container principal
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f5f5f5;");

        // Painel central
        VBox centerBox = new VBox(20);
        centerBox.setAlignment(Pos.CENTER);
        centerBox.setPadding(new Insets(40));
        centerBox.setMaxWidth(400);
        centerBox.setStyle("-fx-background-color: white; -fx-background-radius: 10;");

        // Logo e Título
        ImageView logoImage = new ImageView(
        new Image(getClass().getResourceAsStream("/imagens/logo_2.png"))
        );

        logoImage.setFitWidth(80);   // ajusta se quiseres maior/menor
        logoImage.setFitHeight(80);
        logoImage.setPreserveRatio(true);

        Label titleLabel = new Label("Biblioteca ISPTEC");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 28));
        titleLabel.setTextFill(Color.web("#2563eb"));

        Label subtitleLabel = new Label("Sistema de Gestão de Biblioteca");
        subtitleLabel.setFont(Font.font("System", 14));
        subtitleLabel.setTextFill(Color.web("#6b7280"));

        // Campos de formulário
        Label emailLabel = new Label("Email");
        emailLabel.setFont(Font.font("System", FontWeight.SEMI_BOLD, 12));
        TextField emailField = new TextField();
        emailField.setPromptText("seu@email.com");
        emailField.setPrefHeight(40);
        emailField.setStyle("-fx-font-size: 14; -fx-border-color: #d1d5db; -fx-border-radius: 5; -fx-background-radius: 5;");

        Label senhaLabel = new Label("Senha");
        senhaLabel.setFont(Font.font("System", FontWeight.SEMI_BOLD, 12));
        PasswordField senhaField = new PasswordField();
        senhaField.setPromptText("********");
        senhaField.setPrefHeight(40);
        senhaField.setStyle("-fx-font-size: 14; -fx-border-color: #d1d5db; -fx-border-radius: 5; -fx-background-radius: 5;");

        // Label de erro
        Label errorLabel = new Label();
        errorLabel.setTextFill(Color.RED);
        errorLabel.setVisible(false);

        // Botão de login
        Button loginButton = new Button("Entrar");
        loginButton.setPrefWidth(Double.MAX_VALUE);
        loginButton.setPrefHeight(45);
        loginButton.setStyle(
            "-fx-background-color: #2563eb; " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 16; " +
            "-fx-font-weight: bold; " +
            "-fx-background-radius: 5; " +
            "-fx-cursor: hand;"
        );

        loginButton.setOnMouseEntered(e -> 
            loginButton.setStyle(
                "-fx-background-color: #1d4ed8; " +
                "-fx-text-fill: white; " +
                "-fx-font-size: 16; " +
                "-fx-font-weight: bold; " +
                "-fx-background-radius: 5; " +
                "-fx-cursor: hand;"
            )
        );

        loginButton.setOnMouseExited(e -> 
            loginButton.setStyle(
                "-fx-background-color: #2563eb; " +
                "-fx-text-fill: white; " +
                "-fx-font-size: 16; " +
                "-fx-font-weight: bold; " +
                "-fx-background-radius: 5; " +
                "-fx-cursor: hand;"
            )
        );

        loginButton.setOnAction(e -> {
            String email = emailField.getText();
            String senha = senhaField.getText();

            if (email.isEmpty() || senha.isEmpty()) {
                errorLabel.setText("Por favor, preencha todos os campos");
                errorLabel.setVisible(true);
                return;
            }

            if (authService.login(email, senha)) {
                // Verifica se é o primeiro login e exige troca de senha
                if (authService.isPrimeiraSenha()) {
                    String novaSenha = TrocarSenhaDialog.mostrar();
                    if (novaSenha != null) {
                        authService.alterarSenha(senha, novaSenha);
                    }
                }
                
                // Login bem-sucedido
                if (authService.isAdministrador()) {
                    new DashboardAdminView(stage).show();
                } else {
                    new DashboardUserView(stage).show();
                }
            } else {
                errorLabel.setText("Email ou senha incorretos");
                errorLabel.setVisible(true);
            }
        });

        // Permite login ao pressionar Enter
        senhaField.setOnAction(e -> loginButton.fire());

        // Informações de teste
        VBox infoBox = new VBox(8);
        infoBox.setStyle("-fx-background-color: #eff6ff; -fx-padding: 15; -fx-background-radius: 5;");
        Label infoTitle = new Label("🔑 Credenciais de Teste:");
        infoTitle.setFont(Font.font("System", FontWeight.BOLD, 12));
        Label adminInfo = new Label("Admin: admin001@isptec.co.ao / admin123");
        adminInfo.setFont(Font.font("System", 11));
        Label estudanteInfo = new Label("Estudante: 20230001@isptec.co.ao / 1234");
        estudanteInfo.setFont(Font.font("System", 11));
        infoBox.getChildren().addAll(infoTitle, adminInfo, estudanteInfo);

        // Adicionar todos os elementos
        centerBox.getChildren().addAll(
            logoImage,
            titleLabel,
            subtitleLabel,
            new Separator(),
            emailLabel,
            emailField,
            senhaLabel,
            senhaField,
            errorLabel,
            loginButton,
            infoBox
        );

        // Centralizar o painel
        StackPane stackPane = new StackPane(centerBox);
        stackPane.setPadding(new Insets(40));
        root.setCenter(stackPane);

        Scene scene = new Scene(root, 800, 600);
        return scene;
    }

    public void show() {
        stage.setScene(createScene());
        stage.setTitle("Login - Biblioteca ISPTEC");
        stage.show();
    }
}