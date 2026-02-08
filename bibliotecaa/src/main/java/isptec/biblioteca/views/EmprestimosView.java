package isptec.biblioteca.views;

import isptec.biblioteca.model.Emprestimo;
import isptec.biblioteca.model.Livro;
import isptec.biblioteca.model.Membro;
import isptec.biblioteca.service.LibraryService;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class EmprestimosView {
    private final BorderPane mainLayout;
    private final LibraryService libraryService;
    private TableView<Emprestimo> table;

    public EmprestimosView(Stage stage, BorderPane mainLayout) {
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

        Label title = new Label("Gestão de Empréstimos");
        title.setFont(Font.font("System", FontWeight.BOLD, 28));

        HBox toolbar = new HBox(10);
        Button addBtn = new Button("➕ Novo Empréstimo");
        addBtn.setStyle("-fx-background-color: #2563eb; -fx-text-fill: white;");
        addBtn.setOnAction(e -> showAddDialog());

        Button returnBtn = new Button("📥 Registrar Devolução");
        returnBtn.setStyle("-fx-background-color: #16a34a; -fx-text-fill: white;");
        returnBtn.setOnAction(e -> {
            Emprestimo selected = table.getSelectionModel().getSelectedItem();
            if (selected != null && selected.isAtivo()) {
                libraryService.devolverLivro(selected.getId());
                refreshTable();
            }
        });

        Button multaBtn = new Button("💰 Aplicar Multa");
        multaBtn.setOnAction(e -> {
            Emprestimo selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) showMultaDialog(selected);
        });

        toolbar.getChildren().addAll(addBtn, returnBtn, multaBtn);

        table = new TableView<>();
        TableColumn<Emprestimo, String> livroCol = new TableColumn<>("Livro");
        livroCol.setCellValueFactory(new PropertyValueFactory<>("tituloLivro"));
        livroCol.setPrefWidth(200);

        TableColumn<Emprestimo, String> membroCol = new TableColumn<>("Membro");
        membroCol.setCellValueFactory(new PropertyValueFactory<>("nomeMembro"));
        membroCol.setPrefWidth(150);

        TableColumn<Emprestimo, String> dataEmpCol = new TableColumn<>("Data Empréstimo");
        dataEmpCol.setCellValueFactory(new PropertyValueFactory<>("dataEmprestimo"));

        TableColumn<Emprestimo, String> dataDevCol = new TableColumn<>("Data Devolução");
        dataDevCol.setCellValueFactory(new PropertyValueFactory<>("dataDevolucaoPrevista"));

        TableColumn<Emprestimo, Double> multaCol = new TableColumn<>("Multa");
        multaCol.setCellValueFactory(new PropertyValueFactory<>("multa"));

        table.getColumns().addAll(livroCol, membroCol, dataEmpCol, dataDevCol, multaCol);
        refreshTable();

        content.getChildren().addAll(title, toolbar, table);
        VBox.setVgrow(table, Priority.ALWAYS);
        return content;
    }

    private void refreshTable() {
        table.getItems().clear();
        table.getItems().addAll(libraryService.listarEmprestimos());
    }

    private void showAddDialog() {
        Dialog<Boolean> dialog = new Dialog<>();
        dialog.setTitle("Novo Empréstimo");

        ButtonType saveButtonType = new ButtonType("Salvar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        ComboBox<Livro> livroCombo = new ComboBox<>();
        livroCombo.getItems().addAll(libraryService.listarLivros());
        livroCombo.setPromptText("Selecione um livro");

        ComboBox<Membro> membroCombo = new ComboBox<>();
        membroCombo.getItems().addAll(libraryService.listarMembros());
        membroCombo.setPromptText("Selecione um membro");

        grid.add(new Label("Livro:"), 0, 0);
        grid.add(livroCombo, 1, 0);
        grid.add(new Label("Membro:"), 0, 1);
        grid.add(membroCombo, 1, 1);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                Livro livro = livroCombo.getValue();
                Membro membro = membroCombo.getValue();
                if (livro != null && membro != null) {
                    return libraryService.realizarEmprestimo(livro.getId(), membro.getId());
                }
            }
            return false;
        });

        dialog.showAndWait().ifPresent(success -> {
            if (success) refreshTable();
        });
    }

    private void showMultaDialog(Emprestimo emprestimo) {
        TextInputDialog dialog = new TextInputDialog(String.valueOf(emprestimo.getMulta()));
        dialog.setTitle("Aplicar Multa");
        dialog.setHeaderText("Empréstimo: " + emprestimo.getTituloLivro());
        dialog.setContentText("Valor da multa (R$):");

        dialog.showAndWait().ifPresent(valor -> {
            try {
                double multa = Double.parseDouble(valor);
                libraryService.aplicarMulta(emprestimo.getId(), multa);
                refreshTable();
            } catch (NumberFormatException ignored) {}
        });
    }
}
