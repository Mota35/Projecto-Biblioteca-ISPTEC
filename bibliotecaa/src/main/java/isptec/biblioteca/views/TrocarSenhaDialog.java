package isptec.biblioteca.views;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class TrocarSenhaDialog {
    
    public static String mostrar() {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Trocar Senha");
        dialog.setHeaderText("Primeiro Acesso - Troca de Senha Obrigatória");
        
        Label infoLabel = new Label("Por segurança, você deve alterar sua senha no primeiro acesso.");
        infoLabel.setWrapText(true);
        infoLabel.setFont(Font.font("System", 11));
        infoLabel.setTextFill(Color.web("#ef4444"));
        infoLabel.setStyle("-fx-background-color: #fee2e2; -fx-padding: 10; -fx-background-radius: 5;");

        ButtonType confirmarButtonType = new ButtonType("Confirmar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(confirmarButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        PasswordField novaSenhaField = new PasswordField();
        novaSenhaField.setPromptText("Digite a nova senha");
        novaSenhaField.setPrefWidth(250);

        PasswordField confirmarSenhaField = new PasswordField();
        confirmarSenhaField.setPromptText("Confirme a nova senha");
        confirmarSenhaField.setPrefWidth(250);

        Label avisoLabel = new Label();
        avisoLabel.setTextFill(Color.RED);
        avisoLabel.setVisible(false);

        grid.add(infoLabel, 0, 0, 2, 1);
        grid.add(new Label("Nova Senha:"), 0, 1);
        grid.add(novaSenhaField, 1, 1);
        grid.add(new Label("Confirmar Senha:"), 0, 2);
        grid.add(confirmarSenhaField, 1, 2);
        grid.add(avisoLabel, 0, 3, 2, 1);

        dialog.getDialogPane().setContent(grid);

        // Validação
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == confirmarButtonType) {
                String novaSenha = novaSenhaField.getText();
                String confirmar = confirmarSenhaField.getText();

                if (novaSenha.isEmpty() || confirmar.isEmpty()) {
                    avisoLabel.setText("Por favor, preencha todos os campos!");
                    avisoLabel.setVisible(true);
                    return null;
                }

                if (novaSenha.length() < 4) {
                    avisoLabel.setText("A senha deve ter no mínimo 4 caracteres!");
                    avisoLabel.setVisible(true);
                    return null;
                }

                if (!novaSenha.equals(confirmar)) {
                    avisoLabel.setText("As senhas não coincidem!");
                    avisoLabel.setVisible(true);
                    return null;
                }

                return novaSenha;
            }
            return null;
        });

        while (true) {
            var result = dialog.showAndWait();
            if (result.isPresent() && result.get() != null) {
                return result.get();
            } else if (!result.isPresent()) {
                // Usuário cancelou - não permite
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Atenção");
                alert.setHeaderText(null);
                alert.setContentText("Você deve trocar a senha para continuar!");
                alert.showAndWait();
                // Continua no loop
            }
        }
    }
}

