package isptec.biblioteca.util;

import javafx.geometry.Pos;
import javafx.scene.control.Button;

public class MenuHelper {
    
    public static Button createMenuButton(String text, boolean active) {
        Button btn = new Button(text);
        btn.setPrefWidth(Double.MAX_VALUE);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setPrefHeight(40);
        
        if (active) {
            applyActiveStyle(btn);
        } else {
            applyInactiveStyle(btn);
        }
        
        return btn;
    }
    
    public static void applyActiveStyle(Button btn) {
        btn.setStyle(
            "-fx-background-color: #2563eb; " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 14; " +
            "-fx-background-radius: 5; " +
            "-fx-cursor: hand;"
        );
    }
    
    public static void applyInactiveStyle(Button btn) {
        btn.setStyle(
            "-fx-background-color: transparent; " +
            "-fx-text-fill: #374151; " +
            "-fx-font-size: 14; " +
            "-fx-cursor: hand;"
        );
        
        btn.setOnMouseEntered(e -> {
            if (!btn.getStyle().contains("#2563eb")) {
                btn.setStyle(
                    "-fx-background-color: #f3f4f6; " +
                    "-fx-text-fill: #374151; " +
                    "-fx-font-size: 14; " +
                    "-fx-background-radius: 5; " +
                    "-fx-cursor: hand;"
                );
            }
        });
        
        btn.setOnMouseExited(e -> {
            if (!btn.getStyle().contains("#2563eb")) {
                btn.setStyle(
                    "-fx-background-color: transparent; " +
                    "-fx-text-fill: #374151; " +
                    "-fx-font-size: 14; " +
                    "-fx-cursor: hand;"
                );
            }
        });
    }
}
