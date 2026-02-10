package isptec.biblioteca.views;

import isptec.biblioteca.model.Livro;
import isptec.biblioteca.service.LibraryService;
import javafx.animation.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Scene;
import javafx.util.Duration;

/**
 * Dialog para exibir detalhes de um livro de forma aesthetic
 */
public class LivroDetalhesDialog {

    private final Livro livro;
    private final Stage dialogStage;
    private final LibraryService libraryService;

    public LivroDetalhesDialog(Stage owner, Livro livro) {
        this.livro = livro;
        this.libraryService = LibraryService.getInstance();
        this.dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.initOwner(owner);
        dialogStage.initStyle(StageStyle.TRANSPARENT);
    }

    public void show() {
        VBox root = new VBox();
        root.setStyle("-fx-background-color: transparent;");
        root.setPadding(new Insets(20));

        // Container principal com sombra
        VBox container = new VBox(0);
        container.setMaxWidth(500);
        container.setStyle("-fx-background-color: white; -fx-background-radius: 20;");

        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.web("#000000", 0.2));
        shadow.setRadius(30);
        shadow.setOffsetY(10);
        container.setEffect(shadow);

        // Header com gradiente
        StackPane header = createHeader();

        // Conteúdo
        VBox content = createContent();

        // Botões de ação
        HBox actions = createActions();

        container.getChildren().addAll(header, content, actions);
        root.getChildren().add(container);

        Scene scene = new Scene(root, 540, 650);
        scene.setFill(Color.TRANSPARENT);

        dialogStage.setScene(scene);
        dialogStage.setTitle("Detalhes do Livro");

        // Animação de entrada
        FadeTransition fadeIn = new FadeTransition(Duration.millis(200), container);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        ScaleTransition scaleIn = new ScaleTransition(Duration.millis(200), container);
        scaleIn.setFromX(0.8);
        scaleIn.setFromY(0.8);
        scaleIn.setToX(1);
        scaleIn.setToY(1);

        ParallelTransition parallelIn = new ParallelTransition(fadeIn, scaleIn);
        parallelIn.play();

        dialogStage.showAndWait();
    }

    private StackPane createHeader() {
        StackPane header = new StackPane();
        header.setPrefHeight(200);

        // Gradiente de fundo
        Rectangle bg = new Rectangle();
        bg.widthProperty().bind(header.widthProperty());
        bg.setHeight(200);
        bg.setArcWidth(20);
        bg.setArcHeight(20);

        // Cortar apenas os cantos superiores
        Rectangle clip = new Rectangle();
        clip.widthProperty().bind(header.widthProperty());
        clip.setHeight(220);
        clip.setArcWidth(20);
        clip.setArcHeight(20);
        bg.setClip(clip);

        LinearGradient gradient = new LinearGradient(
            0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
            new Stop(0, Color.web("#6366f1")),
            new Stop(0.5, Color.web("#8b5cf6")),
            new Stop(1, Color.web("#a855f7"))
        );
        bg.setFill(gradient);

        // Conteúdo do header
        VBox headerContent = new VBox(10);
        headerContent.setAlignment(Pos.CENTER);
        headerContent.setPadding(new Insets(30));

        // Ícone do livro
        Label bookIcon = new Label("📖");
        bookIcon.setStyle("-fx-font-size: 60px;");

        // Categoria como badge
        Label categoryBadge = new Label(livro.getCategoria());
        categoryBadge.setStyle(
            "-fx-background-color: rgba(255,255,255,0.2); " +
            "-fx-text-fill: white; " +
            "-fx-padding: 6 16; " +
            "-fx-background-radius: 20; " +
            "-fx-font-size: 12;"
        );

        // Disponibilidade
        boolean disponivel = livro.getQuantidadeDisponivel() > 0;
        Label statusBadge = new Label(disponivel ? "✓ Disponível" : "✗ Indisponível");
        statusBadge.setStyle(
            "-fx-background-color: " + (disponivel ? "rgba(34,197,94,0.9)" : "rgba(239,68,68,0.9)") + "; " +
            "-fx-text-fill: white; " +
            "-fx-padding: 6 16; " +
            "-fx-background-radius: 20; " +
            "-fx-font-size: 12; " +
            "-fx-font-weight: bold;"
        );

        headerContent.getChildren().addAll(bookIcon, categoryBadge, statusBadge);

        // Botão fechar
        Button closeBtn = new Button("✕");
        closeBtn.setStyle(
            "-fx-background-color: rgba(255,255,255,0.2); " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 16; " +
            "-fx-background-radius: 20; " +
            "-fx-min-width: 36; " +
            "-fx-min-height: 36; " +
            "-fx-cursor: hand;"
        );
        closeBtn.setOnMouseEntered(e -> closeBtn.setStyle(
            "-fx-background-color: rgba(255,255,255,0.3); " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 16; " +
            "-fx-background-radius: 20; " +
            "-fx-min-width: 36; " +
            "-fx-min-height: 36; " +
            "-fx-cursor: hand;"
        ));
        closeBtn.setOnMouseExited(e -> closeBtn.setStyle(
            "-fx-background-color: rgba(255,255,255,0.2); " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 16; " +
            "-fx-background-radius: 20; " +
            "-fx-min-width: 36; " +
            "-fx-min-height: 36; " +
            "-fx-cursor: hand;"
        ));
        closeBtn.setOnAction(e -> dialogStage.close());

        StackPane.setAlignment(closeBtn, Pos.TOP_RIGHT);
        StackPane.setMargin(closeBtn, new Insets(15));

        header.getChildren().addAll(bg, headerContent, closeBtn);

        return header;
    }

    private VBox createContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));
        content.setStyle("-fx-background-color: white;");

        // Título
        Label titleLabel = new Label(livro.getTitulo());
        titleLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));
        titleLabel.setTextFill(Color.web("#1e293b"));
        titleLabel.setWrapText(true);

        // Autor
        HBox autorBox = createInfoRow("✍️", "Autor", livro.getAutor());

        // ISBN
        HBox isbnBox = createInfoRow("📋", "ISBN", livro.getIsbn());

        // Editora
        HBox editoraBox = createInfoRow("🏢", "Editora", livro.getEditora());

        // Ano
        HBox anoBox = createInfoRow("📅", "Ano de Publicação", String.valueOf(livro.getAno()));

        // Exemplares
        HBox exemplaresBox = createInfoRow("📚", "Exemplares Disponíveis",
            livro.getQuantidadeDisponivel() + " de " + livro.getQuantidade());

        // Descrição
        VBox descBox = new VBox(8);
        Label descTitle = new Label("📝 Descrição");
        descTitle.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 14));
        descTitle.setTextFill(Color.web("#64748b"));

        Label descText = new Label(livro.getDescricao() != null ? livro.getDescricao() : "Sem descrição disponível.");
        descText.setFont(Font.font("Segoe UI", 14));
        descText.setTextFill(Color.web("#374151"));
        descText.setWrapText(true);
        descText.setStyle(
            "-fx-background-color: #f8fafc; " +
            "-fx-padding: 15; " +
            "-fx-background-radius: 10;"
        );

        descBox.getChildren().addAll(descTitle, descText);

        content.getChildren().addAll(titleLabel, autorBox, isbnBox, editoraBox, anoBox, exemplaresBox, descBox);

        return content;
    }

    private HBox createInfoRow(String icon, String label, String value) {
        HBox row = new HBox(15);
        row.setAlignment(Pos.CENTER_LEFT);

        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-font-size: 18px;");
        iconLabel.setMinWidth(30);

        VBox textBox = new VBox(2);
        Label labelText = new Label(label);
        labelText.setFont(Font.font("Segoe UI", 12));
        labelText.setTextFill(Color.web("#64748b"));

        Label valueText = new Label(value);
        valueText.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 14));
        valueText.setTextFill(Color.web("#1e293b"));

        textBox.getChildren().addAll(labelText, valueText);
        row.getChildren().addAll(iconLabel, textBox);

        return row;
    }

    private HBox createActions() {
        HBox actions = new HBox(15);
        actions.setAlignment(Pos.CENTER);
        actions.setPadding(new Insets(20, 30, 30, 30));
        actions.setStyle("-fx-background-color: white; -fx-background-radius: 0 0 20 20;");

        Button reservarBtn = new Button("📅 Reservar");
        reservarBtn.setPrefWidth(200);
        reservarBtn.setPrefHeight(45);
        reservarBtn.setStyle(
            "-fx-background-color: linear-gradient(to right, #6366f1, #8b5cf6); " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 14; " +
            "-fx-font-weight: bold; " +
            "-fx-background-radius: 10; " +
            "-fx-cursor: hand;"
        );

        DropShadow btnShadow = new DropShadow();
        btnShadow.setColor(Color.web("#6366f1", 0.4));
        btnShadow.setRadius(10);
        btnShadow.setOffsetY(4);
        reservarBtn.setEffect(btnShadow);

        reservarBtn.setOnMouseEntered(e -> {
            reservarBtn.setStyle(
                "-fx-background-color: linear-gradient(to right, #4f46e5, #7c3aed); " +
                "-fx-text-fill: white; " +
                "-fx-font-size: 14; " +
                "-fx-font-weight: bold; " +
                "-fx-background-radius: 10; " +
                "-fx-cursor: hand;"
            );
        });
        reservarBtn.setOnMouseExited(e -> {
            reservarBtn.setStyle(
                "-fx-background-color: linear-gradient(to right, #6366f1, #8b5cf6); " +
                "-fx-text-fill: white; " +
                "-fx-font-size: 14; " +
                "-fx-font-weight: bold; " +
                "-fx-background-radius: 10; " +
                "-fx-cursor: hand;"
            );
        });

        reservarBtn.setOnAction(e -> {
            if (livro.getQuantidadeDisponivel() > 0) {
                showSuccessToast("Reserva realizada com sucesso!");
                dialogStage.close();
            } else {
                showErrorToast("Livro indisponível para reserva");
            }
        });

        Button fecharBtn = new Button("Fechar");
        fecharBtn.setPrefWidth(120);
        fecharBtn.setPrefHeight(45);
        fecharBtn.setStyle(
            "-fx-background-color: white; " +
            "-fx-text-fill: #64748b; " +
            "-fx-font-size: 14; " +
            "-fx-font-weight: bold; " +
            "-fx-background-radius: 10; " +
            "-fx-border-color: #e2e8f0; " +
            "-fx-border-radius: 10; " +
            "-fx-cursor: hand;"
        );
        fecharBtn.setOnMouseEntered(e -> {
            fecharBtn.setStyle(
                "-fx-background-color: #f8fafc; " +
                "-fx-text-fill: #64748b; " +
                "-fx-font-size: 14; " +
                "-fx-font-weight: bold; " +
                "-fx-background-radius: 10; " +
                "-fx-border-color: #e2e8f0; " +
                "-fx-border-radius: 10; " +
                "-fx-cursor: hand;"
            );
        });
        fecharBtn.setOnMouseExited(e -> {
            fecharBtn.setStyle(
                "-fx-background-color: white; " +
                "-fx-text-fill: #64748b; " +
                "-fx-font-size: 14; " +
                "-fx-font-weight: bold; " +
                "-fx-background-radius: 10; " +
                "-fx-border-color: #e2e8f0; " +
                "-fx-border-radius: 10; " +
                "-fx-cursor: hand;"
            );
        });
        fecharBtn.setOnAction(e -> dialogStage.close());

        actions.getChildren().addAll(reservarBtn, fecharBtn);

        return actions;
    }

    private void showSuccessToast(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sucesso");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showErrorToast(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Aviso");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

