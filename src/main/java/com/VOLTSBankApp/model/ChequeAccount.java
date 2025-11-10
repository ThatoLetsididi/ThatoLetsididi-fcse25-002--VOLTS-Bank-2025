package com.VOLTSBankApp.model;

import com.VOLTSBankApp.interfaces.Withdrawable;

public class ChequeAccount extends Account implements Withdrawable {
    private String employerName;
    private String employerAddress;

    public ChequeAccount() {
        super();
        setAccountType(AccountType.CHEQUE);
    }

    public ChequeAccount(String accountNumber, String branch, Customer customer,
                         String employerName, String employerAddress) {
        super(accountNumber, branch, customer, AccountType.CHEQUE);
        this.employerName = employerName;
        this.employerAddress = employerAddress;
    }

    @Override
    public boolean withdraw(double amount) {
        if (amount <= 0 || amount > getBalance()) {
            return false;
        }
        setBalance(getBalance() - amount);
        return true;
    }

    @Override
    public double getAvailableBalance() {
        return getBalance();
    }

    public String getEmployerName() { return employerName; }
    public void setEmployerName(String employerName) { this.employerName = employerName; }

    public String getEmployerAddress() { return employerAddress; }
    public void setEmployerAddress(String employerAddress) { this.employerAddress = employerAddress; }

    @Override
    public String getAccountDetails() {
        return String.format("Cheque Account\nAccount Number: %s\nBalance: BWP %.2f\n" +
                        "Employer: %s\nEmployer Address: %s\nDeposits & Withdrawals: Allowed",
                getAccountNumber(), getBalance(), employerName, employerAddress);
    }
}