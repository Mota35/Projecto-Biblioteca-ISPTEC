package isptec.biblioteca.ui;

import isptec.biblioteca.service.AuthService;
import isptec.biblioteca.service.impl.AuthServiceImpl;
import isptec.biblioteca.domain.entities.Pessoa;
import isptec.biblioteca.domain.entities.Bibliotecario;
import isptec.biblioteca.domain.entities.Estudante;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

public class LoginUI extends Application {

    private final AuthService authService = new AuthServiceImpl();
    private static Scene loginScene;
    private static Stage primaryStage;

    // Constantes de estilo
    private static final String STYLE_BUTTON_PRIMARY = "-fx-font-size: 14; -fx-font-weight: bold; -fx-background-color: #0a1428; -fx-text-fill: white; -fx-border-radius: 4; -fx-cursor: hand; -fx-padding: 10;";
    private static final String STYLE_BUTTON_SECONDARY = "-fx-font-size: 14; -fx-font-weight: bold; -fx-background-color: #f5f5f5; -fx-text-fill: #0a1428; -fx-border-color: #0a1428; -fx-border-width: 2; -fx-border-radius: 4; -fx-cursor: hand; -fx-padding: 10;";
    private static final String STYLE_BUTTON_PRIMARY_HOVER = "-fx-font-size: 14; -fx-font-weight: bold; -fx-background-color: #1a2840; -fx-text-fill: white; -fx-border-radius: 4; -fx-cursor: hand; -fx-padding: 10;";
    private static final String STYLE_TEXTFIELD = "-fx-font-size: 12; -fx-padding: 10; -fx-background-color: #f5f5f5; -fx-border-color: #e0e0e0; -fx-border-radius: 4;";
    private static final String STYLE_LABEL = "-fx-font-size: 11; -fx-text-fill: #333333; -fx-font-weight: bold;";
    private static final String STYLE_BUTTON_DEFAULT = "-fx-font-size: 14; -fx-font-weight: bold; -fx-background-color: #0a1428; -fx-text-fill: white; -fx-border-radius: 4; -fx-cursor: hand;";
    
    private static final String TEXT_SENHA = "Senha";
    private static final String TEXT_EMAIL = "Email";

    @Override
    public void start(Stage stage) {
        LoginUI.primaryStage = stage;
        stage.setTitle("Biblioteca ISPTEC");
        stage.setWidth(400);
        stage.setHeight(700);
        stage.centerOnScreen();
        stage.setResizable(false);

        // Criar cena
        loginScene = createLoginScene();
        loginScene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

        stage.setScene(loginScene);
        stage.show();
    }

    public static Scene getLoginScene() {
        return loginScene;
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    private Scene createLoginScene() {
        VBox mainContainer = new VBox();
        mainContainer.setId("root-background");
        mainContainer.setStyle("-fx-background-color: linear-gradient(to bottom, #e1f5ff, #b3e5fc);");
        mainContainer.setAlignment(Pos.TOP_CENTER);
        mainContainer.setPadding(new Insets(20));

        // ScrollPane para permitir scroll
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setStyle("-fx-background-color: transparent; -fx-control-inner-background: transparent;");
        scrollPane.setFitToWidth(true);

        StackPane tabContainer = new StackPane();
        tabContainer.setStyle("-fx-background-color: transparent;");

        VBox cardContainer = new VBox();
        cardContainer.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 10, 0, 0, 5);");
        cardContainer.setPrefWidth(340);
        cardContainer.setPadding(new Insets(30));
        cardContainer.setSpacing(15);
        cardContainer.setAlignment(Pos.TOP_CENTER);

        // Logo
        Label logoLabel = new Label("ISPTEC");
        logoLabel.setStyle("-fx-font-size: 48; -fx-text-fill: #D4A574; -fx-font-weight: bold;");
        logoLabel.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("Biblioteca ISPTEC");
        titleLabel.setStyle("-fx-font-size: 20; -fx-font-weight: bold; -fx-text-fill: #1a1a1a;");
        titleLabel.setAlignment(Pos.CENTER);

        Label subtitleLabel = new Label("Tela de autenticação do sistema");
        subtitleLabel.setStyle("-fx-font-size: 11; -fx-text-fill: #999999;");
        subtitleLabel.setAlignment(Pos.CENTER);

        VBox headerBox = new VBox(5, logoLabel, titleLabel, subtitleLabel);
        headerBox.setAlignment(Pos.CENTER);
        headerBox.setPadding(new Insets(0, 0, 15, 0));

        // Botões de Login e Cadastro
        HBox buttonBox = new HBox();
        buttonBox.setStyle("-fx-spacing: 10;");
        buttonBox.setAlignment(Pos.CENTER);

        Button loginBtn = new Button("Login");
        loginBtn.setPrefWidth(120);
        loginBtn.setPrefHeight(40);
        loginBtn.setStyle(STYLE_BUTTON_PRIMARY);

        Button cadastroBtn = new Button("Cadastro");
        cadastroBtn.setPrefWidth(120);
        cadastroBtn.setPrefHeight(40);
        cadastroBtn.setStyle(STYLE_BUTTON_SECONDARY);

        // Hover effects
        loginBtn.setOnMouseEntered(e -> loginBtn.setStyle(STYLE_BUTTON_PRIMARY_HOVER));
        loginBtn.setOnMouseExited(e -> loginBtn.setStyle(STYLE_BUTTON_PRIMARY));

        cadastroBtn.setOnMouseEntered(e -> cadastroBtn.setStyle(STYLE_BUTTON_PRIMARY));
        cadastroBtn.setOnMouseExited(e -> cadastroBtn.setStyle(STYLE_BUTTON_SECONDARY));

        buttonBox.getChildren().addAll(loginBtn, cadastroBtn);

        // Conteúdo das abas
        VBox loginContent = createLoginTab();
        VBox cadastroContent = createCadastroTab();

        // Inicialmente apenas login visível
        loginContent.setVisible(true);
        loginContent.setManaged(true);
        cadastroContent.setVisible(false);
        cadastroContent.setManaged(false);

        cardContainer.getChildren().addAll(headerBox, buttonBox, loginContent, cadastroContent);

        StackPane.setAlignment(cardContainer, Pos.CENTER);
        tabContainer.getChildren().add(cardContainer);

        // Adicionar tabContainer ao scrollPane
        scrollPane.setContent(tabContainer);
        mainContainer.getChildren().add(scrollPane);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        // Ações dos botões
        loginBtn.setOnAction(e -> {
            loginContent.setVisible(true);
            loginContent.setManaged(true);
            cadastroContent.setVisible(false);
            cadastroContent.setManaged(false);
            loginBtn.setStyle(STYLE_BUTTON_PRIMARY);
            cadastroBtn.setStyle(STYLE_BUTTON_SECONDARY);
        });

        cadastroBtn.setOnAction(e -> {
            loginContent.setVisible(false);
            loginContent.setManaged(false);
            cadastroContent.setVisible(true);
            cadastroContent.setManaged(true);
            loginBtn.setStyle(STYLE_BUTTON_SECONDARY);
            cadastroBtn.setStyle(STYLE_BUTTON_PRIMARY);
        });

        return new Scene(mainContainer);
    }

    private VBox createLoginTab() {
        VBox loginBox = new VBox(12);
        loginBox.setPadding(new Insets(20, 0, 0, 0));

        TextField emailField = new TextField();
        emailField.setPromptText("seu@email.com");
        emailField.setStyle(STYLE_TEXTFIELD);

        PasswordField senhaField = new PasswordField();
        senhaField.setPromptText(TEXT_SENHA);
        senhaField.setStyle(STYLE_TEXTFIELD);

        Label emailLabel = new Label(TEXT_EMAIL);
        emailLabel.setStyle(STYLE_LABEL);

        Label senhaLabel = new Label(TEXT_SENHA);
        senhaLabel.setStyle(STYLE_LABEL);

        Button entrarBtn = new Button("Entrar");
        entrarBtn.setPrefWidth(Double.MAX_VALUE);
        entrarBtn.setPrefHeight(40);
        entrarBtn.setStyle(STYLE_BUTTON_DEFAULT);

        // Hover effect
        entrarBtn.setOnMouseEntered(e -> entrarBtn.setStyle("-fx-font-size: 14; -fx-font-weight: bold; -fx-background-color: #1a2840; -fx-text-fill: white; -fx-border-radius: 4; -fx-cursor: hand;"));
        entrarBtn.setOnMouseExited(e -> entrarBtn.setStyle(STYLE_BUTTON_DEFAULT));

        Label separador = new Label("ou");
        separador.setStyle("-fx-font-size: 11; -fx-text-fill: #999999;");
        separador.setAlignment(Pos.CENTER);

        Hyperlink adminLink = new Hyperlink("Entrar como Administrador");
        adminLink.setStyle("-fx-font-size: 12; -fx-text-fill: #1976d2; -fx-underline: true;");
        adminLink.setAlignment(Pos.CENTER);

        Label esqueceuLabel = new Label("Esqueceu a senha?");
        esqueceuLabel.setStyle("-fx-font-size: 10; -fx-text-fill: #999999;");
        esqueceuLabel.setAlignment(Pos.CENTER);

        loginBox.getChildren().addAll(
                emailLabel, emailField,
                senhaLabel, senhaField,
                entrarBtn,
                separador,
                adminLink,
                esqueceuLabel
        );

        entrarBtn.setOnAction(e -> {
            String email = emailField.getText();
            String senha = senhaField.getText();
            Pessoa p = authService.login(email, senha);
            if (p == null) {
                mostrarAlerta("Erro", "Email ou senha incorretos", Alert.AlertType.ERROR);
                return;
            }
            if (p instanceof Bibliotecario admin) {
                Scene adminScene = EstudanteDashboard.createScene(primaryStage, admin);
                primaryStage.setScene(adminScene);
                primaryStage.setWidth(900);
                primaryStage.setHeight(600);
                primaryStage.centerOnScreen();
            } else if (p instanceof Estudante estudante) {
                Scene estudanteScene = AdministradorDashboard.createScene(primaryStage, estudante);
                primaryStage.setScene(estudanteScene);
                primaryStage.setWidth(1200);
                primaryStage.setHeight(700);
                primaryStage.centerOnScreen();
            }
            emailField.clear();
            senhaField.clear();
        });

        adminLink.setOnAction(e -> {
            Dialog<String> dialog = new Dialog<>();
            dialog.setTitle("Acesso de Administrador");

            TextField adminEmail = new TextField();
            adminEmail.setPromptText(TEXT_EMAIL);

            PasswordField adminSenha = new PasswordField();
            adminSenha.setPromptText(TEXT_SENHA);

            VBox dialogBox = new VBox(10, new Label("Email:"), adminEmail, new Label("Senha:"), adminSenha);
            dialogBox.setPadding(new Insets(10));

            dialog.getDialogPane().setContent(dialogBox);
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            dialog.showAndWait().ifPresent(result -> {
                String email = adminEmail.getText();
                String senha = adminSenha.getText();
                Pessoa p = authService.login(email, senha);
                if (p instanceof Bibliotecario) {
                    mostrarSucesso("Acesso de Administrador Concedido!\n" + p.getNome());
                } else {
                    mostrarAlerta("Erro", "Credenciais de administrador inválidas", Alert.AlertType.ERROR);
                }
            });
        });

        return loginBox;
    }

    private VBox createCadastroTab() {
        VBox cadastroBox = new VBox(15);
        cadastroBox.setPadding(new Insets(20, 0, 0, 0));

        TextField idField = new TextField();
        idField.setPromptText("0001234");
        idField.setStyle(STYLE_TEXTFIELD);

        TextField emailField = new TextField();
        emailField.setPromptText("seu@email.com");
        emailField.setStyle(STYLE_TEXTFIELD);

        PasswordField senhaField = new PasswordField();
        senhaField.setPromptText(TEXT_SENHA);
        senhaField.setStyle(STYLE_TEXTFIELD);

        ComboBox<String> tipoUsuarioCombo = new ComboBox<>();
        tipoUsuarioCombo.getItems().addAll("Estudante", "Administrador");
        tipoUsuarioCombo.setValue("Estudante");
        tipoUsuarioCombo.setStyle(STYLE_TEXTFIELD);
        tipoUsuarioCombo.setPrefWidth(Double.MAX_VALUE);

        Button criarBtn = new Button("Criar Conta");
        criarBtn.setPrefWidth(Double.MAX_VALUE);
        criarBtn.setPrefHeight(40);
        criarBtn.setStyle(STYLE_BUTTON_DEFAULT);

        criarBtn.setOnMouseEntered(e -> criarBtn.setStyle("-fx-font-size: 14; -fx-font-weight: bold; -fx-background-color: #1a2840; -fx-text-fill: white; -fx-border-radius: 4; -fx-cursor: hand;"));
        criarBtn.setOnMouseExited(e -> criarBtn.setStyle(STYLE_BUTTON_DEFAULT));

        Label idLabel = new Label("Id");
        idLabel.setStyle(STYLE_LABEL);

        Label emailLabel = new Label(TEXT_EMAIL);
        emailLabel.setStyle(STYLE_LABEL);

        Label senhaLabel = new Label(TEXT_SENHA);
        senhaLabel.setStyle(STYLE_LABEL);

        Label tipoLabel = new Label("Tipo de Usuário");
        tipoLabel.setStyle(STYLE_LABEL);

        cadastroBox.getChildren().addAll(
                tipoLabel, tipoUsuarioCombo,
                idLabel, idField,
                emailLabel, emailField,
                senhaLabel, senhaField,
                criarBtn
        );

        criarBtn.setOnAction(e -> {
            if (idField.getText().isEmpty() || emailField.getText().isEmpty() || senhaField.getText().isEmpty()) {
                mostrarAlerta("Aviso", "Preencha todos os campos", Alert.AlertType.WARNING);
                return;
            }
            
            String id = idField.getText();
            String email = emailField.getText();
            String senha = senhaField.getText();
            String tipo = tipoUsuarioCombo.getValue();
            
            boolean sucesso = false;
            
            if ("Estudante".equals(tipo)) {
                Estudante estudante = new Estudante();
                estudante.setNome(email.split("@")[0]); // Usa parte do email como nome
                estudante.setEmail(email);
                estudante.setSenha(senha);
                estudante.setMatricula(id);
                sucesso = authService.registrarEstudante(estudante);
            } else if ("Administrador".equals(tipo)) {
                Bibliotecario bibliotecario = new Bibliotecario();
                bibliotecario.setNome(email.split("@")[0]); // Usa parte do email como nome
                bibliotecario.setEmail(email);
                bibliotecario.setSenha(senha);
                bibliotecario.setMatricula(id);
                sucesso = authService.registrarBibliotecario(bibliotecario);
            }
            
            if (sucesso) {
                mostrarSucesso("Conta criada com sucesso!");
                idField.clear();
                emailField.clear();
                senhaField.clear();
                tipoUsuarioCombo.setValue("Estudante");
            } else {
                mostrarAlerta("Erro", "Email já cadastrado ou dados inválidos", Alert.AlertType.ERROR);
            }
        });

        return cadastroBox;
    }

    private void mostrarAlerta(String titulo, String mensagem, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void mostrarSucesso(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sucesso");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    public static void launchLogin(String... args) {
        launch(args);
    }
}
