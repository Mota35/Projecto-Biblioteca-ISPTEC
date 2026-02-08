package isptec.biblioteca.views;

import isptec.biblioteca.ServiceFactory;
import isptec.biblioteca.model.Reserva;
import isptec.biblioteca.service.AuthService;
import isptec.biblioteca.service.LibraryService;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.List;

public class MinhasReservasView {
    private final BorderPane mainLayout;
    private final LibraryService libraryService;
    private final AuthService authService;

    public MinhasReservasView(Stage stage, BorderPane mainLayout) {
        this.mainLayout = mainLayout;
        this.libraryService = LibraryService.getInstance();
        this.authService = ServiceFactory.getInstance().getAuthService();
    }

    public void show() {
        mainLayout.setCenter(createContent());
    }

    private ScrollPane createContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));
        content.setStyle("-fx-background-color: #f9fafb;");

        Label title = new Label("Minhas Reservas");
        title.setFont(Font.font("System", FontWeight.BOLD, 28));

        String userId = String.valueOf(authService.getUsuarioLogado().getId());
        List<Reserva> minhasReservas = libraryService.listarReservasPorMembro(userId);
        List<Reserva> pendentes = minhasReservas.stream()
            .filter(Reserva::isPendente)
            .toList();

        Label subtitle = new Label(pendentes.size() + " reserva(s) ativa(s)");
        subtitle.setFont(Font.font("System", 14));

        VBox reservasBox = new VBox(15);

        if (pendentes.isEmpty()) {
            VBox emptyState = new VBox(10);
            emptyState.setAlignment(javafx.geometry.Pos.CENTER);
            emptyState.setPadding(new Insets(50));
            
            Label emptyIcon = new Label("⏰");
            emptyIcon.setFont(Font.font(40));
            Label emptyText = new Label("Você não possui reservas ativas");
            Label emptySubtext = new Label("Vá para \"Consultar Livros\" para reservar um livro indisponível");
            emptySubtext.setFont(Font.font("System", 11));
            
            emptyState.getChildren().addAll(emptyIcon, emptyText, emptySubtext);
            reservasBox.getChildren().add(emptyState);
        } else {
            for (int i = 0; i < pendentes.size(); i++) {
                reservasBox.getChildren().add(criarCardReserva(pendentes.get(i), i + 1));
            }
        }

        content.getChildren().addAll(title, subtitle, reservasBox);

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: #f9fafb;");
        return scrollPane;
    }

    private VBox criarCardReserva(Reserva reserva, int posicao) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(20));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-border-color: #e5e7eb; -fx-border-radius: 10;");

        HBox header = new HBox(10);
        header.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        
        Label titulo = new Label(reserva.getTituloLivro());
        titulo.setFont(Font.font("System", FontWeight.BOLD, 14));
        HBox.setHgrow(titulo, Priority.ALWAYS);

        Label posicaoLabel = new Label("#" + posicao + " na fila");
        posicaoLabel.setStyle("-fx-background-color: #e0e7ff; -fx-padding: 5 10; -fx-background-radius: 5;");

        header.getChildren().addAll(titulo, posicaoLabel);

        Label data = new Label("📅 Reservado em: " + reserva.getDataReserva());
        data.setFont(Font.font("System", 11));

        Button cancelarBtn = new Button("Cancelar Reserva");
        cancelarBtn.setStyle("-fx-background-color: #ef4444; -fx-text-fill: white;");
        cancelarBtn.setOnAction(e -> {
            libraryService.cancelarReserva(reserva.getId());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Sucesso");
            alert.setHeaderText(null);
            alert.setContentText("Reserva cancelada com sucesso!");
            alert.showAndWait();
            mainLayout.setCenter(createContent());
        });

        card.getChildren().addAll(header, data, cancelarBtn);
        return card;
    }
}
