package com.VOLTSBankApp.controller;

import com.VOLTSBankApp.dao.CustomerDAO;
import com.VOLTSBankApp.model.Customer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class LoginControllerFX {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Button registerButton;

    @FXML
    private Label messageLabel;

    private CustomerDAO customerDAO;

    public void initialize() {
        customerDAO = new CustomerDAO();
        messageLabel.setText("");
        System.out.println("✅ LoginController initialized");
    }

    @FXML
    private void handleLogin() {
        String email = emailField.getText().trim();
        String password = passwordField.getText();

        System.out.println("🔐 Login attempt for: " + email);

        // Clear previous messages
        messageLabel.setText("");

        // Validate inputs
        if (email.isEmpty() || password.isEmpty()) {
            showError("Please enter both email and password");
            return;
        }

        // Validate email format
        if (!email.contains("@") || !email.contains(".")) {
            showError("Please enter a valid email address");
            return;
        }

        try {
            // Authenticate user
            Customer customer = customerDAO.authenticate(email, password);

            if (customer != null) {
                // Load customer accounts
                customerDAO.loadCustomerAccounts(customer);

                System.out.println("✅ Login successful for: " + customer.getFullName());
                System.out.println("   Customer ID: " + customer.getCustomerId());
                System.out.println("   Number of accounts: " + customer.getAccounts().size());

                showSuccess("Login successful! Welcome " + customer.getFullName());

                // Open Dashboard
                openDashboard(customer);

            } else {
                System.out.println("❌ Login failed: Invalid credentials");
                showError("Invalid email or password. Please try again.");
            }

        } catch (Exception e) {
            System.err.println("❌ Login error: " + e.getMessage());
            e.printStackTrace();
            showError("Login failed: " + e.getMessage());
        }
    }

    @FXML
    private void handleRegister() {
        System.out.println("📝 Register button clicked");
        showInfo("Registration",
                "Registration feature coming soon!\n\n" +
                        "For now, use these test accounts:\n\n" +
                        "Email: john.doe@email.com\n" +
                        "Password: password123");
    }

    private void openDashboard(Customer customer) {
        try {
            System.out.println("📂 Loading Dashboard...");

            // Load Dashboard FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Dashboard.fxml"));
            Parent root = loader.load();

            // Get Dashboard controller and set customer
            DashboardControllerFX dashboardController = loader.getController();
            dashboardController.setCustomer(customer);

            // Get current stage and switch scene
            Stage stage = (Stage) loginButton.getScene().getWindow();
            Scene scene = new Scene(root, 900, 650);
            stage.setScene(scene);
            stage.setTitle("VOLTS Bank - Dashboard");

            System.out.println("✅ Dashboard opened successfully");

        } catch (Exception e) {
            System.err.println("❌ Error opening dashboard:");
            e.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Failed to Open Dashboard");
            alert.setContentText("Could not load dashboard.\n\nError: " + e.getMessage());
            alert.showAndWait();
        }
    }

    private void showError(String message) {
        messageLabel.setText("❌ " + message);
        messageLabel.setTextFill(Color.web("#e74c3c"));
        messageLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold;");
    }

    private void showSuccess(String message) {
        messageLabel.setText("✅ " + message);
        messageLabel.setTextFill(Color.web("#27ae60"));
        messageLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold;");
    }

    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}