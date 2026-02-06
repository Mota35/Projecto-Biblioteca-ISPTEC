package isptec.biblioteca.views;

import isptec.biblioteca.service.AuthService;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class PerfilUserView {
    private Stage stage;
    private BorderPane mainLayout;
    private AuthService authService;

    public PerfilUserView(Stage stage, BorderPane mainLayout) {
        this.stage = stage;
        this.mainLayout = mainLayout;
        this.authService = AuthService.getInstance();
    }

    public void show() {
        mainLayout.setCenter(createContent());
    }

    private VBox createContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));
        content.setStyle("-fx-background-color: #f9fafb;");
        content.setMaxWidth(600);

        Label title = new Label("Meu Perfil");
        title.setFont(Font.font("System", FontWeight.BOLD, 28));

        // Informações Pessoais
        VBox infoBox = new VBox(15);
        infoBox.setPadding(new Insets(20));
        infoBox.setStyle("-fx-background-color: white; -fx-background-radius: 10;");

        Label infoTitle = new Label("Informações Pessoais");
        infoTitle.setFont(Font.font("System", FontWeight.BOLD, 16));

        HBox nomeBox = new HBox(10);
        Label nomeIcon = new Label("👤");
        nomeIcon.setFont(Font.font(20));
        VBox nomeText = new VBox(2);
        Label nomeLabel = new Label("Nome");
        nomeLabel.setFont(Font.font("System", 11));
        Label nomeValue = new Label(authService.getUsuarioLogado().getNome());
        nomeValue.setFont(Font.font("System", FontWeight.SEMI_BOLD, 14));
        nomeText.getChildren().addAll(nomeLabel, nomeValue);
        nomeBox.getChildren().addAll(nomeIcon, nomeText);

        HBox emailBox = new HBox(10);
        Label emailIcon = new Label("📧");
        emailIcon.setFont(Font.font(20));
        VBox emailText = new VBox(2);
        Label emailLabel = new Label("Email");
        emailLabel.setFont(Font.font("System", 11));
        Label emailValue = new Label(authService.getUsuarioLogado().getEmail());
        emailValue.setFont(Font.font("System", FontWeight.SEMI_BOLD, 14));
        emailText.getChildren().addAll(emailLabel, emailValue);
        emailBox.getChildren().addAll(emailIcon, emailText);

        infoBox.getChildren().addAll(infoTitle, nomeBox, emailBox);

        // Alterar Senha
        VBox senhaBox = new VBox(15);
        senhaBox.setPadding(new Insets(20));
        senhaBox.setStyle("-fx-background-color: white; -fx-background-radius: 10;");

        Label senhaTitle = new Label("🔒 Segurança");
        senhaTitle.setFont(Font.font("System", FontWeight.BOLD, 16));

        Label senhaDesc = new Label("Altere sua senha de acesso");
        senhaDesc.setFont(Font.font("System", 11));

        GridPane senhaForm = new GridPane();
        senhaForm.setHgap(10);
        senhaForm.setVgap(10);

        PasswordField senhaAtualField = new PasswordField();
        senhaAtualField.setPromptText("Senha atual");

        PasswordField novaSenhaField = new PasswordField();
        novaSenhaField.setPromptText("Nova senha");

        PasswordField confirmarSenhaField = new PasswordField();
        confirmarSenhaField.setPromptText("Confirmar nova senha");

        senhaForm.add(new Label("Senha Atual:"), 0, 0);
        senhaForm.add(senhaAtualField, 1, 0);
        senhaForm.add(new Label("Nova Senha:"), 0, 1);
        senhaForm.add(novaSenhaField, 1, 1);
        senhaForm.add(new Label("Confirmar:"), 0, 2);
        senhaForm.add(confirmarSenhaField, 1, 2);

        Button salvarBtn = new Button("Salvar Senha");
        salvarBtn.setStyle("-fx-background-color: #2563eb; -fx-text-fill: white;");
        salvarBtn.setOnAction(e -> {
            String senhaAtual = senhaAtualField.getText();
            String novaSenha = novaSenhaField.getText();
            String confirmar = confirmarSenhaField.getText();

            if (novaSenha.equals(confirmar)) {
                authService.alterarSenha(senhaAtual, novaSenha);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Sucesso");
                alert.setHeaderText(null);
                alert.setContentText("Senha alterada com sucesso!");
                alert.showAndWait();
                senhaAtualField.clear();
                novaSenhaField.clear();
                confirmarSenhaField.clear();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erro");
                alert.setHeaderText(null);
                alert.setContentText("As senhas não coincidem!");
                alert.showAndWait();
            }
        });

        senhaBox.getChildren().addAll(senhaTitle, senhaDesc, senhaForm, salvarBtn);

        content.getChildren().addAll(title, infoBox, senhaBox);
        return content;
    }
}
