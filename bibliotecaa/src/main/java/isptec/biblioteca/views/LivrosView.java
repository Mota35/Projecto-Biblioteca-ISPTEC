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
    private final BorderPane mainLayout;
    private final LibraryService libraryService;
    private TableView<Livro> table;

    public LivrosView(Stage stage, BorderPane mainLayout) {
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
        Dialog<Livro> dialog = new Dialog<>();
        dialog.setTitle("📷 Cadastrar Livro por Foto (OCR)");
        dialog.setHeaderText("Inteligência Artificial para extração de dados");

        ButtonType scanButtonType = new ButtonType("🔍 Analisar Imagem", ButtonBar.ButtonData.OTHER);
        ButtonType saveButtonType = new ButtonType("💾 Salvar Livro", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(scanButtonType, saveButtonType, ButtonType.CANCEL);

        VBox mainContent = new VBox(15);
        mainContent.setPadding(new Insets(20));

        // Seção de upload
        VBox uploadSection = new VBox(10);
        uploadSection.setStyle("-fx-background-color: #f3f4f6; -fx-padding: 20; -fx-background-radius: 10;");

        Label uploadIcon = new Label("📷");
        uploadIcon.setStyle("-fx-font-size: 40px;");

        Label uploadLabel = new Label("Selecione uma imagem da capa ou folha de rosto do livro");
        uploadLabel.setWrapText(true);

        Button selectFileBtn = new Button("📁 Selecionar Imagem");
        selectFileBtn.setStyle("-fx-background-color: #2563eb; -fx-text-fill: white;");

        Label selectedFileLabel = new Label("Nenhum arquivo selecionado");
        selectedFileLabel.setStyle("-fx-text-fill: #6b7280; -fx-font-size: 11px;");

        // Simular seleção de arquivo
        final String[] selectedFile = {null};
        selectFileBtn.setOnAction(e -> {
            javafx.stage.FileChooser fileChooser = new javafx.stage.FileChooser();
            fileChooser.setTitle("Selecionar Imagem do Livro");
            fileChooser.getExtensionFilters().addAll(
                new javafx.stage.FileChooser.ExtensionFilter("Imagens", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp")
            );
            java.io.File file = fileChooser.showOpenDialog(dialog.getOwner());
            if (file != null) {
                selectedFile[0] = file.getAbsolutePath();
                selectedFileLabel.setText("✅ " + file.getName());
            }
        });

        uploadSection.getChildren().addAll(uploadIcon, uploadLabel, selectFileBtn, selectedFileLabel);
        uploadSection.setAlignment(javafx.geometry.Pos.CENTER);

        // Status da análise
        Label statusLabel = new Label("");
        statusLabel.setStyle("-fx-font-size: 12px;");
        ProgressBar progressBar = new ProgressBar(0);
        progressBar.setPrefWidth(400);
        progressBar.setVisible(false);

        // Campos extraídos
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setVisible(false);

        TextField tituloField = new TextField();
        tituloField.setPromptText("Será preenchido automaticamente...");
        TextField autorField = new TextField();
        autorField.setPromptText("Será preenchido automaticamente...");
        TextField isbnField = new TextField();
        isbnField.setPromptText("Será preenchido automaticamente...");
        TextField editoraField = new TextField();
        editoraField.setPromptText("Será preenchido automaticamente...");
        TextField anoField = new TextField();
        anoField.setPromptText("Ex: 2023");
        TextField categoriaField = new TextField();
        categoriaField.setPromptText("Ex: Programação");
        TextField quantidadeField = new TextField("1");
        TextArea descricaoField = new TextArea();
        descricaoField.setPrefRowCount(2);
        descricaoField.setPromptText("Descrição do livro...");

        grid.add(new Label("📖 Título:"), 0, 0);
        grid.add(tituloField, 1, 0);
        grid.add(new Label("✍️ Autor:"), 0, 1);
        grid.add(autorField, 1, 1);
        grid.add(new Label("🔢 ISBN:"), 0, 2);
        grid.add(isbnField, 1, 2);
        grid.add(new Label("🏢 Editora:"), 0, 3);
        grid.add(editoraField, 1, 3);
        grid.add(new Label("📅 Ano:"), 0, 4);
        grid.add(anoField, 1, 4);
        grid.add(new Label("📂 Categoria:"), 0, 5);
        grid.add(categoriaField, 1, 5);
        grid.add(new Label("📚 Quantidade:"), 0, 6);
        grid.add(quantidadeField, 1, 6);
        grid.add(new Label("📝 Descrição:"), 0, 7);
        grid.add(descricaoField, 1, 7);

        // Info sobre OCR
        Label infoLabel = new Label("💡 A IA irá analisar a imagem e extrair: título, autor, ISBN e editora automaticamente.");
        infoLabel.setWrapText(true);
        infoLabel.setStyle("-fx-text-fill: #6b7280; -fx-font-size: 11px;");

        mainContent.getChildren().addAll(uploadSection, statusLabel, progressBar, grid, infoLabel);
        dialog.getDialogPane().setContent(mainContent);
        dialog.getDialogPane().setPrefWidth(500);

        // Ação do botão de análise
        Button scanBtn = (Button) dialog.getDialogPane().lookupButton(scanButtonType);
        scanBtn.addEventFilter(javafx.event.ActionEvent.ACTION, event -> {
            event.consume(); // Não fechar o diálogo

            if (selectedFile[0] == null) {
                statusLabel.setText("❌ Por favor, selecione uma imagem primeiro!");
                statusLabel.setStyle("-fx-text-fill: #ef4444; -fx-font-size: 12px;");
                return;
            }

            // Simular análise OCR
            progressBar.setVisible(true);
            progressBar.setProgress(0);
            statusLabel.setText("🔄 Analisando imagem com IA...");
            statusLabel.setStyle("-fx-text-fill: #2563eb; -fx-font-size: 12px;");

            // Simular progresso
            javafx.animation.Timeline timeline = new javafx.animation.Timeline(
                new javafx.animation.KeyFrame(javafx.util.Duration.millis(100), e -> progressBar.setProgress(progressBar.getProgress() + 0.05))
            );
            timeline.setCycleCount(20);
            timeline.setOnFinished(e -> {
                // Simular dados extraídos (em produção seria chamada a API de OCR)
                String[] livrosSimulados = {
                    "Algoritmos: Teoria e Prática|Thomas H. Cormen|978-8535236996|Campus",
                    "Java: Como Programar|Paul Deitel|978-8543004792|Pearson",
                    "Python Crash Course|Eric Matthes|978-1593279288|No Starch Press",
                    "Clean Architecture|Robert C. Martin|978-0134494166|Prentice Hall",
                    "Refactoring|Martin Fowler|978-0134757599|Addison-Wesley"
                };

                String[] dados = livrosSimulados[new java.util.Random().nextInt(livrosSimulados.length)].split("\\|");

                tituloField.setText(dados[0]);
                autorField.setText(dados[1]);
                isbnField.setText(dados[2]);
                editoraField.setText(dados[3]);
                anoField.setText(String.valueOf(2015 + new java.util.Random().nextInt(10)));
                categoriaField.setText("Programação");

                grid.setVisible(true);
                statusLabel.setText("✅ Dados extraídos com sucesso! Verifique e corrija se necessário.");
                statusLabel.setStyle("-fx-text-fill: #22c55e; -fx-font-size: 12px;");
                progressBar.setProgress(1);
            });
            timeline.play();
        });

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                try {
                    if (tituloField.getText().isEmpty()) {
                        return null;
                    }
                    String id = UUID.randomUUID().toString();
                    return new Livro(
                        id,
                        tituloField.getText(),
                        autorField.getText(),
                        isbnField.getText(),
                        editoraField.getText(),
                        Integer.parseInt(anoField.getText().isEmpty() ? "2024" : anoField.getText()),
                        categoriaField.getText(),
                        Integer.parseInt(quantidadeField.getText().isEmpty() ? "1" : quantidadeField.getText()),
                        descricaoField.getText()
                    );
                } catch (NumberFormatException e) {
                    return null;
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(livro -> {
            if (livro != null) {
                libraryService.adicionarLivro(livro);
                refreshTable();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Sucesso");
                alert.setHeaderText(null);
                alert.setContentText("📚 Livro \"" + livro.getTitulo() + "\" cadastrado com sucesso via OCR!");
                alert.showAndWait();
            }
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