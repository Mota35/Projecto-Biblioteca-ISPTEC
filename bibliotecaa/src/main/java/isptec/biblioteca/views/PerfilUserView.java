package isptec.biblioteca.views;

import isptec.biblioteca.ServiceFactory;
import isptec.biblioteca.model.Emprestimo;
import isptec.biblioteca.model.Reserva;
import isptec.biblioteca.service.AuthService;
import isptec.biblioteca.service.LibraryService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.List;

public class PerfilUserView {
    private final BorderPane mainLayout;
    private final AuthService authService;
    private final LibraryService libraryService;

    public PerfilUserView(Stage stage, BorderPane mainLayout) {
        this.mainLayout = mainLayout;
        this.authService = ServiceFactory.getInstance().getAuthService();
        this.libraryService = LibraryService.getInstance();
    }

    public void show() {
        mainLayout.setCenter(createContent());
    }

    private ScrollPane createContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));
        content.setStyle("-fx-background-color: #f9fafb;");
        content.setMaxWidth(700);

        Label title = new Label("👤 Meu Perfil");
        title.setFont(Font.font("System", FontWeight.BOLD, 28));

        // Informações Pessoais
        VBox infoBox = new VBox(15);
        infoBox.setPadding(new Insets(20));
        infoBox.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-border-color: #e5e7eb; -fx-border-radius: 10;");

        Label infoTitle = new Label("📋 Informações Pessoais");
        infoTitle.setFont(Font.font("System", FontWeight.BOLD, 16));

        GridPane infoGrid = new GridPane();
        infoGrid.setHgap(20);
        infoGrid.setVgap(10);

        infoGrid.add(createInfoField("👤", "Nome", authService.getUsuarioLogado().getNome()), 0, 0);
        infoGrid.add(createInfoField("📧", "Email", authService.getUsuarioLogado().getEmail()), 1, 0);
        infoGrid.add(createInfoField("🎓", "Perfil", authService.getUsuarioLogado().getPerfil().toString()), 0, 1);

        infoBox.getChildren().addAll(infoTitle, infoGrid);

        // Estatísticas do Usuário
        String userId = String.valueOf(authService.getUsuarioLogado().getId());
        List<Emprestimo> meusEmprestimos = libraryService.listarEmprestimosPorMembro(userId);
        List<Reserva> minhasReservas = libraryService.listarReservasPorMembro(userId);
        long reservasPendentes = minhasReservas.stream().filter(Reserva::isPendente).count();

        HBox statsBox = new HBox(15);
        statsBox.getChildren().addAll(
            createStatCard("📖", "Empréstimos Ativos", String.valueOf(meusEmprestimos.size()), "#3b82f6"),
            createStatCard("⏳", "Reservas Pendentes", String.valueOf(reservasPendentes), "#8b5cf6"),
            createStatCard("💰", "Multas Pendentes", "R$ " + String.format("%.2f", calcularMinhasMultas(meusEmprestimos)), "#ef4444")
        );

        // Meus Empréstimos Ativos
        VBox emprestimosBox = new VBox(10);
        emprestimosBox.setPadding(new Insets(20));
        emprestimosBox.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-border-color: #e5e7eb; -fx-border-radius: 10;");

        Label emprestimosTitle = new Label("📚 Meus Empréstimos Ativos");
        emprestimosTitle.setFont(Font.font("System", FontWeight.BOLD, 16));

        if (meusEmprestimos.isEmpty()) {
            Label emptyLabel = new Label("Você não possui empréstimos ativos.");
            emptyLabel.setStyle("-fx-text-fill: #6b7280;");
            emprestimosBox.getChildren().addAll(emprestimosTitle, emptyLabel);
        } else {
            VBox emprestimosList = new VBox(8);
            for (Emprestimo e : meusEmprestimos) {
                HBox item = new HBox(10);
                item.setAlignment(Pos.CENTER_LEFT);
                item.setPadding(new Insets(10));
                item.setStyle("-fx-background-color: #f3f4f6; -fx-background-radius: 5;");

                Label livroLabel = new Label("📖 " + e.getTituloLivro());
                livroLabel.setFont(Font.font("System", FontWeight.SEMI_BOLD, 12));
                HBox.setHgrow(livroLabel, Priority.ALWAYS);

                Label dataLabel = new Label("Devolver até: " + e.getDataDevolucaoPrevista());
                dataLabel.setFont(Font.font("System", 11));

                if (e.isAtrasado()) {
                    dataLabel.setStyle("-fx-text-fill: #ef4444;");
                    dataLabel.setText("⚠️ ATRASADO! Multa: R$ " + String.format("%.2f", e.getMulta()));
                }

                item.getChildren().addAll(livroLabel, dataLabel);
                emprestimosList.getChildren().add(item);
            }
            emprestimosBox.getChildren().addAll(emprestimosTitle, emprestimosList);
        }

        // Alterar Senha
        VBox senhaBox = new VBox(15);
        senhaBox.setPadding(new Insets(20));
        senhaBox.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-border-color: #e5e7eb; -fx-border-radius: 10;");

        Label senhaTitle = new Label("🔒 Alterar Senha");
        senhaTitle.setFont(Font.font("System", FontWeight.BOLD, 16));

        GridPane senhaForm = new GridPane();
        senhaForm.setHgap(10);
        senhaForm.setVgap(10);

        PasswordField senhaAtualField = new PasswordField();
        senhaAtualField.setPromptText("Senha atual");
        senhaAtualField.setPrefWidth(200);

        PasswordField novaSenhaField = new PasswordField();
        novaSenhaField.setPromptText("Nova senha");
        novaSenhaField.setPrefWidth(200);

        PasswordField confirmarSenhaField = new PasswordField();
        confirmarSenhaField.setPromptText("Confirmar nova senha");
        confirmarSenhaField.setPrefWidth(200);

        senhaForm.add(new Label("Senha Atual:"), 0, 0);
        senhaForm.add(senhaAtualField, 1, 0);
        senhaForm.add(new Label("Nova Senha:"), 0, 1);
        senhaForm.add(novaSenhaField, 1, 1);
        senhaForm.add(new Label("Confirmar:"), 0, 2);
        senhaForm.add(confirmarSenhaField, 1, 2);

        Button salvarBtn = new Button("💾 Salvar Nova Senha");
        salvarBtn.setStyle("-fx-background-color: #2563eb; -fx-text-fill: white; -fx-cursor: hand;");
        salvarBtn.setOnAction(e -> {
            String senhaAtual = senhaAtualField.getText();
            String novaSenha = novaSenhaField.getText();
            String confirmar = confirmarSenhaField.getText();

            if (senhaAtual.isEmpty() || novaSenha.isEmpty() || confirmar.isEmpty()) {
                showAlert("Erro", "Por favor, preencha todos os campos!", Alert.AlertType.ERROR);
                return;
            }

            if (novaSenha.length() < 4) {
                showAlert("Erro", "A nova senha deve ter pelo menos 4 caracteres!", Alert.AlertType.ERROR);
                return;
            }

            if (novaSenha.equals(confirmar)) {
                boolean sucesso = authService.alterarSenha(
                        authService.getUsuarioLogado().getEmail(),
                        senhaAtual,
                        novaSenha
                );
                if (sucesso) {
                    showAlert("Sucesso", "Senha alterada com sucesso!", Alert.AlertType.INFORMATION);
                    senhaAtualField.clear();
                    novaSenhaField.clear();
                    confirmarSenhaField.clear();
                } else {
                    showAlert("Erro", "Senha atual incorreta!", Alert.AlertType.ERROR);
                }
            } else {
                showAlert("Erro", "As senhas não coincidem!", Alert.AlertType.ERROR);
            }
        });

        senhaBox.getChildren().addAll(senhaTitle, senhaForm, salvarBtn);

        content.getChildren().addAll(title, infoBox, statsBox, emprestimosBox, senhaBox);

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: #f9fafb;");
        return scrollPane;
    }

    private VBox createInfoField(String icon, String label, String value) {
        VBox box = new VBox(2);
        HBox header = new HBox(5);
        header.setAlignment(Pos.CENTER_LEFT);

        Label iconLabel = new Label(icon);
        iconLabel.setFont(Font.font(16));
        Label labelText = new Label(label);
        labelText.setFont(Font.font("System", 11));
        labelText.setStyle("-fx-text-fill: #6b7280;");
        header.getChildren().addAll(iconLabel, labelText);

        Label valueLabel = new Label(value);
        valueLabel.setFont(Font.font("System", FontWeight.SEMI_BOLD, 14));

        box.getChildren().addAll(header, valueLabel);
        return box;
    }

    private VBox createStatCard(String icon, String label, String value, String color) {
        VBox card = new VBox(5);
        card.setPadding(new Insets(15));
        card.setAlignment(Pos.CENTER);
        card.setPrefWidth(180);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-border-color: #e5e7eb; -fx-border-radius: 10;");

        Label iconLabel = new Label(icon);
        iconLabel.setFont(Font.font(24));

        Label valueLabel = new Label(value);
        valueLabel.setFont(Font.font("System", FontWeight.BOLD, 18));
        valueLabel.setStyle("-fx-text-fill: " + color + ";");

        Label nameLabel = new Label(label);
        nameLabel.setFont(Font.font("System", 10));
        nameLabel.setStyle("-fx-text-fill: #6b7280;");

        card.getChildren().addAll(iconLabel, valueLabel, nameLabel);
        return card;
    }

    private double calcularMinhasMultas(List<Emprestimo> emprestimos) {
        return emprestimos.stream()
                .filter(Emprestimo::isAtrasado)
                .mapToDouble(Emprestimo::getMulta)
                .sum();
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
