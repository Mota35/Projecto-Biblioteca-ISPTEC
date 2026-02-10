package isptec.biblioteca.views;

import isptec.biblioteca.ServiceFactory;
import isptec.biblioteca.enumeracao.Perfil;
import isptec.biblioteca.model.entities.Pessoa;
import isptec.biblioteca.service.AuthService;
import javafx.animation.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.paint.CycleMethod;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Objects;

public class LoginView {
    private final Stage stage;
    private final AuthService authService;

    public LoginView(Stage stage) {
        this.stage = stage;
        this.authService = ServiceFactory.getInstance().getAuthService();
    }

    public Scene createScene() {
        // Container principal com gradiente
        HBox root = new HBox();

        // Lado esquerdo - Painel decorativo
        StackPane leftPane = createDecorativePane();
        HBox.setHgrow(leftPane, Priority.ALWAYS);

        // Lado direito - Formulário de login
        VBox rightPane = createLoginForm();
        rightPane.setPrefWidth(500);
        rightPane.setMinWidth(450);

        root.getChildren().addAll(leftPane, rightPane);

        Scene scene = new Scene(root, 1100, 700);
        return scene;
    }

    private StackPane createDecorativePane() {
        StackPane pane = new StackPane();

        // Gradiente de fundo
        Rectangle background = new Rectangle();
        background.widthProperty().bind(pane.widthProperty());
        background.heightProperty().bind(pane.heightProperty());

        LinearGradient gradient = new LinearGradient(
            0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
            new Stop(0, Color.web("#6366f1")),
            new Stop(0.5, Color.web("#8b5cf6")),
            new Stop(1, Color.web("#a855f7"))
        );
        background.setFill(gradient);

        // Círculos decorativos animados
        VBox decorElements = new VBox(30);
        decorElements.setAlignment(Pos.CENTER);

        // Ícone grande de livro
        Label bookIcon = new Label("📚");
        bookIcon.setStyle("-fx-font-size: 80px;");

        // Texto de boas-vindas
        Label welcomeText = new Label("Bem-vindo à");
        welcomeText.setFont(Font.font("Segoe UI", FontWeight.LIGHT, 24));
        welcomeText.setTextFill(Color.WHITE);

        Label titleText = new Label("Biblioteca ISPTEC");
        titleText.setFont(Font.font("Segoe UI", FontWeight.BOLD, 42));
        titleText.setTextFill(Color.WHITE);

        Label descText = new Label("O seu portal de conhecimento");
        descText.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 16));
        descText.setTextFill(Color.web("#e0e7ff"));

        // Características
        VBox features = new VBox(15);
        features.setAlignment(Pos.CENTER_LEFT);
        features.setPadding(new Insets(40, 60, 0, 60));

        features.getChildren().addAll(
            createFeatureItem("✨", "Milhares de livros disponíveis"),
            createFeatureItem("🔍", "Pesquisa inteligente com IA"),
            createFeatureItem("📱", "Reservas online fáceis"),
            createFeatureItem("💬", "Chatbot assistente 24/7")
        );

        decorElements.getChildren().addAll(bookIcon, welcomeText, titleText, descText, features);

        // Círculos decorativos de fundo
        Circle circle1 = new Circle(150, Color.web("#ffffff", 0.1));
        circle1.setTranslateX(-200);
        circle1.setTranslateY(-150);

        Circle circle2 = new Circle(100, Color.web("#ffffff", 0.08));
        circle2.setTranslateX(200);
        circle2.setTranslateY(200);

        Circle circle3 = new Circle(80, Color.web("#ffffff", 0.05));
        circle3.setTranslateX(-150);
        circle3.setTranslateY(250);

        // Animação dos círculos
        animateCircle(circle1, 3000);
        animateCircle(circle2, 4000);
        animateCircle(circle3, 5000);

        pane.getChildren().addAll(background, circle1, circle2, circle3, decorElements);

        return pane;
    }

    private HBox createFeatureItem(String icon, String text) {
        HBox item = new HBox(12);
        item.setAlignment(Pos.CENTER_LEFT);

        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-font-size: 20px;");

        Label textLabel = new Label(text);
        textLabel.setFont(Font.font("Segoe UI", 14));
        textLabel.setTextFill(Color.web("#e0e7ff"));

        item.getChildren().addAll(iconLabel, textLabel);
        return item;
    }

    private void animateCircle(Circle circle, int duration) {
        TranslateTransition translate = new TranslateTransition(Duration.millis(duration), circle);
        translate.setByY(30);
        translate.setCycleCount(Animation.INDEFINITE);
        translate.setAutoReverse(true);
        translate.setInterpolator(Interpolator.EASE_BOTH);
        translate.play();
    }

    private VBox createLoginForm() {
        VBox formPane = new VBox(25);
        formPane.setAlignment(Pos.CENTER);
        formPane.setPadding(new Insets(50));
        formPane.setStyle("-fx-background-color: #ffffff;");

        // Logo
        try {
            ImageView logoImage = new ImageView(
                new Image(Objects.requireNonNull(getClass().getResourceAsStream("/imagens/logo_2.png")))
            );
            logoImage.setFitWidth(70);
            logoImage.setFitHeight(70);
            logoImage.setPreserveRatio(true);
            formPane.getChildren().add(logoImage);
        } catch (Exception e) {
            Label logoPlaceholder = new Label("📖");
            logoPlaceholder.setStyle("-fx-font-size: 50px;");
            formPane.getChildren().add(logoPlaceholder);
        }

        // Título
        Label titleLabel = new Label("Iniciar Sessão");
        titleLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 28));
        titleLabel.setTextFill(Color.web("#1e293b"));

        Label subtitleLabel = new Label("Entre com as suas credenciais");
        subtitleLabel.setFont(Font.font("Segoe UI", 14));
        subtitleLabel.setTextFill(Color.web("#64748b"));

        // Formulário
        VBox formFields = new VBox(20);
        formFields.setMaxWidth(350);

        // Campo Email
        VBox emailBox = new VBox(8);
        Label emailLabel = new Label("📧  Email");
        emailLabel.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 13));
        emailLabel.setTextFill(Color.web("#374151"));

        TextField emailField = new TextField();
        emailField.setPromptText("seu@email.com");
        emailField.setPrefHeight(50);
        emailField.setStyle(
            "-fx-background-color: #f8fafc; " +
            "-fx-border-color: #e2e8f0; " +
            "-fx-border-radius: 12; " +
            "-fx-background-radius: 12; " +
            "-fx-font-size: 14; " +
            "-fx-padding: 0 15;"
        );
        emailField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                emailField.setStyle(
                    "-fx-background-color: #ffffff; " +
                    "-fx-border-color: #6366f1; " +
                    "-fx-border-width: 2; " +
                    "-fx-border-radius: 12; " +
                    "-fx-background-radius: 12; " +
                    "-fx-font-size: 14; " +
                    "-fx-padding: 0 15;"
                );
            } else {
                emailField.setStyle(
                    "-fx-background-color: #f8fafc; " +
                    "-fx-border-color: #e2e8f0; " +
                    "-fx-border-radius: 12; " +
                    "-fx-background-radius: 12; " +
                    "-fx-font-size: 14; " +
                    "-fx-padding: 0 15;"
                );
            }
        });
        emailBox.getChildren().addAll(emailLabel, emailField);

        // Campo Senha
        VBox senhaBox = new VBox(8);
        Label senhaLabel = new Label("🔒  Senha");
        senhaLabel.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 13));
        senhaLabel.setTextFill(Color.web("#374151"));

        PasswordField senhaField = new PasswordField();
        senhaField.setPromptText("••••••••");
        senhaField.setPrefHeight(50);
        senhaField.setStyle(
            "-fx-background-color: #f8fafc; " +
            "-fx-border-color: #e2e8f0; " +
            "-fx-border-radius: 12; " +
            "-fx-background-radius: 12; " +
            "-fx-font-size: 14; " +
            "-fx-padding: 0 15;"
        );
        senhaField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                senhaField.setStyle(
                    "-fx-background-color: #ffffff; " +
                    "-fx-border-color: #6366f1; " +
                    "-fx-border-width: 2; " +
                    "-fx-border-radius: 12; " +
                    "-fx-background-radius: 12; " +
                    "-fx-font-size: 14; " +
                    "-fx-padding: 0 15;"
                );
            } else {
                senhaField.setStyle(
                    "-fx-background-color: #f8fafc; " +
                    "-fx-border-color: #e2e8f0; " +
                    "-fx-border-radius: 12; " +
                    "-fx-background-radius: 12; " +
                    "-fx-font-size: 14; " +
                    "-fx-padding: 0 15;"
                );
            }
        });
        senhaBox.getChildren().addAll(senhaLabel, senhaField);

        // Label de erro
        Label errorLabel = new Label();
        errorLabel.setTextFill(Color.web("#ef4444"));
        errorLabel.setFont(Font.font("Segoe UI", 13));
        errorLabel.setVisible(false);

        // Botão de login
        Button loginButton = new Button("Entrar");
        loginButton.setPrefWidth(Double.MAX_VALUE);
        loginButton.setPrefHeight(50);
        loginButton.setStyle(
            "-fx-background-color: linear-gradient(to right, #6366f1, #8b5cf6); " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 16; " +
            "-fx-font-weight: bold; " +
            "-fx-background-radius: 12; " +
            "-fx-cursor: hand;"
        );

        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.web("#6366f1", 0.4));
        shadow.setRadius(15);
        shadow.setOffsetY(5);
        loginButton.setEffect(shadow);

        loginButton.setOnMouseEntered(e -> {
            loginButton.setStyle(
                "-fx-background-color: linear-gradient(to right, #4f46e5, #7c3aed); " +
                "-fx-text-fill: white; " +
                "-fx-font-size: 16; " +
                "-fx-font-weight: bold; " +
                "-fx-background-radius: 12; " +
                "-fx-cursor: hand;"
            );
            shadow.setRadius(20);
            shadow.setOffsetY(8);
        });

        loginButton.setOnMouseExited(e -> {
            loginButton.setStyle(
                "-fx-background-color: linear-gradient(to right, #6366f1, #8b5cf6); " +
                "-fx-text-fill: white; " +
                "-fx-font-size: 16; " +
                "-fx-font-weight: bold; " +
                "-fx-background-radius: 12; " +
                "-fx-cursor: hand;"
            );
            shadow.setRadius(15);
            shadow.setOffsetY(5);
        });

        loginButton.setOnAction(e -> handleLogin(emailField.getText(), senhaField.getText(), errorLabel));
        senhaField.setOnAction(e -> loginButton.fire());

        formFields.getChildren().addAll(emailBox, senhaBox, errorLabel, loginButton);

        // Link de registo
        HBox registerBox = new HBox(5);
        registerBox.setAlignment(Pos.CENTER);
        Label noAccountLabel = new Label("Não tem conta?");
        noAccountLabel.setFont(Font.font("Segoe UI", 13));
        noAccountLabel.setTextFill(Color.web("#64748b"));

        Hyperlink registerLink = new Hyperlink("Registar-se");
        registerLink.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 13));
        registerLink.setTextFill(Color.web("#6366f1"));
        registerLink.setBorder(Border.EMPTY);
        registerLink.setOnAction(e -> showRegisterDialog());

        registerBox.getChildren().addAll(noAccountLabel, registerLink);

        // Separador
        HBox separatorBox = new HBox(15);
        separatorBox.setAlignment(Pos.CENTER);
        Separator sep1 = new Separator();
        sep1.setPrefWidth(100);
        Label orLabel = new Label("ou");
        orLabel.setTextFill(Color.web("#94a3b8"));
        Separator sep2 = new Separator();
        sep2.setPrefWidth(100);
        separatorBox.getChildren().addAll(sep1, orLabel, sep2);

        // Credenciais de teste
        VBox infoBox = new VBox(10);
        infoBox.setMaxWidth(350);
        infoBox.setStyle(
            "-fx-background-color: linear-gradient(to right, #ede9fe, #fae8ff); " +
            "-fx-padding: 20; " +
            "-fx-background-radius: 12;"
        );

        Label infoTitle = new Label("🔑 Credenciais de Teste");
        infoTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));
        infoTitle.setTextFill(Color.web("#5b21b6"));

        VBox credentials = new VBox(6);
        Label adminInfo = new Label("👨‍💼 Admin: admin001@isptec.co.ao / admin123");
        adminInfo.setFont(Font.font("Segoe UI", 12));
        adminInfo.setTextFill(Color.web("#6b21a8"));

        Label estudanteInfo = new Label("👨‍🎓 Estudante: 20230001@isptec.co.ao / 1234");
        estudanteInfo.setFont(Font.font("Segoe UI", 12));
        estudanteInfo.setTextFill(Color.web("#6b21a8"));

        credentials.getChildren().addAll(adminInfo, estudanteInfo);
        infoBox.getChildren().addAll(infoTitle, credentials);

        formPane.getChildren().addAll(
            titleLabel,
            subtitleLabel,
            formFields,
            registerBox,
            separatorBox,
            infoBox
        );

        return formPane;
    }

    private void handleLogin(String email, String senha, Label errorLabel) {
        if (email.isEmpty() || senha.isEmpty()) {
            showError(errorLabel, "Por favor, preencha todos os campos");
            return;
        }

        Pessoa usuario = authService.login(email, senha);
        if (usuario != null) {
            // Verificar se é admin ou bibliotecário
            if (usuario.getPerfil() == Perfil.BIBLIOTECARIO || usuario.getPerfil() == Perfil.ADMIN) {
                new DashboardAdminView(stage).show();
            } else {
                new DashboardUserView(stage).show();
            }
        } else {
            showError(errorLabel, "Email ou senha incorretos");
            shakeAnimation(errorLabel);
        }
    }

    private void showError(Label errorLabel, String message) {
        errorLabel.setText("⚠️ " + message);
        errorLabel.setVisible(true);

        FadeTransition fade = new FadeTransition(Duration.millis(200), errorLabel);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();
    }

    private void shakeAnimation(Label label) {
        TranslateTransition shake = new TranslateTransition(Duration.millis(50), label);
        shake.setByX(10);
        shake.setCycleCount(6);
        shake.setAutoReverse(true);
        shake.play();
    }

    private void showRegisterDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Registo");
        alert.setHeaderText("Funcionalidade em desenvolvimento");
        alert.setContentText("O registo de novos utilizadores estará disponível em breve.\nContacte a biblioteca para criar uma conta.");
        alert.showAndWait();
    }

    public void show() {
        stage.setScene(createScene());
        stage.setTitle("Login - Biblioteca ISPTEC");
        stage.centerOnScreen();
        stage.show();
    }
}