package com.VOLTSBankApp.view;

import com.VOLTSBankApp.model.Customer;
import java.util.List;

/**
 * CustomerView - Handles display of customer-related information
 * This is the boundary class for customer operations
 */
public class CustomerView {

    /**
     * Display customer management menu
     */
    public void displayMenu() {
        System.out.println("\n╔════════════════════════════════════╗");
        System.out.println("║   CUSTOMER MANAGEMENT MENU         ║");
        System.out.println("╠════════════════════════════════════╣");
        System.out.println("║ 1. Register New Customer           ║");
        System.out.println("║ 2. View Customer Details           ║");
        System.out.println("║ 3. Update Customer Information     ║");
        System.out.println("║ 4. Delete Customer                 ║");
        System.out.println("║ 5. View All Customers              ║");
        System.out.println("║ 6. Back to Main Menu               ║");
        System.out.println("╚════════════════════════════════════╝");
    }

    /**
     * Display detailed customer information
     */
    public void displayCustomerDetails(Customer customer) {
        System.out.println("\n╔════════════════════════════════════════════════╗");
        System.out.println("║           CUSTOMER DETAILS                     ║");
        System.out.println("╠════════════════════════════════════════════════╣");
        System.out.printf("║ Customer ID    : %-29d ║%n", customer.getCustomerId());
        System.out.printf("║ Name           : %-29s ║%n", customer.getFullName());
        System.out.printf("║ First Name     : %-29s ║%n", customer.getFirstName());
        System.out.printf("║ Surname        : %-29s ║%n", customer.getSurname());
        System.out.printf("║ Email          : %-29s ║%n", customer.getEmail());
        System.out.printf("║ Phone          : %-29s ║%n",
                customer.getPhoneNumber() != null ? customer.getPhoneNumber() : "N/A");
        System.out.printf("║ Address        : %-29s ║%n",
                customer.getAddress() != null ? customer.getAddress() : "N/A");
        System.out.printf("║ No. of Accounts: %-29d ║%n", customer.getAccounts().size());
        System.out.println("╚════════════════════════════════════════════════╝");
    }

    /**
     * Display list of customers in table format
     */
    public void displayCustomerList(List<Customer> customers) {
        System.out.println("\n╔═════╦═══════════════════════════╦════════════════════════════════╦═══════════════╗");
        System.out.println("║ ID  ║         Name              ║            Email               ║   Accounts    ║");
        System.out.println("╠═════╬═══════════════════════════╬════════════════════════════════╬═══════════════╣");

        for (Customer customer : customers) {
            System.out.printf("║ %-3d ║ %-25s ║ %-30s ║      %-8d ║%n",
                    customer.getCustomerId(),
                    truncate(customer.getFullName(), 25),
                    truncate(customer.getEmail(), 30),
                    customer.getAccounts().size());
        }

        System.out.println("╚═════╩═══════════════════════════╩════════════════════════════════╩═══════════════╝");
        System.out.println("Total Customers: " + customers.size());
    }

    /**
     * Display success message
     */
    public void displaySuccessMessage(String message) {
        System.out.println("\n✓ SUCCESS: " + message);
    }

    /**
     * Display error message
     */
    public void displayErrorMessage(String message) {
        System.out.println("\n✗ ERROR: " + message);
    }

    /**
     * Display info message
     */
    public void displayInfoMessage(String message) {
        System.out.println("\nℹ INFO: " + message);
    }

    /**
     * Truncate string to specified length
     */
    private String truncate(String str, int length) {
        if (str == null) {
            return "";
        }
        if (str.length() <= length) {
            return str;
        }
        return str.substring(0, length - 3) + "...";
    }
}
