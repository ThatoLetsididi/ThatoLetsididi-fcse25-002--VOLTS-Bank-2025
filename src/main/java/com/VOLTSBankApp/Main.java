package com.VOLTSBankApp;

import com.VOLTSBankApp.util.DatabaseConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Test database connection
            System.out.println("🔄 Testing database connection...");
            if (DatabaseConnection.testConnection()) {
                System.out.println("✅ Database connected successfully!");
            } else {
                System.err.println("❌ Database connection failed!");
                showErrorAndExit("Database connection failed. Please start XAMPP MySQL.");
                return;
            }

            // Load Login screen
            System.out.println("📂 Loading FXML...");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Login.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root, 600, 550);

            primaryStage.setTitle("VOLTS Bank - Login");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();

            System.out.println("✅ Application started successfully!");

        } catch (Exception e) {
            System.err.println("❌ Error:");
            e.printStackTrace();
            showErrorAndExit("Failed to load application:\n" + e.getMessage());
        }
    }

    private void showErrorAndExit(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Application Error");
        alert.setContentText(message);
        alert.showAndWait();
        System.exit(1);
    }

    @Override
    public void stop() {
        DatabaseConnection.closeConnection();
        System.out.println("Application closed.");
    }

    static void main(String[] args) {
        launch(args);
    }
}