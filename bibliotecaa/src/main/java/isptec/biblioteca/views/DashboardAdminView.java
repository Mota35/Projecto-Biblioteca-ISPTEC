package isptec.biblioteca.views;

import isptec.biblioteca.service.AuthService;
import isptec.biblioteca.service.LibraryService;
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


public class DashboardAdminView {
    private Stage stage;
    private AuthService authService;
    private LibraryService libraryService;
    private BorderPane mainLayout;

    public DashboardAdminView(Stage stage) {
        this.stage = stage;
        this.authService = AuthService.getInstance();
        this.libraryService = LibraryService.getInstance();
        this.mainLayout = new BorderPane();
    }

    public void show() {
        // Menu lateral
        VBox sidebar = createSidebar();
        mainLayout.setLeft(sidebar);

        // Conteúdo inicial - Dashboard
        mainLayout.setCenter(createDashboardContent());

        Scene scene = new Scene(mainLayout, 1200, 700);
        stage.setScene(scene);
        stage.setTitle("Dashboard Administrador - Biblioteca ISPTEC");
        stage.show();
    }

    private VBox createSidebar() {
        VBox sidebar = new VBox(10);
        sidebar.setPrefWidth(250);
        sidebar.setStyle("-fx-background-color: white; -fx-border-color: #e5e7eb; -fx-border-width: 0 1 0 0;");
        sidebar.setPadding(new Insets(20));

        // Logo
        HBox logoBox = new HBox(10);
        logoBox.setAlignment(Pos.CENTER_LEFT);
        ImageView logoImage = new ImageView(
        new Image(getClass().getResourceAsStream("/imagens/logo_2.png"))
        );

        logoImage.setFitWidth(34);   // ajusta se quiseres maior/menor
        logoImage.setFitHeight(34);
        logoImage.setPreserveRatio(true);
        
        VBox logoText = new VBox(2);
        Label titleLabel = new Label("Biblioteca ISPTEC");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        Label subtitleLabel = new Label("Administrador");
        subtitleLabel.setFont(Font.font("System", 10));
        subtitleLabel.setTextFill(Color.web("#6b7280"));
        logoText.getChildren().addAll(titleLabel, subtitleLabel);
        
        logoBox.getChildren().addAll(logoImage, logoText);

        Separator separator = new Separator();
        separator.setPadding(new Insets(10, 0, 10, 0));

        // Menu items
        VBox menuBox = new VBox(5);
        
        Button dashboardBtn = createMenuButton("📊 Dashboard", true);
        dashboardBtn.setOnAction(e -> mainLayout.setCenter(createDashboardContent()));
        
        Button livrosBtn = createMenuButton("📚 Livros", false);
        livrosBtn.setOnAction(e -> new LivrosView(stage, mainLayout).show());
        
        Button membrosBtn = createMenuButton("👥 Membros", false);
        membrosBtn.setOnAction(e -> new MembrosView(stage, mainLayout).show());
        
        Button emprestimosBtn = createMenuButton("📖 Empréstimos", false);
        emprestimosBtn.setOnAction(e -> new EmprestimosView(stage, mainLayout).show());
        
        Button reservasBtn = createMenuButton("⏰ Reservas", false);
        reservasBtn.setOnAction(e -> new ReservasView(stage, mainLayout).show());
        
        Button relatoriosBtn = createMenuButton("📄 Relatórios", false);
        relatoriosBtn.setOnAction(e -> new RelatoriosView(stage, mainLayout).show());
        
        Button chatbotBtn = createMenuButton("💬 Chatbot", false);
        chatbotBtn.setOnAction(e -> new ChatbotView(stage, mainLayout).show());

        menuBox.getChildren().addAll(
            dashboardBtn, livrosBtn, membrosBtn, emprestimosBtn, 
            reservasBtn, relatoriosBtn, chatbotBtn
        );

        // Spacer
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        // User info e logout
        VBox userBox = new VBox(10);
        userBox.setStyle("-fx-background-color: #f9fafb; -fx-padding: 10; -fx-background-radius: 5;");
        
        Label userName = new Label(authService.getUsuarioLogado().getNome());
        userName.setFont(Font.font("System", FontWeight.SEMI_BOLD, 12));
        
        Label userEmail = new Label(authService.getUsuarioLogado().getEmail());
        userEmail.setFont(Font.font("System", 10));
        userEmail.setTextFill(Color.web("#6b7280"));
        
        Button logoutBtn = new Button("🚪 Sair");
        logoutBtn.setPrefWidth(Double.MAX_VALUE);
        logoutBtn.setStyle("-fx-background-color: #ef4444; -fx-text-fill: white; -fx-cursor: hand;");
        logoutBtn.setOnAction(e -> {
            authService.logout();
            new LoginView(stage).show();
        });
        
        userBox.getChildren().addAll(userName, userEmail, logoutBtn);

        sidebar.getChildren().addAll(logoBox, separator, menuBox, spacer, userBox);
        return sidebar;
    }

    private Button createMenuButton(String text, boolean active) {
        Button btn = new Button(text);
        btn.setPrefWidth(Double.MAX_VALUE);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setPrefHeight(40);
        
        if (active) {
            btn.setStyle(
                "-fx-background-color: #2563eb; " +
                "-fx-text-fill: white; " +
                "-fx-font-size: 14; " +
                "-fx-background-radius: 5; " +
                "-fx-cursor: hand;"
            );
        } else {
            btn.setStyle(
                "-fx-background-color: transparent; " +
                "-fx-text-fill: #374151; " +
                "-fx-font-size: 14; " +
                "-fx-cursor: hand;"
            );
            
            btn.setOnMouseEntered(e -> 
                btn.setStyle(
                    "-fx-background-color: #f3f4f6; " +
                    "-fx-text-fill: #374151; " +
                    "-fx-font-size: 14; " +
                    "-fx-background-radius: 5; " +
                    "-fx-cursor: hand;"
                )
            );
            
            btn.setOnMouseExited(e -> 
                btn.setStyle(
                    "-fx-background-color: transparent; " +
                    "-fx-text-fill: #374151; " +
                    "-fx-font-size: 14; " +
                    "-fx-cursor: hand;"
                )
            );
        }
        
        return btn;
    }

    private VBox createDashboardContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));
        content.setStyle("-fx-background-color: #f9fafb;");

        // Título
        Label title = new Label("Dashboard do Administrador");
        title.setFont(Font.font("System", FontWeight.BOLD, 28));

        Label subtitle = new Label("Visão geral completa do sistema da biblioteca");
        subtitle.setFont(Font.font("System", 14));
        subtitle.setTextFill(Color.web("#6b7280"));

        // Cards de estatísticas
        GridPane statsGrid = new GridPane();
        statsGrid.setHgap(15);
        statsGrid.setVgap(15);

        // Card 1: Total de Livros
        VBox card1 = createStatCard("📚", "Total de Livros", 
            String.valueOf(libraryService.getTotalLivros()), 
            libraryService.listarLivros().size() + " títulos diferentes",
            "#2563eb");

        // Card 2: Livros Emprestados
        VBox card2 = createStatCard("📈", "Livros Emprestados", 
            String.valueOf(libraryService.getLivrosEmprestados()), 
            "Empréstimos ativos",
            "#16a34a");

        // Card 3: Reservas Pendentes
        VBox card3 = createStatCard("⏰", "Reservas Pendentes", 
            String.valueOf(libraryService.getReservasPendentes()), 
            "Aguardando disponibilidade",
            "#f59e0b");

        // Card 4: Membros Ativos
        VBox card4 = createStatCard("👥", "Membros Ativos", 
            String.valueOf(libraryService.getMembrosAtivos()), 
            "De " + libraryService.listarMembros().size() + " cadastrados",
            "#8b5cf6");

        // Card 5: Multas em Atraso
        int emprestimosAtrasados = libraryService.listarEmprestimosAtrasados().size();
        VBox card5 = createStatCard("⚠️", "Multas em Atraso", 
            String.valueOf(emprestimosAtrasados), 
            emprestimosAtrasados > 0 ? "Total: R$ " + String.format("%.2f", libraryService.getTotalMultas()) : "Nenhuma multa",
            "#ef4444");

        statsGrid.add(card1, 0, 0);
        statsGrid.add(card2, 1, 0);
        statsGrid.add(card3, 2, 0);
        statsGrid.add(card4, 0, 1);
        statsGrid.add(card5, 1, 1);

        // Configurar colunas
        for (int i = 0; i < 3; i++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setPercentWidth(33.33);
            statsGrid.getColumnConstraints().add(col);
        }

        content.getChildren().addAll(title, subtitle, statsGrid);
        return content;
    }

    private VBox createStatCard(String icon, String label, String value, String subtitle, String color) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(20));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);");
        card.setPrefHeight(120);

        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(header, Priority.ALWAYS);

        Label titleLabel = new Label(label);
        titleLabel.setFont(Font.font("System", 12));
        titleLabel.setTextFill(Color.web("#6b7280"));

        Label iconLabel = new Label(icon);
        iconLabel.setFont(Font.font(20));
        iconLabel.setTextFill(Color.web(color));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        header.getChildren().addAll(titleLabel, spacer, iconLabel);

        Label valueLabel = new Label(value);
        valueLabel.setFont(Font.font("System", FontWeight.BOLD, 32));
        valueLabel.setTextFill(Color.web("#111827"));

        Label subtitleLabel = new Label(subtitle);
        subtitleLabel.setFont(Font.font("System", 11));
        subtitleLabel.setTextFill(Color.web("#6b7280"));

        card.getChildren().addAll(header, valueLabel, subtitleLabel);
        return card;
    }
}

