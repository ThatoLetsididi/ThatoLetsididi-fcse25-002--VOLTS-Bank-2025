package com.VOLTSBankApp.dao;

import com.VOLTSBankApp.interfaces.InterestBearing;
import com.VOLTSBankApp.model.*;
import com.VOLTSBankApp.util.DatabaseConnection;

import java.sql.*;

public class AccountDAO {

    /**
     * Create new account
     */
    public boolean createAccount(Account account) {
        String sql = "INSERT INTO accounts (account_number, customer_id, account_type, balance, " +
                "branch, accrued_interest, employer_name, employer_address) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, account.getAccountNumber());
            pstmt.setInt(2, account.getCustomer().getCustomerId());
            pstmt.setString(3, account.getAccountType().name());
            pstmt.setDouble(4, account.getBalance());
            pstmt.setString(5, account.getBranch());

            // Set accrued interest for interest-bearing accounts
            if (account instanceof InterestBearing) {
                if (account instanceof SavingsAccount) {
                    pstmt.setDouble(6, ((SavingsAccount) account).getAccruedInterest());
                } else if (account instanceof InvestmentAccount) {
                    pstmt.setDouble(6, ((InvestmentAccount) account).getAccruedInterest());
                }
            } else {
                pstmt.setDouble(6, 0.0);
            }

            // Set employer details for cheque accounts
            if (account instanceof ChequeAccount) {
                ChequeAccount chequeAccount = (ChequeAccount) account;
                pstmt.setString(7, chequeAccount.getEmployerName());
                pstmt.setString(8, chequeAccount.getEmployerAddress());
            } else {
                pstmt.setNull(7, Types.VARCHAR);
                pstmt.setNull(8, Types.VARCHAR);
            }

            int result = pstmt.executeUpdate();

            // Record opening transaction
            if (result > 0 && account.getBalance() > 0) {
                TransactionDAO transactionDAO = new TransactionDAO();
                Transaction transaction = new Transaction(
                        account.getAccountNumber(),
                        "OPENING",
                        account.getBalance(),
                        account.getBalance(),
                        "Account opening deposit"
                );
                transactionDAO.createTransaction(transaction);
            }

            return result > 0;
        } catch (SQLException e) {
            System.err.println("Error creating account");
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Get account by account number
     */
    public Account getAccountByNumber(String accountNumber) {
        String sql = "SELECT * FROM accounts WHERE account_number = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, accountNumber);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return extractAccountFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error getting account");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Load all accounts for a customer
     */
    public void loadAccountsForCustomer(Customer customer) {
        String sql = "SELECT * FROM accounts WHERE customer_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, customer.getCustomerId());
            ResultSet rs = pstmt.executeQuery();

            customer.getAccounts().clear();
            while (rs.next()) {
                Account account = extractAccountFromResultSet(rs);
                account.setCustomer(customer);
                customer.addAccount(account);
            }
        } catch (SQLException e) {
            System.err.println("Error loading customer accounts");
            e.printStackTrace();
        }
    }

    /**
     * Update account balance
     */
    public boolean updateAccountBalance(String accountNumber, double newBalance) {
        String sql = "UPDATE accounts SET balance = ? WHERE account_number = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDouble(1, newBalance);
            pstmt.setString(2, accountNumber);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating account balance");
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Update account with interest
     */
    public boolean updateAccountWithInterest(String accountNumber, double newBalance, double accruedInterest) {
        String sql = "UPDATE accounts SET balance = ?, accrued_interest = ? WHERE account_number = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDouble(1, newBalance);
            pstmt.setDouble(2, accruedInterest);
            pstmt.setString(3, accountNumber);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating account with interest");
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Delete account
     */
    public boolean deleteAccount(String accountNumber) {
        String sql = "DELETE FROM accounts WHERE account_number = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, accountNumber);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting account");
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Extract account object from ResultSet
     */
    private Account extractAccountFromResultSet(ResultSet rs) throws SQLException {
        String accountType = rs.getString("account_type");
        String accountNumber = rs.getString("account_number");
        String branch = rs.getString("branch");
        double balance = rs.getDouble("balance");
        double accruedInterest = rs.getDouble("accrued_interest");

        Account account = null;

        switch (AccountType.valueOf(accountType)) {
            case SAVINGS:
                SavingsAccount savingsAccount = new SavingsAccount();
                savingsAccount.setAccountNumber(accountNumber);
                savingsAccount.setBranch(branch);
                savingsAccount.setBalance(balance);
                account = savingsAccount;
                break;

            case INVESTMENT:
                InvestmentAccount investmentAccount = new InvestmentAccount();
                investmentAccount.setAccountNumber(accountNumber);
                investmentAccount.setBranch(branch);
                investmentAccount.setBalance(balance);
                account = investmentAccount;
                break;

            case CHEQUE:
                ChequeAccount chequeAccount = new ChequeAccount();
                chequeAccount.setAccountNumber(accountNumber);
                chequeAccount.setBranch(branch);
                chequeAccount.setBalance(balance);
                chequeAccount.setEmployerName(rs.getString("employer_name"));
                chequeAccount.setEmployerAddress(rs.getString("employer_address"));
                account = chequeAccount;
                break;
        }

        if (account != null) {
            Timestamp timestamp = rs.getTimestamp("date_opened");
            if (timestamp != null) {
                account.setDateOpened(timestamp.toLocalDateTime());
            }
        }

        return account;
    }
}
