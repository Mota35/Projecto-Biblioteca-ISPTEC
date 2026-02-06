package isptec.biblioteca.views;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class RelatoriosView {
    private Stage stage;
    private BorderPane mainLayout;

    public RelatoriosView(Stage stage, BorderPane mainLayout) {
        this.stage = stage;
        this.mainLayout = mainLayout;
    }

    public void show() {
        mainLayout.setCenter(createContent());
    }

    private VBox createContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));
        content.setStyle("-fx-background-color: #f9fafb;");

        Label title = new Label("Relatórios");
        title.setFont(Font.font("System", FontWeight.BOLD, 28));

        Label placeholder = new Label("Funcionalidade de relatórios em desenvolvimento...");
        placeholder.setFont(Font.font("System", 14));

        content.getChildren().addAll(title, placeholder);
        return content;
    }
}

