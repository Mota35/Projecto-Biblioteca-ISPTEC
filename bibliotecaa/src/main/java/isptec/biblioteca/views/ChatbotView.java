package isptec.biblioteca.views;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class ChatbotView {
    private Stage stage;
    private BorderPane mainLayout;
    private TextArea chatArea;

    public ChatbotView(Stage stage, BorderPane mainLayout) {
        this.stage = stage;
        this.mainLayout = mainLayout;
    }

    public void show() {
        mainLayout.setCenter(createContent());
    }

    private VBox createContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));
        content.setStyle("-fx-background-color: #f9fafb;");

        Label title = new Label("💬 Chatbot - Assistente da Biblioteca");
        title.setFont(Font.font("System", FontWeight.BOLD, 28));

        VBox chatBox = new VBox(10);
        chatBox.setStyle("-fx-background-color: white; -fx-padding: 20; -fx-background-radius: 10;");

        chatArea = new TextArea();
        chatArea.setEditable(false);
        chatArea.setWrapText(true);
        chatArea.setPrefHeight(400);
        chatArea.setText("Olá! Como posso ajudá-lo hoje?\n\nPergunte sobre:\n- Disponibilidade de livros\n- Prazos de devolução\n- Como fazer reservas\n- Políticas da biblioteca");

        HBox inputBox = new HBox(10);
        TextField inputField = new TextField();
        inputField.setPromptText("Digite sua pergunta...");
        inputField.setPrefHeight(40);
        HBox.setHgrow(inputField, Priority.ALWAYS);

        Button sendBtn = new Button("Enviar");
        sendBtn.setPrefHeight(40);
        sendBtn.setStyle("-fx-background-color: #2563eb; -fx-text-fill: white;");
        sendBtn.setOnAction(e -> {
            String msg = inputField.getText();
            if (!msg.isEmpty()) {
                chatArea.appendText("\n\nVocê: " + msg);
                chatArea.appendText("\n\nBot: Desculpe, esta é uma versão demo. A funcionalidade completa do chatbot está em desenvolvimento.");
                inputField.clear();
            }
        });

        inputField.setOnAction(e -> sendBtn.fire());

        inputBox.getChildren().addAll(inputField, sendBtn);
        chatBox.getChildren().addAll(chatArea, inputBox);

        content.getChildren().addAll(title, chatBox);
        VBox.setVgrow(chatBox, Priority.ALWAYS);
        return content;
    }
}

