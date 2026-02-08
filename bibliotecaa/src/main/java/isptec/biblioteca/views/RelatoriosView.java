package isptec.biblioteca.views;

import isptec.biblioteca.model.Emprestimo;
import isptec.biblioteca.model.Livro;
import isptec.biblioteca.model.Membro;
import isptec.biblioteca.model.Reserva;
import isptec.biblioteca.service.LibraryService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class RelatoriosView {
    private final BorderPane mainLayout;
    private final LibraryService libraryService;
    private TextArea reportArea;

    public RelatoriosView(Stage stage, BorderPane mainLayout) {
        this.mainLayout = mainLayout;
        this.libraryService = LibraryService.getInstance();
    }

    public void show() {
        mainLayout.setCenter(createContent());
    }

    private ScrollPane createContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));
        content.setStyle("-fx-background-color: #f9fafb;");

        Label title = new Label("📊 Relatórios e Estatísticas");
        title.setFont(Font.font("System", FontWeight.BOLD, 28));

        Label subtitle = new Label("Gerado em: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        subtitle.setFont(Font.font("System", 12));
        subtitle.setStyle("-fx-text-fill: #6b7280;");

        // Cards de estatísticas
        HBox statsCards = new HBox(15);
        statsCards.getChildren().addAll(
            createStatCard("📚", "Total de Livros", String.valueOf(libraryService.getTotalLivros()), "#3b82f6"),
            createStatCard("📖", "Emprestados", String.valueOf(libraryService.getLivrosEmprestados()), "#f59e0b"),
            createStatCard("⏳", "Reservas", String.valueOf(libraryService.getReservasPendentes()), "#8b5cf6"),
            createStatCard("👥", "Membros", String.valueOf(libraryService.getMembrosAtivos()), "#10b981"),
            createStatCard("💰", "Multas", "R$ " + String.format("%.2f", libraryService.getTotalMultas()), "#ef4444")
        );

        // Botões de relatórios
        Label reportLabel = new Label("Gerar Relatório:");
        reportLabel.setFont(Font.font("System", FontWeight.BOLD, 14));

        HBox reportButtons = new HBox(10);

        Button btnGeral = new Button("📋 Relatório Geral");
        btnGeral.setStyle("-fx-background-color: #2563eb; -fx-text-fill: white; -fx-cursor: hand;");
        btnGeral.setOnAction(e -> generateGeralReport());

        Button btnEmprestimos = new Button("📖 Empréstimos Ativos");
        btnEmprestimos.setStyle("-fx-background-color: #16a34a; -fx-text-fill: white; -fx-cursor: hand;");
        btnEmprestimos.setOnAction(e -> generateEmprestimosReport());

        Button btnAtrasados = new Button("⚠️ Livros Atrasados");
        btnAtrasados.setStyle("-fx-background-color: #ef4444; -fx-text-fill: white; -fx-cursor: hand;");
        btnAtrasados.setOnAction(e -> generateAtrasadosReport());

        Button btnReservas = new Button("📝 Reservas Pendentes");
        btnReservas.setStyle("-fx-background-color: #8b5cf6; -fx-text-fill: white; -fx-cursor: hand;");
        btnReservas.setOnAction(e -> generateReservasReport());

        Button btnMembros = new Button("👥 Lista de Membros");
        btnMembros.setStyle("-fx-background-color: #0891b2; -fx-text-fill: white; -fx-cursor: hand;");
        btnMembros.setOnAction(e -> generateMembrosReport());

        Button btnLivros = new Button("📚 Catálogo Completo");
        btnLivros.setStyle("-fx-background-color: #7c3aed; -fx-text-fill: white; -fx-cursor: hand;");
        btnLivros.setOnAction(e -> generateLivrosReport());

        reportButtons.getChildren().addAll(btnGeral, btnEmprestimos, btnAtrasados, btnReservas, btnMembros, btnLivros);

        // Área de exibição do relatório
        VBox reportBox = new VBox(10);
        reportBox.setStyle("-fx-background-color: white; -fx-padding: 20; -fx-background-radius: 10; -fx-border-color: #e5e7eb; -fx-border-radius: 10;");

        Label reportTitle = new Label("📄 Relatório");
        reportTitle.setFont(Font.font("System", FontWeight.BOLD, 16));

        reportArea = new TextArea();
        reportArea.setEditable(false);
        reportArea.setWrapText(true);
        reportArea.setPrefHeight(400);
        reportArea.setStyle("-fx-font-family: 'Consolas', monospace; -fx-font-size: 12px;");
        reportArea.setText("Selecione um tipo de relatório acima para gerar.");

        reportBox.getChildren().addAll(reportTitle, reportArea);

        content.getChildren().addAll(title, subtitle, statsCards, reportLabel, reportButtons, reportBox);
        VBox.setVgrow(reportBox, Priority.ALWAYS);

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: #f9fafb;");
        return scrollPane;
    }

    private VBox createStatCard(String icon, String label, String value, String color) {
        VBox card = new VBox(5);
        card.setPadding(new Insets(15));
        card.setAlignment(Pos.CENTER);
        card.setPrefWidth(140);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-border-color: #e5e7eb; -fx-border-radius: 10;");

        Label iconLabel = new Label(icon);
        iconLabel.setFont(Font.font(24));

        Label valueLabel = new Label(value);
        valueLabel.setFont(Font.font("System", FontWeight.BOLD, 20));
        valueLabel.setStyle("-fx-text-fill: " + color + ";");

        Label nameLabel = new Label(label);
        nameLabel.setFont(Font.font("System", 11));
        nameLabel.setStyle("-fx-text-fill: #6b7280;");

        card.getChildren().addAll(iconLabel, valueLabel, nameLabel);
        return card;
    }

    private void generateGeralReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("═══════════════════════════════════════════════════════════════\n");
        sb.append("                    RELATÓRIO GERAL DA BIBLIOTECA\n");
        sb.append("                    Biblioteca ISPTEC - ").append(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))).append("\n");
        sb.append("═══════════════════════════════════════════════════════════════\n\n");

        sb.append("RESUMO ESTATÍSTICO:\n");
        sb.append("───────────────────────────────────────────────────────────────\n");
        sb.append(String.format("  📚 Total de Exemplares:      %d\n", libraryService.getTotalLivros()));
        sb.append(String.format("  📖 Livros Emprestados:       %d\n", libraryService.getLivrosEmprestados()));
        sb.append(String.format("  ⏳ Reservas Pendentes:       %d\n", libraryService.getReservasPendentes()));
        sb.append(String.format("  👥 Membros Ativos:           %d\n", libraryService.getMembrosAtivos()));
        sb.append(String.format("  💰 Total em Multas:          R$ %.2f\n", libraryService.getTotalMultas()));
        sb.append("\n");

        List<Emprestimo> atrasados = libraryService.listarEmprestimosAtrasados();
        sb.append("ALERTAS:\n");
        sb.append("───────────────────────────────────────────────────────────────\n");
        if (atrasados.isEmpty()) {
            sb.append("  ✅ Nenhum empréstimo atrasado!\n");
        } else {
            sb.append(String.format("  ⚠️  %d empréstimo(s) em atraso!\n", atrasados.size()));
        }

        sb.append("\n═══════════════════════════════════════════════════════════════\n");
        sb.append("                         FIM DO RELATÓRIO\n");
        sb.append("═══════════════════════════════════════════════════════════════\n");

        reportArea.setText(sb.toString());
    }

    private void generateEmprestimosReport() {
        List<Emprestimo> emprestimos = libraryService.listarEmprestimosAtivos();
        StringBuilder sb = new StringBuilder();
        sb.append("═══════════════════════════════════════════════════════════════\n");
        sb.append("                    RELATÓRIO DE EMPRÉSTIMOS ATIVOS\n");
        sb.append("═══════════════════════════════════════════════════════════════\n\n");

        if (emprestimos.isEmpty()) {
            sb.append("Nenhum empréstimo ativo no momento.\n");
        } else {
            sb.append(String.format("Total de empréstimos ativos: %d\n\n", emprestimos.size()));
            for (Emprestimo e : emprestimos) {
                sb.append("───────────────────────────────────────────────────────────────\n");
                sb.append(String.format("📖 Livro: %s\n", e.getTituloLivro()));
                sb.append(String.format("👤 Membro: %s\n", e.getNomeMembro()));
                sb.append(String.format("📅 Empréstimo: %s\n", e.getDataEmprestimo()));
                sb.append(String.format("📅 Devolução Prevista: %s\n", e.getDataDevolucaoPrevista()));
                sb.append(String.format("🔄 Renovações: %d/2\n", e.getNumeroRenovacoes()));
                if (e.isAtrasado()) {
                    sb.append(String.format("⚠️  ATRASADO! Multa: R$ %.2f\n", e.getMulta()));
                }
            }
        }

        sb.append("\n═══════════════════════════════════════════════════════════════\n");
        reportArea.setText(sb.toString());
    }

    private void generateAtrasadosReport() {
        List<Emprestimo> atrasados = libraryService.listarEmprestimosAtrasados();
        StringBuilder sb = new StringBuilder();
        sb.append("═══════════════════════════════════════════════════════════════\n");
        sb.append("                    ⚠️ RELATÓRIO DE ATRASOS\n");
        sb.append("═══════════════════════════════════════════════════════════════\n\n");

        if (atrasados.isEmpty()) {
            sb.append("✅ Excelente! Nenhum empréstimo em atraso.\n");
        } else {
            double totalMultas = atrasados.stream().mapToDouble(Emprestimo::getMulta).sum();
            sb.append(String.format("Total de empréstimos atrasados: %d\n", atrasados.size()));
            sb.append(String.format("Total em multas acumuladas: R$ %.2f\n\n", totalMultas));

            for (Emprestimo e : atrasados) {
                sb.append("───────────────────────────────────────────────────────────────\n");
                sb.append(String.format("📖 Livro: %s\n", e.getTituloLivro()));
                sb.append(String.format("👤 Membro: %s\n", e.getNomeMembro()));
                sb.append(String.format("📅 Deveria devolver em: %s\n", e.getDataDevolucaoPrevista()));
                sb.append(String.format("⏰ Dias de atraso: %d\n", e.getDiasAtraso()));
                sb.append(String.format("💰 Multa: R$ %.2f\n", e.getMulta()));
            }
        }

        sb.append("\n═══════════════════════════════════════════════════════════════\n");
        reportArea.setText(sb.toString());
    }

    private void generateReservasReport() {
        List<Reserva> reservas = libraryService.listarReservasPendentes();
        StringBuilder sb = new StringBuilder();
        sb.append("═══════════════════════════════════════════════════════════════\n");
        sb.append("                    RELATÓRIO DE RESERVAS PENDENTES\n");
        sb.append("═══════════════════════════════════════════════════════════════\n\n");

        if (reservas.isEmpty()) {
            sb.append("Nenhuma reserva pendente no momento.\n");
        } else {
            sb.append(String.format("Total de reservas pendentes: %d\n\n", reservas.size()));
            for (Reserva r : reservas) {
                sb.append("───────────────────────────────────────────────────────────────\n");
                sb.append(String.format("📖 Livro: %s\n", r.getTituloLivro()));
                sb.append(String.format("👤 Membro: %s\n", r.getNomeMembro()));
                sb.append(String.format("📅 Data da Reserva: %s\n", r.getDataReserva()));
            }
        }

        sb.append("\n═══════════════════════════════════════════════════════════════\n");
        reportArea.setText(sb.toString());
    }

    private void generateMembrosReport() {
        List<Membro> membros = libraryService.listarMembros();
        StringBuilder sb = new StringBuilder();
        sb.append("═══════════════════════════════════════════════════════════════\n");
        sb.append("                    LISTA DE MEMBROS DA BIBLIOTECA\n");
        sb.append("═══════════════════════════════════════════════════════════════\n\n");

        sb.append(String.format("Total de membros: %d\n\n", membros.size()));

        for (Membro m : membros) {
            sb.append("───────────────────────────────────────────────────────────────\n");
            sb.append(String.format("👤 Nome: %s\n", m.getNome()));
            sb.append(String.format("📧 Email: %s\n", m.getEmail()));
            sb.append(String.format("🎓 Matrícula: %s\n", m.getMatricula()));
            sb.append(String.format("📱 Telefone: %s\n", m.getTelefone()));
            sb.append(String.format("📅 Desde: %s\n", m.getDataIngresso()));
            sb.append(String.format("📊 Status: %s\n", m.isAtivo() ? "✅ Ativo" : "❌ Bloqueado"));
        }

        sb.append("\n═══════════════════════════════════════════════════════════════\n");
        reportArea.setText(sb.toString());
    }

    private void generateLivrosReport() {
        List<Livro> livros = libraryService.listarLivros();
        StringBuilder sb = new StringBuilder();
        sb.append("═══════════════════════════════════════════════════════════════\n");
        sb.append("                    CATÁLOGO COMPLETO DE LIVROS\n");
        sb.append("═══════════════════════════════════════════════════════════════\n\n");

        sb.append(String.format("Total de títulos: %d\n\n", livros.size()));

        for (Livro l : livros) {
            sb.append("───────────────────────────────────────────────────────────────\n");
            sb.append(String.format("📖 Título: %s\n", l.getTitulo()));
            sb.append(String.format("✍️  Autor: %s\n", l.getAutor()));
            sb.append(String.format("🔢 ISBN: %s\n", l.getIsbn()));
            sb.append(String.format("🏢 Editora: %s (%d)\n", l.getEditora(), l.getAno()));
            sb.append(String.format("📂 Categoria: %s\n", l.getCategoria()));
            sb.append(String.format("📚 Exemplares: %d total | %d disponível\n", l.getQuantidade(), l.getQuantidadeDisponivel()));
            sb.append(String.format("📊 Status: %s\n", l.isDisponivel() ? "✅ Disponível" : "❌ Indisponível"));
        }

        sb.append("\n═══════════════════════════════════════════════════════════════\n");
        reportArea.setText(sb.toString());
    }
}
