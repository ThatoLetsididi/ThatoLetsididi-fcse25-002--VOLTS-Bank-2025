package com.VOLTSBankApp.controller;

import com.VOLTSBankApp.dao.CustomerDAO;
import com.VOLTSBankApp.model.Customer;
import com.VOLTSBankApp.view.CustomerView;

import java.util.List;
import java.util.Scanner;

/**
 * CustomerController - Handles user actions and coordinates between DAO and View
 * This is a console-based controller for customer management operations
 */
public class CustomerController {

    private CustomerDAO customerDAO;
    private CustomerView customerView;
    private Scanner scanner;

    public CustomerController() {
        this.customerDAO = new CustomerDAO();
        this.customerView = new CustomerView();
        this.scanner = new Scanner(System.in);
    }

    /**
     * Display customer management menu
     */
    public void showCustomerMenu() {
        boolean running = true;

        while (running) {
            customerView.displayMenu();
            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    handleRegisterCustomer();
                    break;
                case 2:
                    handleViewCustomer();
                    break;
                case 3:
                    handleUpdateCustomer();
                    break;
                case 4:
                    handleDeleteCustomer();
                    break;
                case 5:
                    handleViewAllCustomers();
                    break;
                case 6:
                    running = false;
                    System.out.println("Returning to main menu...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    /**
     * Handle customer registration
     */
    private void handleRegisterCustomer() {
        System.out.println("\n=== Register New Customer ===");

        System.out.print("First Name: ");
        String firstName = scanner.nextLine();

        System.out.print("Surname: ");
        String surname = scanner.nextLine();

        System.out.print("Address: ");
        String address = scanner.nextLine();

        System.out.print("Phone Number: ");
        String phoneNumber = scanner.nextLine();

        System.out.print("Email: ");
        String email = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        System.out.print("Confirm Password: ");
        String confirmPassword = scanner.nextLine();

        // Validate inputs
        if (firstName.isEmpty() || surname.isEmpty() || email.isEmpty() || password.isEmpty()) {
            System.out.println("ERROR: All fields are required!");
            return;
        }

        if (!password.equals(confirmPassword)) {
            System.out.println("ERROR: Passwords do not match!");
            return;
        }

        if (password.length() < 6) {
            System.out.println("ERROR: Password must be at least 6 characters long!");
            return;
        }

        // Create customer object
        Customer customer = new Customer(firstName, surname, address, phoneNumber, email, password);

        // Save to database
        if (customerDAO.createCustomer(customer)) {
            System.out.println("\nSUCCESS: Customer registered successfully!");
            System.out.println("Customer ID: " + customer.getCustomerId());
        } else {
            System.out.println("\nERROR: Failed to register customer. Email may already exist.");
        }
    }

    /**
     * Handle view customer by ID
     */
    private void handleViewCustomer() {
        System.out.println("\n=== View Customer Details ===");

        int customerId = getIntInput("Enter Customer ID: ");

        Customer customer = customerDAO.getCustomerById(customerId);

        if (customer != null) {
            customerView.displayCustomerDetails(customer);
        } else {
            System.out.println("ERROR: Customer not found!");
        }
    }

    /**
     * Handle update customer information
     */
    private void handleUpdateCustomer() {
        System.out.println("\n=== Update Customer Information ===");

        int customerId = getIntInput("Enter Customer ID: ");

        Customer customer = customerDAO.getCustomerById(customerId);

        if (customer == null) {
            System.out.println("ERROR: Customer not found!");
            return;
        }

        System.out.println("Current details:");
        customerView.displayCustomerDetails(customer);

        System.out.println("\nEnter new details (press Enter to keep current value):");

        System.out.print("First Name [" + customer.getFirstName() + "]: ");
        String firstName = scanner.nextLine();
        if (!firstName.isEmpty()) {
            customer.setFirstName(firstName);
        }

        System.out.print("Surname [" + customer.getSurname() + "]: ");
        String surname = scanner.nextLine();
        if (!surname.isEmpty()) {
            customer.setSurname(surname);
        }

        System.out.print("Address [" + customer.getAddress() + "]: ");
        String address = scanner.nextLine();
        if (!address.isEmpty()) {
            customer.setAddress(address);
        }

        System.out.print("Phone Number [" + customer.getPhoneNumber() + "]: ");
        String phoneNumber = scanner.nextLine();
        if (!phoneNumber.isEmpty()) {
            customer.setPhoneNumber(phoneNumber);
        }

        System.out.print("Email [" + customer.getEmail() + "]: ");
        String email = scanner.nextLine();
        if (!email.isEmpty()) {
            customer.setEmail(email);
        }

        // Update in database
        if (customerDAO.updateCustomer(customer)) {
            System.out.println("\nSUCCESS: Customer updated successfully!");
        } else {
            System.out.println("\nERROR: Failed to update customer!");
        }
    }

    /**
     * Handle delete customer
     */
    private void handleDeleteCustomer() {
        System.out.println("\n=== Delete Customer ===");

        int customerId = getIntInput("Enter Customer ID: ");

        Customer customer = customerDAO.getCustomerById(customerId);

        if (customer == null) {
            System.out.println("ERROR: Customer not found!");
            return;
        }

        customerView.displayCustomerDetails(customer);

        System.out.print("\nAre you sure you want to delete this customer? (yes/no): ");
        String confirmation = scanner.nextLine();

        if (confirmation.equalsIgnoreCase("yes")) {
            if (customerDAO.deleteCustomer(customerId)) {
                System.out.println("SUCCESS: Customer deleted successfully!");
            } else {
                System.out.println("ERROR: Failed to delete customer!");
            }
        } else {
            System.out.println("Delete operation cancelled.");
        }
    }

    /**
     * Handle view all customers
     */
    private void handleViewAllCustomers() {
        System.out.println("\n=== All Customers ===");

        List<Customer> customers = customerDAO.getAllCustomers();

        if (customers.isEmpty()) {
            System.out.println("No customers found.");
        } else {
            customerView.displayCustomerList(customers);
        }
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
     * Close scanner resources
     */
    public void close() {
        if (scanner != null) {
            scanner.close();
        }
    }
}