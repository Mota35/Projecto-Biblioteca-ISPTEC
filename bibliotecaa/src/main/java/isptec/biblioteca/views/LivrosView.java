package isptec.biblioteca.views;

import isptec.biblioteca.model.Livro;
import isptec.biblioteca.service.LibraryService;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.UUID;

public class LivrosView {
    private Stage stage;
    private BorderPane mainLayout;
    private LibraryService libraryService;
    private TableView<Livro> table;

    public LivrosView(Stage stage, BorderPane mainLayout) {
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

        Label title = new Label("Gestão de Livros");
        title.setFont(Font.font("System", FontWeight.BOLD, 28));

        HBox toolbar = new HBox(10);
        Button addBtn = new Button("➕ Adicionar Livro");
        addBtn.setStyle("-fx-background-color: #2563eb; -fx-text-fill: white; -fx-cursor: hand;");
        addBtn.setOnAction(e -> showAddDialog());

        Button addFotoBtn = new Button("📷 Cadastrar por Foto (OCR)");
        addFotoBtn.setStyle("-fx-background-color: #16a34a; -fx-text-fill: white; -fx-cursor: hand;");
        addFotoBtn.setOnAction(e -> showAddFotoDialog());

        Button editBtn = new Button("✏️ Editar");
        editBtn.setOnAction(e -> {
            Livro selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) showEditDialog(selected);
        });

        Button deleteBtn = new Button("🗑️ Remover");
        deleteBtn.setStyle("-fx-background-color: #ef4444; -fx-text-fill: white; -fx-cursor: hand;");
        deleteBtn.setOnAction(e -> {
            Livro selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                libraryService.removerLivro(selected.getId());
                refreshTable();
            }
        });

        toolbar.getChildren().addAll(addBtn, addFotoBtn, editBtn, deleteBtn);

        table = new TableView<>();
        TableColumn<Livro, String> tituloCol = new TableColumn<>("Título");
        tituloCol.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        tituloCol.setPrefWidth(200);

        TableColumn<Livro, String> autorCol = new TableColumn<>("Autor");
        autorCol.setCellValueFactory(new PropertyValueFactory<>("autor"));
        autorCol.setPrefWidth(150);

        TableColumn<Livro, String> isbnCol = new TableColumn<>("ISBN");
        isbnCol.setCellValueFactory(new PropertyValueFactory<>("isbn"));

        TableColumn<Livro, String> categoriaCol = new TableColumn<>("Categoria");
        categoriaCol.setCellValueFactory(new PropertyValueFactory<>("categoria"));

        TableColumn<Livro, Integer> quantidadeCol = new TableColumn<>("Quantidade");
        quantidadeCol.setCellValueFactory(new PropertyValueFactory<>("quantidade"));

        TableColumn<Livro, Integer> disponivelCol = new TableColumn<>("Disponível");
        disponivelCol.setCellValueFactory(new PropertyValueFactory<>("quantidadeDisponivel"));

        table.getColumns().addAll(tituloCol, autorCol, isbnCol, categoriaCol, quantidadeCol, disponivelCol);
        refreshTable();

        content.getChildren().addAll(title, toolbar, table);
        VBox.setVgrow(table, Priority.ALWAYS);
        return content;
    }

    private void refreshTable() {
        table.getItems().clear();
        table.getItems().addAll(libraryService.listarLivros());
    }

    private void showAddDialog() {
        Dialog<Livro> dialog = new Dialog<>();
        dialog.setTitle("Adicionar Livro");
        dialog.setHeaderText("Preencha os dados do livro");

        ButtonType saveButtonType = new ButtonType("Salvar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField tituloField = new TextField();
        TextField autorField = new TextField();
        TextField isbnField = new TextField();
        TextField editoraField = new TextField();
        TextField anoField = new TextField();
        TextField categoriaField = new TextField();
        TextField quantidadeField = new TextField();
        TextArea descricaoField = new TextArea();
        descricaoField.setPrefRowCount(3);

        grid.add(new Label("Título:"), 0, 0);
        grid.add(tituloField, 1, 0);
        grid.add(new Label("Autor:"), 0, 1);
        grid.add(autorField, 1, 1);
        grid.add(new Label("ISBN:"), 0, 2);
        grid.add(isbnField, 1, 2);
        grid.add(new Label("Editora:"), 0, 3);
        grid.add(editoraField, 1, 3);
        grid.add(new Label("Ano:"), 0, 4);
        grid.add(anoField, 1, 4);
        grid.add(new Label("Categoria:"), 0, 5);
        grid.add(categoriaField, 1, 5);
        grid.add(new Label("Quantidade:"), 0, 6);
        grid.add(quantidadeField, 1, 6);
        grid.add(new Label("Descrição:"), 0, 7);
        grid.add(descricaoField, 1, 7);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                try {
                    String id = UUID.randomUUID().toString();
                    return new Livro(
                        id,
                        tituloField.getText(),
                        autorField.getText(),
                        isbnField.getText(),
                        editoraField.getText(),
                        Integer.parseInt(anoField.getText()),
                        categoriaField.getText(),
                        Integer.parseInt(quantidadeField.getText()),
                        descricaoField.getText()
                    );
                } catch (NumberFormatException e) {
                    return null;
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(livro -> {
            libraryService.adicionarLivro(livro);
            refreshTable();
        });
    }

    private void showAddFotoDialog() {
        // Implementar a lógica para adicionar livro por foto (OCR)
        // Este é um exemplo de como você pode começar a implementar isso
        Dialog<Livro> dialog = new Dialog<>();
        dialog.setTitle("Adicionar Livro por Foto (OCR)");
        dialog.setHeaderText("Selecione uma foto do livro para extrair os dados");

        ButtonType saveButtonType = new ButtonType("Salvar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField tituloField = new TextField();
        TextField autorField = new TextField();
        TextField isbnField = new TextField();
        TextField editoraField = new TextField();
        TextField anoField = new TextField();
        TextField categoriaField = new TextField();
        TextField quantidadeField = new TextField();
        TextArea descricaoField = new TextArea();
        descricaoField.setPrefRowCount(3);

        grid.add(new Label("Título:"), 0, 0);
        grid.add(tituloField, 1, 0);
        grid.add(new Label("Autor:"), 0, 1);
        grid.add(autorField, 1, 1);
        grid.add(new Label("ISBN:"), 0, 2);
        grid.add(isbnField, 1, 2);
        grid.add(new Label("Editora:"), 0, 3);
        grid.add(editoraField, 1, 3);
        grid.add(new Label("Ano:"), 0, 4);
        grid.add(anoField, 1, 4);
        grid.add(new Label("Categoria:"), 0, 5);
        grid.add(categoriaField, 1, 5);
        grid.add(new Label("Quantidade:"), 0, 6);
        grid.add(quantidadeField, 1, 6);
        grid.add(new Label("Descrição:"), 0, 7);
        grid.add(descricaoField, 1, 7);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                try {
                    String id = UUID.randomUUID().toString();
                    return new Livro(
                        id,
                        tituloField.getText(),
                        autorField.getText(),
                        isbnField.getText(),
                        editoraField.getText(),
                        Integer.parseInt(anoField.getText()),
                        categoriaField.getText(),
                        Integer.parseInt(quantidadeField.getText()),
                        descricaoField.getText()
                    );
                } catch (NumberFormatException e) {
                    return null;
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(livro -> {
            libraryService.adicionarLivro(livro);
            refreshTable();
        });
    }

    private void showEditDialog(Livro livro) {
        Dialog<Livro> dialog = new Dialog<>();
        dialog.setTitle("Editar Livro");
        dialog.setHeaderText("Editar dados do livro");

        ButtonType saveButtonType = new ButtonType("Salvar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField tituloField = new TextField(livro.getTitulo());
        TextField autorField = new TextField(livro.getAutor());
        TextField quantidadeField = new TextField(String.valueOf(livro.getQuantidade()));

        grid.add(new Label("Título:"), 0, 0);
        grid.add(tituloField, 1, 0);
        grid.add(new Label("Autor:"), 0, 1);
        grid.add(autorField, 1, 1);
        grid.add(new Label("Quantidade:"), 0, 2);
        grid.add(quantidadeField, 1, 2);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                livro.setTitulo(tituloField.getText());
                livro.setAutor(autorField.getText());
                try {
                    livro.setQuantidade(Integer.parseInt(quantidadeField.getText()));
                } catch (NumberFormatException ignored) {}
                return livro;
            }
            return null;
        });

        dialog.showAndWait().ifPresent(l -> {
            libraryService.atualizarLivro(l);
            refreshTable();
        });
    }
}