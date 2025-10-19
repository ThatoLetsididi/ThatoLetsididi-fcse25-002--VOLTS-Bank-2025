package com.VOLTSBankApp.view;

import com.VOLTSBankApp.model.Account;
import com.VOLTSBankApp.model.Transaction;
import com.VOLTSBankApp.model.SavingsAccount;
import com.VOLTSBankApp.model.InvestmentAccount;
import com.VOLTSBankApp.model.ChequeAccount;

import java.time.format.DateTimeFormatter;
import java.util.List;


public class AccountView {
    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Display account management menu
     */
    public void displayMenu() {
        System.out.println("\n╔════════════════════════════════════╗");
        System.out.println("║   ACCOUNT MANAGEMENT MENU          ║");
        System.out.println("╠════════════════════════════════════╣");
        System.out.println("║ 1. Create New Account              ║");
        System.out.println("║ 2. View Account Details            ║");
        System.out.println("║ 3. View Customer Accounts          ║");
        System.out.println("║ 4. Pay Interest                    ║");
        System.out.println("║ 5. View Account Transactions       ║");
        System.out.println("║ 6. Delete Account                  ║");
        System.out.println("║ 7. Back to Main Menu               ║");
        System.out.println("╚════════════════════════════════════╝");
    }

    /**
     * Display detailed account information
     */
    public void displayAccountDetails(Account account) {
        System.out.println("\n╔════════════════════════════════════════════════════╗");
        System.out.println("║           ACCOUNT DETAILS                          ║");
        System.out.println("╠════════════════════════════════════════════════════╣");
        System.out.printf("║ Account Number : %-33s ║%n", account.getAccountNumber());
        System.out.printf("║ Account Type   : %-33s ║%n", account.getAccountType().getDisplayName());
        System.out.printf("║ Branch         : %-33s ║%n", account.getBranch());
        System.out.printf("║ Balance        : BWP %-28s ║%n", String.format("%.2f", account.getBalance()));
        System.out.printf("║ Date Opened    : %-33s ║%n",
                account.getDateOpened() != null ? account.getDateOpened().format(DATE_FORMATTER) : "N/A");

        // Display account-specific information
        if (account instanceof SavingsAccount) {
            SavingsAccount savingsAccount = (SavingsAccount) account;
            System.out.printf("║ Interest Rate  : %-33s ║%n",
                    String.format("%.3f%% monthly", savingsAccount.getInterestRate() * 100));
            System.out.printf("║ Accrued Int.   : BWP %-28s ║%n",
                    String.format("%.2f", savingsAccount.getAccruedInterest()));
            System.out.printf("║ Withdrawals    : %-33s ║%n", "Not Allowed");
        } else if (account instanceof InvestmentAccount) {
            InvestmentAccount investmentAccount = (InvestmentAccount) account;
            System.out.printf("║ Interest Rate  : %-33s ║%n",
                    String.format("%.1f%% monthly", investmentAccount.getInterestRate() * 100));
            System.out.printf("║ Accrued Int.   : BWP %-28s ║%n",
                    String.format("%.2f", investmentAccount.getAccruedInterest()));
            System.out.printf("║ Min. Opening   : BWP %-28s ║%n",
                    String.format("%.2f", InvestmentAccount.getMinimumOpeningBalance()));
            System.out.printf("║ Withdrawals    : %-33s ║%n", "Allowed");
        } else if (account instanceof ChequeAccount) {
            ChequeAccount chequeAccount = (ChequeAccount) account;
            System.out.printf("║ Employer       : %-33s ║%n",
                    chequeAccount.getEmployerName() != null ? chequeAccount.getEmployerName() : "N/A");
            System.out.printf("║ Employer Addr. : %-33s ║%n",
                    truncate(chequeAccount.getEmployerAddress(), 33));
            System.out.printf("║ Withdrawals    : %-33s ║%n", "Allowed");
        }

        System.out.println("╚════════════════════════════════════════════════════╝");
    }

    /**
     * Display list of accounts in table format
     */
    public void displayAccountList(List<Account> accounts) {
        System.out.println("\n╔═══════════════╦══════════════════════╦════════════════════╦═══════════════╗");
        System.out.println("║ Account Number║    Account Type      ║      Branch        ║    Balance    ║");
        System.out.println("╠═══════════════╬══════════════════════╬════════════════════╬═══════════════╣");

        for (Account account : accounts) {
            System.out.printf("║ %-13s ║ %-20s ║ %-18s ║ BWP %9.2f ║%n",
                    account.getAccountNumber(),
                    account.getAccountType().getDisplayName(),
                    truncate(account.getBranch(), 18),
                    account.getBalance());
        }

        System.out.println("╚═══════════════╩══════════════════════╩════════════════════╩═══════════════╝");
        System.out.println("Total Accounts: " + accounts.size());
    }

    /**
     * Display list of transactions
     */
    public void displayTransactionList(List<Transaction> transactions) {
        System.out.println("\n╔═════╦══════════════╦═════════════╦══════════════╦═════════════════════╗");
        System.out.println("║ ID  ║     Type     ║   Amount    ║ Balance After║      Date           ║");
        System.out.println("╠═════╬══════════════╬═════════════╬══════════════╬═════════════════════╣");

        for (Transaction transaction : transactions) {
            System.out.printf("║ %-3d ║ %-12s ║ BWP %7.2f ║ BWP %8.2f ║ %-19s ║%n",
                    transaction.getTransactionId(),
                    transaction.getTransactionType(),
                    transaction.getAmount(),
                    transaction.getBalanceAfter(),
                    transaction.getTransactionDate().format(DATE_FORMATTER));
        }

        System.out.println("╚═════╩══════════════╩═════════════╩══════════════╩═════════════════════╝");

        if (!transactions.isEmpty()) {
            System.out.println("Total Transactions: " + transactions.size());
        }
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
