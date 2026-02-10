package isptec.biblioteca.views;

import isptec.biblioteca.ServiceFactory;
import isptec.biblioteca.model.Emprestimo;
import isptec.biblioteca.model.Reserva;
import isptec.biblioteca.service.AuthService;
import isptec.biblioteca.service.LibraryService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.List;
import java.util.Objects;

public class DashboardUserView {
    private final Stage stage;
    private final AuthService authService;
    private final LibraryService libraryService;
    private final BorderPane mainLayout;

    public DashboardUserView(Stage stage) {
        this.stage = stage;
        this.authService = ServiceFactory.getInstance().getAuthService();
        this.libraryService = LibraryService.getInstance();
        this.mainLayout = new BorderPane();
    }

    public void show() {
        VBox sidebar = createSidebar();
        mainLayout.setLeft(sidebar);
        mainLayout.setCenter(createDashboardContent());

        Scene scene = new Scene(mainLayout, 1200, 700);
        stage.setScene(scene);
        stage.setTitle("Meu Painel - Biblioteca ISPTEC");
        stage.show();
    }

    private VBox createSidebar() {
        VBox sidebar = new VBox(10);
        sidebar.setPrefWidth(250);
        sidebar.setStyle("-fx-background-color: white; -fx-border-color: #e5e7eb; -fx-border-width: 0 1 0 0;");
        sidebar.setPadding(new Insets(20));


        HBox logoBox = new HBox(10);
        logoBox.setAlignment(Pos.CENTER_LEFT);
        ImageView logoImage = new ImageView(
        new Image(Objects.requireNonNull(getClass().getResourceAsStream("/imagens/logo_2.png")))
        );

        logoImage.setFitWidth(34);   // ajusta se quiseres maior/menor
        logoImage.setFitHeight(34);
        logoImage.setPreserveRatio(true);
        
        VBox logoText = new VBox(2);
        Label titleLabel = new Label("Biblioteca ISPTEC");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        Label subtitleLabel = new Label("Estudante");
        subtitleLabel.setFont(Font.font("System", 10));
        subtitleLabel.setTextFill(Color.web("#6b7280"));
        logoText.getChildren().addAll(titleLabel, subtitleLabel);
        
        logoBox.getChildren().addAll(logoImage, logoText);

        Separator separator = new Separator();
        separator.setPadding(new Insets(10, 0, 10, 0));

        VBox menuBox = new VBox(5);
        
        Button dashboardBtn = createMenuButton("📊 Dashboard", true);
        dashboardBtn.setOnAction(e -> mainLayout.setCenter(createDashboardContent()));
        
        Button catalogoBtn = createMenuButton("🔍 Consultar Livros", false);
        catalogoBtn.setOnAction(e -> new CatalogoUserView(stage, mainLayout).show());
        
        Button reservasBtn = createMenuButton("⏰ Minhas Reservas", false);
        reservasBtn.setOnAction(e -> new MinhasReservasView(stage, mainLayout).show());
        
        Button chatbotBtn = createMenuButton("💬 Chatbot", false);
        chatbotBtn.setOnAction(e -> new ChatbotView(stage, mainLayout).show());
        
        Button notificacoesBtn = createMenuButton("🔔 Notificações", false);
        notificacoesBtn.setOnAction(e -> new NotificacoesView(stage, mainLayout).show());

        Button perfilBtn = createMenuButton("👤 Perfil", false);
        perfilBtn.setOnAction(e -> new PerfilUserView(stage, mainLayout).show());

        Button configBtn = createMenuButton("⚙️ Configurações", false);
        configBtn.setOnAction(e -> new ConfiguracoesView(stage, mainLayout).show());

        Button sobreBtn = createMenuButton("ℹ️ Sobre", false);
        sobreBtn.setOnAction(e -> new SobreView(stage, mainLayout).show());

        menuBox.getChildren().addAll(dashboardBtn, catalogoBtn, reservasBtn, chatbotBtn, notificacoesBtn, perfilBtn, configBtn, sobreBtn);

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

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
            btn.setStyle("-fx-background-color: #2563eb; -fx-text-fill: white; -fx-font-size: 14; -fx-background-radius: 5; -fx-cursor: hand;");
        } else {
            btn.setStyle("-fx-background-color: transparent; -fx-text-fill: #374151; -fx-font-size: 14; -fx-cursor: hand;");
            btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: #f3f4f6; -fx-text-fill: #374151; -fx-font-size: 14; -fx-background-radius: 5; -fx-cursor: hand;"));
            btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: transparent; -fx-text-fill: #374151; -fx-font-size: 14; -fx-cursor: hand;"));
        }
        
        return btn;
    }

    private ScrollPane createDashboardContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));
        content.setStyle("-fx-background-color: #f9fafb;");

        Label title = new Label("Meu Painel");
        title.setFont(Font.font("System", FontWeight.BOLD, 28));

        Label subtitle = new Label("Bem-vindo, " + authService.getUsuarioLogado().getNome() + "!");
        subtitle.setFont(Font.font("System", 14));
        subtitle.setTextFill(Color.web("#6b7280"));

        // Estatísticas do usuário
        String userId = String.valueOf(authService.getUsuarioLogado().getId());
        List<Emprestimo> meusEmprestimos = libraryService.listarEmprestimosPorMembro(userId);
        List<Reserva> minhasReservas = libraryService.listarReservasPorMembro(userId);
        int reservasPendentes = (int) minhasReservas.stream().filter(Reserva::isPendente).count();
        
        double totalMultas = meusEmprestimos.stream()
            .peek(Emprestimo::calcularMulta)
            .mapToDouble(Emprestimo::getMulta)
            .sum();

        GridPane statsGrid = new GridPane();
        statsGrid.setHgap(15);
        statsGrid.setVgap(15);

        VBox card1 = createStatCard("📚", "Livros Emprestados", 
            String.valueOf(meusEmprestimos.size()), 
            "Seus empréstimos ativos", "#2563eb");

        VBox card2 = createStatCard("⏰", "Reservas Ativas", 
            String.valueOf(reservasPendentes), 
            "Livros reservados", "#f59e0b");

        String multaColor = totalMultas > 0 ? "#ef4444" : "#6b7280";
        VBox card3 = createStatCard("💰", "Multas", 
            String.format("R$ %.2f", totalMultas), 
            totalMultas > 0 ? "Pendente de pagamento" : "Nenhuma multa", multaColor);

        statsGrid.add(card1, 0, 0);
        statsGrid.add(card2, 1, 0);
        statsGrid.add(card3, 2, 0);

        for (int i = 0; i < 3; i++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setPercentWidth(33.33);
            statsGrid.getColumnConstraints().add(col);
        }

        // Meus Empréstimos
        VBox emprestimosBox = createEmprestimosSection(meusEmprestimos);

        // Dicas
        VBox dicasBox = new VBox(10);
        dicasBox.setPadding(new Insets(20));
        dicasBox.setStyle("-fx-background-color: #eff6ff; -fx-background-radius: 10; -fx-border-color: #bfdbfe; -fx-border-radius: 10;");
        
        Label dicasTitle = new Label("💡 Dicas");
        dicasTitle.setFont(Font.font("System", FontWeight.BOLD, 14));
        
        VBox dicasList = new VBox(5);
        dicasList.getChildren().addAll(
            new Label("• Você pode renovar cada empréstimo até 2 vezes"),
            new Label("• Não é possível renovar se houver reservas pendentes para o livro"),
            new Label("• Devoluções em atraso geram multa de R$ 2,00 por dia"),
            new Label("• Use o Chatbot para tirar dúvidas ou encontrar livros")
        );
        
        dicasBox.getChildren().addAll(dicasTitle, dicasList);

        content.getChildren().addAll(title, subtitle, statsGrid, emprestimosBox, dicasBox);
        
        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: #f9fafb;");
        return scrollPane;
    }

    private VBox createEmprestimosSection(List<Emprestimo> emprestimos) {
        VBox section = new VBox(15);
        section.setPadding(new Insets(20));
        section.setStyle("-fx-background-color: white; -fx-background-radius: 10;");

        Label sectionTitle = new Label("Meus Empréstimos");
        sectionTitle.setFont(Font.font("System", FontWeight.BOLD, 18));

        if (emprestimos.isEmpty()) {
            VBox emptyState = new VBox(10);
            emptyState.setAlignment(Pos.CENTER);
            emptyState.setPadding(new Insets(30));
            
            Label emptyIcon = new Label("📚");
            emptyIcon.setFont(Font.font(40));
            Label emptyText = new Label("Você não possui empréstimos ativos");
            emptyText.setTextFill(Color.web("#6b7280"));
            Label emptySubtext = new Label("Visite a seção \"Consultar Livros\" para encontrar seu próximo livro");
            emptySubtext.setFont(Font.font("System", 11));
            emptySubtext.setTextFill(Color.web("#9ca3af"));
            
            emptyState.getChildren().addAll(emptyIcon, emptyText, emptySubtext);
            section.getChildren().addAll(sectionTitle, emptyState);
        } else {
            VBox emprestimosList = new VBox(10);
            
            for (Emprestimo emp : emprestimos) {
                VBox empBox = createEmprestimoCard(emp);
                emprestimosList.getChildren().add(empBox);
            }
            
            section.getChildren().addAll(sectionTitle, emprestimosList);
        }

        return section;
    }

    private VBox createEmprestimoCard(Emprestimo emprestimo) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(15));
        card.setStyle("-fx-background-color: #f9fafb; -fx-background-radius: 5; -fx-border-color: #e5e7eb; -fx-border-radius: 5;");

        Label titulo = new Label(emprestimo.getTituloLivro());
        titulo.setFont(Font.font("System", FontWeight.BOLD, 14));

        Label dataEmprestimo = new Label("📅 Emprestado em: " + emprestimo.getDataEmprestimo());
        dataEmprestimo.setFont(Font.font("System", 11));

        Label dataDevolucao = new Label("📅 Devolução até: " + emprestimo.getDataDevolucaoPrevista());
        dataDevolucao.setFont(Font.font("System", 11));
        if (emprestimo.isAtrasado()) {
            dataDevolucao.setTextFill(Color.RED);
        }

        HBox statusBox = new HBox(10);
        
        if (emprestimo.isAtrasado()) {
            Label atrasoLabel = new Label("⚠️ Atrasado " + emprestimo.getDiasAtraso() + " dias");
            atrasoLabel.setStyle("-fx-background-color: #fee2e2; -fx-padding: 5 10; -fx-background-radius: 3; -fx-text-fill: #991b1b;");
            statusBox.getChildren().add(atrasoLabel);
        }

        Label renovacoesLabel = new Label("Renovações: " + emprestimo.getNumeroRenovacoes() + "/2");
        renovacoesLabel.setStyle("-fx-background-color: #e5e7eb; -fx-padding: 5 10; -fx-background-radius: 3;");
        statusBox.getChildren().add(renovacoesLabel);

        if (emprestimo.getMulta() > 0) {
            Label multaLabel = new Label(String.format("Multa: R$ %.2f", emprestimo.getMulta()));
            multaLabel.setStyle("-fx-background-color: #fee2e2; -fx-padding: 5 10; -fx-background-radius: 3; -fx-text-fill: #991b1b;");
            statusBox.getChildren().add(multaLabel);
        }

        Button renovarBtn = new Button("Renovar Empréstimo");
        renovarBtn.setStyle("-fx-background-color: #2563eb; -fx-text-fill: white; -fx-cursor: hand;");
        renovarBtn.setDisable(emprestimo.getNumeroRenovacoes() >= 2);
        renovarBtn.setOnAction(e -> {
            boolean renovado = libraryService.renovarEmprestimo(emprestimo.getId());
            if (renovado) {
                showAlert("Sucesso", "Empréstimo renovado com sucesso!", Alert.AlertType.INFORMATION);
                mainLayout.setCenter(createDashboardContent());
            } else {
                showAlert("Erro", "Não foi possível renovar. Verifique se há reservas pendentes.", Alert.AlertType.ERROR);
            }
        });

        card.getChildren().addAll(titulo, dataEmprestimo, dataDevolucao, statusBox, renovarBtn);
        return card;
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
        valueLabel.setTextFill(Color.web(color));

        Label subtitleLabel = new Label(subtitle);
        subtitleLabel.setFont(Font.font("System", 11));
        subtitleLabel.setTextFill(Color.web("#6b7280"));

        card.getChildren().addAll(header, valueLabel, subtitleLabel);
        return card;
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
