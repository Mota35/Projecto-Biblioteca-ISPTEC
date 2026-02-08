package isptec.biblioteca.views;

import isptec.biblioteca.model.Reserva;
import isptec.biblioteca.service.LibraryService;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class ReservasView {
    private final BorderPane mainLayout;
    private final LibraryService libraryService;
    private TableView<Reserva> table;

    public ReservasView(Stage stage, BorderPane mainLayout) {
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

        Label title = new Label("Gestão de Reservas");
        title.setFont(Font.font("System", FontWeight.BOLD, 28));

        HBox toolbar = new HBox(10);
        Button atenderBtn = new Button("✅ Atender Reserva");
        atenderBtn.setStyle("-fx-background-color: #16a34a; -fx-text-fill: white;");
        atenderBtn.setOnAction(e -> {
            Reserva selected = table.getSelectionModel().getSelectedItem();
            if (selected != null && selected.isPendente()) {
                libraryService.atenderReserva(selected.getId());
                refreshTable();
            }
        });

        Button cancelBtn = new Button("❌ Cancelar Reserva");
        cancelBtn.setStyle("-fx-background-color: #ef4444; -fx-text-fill: white;");
        cancelBtn.setOnAction(e -> {
            Reserva selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                libraryService.cancelarReserva(selected.getId());
                refreshTable();
            }
        });

        toolbar.getChildren().addAll(atenderBtn, cancelBtn);

        table = new TableView<>();
        TableColumn<Reserva, String> livroCol = new TableColumn<>("Livro");
        livroCol.setCellValueFactory(new PropertyValueFactory<>("tituloLivro"));
        livroCol.setPrefWidth(250);

        TableColumn<Reserva, String> membroCol = new TableColumn<>("Membro");
        membroCol.setCellValueFactory(new PropertyValueFactory<>("nomeMembro"));
        membroCol.setPrefWidth(200);

        TableColumn<Reserva, String> dataCol = new TableColumn<>("Data Reserva");
        dataCol.setCellValueFactory(new PropertyValueFactory<>("dataReserva"));

        TableColumn<Reserva, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        table.getColumns().addAll(livroCol, membroCol, dataCol, statusCol);
        refreshTable();

        content.getChildren().addAll(title, toolbar, table);
        VBox.setVgrow(table, Priority.ALWAYS);
        return content;
    }

    private void refreshTable() {
        table.getItems().clear();
        table.getItems().addAll(libraryService.listarReservas());
    }
}

