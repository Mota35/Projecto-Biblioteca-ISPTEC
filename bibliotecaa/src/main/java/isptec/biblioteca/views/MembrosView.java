package isptec.biblioteca.views;

import isptec.biblioteca.model.Membro;
import isptec.biblioteca.service.AuthService;
import isptec.biblioteca.service.LibraryService;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.UUID;

public class MembrosView {
    private Stage stage;
    private BorderPane mainLayout;
    private LibraryService libraryService;
    private TableView<Membro> table;

    public MembrosView(Stage stage, BorderPane mainLayout) {
        this.stage = stage;
        this.mainLayout = mainLayout;
        this.libraryService = LibraryService.getInstance();
    }

    public void show() {
        mainLayout.setCenter(createContent());
    }

    private VBox createContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));
        content.setStyle("-fx-background-color: #f9fafb;");

        Label title = new Label("Gestão de Membros");
        title.setFont(Font.font("System", FontWeight.BOLD, 28));

        HBox toolbar = new HBox(10);
        Button addBtn = new Button("➕ Adicionar Membro");
        addBtn.setStyle("-fx-background-color: #2563eb; -fx-text-fill: white;");
        addBtn.setOnAction(e -> showAddDialog());

        Button blockBtn = new Button("🚫 Bloquear");
        blockBtn.setOnAction(e -> {
            Membro selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                libraryService.bloquearMembro(selected.getId());
                refreshTable();
            }
        });

        Button unblockBtn = new Button("✅ Desbloquear");
        unblockBtn.setOnAction(e -> {
            Membro selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                libraryService.desbloquearMembro(selected.getId());
                refreshTable();
            }
        });

        toolbar.getChildren().addAll(addBtn, blockBtn, unblockBtn);

        table = new TableView<>();
        TableColumn<Membro, String> nomeCol = new TableColumn<>("Nome");
        nomeCol.setCellValueFactory(new PropertyValueFactory<>("nome"));
        nomeCol.setPrefWidth(200);

        TableColumn<Membro, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        emailCol.setPrefWidth(200);

        TableColumn<Membro, String> matriculaCol = new TableColumn<>("Matrícula");
        matriculaCol.setCellValueFactory(new PropertyValueFactory<>("matricula"));

        TableColumn<Membro, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        table.getColumns().addAll(nomeCol, emailCol, matriculaCol, statusCol);
        refreshTable();

        content.getChildren().addAll(title, toolbar, table);
        VBox.setVgrow(table, Priority.ALWAYS);
        return content;
    }

    private void refreshTable() {
        table.getItems().clear();
        table.getItems().addAll(libraryService.listarMembros());
    }

    private void showAddDialog() {
        Dialog<Membro> dialog = new Dialog<>();
        dialog.setTitle("Adicionar Membro Estudante");
        dialog.setHeaderText("A senha padrão será: 1234 (deve ser alterada no primeiro login)");

        ButtonType saveButtonType = new ButtonType("Salvar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField nomeField = new TextField();
        TextField telefoneField = new TextField();
        TextField matriculaField = new TextField();
        Label emailAutoLabel = new Label();
        emailAutoLabel.setTextFill(Color.web("#2563eb"));
        emailAutoLabel.setFont(Font.font("System", FontWeight.BOLD, 12));
        
        Label infoLabel = new Label("O email será gerado automaticamente como: matricula@isptec.co.ao");
        infoLabel.setWrapText(true);
        infoLabel.setFont(Font.font("System", 10));
        infoLabel.setTextFill(Color.web("#6b7280"));

        // Atualiza o preview do email quando digitar a matrícula
        matriculaField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && !newVal.isEmpty()) {
                emailAutoLabel.setText("Email: " + newVal + "@isptec.co.ao");
            } else {
                emailAutoLabel.setText("");
            }
        });

        grid.add(new Label("Nome:"), 0, 0);
        grid.add(nomeField, 1, 0);
        grid.add(new Label("Matrícula:"), 0, 1);
        grid.add(matriculaField, 1, 1);
        grid.add(emailAutoLabel, 1, 2);
        grid.add(new Label("Telefone:"), 0, 3);
        grid.add(telefoneField, 1, 3);
        grid.add(infoLabel, 0, 4, 2, 1);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                String matricula = matriculaField.getText().trim();
                String nome = nomeField.getText().trim();
                String telefone = telefoneField.getText().trim();
                
                // Validações
                if (matricula.isEmpty() || nome.isEmpty()) {
                    showAlert("Erro", "Por favor, preencha todos os campos obrigatórios!", Alert.AlertType.ERROR);
                    return null;
                }
                
                // Gera email automaticamente
                String email = AuthService.gerarEmailEstudante(matricula);
                
                // Verifica se já existe membro com essa matrícula
                for (Membro m : libraryService.listarMembros()) {
                    if (m.getMatricula().equals(matricula)) {
                        showAlert("Erro", "Já existe um membro com essa matrícula!", Alert.AlertType.ERROR);
                        return null;
                    }
                    if (m.getEmail().equals(email)) {
                        showAlert("Erro", "Já existe um membro com esse email!", Alert.AlertType.ERROR);
                        return null;
                    }
                }
                
                String id = UUID.randomUUID().toString();
                Membro novoMembro = new Membro(id, nome, email, telefone, matricula);
                // A senha padrão "1234" já é definida no construtor do Membro
                
                showAlert("Sucesso", 
                    "Membro criado com sucesso!\n\n" +
                    "Email: " + email + "\n" +
                    "Senha: 1234\n\n" +
                    "O estudante deverá alterar a senha no primeiro login.", 
                    Alert.AlertType.INFORMATION);
                
                return novoMembro;
            }
            return null;
        });

        dialog.showAndWait().ifPresent(membro -> {
            if (membro != null) {
                libraryService.adicionarMembro(membro);
                refreshTable();
            }
        });
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
