package com.VOLTSBankApp.controller;

import com.VOLTSBankApp.dao.AccountDAO;
import com.VOLTSBankApp.dao.TransactionDAO;
import com.VOLTSBankApp.model.*;
import com.VOLTSBankApp.view.TransactionView;
import com.VOLTSBankApp.interfaces.Withdrawable;

import java.util.List;
import java.util.Scanner;

public class TransactionController {
    private TransactionDAO transactionDAO;
    private AccountDAO accountDAO;
    private TransactionView transactionView;
    private Scanner scanner;

    public TransactionController() {
        this.transactionDAO = new TransactionDAO();
        this.accountDAO = new AccountDAO();
        this.transactionView = new TransactionView();
        this.scanner = new Scanner(System.in);
    }

    /**
     * Display transaction menu
     */
    public void showTransactionMenu() {
        boolean running = true;

        while (running) {
            transactionView.displayMenu();
            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    handleDeposit();
                    break;
                case 2:
                    handleWithdrawal();
                    break;
                case 3:
                    handleViewTransactions();
                    break;
                case 4:
                    handleTransactionReport();
                    break;
                case 5:
                    running = false;
                    System.out.println("Returning to main menu...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    /**
     * Handle deposit funds
     */
    private void handleDeposit() {
        System.out.println("\n=== Deposit Funds ===");

        System.out.print("Enter Account Number: ");
        String accountNumber = scanner.nextLine();

        Account account = accountDAO.getAccountByNumber(accountNumber);

        if (account == null) {
            System.out.println("ERROR: Account not found!");
            return;
        }

        System.out.println("Account: " + account.getAccountNumber());
        System.out.println("Type: " + account.getAccountType().getDisplayName());
        System.out.println("Current Balance: BWP " + String.format("%.2f", account.getBalance()));

        double amount = getDoubleInput("\nEnter deposit amount (BWP): ");

        if (amount <= 0) {
            System.out.println("ERROR: Deposit amount must be positive!");
            return;
        }

        System.out.print("Description (optional): ");
        String description = scanner.nextLine();
        if (description.isEmpty()) {
            description = "Cash deposit";
        }

        // Perform deposit
        double oldBalance = account.getBalance();
        if (account.deposit(amount)) {
            double newBalance = account.getBalance();

            // Update database
            if (accountDAO.updateAccountBalance(accountNumber, newBalance)) {
                // Record transaction
                Transaction transaction = new Transaction(
                        accountNumber,
                        "DEPOSIT",
                        amount,
                        newBalance,
                        description
                );

                if (transactionDAO.createTransaction(transaction)) {
                    System.out.println("\n✓ SUCCESS: Deposit completed successfully!");
                    System.out.println("Amount Deposited: BWP " + String.format("%.2f", amount));
                    System.out.println("Previous Balance: BWP " + String.format("%.2f", oldBalance));
                    System.out.println("New Balance: BWP " + String.format("%.2f", newBalance));
                    System.out.println("Transaction ID: " + transaction.getTransactionId());
                } else {
                    System.out.println("WARNING: Deposit successful but transaction record failed!");
                }
            } else {
                System.out.println("ERROR: Failed to update account balance!");
            }
        } else {
            System.out.println("ERROR: Deposit failed!");
        }
    }

    /**
     * Handle withdrawal funds
     */
    private void handleWithdrawal() {
        System.out.println("\n=== Withdraw Funds ===");

        System.out.print("Enter Account Number: ");
        String accountNumber = scanner.nextLine();

        Account account = accountDAO.getAccountByNumber(accountNumber);

        if (account == null) {
            System.out.println("ERROR: Account not found!");
            return;
        }

        System.out.println("Account: " + account.getAccountNumber());
        System.out.println("Type: " + account.getAccountType().getDisplayName());
        System.out.println("Current Balance: BWP " + String.format("%.2f", account.getBalance()));

        // Check if account supports withdrawals
        if (!(account instanceof Withdrawable)) {
            System.out.println("\nERROR: This account type does not allow withdrawals!");
            System.out.println(account.getAccountType().getDisplayName() + " accounts are deposit-only.");
            return;
        }

        Withdrawable withdrawableAccount = (Withdrawable) account;
        System.out.println("Available Balance: BWP " +
                String.format("%.2f", withdrawableAccount.getAvailableBalance()));

        double amount = getDoubleInput("\nEnter withdrawal amount (BWP): ");

        if (amount <= 0) {
            System.out.println("ERROR: Withdrawal amount must be positive!");
            return;
        }

        if (amount > withdrawableAccount.getAvailableBalance()) {
            System.out.println("ERROR: Insufficient funds!");
            System.out.println("Available: BWP " +
                    String.format("%.2f", withdrawableAccount.getAvailableBalance()));
            return;
        }

        System.out.print("Description (optional): ");
        String description = scanner.nextLine();
        if (description.isEmpty()) {
            description = "Cash withdrawal";
        }

        // Confirm withdrawal
        System.out.print("\nConfirm withdrawal of BWP " + String.format("%.2f", amount) + "? (yes/no): ");
        String confirmation = scanner.nextLine();

        if (!confirmation.equalsIgnoreCase("yes")) {
            System.out.println("Withdrawal cancelled.");
            return;
        }

        // Perform withdrawal
        double oldBalance = account.getBalance();
        if (withdrawableAccount.withdraw(amount)) {
            double newBalance = account.getBalance();

            // Update database
            if (accountDAO.updateAccountBalance(accountNumber, newBalance)) {
                // Record transaction
                Transaction transaction = new Transaction(
                        accountNumber,
                        "WITHDRAWAL",
                        amount,
                        newBalance,
                        description
                );

                if (transactionDAO.createTransaction(transaction)) {
                    System.out.println("\n✓ SUCCESS: Withdrawal completed successfully!");
                    System.out.println("Amount Withdrawn: BWP " + String.format("%.2f", amount));
                    System.out.println("Previous Balance: BWP " + String.format("%.2f", oldBalance));
                    System.out.println("New Balance: BWP " + String.format("%.2f", newBalance));
                    System.out.println("Transaction ID: " + transaction.getTransactionId());
                } else {
                    System.out.println("WARNING: Withdrawal successful but transaction record failed!");
                }
            } else {
                System.out.println("ERROR: Failed to update account balance!");
            }
        } else {
            System.out.println("ERROR: Withdrawal failed!");
        }
    }

    /**
     * Handle view transactions
     */
    private void handleViewTransactions() {
        System.out.println("\n=== View Transactions ===");

        System.out.print("Enter Account Number: ");
        String accountNumber = scanner.nextLine();

        Account account = accountDAO.getAccountByNumber(accountNumber);

        if (account == null) {
            System.out.println("ERROR: Account not found!");
            return;
        }

        System.out.println("\nAccount: " + account.getAccountNumber());
        System.out.println("Type: " + account.getAccountType().getDisplayName());
        System.out.println("Current Balance: BWP " + String.format("%.2f", account.getBalance()));

        System.out.println("\nSelect option:");
        System.out.println("1. View All Transactions");
        System.out.println("2. View Recent Transactions (Last 10)");
        System.out.println("3. View by Transaction Type");

        int option = getIntInput("Enter choice: ");

        List<Transaction> transactions = null;

        switch (option) {
            case 1:
                transactions = transactionDAO.getTransactionsByAccount(accountNumber);
                break;
            case 2:
                transactions = transactionDAO.getRecentTransactions(accountNumber, 10);
                break;
            case 3:
                System.out.print("Enter transaction type (DEPOSIT/WITHDRAWAL/INTEREST/OPENING): ");
                String type = scanner.nextLine().toUpperCase();
                transactions = transactionDAO.getTransactionsByAccount(accountNumber);
                // Filter by type
                transactions.removeIf(t -> !t.getTransactionType().equals(type));
                break;
            default:
                System.out.println("Invalid option!");
                return;
        }

        if (transactions == null || transactions.isEmpty()) {
            System.out.println("\nNo transactions found.");
        } else {
            System.out.println("\n--- Transactions ---");
            transactionView.displayTransactionList(transactions);
        }
    }

    /**
     * Handle transaction report
     */
    private void handleTransactionReport() {
        System.out.println("\n=== Transaction Report ===");

        System.out.print("Enter Account Number: ");
        String accountNumber = scanner.nextLine();

        Account account = accountDAO.getAccountByNumber(accountNumber);

        if (account == null) {
            System.out.println("ERROR: Account not found!");
            return;
        }

        List<Transaction> transactions = transactionDAO.getTransactionsByAccount(accountNumber);

        if (transactions.isEmpty()) {
            System.out.println("\nNo transactions found for this account.");
            return;
        }

        // Calculate statistics
        int totalTransactions = transactions.size();
        double totalDeposits = 0;
        double totalWithdrawals = 0;
        double totalInterest = 0;
        int depositCount = 0;
        int withdrawalCount = 0;
        int interestCount = 0;

        for (Transaction t : transactions) {
            switch (t.getTransactionType()) {
                case "DEPOSIT":
                case "OPENING":
                    totalDeposits += t.getAmount();
                    depositCount++;
                    break;
                case "WITHDRAWAL":
                    totalWithdrawals += t.getAmount();
                    withdrawalCount++;
                    break;
                case "INTEREST":
                    totalInterest += t.getAmount();
                    interestCount++;
                    break;
            }
        }

        // Display report
        System.out.println("\n╔════════════════════════════════════════════════════╗");
        System.out.println("║           TRANSACTION REPORT                       ║");
        System.out.println("╠════════════════════════════════════════════════════╣");
        System.out.printf("║ Account Number    : %-30s ║%n", accountNumber);
        System.out.printf("║ Account Type      : %-30s ║%n", account.getAccountType().getDisplayName());
        System.out.printf("║ Current Balance   : BWP %-25s ║%n", String.format("%.2f", account.getBalance()));
        System.out.println("╠════════════════════════════════════════════════════╣");
        System.out.printf("║ Total Transactions: %-30d ║%n", totalTransactions);
        System.out.println("╠════════════════════════════════════════════════════╣");
        System.out.printf("║ Deposits          : %-5d (BWP %16s) ║%n",
                depositCount, String.format("%.2f", totalDeposits));
        System.out.printf("║ Withdrawals       : %-5d (BWP %16s) ║%n",
                withdrawalCount, String.format("%.2f", totalWithdrawals));
        System.out.printf("║ Interest Payments : %-5d (BWP %16s) ║%n",
                interestCount, String.format("%.2f", totalInterest));
        System.out.println("╠════════════════════════════════════════════════════╣");

        double netChange = totalDeposits + totalInterest - totalWithdrawals;
        System.out.printf("║ Net Change        : BWP %-26s ║%n", String.format("%.2f", netChange));
        System.out.println("╚════════════════════════════════════════════════════╝");

        // Show recent transactions
        System.out.println("\n--- Recent Transactions (Last 5) ---");
        List<Transaction> recentTransactions = transactionDAO.getRecentTransactions(accountNumber, 5);
        transactionView.displayTransactionList(recentTransactions);
    }

    /**
     * Get integer input with validation
     */
    private int getIntInput(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            scanner.next();
            System.out.print("Invalid input. Please enter a number: ");
        }
        int value = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        return value;
    }

    /**
     * Get double input with validation
     */
    private double getDoubleInput(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextDouble()) {
            scanner.next();
            System.out.print("Invalid input. Please enter a number: ");
        }
        double value = scanner.nextDouble();
        scanner.nextLine(); // Consume newline
        return value;
    }

    /**
     * Close scanner resources
     */
    public void close() {
        if (scanner != null) {
            scanner.close();
        }
    }
}
