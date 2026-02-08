package isptec.biblioteca.views;

import isptec.biblioteca.service.ChatbotService;
import isptec.biblioteca.service.impl.ChatbotServiceImpl;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CompletableFuture;

/**
 * View do Chatbot com integração de IA.
 * Oferece assistência virtual para usuários da biblioteca.
 */
public class ChatbotView {
    private final BorderPane mainLayout;
    private final ChatbotService chatbotService;
    private VBox messagesContainer;
    private ScrollPane scrollPane;
    private TextField inputField;
    private Button sendBtn;
    private boolean isProcessing = false;

    public ChatbotView(Stage stage, BorderPane mainLayout) {
        this.mainLayout = mainLayout;
        this.chatbotService = new ChatbotServiceImpl();
    }

    public void show() {
        mainLayout.setCenter(createContent());
    }

    private VBox createContent() {
        VBox content = new VBox(0);
        content.setStyle("-fx-background-color: #f0f4f8;");

        // Header
        HBox header = createHeader();

        // Chat area
        VBox chatArea = createChatArea();
        VBox.setVgrow(chatArea, Priority.ALWAYS);

        // Input area
        HBox inputArea = createInputArea();

        content.getChildren().addAll(header, chatArea, inputArea);
        return content;
    }

    private HBox createHeader() {
        HBox header = new HBox(15);
        header.setPadding(new Insets(20));
        header.setAlignment(Pos.CENTER_LEFT);
        header.setStyle("-fx-background-color: linear-gradient(to right, #2563eb, #7c3aed); -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 10, 0, 0, 2);");

        // Avatar do bot
        VBox avatarBox = new VBox();
        avatarBox.setAlignment(Pos.CENTER);
        Label avatar = new Label("🤖");
        avatar.setFont(Font.font(36));
        Label statusDot = new Label("●");
        statusDot.setStyle("-fx-text-fill: #22c55e; -fx-font-size: 10px;");
        avatarBox.getChildren().addAll(avatar, statusDot);

        // Info do bot
        VBox infoBox = new VBox(2);
        Label title = new Label("BiblioBot");
        title.setFont(Font.font("System", FontWeight.BOLD, 20));
        title.setTextFill(Color.WHITE);

        Label subtitle = new Label(chatbotService.isIADisponivel() ?
                "🟢 IA Ativa - Powered by AI" :
                "🟡 Modo Assistente Local");
        subtitle.setFont(Font.font("System", 12));
        subtitle.setTextFill(Color.web("#e0e7ff"));

        infoBox.getChildren().addAll(title, subtitle);
        HBox.setHgrow(infoBox, Priority.ALWAYS);

        // Botão de configuração de API
        Button configBtn = new Button("⚙️");
        configBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 18px; -fx-cursor: hand;");
        configBtn.setTooltip(new Tooltip("Configurar API de IA"));
        configBtn.setOnAction(e -> showApiConfigDialog());

        header.getChildren().addAll(avatarBox, infoBox, configBtn);
        return header;
    }

    private VBox createChatArea() {
        VBox chatArea = new VBox(10);
        chatArea.setPadding(new Insets(20));
        chatArea.setStyle("-fx-background-color: #f0f4f8;");

        messagesContainer = new VBox(15);
        messagesContainer.setPadding(new Insets(10));

        scrollPane = new ScrollPane(messagesContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: #f0f4f8; -fx-background-color: #f0f4f8; -fx-border-color: transparent;");
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        // Mensagem inicial
        addBotMessage("""
            Olá! 👋 Sou o BiblioBot, seu assistente virtual da Biblioteca ISPTEC.
            
            Posso ajudá-lo com:
            • 📚 Buscar e recomendar livros
            • 📊 Estatísticas da biblioteca
            • ⏰ Prazos e renovações
            • 📋 Políticas e regras
            • 💰 Informações sobre multas
            • 📝 Reservas de livros
            
            Como posso ajudá-lo hoje?
            """);

        // Sugestões rápidas
        HBox suggestionsBox = createSuggestionsBox();

        chatArea.getChildren().addAll(scrollPane, suggestionsBox);
        return chatArea;
    }

    private HBox createSuggestionsBox() {
        HBox box = new HBox(10);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(10, 0, 10, 0));

        String[][] suggestions = {
            {"📚", "Buscar livros"},
            {"💡", "Recomendações"},
            {"📊", "Estatísticas"},
            {"⏰", "Prazos"},
            {"📋", "Políticas"},
            {"❓", "Ajuda"}
        };

        for (String[] suggestion : suggestions) {
            Button btn = new Button(suggestion[0] + " " + suggestion[1]);
            btn.setStyle("-fx-background-color: white; -fx-text-fill: #2563eb; -fx-background-radius: 20; -fx-border-color: #2563eb; -fx-border-radius: 20; -fx-padding: 8 15; -fx-cursor: hand; -fx-font-size: 12px;");
            btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: #2563eb; -fx-text-fill: white; -fx-background-radius: 20; -fx-border-color: #2563eb; -fx-border-radius: 20; -fx-padding: 8 15; -fx-cursor: hand; -fx-font-size: 12px;"));
            btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: white; -fx-text-fill: #2563eb; -fx-background-radius: 20; -fx-border-color: #2563eb; -fx-border-radius: 20; -fx-padding: 8 15; -fx-cursor: hand; -fx-font-size: 12px;"));
            btn.setOnAction(e -> {
                inputField.setText(suggestion[1].toLowerCase());
                sendMessage();
            });
            box.getChildren().add(btn);
        }

        return box;
    }

    private HBox createInputArea() {
        HBox inputArea = new HBox(10);
        inputArea.setPadding(new Insets(15, 20, 20, 20));
        inputArea.setAlignment(Pos.CENTER);
        inputArea.setStyle("-fx-background-color: white; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, -2);");

        inputField = new TextField();
        inputField.setPromptText("Digite sua mensagem...");
        inputField.setPrefHeight(50);
        inputField.setStyle("-fx-font-size: 14px; -fx-background-radius: 25; -fx-border-radius: 25; -fx-border-color: #e5e7eb; -fx-padding: 0 20;");
        HBox.setHgrow(inputField, Priority.ALWAYS);
        inputField.setOnAction(e -> sendMessage());

        sendBtn = new Button("➤");
        sendBtn.setPrefSize(50, 50);
        sendBtn.setStyle("-fx-background-color: #2563eb; -fx-text-fill: white; -fx-font-size: 18px; -fx-background-radius: 25; -fx-cursor: hand;");
        sendBtn.setOnAction(e -> sendMessage());

        inputArea.getChildren().addAll(inputField, sendBtn);
        return inputArea;
    }

    private void sendMessage() {
        if (isProcessing) return;

        String message = inputField.getText().trim();
        if (message.isEmpty()) return;

        // Adiciona mensagem do usuário
        addUserMessage(message);
        inputField.clear();

        // Processa resposta de forma assíncrona
        isProcessing = true;
        sendBtn.setDisable(true);
        inputField.setDisable(true);

        // Adiciona indicador de "digitando"
        HBox typingIndicator = createTypingIndicator();
        messagesContainer.getChildren().add(typingIndicator);
        scrollToBottom();

        CompletableFuture.supplyAsync(() -> chatbotService.responderPergunta(message))
            .thenAcceptAsync(response -> Platform.runLater(() -> {
                messagesContainer.getChildren().remove(typingIndicator);
                addBotMessage(response);
                isProcessing = false;
                sendBtn.setDisable(false);
                inputField.setDisable(false);
                inputField.requestFocus();
            }))
            .exceptionally(ex -> {
                Platform.runLater(() -> {
                    messagesContainer.getChildren().remove(typingIndicator);
                    addBotMessage("❌ Desculpe, ocorreu um erro. Por favor, tente novamente.");
                    isProcessing = false;
                    sendBtn.setDisable(false);
                    inputField.setDisable(false);
                });
                return null;
            });
    }

    private void addUserMessage(String message) {
        HBox container = new HBox();
        container.setAlignment(Pos.CENTER_RIGHT);
        container.setPadding(new Insets(0, 0, 0, 50));

        VBox bubble = new VBox(5);
        bubble.setStyle("-fx-background-color: #2563eb; -fx-background-radius: 18 18 5 18; -fx-padding: 12 16;");
        bubble.setMaxWidth(400);

        Label messageLabel = new Label(message);
        messageLabel.setWrapText(true);
        messageLabel.setTextFill(Color.WHITE);
        messageLabel.setFont(Font.font("System", 14));

        Label timeLabel = new Label(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));
        timeLabel.setFont(Font.font("System", 10));
        timeLabel.setTextFill(Color.web("#bfdbfe"));

        bubble.getChildren().addAll(messageLabel, timeLabel);
        container.getChildren().add(bubble);
        messagesContainer.getChildren().add(container);
        scrollToBottom();
    }

    private void addBotMessage(String message) {
        HBox container = new HBox(10);
        container.setAlignment(Pos.CENTER_LEFT);
        container.setPadding(new Insets(0, 50, 0, 0));

        // Avatar do bot
        Label avatar = new Label("🤖");
        avatar.setFont(Font.font(24));
        avatar.setStyle("-fx-background-color: #e0e7ff; -fx-background-radius: 20; -fx-padding: 8;");

        VBox bubble = new VBox(5);
        bubble.setStyle("-fx-background-color: white; -fx-background-radius: 18 18 18 5; -fx-padding: 12 16; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 8, 0, 0, 2);");
        bubble.setMaxWidth(450);

        Label messageLabel = new Label(message);
        messageLabel.setWrapText(true);
        messageLabel.setTextFill(Color.web("#1f2937"));
        messageLabel.setFont(Font.font("System", 14));

        Label timeLabel = new Label(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));
        timeLabel.setFont(Font.font("System", 10));
        timeLabel.setTextFill(Color.web("#9ca3af"));

        bubble.getChildren().addAll(messageLabel, timeLabel);
        container.getChildren().addAll(avatar, bubble);
        messagesContainer.getChildren().add(container);
        scrollToBottom();
    }

    private HBox createTypingIndicator() {
        HBox container = new HBox(10);
        container.setAlignment(Pos.CENTER_LEFT);

        Label avatar = new Label("🤖");
        avatar.setFont(Font.font(24));
        avatar.setStyle("-fx-background-color: #e0e7ff; -fx-background-radius: 20; -fx-padding: 8;");

        HBox bubble = new HBox(5);
        bubble.setStyle("-fx-background-color: white; -fx-background-radius: 18; -fx-padding: 15 20;");

        for (int i = 0; i < 3; i++) {
            Label dot = new Label("●");
            dot.setStyle("-fx-text-fill: #9ca3af; -fx-font-size: 12px;");
            bubble.getChildren().add(dot);
        }

        Label typingLabel = new Label(" Digitando...");
        typingLabel.setStyle("-fx-text-fill: #9ca3af; -fx-font-size: 12px;");
        bubble.getChildren().add(typingLabel);

        container.getChildren().addAll(avatar, bubble);
        return container;
    }

    private void scrollToBottom() {
        Platform.runLater(() -> scrollPane.setVvalue(1.0));
    }

    private void showApiConfigDialog() {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Configurar IA");
        dialog.setHeaderText("Configure a API de Inteligência Artificial");

        ButtonType saveBtn = new ButtonType("Salvar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveBtn, ButtonType.CANCEL);

        VBox content = new VBox(15);
        content.setPadding(new Insets(20));

        Label infoLabel = new Label("Para usar IA avançada, insira sua chave de API OpenAI.\nSem a chave, o chatbot funcionará em modo local.");
        infoLabel.setWrapText(true);
        infoLabel.setStyle("-fx-text-fill: #6b7280;");

        TextField apiKeyField = new PasswordField();
        apiKeyField.setPromptText("sk-...");
        apiKeyField.setPrefWidth(350);

        Label statusLabel = new Label(chatbotService.isIADisponivel() ?
                "✅ IA configurada e ativa" :
                "⚠️ IA não configurada - Modo local ativo");
        statusLabel.setStyle(chatbotService.isIADisponivel() ?
                "-fx-text-fill: #22c55e;" :
                "-fx-text-fill: #f59e0b;");

        content.getChildren().addAll(
            new Label("Chave da API OpenAI:"),
            apiKeyField,
            statusLabel,
            infoLabel
        );

        dialog.getDialogPane().setContent(content);

        dialog.setResultConverter(button -> {
            if (button == saveBtn) {
                return apiKeyField.getText().trim();
            }
            return null;
        });

        dialog.showAndWait().ifPresent(apiKey -> {
            if (!apiKey.isEmpty()) {
                chatbotService.setApiKey(apiKey);
                addBotMessage("✅ API configurada! Agora estou usando IA avançada para responder suas perguntas.");
            }
        });
    }
}
