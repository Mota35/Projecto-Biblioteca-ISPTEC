package isptec.biblioteca.views;

import javafx.animation.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * View "Sobre" com informações sobre a biblioteca e a aplicação
 */
public class SobreView {

    private final Stage stage;
    private final BorderPane mainLayout;

    public SobreView(Stage stage, BorderPane mainLayout) {
        this.stage = stage;
        this.mainLayout = mainLayout;
    }

    public void show() {
        ScrollPane scrollPane = new ScrollPane(createContent());
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: #f8fafc;");
        mainLayout.setCenter(scrollPane);
    }

    private VBox createContent() {
        VBox content = new VBox(40);
        content.setPadding(new Insets(0));
        content.setStyle("-fx-background-color: #f8fafc;");

        // Hero Section
        StackPane heroSection = createHeroSection();

        // Sobre a Biblioteca
        VBox aboutSection = createAboutSection();

        // Estatísticas
        HBox statsSection = createStatsSection();

        // Equipa
        VBox teamSection = createTeamSection();

        // Funcionalidades
        VBox featuresSection = createFeaturesSection();

        // Footer
        VBox footerSection = createFooterSection();

        content.getChildren().addAll(heroSection, aboutSection, statsSection, teamSection, featuresSection, footerSection);

        return content;
    }

    private StackPane createHeroSection() {
        StackPane hero = new StackPane();
        hero.setPrefHeight(350);

        // Fundo com gradiente
        Rectangle bg = new Rectangle();
        bg.widthProperty().bind(hero.widthProperty());
        bg.heightProperty().bind(hero.heightProperty());

        LinearGradient gradient = new LinearGradient(
            0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
            new Stop(0, Color.web("#1e1b4b")),
            new Stop(0.5, Color.web("#312e81")),
            new Stop(1, Color.web("#4c1d95"))
        );
        bg.setFill(gradient);

        // Círculos decorativos
        Circle c1 = new Circle(120, Color.web("#ffffff", 0.05));
        c1.setTranslateX(-300);
        c1.setTranslateY(-50);

        Circle c2 = new Circle(80, Color.web("#ffffff", 0.08));
        c2.setTranslateX(350);
        c2.setTranslateY(80);

        Circle c3 = new Circle(60, Color.web("#ffffff", 0.03));
        c3.setTranslateX(-200);
        c3.setTranslateY(100);

        // Conteúdo
        VBox heroContent = new VBox(20);
        heroContent.setAlignment(Pos.CENTER);

        Label icon = new Label("🏛️");
        icon.setStyle("-fx-font-size: 70px;");

        Label title = new Label("Biblioteca ISPTEC");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 48));
        title.setTextFill(Color.WHITE);

        Label subtitle = new Label("O conhecimento que transforma o futuro");
        subtitle.setFont(Font.font("Segoe UI", FontWeight.LIGHT, 20));
        subtitle.setTextFill(Color.web("#c4b5fd"));

        Label version = new Label("Versão 1.0.0 • Sistema de Gestão de Biblioteca");
        version.setFont(Font.font("Segoe UI", 14));
        version.setTextFill(Color.web("#a5b4fc"));
        version.setStyle("-fx-background-color: rgba(255,255,255,0.1); -fx-padding: 8 20; -fx-background-radius: 20;");

        heroContent.getChildren().addAll(icon, title, subtitle, version);

        hero.getChildren().addAll(bg, c1, c2, c3, heroContent);

        // Animações
        animateFloat(icon, 2000);

        return hero;
    }

    private void animateFloat(Label label, int duration) {
        TranslateTransition translate = new TranslateTransition(Duration.millis(duration), label);
        translate.setByY(-15);
        translate.setCycleCount(Animation.INDEFINITE);
        translate.setAutoReverse(true);
        translate.setInterpolator(Interpolator.EASE_BOTH);
        translate.play();
    }

    private VBox createAboutSection() {
        VBox section = new VBox(25);
        section.setPadding(new Insets(50, 80, 50, 80));
        section.setAlignment(Pos.CENTER);

        Label sectionTitle = new Label("Sobre a Biblioteca");
        sectionTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 32));
        sectionTitle.setTextFill(Color.web("#1e293b"));

        Label description = new Label(
            "A Biblioteca do ISPTEC é um centro de recursos de informação que apoia as atividades " +
            "académicas e de investigação da instituição. Com um acervo diversificado e moderno, " +
            "oferecemos aos nossos utilizadores acesso a milhares de obras em diversas áreas do conhecimento.\n\n" +
            "O nosso sistema de gestão digital permite reservas online, consulta do catálogo, " +
            "renovação de empréstimos e muito mais, tudo na palma da sua mão."
        );
        description.setFont(Font.font("Segoe UI", 16));
        description.setTextFill(Color.web("#475569"));
        description.setWrapText(true);
        description.setMaxWidth(800);
        description.setTextAlignment(TextAlignment.CENTER);
        description.setLineSpacing(6);

        section.getChildren().addAll(sectionTitle, description);

        return section;
    }

    private HBox createStatsSection() {
        HBox stats = new HBox(30);
        stats.setAlignment(Pos.CENTER);
        stats.setPadding(new Insets(40, 80, 40, 80));

        VBox stat1 = createStatCard("📚", "10,000+", "Livros no Acervo", "#6366f1");
        VBox stat2 = createStatCard("👥", "5,000+", "Membros Ativos", "#8b5cf6");
        VBox stat3 = createStatCard("📖", "50,000+", "Empréstimos/Ano", "#a855f7");
        VBox stat4 = createStatCard("🏆", "15+", "Anos de História", "#c026d3");

        stats.getChildren().addAll(stat1, stat2, stat3, stat4);

        return stats;
    }

    private VBox createStatCard(String icon, String value, String label, String color) {
        VBox card = new VBox(10);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(30));
        card.setPrefWidth(200);
        card.setStyle(
            "-fx-background-color: white; " +
            "-fx-background-radius: 20;"
        );

        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.web(color, 0.15));
        shadow.setRadius(20);
        shadow.setOffsetY(8);
        card.setEffect(shadow);

        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-font-size: 40px;");

        Label valueLabel = new Label(value);
        valueLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 32));
        valueLabel.setTextFill(Color.web(color));

        Label labelText = new Label(label);
        labelText.setFont(Font.font("Segoe UI", 14));
        labelText.setTextFill(Color.web("#64748b"));

        card.getChildren().addAll(iconLabel, valueLabel, labelText);

        // Hover effect
        card.setOnMouseEntered(e -> {
            ScaleTransition scale = new ScaleTransition(Duration.millis(200), card);
            scale.setToX(1.05);
            scale.setToY(1.05);
            scale.play();
        });

        card.setOnMouseExited(e -> {
            ScaleTransition scale = new ScaleTransition(Duration.millis(200), card);
            scale.setToX(1);
            scale.setToY(1);
            scale.play();
        });

        return card;
    }

    private VBox createTeamSection() {
        VBox section = new VBox(30);
        section.setPadding(new Insets(50, 80, 50, 80));
        section.setAlignment(Pos.CENTER);
        section.setStyle("-fx-background-color: #f1f5f9;");

        Label sectionTitle = new Label("Nossa Equipa");
        sectionTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 32));
        sectionTitle.setTextFill(Color.web("#1e293b"));

        Label sectionSubtitle = new Label("Os profissionais dedicados que fazem a biblioteca funcionar");
        sectionSubtitle.setFont(Font.font("Segoe UI", 16));
        sectionSubtitle.setTextFill(Color.web("#64748b"));

        HBox teamCards = new HBox(25);
        teamCards.setAlignment(Pos.CENTER);

        VBox member1 = createTeamCard("👨‍💼", "Dr. António Silva", "Diretor da Biblioteca", "#6366f1");
        VBox member2 = createTeamCard("👩‍💻", "Eng. Maria Santos", "Gestora de Sistemas", "#8b5cf6");
        VBox member3 = createTeamCard("👨‍🏫", "Prof. João Costa", "Curador do Acervo", "#a855f7");
        VBox member4 = createTeamCard("👩‍🔬", "Dra. Ana Fernandes", "Bibliotecária Chefe", "#c026d3");

        teamCards.getChildren().addAll(member1, member2, member3, member4);

        section.getChildren().addAll(sectionTitle, sectionSubtitle, teamCards);

        return section;
    }

    private VBox createTeamCard(String avatar, String name, String role, String color) {
        VBox card = new VBox(12);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(30));
        card.setPrefWidth(220);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 20;");

        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.web("#000000", 0.08));
        shadow.setRadius(15);
        shadow.setOffsetY(5);
        card.setEffect(shadow);

        // Avatar com gradiente
        StackPane avatarPane = new StackPane();
        Circle avatarBg = new Circle(45);
        avatarBg.setFill(Color.web(color, 0.1));

        Label avatarLabel = new Label(avatar);
        avatarLabel.setStyle("-fx-font-size: 40px;");

        avatarPane.getChildren().addAll(avatarBg, avatarLabel);

        Label nameLabel = new Label(name);
        nameLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        nameLabel.setTextFill(Color.web("#1e293b"));

        Label roleLabel = new Label(role);
        roleLabel.setFont(Font.font("Segoe UI", 13));
        roleLabel.setTextFill(Color.web("#64748b"));

        card.getChildren().addAll(avatarPane, nameLabel, roleLabel);

        return card;
    }

    private VBox createFeaturesSection() {
        VBox section = new VBox(30);
        section.setPadding(new Insets(50, 80, 50, 80));
        section.setAlignment(Pos.CENTER);

        Label sectionTitle = new Label("Funcionalidades do Sistema");
        sectionTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 32));
        sectionTitle.setTextFill(Color.web("#1e293b"));

        GridPane features = new GridPane();
        features.setHgap(25);
        features.setVgap(25);
        features.setAlignment(Pos.CENTER);

        VBox f1 = createFeatureCard("🔍", "Pesquisa Inteligente", "Encontre livros rapidamente com nossa pesquisa avançada");
        VBox f2 = createFeatureCard("📅", "Reservas Online", "Reserve livros de qualquer lugar, a qualquer hora");
        VBox f3 = createFeatureCard("📱", "Acesso Mobile", "Interface responsiva para todos os dispositivos");
        VBox f4 = createFeatureCard("💬", "Chatbot IA", "Assistente virtual para tirar suas dúvidas");
        VBox f5 = createFeatureCard("📊", "Relatórios", "Estatísticas detalhadas de uso e empréstimos");
        VBox f6 = createFeatureCard("🔔", "Notificações", "Alertas de vencimento e disponibilidade");

        features.add(f1, 0, 0);
        features.add(f2, 1, 0);
        features.add(f3, 2, 0);
        features.add(f4, 0, 1);
        features.add(f5, 1, 1);
        features.add(f6, 2, 1);

        section.getChildren().addAll(sectionTitle, features);

        return section;
    }

    private VBox createFeatureCard(String icon, String title, String description) {
        VBox card = new VBox(12);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(25));
        card.setPrefWidth(280);
        card.setStyle(
            "-fx-background-color: white; " +
            "-fx-background-radius: 16; " +
            "-fx-border-color: #e2e8f0; " +
            "-fx-border-radius: 16;"
        );

        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-font-size: 35px;");

        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        titleLabel.setTextFill(Color.web("#1e293b"));

        Label descLabel = new Label(description);
        descLabel.setFont(Font.font("Segoe UI", 13));
        descLabel.setTextFill(Color.web("#64748b"));
        descLabel.setWrapText(true);
        descLabel.setTextAlignment(TextAlignment.CENTER);

        card.getChildren().addAll(iconLabel, titleLabel, descLabel);

        card.setOnMouseEntered(e -> {
            card.setStyle(
                "-fx-background-color: white; " +
                "-fx-background-radius: 16; " +
                "-fx-border-color: #6366f1; " +
                "-fx-border-radius: 16; " +
                "-fx-border-width: 2;"
            );
        });

        card.setOnMouseExited(e -> {
            card.setStyle(
                "-fx-background-color: white; " +
                "-fx-background-radius: 16; " +
                "-fx-border-color: #e2e8f0; " +
                "-fx-border-radius: 16;"
            );
        });

        return card;
    }

    private VBox createFooterSection() {
        VBox footer = new VBox(20);
        footer.setAlignment(Pos.CENTER);
        footer.setPadding(new Insets(40));
        footer.setStyle("-fx-background-color: #1e1b4b;");

        Label copyright = new Label("© 2025 ISPTEC - Instituto Superior Politécnico de Tecnologias e Ciências");
        copyright.setFont(Font.font("Segoe UI", 14));
        copyright.setTextFill(Color.web("#a5b4fc"));

        Label address = new Label("📍 Luanda, Angola • 📞 +244 XXX XXX XXX • ✉️ biblioteca@isptec.co.ao");
        address.setFont(Font.font("Segoe UI", 13));
        address.setTextFill(Color.web("#818cf8"));

        HBox socialLinks = new HBox(15);
        socialLinks.setAlignment(Pos.CENTER);

        Button fb = createSocialButton("📘");
        Button tw = createSocialButton("🐦");
        Button ig = createSocialButton("📷");
        Button li = createSocialButton("💼");

        socialLinks.getChildren().addAll(fb, tw, ig, li);

        Label devCredit = new Label("Desenvolvido com ❤️ por Grupo 5 - Engenharia de Software I");
        devCredit.setFont(Font.font("Segoe UI", 12));
        devCredit.setTextFill(Color.web("#6366f1"));

        footer.getChildren().addAll(copyright, address, socialLinks, devCredit);

        return footer;
    }

    private Button createSocialButton(String icon) {
        Button btn = new Button(icon);
        btn.setStyle(
            "-fx-background-color: rgba(255,255,255,0.1); " +
            "-fx-font-size: 18; " +
            "-fx-min-width: 45; " +
            "-fx-min-height: 45; " +
            "-fx-background-radius: 25; " +
            "-fx-cursor: hand;"
        );
        btn.setOnMouseEntered(e -> btn.setStyle(
            "-fx-background-color: rgba(255,255,255,0.2); " +
            "-fx-font-size: 18; " +
            "-fx-min-width: 45; " +
            "-fx-min-height: 45; " +
            "-fx-background-radius: 25; " +
            "-fx-cursor: hand;"
        ));
        btn.setOnMouseExited(e -> btn.setStyle(
            "-fx-background-color: rgba(255,255,255,0.1); " +
            "-fx-font-size: 18; " +
            "-fx-min-width: 45; " +
            "-fx-min-height: 45; " +
            "-fx-background-radius: 25; " +
            "-fx-cursor: hand;"
        ));
        return btn;
    }
}

