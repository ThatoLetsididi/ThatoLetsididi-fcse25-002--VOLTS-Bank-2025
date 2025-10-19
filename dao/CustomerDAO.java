package com.VOLTSBankApp.dao;

import com.VOLTSBankApp.model.Customer;
import java.sql.*;

public class CustomerDAO {

    /**
     * Authenticate customer login
     */
    public Customer authenticate(String email, String password) {
        String sql = "SELECT * FROM customers WHERE email = ? AND password = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return extractCustomerFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error authenticating customer");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Create new customer
     */
    public boolean createCustomer(Customer customer) {
        String sql = "INSERT INTO customers (first_name, surname, address, phone_number, email, password) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, customer.getFirstName());
            pstmt.setString(2, customer.getSurname());
            pstmt.setString(3, customer.getAddress());
            pstmt.setString(4, customer.getPhoneNumber());
            pstmt.setString(5, customer.getEmail());
            pstmt.setString(6, customer.getPassword());

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    customer.setCustomerId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error creating customer");
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Get customer by ID
     */
    public Customer getCustomerById(int customerId) {
        String sql = "SELECT * FROM customers WHERE customer_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, customerId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return extractCustomerFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error getting customer by ID");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Update customer information
     */
    public boolean updateCustomer(Customer customer) {
        String sql = "UPDATE customers SET first_name = ?, surname = ?, address = ?, " +
                "phone_number = ?, email = ? WHERE customer_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, customer.getFirstName());
            pstmt.setString(2, customer.getSurname());
            pstmt.setString(3, customer.getAddress());
            pstmt.setString(4, customer.getPhoneNumber());
            pstmt.setString(5, customer.getEmail());
            pstmt.setInt(6, customer.getCustomerId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating customer");
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Delete customer
     */
    public boolean deleteCustomer(int customerId) {
        String sql = "DELETE FROM customers WHERE customer_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, customerId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting customer");
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Get all customers
     */
    public java.util.List<Customer> getAllCustomers() {
        java.util.List<Customer> customers = new java.util.ArrayList<>();
        String sql = "SELECT * FROM customers ORDER BY customer_id";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                customers.add(extractCustomerFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting all customers");
            e.printStackTrace();
        }
        return customers;
    }

    /**
     * Load all accounts for a customer
     */
    public void loadCustomerAccounts(Customer customer) {
        AccountDAO accountDAO = new AccountDAO();
        accountDAO.loadAccountsForCustomer(customer);
    }

    /**
     * Extract customer object from ResultSet
     */
    private Customer extractCustomerFromResultSet(ResultSet rs) throws SQLException {
        Customer customer = new Customer();
        customer.setCustomerId(rs.getInt("customer_id"));
        customer.setFirstName(rs.getString("first_name"));
        customer.setSurname(rs.getString("surname"));
        customer.setAddress(rs.getString("address"));
        customer.setPhoneNumber(rs.getString("phone_number"));
        customer.setEmail(rs.getString("email"));
        customer.setPassword(rs.getString("password"));
        return customer;
    }
}
