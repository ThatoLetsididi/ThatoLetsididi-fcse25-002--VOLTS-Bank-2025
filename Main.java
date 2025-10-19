package com.VOLTSBankApp;

import com.VOLTSBankApp.controller.CustomerController;
import com.VOLTSBankApp.controller.AccountController;
import com.VOLTSBankApp.controller.TransactionController;
import com.VOLTSBankApp.dao.DatabaseConnection;

import java.util.Scanner;

/**
 * Main - Entry point for VOLTS Bank Console Application
 * This is the main application that provides menu-driven interface
 */
public class Main {

    private static Scanner scanner = new Scanner(System.in);
    private static CustomerController customerController;
    private static AccountController accountController;
    private static TransactionController transactionController;

    public static void main(String[] args) {
        // Display welcome banner
        displayWelcomeBanner();

        // Test database connection
        if (!testDatabaseConnection()) {
            System.out.println("\n❌ ERROR: Cannot connect to database!");
            System.out.println("Please ensure:");
            System.out.println("1. XAMPP is running");
            System.out.println("2. MySQL service is started");
            System.out.println("3. Database 'volts_bank' exists");
            System.out.println("\nExiting application...");
            return;
        }

        // Initialize controllers
        initializeControllers();

        // Run main menu
        runMainMenu();

        // Cleanup
        cleanup();

        System.out.println("\nThank you for using VOLTS Bank!");
        System.out.println("Goodbye! 👋");
    }

    private static void cleanup() {
    }

    /**
     * Display welcome banner
     */
    private static void displayWelcomeBanner() {
        System.out.println("\n╔══════════════════════════════════════════════════════╗");
        System.out.println("║                                                      ║");
        System.out.println("║              ⚡ VOLTS BANK SYSTEM ⚡                   ║");
        System.out.println("║                                                      ║");
        System.out.println("║           Banking Made Simple & Secure               ║");
        System.out.println("║                                                      ║");
        System.out.println("║              Version 1.0 - 2025                      ║");
        System.out.println("║                                                      ║");
        System.out.println("╚══════════════════════════════════════════════════════╝");
    }

    /**
     * Test database connection
     */
    private static boolean testDatabaseConnection() {
        System.out.println("\n🔄 Testing database connection...");

        if (DatabaseConnection.testConnection()) {
            System.out.println("✅ Database connected successfully!");
            return true;
        } else {
            return false;
        }
    }

    /**
     * Initialize all controllers
     */
    private static void initializeControllers() {
        System.out.println("🔄 Initializing application...");
        customerController = new CustomerController();
        accountController = new AccountController();
        transactionController = new TransactionController();
        System.out.println("✅ Application ready!");
    }

    /**
     * Run main menu loop
     */
    private static void runMainMenu() {
        boolean running = true;

        while (running) {
            displayMainMenu();

            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    customerController.showCustomerMenu();
                    break;
                case 2:
                    accountController.showAccountMenu();
                    break;
                case 3:
                    transactionController.showTransactionMenu();
                    break;
                case 4:
                    displaySystemInfo();
                    break;
                case 5:
                    displayAbout();
                    break;
                case 6:
                    running = confirmExit();
                    break;
                default:
                    System.out.println("\n❌ Invalid choice! Please enter a number between 1 and 6.");
            }
        }
    }

    private static int getIntInput(String s) {
        return 0;
    }

    private static boolean confirmExit() {
        return false;
    }

    /**
     * Display main menu
     */
    private static void displayMainMenu() {
        System.out.println("\n╔══════════════════════════════════════════════════════╗");
        System.out.println("║                    MAIN MENU                         ║");
        System.out.println("╠══════════════════════════════════════════════════════╣");
        System.out.println("║                                                      ║");
        System.out.println("║  1. 👥 Customer Management                          ║");
        System.out.println("║     • Register, View, Update, Delete Customers       ║");
        System.out.println("║                                                      ║");
        System.out.println("║  2. 💼 Account Management                           ║");
        System.out.println("║     • Create Accounts, Pay Interest, View Details    ║");
        System.out.println("║                                                      ║");
        System.out.println("║  3. 💰 Transaction Management                       ║");
        System.out.println("║     • Deposits, Withdrawals, Transaction History     ║");
        System.out.println("║                                                      ║");
        System.out.println("║  4. ℹ️  System Information                          ║");
        System.out.println("║                                                      ║");
        System.out.println("║  5. 📖 About VOLTS Bank                             ║");
        System.out.println("║                                                      ║");
        System.out.println("║  6. 🚪 Exit Application                             ║");
        System.out.println("║                                                      ║");
        System.out.println("╚══════════════════════════════════════════════════════╝");
    }

    /**
     * Display system information
     */
    private static void displaySystemInfo() {
        System.out.println("\n╔══════════════════════════════════════════════════════╗");
        System.out.println("║              SYSTEM INFORMATION                      ║");
        System.out.println("╠══════════════════════════════════════════════════════╣");
        System.out.println("║ Application    : VOLTS Bank Management System        ║");
        System.out.println("║ Version        : 1.0.0                               ║");
        System.out.println("║ Database       : MySQL (XAMPP)                       ║");
        System.out.println("║ Database Name  : volts_bank                          ║");
        System.out.println("║ Java Version   : " + System.getProperty("java.version") + "                                   ║");
        System.out.println("║ OS             : " + System.getProperty("os.name") + "                      ║");
        System.out.println("║                                                      ║");
        System.out.println("║ Account Types:                                       ║");
        System.out.println("║ • Savings Account (0.05% monthly interest)           ║");
        System.out.println("║ • Investment Account (5% monthly interest)           ║");
        System.out.println("║ • Cheque Account (for salaried customers)            ║");
        System.out.println("╚══════════════════════════════════════════════════════╝");

        System.out.print("\nPress Enter to continue...");
        scanner.nextLine();
    }

    /**
     * Display about information
     */
    private static void displayAbout() {
        System.out.println("\n╔══════════════════════════════════════════════════════╗");
        System.out.println("║                 ABOUT VOLTS BANK                     ║");
        System.out.println("╠══════════════════════════════════════════════════════╣");
        System.out.println("║                                                      ║");
        System.out.println("║  VOLTS Bank - Your Trusted Banking Partner           ║");
        System.out.println("║                                                      ║");
        System.out.println("║  This system demonstrates:                           ║");
        System.out.println("║  ✓ Object-Oriented Programming principles            ║");
        System.out.println("║  ✓ Inheritance and Polymorphism                      ║");
        System.out.println("║  ✓ Interface Implementation                          ║");
        System.out.println("║  ✓ Database Connectivity (JDBC)                      ║");
        System.out.println("║  ✓ MVC Architecture Pattern                          ║");
        System.out.println("║  ✓ CRUD Operations                                   ║");
        System.out.println("║                                                      ║");
        System.out.println("║  Developed for: CSE202 - OOAD with Java              ║");
        System.out.println("║  By : Thato Letsididi                                ║");
        System.out.println("║  Year: 2025                                          ║");
        System.out.println("║                                                      ║");
        System.out.println("╚══════════════════════════════════════════════════════╝");

        System.out.print("\nPress Enter to continue...");
        scanner.nextLine();
    }
}
