package com.VOLTSBankApp.controller;

import com.VOLTSBankApp.dao.AccountDAO;
import com.VOLTSBankApp.dao.CustomerDAO;
import com.VOLTSBankApp.dao.TransactionDAO;
import com.VOLTSBankApp.model.*;
import com.VOLTSBankApp.view.AccountView;
import com.VOLTSBankApp.interfaces.InterestBearing;

import java.util.List;
import java.util.Scanner;

public class AccountController {
    private AccountDAO accountDAO;
    private CustomerDAO customerDAO;
    private TransactionDAO transactionDAO;
    private AccountView accountView;
    private Scanner scanner;

    public AccountController() {
        this.accountDAO = new AccountDAO();
        this.customerDAO = new CustomerDAO();
        this.transactionDAO = new TransactionDAO();
        this.accountView = new AccountView();
        this.scanner = new Scanner(System.in);
    }

    /**
     * Display account management menu
     */
    public void showAccountMenu() {
        boolean running = true;

        while (running) {
            accountView.displayMenu();
            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    handleCreateAccount();
                    break;
                case 2:
                    handleViewAccount();
                    break;
                case 3:
                    handleViewCustomerAccounts();
                    break;
                case 4:
                    handlePayInterest();
                    break;
                case 5:
                    handleViewAccountTransactions();
                    break;
                case 6:
                    handleDeleteAccount();
                    break;
                case 7:
                    running = false;
                    System.out.println("Returning to main menu...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    /**
     * Handle create new account
     */
    private void handleCreateAccount() {
        System.out.println("\n=== Create New Account ===");

        int customerId = getIntInput("Enter Customer ID: ");

        Customer customer = customerDAO.getCustomerById(customerId);
        if (customer == null) {
            System.out.println("ERROR: Customer not found!");
            return;
        }

        System.out.println("Customer: " + customer.getFullName());
        System.out.println("\nSelect Account Type:");
        System.out.println("1. Savings Account (0.05% monthly interest, no withdrawals)");
        System.out.println("2. Investment Account (5% monthly interest, min BWP 500.00)");
        System.out.println("3. Cheque Account (requires employment details)");

        int typeChoice = getIntInput("Enter choice: ");

        System.out.print("Branch: ");
        String branch = scanner.nextLine();

        Account account = null;
        String accountNumber = generateAccountNumber(customer.getCustomerId(), typeChoice);

        switch (typeChoice) {
            case 1:
                account = createSavingsAccount(accountNumber, branch, customer);
                break;
            case 2:
                account = createInvestmentAccount(accountNumber, branch, customer);
                break;
            case 3:
                account = createChequeAccount(accountNumber, branch, customer);
                break;
            default:
                System.out.println("Invalid account type!");
                return;
        }

        if (account != null && accountDAO.createAccount(account)) {
            System.out.println("\nSUCCESS: Account created successfully!");
            accountView.displayAccountDetails(account);
        } else {
            System.out.println("\nERROR: Failed to create account!");
        }
    }

    /**
     * Create Savings Account
     */
    private SavingsAccount createSavingsAccount(String accountNumber, String branch, Customer customer) {
        double initialDeposit = getDoubleInput("Initial deposit (optional, BWP): ");

        SavingsAccount account = new SavingsAccount(accountNumber, branch, customer);
        if (initialDeposit > 0) {
            account.deposit(initialDeposit);
        }

        return account;
    }

    /**
     * Create Investment Account
     */
    private InvestmentAccount createInvestmentAccount(String accountNumber, String branch, Customer customer) {
        System.out.println("Minimum opening balance: BWP " +
                String.format("%.2f", InvestmentAccount.getMinimumOpeningBalance()));

        double initialDeposit = getDoubleInput("Initial deposit (BWP): ");

        if (initialDeposit < InvestmentAccount.getMinimumOpeningBalance()) {
            System.out.println("ERROR: Initial deposit must be at least BWP " +
                    String.format("%.2f", InvestmentAccount.getMinimumOpeningBalance()));
            return null;
        }

        return new InvestmentAccount(accountNumber, branch, customer, initialDeposit);
    }

    /**
     * Create Cheque Account
     */
    private ChequeAccount createChequeAccount(String accountNumber, String branch, Customer customer) {
        System.out.print("Employer Name: ");
        String employerName = scanner.nextLine();

        System.out.print("Employer Address: ");
        String employerAddress = scanner.nextLine();

        if (employerName.isEmpty()) {
            System.out.println("ERROR: Employer name is required for Cheque Account!");
            return null;
        }

        ChequeAccount account = new ChequeAccount(accountNumber, branch, customer,
                employerName, employerAddress);

        double initialDeposit = getDoubleInput("Initial deposit (optional, BWP): ");
        if (initialDeposit > 0) {
            account.deposit(initialDeposit);
        }

        return account;
    }

    /**
     * Handle view account details
     */
    private void handleViewAccount() {
        System.out.println("\n=== View Account Details ===");

        System.out.print("Enter Account Number: ");
        String accountNumber = scanner.nextLine();

        Account account = accountDAO.getAccountByNumber(accountNumber);

        if (account != null) {
            accountView.displayAccountDetails(account);

            // Show recent transactions
            List<Transaction> transactions = transactionDAO.getRecentTransactions(accountNumber, 5);
            if (!transactions.isEmpty()) {
                System.out.println("\n--- Recent Transactions (Last 5) ---");
                accountView.displayTransactionList(transactions);
            }
        } else {
            System.out.println("ERROR: Account not found!");
        }
    }

    /**
     * Handle view all accounts for a customer
     */
    private void handleViewCustomerAccounts() {
        System.out.println("\n=== View Customer Accounts ===");

        int customerId = getIntInput("Enter Customer ID: ");

        Customer customer = customerDAO.getCustomerById(customerId);
        if (customer == null) {
            System.out.println("ERROR: Customer not found!");
            return;
        }

        customerDAO.loadCustomerAccounts(customer);

        System.out.println("\nCustomer: " + customer.getFullName());

        if (customer.getAccounts().isEmpty()) {
            System.out.println("No accounts found for this customer.");
        } else {
            accountView.displayAccountList(customer.getAccounts());

            // Calculate and display total balance
            double totalBalance = customer.getAccounts().stream()
                    .mapToDouble(Account::getBalance)
                    .sum();
            System.out.println("\nTotal Balance across all accounts: BWP " +
                    String.format("%.2f", totalBalance));
        }
    }

    /**
     * Handle pay interest to accounts
     */
    private void handlePayInterest() {
        System.out.println("\n=== Pay Interest ===");

        System.out.print("Enter Account Number: ");
        String accountNumber = scanner.nextLine();

        Account account = accountDAO.getAccountByNumber(accountNumber);

        if (account == null) {
            System.out.println("ERROR: Account not found!");
            return;
        }

        if (!(account instanceof InterestBearing)) {
            System.out.println("ERROR: This account type does not earn interest!");
            return;
        }

        InterestBearing interestAccount = (InterestBearing) account;
        double oldBalance = account.getBalance();
        double interestRate = interestAccount.getInterestRate();

        // Calculate and pay interest
        interestAccount.calculateInterest();
        double newBalance = account.getBalance();
        double interestPaid = newBalance - oldBalance;

        // Update in database
        double accruedInterest = 0;
        if (account instanceof SavingsAccount) {
            accruedInterest = ((SavingsAccount) account).getAccruedInterest();
        } else if (account instanceof InvestmentAccount) {
            accruedInterest = ((InvestmentAccount) account).getAccruedInterest();
        }

        if (accountDAO.updateAccountWithInterest(accountNumber, newBalance, accruedInterest)) {
            // Record transaction
            Transaction transaction = new Transaction(
                    accountNumber,
                    "INTEREST",
                    interestPaid,
                    newBalance,
                    String.format("Monthly interest payment - %.2f%%", interestRate * 100)
            );
            transactionDAO.createTransaction(transaction);

            System.out.println("\nSUCCESS: Interest paid successfully!");
            System.out.println("Interest Rate: " + String.format("%.3f%%", interestRate * 100));
            System.out.println("Interest Paid: BWP " + String.format("%.2f", interestPaid));
            System.out.println("Old Balance: BWP " + String.format("%.2f", oldBalance));
            System.out.println("New Balance: BWP " + String.format("%.2f", newBalance));
        } else {
            System.out.println("ERROR: Failed to update account with interest!");
        }
    }

    /**
     * Handle view account transactions
     */
    private void handleViewAccountTransactions() {
        System.out.println("\n=== View Account Transactions ===");

        System.out.print("Enter Account Number: ");
        String accountNumber = scanner.nextLine();

        Account account = accountDAO.getAccountByNumber(accountNumber);
        if (account == null) {
            System.out.println("ERROR: Account not found!");
            return;
        }

        System.out.println("\nAccount: " + accountNumber + " (" +
                account.getAccountType().getDisplayName() + ")");
        System.out.println("Current Balance: BWP " + String.format("%.2f", account.getBalance()));

        List<Transaction> transactions = transactionDAO.getTransactionsByAccount(accountNumber);

        if (transactions.isEmpty()) {
            System.out.println("\nNo transactions found.");
        } else {
            System.out.println("\n--- All Transactions ---");
            accountView.displayTransactionList(transactions);
        }
    }

    /**
     * Handle delete account
     */
    private void handleDeleteAccount() {
        System.out.println("\n=== Delete Account ===");

        System.out.print("Enter Account Number: ");
        String accountNumber = scanner.nextLine();

        Account account = accountDAO.getAccountByNumber(accountNumber);
        if (account == null) {
            System.out.println("ERROR: Account not found!");
            return;
        }

        accountView.displayAccountDetails(account);

        if (account.getBalance() > 0) {
            System.out.println("\nWARNING: This account has a balance of BWP " +
                    String.format("%.2f", account.getBalance()));
        }

        System.out.print("\nAre you sure you want to delete this account? (yes/no): ");
        String confirmation = scanner.nextLine();

        if (confirmation.equalsIgnoreCase("yes")) {
            if (accountDAO.deleteAccount(accountNumber)) {
                System.out.println("SUCCESS: Account deleted successfully!");
            } else {
                System.out.println("ERROR: Failed to delete account!");
            }
        } else {
            System.out.println("Delete operation cancelled.");
        }
    }

    /**
     * Generate account number
     * Format: [TYPE][CUSTOMER_ID][SEQUENCE]
     */
    private String generateAccountNumber(int customerId, int accountType) {
        String prefix;
        switch (accountType) {
            case 1:
                prefix = "SAV";
                break;
            case 2:
                prefix = "INV";
                break;
            case 3:
                prefix = "CHQ";
                break;
            default:
                prefix = "ACC";
        }

        long timestamp = System.currentTimeMillis() % 100000;
        return String.format("%s%03d%05d", prefix, customerId, timestamp);
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
