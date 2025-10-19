package com.VOLTSBankApp.view;

import com.VOLTSBankApp.model.Transaction;
import java.time.format.DateTimeFormatter;
import java.util.List;


public class TransactionView {
    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Display transaction menu
     */
    public void displayMenu() {
        System.out.println("\n╔════════════════════════════════════╗");
        System.out.println("║   TRANSACTION MANAGEMENT MENU      ║");
        System.out.println("╠════════════════════════════════════╣");
        System.out.println("║ 1. Deposit Funds                   ║");
        System.out.println("║ 2. Withdraw Funds                  ║");
        System.out.println("║ 3. View Transactions               ║");
        System.out.println("║ 4. Transaction Report              ║");
        System.out.println("║ 5. Back to Main Menu               ║");
        System.out.println("╚════════════════════════════════════╝");
    }

    /**
     * Display transaction details
     */
    public void displayTransactionDetails(Transaction transaction) {
        System.out.println("\n╔════════════════════════════════════════════════════╗");
        System.out.println("║           TRANSACTION DETAILS                      ║");
        System.out.println("╠════════════════════════════════════════════════════╣");
        System.out.printf("║ Transaction ID : %-33d ║%n", transaction.getTransactionId());
        System.out.printf("║ Account Number : %-33s ║%n", transaction.getAccountNumber());
        System.out.printf("║ Type           : %-33s ║%n", transaction.getTransactionType());
        System.out.printf("║ Amount         : BWP %-28s ║%n",
                String.format("%.2f", transaction.getAmount()));
        System.out.printf("║ Balance After  : BWP %-28s ║%n",
                String.format("%.2f", transaction.getBalanceAfter()));
        System.out.printf("║ Date           : %-33s ║%n",
                transaction.getTransactionDate().format(DATE_FORMATTER));
        System.out.printf("║ Description    : %-33s ║%n",
                truncate(transaction.getDescription(), 33));
        System.out.println("╚════════════════════════════════════════════════════╝");
    }

    /**
     * Display list of transactions in table format
     */
    public void displayTransactionList(List<Transaction> transactions) {
        System.out.println("\n╔═════╦══════════════╦═════════════╦══════════════╦═════════════════════╦══════════════════════╗");
        System.out.println("║ ID  ║     Type     ║   Amount    ║ Balance After║        Date         ║     Description      ║");
        System.out.println("╠═════╬══════════════╬═════════════╬══════════════╬═════════════════════╬══════════════════════╣");

        for (Transaction transaction : transactions) {
            System.out.printf("║ %-3d ║ %-12s ║ BWP %7.2f ║ BWP %8.2f ║ %-19s ║ %-20s ║%n",
                    transaction.getTransactionId(),
                    transaction.getTransactionType(),
                    transaction.getAmount(),
                    transaction.getBalanceAfter(),
                    transaction.getTransactionDate().format(DATE_FORMATTER),
                    truncate(transaction.getDescription() != null ?
                            transaction.getDescription() : "", 20));
        }

        System.out.println("╚═════╩══════════════╩═════════════╩══════════════╩═════════════════════╩══════════════════════╝");

        if (!transactions.isEmpty()) {
            System.out.println("Total Transactions: " + transactions.size());
        }
    }

    /**
     * Display transaction summary
     */
    public void displayTransactionSummary(List<Transaction> transactions) {
        if (transactions.isEmpty()) {
            System.out.println("\nNo transactions to summarize.");
            return;
        }

        int totalCount = transactions.size();
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

        System.out.println("\n╔════════════════════════════════════════════════════╗");
        System.out.println("║           TRANSACTION SUMMARY                      ║");
        System.out.println("╠════════════════════════════════════════════════════╣");
        System.out.printf("║ Total Transactions : %-29d ║%n", totalCount);
        System.out.println("╠════════════════════════════════════════════════════╣");
        System.out.printf("║ Deposits           : %-5d (BWP %16s) ║%n",
                depositCount, String.format("%.2f", totalDeposits));
        System.out.printf("║ Withdrawals        : %-5d (BWP %16s) ║%n",
                withdrawalCount, String.format("%.2f", totalWithdrawals));
        System.out.printf("║ Interest Payments  : %-5d (BWP %16s) ║%n",
                interestCount, String.format("%.2f", totalInterest));
        System.out.println("╠════════════════════════════════════════════════════╣");

        double netChange = totalDeposits + totalInterest - totalWithdrawals;
        System.out.printf("║ Net Change         : BWP %-26s ║%n",
                String.format("%.2f", netChange));
        System.out.println("╚════════════════════════════════════════════════════╝");
    }

    /**
     * Display deposit confirmation
     */
    public void displayDepositConfirmation(String accountNumber, double amount, double newBalance) {
        System.out.println("\n╔════════════════════════════════════════════════════╗");
        System.out.println("║           DEPOSIT CONFIRMATION                     ║");
        System.out.println("╠════════════════════════════════════════════════════╣");
        System.out.printf("║ Account Number : %-33s ║%n", accountNumber);
        System.out.printf("║ Amount         : BWP %-28s ║%n", String.format("%.2f", amount));
        System.out.printf("║ New Balance    : BWP %-28s ║%n", String.format("%.2f", newBalance));
        System.out.println("╚════════════════════════════════════════════════════╝");
    }

    /**
     * Display withdrawal confirmation
     */
    public void displayWithdrawalConfirmation(String accountNumber, double amount, double newBalance) {
        System.out.println("\n╔════════════════════════════════════════════════════╗");
        System.out.println("║           WITHDRAWAL CONFIRMATION                  ║");
        System.out.println("╠════════════════════════════════════════════════════╣");
        System.out.printf("║ Account Number : %-33s ║%n", accountNumber);
        System.out.printf("║ Amount         : BWP %-28s ║%n", String.format("%.2f", amount));
        System.out.printf("║ New Balance    : BWP %-28s ║%n", String.format("%.2f", newBalance));
        System.out.println("╚════════════════════════════════════════════════════╝");
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
     * Display warning message
     */
    public void displayWarningMessage(String message) {
        System.out.println("\n⚠ WARNING: " + message);
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
