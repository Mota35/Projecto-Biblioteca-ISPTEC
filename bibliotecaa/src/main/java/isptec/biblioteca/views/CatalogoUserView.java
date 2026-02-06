package isptec.biblioteca.views;

import isptec.biblioteca.model.Livro;
import isptec.biblioteca.service.AuthService;
import isptec.biblioteca.service.LibraryService;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class CatalogoUserView {
    private Stage stage;
    private BorderPane mainLayout;
    private LibraryService libraryService;
    private AuthService authService;
    private VBox catalogoBox;

    public CatalogoUserView(Stage stage, BorderPane mainLayout) {
        this.stage = stage;
        this.mainLayout = mainLayout;
        this.libraryService = LibraryService.getInstance();
        this.authService = AuthService.getInstance();
    }

    public void show() {
        mainLayout.setCenter(createContent());
    }

    private ScrollPane createContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));
        content.setStyle("-fx-background-color: #f9fafb;");

        Label title = new Label("🔍 Consultar Livros");
        title.setFont(Font.font("System", FontWeight.BOLD, 28));

        Label subtitle = new Label("Explore o catálogo da biblioteca");
        subtitle.setFont(Font.font("System", 14));

        HBox searchBox = new HBox(10);
        TextField searchField = new TextField();
        searchField.setPromptText("Buscar por título, autor ou categoria...");
        searchField.setPrefHeight(40);
        HBox.setHgrow(searchField, Priority.ALWAYS);

        Button searchBtn = new Button("🔍 Buscar");
        searchBtn.setPrefHeight(40);
        searchBtn.setStyle("-fx-background-color: #2563eb; -fx-text-fill: white;");
        searchBtn.setOnAction(e -> buscarLivros(searchField.getText()));

        searchField.setOnAction(e -> searchBtn.fire());

        searchBox.getChildren().addAll(searchField, searchBtn);

        catalogoBox = new VBox(15);
        carregarTodosLivros();

        content.getChildren().addAll(title, subtitle, searchBox, catalogoBox);

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: #f9fafb;");
        return scrollPane;
    }

    private void carregarTodosLivros() {
        catalogoBox.getChildren().clear();
        for (Livro livro : libraryService.listarLivros()) {
            catalogoBox.getChildren().add(criarCardLivro(livro));
        }
    }

    private void buscarLivros(String termo) {
        catalogoBox.getChildren().clear();
        for (Livro livro : libraryService.buscarLivros(termo)) {
            catalogoBox.getChildren().add(criarCardLivro(livro));
        }
    }

    private VBox criarCardLivro(Livro livro) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(20));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-border-color: #e5e7eb; -fx-border-radius: 10;");

        HBox header = new HBox(10);
        header.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        
        Label titulo = new Label(livro.getTitulo());
        titulo.setFont(Font.font("System", FontWeight.BOLD, 16));
        HBox.setHgrow(titulo, Priority.ALWAYS);

        Label status = new Label(livro.isDisponivel() ? "✅ Disponível" : "❌ Indisponível");
        status.setStyle(livro.isDisponivel() ? 
            "-fx-background-color: #d1fae5; -fx-padding: 5 10; -fx-background-radius: 5; -fx-text-fill: #065f46;" :
            "-fx-background-color: #fee2e2; -fx-padding: 5 10; -fx-background-radius: 5; -fx-text-fill: #991b1b;");

        header.getChildren().addAll(titulo, status);

        Label autor = new Label("por " + livro.getAutor() + " • " + livro.getAno());
        autor.setFont(Font.font("System", 12));

        Label descricao = new Label(livro.getDescricao());
        descricao.setWrapText(true);
        descricao.setFont(Font.font("System", 11));

        HBox detalhes = new HBox(20);
        detalhes.getChildren().addAll(
            new Label("📚 Editora: " + livro.getEditora()),
            new Label("📖 ISBN: " + livro.getIsbn()),
            new Label("📂 " + livro.getCategoria())
        );

        if (livro.isDisponivel()) {
            Label disponivel = new Label(livro.getQuantidadeDisponivel() + " exemplar(es) disponível(is)");
            disponivel.setStyle("-fx-background-color: #d1fae5; -fx-padding: 10; -fx-background-radius: 5; -fx-text-fill: #065f46;");
            
            Button solicitarBtn = new Button("Solicitar Empréstimo");
            solicitarBtn.setStyle("-fx-background-color: #2563eb; -fx-text-fill: white; -fx-cursor: hand;");
            solicitarBtn.setOnAction(e -> {
                String userId = authService.getUsuarioLogado().getId();
                boolean sucesso = libraryService.realizarEmprestimo(livro.getId(), userId);
                if (sucesso) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Sucesso");
                    alert.setHeaderText(null);
                    alert.setContentText("Empréstimo realizado com sucesso!\n\nPrazo de devolução: 14 dias\nRenovações permitidas: até 2 vezes");
                    alert.showAndWait();
                    mainLayout.setCenter(createContent());
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erro");
                    alert.setHeaderText(null);
                    alert.setContentText("Não foi possível realizar o empréstimo. Verifique se você está bloqueado.");
                    alert.showAndWait();
                }
            });
            
            card.getChildren().addAll(header, autor, descricao, detalhes, disponivel, solicitarBtn);
        } else {
            Label indisponivelLabel = new Label("Livro indisponível no momento");
            indisponivelLabel.setStyle("-fx-background-color: #fee2e2; -fx-padding: 10; -fx-background-radius: 5; -fx-text-fill: #991b1b;");
            
            Button reservarBtn = new Button("Entrar na Fila de Reservas");
            reservarBtn.setStyle("-fx-background-color: #f59e0b; -fx-text-fill: white; -fx-cursor: hand;");
            reservarBtn.setOnAction(e -> {
                String userId = authService.getUsuarioLogado().getId();
                boolean sucesso = libraryService.realizarReserva(livro.getId(), userId);
                if (sucesso) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Sucesso");
                    alert.setHeaderText(null);
                    alert.setContentText("Você foi adicionado à fila de reservas!\n\nSerá notificado quando o livro estiver disponível.");
                    alert.showAndWait();
                    mainLayout.setCenter(createContent());
                }
            });
            card.getChildren().addAll(header, autor, descricao, detalhes, indisponivelLabel, reservarBtn);
        }

        return card;
    }
}