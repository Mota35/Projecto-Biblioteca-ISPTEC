package isptec.biblioteca.views;

import isptec.biblioteca.ServiceFactory;
import isptec.biblioteca.service.AuthService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 * View de Configurações do sistema com design moderno
 */
public class ConfiguracoesView {

    private final Stage stage;
    private final BorderPane mainLayout;
    private final AuthService authService;

    public ConfiguracoesView(Stage stage, BorderPane mainLayout) {
        this.stage = stage;
        this.mainLayout = mainLayout;
        this.authService = ServiceFactory.getInstance().getAuthService();
    }

    public void show() {
        ScrollPane scrollPane = new ScrollPane(createContent());
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: #f8fafc;");
        mainLayout.setCenter(scrollPane);
    }

    private VBox createContent() {
        VBox content = new VBox(30);
        content.setPadding(new Insets(30));
        content.setStyle("-fx-background-color: #f8fafc;");

        // Header
        VBox header = createHeader();

        // Seções de configuração
        VBox perfilSection = createPerfilSection();
        VBox aparenciaSection = createAparenciaSection();
        VBox notificacoesSection = createNotificacoesSection();
        VBox privacidadeSection = createPrivacidadeSection();
        VBox sistemaSection = createSistemaSection();

        content.getChildren().addAll(header, perfilSection, aparenciaSection,
            notificacoesSection, privacidadeSection, sistemaSection);

        return content;
    }

    private VBox createHeader() {
        VBox header = new VBox(5);

        Label title = new Label("⚙️ Configurações");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 32));
        title.setTextFill(Color.web("#1e293b"));

        Label subtitle = new Label("Personalize sua experiência na biblioteca");
        subtitle.setFont(Font.font("Segoe UI", 14));
        subtitle.setTextFill(Color.web("#64748b"));

        header.getChildren().addAll(title, subtitle);

        return header;
    }

    private VBox createPerfilSection() {
        VBox section = createSectionCard("👤 Perfil", "Gerencie suas informações pessoais");
        VBox content = (VBox) section.getChildren().get(1);

        // Avatar
        HBox avatarRow = new HBox(20);
        avatarRow.setAlignment(Pos.CENTER_LEFT);

        StackPane avatarPane = new StackPane();
        avatarPane.setMinSize(80, 80);
        avatarPane.setMaxSize(80, 80);
        avatarPane.setStyle(
            "-fx-background-color: linear-gradient(to bottom right, #6366f1, #8b5cf6); " +
            "-fx-background-radius: 40;"
        );

        Label avatarLabel = new Label("👤");
        avatarLabel.setStyle("-fx-font-size: 36px;");
        avatarPane.getChildren().add(avatarLabel);

        VBox avatarInfo = new VBox(5);
        Label nameLabel = new Label(authService.getUsuarioLogado().getNome());
        nameLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        nameLabel.setTextFill(Color.web("#1e293b"));

        Label emailLabel = new Label(authService.getUsuarioLogado().getEmail());
        emailLabel.setFont(Font.font("Segoe UI", 13));
        emailLabel.setTextFill(Color.web("#64748b"));

        Button changeAvatarBtn = new Button("Alterar foto");
        changeAvatarBtn.setStyle(
            "-fx-background-color: #f1f5f9; " +
            "-fx-text-fill: #6366f1; " +
            "-fx-font-size: 12; " +
            "-fx-padding: 6 12; " +
            "-fx-background-radius: 6; " +
            "-fx-cursor: hand;"
        );

        avatarInfo.getChildren().addAll(nameLabel, emailLabel, changeAvatarBtn);
        avatarRow.getChildren().addAll(avatarPane, avatarInfo);

        Separator sep = new Separator();
        sep.setPadding(new Insets(10, 0, 10, 0));

        // Campos editáveis
        HBox fieldsRow = new HBox(20);

        VBox nomeField = createFormField("Nome completo", authService.getUsuarioLogado().getNome());
        VBox emailField = createFormField("Email", authService.getUsuarioLogado().getEmail());

        HBox.setHgrow(nomeField, Priority.ALWAYS);
        HBox.setHgrow(emailField, Priority.ALWAYS);

        fieldsRow.getChildren().addAll(nomeField, emailField);

        Button saveBtn = createPrimaryButton("💾 Guardar alterações");

        content.getChildren().addAll(avatarRow, sep, fieldsRow, saveBtn);

        return section;
    }

    private VBox createFormField(String label, String value) {
        VBox field = new VBox(8);

        Label labelText = new Label(label);
        labelText.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 13));
        labelText.setTextFill(Color.web("#374151"));

        TextField textField = new TextField(value);
        textField.setPrefHeight(45);
        textField.setStyle(
            "-fx-background-color: #f8fafc; " +
            "-fx-border-color: #e2e8f0; " +
            "-fx-border-radius: 10; " +
            "-fx-background-radius: 10; " +
            "-fx-font-size: 14; " +
            "-fx-padding: 0 15;"
        );
        textField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                textField.setStyle(
                    "-fx-background-color: white; " +
                    "-fx-border-color: #6366f1; " +
                    "-fx-border-width: 2; " +
                    "-fx-border-radius: 10; " +
                    "-fx-background-radius: 10; " +
                    "-fx-font-size: 14; " +
                    "-fx-padding: 0 15;"
                );
            } else {
                textField.setStyle(
                    "-fx-background-color: #f8fafc; " +
                    "-fx-border-color: #e2e8f0; " +
                    "-fx-border-radius: 10; " +
                    "-fx-background-radius: 10; " +
                    "-fx-font-size: 14; " +
                    "-fx-padding: 0 15;"
                );
            }
        });

        field.getChildren().addAll(labelText, textField);

        return field;
    }

    private VBox createAparenciaSection() {
        VBox section = createSectionCard("🎨 Aparência", "Personalize a interface do sistema");
        VBox content = (VBox) section.getChildren().get(1);

        // Tema
        HBox temaRow = createSettingRow(
            "Tema da interface",
            "Escolha entre tema claro ou escuro",
            createThemeToggle()
        );

        // Tamanho da fonte
        HBox fonteRow = createSettingRow(
            "Tamanho da fonte",
            "Ajuste o tamanho do texto",
            createFontSizeSelector()
        );

        // Cor de destaque
        HBox corRow = createSettingRow(
            "Cor de destaque",
            "Escolha a cor principal do sistema",
            createColorSelector()
        );

        content.getChildren().addAll(temaRow, new Separator(), fonteRow, new Separator(), corRow);

        return section;
    }

    private HBox createThemeToggle() {
        HBox toggle = new HBox(10);
        toggle.setAlignment(Pos.CENTER_RIGHT);

        ToggleGroup group = new ToggleGroup();

        ToggleButton lightBtn = new ToggleButton("☀️ Claro");
        lightBtn.setToggleGroup(group);
        lightBtn.setSelected(true);
        styleToggleButton(lightBtn, true);

        ToggleButton darkBtn = new ToggleButton("🌙 Escuro");
        darkBtn.setToggleGroup(group);
        styleToggleButton(darkBtn, false);

        lightBtn.selectedProperty().addListener((obs, oldVal, newVal) -> {
            styleToggleButton(lightBtn, newVal);
        });

        darkBtn.selectedProperty().addListener((obs, oldVal, newVal) -> {
            styleToggleButton(darkBtn, newVal);
        });

        toggle.getChildren().addAll(lightBtn, darkBtn);

        return toggle;
    }

    private void styleToggleButton(ToggleButton btn, boolean selected) {
        btn.setStyle(
            "-fx-background-color: " + (selected ? "#6366f1" : "white") + "; " +
            "-fx-text-fill: " + (selected ? "white" : "#64748b") + "; " +
            "-fx-font-size: 12; " +
            "-fx-padding: 8 16; " +
            "-fx-background-radius: 8; " +
            "-fx-border-color: " + (selected ? "#6366f1" : "#e2e8f0") + "; " +
            "-fx-border-radius: 8; " +
            "-fx-cursor: hand;"
        );
    }

    private HBox createFontSizeSelector() {
        HBox selector = new HBox(10);
        selector.setAlignment(Pos.CENTER_RIGHT);

        ComboBox<String> combo = new ComboBox<>();
        combo.getItems().addAll("Pequeno", "Normal", "Grande", "Extra Grande");
        combo.setValue("Normal");
        combo.setStyle(
            "-fx-background-color: white; " +
            "-fx-border-color: #e2e8f0; " +
            "-fx-border-radius: 8; " +
            "-fx-background-radius: 8;"
        );

        selector.getChildren().add(combo);

        return selector;
    }

    private HBox createColorSelector() {
        HBox selector = new HBox(10);
        selector.setAlignment(Pos.CENTER_RIGHT);

        String[] colors = {"#6366f1", "#8b5cf6", "#ec4899", "#14b8a6", "#f59e0b", "#ef4444"};

        for (String color : colors) {
            Button colorBtn = new Button();
            colorBtn.setMinSize(32, 32);
            colorBtn.setMaxSize(32, 32);
            colorBtn.setStyle(
                "-fx-background-color: " + color + "; " +
                "-fx-background-radius: 16; " +
                "-fx-cursor: hand; " +
                "-fx-border-color: transparent; " +
                "-fx-border-radius: 16; " +
                "-fx-border-width: 3;"
            );
            colorBtn.setOnMouseEntered(e -> colorBtn.setStyle(
                "-fx-background-color: " + color + "; " +
                "-fx-background-radius: 16; " +
                "-fx-cursor: hand; " +
                "-fx-border-color: white; " +
                "-fx-border-radius: 16; " +
                "-fx-border-width: 3; " +
                "-fx-effect: dropshadow(gaussian, " + color + ", 10, 0.5, 0, 0);"
            ));
            colorBtn.setOnMouseExited(e -> colorBtn.setStyle(
                "-fx-background-color: " + color + "; " +
                "-fx-background-radius: 16; " +
                "-fx-cursor: hand; " +
                "-fx-border-color: transparent; " +
                "-fx-border-radius: 16; " +
                "-fx-border-width: 3;"
            ));
            selector.getChildren().add(colorBtn);
        }

        return selector;
    }

    private VBox createNotificacoesSection() {
        VBox section = createSectionCard("🔔 Notificações", "Configure como receber alertas");
        VBox content = (VBox) section.getChildren().get(1);

        HBox emailNotif = createSwitchRow("Notificações por email", "Receba alertas no seu email", true);
        HBox pushNotif = createSwitchRow("Notificações push", "Alertas no navegador", true);
        HBox emprestimosNotif = createSwitchRow("Lembretes de devolução", "Aviso 3 dias antes do vencimento", true);
        HBox reservasNotif = createSwitchRow("Atualizações de reservas", "Quando um livro reservado estiver disponível", true);
        HBox newsletterNotif = createSwitchRow("Newsletter", "Novidades e eventos da biblioteca", false);

        content.getChildren().addAll(emailNotif, new Separator(), pushNotif, new Separator(),
            emprestimosNotif, new Separator(), reservasNotif, new Separator(), newsletterNotif);

        return section;
    }

    private HBox createSwitchRow(String title, String description, boolean enabled) {
        HBox row = new HBox();
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(5, 0, 5, 0));

        VBox textBox = new VBox(3);
        HBox.setHgrow(textBox, Priority.ALWAYS);

        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 14));
        titleLabel.setTextFill(Color.web("#1e293b"));

        Label descLabel = new Label(description);
        descLabel.setFont(Font.font("Segoe UI", 12));
        descLabel.setTextFill(Color.web("#64748b"));

        textBox.getChildren().addAll(titleLabel, descLabel);

        // Toggle Switch customizado
        CheckBox toggle = new CheckBox();
        toggle.setSelected(enabled);
        toggle.setStyle("-fx-font-size: 14;");

        row.getChildren().addAll(textBox, toggle);

        return row;
    }

    private VBox createPrivacidadeSection() {
        VBox section = createSectionCard("🔒 Privacidade e Segurança", "Proteja sua conta");
        VBox content = (VBox) section.getChildren().get(1);

        Button changePasswordBtn = createSecondaryButton("🔑 Alterar senha");
        changePasswordBtn.setOnAction(e -> TrocarSenhaDialog.mostrar());

        Button twoFactorBtn = createSecondaryButton("📱 Ativar autenticação em dois fatores");

        Button exportDataBtn = createSecondaryButton("📥 Exportar meus dados");

        Button deleteAccountBtn = new Button("🗑️ Eliminar conta");
        deleteAccountBtn.setPrefWidth(250);
        deleteAccountBtn.setPrefHeight(40);
        deleteAccountBtn.setStyle(
            "-fx-background-color: white; " +
            "-fx-text-fill: #ef4444; " +
            "-fx-font-size: 13; " +
            "-fx-background-radius: 8; " +
            "-fx-border-color: #ef4444; " +
            "-fx-border-radius: 8; " +
            "-fx-cursor: hand;"
        );
        deleteAccountBtn.setOnMouseEntered(e -> deleteAccountBtn.setStyle(
            "-fx-background-color: #fef2f2; " +
            "-fx-text-fill: #ef4444; " +
            "-fx-font-size: 13; " +
            "-fx-background-radius: 8; " +
            "-fx-border-color: #ef4444; " +
            "-fx-border-radius: 8; " +
            "-fx-cursor: hand;"
        ));
        deleteAccountBtn.setOnMouseExited(e -> deleteAccountBtn.setStyle(
            "-fx-background-color: white; " +
            "-fx-text-fill: #ef4444; " +
            "-fx-font-size: 13; " +
            "-fx-background-radius: 8; " +
            "-fx-border-color: #ef4444; " +
            "-fx-border-radius: 8; " +
            "-fx-cursor: hand;"
        ));

        content.getChildren().addAll(changePasswordBtn, twoFactorBtn, exportDataBtn, deleteAccountBtn);

        return section;
    }

    private VBox createSistemaSection() {
        VBox section = createSectionCard("💻 Sistema", "Informações e opções do sistema");
        VBox content = (VBox) section.getChildren().get(1);

        HBox versionRow = createInfoRow("Versão", "1.0.0");
        HBox lastUpdateRow = createInfoRow("Última atualização", "10/02/2025");
        HBox storageRow = createInfoRow("Armazenamento usado", "45.2 MB");

        Button clearCacheBtn = createSecondaryButton("🧹 Limpar cache");
        Button checkUpdatesBtn = createSecondaryButton("🔄 Verificar atualizações");

        content.getChildren().addAll(versionRow, new Separator(), lastUpdateRow, new Separator(),
            storageRow, new Separator(), clearCacheBtn, checkUpdatesBtn);

        return section;
    }

    private HBox createInfoRow(String label, String value) {
        HBox row = new HBox();
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(5, 0, 5, 0));

        Label labelText = new Label(label);
        labelText.setFont(Font.font("Segoe UI", 14));
        labelText.setTextFill(Color.web("#64748b"));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label valueText = new Label(value);
        valueText.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 14));
        valueText.setTextFill(Color.web("#1e293b"));

        row.getChildren().addAll(labelText, spacer, valueText);

        return row;
    }

    private VBox createSectionCard(String title, String description) {
        VBox card = new VBox(20);
        card.setPadding(new Insets(25));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 16;");

        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.web("#000000", 0.05));
        shadow.setRadius(15);
        shadow.setOffsetY(5);
        card.setEffect(shadow);

        VBox headerBox = new VBox(5);
        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        titleLabel.setTextFill(Color.web("#1e293b"));

        Label descLabel = new Label(description);
        descLabel.setFont(Font.font("Segoe UI", 13));
        descLabel.setTextFill(Color.web("#64748b"));

        headerBox.getChildren().addAll(titleLabel, descLabel);

        VBox contentBox = new VBox(15);

        card.getChildren().addAll(headerBox, contentBox);

        return card;
    }

    private HBox createSettingRow(String title, String description, HBox control) {
        HBox row = new HBox();
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(10, 0, 10, 0));

        VBox textBox = new VBox(3);
        HBox.setHgrow(textBox, Priority.ALWAYS);

        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 14));
        titleLabel.setTextFill(Color.web("#1e293b"));

        Label descLabel = new Label(description);
        descLabel.setFont(Font.font("Segoe UI", 12));
        descLabel.setTextFill(Color.web("#64748b"));

        textBox.getChildren().addAll(titleLabel, descLabel);

        row.getChildren().addAll(textBox, control);

        return row;
    }

    private Button createPrimaryButton(String text) {
        Button btn = new Button(text);
        btn.setPrefHeight(45);
        btn.setStyle(
            "-fx-background-color: linear-gradient(to right, #6366f1, #8b5cf6); " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 14; " +
            "-fx-font-weight: bold; " +
            "-fx-background-radius: 10; " +
            "-fx-cursor: hand;"
        );

        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.web("#6366f1", 0.3));
        shadow.setRadius(10);
        shadow.setOffsetY(4);
        btn.setEffect(shadow);

        btn.setOnMouseEntered(e -> btn.setStyle(
            "-fx-background-color: linear-gradient(to right, #4f46e5, #7c3aed); " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 14; " +
            "-fx-font-weight: bold; " +
            "-fx-background-radius: 10; " +
            "-fx-cursor: hand;"
        ));
        btn.setOnMouseExited(e -> btn.setStyle(
            "-fx-background-color: linear-gradient(to right, #6366f1, #8b5cf6); " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 14; " +
            "-fx-font-weight: bold; " +
            "-fx-background-radius: 10; " +
            "-fx-cursor: hand;"
        ));

        return btn;
    }

    private Button createSecondaryButton(String text) {
        Button btn = new Button(text);
        btn.setPrefWidth(250);
        btn.setPrefHeight(40);
        btn.setStyle(
            "-fx-background-color: #f1f5f9; " +
            "-fx-text-fill: #475569; " +
            "-fx-font-size: 13; " +
            "-fx-background-radius: 8; " +
            "-fx-cursor: hand;"
        );
        btn.setOnMouseEntered(e -> btn.setStyle(
            "-fx-background-color: #e2e8f0; " +
            "-fx-text-fill: #475569; " +
            "-fx-font-size: 13; " +
            "-fx-background-radius: 8; " +
            "-fx-cursor: hand;"
        ));
        btn.setOnMouseExited(e -> btn.setStyle(
            "-fx-background-color: #f1f5f9; " +
            "-fx-text-fill: #475569; " +
            "-fx-font-size: 13; " +
            "-fx-background-radius: 8; " +
            "-fx-cursor: hand;"
        ));

        return btn;
    }
}

