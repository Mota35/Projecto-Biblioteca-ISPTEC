package isptec.biblioteca.views;

import javafx.animation.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * View de Notificações com design moderno e aesthetic
 */
public class NotificacoesView {

    private final Stage stage;
    private final BorderPane mainLayout;
    private final List<Notificacao> notificacoes;

    public NotificacoesView(Stage stage, BorderPane mainLayout) {
        this.stage = stage;
        this.mainLayout = mainLayout;
        this.notificacoes = carregarNotificacoesMock();
    }

    public void show() {
        ScrollPane scrollPane = new ScrollPane(createContent());
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: #f8fafc;");
        mainLayout.setCenter(scrollPane);
    }

    private VBox createContent() {
        VBox content = new VBox(25);
        content.setPadding(new Insets(30));
        content.setStyle("-fx-background-color: #f8fafc;");

        // Header
        HBox header = createHeader();

        // Filtros
        HBox filters = createFilters();

        // Lista de notificações
        VBox notificationsList = createNotificationsList();

        content.getChildren().addAll(header, filters, notificationsList);

        return content;
    }

    private HBox createHeader() {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);

        VBox titleBox = new VBox(5);
        Label title = new Label("🔔 Notificações");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 32));
        title.setTextFill(Color.web("#1e293b"));

        Label subtitle = new Label("Mantenha-se atualizado sobre suas atividades na biblioteca");
        subtitle.setFont(Font.font("Segoe UI", 14));
        subtitle.setTextFill(Color.web("#64748b"));

        titleBox.getChildren().addAll(title, subtitle);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Badge de não lidas
        long naoLidas = notificacoes.stream().filter(n -> !n.isLida()).count();

        Button marcarTodasBtn = new Button("✓ Marcar todas como lidas");
        marcarTodasBtn.setStyle(
            "-fx-background-color: white; " +
            "-fx-text-fill: #6366f1; " +
            "-fx-font-size: 13; " +
            "-fx-padding: 10 20; " +
            "-fx-background-radius: 8; " +
            "-fx-border-color: #6366f1; " +
            "-fx-border-radius: 8; " +
            "-fx-cursor: hand;"
        );
        marcarTodasBtn.setOnMouseEntered(e -> marcarTodasBtn.setStyle(
            "-fx-background-color: #6366f1; " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 13; " +
            "-fx-padding: 10 20; " +
            "-fx-background-radius: 8; " +
            "-fx-border-color: #6366f1; " +
            "-fx-border-radius: 8; " +
            "-fx-cursor: hand;"
        ));
        marcarTodasBtn.setOnMouseExited(e -> marcarTodasBtn.setStyle(
            "-fx-background-color: white; " +
            "-fx-text-fill: #6366f1; " +
            "-fx-font-size: 13; " +
            "-fx-padding: 10 20; " +
            "-fx-background-radius: 8; " +
            "-fx-border-color: #6366f1; " +
            "-fx-border-radius: 8; " +
            "-fx-cursor: hand;"
        ));

        Label badgeLabel = new Label(String.valueOf(naoLidas));
        badgeLabel.setStyle(
            "-fx-background-color: #ef4444; " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 12; " +
            "-fx-font-weight: bold; " +
            "-fx-padding: 4 10; " +
            "-fx-background-radius: 12;"
        );

        HBox badgeBox = new HBox(10);
        badgeBox.setAlignment(Pos.CENTER);
        badgeBox.getChildren().addAll(badgeLabel, marcarTodasBtn);

        header.getChildren().addAll(titleBox, spacer, badgeBox);

        return header;
    }

    private HBox createFilters() {
        HBox filters = new HBox(10);
        filters.setAlignment(Pos.CENTER_LEFT);
        filters.setPadding(new Insets(10, 0, 10, 0));

        ToggleGroup filterGroup = new ToggleGroup();

        ToggleButton allBtn = createFilterButton("Todas", true);
        allBtn.setToggleGroup(filterGroup);
        allBtn.setSelected(true);

        ToggleButton unreadBtn = createFilterButton("Não lidas", false);
        unreadBtn.setToggleGroup(filterGroup);

        ToggleButton emprestimosBtn = createFilterButton("📚 Empréstimos", false);
        emprestimosBtn.setToggleGroup(filterGroup);

        ToggleButton reservasBtn = createFilterButton("📅 Reservas", false);
        reservasBtn.setToggleGroup(filterGroup);

        ToggleButton sistemaBtn = createFilterButton("⚙️ Sistema", false);
        sistemaBtn.setToggleGroup(filterGroup);

        filters.getChildren().addAll(allBtn, unreadBtn, emprestimosBtn, reservasBtn, sistemaBtn);

        return filters;
    }

    private ToggleButton createFilterButton(String text, boolean selected) {
        ToggleButton btn = new ToggleButton(text);
        btn.setStyle(
            "-fx-background-color: " + (selected ? "#6366f1" : "white") + "; " +
            "-fx-text-fill: " + (selected ? "white" : "#64748b") + "; " +
            "-fx-font-size: 13; " +
            "-fx-padding: 8 16; " +
            "-fx-background-radius: 20; " +
            "-fx-border-color: " + (selected ? "#6366f1" : "#e2e8f0") + "; " +
            "-fx-border-radius: 20; " +
            "-fx-cursor: hand;"
        );

        btn.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                btn.setStyle(
                    "-fx-background-color: #6366f1; " +
                    "-fx-text-fill: white; " +
                    "-fx-font-size: 13; " +
                    "-fx-padding: 8 16; " +
                    "-fx-background-radius: 20; " +
                    "-fx-border-color: #6366f1; " +
                    "-fx-border-radius: 20; " +
                    "-fx-cursor: hand;"
                );
            } else {
                btn.setStyle(
                    "-fx-background-color: white; " +
                    "-fx-text-fill: #64748b; " +
                    "-fx-font-size: 13; " +
                    "-fx-padding: 8 16; " +
                    "-fx-background-radius: 20; " +
                    "-fx-border-color: #e2e8f0; " +
                    "-fx-border-radius: 20; " +
                    "-fx-cursor: hand;"
                );
            }
        });

        return btn;
    }

    private VBox createNotificationsList() {
        VBox list = new VBox(12);

        for (int i = 0; i < notificacoes.size(); i++) {
            Notificacao notif = notificacoes.get(i);
            HBox card = createNotificationCard(notif, i);
            list.getChildren().add(card);
        }

        return list;
    }

    private HBox createNotificationCard(Notificacao notif, int index) {
        HBox card = new HBox(15);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(20));
        card.setStyle(
            "-fx-background-color: " + (notif.isLida() ? "white" : "#f0f9ff") + "; " +
            "-fx-background-radius: 16; " +
            (notif.isLida() ? "" : "-fx-border-color: #6366f1; -fx-border-width: 0 0 0 4; ") +
            "-fx-border-radius: 16;"
        );

        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.web("#000000", 0.05));
        shadow.setRadius(10);
        shadow.setOffsetY(4);
        card.setEffect(shadow);

        // Ícone
        StackPane iconPane = new StackPane();
        iconPane.setMinSize(50, 50);
        iconPane.setMaxSize(50, 50);
        iconPane.setStyle(
            "-fx-background-color: " + getNotifColor(notif.getTipo(), 0.1) + "; " +
            "-fx-background-radius: 25;"
        );

        Label iconLabel = new Label(getNotifIcon(notif.getTipo()));
        iconLabel.setStyle("-fx-font-size: 22px;");
        iconPane.getChildren().add(iconLabel);

        // Conteúdo
        VBox contentBox = new VBox(5);
        HBox.setHgrow(contentBox, Priority.ALWAYS);

        HBox titleRow = new HBox(10);
        titleRow.setAlignment(Pos.CENTER_LEFT);

        Label titleLabel = new Label(notif.getTitulo());
        titleLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 15));
        titleLabel.setTextFill(Color.web("#1e293b"));

        if (!notif.isLida()) {
            Label newBadge = new Label("NOVO");
            newBadge.setStyle(
                "-fx-background-color: #6366f1; " +
                "-fx-text-fill: white; " +
                "-fx-font-size: 10; " +
                "-fx-font-weight: bold; " +
                "-fx-padding: 2 8; " +
                "-fx-background-radius: 10;"
            );
            titleRow.getChildren().addAll(titleLabel, newBadge);
        } else {
            titleRow.getChildren().add(titleLabel);
        }

        Label messageLabel = new Label(notif.getMensagem());
        messageLabel.setFont(Font.font("Segoe UI", 13));
        messageLabel.setTextFill(Color.web("#64748b"));
        messageLabel.setWrapText(true);

        Label timeLabel = new Label(formatTime(notif.getDataHora()));
        timeLabel.setFont(Font.font("Segoe UI", 12));
        timeLabel.setTextFill(Color.web("#94a3b8"));

        contentBox.getChildren().addAll(titleRow, messageLabel, timeLabel);

        // Ações
        VBox actionsBox = new VBox(8);
        actionsBox.setAlignment(Pos.CENTER_RIGHT);

        Button actionBtn = new Button("Ver detalhes →");
        actionBtn.setStyle(
            "-fx-background-color: transparent; " +
            "-fx-text-fill: #6366f1; " +
            "-fx-font-size: 12; " +
            "-fx-cursor: hand;"
        );
        actionBtn.setOnMouseEntered(e -> actionBtn.setStyle(
            "-fx-background-color: #ede9fe; " +
            "-fx-text-fill: #6366f1; " +
            "-fx-font-size: 12; " +
            "-fx-background-radius: 5; " +
            "-fx-cursor: hand;"
        ));
        actionBtn.setOnMouseExited(e -> actionBtn.setStyle(
            "-fx-background-color: transparent; " +
            "-fx-text-fill: #6366f1; " +
            "-fx-font-size: 12; " +
            "-fx-cursor: hand;"
        ));

        actionsBox.getChildren().add(actionBtn);

        card.getChildren().addAll(iconPane, contentBox, actionsBox);

        // Hover effect
        card.setOnMouseEntered(e -> {
            card.setStyle(
                "-fx-background-color: " + (notif.isLida() ? "#f8fafc" : "#e0f2fe") + "; " +
                "-fx-background-radius: 16; " +
                (notif.isLida() ? "" : "-fx-border-color: #6366f1; -fx-border-width: 0 0 0 4; ") +
                "-fx-border-radius: 16;"
            );
        });

        card.setOnMouseExited(e -> {
            card.setStyle(
                "-fx-background-color: " + (notif.isLida() ? "white" : "#f0f9ff") + "; " +
                "-fx-background-radius: 16; " +
                (notif.isLida() ? "" : "-fx-border-color: #6366f1; -fx-border-width: 0 0 0 4; ") +
                "-fx-border-radius: 16;"
            );
        });

        // Animação de entrada
        card.setOpacity(0);
        card.setTranslateY(20);

        Timeline timeline = new Timeline(
            new KeyFrame(Duration.millis(index * 100), e -> {
                FadeTransition fade = new FadeTransition(Duration.millis(300), card);
                fade.setFromValue(0);
                fade.setToValue(1);

                TranslateTransition translate = new TranslateTransition(Duration.millis(300), card);
                translate.setFromY(20);
                translate.setToY(0);

                ParallelTransition parallel = new ParallelTransition(fade, translate);
                parallel.play();
            })
        );
        timeline.play();

        return card;
    }

    private String getNotifIcon(String tipo) {
        switch (tipo) {
            case "emprestimo": return "📚";
            case "reserva": return "📅";
            case "vencimento": return "⏰";
            case "multa": return "💰";
            case "sistema": return "⚙️";
            case "sucesso": return "✅";
            default: return "🔔";
        }
    }

    private String getNotifColor(String tipo, double opacity) {
        String color;
        switch (tipo) {
            case "emprestimo": color = "#6366f1"; break;
            case "reserva": color = "#8b5cf6"; break;
            case "vencimento": color = "#f59e0b"; break;
            case "multa": color = "#ef4444"; break;
            case "sistema": color = "#64748b"; break;
            case "sucesso": color = "#22c55e"; break;
            default: color = "#6366f1";
        }
        return color.replace("#", "rgba(") + "," + opacity + ")";
    }

    private String formatTime(LocalDateTime dateTime) {
        LocalDateTime now = LocalDateTime.now();
        long minutes = java.time.Duration.between(dateTime, now).toMinutes();

        if (minutes < 1) return "Agora mesmo";
        if (minutes < 60) return "Há " + minutes + " minuto" + (minutes > 1 ? "s" : "");

        long hours = minutes / 60;
        if (hours < 24) return "Há " + hours + " hora" + (hours > 1 ? "s" : "");

        long days = hours / 24;
        if (days < 7) return "Há " + days + " dia" + (days > 1 ? "s" : "");

        return dateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }

    private List<Notificacao> carregarNotificacoesMock() {
        List<Notificacao> lista = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        lista.add(new Notificacao(
            "Empréstimo realizado com sucesso",
            "O livro 'Clean Code' foi emprestado. Data de devolução: " + now.plusDays(14).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
            "emprestimo",
            now.minusMinutes(5),
            false
        ));

        lista.add(new Notificacao(
            "Reserva confirmada",
            "A sua reserva do livro 'Design Patterns' foi confirmada. Retire em até 3 dias.",
            "reserva",
            now.minusHours(2),
            false
        ));

        lista.add(new Notificacao(
            "Lembrete de devolução",
            "O livro 'Estruturas de Dados' deve ser devolvido em 2 dias.",
            "vencimento",
            now.minusHours(6),
            false
        ));

        lista.add(new Notificacao(
            "Novo livro disponível",
            "O livro 'Java: The Complete Reference' que você reservou já está disponível!",
            "sucesso",
            now.minusDays(1),
            true
        ));

        lista.add(new Notificacao(
            "Multa pendente",
            "Você tem uma multa de 500 KZ pendente por atraso na devolução.",
            "multa",
            now.minusDays(2),
            true
        ));

        lista.add(new Notificacao(
            "Manutenção programada",
            "O sistema estará em manutenção no próximo sábado das 00:00 às 06:00.",
            "sistema",
            now.minusDays(3),
            true
        ));

        return lista;
    }

    // Classe interna para representar notificação
    private static class Notificacao {
        private final String titulo;
        private final String mensagem;
        private final String tipo;
        private final LocalDateTime dataHora;
        private boolean lida;

        public Notificacao(String titulo, String mensagem, String tipo, LocalDateTime dataHora, boolean lida) {
            this.titulo = titulo;
            this.mensagem = mensagem;
            this.tipo = tipo;
            this.dataHora = dataHora;
            this.lida = lida;
        }

        public String getTitulo() { return titulo; }
        public String getMensagem() { return mensagem; }
        public String getTipo() { return tipo; }
        public LocalDateTime getDataHora() { return dataHora; }
        public boolean isLida() { return lida; }
    }
}

