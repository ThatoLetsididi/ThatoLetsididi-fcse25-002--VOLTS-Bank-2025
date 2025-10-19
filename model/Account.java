package com.VOLTSBankApp.model;

import java.time.LocalDateTime;

public abstract class Account {
    private String accountNumber;
    private double balance;
    private String branch;
    private LocalDateTime dateOpened;
    private Customer customer;
    private AccountType accountType;

    public Account() {
        this.dateOpened = LocalDateTime.now();
        this.balance = 0.0;
    }

    public Account(String accountNumber, String branch, Customer customer, AccountType accountType) {
        this.accountNumber = accountNumber;
        this.branch = branch;
        this.customer = customer;
        this.accountType = accountType;
        this.dateOpened = LocalDateTime.now();
        this.balance = 0.0;
    }

    // Deposit method - common to all accounts
    public boolean deposit(double amount) {
        if (amount <= 0) {
            return false;
        }
        this.balance += amount;
        return true;
    }

    public abstract double getAvailableBalance();

    // Abstract method - must be implemented by subclasses
    public abstract String getAccountDetails();

    // Getters and Setters
    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }

    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }

    public String getBranch() { return branch; }
    public void setBranch(String branch) { this.branch = branch; }

    public LocalDateTime getDateOpened() { return dateOpened; }
    public void setDateOpened(LocalDateTime dateOpened) { this.dateOpened = dateOpened; }

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }

    public AccountType getAccountType() { return accountType; }
    public void setAccountType(AccountType accountType) { this.accountType = accountType; }

    @Override
    public String toString() {
        return "Account{" +
                "accountNumber='" + accountNumber + '\'' +
                ", balance=" + String.format("%.2f", balance) +
                ", type=" + accountType.getDisplayName() +
                '}';
    }
}