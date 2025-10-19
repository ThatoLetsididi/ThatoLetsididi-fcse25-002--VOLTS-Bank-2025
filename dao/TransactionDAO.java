package com.VOLTSBankApp.dao;

import com.VOLTSBankApp.model.Transaction;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {
    /**
     * Create new transaction
     */
    public boolean createTransaction(Transaction transaction) {
        String sql = "INSERT INTO transactions (account_number, transaction_type, amount, " +
                "balance_after, description) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, transaction.getAccountNumber());
            pstmt.setString(2, transaction.getTransactionType());
            pstmt.setDouble(3, transaction.getAmount());
            pstmt.setDouble(4, transaction.getBalanceAfter());
            pstmt.setString(5, transaction.getDescription());

            int result = pstmt.executeUpdate();

            if (result > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    transaction.setTransactionId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error creating transaction");
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Get all transactions for an account
     */
    public List<Transaction> getTransactionsByAccount(String accountNumber) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions WHERE account_number = ? ORDER BY transaction_date DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, accountNumber);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                transactions.add(extractTransactionFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting transactions");
            e.printStackTrace();
        }
        return transactions;
    }

    /**
     * Get recent transactions (limit)
     */
    public List<Transaction> getRecentTransactions(String accountNumber, int limit) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions WHERE account_number = ? " +
                "ORDER BY transaction_date DESC LIMIT ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, accountNumber);
            pstmt.setInt(2, limit);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                transactions.add(extractTransactionFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting recent transactions");
            e.printStackTrace();
        }
        return transactions;
    }

    /**
     * Extract transaction from ResultSet
     */
    private Transaction extractTransactionFromResultSet(ResultSet rs) throws SQLException {
        Transaction transaction = new Transaction();
        transaction.setTransactionId(rs.getInt("transaction_id"));
        transaction.setAccountNumber(rs.getString("account_number"));
        transaction.setTransactionType(rs.getString("transaction_type"));
        transaction.setAmount(rs.getDouble("amount"));
        transaction.setBalanceAfter(rs.getDouble("balance_after"));
        transaction.setDescription(rs.getString("description"));

        Timestamp timestamp = rs.getTimestamp("transaction_date");
        if (timestamp != null) {
            transaction.setTransactionDate(timestamp.toLocalDateTime());
        }

        return transaction;
    }

    /**
     * Delete transaction
     */
    public boolean deleteTransaction(int transactionId) {
        String sql = "DELETE FROM transactions WHERE transaction_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, transactionId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting transaction");
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Get transaction by ID
     */
    public Transaction getTransactionById(int transactionId) {
        String sql = "SELECT * FROM transactions WHERE transaction_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, transactionId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return extractTransactionFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error getting transaction by ID");
            e.printStackTrace();
        }
        return null;
    }
}