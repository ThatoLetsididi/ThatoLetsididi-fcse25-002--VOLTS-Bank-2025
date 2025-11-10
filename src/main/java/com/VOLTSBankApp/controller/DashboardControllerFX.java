package com.VOLTSBankApp.controller;

import com.VOLTSBankApp.dao.AccountDAO;
import com.VOLTSBankApp.model.Account;
import com.VOLTSBankApp.model.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class DashboardControllerFX {

    @FXML private Label welcomeLabel;
    @FXML private Label totalBalanceLabel;
    @FXML private ListView<String> accountListView;
    @FXML private Button createAccountButton;
    @FXML private Button viewAccountButton;
    @FXML private Button depositButton;
    @FXML private Button withdrawButton;
    @FXML private Button payInterestButton;
    @FXML private Button logoutButton;

    private Customer customer;
    private AccountDAO accountDAO;

    public void initialize() {
        accountDAO = new AccountDAO();
        System.out.println("✅ DashboardController initialized");
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
        welcomeLabel.setText("Welcome, " + customer.getFullName());
        refreshDashboard();
    }

    public void refreshDashboard() {
        System.out.println("🔄 Refreshing dashboard...");

        // Reload accounts from database
        accountDAO.loadAccountsForCustomer(customer);

        ObservableList<String> accountItems = FXCollections.observableArrayList();
        double totalBalance = 0;

        if (customer.getAccounts().isEmpty()) {
            accountListView.setPlaceholder(
                    new Label("No accounts yet. Click 'Create Account' to get started!")
            );
        } else {
            for (Account account : customer.getAccounts()) {
                String displayText = String.format("%s - %s | BWP %.2f",
                        account.getAccountType().getDisplayName(),
                        account.getAccountNumber(),
                        account.getBalance());
                accountItems.add(displayText);
                totalBalance += account.getBalance();
            }
        }

        accountListView.setItems(accountItems);
        totalBalanceLabel.setText(String.format("BWP %.2f", totalBalance));

        System.out.println("✅ Dashboard refreshed");
        System.out.println("   Total accounts: " + customer.getAccounts().size());
        System.out.println("   Total balance: BWP " + String.format("%.2f", totalBalance));
    }

    @FXML
    private void handleCreateAccount() {
        System.out.println("➕ Create Account clicked");
        showInfo("Create Account",
                "Create Account feature coming next!\n\n" +
                        "This will allow you to create:\n" +
                        "• Savings Account (0.05% monthly interest)\n" +
                        "• Investment Account (5% monthly interest, min BWP 500)\n" +
                        "• Cheque Account (requires employment details)");
    }

    @FXML
    private void handleViewAccount() {
        String selected = accountListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarning("No Selection", "Please select an account to view");
            return;
        }

        String accountNumber = extractAccountNumber(selected);
        Account account = findAccountByNumber(accountNumber);

        if (account != null) {
            showAccountDetails(account);
        }
    }

    @FXML
    private void handleDeposit() {
        if (customer.getAccounts().isEmpty()) {
            showWarning("No Accounts",
                    "You don't have any accounts yet.\nPlease create an account first.");
            return;
        }

        System.out.println("💰 Deposit clicked");
        showInfo("Deposit", "Deposit feature coming soon!");
    }

    @FXML
    private void handleWithdraw() {
        if (customer.getAccounts().isEmpty()) {
            showWarning("No Accounts",
                    "You don't have any accounts yet.\nPlease create an account first.");
            return;
        }

        System.out.println("💸 Withdraw clicked");
        showInfo("Withdraw", "Withdrawal feature coming soon!");
    }

    @FXML
    private void handlePayInterest() {
        if (customer.getAccounts().isEmpty()) {
            showWarning("No Accounts",
                    "You don't have any accounts yet.\nPlease create an account first.");
            return;
        }

        System.out.println("📈 Pay Interest clicked");
        showInfo("Pay Interest", "Interest payment feature coming soon!");
    }

    @FXML
    private void handleLogout() {
        System.out.println("🚪 Logout clicked");

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("Are you sure you want to logout?");
        alert.setContentText("You will be returned to the login screen.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Login.fxml"));
                    Parent root = loader.load();

                    Stage stage = (Stage) logoutButton.getScene().getWindow();
                    stage.setScene(new Scene(root, 600, 550));
                    stage.setTitle("VOLTS Bank - Login");

                    System.out.println("✅ Logged out successfully");
                } catch (IOException e) {
                    e.printStackTrace();
                    showError("Error", "Failed to return to login screen");
                }
            }
        });
    }

    private void showAccountDetails(Account account) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Account Details");
        alert.setHeaderText(account.getAccountType().getDisplayName());

        String details = String.format(
                "Account Number: %s\n" +
                        "Balance: BWP %.2f\n" +
                        "Branch: %s\n" +
                        "Date Opened: %s\n\n" +
                        "%s",
                account.getAccountNumber(),
                account.getBalance(),
                account.getBranch(),
                account.getDateOpened() != null ?
                        account.getDateOpened().toString() : "N/A",
                account.getAccountDetails()
        );

        alert.setContentText(details);
        alert.showAndWait();
    }

    private String extractAccountNumber(String listItem) {
        // Format: "Account Type - ACC123 | BWP 1000.00"
        int start = listItem.indexOf(" - ") + 3;
        int end = listItem.indexOf(" | ");
        return listItem.substring(start, end);
    }

    private Account findAccountByNumber(String accountNumber) {
        for (Account account : customer.getAccounts()) {
            if (account.getAccountNumber().equals(accountNumber)) {
                return account;
            }
        }
        return null;
    }

    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showWarning(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}