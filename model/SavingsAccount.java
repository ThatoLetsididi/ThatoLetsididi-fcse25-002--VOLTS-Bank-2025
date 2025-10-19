package com.VOLTSBankApp.model;

import com.VOLTSBankApp.interfaces.InterestBearing;

public class SavingsAccount extends Account implements InterestBearing {
    private static final double INTEREST_RATE = 0.0005; // 0.05% monthly
    private double accruedInterest;

    public SavingsAccount() {
        super();
        setAccountType(AccountType.SAVINGS);
    }

    @Override
    public double getAvailableBalance() {
        return 0;
    }

    public SavingsAccount(String accountNumber, String branch, Customer customer) {
        super(accountNumber, branch, customer, AccountType.SAVINGS);
        this.accruedInterest = 0.0;
    }

    @Override
    public void calculateInterest() {
        double interest = getBalance() * INTEREST_RATE;
        this.accruedInterest += interest;
        setBalance(getBalance() + interest);
    }

    @Override
    public double getInterestRate() {
        return INTEREST_RATE;
    }

    public double getAccruedInterest() {
        return accruedInterest;
    }

    @Override
    public String getAccountDetails() {
        return String.format("Savings Account\nAccount Number: %s\nBalance: BWP %.2f\n" +
                        "Interest Rate: %.3f%%\nAccrued Interest: BWP %.2f\nNote: No withdrawals allowed",
                getAccountNumber(), getBalance(), getInterestRate() * 100, accruedInterest);
    }
}
